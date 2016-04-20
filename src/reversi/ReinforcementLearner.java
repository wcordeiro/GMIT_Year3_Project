package reversi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ReinforcementLearner implements Player{
	private Double a; 
	private Double b;
	private Double c;
	private Double r;
	private Double learningRate;
	private AI artificialInteligence;
	private Color ownColor;
	private Color otherColor;
	private Double newA;
	private Double newB;
	private Double newC;
	private Double v;
	
	public ReinforcementLearner(Color color) throws IOException{
		File arq = new File("ReinforcementMiniMax.txt");
		if(!arq.exists()){
			this.a = Math.random();
			this.b = Math.random();
			this.c = Math.random();
			this.v = 0.0;
		}
		else{
			BufferedReader br = new BufferedReader(new FileReader("ReinforcementMiniMax.txt"));
			String aux;
			aux = br.readLine();
			this.a = Double.valueOf(aux);
			aux = br.readLine();
			this.b = Double.valueOf(aux);
			aux = br.readLine();
			this.c = Double.valueOf(aux);
			//aux = br.readLine();
			//this.v = Double.valueOf(aux);
			br.close();
		}
		this.v = 0.0;
		this.r = 0.9;
		this.learningRate = 0.2;
		this.otherColor = color;
		if(color == Color.BLACK)
			this.ownColor = Color.WHITE;
		else
			this.ownColor = Color.BLACK;
		this.newA = 0.0;
		this.newB = 0.0;
		this.newC = 0.0;
	}
	
	public Double findInputA(Node node){
		List<Movement> possibleMoves = node.board.calcultePossibleMovesByColor(ownColor);
		double score = artificialInteligence.calculateLevelEasyFunction(node.board, ownColor);
		double max = 0;
		Node nodo = new Node(node.board,ownColor);
		if(possibleMoves == null){
			return (64-score)/64;
		}
		for(Movement move : possibleMoves){
			Node filho = new Node(nodo, move);
			score = artificialInteligence.calculateLevelEasyFunction(filho.board, ownColor) - score;
			if(max < score){
				max = score;
			}
		}
		return (64-max)/64;
	}
	
	public Double findInputB(Node node){
		List<Movement> possibleMoves = node.board.calcultePossibleMovesByColor(otherColor);
		double score = artificialInteligence.calculateLevelEasyFunction(node.board, otherColor);
		double max = 0;
		Node nodo = new Node(node.board,otherColor);
		if(possibleMoves == null){
			return (64-score)/64;
		}
		for(Movement move : possibleMoves){
			Node filho = new Node(nodo, move);
			score = artificialInteligence.calculateLevelEasyFunction(filho.board, otherColor) - score;
			if(max < score){
				max = score;
			}
		}
		return (64-max)/64;
	}
	
	public Double findInputC(Node node){
		List<Movement> possibleMovesW = node.board.calcultePossibleMovesByColor(ownColor);
		List<Movement> possibleMovesB = node.board.calcultePossibleMovesByColor(otherColor);
		
		if(possibleMovesB == null){
			return 100.0;	
		}
		if(possibleMovesW == null){
			return 1.0;
		}
		
		return (possibleMovesW.size() / possibleMovesB.size())/1.0;
	}
	
	public Double findV(Node node){
		newA = this.findInputA(node);
		newB = this.findInputB(node);
		TreeMap<Color, List<Movement>> possibleMoves
        = new TreeMap<Color, List<Movement>>();

		possibleMoves.put(
        Color.BLACK,
        artificialInteligence.board.calcultePossibleMovesByColor(Color.BLACK)
		);

		possibleMoves.put(
        Color.WHITE,
        artificialInteligence.board.calcultePossibleMovesByColor(Color.WHITE)
		);

		if (!(possibleMoves.get(Color.BLACK) == null && possibleMoves.get(Color.WHITE) == null)) {
			newC = this.findInputC(node);
		}
		else{
			newC = 1.0;
			this.c = Math.random();
		}
		v = this.a * newA + this.b * newB + this.c * newC;
		
		return v;
	}
	
	public Movement findMove(){
		Map<Double,Movement> ascsortedMAP = new TreeMap<Double,Movement>();
		List<Movement> possibleMoves = artificialInteligence.board.calcultePossibleMovesByColor(ownColor);
		Double maxV = Double.NEGATIVE_INFINITY;
		Double mia = 0.0;
		Double mib = 0.0;
		Double mic = 0.0;
		Node node = new Node(artificialInteligence.board,ownColor);
		
		if(possibleMoves == null){
			return null;
		}
		
		for(Movement move : possibleMoves){
			Node child = new Node(node, move);
			v = this.findV(child);
			
			v = this.r * v;
			
			if(maxV < v){
				maxV = v;
			}
			
			if(mia < newA){
				mia = newA;
			}
			
			if(mib < newB){
				mib = newB;
			}
			
			if(mic < newC){
				mic = newC;
			}
			
			ascsortedMAP.put(v, move);
		}
		v = maxV;
		return ascsortedMAP.get(maxV);
	}
	
	public void learn (Node node, Double V, Double A,Double B,Double C) throws IOException{
		//Node node = new Node(artificialInteligence.board,Color.WHITE);
		Double V2 = this.findV(node);
		V2 = this.r * V2;
		
		TreeMap<Color, List<Movement>> possibleMoves
        = new TreeMap<Color, List<Movement>>();

		possibleMoves.put(
        Color.BLACK,
        artificialInteligence.board.calcultePossibleMovesByColor(Color.BLACK)
		);

		possibleMoves.put(
        Color.WHITE,
        artificialInteligence.board.calcultePossibleMovesByColor(Color.WHITE)
		);

		if (possibleMoves.get(Color.BLACK) == null && possibleMoves.get(Color.WHITE) == null) {
			double score1 = artificialInteligence.calculateLevelEasyFunction(node.board, ownColor);
			double score2 = artificialInteligence.calculateLevelEasyFunction(node.board, otherColor);
			if(score1 > score2 ){
				V2 += 100;
			}
			else{
				V2 -= 100;
			}
		}
		
		this.a = this.a + this.learningRate *  (V2 - V) * A;
		this.b = this.b + this.learningRate *  (V2 - V) * B;
		this.c = this.c + this.learningRate *  (V2 - V) * C;
		
		BufferedWriter bw = null;
		
		FileWriter fw = new FileWriter("ReinforcementMiniMax.txt");
		
		bw = new BufferedWriter (fw);
		
		bw.write(this.a.toString());
		bw.newLine();
		bw.write(this.b.toString());
		bw.newLine();
		bw.write(this.c.toString());
		//bw.newLine();
		//bw.write(v.toString());
		
		bw.close();
		fw.close();
	}

	public Double getA() {
		return a;
	}

	public void setA(Double a) {
		this.a = a;
	}

	public Double getB() {
		return b;
	}

	public void setB(Double b) {
		this.b = b;
	}

	public Double getC() {
		return c;
	}

	public void setC(Double c) {
		this.c = c;
	}

	public Double getR() {
		return r;
	}

	public void setR(Double r) {
		this.r = r;
	}

	public Double getLearningRate() {
		return learningRate;
	}

	public void setLearningRate(Double learningRate) {
		this.learningRate = learningRate;
	}

	public AI getArtificialInteligence() {
		return artificialInteligence;
	}

	public void setArtificialInteligence(AI artificialInteligence) {
		this.artificialInteligence = artificialInteligence;
	}

	public Color getOwnColor() {
		return ownColor;
	}

	public void setOwnColor(Color ownColor) {
		this.ownColor = ownColor;
	}

	public Color getOtherColor() {
		return otherColor;
	}

	public void setOtherColor(Color otherColor) {
		this.otherColor = otherColor;
	}

	public Double getNewA() {
		return newA;
	}

	public void setNewA(Double newA) {
		this.newA = newA;
	}

	public Double getNewB() {
		return newB;
	}

	public void setNewB(Double newB) {
		this.newB = newB;
	}

	public Double getNewC() {
		return newC;
	}

	public void setNewC(Double newC) {
		this.newC = newC;
	}

	public Double getV() {
		return v;
	}

	public void setV(Double v) {
		this.v = v;
	}

	@Override
	public Movement selectMovement(Board board, Color color) {
		return this.findMove();
	}

	@Override
	public void setSVMParameters(AI artificialInteligence, Map<Position, Double> movementsMade) {
		this.artificialInteligence = artificialInteligence;
		
	}
	
	
}
