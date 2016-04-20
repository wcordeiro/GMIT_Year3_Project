package reversi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JOptionPane;

public class AI implements Player{
  public Color BEGGINER_COLOR = Color.BLACK;
  public Integer DEFAUTL_DEPTH = 5;
  public Integer MAX_DEPTH = DEFAUTL_DEPTH;
  public Color humanColor;
  public Board board;
  public String level;
  Map<Position,Double> movementsMap = new HashMap<Position,Double>();
  
  private  boolean isStopCondition(Node nodo) {
    return nodo.depth >= MAX_DEPTH;
  }

  public  double calculateLevelHardFunction(Board grid, Color my_color) {
    int my_tiles = 0, opp_tiles = 0, i, j, k, my_front_tiles = 0, opp_front_tiles = 0, x, y;
    double p = 0, c = 0, l = 0, m = 0, f = 0, d = 0;
    Color opp_color;
    opp_color = Color.getOpositeColor(my_color);
    Position posicion = new Position(-1, -1);
    int X1[] = {-1, -1, 0, 1, 1, 1, 0, -1};
    int Y1[] = {0, 1, 1, 1, 0, -1, -1, -1};
    int V[][] = {
      {20, -3, 11, 8, 8, 11, -3, 20},
      {-3, -7, -4, 1, 1, -4, -7, -3},
      {11, -4, 2, 2, 2, 2, -4, 11},
      {8, 1, 2, -3, -3, 2, 1, 8},
      {8, 1, 2, -3, -3, 2, 1, 8},
      {11, -4, 2, 2, 2, 2, -4, 11},
      {-3, -7, -4, 1, 1, -4, -7, -3},
      {20, -3, 11, 8, 8, 11, -3, 20}
    };
// Piece difference, frontier disks and disk squares
    for (i = 0; i < 8; i++) {
      for (j = 0; j < 8; j++) {
        posicion.y = j;
        posicion.x = i;
        if (grid.getCor(posicion) == my_color) {
          d += V[i][j];
          my_tiles++;
        } else if (grid.getCor(posicion) == opp_color) {
          d -= V[i][j];
          opp_tiles++;
        }
        if (grid.getCor(posicion) != Color.BLANK) {
          for (k = 0; k < 8; k++) {
            x = i + X1[k];
            y = j + Y1[k];
            if (x >= 0 && x < 8 && y >= 0 && y < 8 && grid.getCor(posicion) == Color.BLANK) {
              if (grid.getCor(posicion) == my_color) {
                my_front_tiles++;
              } else {
                opp_front_tiles++;
              }
              break;
            }
          }
        }
      }
    }
    if (my_tiles > opp_tiles) {
      p = (100.0 * my_tiles) / (my_tiles + opp_tiles);
    } else if (my_tiles < opp_tiles) {
      p = -(100.0 * opp_tiles) / (my_tiles + opp_tiles);
    } else {
      p = 0;
    }
    if (my_front_tiles > opp_front_tiles) {
      f = -(100.0 * my_front_tiles) / (my_front_tiles + opp_front_tiles);
    } else if (my_front_tiles < opp_front_tiles) {
      f = (100.0 * opp_front_tiles) / (my_front_tiles + opp_front_tiles);
    } else {
      f = 0;
    }
// Corner occupancy
    my_tiles = opp_tiles = 0;
    if (grid.getCor(new Position(0, 0)) == my_color) {
      my_tiles++;
    } else if (grid.getCor(new Position(0, 0)) == opp_color) {
      opp_tiles++;
    }
    if (grid.getCor(new Position(0, 7)) == my_color) {
      my_tiles++;
    } else if (grid.getCor(new Position(0, 7)) == opp_color) {
      opp_tiles++;
    }
    if (grid.getCor(new Position(7, 0)) == my_color) {
      my_tiles++;
    } else if (grid.getCor(new Position(7, 0)) == opp_color) {
      opp_tiles++;
    }
    if (grid.getCor(new Position(7, 7)) == my_color) {
      my_tiles++;
    } else if (grid.getCor(new Position(7, 7)) == opp_color) {
      opp_tiles++;
    }
    c = 25 * (my_tiles - opp_tiles);
// Corner closeness
    my_tiles = opp_tiles = 0;
    if (grid.getCor(new Position(0, 0)) == Color.BLANK) {
      if (grid.getCor(new Position(0, 1)) == my_color) {
        my_tiles++;
      } else if (grid.getCor(new Position(0, 1)) == opp_color) {
        opp_tiles++;
      }
      if (grid.getCor(new Position(1, 1)) == my_color) {
        my_tiles++;
      } else if (grid.getCor(new Position(1, 1)) == opp_color) {
        opp_tiles++;
      }
      if (grid.getCor(new Position(1, 0)) == my_color) {
        my_tiles++;
      } else if (grid.getCor(new Position(1, 0)) == opp_color) {
        opp_tiles++;
      }
    }
    if (grid.getCor(new Position(0, 7)) == Color.BLANK) {
      if (grid.getCor(new Position(0, 6)) == my_color) {
        my_tiles++;
      } else if (grid.getCor(new Position(0, 6)) == opp_color) {
        opp_tiles++;
      }
      if (grid.getCor(new Position(1, 6)) == my_color) {
        my_tiles++;
      } else if (grid.getCor(new Position(1, 6)) == opp_color) {
        opp_tiles++;
      }
      if (grid.getCor(new Position(1, 7)) == my_color) {
        my_tiles++;
      } else if (grid.getCor(new Position(1, 7)) == opp_color) {
        opp_tiles++;
      }
    }
    if (grid.getCor(new Position(7, 0)) == Color.BLANK) {
      if (grid.getCor(new Position(7, 1)) == my_color) {
        my_tiles++;
      } else if (grid.getCor(new Position(7, 1)) == opp_color) {
        opp_tiles++;
      }
      if (grid.getCor(new Position(6, 1)) == my_color) {
        my_tiles++;
      } else if (grid.getCor(new Position(6, 1)) == opp_color) {
        opp_tiles++;
      }
      if (grid.getCor(new Position(6, 0)) == my_color) {
        my_tiles++;
      } else if (grid.getCor(new Position(6, 0)) == opp_color) {
        opp_tiles++;
      }
    }
    if (grid.getCor(new Position(7, 7)) == Color.BLANK) {
      if (grid.getCor(new Position(6, 7)) == my_color) {
        my_tiles++;
      } else if (grid.getCor(new Position(6, 7)) == opp_color) {
        opp_tiles++;
      }
      if (grid.getCor(new Position(6, 6)) == my_color) {
        my_tiles++;
      } else if (grid.getCor(new Position(6, 6)) == opp_color) {
        opp_tiles++;
      }
      if (grid.getCor(new Position(7, 6)) == my_color) {
        my_tiles++;
      } else if (grid.getCor(new Position(7, 6)) == opp_color) {
        opp_tiles++;
      }
    }
    l = -12.5 * (my_tiles - opp_tiles);
// Mobility
    List<Movement> possibleMoves = grid.calcultePossibleMovesByColor(my_color);
    my_tiles = opp_tiles = 0;
    if (possibleMoves != null) {
      my_tiles = possibleMoves.size();
    }
    possibleMoves = grid.calcultePossibleMovesByColor(opp_color);
    if (possibleMoves != null) {
      opp_tiles = possibleMoves.size();
    }
    if (my_tiles > opp_tiles) {
      m = (100.0 * my_tiles) / (my_tiles + opp_tiles);
    } else if (my_tiles < opp_tiles) {
      m = -(100.0 * opp_tiles) / (my_tiles + opp_tiles);
    } else {
      m = 0;
    }
// final weighted score
    double score = (10 * p) + (801.724 * c) + (382.026 * l) + (78.922 * m) + (74.396 * f) + (10 * d);
    return score;
  }

