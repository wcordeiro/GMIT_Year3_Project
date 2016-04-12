package reversi;

public enum Cor {
	
	// TODO
	PRETO ("B", 'B'), 
	BRANCO ("W", 'W'),
	VAZIO ("~", '.');
	
	private String valorString;
	private char representacao;
	
	Cor(String valorString, char representacao) {
		this.valorString = valorString;
		this.representacao = representacao;
	}
	
	public char getRepresentacao() {
		return this.representacao;
	}
	
	@Override
	public String toString() {
		return this.valorString;
	};
	
	public static Cor getCorOposta(Cor cor) {
		if(cor == PRETO) return BRANCO;
		else if(cor == BRANCO) return PRETO;
		else return VAZIO;
	}
}
