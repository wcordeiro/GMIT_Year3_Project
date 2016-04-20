package reversi;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;
import java.util.TreeMap;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import util.Util;


public class Game {
  private class ImagePanel extends JPanel {

    private Image img;

    public ImagePanel(String img) {
      this(new ImageIcon(img).getImage());
    }

    public ImagePanel(Image img) {
      this.img = img;
      Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
      setPreferredSize(size);
      setMinimumSize(size);
      setMaximumSize(size);
      setSize(size);
      setLayout(null);
    }

    public void paintComponent(Graphics g) {
      g.drawImage(img, 0, 0, null);
    }

  }
  private AI artificialInteligence;
  private Stack boardStack;
  private Board board;
  private JPanel frame;
  private JFrame bigger_frame;
  private JButton B[][];
  private JButton undo_buttom;
  private JLabel black;
  private JLabel white;
  private JLabel play1;
  private JLabel play2;
  private JLabel black_points;
  private JLabel white_points;
  private JPanel bottom_panel;
  private Evento evt;
  private EventoUndo evtUndo;
  private Boolean isWainting;
  
  private Player player;

  private void playAgainstHuman() {
	  Map<Position,Double> movementsMap = new HashMap<Position,Double>();

    // -------------------------------------
    artificialInteligence.board = new Board();
    List<Movement> movements = artificialInteligence.board.calcultePossibleMovesByColor(artificialInteligence.humanColor);
    for (int i = 0; i < B.length; i++) {
      for (int j = 0; j < B[i].length; j++) {
        if (artificialInteligence.board.getCor(new Position(i, j)) == Color.BLACK) {
          B[i][j].setIcon(new ImageIcon("black.png"));
        } else if (artificialInteligence.board.getCor(new Position(i, j)) == Color.WHITE) {
          B[i][j].setIcon(new ImageIcon("white.png"));
        } else {
          B[i][j].setIcon(new ImageIcon("none.png"));
        }
        if (movements != null) {
          for (Movement move : movements) {
            if (move.getEmptyPosition().x == j && move.getEmptyPosition().y == i) {
              B[i][j].setIcon(new ImageIcon("green.png"));
            }
          }
        }
      }
    }
    Color turnColor = artificialInteligence.BEGGINER_COLOR;
    Movement move = null;

    while (true) {

      TreeMap<Color, List<Movement>> possibleMoves
              = new TreeMap<Color, List<Movement>>();

      possibleMoves.put(
              Color.BLACK,
              artificialInteligence.board.calcultePossibleMovesByColor(Color.BLACK)
      );

      possibleMoves.put(
              Color.WHITE,
              artificialInteligence.board.calcultePossibleMovesByColor(Color.WHITE)
      );

      if (possibleMoves.get(Color.BLACK) == null
              && possibleMoves.get(Color.WHITE) == null) {
        break;
      }

      if (turnColor == artificialInteligence.humanColor && possibleMoves.get(turnColor) != null) {

        do {
          int x = Util.readInteger(0, Board.SIZE - 1);
          int y = Util.readInteger(0, Board.SIZE - 1);
          
          System.out.println(x+""+y);

          move = artificialInteligence.board.calcuteMove(new Position(y, x), turnColor);

          if (move == null)
						;

        } while (move == null);
      } else if (possibleMoves.get(turnColor) != null) {

        long tempoInicio = System.currentTimeMillis();
        player.setSVMParameters(artificialInteligence, movementsMap);
        move = player.selectMovement(artificialInteligence.board, turnColor);

        long tempoFinal = System.currentTimeMillis() - tempoInicio;
        Double segundos = ((double) tempoFinal) / 1000;

      }

      if (move != null) {                
        artificialInteligence.board.executeMovement(move);
        Node node = new Node(artificialInteligence.board,turnColor);
        Node child = new Node(node, move);
        move.evalScore = artificialInteligence.calculateLevelHardFunction(child.board, turnColor);
        movementsMap.put(move.emptyPosition, move.evalScore);
      }

      turnColor = Color.getOpositeColor(turnColor);
      printBoard(turnColor);
    }
    
  }
  
