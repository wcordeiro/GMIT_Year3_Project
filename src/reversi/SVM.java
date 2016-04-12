package reversi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SVM {
	private HashMap<String,Integer> dictionary;
	Map<Integer,Double> ascsortedMAP = new TreeMap<Integer,Double>();
	
	public SVM(){
	 // Thing to come;
		File arq = new File("testLearning.txt");
		if(!arq.exists()){
			Parser parse = new Parser();
			parse.generateFile();
			this.generateModel();
		}
		dictionary = new HashMap<String,Integer>();
		FileReader fr = null;
		try {
			fr = new FileReader("dictionary.txt");
			BufferedReader br = new BufferedReader( fr );
			while( br.ready()){
				String linha = br.readLine();
				String vector[] = linha.split(" ");
				dictionary.put(vector[1],Integer.decode(vector[0]));
			}
			br.close();
			fr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void generateModel(){
		CMDExecutor executor = new CMDExecutor();
		try {
			executor.execCommand("svm_learn testLearning.txt SVM/model.dat");
			//Thread.currentThread().sleep(500);
		//	executor.execCommand("svm_learn testLearning.txt SVM/model2.dat");
		//	Thread.currentThread().sleep(500);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void generatePrediction(){
		CMDExecutor executor = new CMDExecutor();
		try {
			executor.execCommand("svm_classify PossibleMoves.dat SVM/model.dat SVM/predictions.dat");
	//		Thread.currentThread().sleep(500);
		//	executor.execCommand("svm_classify PossibleMoves.dat SVM/model2.dat SVM/predictions2.dat");
	//		Thread.currentThread().sleep(500);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void archivePreparation(Jogada possiblePlay, Map<Posicao,Double> movementsMade){
		for(Map.Entry<Posicao, Double> mapData : movementsMade.entrySet()) {
			int letter = (mapData.getKey().x+97);
		    String newLine = Character.toString ((char)(letter)) + "" + (mapData.getKey().y+1);
		    if(dictionary.containsKey(newLine)){
		    	Integer key = dictionary.get(newLine);
		    	ascsortedMAP.put(key,mapData.getValue());
		    }
		    
		}
		int letter = possiblePlay.posicaoVazia.x + 97;
		String newLine = Character.toString ((char)(letter)) + "" + (possiblePlay.posicaoVazia.y+1);
		if(dictionary.containsKey(newLine)){
	    	Integer key = dictionary.get(newLine);
	    	ascsortedMAP.put(key,possiblePlay.evalScore);
	    }
	}
	
	public Jogada evaluatePositions(Color color, AI IA,Map<Posicao,Double> movementsMade){
		List<Jogada> possibleMoves = IA.board.calculaJogadasPossiveisJogador(color);
		FileWriter fw = null;
		Nodo nodo = new Nodo(IA.board,color);
		BufferedWriter bw = null;
		try {
			fw = new FileWriter("PossibleMoves.dat");
			bw = new BufferedWriter( fw );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(possibleMoves == null){
			return null;
		}
		for(Jogada move : possibleMoves){
			Nodo filho = new Nodo(nodo, move);
			move.evalScore = IA.calculateLevelHardFunction(filho.estado, color);
			try {
				bw.write("1 ");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			this.archivePreparation(move, movementsMade);
			for(Map.Entry<Integer,Double> mapData : ascsortedMAP.entrySet()) {
				try {
					//System.out.println(dictionary.get(mapData.getKey()) + ":" + mapData.getValue() + " ");
					if(mapData.getValue() == null)
						bw.write(mapData.getKey() + ":0 ");
					else{
						if(mapData.getValue() < 0){
							Double aux = Math.log(Math.abs(mapData.getValue()));
							bw.write(mapData.getKey() + ":" + (aux*-1) + " ");
						}
						else if(mapData.getValue() == 0)
							bw.write(mapData.getKey() + ":0 ");
						else
							bw.write(mapData.getKey() + ":" + Math.log(mapData.getValue()) + " ");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			ascsortedMAP.clear();
			try {
				bw.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			bw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.generatePrediction();
		Integer index = this.selectBestPrediction(possibleMoves.size());
		return possibleMoves.get(index);
	}

	private Integer selectBestPrediction(int size) {
		Double biggest = Double.NEGATIVE_INFINITY;
		Integer index = 0;
		Integer biggestIndex = 0;
		FileReader fr = null;
		try {
			fr = new FileReader("SVM/predictions.dat");
			BufferedReader br = new BufferedReader( fr );
			while( br.ready()){
				String linha = br.readLine();
				Double number = Double.valueOf(linha);
				if(number > biggest){
					biggest = number;
					biggestIndex = index;
				}
				index++;
			}
			br.close();
			fr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return biggestIndex;
	}
}
