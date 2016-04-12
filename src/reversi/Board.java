package reversi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Board {
	
	public static final Integer SIZE = 8;
	
	private Color [][] board;

	public Board() {
		this.board = new Color[SIZE][SIZE];
		
		// Inicializa tabuleiro
		for(int y=0 ; y<SIZE ; y++)
			for(int x=0 ; x<SIZE ; x++)
				this.board[y][x] = Color.BLANK;
		
		// Inicializa peças.
		setPosition(new Position(SIZE/2-1, SIZE/2-1), Color.WHITE);
		setPosition(new Position(SIZE/2, SIZE/2-1), Color.BLACK);
		setPosition(new Position(SIZE/2-1, SIZE/2), Color.BLACK);
		setPosition(new Position(SIZE/2, SIZE/2), Color.WHITE);
	}
	
	public void setPosition(Position position, Color color) {
		this.board[position.y][position.x] = color; 
	}

	public Color getCor(Position position) {
		return this.board[position.y][position.x];
	}
	
	public Movement calcuteMove(Position emptySpot, Color playerColor) {
		
		Movement movement = null;
		
		List<PartialMovement> moves = 
				calculatePossibleMoves(emptySpot, playerColor);
		
		if(moves != null)
			return new Movement(emptySpot, moves);
			
		return movement;
	}
	
	public List<PartialMovement> calculatePossibleMoves(
			Position emptySpot, 
			Color playerColor) 
	{
		
		List<PartialMovement> moves = new ArrayList<PartialMovement>();
			
		for(Direction direction : Direction.getAll()) {
			
			PartialMovement movement = searchMoveDirection(emptySpot, playerColor, direction);
			if(movement != null)
				moves.add(movement);
		}
		
		if(moves.isEmpty())
			moves = null;
		
		return moves;
	}

	public List<Movement> calcultePossibleMovesByColor(Color playerColor) {

		List<Movement> movements = new ArrayList<Movement>();
		
		// Percorre as posições vazias em busca de jogadas para o jogador.

		for(Position emptySpot : getEmptySpots()) {
			List<PartialMovement> moves = 
				calculatePossibleMoves(emptySpot, playerColor);
			
			// Se tiver movimentos, adiciona na lista de jogadas.
			if(moves != null)
				movements.add(new Movement(emptySpot, moves));
		}
		
		if(movements.isEmpty())
			movements = null;
		
		return movements;
	}
	
	public List<Position> getEmptySpots() {
		
		List<Position> emptyPositions = new ArrayList<Position>();
		
		for(int y=0 ; y<SIZE ; y++)
			for(int x=0 ; x<SIZE ; x++)
				if(this.board[y][x] == Color.BLANK)
					emptyPositions.add(new Position(y, x));
		
		return emptyPositions;
	}
	
	public PartialMovement searchMoveDirection(
			Position emptySpot, 
			Color playerColor,
			Direction direction) 
	{
		
		Color opositeColor = Color.getOpositeColor(playerColor);
		int y = emptySpot.y;
		int x = emptySpot.x;
		boolean positionToTurnFlag = false;
		int numberTurnedTiles = 0;
		Color positionColor = getCor(emptySpot);

		if(positionColor != Color.BLANK)
			return null;
		
		while(positionColor != playerColor) {
			
			// Avança na direção
			if(direction == Direction.RIGTH)
				x++;
			else if(direction == Direction.LEFT)
				x--;
			else if(direction == Direction.UP)
				y--;
			else if(direction == Direction.DOWN)
				y++;
			else if(direction == Direction.DOWN_RIGTH) {
				y++;
				x++;
			}
			else if(direction == Direction.DOWN_LEFT) {
				y++;
				x--;
			}
			else if(direction == Direction.UP_RIGTH) {
				y--;
				x++;
			}
			else if(direction == Direction.UP_LEFT) {
				y--;
				x--;
			}

			// Ultrapassou barreiras do tabuleiro.
			if(x >= SIZE || x < 0 || y >= SIZE || y < 0)
				return null;
			
			positionColor = getCor(new Position(y, x));
						
			// Não pode sair de vazio e ir p/ vazio.
			if(positionColor == Color.BLANK)
				return null;
			else if(positionColor == opositeColor)
				positionToTurnFlag = true;
			
			numberTurnedTiles++;
		}
		
		// Cria movimento.
		if(positionToTurnFlag) {
			return new PartialMovement(
					emptySpot, 
					new Position(y, x), 
					direction,
					numberTurnedTiles-1);
		}
		
		return null;
	}

	public void executePartialMovement(PartialMovement move) {
		
		Position emptySpot = move.emptySpace;
		Direction direction = move.direction;
		Color playerColor = getCor(move.playerTile);

		// Coloca a nova peça.
		setPosition(emptySpot, playerColor);

		int y = emptySpot.y;
		int x = emptySpot.x;
		
		// Troca todas as peças que devem ser viradas.		
		for(int cont=1 ; cont <= move.turnedTiles; cont++) {
			
			// Avança na direção
			if(direction == Direction.RIGTH)
				x++;
			else if(direction == Direction.LEFT)
				x--;
			else if(direction == Direction.UP)
				y--;
			else if(direction == Direction.DOWN)
				y++;
			else if(direction == Direction.DOWN_RIGTH) {
				y++;
				x++;
			}
			else if(direction == Direction.DOWN_LEFT) {
				y++;
				x--;
			}
			else if(direction == Direction.UP_RIGTH) {
				y--;
				x++;
			}
			else if(direction == Direction.UP_LEFT) {
				y--;
				x--;
			}
			
			setPosition(new Position(y, x), playerColor);
		}
	}

	public void executeMovement(Movement move) {
		
		for(PartialMovement movimento : move.movements)
			executePartialMovement(movimento);
	}
	
	public String getConfiguration() {
		
		String configuration = new String();
		
		for(int y=0 ; y<SIZE ; y++)
			for(int x=0 ; x<SIZE ; x++)
				configuration += this.board[y][x];
		
		return configuration;		
	}

	public TreeMap<Color, Integer> calculateNumberTiles() {
		
		TreeMap<Color, Integer> values = new TreeMap<Color, Integer>();
		
		Integer blackTiles = 0;
		Integer whiteTiles = 0;
		
		for(int y=0 ; y<SIZE ; y++) {
			for(int x=0 ; x<SIZE ; x++) {
				Color color = this.board[y][x];
				
				if(color == Color.BLACK)
					blackTiles++;
				else if(color == Color.WHITE)
					whiteTiles++;
			}
		}
		
		values.put(Color.BLACK, blackTiles);
		values.put(Color.WHITE, whiteTiles);
		
		return values;
	}
	
	public int calculteNumberTiles() {
		
		int tilesNumber = 0;
		
		for(int y=0 ; y<SIZE ; y++)
			for(int x=0 ; x<SIZE ; x++)
				if(this.board[y][x] != Color.BLANK)
					tilesNumber++;
		
		return tilesNumber;
	}
	
	public static Board readBoardFile(String path) 
			throws IOException {
			
		Board board = new Board();
		BufferedReader buffer = new BufferedReader(new FileReader(path));
		
		for(int y=0 ; y<SIZE ; y++) {
			for(int x=0 ; x<SIZE ; x++) {
				char c = (char) buffer.read();

				Color color;
				
				if(c == Color.BLACK.getRepresentation())
					color = Color.BLACK;
				else if(c == Color.WHITE.getRepresentation())
					color = Color.WHITE;
				else
					color = Color.BLANK;
						
				board.board[y][x] = color;
			}
			// Quebra de linha.
			buffer.read();
		}
	
		return board;
	}
	
	public int calculateBoardersNumber(Color color) {
		
		int cont = 0;

		// Quinas
		if(board[0][0] == color) cont++;
		if(board[0][SIZE-1] == color) cont++;
		if(board[SIZE-1][0] == color) cont++;
		if(board[SIZE-1][SIZE-1] == color) cont++;
		
		return cont;
	}
	
	@Override
	public Board clone() {
		Board novo = new Board();
		
		for(int y=0 ; y<SIZE ; y++)
			for(int x=0 ; x<SIZE ; x++)
				novo.board[y][x] = this.board[y][x];
		
		return novo;
	}
	
	public void print() {

		System.out.print(" |");
		for(int y=0 ; y<SIZE ; y++)
			System.out.print(y +"|");
		System.out.println();
			
		for(int y=0 ; y<SIZE ; y++) {
			System.out.print(y +"|");
			
			for(int x=0 ; x<SIZE ; x++)
				System.out.print(this.board[y][x] +"|");
			
			System.out.println();
		}
	}

	@Override
	public String toString() {
		return getConfiguration();
	}
}

