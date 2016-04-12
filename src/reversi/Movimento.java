package reversi;

public class Movimento {
	
	public Posicao espacoVazio;
	public Posicao pecaJogador;
	// Da vazia até a peça do jogador.
	public Direction direcao;
	public Integer pecasViradas;

	public Movimento(
			Posicao espacoVazio, 
			Posicao pecaJogador,
			Direction direcao,
			Integer pecasViradas) {
		
		this.espacoVazio = espacoVazio;
		this.pecaJogador = pecaJogador;
		this.direcao = direcao;
		this.pecasViradas = pecasViradas;
	}
	
	@Override
	public String toString() {
//		return espacoVazio +" -> " +direcao +" -> " +pecaJogador +" # " +pecasViradas;
		return direcao +" . " +pecaJogador +" #" +pecasViradas;
	}
}
