package reversi;

import java.util.ArrayList;
import java.util.List;

public class Nodo {
	
	public Nodo antecessor;
	public Tabuleiro estado;
	public Jogada jogada;
	public Integer profundidade;
	public Cor corJogador;
	public boolean isFolha = false;
	// Função de avaliação [PRETO|BRANCO]
	public Double [] fnAvaliacao;
	// TODO
	public List<Nodo> filhos;
	
	public Nodo(Nodo antecessor, Jogada jogada) {
		
		this.antecessor = antecessor;
		
		// Copia tabuleiro e executa jogada.
		this.estado = this.antecessor.estado.clone();
		this.estado.executaJogada(jogada);
		
		this.jogada = jogada;
		this.profundidade = this.antecessor.profundidade+1 ;
	
		this.antecessor.filhos.add(this);
		
		this.corJogador = Cor.getCorOposta(this.antecessor.corJogador);
		
		// TODO
		this.filhos = new ArrayList<Nodo>();
	}
	
	public Nodo(Tabuleiro tabuleiro, Cor corJogador) {
		
		this.estado = tabuleiro;
		this.profundidade = 0;
		this.corJogador = corJogador;
		
		// TODO
		this.filhos = new ArrayList<Nodo>();
	}
	
	public boolean isRaiz() {
		return this.antecessor == null;
	}
	
	public Double fnAvaliacao(Cor cor) {
		if(cor == Cor.PRETO)
			return fnAvaliacao[0];
		else if(cor == Cor.BRANCO)
			return fnAvaliacao[1];
		else
			return -1.0;
	}
}
