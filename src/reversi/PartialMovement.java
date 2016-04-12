package reversi;

public class PartialMovement {
	
	public Position emptySpace;
	public Position playerTile;
	// Da vazia até a peça do jogador.
	public Direction direction;
	public Integer turnedTiles;

	public PartialMovement(
			Position emptySpot, 
			Position playerTile,
			Direction direction,
			Integer turnedTiles) {
		
		this.emptySpace = emptySpot;
		this.playerTile = playerTile;
		this.direction = direction;
		this.turnedTiles = turnedTiles;
	}
	
	@Override
	public String toString() {
//		return espacoVazio +" -> " +direcao +" -> " +pecaJogador +" # " +pecasViradas;
		return direction +" . " +playerTile +" #" +turnedTiles;
	}
}