  public  double calculateLevelMediumFunction(Board grid, Color my_color) {
    int my_tiles = 0, opp_tiles = 0, i, j, k, my_front_tiles = 0, opp_front_tiles = 0, x, y;
    double p = 0, c = 0, l = 0, f = 0, d = 0;
    Color opp_color;
    opp_color = Color.getOpositeColor(my_color);
    Position position = new Position(-1, -1);
    int X1[] = {-1, -1, 0, 1, 1, 1, 0, -1};
    int Y1[] = {0, 1, 1, 1, 0, -1, -1, -1};
    int V[][] = {
      {20, -3, 11, 8, 8, 11, -3, 20},
      {-3, -7, -4, 1, 1, -4, -7, -3},
      {11, -4, 2, 2, 2, 2, -4, 11},
      {8, 1, 2, -3, -3, 2, 1, 8},
      {8, 1, 2, -3, -3, 2, 1, 8},
      {11, -4, 2, 2, 2, 2, -4, 11},
      {-3, -7, -4, 1, 1, -4, -7, -3},
      {20, -3, 11, 8, 8, 11, -3, 20}
    };
// Piece difference, frontier disks and disk squares
    for (i = 0; i < 8; i++) {
      for (j = 0; j < 8; j++) {
        position.y = j;
        position.x = i;
        if (grid.getCor(position) == my_color) {
          d += V[i][j];
          my_tiles++;
        } else if (grid.getCor(position) == opp_color) {
          d -= V[i][j];
          opp_tiles++;
        }
        if (grid.getCor(position) != Color.BLANK) {
          for (k = 0; k < 8; k++) {
            x = i + X1[k];
            y = j + Y1[k];
            if (x >= 0 && x < 8 && y >= 0 && y < 8 && grid.getCor(position) == Color.BLANK) {
              if (grid.getCor(position) == my_color) {
                my_front_tiles++;
              } else {
                opp_front_tiles++;
              }
              break;
            }
          }
        }
      }
    }
    if (my_tiles > opp_tiles) {
      p = (100.0 * my_tiles) / (my_tiles + opp_tiles);
    } else if (my_tiles < opp_tiles) {
      p = -(100.0 * opp_tiles) / (my_tiles + opp_tiles);
    } else {
      p = 0;
    }
    if (my_front_tiles > opp_front_tiles) {
      f = -(100.0 * my_front_tiles) / (my_front_tiles + opp_front_tiles);
    } else if (my_front_tiles < opp_front_tiles) {
      f = (100.0 * opp_front_tiles) / (my_front_tiles + opp_front_tiles);
    } else {
      f = 0;
    }
// Corner occupancy
    my_tiles = opp_tiles = 0;
    if (grid.getCor(new Position(0, 0)) == my_color) {
      my_tiles++;
    } else if (grid.getCor(new Position(0, 0)) == opp_color) {
      opp_tiles++;
    }
    if (grid.getCor(new Position(0, 7)) == my_color) {
      my_tiles++;
    } else if (grid.getCor(new Position(0, 7)) == opp_color) {
      opp_tiles++;
    }
    if (grid.getCor(new Position(7, 0)) == my_color) {
      my_tiles++;
    } else if (grid.getCor(new Position(7, 0)) == opp_color) {
      opp_tiles++;
    }
    if (grid.getCor(new Position(7, 7)) == my_color) {
      my_tiles++;
    } else if (grid.getCor(new Position(7, 7)) == opp_color) {
      opp_tiles++;
    }
    c = 25 * (my_tiles - opp_tiles);
    // final weighted score
    double score = (10 * p) + (801.724 * c) + (382.026 * l) + (74.396 * f) + (10 * d);
    return score;
  }

