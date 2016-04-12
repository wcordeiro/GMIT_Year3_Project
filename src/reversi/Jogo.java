package reversi;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
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


public class Jogo {
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
  private InteligenciaArtificial IA;
  private Stack pilhaTabuleiros;
  private Tabuleiro t;
  private JPanel frame;
  private JFrame bigger_frame;
  private JButton B[][];
  private JButton undo_buttom;
  private JLabel black;
  private JLabel white;
  private JLabel black_points;
  private JLabel white_points;
  private JPanel bottom_panel;
  private Evento evt;
  private EventoUndo evtUndo;
  private Boolean isWainting;

  private void jogarContraHumano() {
    String opcao = JOptionPane.showInputDialog("Escolha uma cor (" + IA.COR_PRIMEIRO_JOGADOR.name() + " começa):\n"
            + "1 - Preto\n2 - Branco");
    IA.dificuldade = JOptionPane.showInputDialog("Escolha uma dificuldade:\n"
            + "1 - Facil\n2 - Medio\n3 - Dificil");

    if (IA.dificuldade.equals("1")) {
      IA.PROFUNDIDADE_PADRAO = 5;
    } else if (IA.dificuldade.equals("2")) {
      IA.PROFUNDIDADE_PADRAO = 5;
    } else {
      IA.PROFUNDIDADE_PADRAO = 6;
    }

    if (opcao.equals("1")) {
      IA.corHumano = Cor.PRETO;
    } else if (opcao.equals("2")) {
      IA.corHumano = Cor.BRANCO;
    } else {
      IA.corHumano = Cor.PRETO;
    }

    // -------------------------------------
    IA.tabuleiro = new Tabuleiro();
    List<Jogada> jogadas = IA.tabuleiro.calculaJogadasPossiveisJogador(IA.corHumano);
    for (int i = 0; i < B.length; i++) {
      for (int j = 0; j < B[i].length; j++) {
        if (IA.tabuleiro.getCor(new Posicao(i, j)) == Cor.PRETO) {
          B[i][j].setIcon(new ImageIcon("black.png"));
        } else if (IA.tabuleiro.getCor(new Posicao(i, j)) == Cor.BRANCO) {
          B[i][j].setIcon(new ImageIcon("white.png"));
        } else {
          B[i][j].setIcon(new ImageIcon("none.png"));
        }
        if (jogadas != null) {
          for (Jogada jogada : jogadas) {
            if (jogada.getPosicaoVazia().x == j && jogada.getPosicaoVazia().y == i) {
              B[i][j].setIcon(new ImageIcon("green.png"));
            }
          }
        }
      }
    }
    Cor corDaVez = IA.COR_PRIMEIRO_JOGADOR;
    Jogada jogada = null;

    while (true) {

      TreeMap<Cor, List<Jogada>> jogadasPossiveis
              = new TreeMap<Cor, List<Jogada>>();

      jogadasPossiveis.put(
              Cor.PRETO,
              IA.tabuleiro.calculaJogadasPossiveisJogador(Cor.PRETO)
      );

      jogadasPossiveis.put(
              Cor.BRANCO,
              IA.tabuleiro.calculaJogadasPossiveisJogador(Cor.BRANCO)
      );

      if (jogadasPossiveis.get(Cor.PRETO) == null
              && jogadasPossiveis.get(Cor.BRANCO) == null) {
        break;
      }

      if (corDaVez == IA.corHumano && jogadasPossiveis.get(corDaVez) != null) {

        do {
          int x = Util.leInteiro(0, Tabuleiro.TAMANHO - 1);
          int y = Util.leInteiro(0, Tabuleiro.TAMANHO - 1);
          
          System.out.println(x+""+y);

          jogada = IA.tabuleiro.calculaJogada(new Posicao(y, x), corDaVez);

          if (jogada == null)
						;

        } while (jogada == null);
      } else if (jogadasPossiveis.get(corDaVez) != null) {

        long tempoInicio = System.currentTimeMillis();

        jogada = IA.play(IA.tabuleiro, corDaVez);

        long tempoFinal = System.currentTimeMillis() - tempoInicio;
        Double segundos = ((double) tempoFinal) / 1000;

      }

      if (jogada != null) {                
        IA.tabuleiro.executaJogada(jogada);
      }

      corDaVez = Cor.getCorOposta(corDaVez);
      imprimeTabuleiro(corDaVez);
    }
    
  }
  
