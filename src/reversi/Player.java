package reversi;

import java.util.Map;

public interface Player {
	public Movement selectMovement(Board board, Color color);
	
	public void setSVMParameters(AI artificialInteligence, Map<Position,Double> movementsMade);
}
