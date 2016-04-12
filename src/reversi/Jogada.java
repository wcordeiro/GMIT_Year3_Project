package reversi;

import java.util.List;

public class Jogada {
	
	public Posicao posicaoVazia;
	public List<Movimento> movimentos;
	public Integer totalPecasViradas;
	public Double evalScore;
	
	public Jogada(Posicao lugarVazio, List<Movimento> movimentos) {
		this.posicaoVazia = lugarVazio;
		this.movimentos = movimentos;
		
		// Calcula o total de peças viradas.
		this.totalPecasViradas = 0;
		for(Movimento movimento : movimentos)
			this.totalPecasViradas += movimento.pecasViradas;
	}

    public Posicao getPosicaoVazia() {
        return posicaoVazia;
    }

    public void setPosicaoVazia(Posicao posicaoVazia) {
        this.posicaoVazia = posicaoVazia;
    }
	
	@Override
	public String toString() {
		
		String str = new String();
		
		str += posicaoVazia +" (";
		for(Movimento mov : movimentos)
			str += mov.direcao +"->" +mov.pecaJogador +", ";
		
		str = str.substring(0,  str.length()-2);
		
		str += ")";
				
		return str;
	}
}
