package reversi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Tabuleiro {
	
	public static final Integer TAMANHO = 8;
	
	private Color [][] tabuleiro;

	public Tabuleiro() {
		this.tabuleiro = new Color[TAMANHO][TAMANHO];
		
		// Inicializa tabuleiro
		for(int y=0 ; y<TAMANHO ; y++)
			for(int x=0 ; x<TAMANHO ; x++)
				this.tabuleiro[y][x] = Color.BLANK;
		
		// Inicializa peças.
		setPosicao(new Posicao(TAMANHO/2-1, TAMANHO/2-1), Color.WHITE);
		setPosicao(new Posicao(TAMANHO/2, TAMANHO/2-1), Color.BLACK);
		setPosicao(new Posicao(TAMANHO/2-1, TAMANHO/2), Color.BLACK);
		setPosicao(new Posicao(TAMANHO/2, TAMANHO/2), Color.WHITE);
	}
	
	public void setPosicao(Posicao posicao, Color cor) {
		this.tabuleiro[posicao.y][posicao.x] = cor; 
	}

	public Color getCor(Posicao posicao) {
		return this.tabuleiro[posicao.y][posicao.x];
	}
	
	public Jogada calculaJogada(Posicao lugarVazio, Color corJogador) {
		
		Jogada jogada = null;
		
		List<Movimento> movimentos = 
				calculaMovimentosPossiveis(lugarVazio, corJogador);
		
		if(movimentos != null)
			return new Jogada(lugarVazio, movimentos);
			
		return jogada;
	}
	
	public List<Movimento> calculaMovimentosPossiveis(
			Posicao lugarVazio, 
			Color corJogador) 
	{
		
		List<Movimento> movimentos = new ArrayList<Movimento>();
			
		for(Direction direcao : Direction.getAll()) {
			
			Movimento movimento = procuraMovimentoDirecao(lugarVazio, corJogador, direcao);
			if(movimento != null)
				movimentos.add(movimento);
		}
		
		if(movimentos.isEmpty())
			movimentos = null;
		
		return movimentos;
	}

	public List<Jogada> calculaJogadasPossiveisJogador(Color corJogador) {

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
				if(this.tabuleiro[y][x] == Color.BLANK)
					vazias.add(new Posicao(y, x));
		
		return vazias;
	}
	
	public Movimento procuraMovimentoDirecao(
			Posicao lugarVazio, 
			Color corJogador,
			Direction direcao) 
	{
		
		Color corOposta = Color.getOpositeColor(corJogador);
		int y = lugarVazio.y;
		int x = lugarVazio.x;
		boolean temPecasParaReverter = false;
		int numeroPecasViradas = 0;
		Color corPosicao = getCor(lugarVazio);

		if(corPosicao != Color.BLANK)
			return null;
		
		while(corPosicao != corJogador) {
			
			// Avança na direção
			if(direcao == Direction.RIGTH)
				x++;
			else if(direcao == Direction.LEFT)
				x--;
			else if(direcao == Direction.UP)
				y--;
			else if(direcao == Direction.DOWN)
				y++;
			else if(direcao == Direction.DOWN_RIGTH) {
				y++;
				x++;
			}
			else if(direcao == Direction.DOWN_LEFT) {
				y++;
				x--;
			}
			else if(direcao == Direction.UP_RIGTH) {
				y--;
				x++;
			}
			else if(direcao == Direction.UP_LEFT) {
				y--;
				x--;
			}

			// Ultrapassou barreiras do tabuleiro.
			if(x >= TAMANHO || x < 0 || y >= TAMANHO || y < 0)
				return null;
			
			corPosicao = getCor(new Posicao(y, x));
						
			// Não pode sair de vazio e ir p/ vazio.
			if(corPosicao == Color.BLANK)
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
		Direction direcao = movimento.direcao;
		Color corJogador = getCor(movimento.pecaJogador);

		// Coloca a nova peça.
		setPosicao(lugarVazio, corJogador);

		int y = lugarVazio.y;
		int x = lugarVazio.x;
		
		// Troca todas as peças que devem ser viradas.		
		for(int cont=1 ; cont <= movimento.pecasViradas; cont++) {
			
			// Avança na direção
			if(direcao == Direction.RIGTH)
				x++;
			else if(direcao == Direction.LEFT)
				x--;
			else if(direcao == Direction.UP)
				y--;
			else if(direcao == Direction.DOWN)
				y++;
			else if(direcao == Direction.DOWN_RIGTH) {
				y++;
				x++;
			}
			else if(direcao == Direction.DOWN_LEFT) {
				y++;
				x--;
			}
			else if(direcao == Direction.UP_RIGTH) {
				y--;
				x++;
			}
			else if(direcao == Direction.UP_LEFT) {
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

	public TreeMap<Color, Integer> calculaNumeroPecas() {
		
		TreeMap<Color, Integer> valores = new TreeMap<Color, Integer>();
		
		Integer numeroPretas = 0;
		Integer numeroBrancas = 0;
		
		for(int y=0 ; y<TAMANHO ; y++) {
			for(int x=0 ; x<TAMANHO ; x++) {
				Color cor = this.tabuleiro[y][x];
				
				if(cor == Color.BLACK)
					numeroPretas++;
				else if(cor == Color.WHITE)
					numeroBrancas++;
			}
		}
		
		valores.put(Color.BLACK, numeroPretas);
		valores.put(Color.WHITE, numeroBrancas);
		
		return valores;
	}
	
	public int calculaTotalPecas() {
		
		int numPecas = 0;
		
		for(int y=0 ; y<TAMANHO ; y++)
			for(int x=0 ; x<TAMANHO ; x++)
				if(this.tabuleiro[y][x] != Color.BLANK)
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

				Color cor;
				
				if(c == Color.BLACK.getRepresentation())
					cor = Color.BLACK;
				else if(c == Color.WHITE.getRepresentation())
					cor = Color.WHITE;
				else
					cor = Color.BLANK;
						
				tabuleiro.tabuleiro[y][x] = cor;
			}
			// Quebra de linha.
			buffer.read();
		}
	
		return tabuleiro;
	}
	
	public int calculaNumeroQuinas(Color cor) {
		
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

