package reversi;

public class Posicao {
	
	public int y;	
	public int x;
	
	public Posicao(int y, int x) {
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
		
		Posicao pos = (Posicao) posicao;
		
		return (pos.y == this.y) && (pos.x == this.x); 
	}
}
