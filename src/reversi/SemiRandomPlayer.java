package reversi;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javax.swing.JOptionPane;

public class SemiRandomPlayer implements Player {
	
	private AI artificialInteligence;
	
	public void setupArtificialInteligence(Color color){

			artificialInteligence.level = "3";

	    	artificialInteligence.DEFAUTL_DEPTH = 6;


	    	if (color == Color.WHITE) {
	    		artificialInteligence.humanColor = Color.BLACK;
	    	} 
	    	else{
	    		artificialInteligence.humanColor = Color.WHITE;
	    	} 	
	}
	
	@Override
	public Movement selectMovement(Board board, Color color) {
		Movement move;
		this.setupArtificialInteligence(color);
		TreeMap<Color, List<Movement>> possibleMoviments= new TreeMap<Color, List<Movement>>();

		possibleMoviments.put(Color.BLACK,artificialInteligence.board.calcultePossibleMovesByColor(Color.BLACK));

		possibleMoviments.put(Color.WHITE,artificialInteligence.board.calcultePossibleMovesByColor(Color.WHITE));
		Random randomize = new Random();
		int option = randomize.nextInt(11);
		if(option == 3 || option == 7){
			move = this.artificialInteligence.play(board, color);
		}
		else{
			Random rand = new Random();
  	  		try{
  	  			if(possibleMoviments.get(color) == null){
  			  		move=null;
  		  		}
  		  		else if(possibleMoviments.get(color).size() == 0){
  			  		move=null;
  		  		}
  		  		else if (possibleMoviments.get(color).size() == 1){
  			  		move = possibleMoviments.get(color).get(0);
  		  		}
  		  		else{
  			  		move = possibleMoviments.get(color).get(rand.nextInt(possibleMoviments.get(color).size() - 1));
  		  		}
  	  		} 	  
  	  		catch (Exception e){
  	  			move = null;
  	  		}
		}
		return move;
	}

	@Override
	public void setSVMParameters(AI artificialInteligence, Map<Position, Double> movementsMade) {
		this.artificialInteligence = artificialInteligence;
		
	}
	
}