  public void playAgainstSVM() throws IOException{
	  Map<Position,Double> movementsMap = new HashMap<Position,Double>();
	  SVM svm = new SVM();
	  
	  FileWriter fw = new FileWriter("teste.txt",true );
	  BufferedWriter bw = new BufferedWriter( fw );
	  
	  artificialInteligence.board = new Board();
	    List<Movement> movements = artificialInteligence.board.calcultePossibleMovesByColor(artificialInteligence.humanColor);
	    for (int i = 0; i < B.length; i++) {
	      for (int j = 0; j < B[i].length; j++) {
	        if (artificialInteligence.board.getCor(new Position(i, j)) == Color.BLACK) {
	          B[i][j].setIcon(new ImageIcon("black.png"));
	        } else if (artificialInteligence.board.getCor(new Position(i, j)) == Color.WHITE) {
	          B[i][j].setIcon(new ImageIcon("white.png"));
	        } else {
	          B[i][j].setIcon(new ImageIcon("none.png"));
	        }
	        if (movements != null) {
	          for (Movement move : movements) {
	            if (move.getEmptyPosition().x == j && move.getEmptyPosition().y == i) {
	              B[i][j].setIcon(new ImageIcon("green.png"));
	            }
	          }
	        }
	      }
	    }
	    Color turnColor = artificialInteligence.BEGGINER_COLOR;
	    Movement move = null;

	    while (true) {

	      TreeMap<Color, List<Movement>> possibleMoviments
	              = new TreeMap<Color, List<Movement>>();

	      possibleMoviments.put(
	              Color.BLACK,
	              artificialInteligence.board.calcultePossibleMovesByColor(Color.BLACK)
	      );

	      possibleMoviments.put(
	              Color.WHITE,
	              artificialInteligence.board.calcultePossibleMovesByColor(Color.WHITE)
	      );

	      if (possibleMoviments.get(Color.BLACK) == null
	              && possibleMoviments.get(Color.WHITE) == null) {
	    	  FileReader fr = null;
	    	  FileWriter fwF = null;
	    	  BufferedWriter bwF = null;
	    	  BufferedReader br = null;
	    	  fr = new FileReader("FileNumber.txt");
	    	  br = new BufferedReader (fr);
	    	  Integer defeat = Integer.decode(br.readLine());
	    	  Integer win = Integer.decode(br.readLine());
	    	  fr.close();
	    	  br.close();
	    	  Integer index = 1;
	    	  TreeMap<Color, Integer> numPecas = artificialInteligence.board.calculateNumberTiles();
	    	  Integer numPretas = numPecas.get(Color.BLACK);
	  	      Integer numBrancas = numPecas.get(Color.WHITE);
	  	      if(numPretas < numBrancas){
	  	    	  win++;
	  	    	  fwF = new FileWriter("Test/win"+win.toString()+".txt");
	  	    	  
	  	      }
	  	      else{
	  	    	  defeat++;
	  	    	  fwF = new FileWriter("Test/defeat"+defeat.toString()+".txt");
	  	      }
	  	      bwF = new BufferedWriter(fwF); 
	    	  for(Map.Entry<Position,Double> mapData : movementsMap.entrySet()) {
	    		  int letter = (mapData.getKey().x+97);
	  		      String newLine = Character.toString ((char)(letter)) + "" + (mapData.getKey().y+1);
	  		      if(mapData.getValue() < 0){
					Double aux = Math.log(Math.abs(mapData.getValue()));
					bwF.write(index + ": " + newLine + " " + (aux*-1));
	  		      }
	  		      else if(mapData.getValue() == 0)
	  		    	bwF.write(index + ": " +newLine + " 0");
	  		      else
					bwF.write(index + ": " +newLine + " " + Math.log(mapData.getValue()));
	    		  bwF.newLine();
	    		  index++;
	    	  }
	    	  bwF.close();
	    	  fwF.close();
	    	  fwF = new FileWriter("FileNumber.txt");
	    	  bwF = new BufferedWriter(fwF); 
	    	  bwF.write(defeat.toString());
	    	  bwF.newLine();
	    	  bwF.write(win.toString());
	    	  bwF.close();
	    	  fwF.close();
	    	  break;
	      }
	      if(turnColor == Color.BLACK){
	    	  Random rand = new Random();
	    	  try{
	    		  if(possibleMoviments.get(turnColor) == null){
	    			  move=null;
	    		  }
	    		  else if(possibleMoviments.get(turnColor).size() == 0){
	    			  move=null;
	    		  }
	    		  else if (possibleMoviments.get(turnColor).size() == 1){
	    			  move = possibleMoviments.get(turnColor).get(0);
	    		  }
	    		  else{
	    			  move = possibleMoviments.get(turnColor).get(rand.nextInt(possibleMoviments.get(turnColor).size() - 1));
	    		  }
	    	  } 	  
	    	  catch (Exception e){
	    		move = null;
	    	  }
	    	  move = artificialInteligence.play(artificialInteligence.board, turnColor);
	      }
	      else
	    	  move = svm.evaluatePositions(turnColor, artificialInteligence,movementsMap);
	      
	      if (move != null) {                
	          artificialInteligence.board.executeMovement(move);
	          Node node = new Node(artificialInteligence.board,turnColor);
	          Node child = new Node(node, move);
	          move.evalScore = artificialInteligence.calculateLevelHardFunction(child.board, turnColor);
	          movementsMap.put(move.emptyPosition, move.evalScore);
	      }
	      

	        turnColor = Color.getOpositeColor(turnColor);
	        printBoard(turnColor);
	    /*    try {
				Thread.currentThread().sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
	      
	    }
	    TreeMap<Color, Integer> numPecas = artificialInteligence.board.calculateNumberTiles();

	    Integer numPretas = numPecas.get(Color.BLACK);
	    Integer numBrancas = numPecas.get(Color.WHITE);
	    bw.write(": +" + numPretas + " " + numBrancas);
	    bw.newLine();
	    bw.close();
	    fw.close();
	  
  }
  
  public void playAIvsAI(Player player1,Player player2, Color player1Color) throws IOException{
	  Map<Position,Double> movementsMap = new HashMap<Position,Double>();	  
	  play1.setVisible(true);
	  play2.setVisible(true);
	  artificialInteligence.board = new Board();
	    List<Movement> movements = artificialInteligence.board.calcultePossibleMovesByColor(artificialInteligence.humanColor);
	    for (int i = 0; i < B.length; i++) {
	      for (int j = 0; j < B[i].length; j++) {
	        if (artificialInteligence.board.getCor(new Position(i, j)) == Color.BLACK) {
	          B[i][j].setIcon(new ImageIcon("black.png"));
	        } else if (artificialInteligence.board.getCor(new Position(i, j)) == Color.WHITE) {
	          B[i][j].setIcon(new ImageIcon("white.png"));
	        } else {
	          B[i][j].setIcon(new ImageIcon("none.png"));
	        }
	        if (movements != null) {
	          for (Movement move : movements) {
	            if (move.getEmptyPosition().x == j && move.getEmptyPosition().y == i) {
	              B[i][j].setIcon(new ImageIcon("green.png"));
	            }
	          }
	        }
	      }
	    }
	    Color turnColor = artificialInteligence.BEGGINER_COLOR;
	    Movement move = null;

	    while (true) {

	      TreeMap<Color, List<Movement>> possibleMoves
	              = new TreeMap<Color, List<Movement>>();

	      possibleMoves.put(
	              Color.BLACK,
	              artificialInteligence.board.calcultePossibleMovesByColor(Color.BLACK)
	      );

	      possibleMoves.put(
	              Color.WHITE,
	              artificialInteligence.board.calcultePossibleMovesByColor(Color.WHITE)
	      );

	      if (possibleMoves.get(Color.BLACK) == null
	              && possibleMoves.get(Color.WHITE) == null) {
	    	  Node node;
	    	  if(player1.getClass() == ReinforcementLearnerWithExtraInfo.class){
	    		  ReinforcementLearnerWithExtraInfo training = (ReinforcementLearnerWithExtraInfo) player1;
	    		  node = new Node(artificialInteligence.board,player1Color);
	    		  training.learn(node, training.getV(), training.getNewA(), training.getNewA(), training.getNewA());
	    	  }
	    	  else if(player2.getClass() == ReinforcementLearnerWithExtraInfo.class){
	    		  ReinforcementLearnerWithExtraInfo training = (ReinforcementLearnerWithExtraInfo) player2;
	    		  node = new Node(artificialInteligence.board,Color.getOpositeColor(player1Color));
	    		  training.learn(node, training.getV(), training.getNewA(), training.getNewA(), training.getNewA());
	    	  }
	    	  break;
	      }
	      if(turnColor == player1Color){
	    	  player1.setSVMParameters(artificialInteligence, movementsMap);
	          move = player1.selectMovement(artificialInteligence.board, turnColor);
	      }
	      else{
	    	  	player2.setSVMParameters(artificialInteligence, movementsMap);
	        	move = player2.selectMovement(artificialInteligence.board, turnColor);
	      }
	      if (move != null) {                
	          artificialInteligence.board.executeMovement(move);
	          Node node = new Node(artificialInteligence.board,turnColor);
	          Node child = new Node(node, move);
	          move.evalScore = artificialInteligence.calculateLevelHardFunction(child.board, turnColor);
	          movementsMap.put(move.emptyPosition, move.evalScore);
	      }
	      
	        turnColor = Color.getOpositeColor(turnColor);
	        printBoard(turnColor);
	      
	    }
	    try {
	    	Thread.currentThread().sleep(100);
	    } 
	    catch (InterruptedException e) {
	    	// TODO Auto-generated catch block
	    	e.printStackTrace();
	    }
  }

  public void printBoard(Color color) {
    List<Movement> possibleColor = artificialInteligence.board.calcultePossibleMovesByColor(color);
    for (int i = 0; i < B.length; i++) {
      for (int j = 0; j < B[i].length; j++) {
    	  
        if (artificialInteligence.board.getCor(new Position(i, j)) == Color.BLACK) {
          B[i][j].setIcon(new ImageIcon("black.png"));
        } else if (artificialInteligence.board.getCor(new Position(i, j)) == Color.WHITE) {
          B[i][j].setIcon(new ImageIcon("white.png"));
        } else {
          B[i][j].setIcon(new ImageIcon("none.png"));
        }
        if (possibleColor != null && !isWainting) {
          for (Movement jogada : possibleColor) {
            if (jogada.getEmptyPosition().x == j && jogada.getEmptyPosition().y == i) {
              B[i][j].setIcon(new ImageIcon("green.png"));
            }
          }
        }
      }
    }
    TreeMap<Color, Integer> numberTiles = artificialInteligence.board.calculateNumberTiles();

    Integer numPretas = numberTiles.get(Color.BLACK);
    Integer numBrancas = numberTiles.get(Color.WHITE);
    white_points.setText(numBrancas+ "p.");
    black_points.setText(numPretas+ "p.");
  }

  public class Evento implements ActionListener {

    @Override
    public void actionPerformed(final ActionEvent e) {
      SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
        @Override
        public Void doInBackground() {
          if(isWainting) 
            return null;
          board = artificialInteligence.board.clone();         
          Map<Position,Double> movementsMap = new HashMap<Position,Double>();
          boardStack.push(board);
          for (int i = 0; i < B.length; i++) {
            for (int j = 0; j < B[i].length; j++) {
              if (e.getSource() == B[i][j]) {
                Movement move = artificialInteligence.board.calcuteMove(new Position(i, j), artificialInteligence.humanColor);
                try {
                  artificialInteligence.board.executeMovement(move);
                  Node node = new Node(artificialInteligence.board,artificialInteligence.humanColor);
                  Node child = new Node(node, move);
                  move.evalScore = artificialInteligence.calculateLevelHardFunction(child.board, artificialInteligence.humanColor);
                  movementsMap.put(move.emptyPosition, move.evalScore);
                  isWainting = true;                  
                  printBoard(Color.getOpositeColor(artificialInteligence.humanColor));
                  player.setSVMParameters(artificialInteligence, movementsMap);
                  Movement play = player.selectMovement(artificialInteligence.board, Color.getOpositeColor(artificialInteligence.humanColor)); 
                 // Movement play = artificialInteligence.play(artificialInteligence.board, Color.getOpositeColor(artificialInteligence.humanColor));                  
                  if (play != null) {                    
                    artificialInteligence.board.executeMovement(play); 
                    Node nodes = new Node(artificialInteligence.board,Color.getOpositeColor(artificialInteligence.humanColor));
                    Node childs = new Node(nodes, move);
                    move.evalScore = artificialInteligence.calculateLevelHardFunction(childs.board, Color.getOpositeColor(artificialInteligence.humanColor));
                    movementsMap.put(move.emptyPosition, move.evalScore);
                  }
                  try {
                    Thread.sleep(1000);                 //1000 milliseconds is one second.
                  } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    ex.printStackTrace();
                  }
                  isWainting = false;
                  printBoard(artificialInteligence.humanColor);
                  TreeMap<Color, List<Movement>> possibleMoves
                          = new TreeMap<Color, List<Movement>>();

                  possibleMoves.put(
                          Color.BLACK,
                          artificialInteligence.board.calcultePossibleMovesByColor(Color.BLACK)
                  );

                  possibleMoves.put(
                          Color.WHITE,
                          artificialInteligence.board.calcultePossibleMovesByColor(Color.WHITE)
                  );
                  while (possibleMoves.get(artificialInteligence.humanColor) == null && possibleMoves.get(Color.getOpositeColor(artificialInteligence.humanColor)) != null) {
                	player.setSVMParameters(artificialInteligence, movementsMap);
                    play = player.selectMovement(artificialInteligence.board, Color.getOpositeColor(artificialInteligence.humanColor)); 
                    if (play != null) {
//                      t = IA.tabuleiro.clone();         
//                      pilhaTabuleiros.push(t);
                      artificialInteligence.board.executeMovement(play);
                      Node nodes = new Node(artificialInteligence.board,Color.getOpositeColor(artificialInteligence.humanColor));
                      Node childs = new Node(nodes, move);
                      move.evalScore = artificialInteligence.calculateLevelHardFunction(childs.board, Color.getOpositeColor(artificialInteligence.humanColor));
                      movementsMap.put(move.emptyPosition, move.evalScore);
                    }
                    printBoard(artificialInteligence.humanColor);
                    frame.repaint();
                    possibleMoves.put(
                            Color.BLACK,
                            artificialInteligence.board.calcultePossibleMovesByColor(Color.BLACK)
                    );

                    possibleMoves.put(
                            Color.WHITE,
                            artificialInteligence.board.calcultePossibleMovesByColor(Color.WHITE)
                    );
                  }
                  if (possibleMoves.get(Color.BLACK) == null
                          && possibleMoves.get(Color.WHITE) == null) {
                    TreeMap<Color, Integer> numPecas = artificialInteligence.board.calculateNumberTiles();
                    Integer numPretas = numPecas.get(Color.BLACK);
                    Integer numBrancas = numPecas.get(Color.WHITE);
                    String msg = "Placar:\nPretas: " + numPretas + "\nBrancas: " + numBrancas;
                    JOptionPane.showMessageDialog(frame, msg);
                    System.exit(0);
                    break;
                  }
                } catch (Exception ex) {
                  System.out.println(ex);
                  ex.printStackTrace();
                }
              }
            }
          }
          
          return null;
            // If you want to return something other than null, change
          // the generic type to something other than Void.
          // This method's return value will be available via get() once the
          // operation has completed.
        }

        @Override
        protected void done() {
          // get() would be available here if you want to use it
//            myButton.setText("Done working");
        }
      };
      worker.execute();
    }
  }
  
  public class EventoUndo implements ActionListener {

    public void actionPerformed(ActionEvent e) {
      if(!isWainting){        
        if(boardStack.empty()){
          JOptionPane.showMessageDialog(bigger_frame, "Não existem mais movimentos para desfazer.");
        }
        else{
          artificialInteligence.board = (Board) boardStack.pop();          
          printBoard(artificialInteligence.humanColor);
        }
      }
    }
  
  }
  public void startGame(int k) {
    boardStack = new Stack();
    isWainting = false;
    artificialInteligence = new AI();
    bigger_frame = new JFrame("Reversi");
    bigger_frame.setSize(800,900);
    bigger_frame.setLayout(new BorderLayout());
    frame = new JPanel();
    frame.setSize(800, 800);
    frame.setLayout(new GridLayout(8, 8));    
//    bigger_frame.setResizable(false);
    bigger_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    B = new JButton[8][8];
    bottom_panel = new ImagePanel(new ImageIcon("bg_bot.png").getImage());
    bottom_panel.setSize(800,100);

    undo_buttom = new JButton("Undo");
    undo_buttom.setSize(150,40);
    black = new JLabel("10");
    black.setIcon(new ImageIcon("mini_black.png"));
    black.setText("123123123");
    black.setSize(50,50);
    black.setLocation(((frame.getWidth()-undo_buttom.getWidth())/5)-10, 25);
    black_points = new JLabel("2 p.");
    black_points.setSize(30,30);
    black_points.setLocation(((frame.getWidth()-undo_buttom.getWidth())/5)+70, 35);
    white = new JLabel("10");
    white.setSize(50,50);
    white.setIcon(new ImageIcon("mini_white.png"));
    white.setLocation(((frame.getWidth()-undo_buttom.getWidth())/4 )* 4 , 25);
    white_points = new JLabel("2 p.");
    white_points.setSize(30,30);
    white_points.setLocation((((frame.getWidth()-undo_buttom.getWidth())/4 )* 4)-30, 35);
    
    undo_buttom.setLocation((frame.getWidth()-undo_buttom.getWidth())/2 + 20, 30);
    
    
    play1 = new JLabel();
    play1.setLocation(0,10);
    
    play1.setSize(100, 50);
    play2 = new JLabel();
    play1.setLocation(0,50);
    play2.setSize(100, 50);
    //play1.setVisible(false);
   // play2.setVisible(false);
    bottom_panel.add(play1);
    bottom_panel.add(black);
    bottom_panel.add(white);
    bottom_panel.add(black_points);
    bottom_panel.add(white_points);
    bottom_panel.add(play2);
    bottom_panel.add(undo_buttom, BorderLayout.CENTER);
    evt = new Game.Evento();
    evtUndo = new Game.EventoUndo();
    undo_buttom.addActionListener(evtUndo);
    for (int i = 0; i < B.length; i++) {
      for (int j = 0; j < B[i].length; j++) {
    	 // if(i ==0 && j ==0 ) break;
        B[i][j] = new JButton("");// Inicializando
        B[i][j].setSize(50,50);
        if ((i == 3 && j == 4) || (i == 4 && j == 3)) {
          B[i][j].setIcon(new ImageIcon("black.png"));
        } else if ((i == 3 && j == 3) || (i == 4 && j == 4)) {
          B[i][j].setIcon(new ImageIcon("white.png"));
        } else {
          B[i][j].setIcon(new ImageIcon("none.png"));
        }
        frame.add(B[i][j]);// Adicionando componentes
        B[i][j].addActionListener(evt); //Colocando como listeners
      }
    }
    bigger_frame.add(frame);//  ,BorderLayout.NORTH);
   // bigger_frame.add(play1,BorderLayout.PAGE_END);
    bigger_frame.add(bottom_panel,BorderLayout.PAGE_END);
   // bigger_frame.add(play2,BorderLayout.PAGE_END);
//    bigger_frame.pack();
    bigger_frame.setVisible(true);

    String option = JOptionPane.showInputDialog("1 - Computer vs Computer \n2 - Human vs Computer");
    if(option.equals("1")){
    	try {
			this.optionsComputerVsComputer();
		} catch (IOException e) {
			System.out.println("Reinforcement Learning error");
			e.printStackTrace();
		}
    }
    else{
    	this.optionsHumansVSComputer();
    }
  }

  private void optionsComputerVsComputer() throws IOException {
	  String option = JOptionPane.showInputDialog("1 - MiniMax vs SVM"
	  		+ " \n2 - MiniMax vs ReinforcementMiniMax"
	  		+ " \n3 - MiniMax vs Reinforcement"
	  		+ " \n4 - MiniMax vs SemiRandom"
	  		+ " \n5 - SVM vs Reinforcement"
	  		+ " \n6 - SVM vs SemiRandom"
	  		+ " \n7 - SVM vs ReinforcementMiniMax"
	  		+ " \n8 - Reinforcement vs ReinforcementMiniMax"
	  		+ " \n9 - Reinforcement vs SemiRandom"
	  		+ " \n10 - ReinforcementMiniMax vs SemiRandom");
	  Player player1 = null;
	  Color colorPlayer1;
	  Player player2 = null;
	  Random rand = new Random();
	  if(rand.nextInt(11) < 5){
		  colorPlayer1 = Color.WHITE;
	  }
	  else{
		  colorPlayer1 = Color.BLACK;
	  }
	  if(option.equals("1")){
		  player1 = this.artificialInteligence;
		  player2 = new SVM();
		  play1.setText("MiniMax -" + colorPlayer1.name());
		  play2.setText("SVM -" + Color.getOpositeColor(colorPlayer1).name());
	  }
	  else if(option.equals("2")){
		  player1 = this.artificialInteligence;
		  player2 = new ReinforcementLearnerWithExtraInfo(Color.getOpositeColor(colorPlayer1));
		  play1.setText("MiniMax -" + colorPlayer1.name());
		  play2.setText("ReinforM -" + Color.getOpositeColor(colorPlayer1).name());
	  }
	  else if(option.equals("3")){
		  player1 = this.artificialInteligence;
		  player2 = new ReinforcementLearner(Color.getOpositeColor(colorPlayer1));
		  play1.setText("MiniMax -" + colorPlayer1.name());
		  play2.setText("Reinfor -" + Color.getOpositeColor(colorPlayer1).name());
	  }
	  else if(option.equals("4")){
		  player1 = this.artificialInteligence;
		  player2 = new SemiRandomPlayer();
		  play1.setText("MiniMax -" + colorPlayer1.name());
		  play2.setText("SemiRandom -" + Color.getOpositeColor(colorPlayer1).name());
	  }
	  else if(option.equals("5")){
		  player1 = new SVM();
		  player2 = new ReinforcementLearner(Color.getOpositeColor(colorPlayer1));
		  play1.setText("SVM -" + colorPlayer1.name());
		  play2.setText("Reinfor -" + Color.getOpositeColor(colorPlayer1).name());
	  }
	  else if(option.equals("6")){
		  player1 = new SVM();
		  player2 = new SemiRandomPlayer();
		  play1.setText("SVM -" + colorPlayer1.name());
		  play2.setText("SemiRandom -" + Color.getOpositeColor(colorPlayer1).name());
	  }
	  else if(option.equals("7")){
		  player1 = new SVM();
		  player2 = new ReinforcementLearnerWithExtraInfo(Color.getOpositeColor(colorPlayer1));
		  play1.setText("SVM -" + colorPlayer1.name());
		  play2.setText("ReinforE -" + Color.getOpositeColor(colorPlayer1).name());
	  }
	  else if(option.equals("8")){
		  player1 = new ReinforcementLearner(colorPlayer1);
		  player2 = new ReinforcementLearnerWithExtraInfo(Color.getOpositeColor(colorPlayer1));
		  play1.setText("Reinfor -" + colorPlayer1.name());
		  play2.setText("ReinforE -" + Color.getOpositeColor(colorPlayer1).name());
	  }
	  else if(option.equals("9")){
		  player1 = new ReinforcementLearner(colorPlayer1);
		  player2 = new SemiRandomPlayer();
		  play1.setText("Reinfor -" + colorPlayer1.name());
		  play2.setText("SemiRandom -" + Color.getOpositeColor(colorPlayer1).name());
	  }
	  else {
		  player1 = new ReinforcementLearnerWithExtraInfo(colorPlayer1);
		  player2 = new SemiRandomPlayer();
		  play1.setText("ReinforE -" + colorPlayer1.name());
		  play2.setText("SemiRandom -" + Color.getOpositeColor(colorPlayer1).name());
	  }
	  this.playAIvsAI(player1, player2, colorPlayer1);
	}

private void optionsHumansVSComputer() {
	String option = JOptionPane.showInputDialog("1 - MiniMax \n2 - SVM \n3 - ReinforcementMiniMax \n4 - Reinforcement \n5 - SemiRandom");
	String colorOption = JOptionPane.showInputDialog("Choose a color (" + artificialInteligence.BEGGINER_COLOR.name() + " starts):\n"
            + "1 - Black\n2 - White");
	if (colorOption.equals("1")) {
		artificialInteligence.humanColor = Color.BLACK;
	} 
	else if (colorOption.equals("2")) {
		artificialInteligence.humanColor = Color.WHITE;
	} 	
	else {
		artificialInteligence.humanColor = Color.BLACK;
	}
	///Player player = null;
	if(option.equals("1")){
		player = this.artificialInteligence;
	}
	else if(option.equals("2")){
		player = new SVM();
	}
	else if(option.equals("3")){
		try {
			player = new ReinforcementLearnerWithExtraInfo(artificialInteligence.humanColor);
		} catch (IOException e) {
			System.out.println("Reinforcement Learning error");
			e.printStackTrace();
		}
	}
	else if(option.equals("4")){
		try {
			player = new ReinforcementLearner(artificialInteligence.humanColor);
		} catch (IOException e) {
			System.out.println("Reinforcement Learning error");
			e.printStackTrace();
		}
	}
	else{
		player = new SemiRandomPlayer();
	}
	this.playAgainstHuman();
  }

}
