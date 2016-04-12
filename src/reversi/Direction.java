package reversi;

import java.util.Arrays;
import java.util.List;

public enum Direction {

	DOWN, 
	UP,
	LEFT,
	RIGTH,
	UP_RIGTH,
	UP_LEFT,
	DOWN_RIGTH,
	DOWN_LEFT;
	
	public static List<Direction> getAll() {

		return Arrays.asList(Direction.values());
	}
}
