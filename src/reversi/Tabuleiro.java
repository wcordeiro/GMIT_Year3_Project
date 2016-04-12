package reversi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Tabuleiro {
	
	public static final Integer TAMANHO = 8;
	
	private Cor [][] tabuleiro;

	public Tabuleiro() {
		this.tabuleiro = new Cor[TAMANHO][TAMANHO];
		
		// Inicializa tabuleiro
		for(int y=0 ; y<TAMANHO ; y++)
			for(int x=0 ; x<TAMANHO ; x++)
				this.tabuleiro[y][x] = Cor.VAZIO;
		
		// Inicializa peças.
		setPosicao(new Posicao(TAMANHO/2-1, TAMANHO/2-1), Cor.BRANCO);
		setPosicao(new Posicao(TAMANHO/2, TAMANHO/2-1), Cor.PRETO);
		setPosicao(new Posicao(TAMANHO/2-1, TAMANHO/2), Cor.PRETO);
		setPosicao(new Posicao(TAMANHO/2, TAMANHO/2), Cor.BRANCO);
	}
	
	public void setPosicao(Posicao posicao, Cor cor) {
		this.tabuleiro[posicao.y][posicao.x] = cor; 
	}

	public Cor getCor(Posicao posicao) {
		return this.tabuleiro[posicao.y][posicao.x];
	}
	
	public Jogada calculaJogada(Posicao lugarVazio, Cor corJogador) {
		
		Jogada jogada = null;
		
		List<Movimento> movimentos = 
				calculaMovimentosPossiveis(lugarVazio, corJogador);
		
		if(movimentos != null)
			return new Jogada(lugarVazio, movimentos);
			
		return jogada;
	}
	
	public List<Movimento> calculaMovimentosPossiveis(
			Posicao lugarVazio, 
			Cor corJogador) 
	{
		
		List<Movimento> movimentos = new ArrayList<Movimento>();
			
		for(Direcao direcao : Direcao.getAll()) {
			
			Movimento movimento = procuraMovimentoDirecao(lugarVazio, corJogador, direcao);
			if(movimento != null)
				movimentos.add(movimento);
		}
		
		if(movimentos.isEmpty())
			movimentos = null;
		
		return movimentos;
	}

	public List<Jogada> calculaJogadasPossiveisJogador(Cor corJogador) {

		List<Jogada> jogadas = new ArrayList<Jogada>();
		
		// Percorre as posições vazias em busca de jogadas para o jogador.

		for(Posicao lugarVazio : getPosicoesVazias()) {
			List<Movimento> movimentos = 
				calculaMovimentosPossiveis(lugarVazio, corJogador);
			
			// Se tiver movimentos, adiciona na lista de jogadas.
			if(movimentos != null)
				jogadas.add(new Jogada(lugarVazio, movimentos));
		}
		
		if(jogadas.isEmpty())
			jogadas = null;
		
		return jogadas;
	}
	
	public List<Posicao> getPosicoesVazias() {
		
		List<Posicao> vazias = new ArrayList<Posicao>();
		
		for(int y=0 ; y<TAMANHO ; y++)
			for(int x=0 ; x<TAMANHO ; x++)
				if(this.tabuleiro[y][x] == Cor.VAZIO)
					vazias.add(new Posicao(y, x));
		
		return vazias;
	}
	
	public Movimento procuraMovimentoDirecao(
			Posicao lugarVazio, 
			Cor corJogador,
			Direcao direcao) 
	{
		
		Cor corOposta = Cor.getCorOposta(corJogador);
		int y = lugarVazio.y;
		int x = lugarVazio.x;
		boolean temPecasParaReverter = false;
		int numeroPecasViradas = 0;
		Cor corPosicao = getCor(lugarVazio);

		if(corPosicao != Cor.VAZIO)
			return null;
		
		while(corPosicao != corJogador) {
			
			// Avança na direção
			if(direcao == Direcao.DIREITA)
				x++;
			else if(direcao == Direcao.ESQUERDA)
				x--;
			else if(direcao == Direcao.CIMA)
				y--;
			else if(direcao == Direcao.BAIXO)
				y++;
			else if(direcao == Direcao.BAIXO_DIREITA) {
				y++;
				x++;
			}
			else if(direcao == Direcao.BAIXO_ESQUERDA) {
				y++;
				x--;
			}
			else if(direcao == Direcao.CIMA_DIREITA) {
				y--;
				x++;
			}
			else if(direcao == Direcao.CIMA_ESQUERDA) {
				y--;
				x--;
			}

			// Ultrapassou barreiras do tabuleiro.
			if(x >= TAMANHO || x < 0 || y >= TAMANHO || y < 0)
				return null;
			
			corPosicao = getCor(new Posicao(y, x));
						
			// Não pode sair de vazio e ir p/ vazio.
			if(corPosicao == Cor.VAZIO)
				return null;
			else if(corPosicao == corOposta)
				temPecasParaReverter = true;
			
			numeroPecasViradas++;
		}
		
		// Cria movimento.
		if(temPecasParaReverter) {
			return new Movimento(
					lugarVazio, 
					new Posicao(y, x), 
					direcao,
					numeroPecasViradas-1);
		}
		
		return null;
	}

	public void executaMovimento(Movimento movimento) {
		
		Posicao lugarVazio = movimento.espacoVazio;
		Direcao direcao = movimento.direcao;
		Cor corJogador = getCor(movimento.pecaJogador);

		// Coloca a nova peça.
		setPosicao(lugarVazio, corJogador);

		int y = lugarVazio.y;
		int x = lugarVazio.x;
		
		// Troca todas as peças que devem ser viradas.		
		for(int cont=1 ; cont <= movimento.pecasViradas; cont++) {
			
			// Avança na direção
			if(direcao == Direcao.DIREITA)
				x++;
			else if(direcao == Direcao.ESQUERDA)
				x--;
			else if(direcao == Direcao.CIMA)
				y--;
			else if(direcao == Direcao.BAIXO)
				y++;
			else if(direcao == Direcao.BAIXO_DIREITA) {
				y++;
				x++;
			}
			else if(direcao == Direcao.BAIXO_ESQUERDA) {
				y++;
				x--;
			}
			else if(direcao == Direcao.CIMA_DIREITA) {
				y--;
				x++;
			}
			else if(direcao == Direcao.CIMA_ESQUERDA) {
				y--;
				x--;
			}
			
			setPosicao(new Posicao(y, x), corJogador);
		}
	}

	public void executaJogada(Jogada jogada) {
		
		for(Movimento movimento : jogada.movimentos)
			executaMovimento(movimento);
	}
	
	public String getConfiguracao() {
		
		String configuracao = new String();
		
		for(int y=0 ; y<TAMANHO ; y++)
			for(int x=0 ; x<TAMANHO ; x++)
				configuracao += this.tabuleiro[y][x];
		
		return configuracao;		
	}

	public TreeMap<Cor, Integer> calculaNumeroPecas() {
		
		TreeMap<Cor, Integer> valores = new TreeMap<Cor, Integer>();
		
		Integer numeroPretas = 0;
		Integer numeroBrancas = 0;
		
		for(int y=0 ; y<TAMANHO ; y++) {
			for(int x=0 ; x<TAMANHO ; x++) {
				Cor cor = this.tabuleiro[y][x];
				
				if(cor == Cor.PRETO)
					numeroPretas++;
				else if(cor == Cor.BRANCO)
					numeroBrancas++;
			}
		}
		
		valores.put(Cor.PRETO, numeroPretas);
		valores.put(Cor.BRANCO, numeroBrancas);
		
		return valores;
	}
	
	public int calculaTotalPecas() {
		
		int numPecas = 0;
		
		for(int y=0 ; y<TAMANHO ; y++)
			for(int x=0 ; x<TAMANHO ; x++)
				if(this.tabuleiro[y][x] != Cor.VAZIO)
					numPecas++;
		
		return numPecas;
	}
	
	public static Tabuleiro leTabuleiroArquivo(String caminho) 
			throws IOException {
			
		Tabuleiro tabuleiro = new Tabuleiro();
		BufferedReader buffer = new BufferedReader(new FileReader(caminho));
		
		for(int y=0 ; y<TAMANHO ; y++) {
			for(int x=0 ; x<TAMANHO ; x++) {
				char c = (char) buffer.read();

				Cor cor;
				
				if(c == Cor.PRETO.getRepresentacao())
					cor = Cor.PRETO;
				else if(c == Cor.BRANCO.getRepresentacao())
					cor = Cor.BRANCO;
				else
					cor = Cor.VAZIO;
						
				tabuleiro.tabuleiro[y][x] = cor;
			}
			// Quebra de linha.
			buffer.read();
		}
	
		return tabuleiro;
	}
	
	public int calculaNumeroQuinas(Cor cor) {
		
		int cont = 0;

		// Quinas
		if(tabuleiro[0][0] == cor) cont++;
		if(tabuleiro[0][TAMANHO-1] == cor) cont++;
		if(tabuleiro[TAMANHO-1][0] == cor) cont++;
		if(tabuleiro[TAMANHO-1][TAMANHO-1] == cor) cont++;
		
		return cont;
	}
	
	@Override
	public Tabuleiro clone() {
		Tabuleiro novo = new Tabuleiro();
		
		for(int y=0 ; y<TAMANHO ; y++)
			for(int x=0 ; x<TAMANHO ; x++)
				novo.tabuleiro[y][x] = this.tabuleiro[y][x];
		
		return novo;
	}
	
	public void imprime() {

		System.out.print(" |");
		for(int y=0 ; y<TAMANHO ; y++)
			System.out.print(y +"|");
		System.out.println();
			
		for(int y=0 ; y<TAMANHO ; y++) {
			System.out.print(y +"|");
			
			for(int x=0 ; x<TAMANHO ; x++)
				System.out.print(this.tabuleiro[y][x] +"|");
			
			System.out.println();
		}
	}

	@Override
	public String toString() {
		return getConfiguracao();
	}
}

