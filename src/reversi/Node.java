package reversi;

import java.util.ArrayList;
import java.util.List;

public class Node {
	
	public Node predecessor;
	public Board board;
	public Movement movement;
	public Integer depth;
	public Color playerColor;
	public boolean isLeaf = false;
	// Função de avaliação [PRETO|BRANCO]
	public Double [] fnAvaliacao;
	// TODO
	public List<Node> children;
	
	public Node(Node predecessor, Movement movement) {
		
		this.predecessor = predecessor;
		
		// Copia tabuleiro e executa jogada.
		this.board = this.predecessor.board.clone();
		this.board.executeMovement(movement);
		
		this.movement = movement;
		this.depth = this.predecessor.depth+1 ;
	
		this.predecessor.children.add(this);
		
		this.playerColor = Color.getOpositeColor(this.predecessor.playerColor);
		
		// TODO
		this.children = new ArrayList<Node>();
	}
	
	public Node(Board board, Color playerColor) {
		
		this.board = board;
		this.depth = 0;
		this.playerColor = playerColor;
		
		// TODO
		this.children = new ArrayList<Node>();
	}
	
	public boolean isRaiz() {
		return this.predecessor == null;
	}
	
	public Double fnAvaliacao(Color cor) {
		if(cor == Color.BLACK)
			return fnAvaliacao[0];
		else if(cor == Color.WHITE)
			return fnAvaliacao[1];
		else
			return -1.0;
	}
}