  public  Double calculateLevelEasyFunction(Board board, Color color) {

    // Calcula o número de peças do jogador no tabuleiro.
    Integer tilesNumber = 0;
    Position position = new Position(-1, -1);

    for (int y = 0; y < Board.SIZE; y++) {
      for (int x = 0; x < Board.SIZE; x++) {
        position.y = y;
        position.x = x;

        if (board.getCor(position) == color) {
          tilesNumber++;
        }
      }
    }

    // Peças seguras
    Integer cornerNumber = board.calculateBoardersNumber(color);
    Double result = (tilesNumber.doubleValue() + cornerNumber.doubleValue());
    return result;
  }

  private  boolean shouldPrune(Node node) {

    Color color = node.playerColor;
    Color oppositeColor = Color.getOpositeColor(color);
    Node checking = node.predecessor;

    /* Caminha par cima na árvore para verificar se pode realizar a poda.*/
    while (checking != null) {

      if (checking.fnAvaliacao != null) {
        if (checking.playerColor == oppositeColor) {
          if (checking.fnAvaliacao(oppositeColor) > node.fnAvaliacao(oppositeColor)) {
            return true;
          }
        }
      }

      checking = checking.predecessor;
    }

    return false;
  }

  private  void setLevel(Node node) {
    node.fnAvaliacao = new Double[2];
    if (level.equals("1")) {
      node.fnAvaliacao[0] = calculateLevelEasyFunction(node.board, Color.BLACK);
      node.fnAvaliacao[1] = calculateLevelEasyFunction(node.board, Color.WHITE);
    } else if (level.equals("2")) {
      node.fnAvaliacao[0] = calculateLevelMediumFunction(node.board, Color.BLACK);
      node.fnAvaliacao[1] = calculateLevelMediumFunction(node.board, Color.WHITE);
    } else {
      node.fnAvaliacao[0] = calculateLevelHardFunction(node.board, Color.BLACK);
      node.fnAvaliacao[1] = calculateLevelHardFunction(node.board, Color.WHITE);
    }
  }

