package reversi;

public class Position {
	
	public int y;	
	public int x;
	
	public Position(int y, int x) {
		this.x = x;
		this.y = y;
	}

	public void setPosicao(int y, int x) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return("" +this.y +"x" +this.x);
	}
	
	@Override
	public boolean equals(Object posicao) {
		
		Position pos = (Position) posicao;
		
		return (pos.y == this.y) && (pos.x == this.x); 
	}
}