  public void jogaContraPc(int k) throws IOException{
	//  IA.dificuldade = "3";
/*	  if(k < 10){
		  IA.dificuldade = "3";
		  IA.PROFUNDIDADE_PADRAO = 1;
	  }
	  else if(k < 20){
		  IA.dificuldade = "3";
		  IA.PROFUNDIDADE_PADRAO = 2;
	  }
	  else if(k < 30){
		  IA.dificuldade = "3";
		  IA.PROFUNDIDADE_PADRAO = 3;
	  }
	  else if(k < 40){
		  IA.dificuldade = "3";
		  IA.PROFUNDIDADE_PADRAO = 4;
	  }
	  else if(k < 50){
		  IA.dificuldade = "3";
		  IA.PROFUNDIDADE_PADRAO = 5;
	  }
	  else if(k < 60){
		  IA.dificuldade = "3";
		  IA.PROFUNDIDADE_PADRAO = 6;
	  }*/
	  IA.dificuldade = "3";
	  IA.PROFUNDIDADE_PADRAO = 1;
	  IA.corHumano = Cor.BRANCO;
	  Map<Posicao,Double> movementsMap = new HashMap<Posicao,Double>();
	  SVM svm = new SVM();
	  
	  FileWriter fw = new FileWriter("teste.txt",true );
	  BufferedWriter bw = new BufferedWriter( fw );
	  
	  IA.tabuleiro = new Tabuleiro();
	    List<Jogada> jogadas = IA.tabuleiro.calculaJogadasPossiveisJogador(IA.corHumano);
	    for (int i = 0; i < B.length; i++) {
	      for (int j = 0; j < B[i].length; j++) {
	        if (IA.tabuleiro.getCor(new Posicao(i, j)) == Cor.PRETO) {
	          B[i][j].setIcon(new ImageIcon("black.png"));
	        } else if (IA.tabuleiro.getCor(new Posicao(i, j)) == Cor.BRANCO) {
	          B[i][j].setIcon(new ImageIcon("white.png"));
	        } else {
	          B[i][j].setIcon(new ImageIcon("none.png"));
	        }
	        if (jogadas != null) {
	          for (Jogada jogada : jogadas) {
	            if (jogada.getPosicaoVazia().x == j && jogada.getPosicaoVazia().y == i) {
	              B[i][j].setIcon(new ImageIcon("green.png"));
	            }
	          }
	        }
	      }
	    }
	    Cor corDaVez = IA.COR_PRIMEIRO_JOGADOR;
	    Jogada jogada = null;

	    while (true) {

	      TreeMap<Cor, List<Jogada>> jogadasPossiveis
	              = new TreeMap<Cor, List<Jogada>>();

	      jogadasPossiveis.put(
	              Cor.PRETO,
	              IA.tabuleiro.calculaJogadasPossiveisJogador(Cor.PRETO)
	      );

	      jogadasPossiveis.put(
	              Cor.BRANCO,
	              IA.tabuleiro.calculaJogadasPossiveisJogador(Cor.BRANCO)
	      );

	      if (jogadasPossiveis.get(Cor.PRETO) == null
	              && jogadasPossiveis.get(Cor.BRANCO) == null) {
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
	    	  TreeMap<Cor, Integer> numPecas = IA.tabuleiro.calculaNumeroPecas();
	    	  Integer numPretas = numPecas.get(Cor.PRETO);
	  	      Integer numBrancas = numPecas.get(Cor.BRANCO);
	  	      if(numPretas < numBrancas){
	  	    	  win++;
	  	    	  fwF = new FileWriter("Test/win"+win.toString()+".txt");
	  	    	  
	  	      }
	  	      else{
	  	    	  defeat++;
	  	    	  fwF = new FileWriter("Test/defeat"+defeat.toString()+".txt");
	  	      }
	  	      bwF = new BufferedWriter(fwF); 
	    	  for(Map.Entry<Posicao,Double> mapData : movementsMap.entrySet()) {
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
	      if(corDaVez == Cor.PRETO){
	    	//  jogada = IA.play(IA.tabuleiro, corDaVez);
	    	  Random rand = new Random();
	    	  try{
	    		  if(jogadasPossiveis.get(corDaVez) == null){
	    			  jogada=null;
	    		  }
	    		  else if(jogadasPossiveis.get(corDaVez).size() == 0){
	    			  jogada=null;
	    		  }
	    		  else if (jogadasPossiveis.get(corDaVez).size() == 1){
	    			  jogada = jogadasPossiveis.get(corDaVez).get(0);
	    		  }
	    		  else{
	    			  jogada = jogadasPossiveis.get(corDaVez).get(rand.nextInt(jogadasPossiveis.get(corDaVez).size() - 1));
	    		  }
	    	  } 	  
	    	  catch (Exception e){
	    		jogada = null;
	    	  }
	    	  jogada = IA.play(IA.tabuleiro, corDaVez);
	      }
	      else
	    	  jogada = svm.evaluatePositions(corDaVez, IA,movementsMap);
	      
	      if (jogada != null) {                
	          IA.tabuleiro.executaJogada(jogada);
	          Nodo nodo = new Nodo(IA.tabuleiro,corDaVez);
	          Nodo filho = new Nodo(nodo, jogada);
	          jogada.evalScore = IA.calculaFuncaoAvaliacaoDificil(filho.estado, corDaVez);
	          movementsMap.put(jogada.posicaoVazia, jogada.evalScore);
	       //   if(corDaVez == Cor.BRANCO)
	       // 	  bw.write("+" + jogada.posicaoVazia.x + jogada.posicaoVazia.y);
	       //   else
	        //	  bw.write("-" + jogada.posicaoVazia.x + jogada.posicaoVazia.y);
	      }
	      //jogada.posicaoVazia.x;
	      
	      

	        corDaVez = Cor.getCorOposta(corDaVez);
	        imprimeTabuleiro(corDaVez);
	    /*    try {
				Thread.currentThread().sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
	      
	    }
	    TreeMap<Cor, Integer> numPecas = IA.tabuleiro.calculaNumeroPecas();

	    Integer numPretas = numPecas.get(Cor.PRETO);
	    Integer numBrancas = numPecas.get(Cor.BRANCO);
	    bw.write(": +" + numPretas + " " + numBrancas);
	    bw.newLine();
	    bw.close();
	    fw.close();
	  
  }

  public void imprimeTabuleiro(Cor color) {
    List<Jogada> jogadasPossiveis = IA.tabuleiro.calculaJogadasPossiveisJogador(color);
    for (int i = 0; i < B.length; i++) {
      for (int j = 0; j < B[i].length; j++) {
    	  
        if (IA.tabuleiro.getCor(new Posicao(i, j)) == Cor.PRETO) {
          B[i][j].setIcon(new ImageIcon("black.png"));
        } else if (IA.tabuleiro.getCor(new Posicao(i, j)) == Cor.BRANCO) {
          B[i][j].setIcon(new ImageIcon("white.png"));
        } else {
          B[i][j].setIcon(new ImageIcon("none.png"));
        }
        if (jogadasPossiveis != null && !isWainting) {
          for (Jogada jogada : jogadasPossiveis) {
            if (jogada.getPosicaoVazia().x == j && jogada.getPosicaoVazia().y == i) {
              B[i][j].setIcon(new ImageIcon("green.png"));
            }
          }
        }
      }
    }
    TreeMap<Cor, Integer> numPecas = IA.tabuleiro.calculaNumeroPecas();

    Integer numPretas = numPecas.get(Cor.PRETO);
    Integer numBrancas = numPecas.get(Cor.BRANCO);
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
          t = IA.tabuleiro.clone();         
          pilhaTabuleiros.push(t);
          for (int i = 0; i < B.length; i++) {
            for (int j = 0; j < B[i].length; j++) {
              if (e.getSource() == B[i][j]) {
                Jogada jogada = IA.tabuleiro.calculaJogada(new Posicao(i, j), IA.corHumano);
                try {
                  IA.tabuleiro.executaJogada(jogada);
                  isWainting = true;                  
                  imprimeTabuleiro(Cor.getCorOposta(IA.corHumano));
                  Jogada play = IA.play(IA.tabuleiro, Cor.getCorOposta(IA.corHumano));                  
                  if (play != null) {                    
                    IA.tabuleiro.executaJogada(play);                    
                  }
                  try {
                    Thread.sleep(1000);                 //1000 milliseconds is one second.
                  } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                  }
                  isWainting = false;
                  imprimeTabuleiro(IA.corHumano);
                  TreeMap<Cor, List<Jogada>> jogadasPossiveis
                          = new TreeMap<Cor, List<Jogada>>();

                  jogadasPossiveis.put(
                          Cor.PRETO,
                          IA.tabuleiro.calculaJogadasPossiveisJogador(Cor.PRETO)
                  );

                  jogadasPossiveis.put(
                          Cor.BRANCO,
                          IA.tabuleiro.calculaJogadasPossiveisJogador(Cor.BRANCO)
                  );
                  while (jogadasPossiveis.get(IA.corHumano) == null && jogadasPossiveis.get(Cor.getCorOposta(IA.corHumano)) != null) {
                    play = IA.play(IA.tabuleiro, Cor.getCorOposta(IA.corHumano));
                    if (play != null) {
//                      t = IA.tabuleiro.clone();         
//                      pilhaTabuleiros.push(t);
                      IA.tabuleiro.executaJogada(play);
                    }
                    imprimeTabuleiro(IA.corHumano);
                    frame.repaint();
                    jogadasPossiveis.put(
                            Cor.PRETO,
                            IA.tabuleiro.calculaJogadasPossiveisJogador(Cor.PRETO)
                    );

                    jogadasPossiveis.put(
                            Cor.BRANCO,
                            IA.tabuleiro.calculaJogadasPossiveisJogador(Cor.BRANCO)
                    );
                  }
                  if (jogadasPossiveis.get(Cor.PRETO) == null
                          && jogadasPossiveis.get(Cor.BRANCO) == null) {
                    TreeMap<Cor, Integer> numPecas = IA.tabuleiro.calculaNumeroPecas();
                    Integer numPretas = numPecas.get(Cor.PRETO);
                    Integer numBrancas = numPecas.get(Cor.BRANCO);
                    String msg = "Placar:\nPretas: " + numPretas + "\nBrancas: " + numBrancas;
                    JOptionPane.showMessageDialog(frame, msg);
                    System.exit(0);
                    break;
                  }
                } catch (Exception ex) {
                  System.out.println(ex);
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
        if(pilhaTabuleiros.empty()){
          JOptionPane.showMessageDialog(bigger_frame, "Não existem mais movimentos para desfazer.");
        }
        else{
          IA.tabuleiro = (Tabuleiro) pilhaTabuleiros.pop();          
          imprimeTabuleiro(IA.corHumano);
        }
      }
    }
  
  }
  public void iniciaJogo(int k) {
    pilhaTabuleiros = new Stack();
    isWainting = false;
    IA = new InteligenciaArtificial();
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
    
    bottom_panel.add(black);
    bottom_panel.add(white);
    bottom_panel.add(black_points);
    bottom_panel.add(white_points);
    bottom_panel.add(undo_buttom, BorderLayout.CENTER);
    evt = new Jogo.Evento();
    evtUndo = new Jogo.EventoUndo();
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
    bigger_frame.add(bottom_panel,BorderLayout.SOUTH);
    
//    bigger_frame.pack();
    bigger_frame.setVisible(true);

    jogarContraHumano();
    /*
    try {
	//for(int i = 0; i < 100; i++)
			jogaContraPc(k);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}*/
  }

}