  private  Movement calculateMoveMinMaxPodaAB(Node node) {

    Movement bestPlay = null;

    Color color = node.playerColor;
    Board estate = node.board;

    // Atingiu profundidade maxima.
    if (isStopCondition(node)) {
      setLevel(node);
      return null;
    }

    // Percorre todas as posicoes vazias em busca de jogadas(Nodos filhos).
    int kidsNumber = 0;
    for (Position emptySpot : estate.getEmptySpots()) {

      Movement play = estate.calcuteMove(emptySpot, color);

      // Se tem jogada pela pos. vazia então expande para esse nodo filho.
      if (play != null) {

        Node son = new Node(node, play);

        // Caminha em profundidade na árvore.
        calculateMoveMinMaxPodaAB(son);

				// Apos calcular o valor do nodo filho verifica se o filho tem
        // um valor melhor p/ contribuir com o pai.
        if (node.fnAvaliacao == null
                || son.fnAvaliacao(color) > node.fnAvaliacao(color)) {
          node.fnAvaliacao = son.fnAvaliacao;
          bestPlay = play;
          bestPlay.evalScore = this.calculateLevelHardFunction(node.board, color);
        }

        // Poda. Se sim, para de buscar nodos filhos.
        if (shouldPrune(node)) {
          break;
        }

        kidsNumber++;
      }
    }

		// Se o nodo nao tem filhos, então é folha.
    // Para esse nodo então, calcula uma função de avaliação.
    if (kidsNumber == 0) {
      setLevel(node);
    }

    return bestPlay;
  }

  public Movement play(Board board, Color color) {    
    Movement bestPlay;
    Node root = new Node(board, color);

    int tilesNumber = board.calculteNumberTiles();
    double percentageDone
            = ((double) tilesNumber) / ((double) Board.SIZE * Board.SIZE);

    if (percentageDone <= 0.6) {
      MAX_DEPTH = DEFAUTL_DEPTH;
    } else {
      MAX_DEPTH = DEFAUTL_DEPTH + 1;
    }

    bestPlay = calculateMoveMinMaxPodaAB(root);
    if(bestPlay != null)
    	movementsMap.put(bestPlay.emptyPosition, bestPlay.evalScore);
    return bestPlay;
  }

Boolean flag = true;  
@Override
public Movement selectMovement(Board board, Color color) {
	if(flag){
		
    	this.level = JOptionPane.showInputDialog("Choose a level:\n"
            + "1 - Easy\n2 - Medium\n3 - Hard");

    	if (this.level.equals("1")) {
    		this.DEFAUTL_DEPTH = 5;
    	} 
    	else if (this.level.equals("2")) {
    		this.DEFAUTL_DEPTH = 5;
    	} 
    	else {
    		this.DEFAUTL_DEPTH = 6;
    	}


    	flag = false;
	}
	return this.play(board, color);
}

@Override
public void setSVMParameters(AI artificialInteligence, Map<Position, Double> movementsMade) {
	// TODO Auto-generated method stub
	
}


  
}
