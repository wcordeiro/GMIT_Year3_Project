package reversi;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ReinforcementLearner {
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
	
	public ReinforcementLearner(AI artificialInteligence,Color color){
		this.a = Math.random();
		this.b = Math.random();
		this.c = Math.random();
		this.r = 0.9;
		this.learningRate = 0.2;
		this.ownColor = color;
		if(color == Color.BLACK)
			this.otherColor = Color.WHITE;
		else
			this.otherColor = Color.BLACK;
		this.artificialInteligence = artificialInteligence;
		this.newA = 0.0;
		this.newB = 0.0;
		this.newC = 0.0;
	}
	
	public Double findInputA(Node node){
		List<Movement> possibleMoves = node.board.calcultePossibleMovesByColor(ownColor);
		double score = artificialInteligence.calculateLevelHardFunction(node.board, ownColor);
		double max = 0;
		Node nodo = new Node(node.board,ownColor);
		if(possibleMoves == null){
			return max;
		}
		for(Movement move : possibleMoves){
			Node filho = new Node(nodo, move);
			score = artificialInteligence.calculateLevelHardFunction(filho.board, ownColor) - score;
			if(max < score){
				max = score;
			}
		}
		return max;
	}
	
	public Double findInputB(Node node){
		List<Movement> possibleMoves = node.board.calcultePossibleMovesByColor(otherColor);
		double score = artificialInteligence.calculateLevelHardFunction(node.board, otherColor);
		double max = 0;
		Node nodo = new Node(node.board,otherColor);
		if(possibleMoves == null){
			return max;
		}
		for(Movement move : possibleMoves){
			Node filho = new Node(nodo, move);
			score = artificialInteligence.calculateLevelHardFunction(filho.board, otherColor) - score;
			if(max < score){
				max = score;
			}
		}
		return max;
	}
	
	public Double findInputC(Node node){
		List<Movement> possibleMovesW = node.board.calcultePossibleMovesByColor(ownColor);
		List<Movement> possibleMovesB = node.board.calcultePossibleMovesByColor(otherColor);
		
		if(possibleMovesB.size() == 0){
			return 100.0;	
		}
		
		return (possibleMovesW.size() / possibleMovesB.size())/1.0;
	}
	
	public Double findV(Node node){
		newA = this.findInputA(node);
		newB = this.findInputB(node);
		
		v = this.a * newA + this.b * newB + this.c * newC;
		
		return v;
	}
	
	public Movement findMove(){
		Map<Double,Movement> ascsortedMAP = new TreeMap<Double,Movement>();
		List<Movement> possibleMoves = artificialInteligence.board.calcultePossibleMovesByColor(ownColor);
		Double maxV = Double.MIN_VALUE;
		Double mia = 0.0;
		Double mib = 0.0;
		Double mic = 0.0;
		Node nodo = new Node(artificialInteligence.board,ownColor);
		
		if(possibleMoves.size() == 0){
			return null;
		}
		
		for(Movement move : possibleMoves){
			Node filho = new Node(nodo, move);
			v = this.findV(filho);
			
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
		return ascsortedMAP.get(maxV);
	}
	
	public void learn (Node node, Double V, Double A,Double B,Double C){
		//Node node = new Node(artificialInteligence.board,Color.WHITE);
		Double V2 = this.findV(node);
		V2 = this.r * V2;
		
		this.a = this.a + this.learningRate *  (V2 - V) * A;
		this.b = this.b + this.learningRate *  (V2 - V) * B;
		this.c = this.c + this.learningRate *  (V2 - V) * C;
	}
	
	
}
