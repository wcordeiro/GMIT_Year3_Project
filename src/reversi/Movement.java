package reversi;

import java.util.List;

public class Movement {
	
	public Position emptyPosition;
	public List<PartialMovement> movements;
	public Integer turnTiles;
	public Double evalScore;
	
	public Movement(Position emptySpot, List<PartialMovement> movements) {
		this.emptyPosition = emptySpot;
		this.movements = movements;
		
		// Calcula o total de peças viradas.
		this.turnTiles = 0;
		for(PartialMovement movimento : movements)
			this.turnTiles += movimento.turnedTiles;
	}

    public Position getEmptyPosition() {
        return emptyPosition;
    }

    public void setEmptyPosition(Position posicaoVazia) {
        this.emptyPosition = posicaoVazia;
    }
	
	@Override
	public String toString() {
		
		String str = new String();
		
		str += emptyPosition +" (";
		for(PartialMovement mov : movements)
			str += mov.direction +"->" +mov.playerTile +", ";
		
		str = str.substring(0,  str.length()-2);
		
		str += ")";
				
		return str;
	}
}
