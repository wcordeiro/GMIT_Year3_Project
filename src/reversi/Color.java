package reversi;

public enum Color {
	
	// TODO
	BLACK ("B", 'B'), 
	WHITE ("W", 'W'),
	BLANK ("~", '.');
	
	private String stringValue;
	private char representation;
	
	Color(String valorString, char representacao) {
		this.stringValue = valorString;
		this.representation = representacao;
	}
	
	public char getRepresentation() {
		return this.representation;
	}
	
	@Override
	public String toString() {
		return this.stringValue;
	};
	
	public static Color getOpositeColor(Color cor) {
		if(cor == BLACK) return WHITE;
		else if(cor == WHITE) return BLACK;
		else return BLANK;
	}
}
