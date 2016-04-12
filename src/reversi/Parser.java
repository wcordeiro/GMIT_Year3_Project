package reversi;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Parser {
	
	public Parser(){
		//this.generateFile();
	}
	
	public void generateFile() {
		Integer i = 0;
		FileReader frI = null;
  	  	BufferedReader brI = null;
  	  	Integer win = 0;
  	  	Integer defeat = 0;
  	  	try {
			frI = new FileReader("FileNumber.txt");
			brI = new BufferedReader (frI);
	  	  	defeat = Integer.decode(brI.readLine());
	  	  	win = Integer.decode(brI.readLine());
	  	  	frI.close();
	  	  	brI.close();
		} catch (FileNotFoundException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  	  	
		FileWriter fw = null;
		Boolean flag = false;
		Integer index = 0;
		BufferedWriter bw = null;
		HashMap<String, Integer> myDictionary = new HashMap<String, Integer>();
		Map<Integer,String> ascsortedMAP = new TreeMap<Integer,String>();
		try {
			fw = new FileWriter("testLearning.txt");
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		bw = new BufferedWriter( fw );
		for(i = 1; i <= win; i++){
			FileReader fr = null;
			System.out.println(i);
			try {
				fr = new FileReader( "test/win"+i+".txt" );
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			BufferedReader br = new BufferedReader( fr );
			try {
				bw.write(1 + " ");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				while( br.ready() && !flag ){
					String linha = br.readLine();
					Integer number  = 0;
					String vector[] = linha.split(" ");
					if(vector[1].equalsIgnoreCase("--")){
						break;
					}
					else if(vector[2].equalsIgnoreCase("Win") || vector[2].equalsIgnoreCase("Loss") || vector[2].equalsIgnoreCase("Draw")){
						flag = true;
						break;
					}
					if(vector[2].isEmpty()){
						vector[2] = "0";
					}
					if(myDictionary.containsKey(vector[1])){
						number = myDictionary.get(vector[1]);
					}
					else{
						index++;
						number = index;
						myDictionary.put(vector[1], index);
					}
					ascsortedMAP.put(number, vector[2]);
					//bw.write(number.toString() + ":" + vector[2] + " ");
					
				}
				for(Map.Entry<Integer, String> mapData : ascsortedMAP.entrySet()) {
				  //  System.out.println("Key : " +mapData.getKey()+ "Value : "+mapData.getKey();
					bw.write(mapData.getKey()+ ":" + mapData.getValue() + " ");
				}
				ascsortedMAP.clear();
				br.close();
				flag = false;
				fr.close();
				bw.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		for(i = 1; i <= defeat; i++){
			FileReader fr = null;
			System.out.println(i);
			try {
				fr = new FileReader( "test/defeat"+i+".txt" );
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			BufferedReader br = new BufferedReader( fr );
			try {
				bw.write(-1 + " ");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				while( br.ready() && !flag ){
					String linha = br.readLine();
					Integer number  = 0;
					String vector[] = linha.split(" ");
					if(vector[1].equalsIgnoreCase("--")){
						break;
					}
					else if(vector[2].equalsIgnoreCase("Win") || vector[2].equalsIgnoreCase("Loss") || vector[2].equalsIgnoreCase("Draw")){
						flag = true;
						break;
					}
					if(vector[2].isEmpty()){
						vector[2] = "0";
					}
					if(myDictionary.containsKey(vector[1])){
						number = myDictionary.get(vector[1]);
					}
					else{
						index++;
						number = index;
						myDictionary.put(vector[1], index);
					}
					ascsortedMAP.put(number, vector[2]);
					
				}
				for(Map.Entry<Integer, String> mapData : ascsortedMAP.entrySet()) {
					  //  System.out.println("Key : " +mapData.getKey()+ "Value : "+mapData.getKey();
					    bw.write(mapData.getKey()+ ":" + mapData.getValue() + " ");
					}
					ascsortedMAP.clear();
				br.close();
				flag = false;
				fr.close();
				bw.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			bw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			fw = new FileWriter("dictionary.txt");
			bw = new BufferedWriter( fw );
			for(Object objname:myDictionary.keySet()) {
				bw.write(myDictionary.get(objname)+ " ");
				bw.write(objname.toString());
				bw.newLine();   
			}
			bw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
