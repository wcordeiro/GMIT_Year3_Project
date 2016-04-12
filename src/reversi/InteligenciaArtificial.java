package reversi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class InteligenciaArtificial {
  public Cor COR_PRIMEIRO_JOGADOR = Cor.PRETO;
  public Integer PROFUNDIDADE_PADRAO = 5;
  public Integer PROFUNDIDADE_MAX = PROFUNDIDADE_PADRAO;
  public Cor corHumano;
  public Tabuleiro tabuleiro;
  public String dificuldade;
  Map<Posicao,Double> movementsMap = new HashMap<Posicao,Double>();
  
  private  boolean isCondicaoParada(Nodo nodo) {
    return nodo.profundidade >= PROFUNDIDADE_MAX;
  }

  public  double calculaFuncaoAvaliacaoDificil(Tabuleiro grid, Cor my_color) {
    int my_tiles = 0, opp_tiles = 0, i, j, k, my_front_tiles = 0, opp_front_tiles = 0, x, y;
    double p = 0, c = 0, l = 0, m = 0, f = 0, d = 0;
    Cor opp_color;
    opp_color = Cor.getCorOposta(my_color);
    Posicao posicao = new Posicao(-1, -1);
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
        posicao.y = j;
        posicao.x = i;
        if (grid.getCor(posicao) == my_color) {
          d += V[i][j];
          my_tiles++;
        } else if (grid.getCor(posicao) == opp_color) {
          d -= V[i][j];
          opp_tiles++;
        }
        if (grid.getCor(posicao) != Cor.VAZIO) {
          for (k = 0; k < 8; k++) {
            x = i + X1[k];
            y = j + Y1[k];
            if (x >= 0 && x < 8 && y >= 0 && y < 8 && grid.getCor(posicao) == Cor.VAZIO) {
              if (grid.getCor(posicao) == my_color) {
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
    if (grid.getCor(new Posicao(0, 0)) == my_color) {
      my_tiles++;
    } else if (grid.getCor(new Posicao(0, 0)) == opp_color) {
      opp_tiles++;
    }
    if (grid.getCor(new Posicao(0, 7)) == my_color) {
      my_tiles++;
    } else if (grid.getCor(new Posicao(0, 7)) == opp_color) {
      opp_tiles++;
    }
    if (grid.getCor(new Posicao(7, 0)) == my_color) {
      my_tiles++;
    } else if (grid.getCor(new Posicao(7, 0)) == opp_color) {
      opp_tiles++;
    }
    if (grid.getCor(new Posicao(7, 7)) == my_color) {
      my_tiles++;
    } else if (grid.getCor(new Posicao(7, 7)) == opp_color) {
      opp_tiles++;
    }
    c = 25 * (my_tiles - opp_tiles);
// Corner closeness
    my_tiles = opp_tiles = 0;
    if (grid.getCor(new Posicao(0, 0)) == Cor.VAZIO) {
      if (grid.getCor(new Posicao(0, 1)) == my_color) {
        my_tiles++;
      } else if (grid.getCor(new Posicao(0, 1)) == opp_color) {
        opp_tiles++;
      }
      if (grid.getCor(new Posicao(1, 1)) == my_color) {
        my_tiles++;
      } else if (grid.getCor(new Posicao(1, 1)) == opp_color) {
        opp_tiles++;
      }
      if (grid.getCor(new Posicao(1, 0)) == my_color) {
        my_tiles++;
      } else if (grid.getCor(new Posicao(1, 0)) == opp_color) {
        opp_tiles++;
      }
    }
    if (grid.getCor(new Posicao(0, 7)) == Cor.VAZIO) {
      if (grid.getCor(new Posicao(0, 6)) == my_color) {
        my_tiles++;
      } else if (grid.getCor(new Posicao(0, 6)) == opp_color) {
        opp_tiles++;
      }
      if (grid.getCor(new Posicao(1, 6)) == my_color) {
        my_tiles++;
      } else if (grid.getCor(new Posicao(1, 6)) == opp_color) {
        opp_tiles++;
      }
      if (grid.getCor(new Posicao(1, 7)) == my_color) {
        my_tiles++;
      } else if (grid.getCor(new Posicao(1, 7)) == opp_color) {
        opp_tiles++;
      }
    }
    if (grid.getCor(new Posicao(7, 0)) == Cor.VAZIO) {
      if (grid.getCor(new Posicao(7, 1)) == my_color) {
        my_tiles++;
      } else if (grid.getCor(new Posicao(7, 1)) == opp_color) {
        opp_tiles++;
      }
      if (grid.getCor(new Posicao(6, 1)) == my_color) {
        my_tiles++;
      } else if (grid.getCor(new Posicao(6, 1)) == opp_color) {
        opp_tiles++;
      }
      if (grid.getCor(new Posicao(6, 0)) == my_color) {
        my_tiles++;
      } else if (grid.getCor(new Posicao(6, 0)) == opp_color) {
        opp_tiles++;
      }
    }
    if (grid.getCor(new Posicao(7, 7)) == Cor.VAZIO) {
      if (grid.getCor(new Posicao(6, 7)) == my_color) {
        my_tiles++;
      } else if (grid.getCor(new Posicao(6, 7)) == opp_color) {
        opp_tiles++;
      }
      if (grid.getCor(new Posicao(6, 6)) == my_color) {
        my_tiles++;
      } else if (grid.getCor(new Posicao(6, 6)) == opp_color) {
        opp_tiles++;
      }
      if (grid.getCor(new Posicao(7, 6)) == my_color) {
        my_tiles++;
      } else if (grid.getCor(new Posicao(7, 6)) == opp_color) {
        opp_tiles++;
      }
    }
    l = -12.5 * (my_tiles - opp_tiles);
// Mobility
    List<Jogada> jogadasPossiveis = grid.calculaJogadasPossiveisJogador(my_color);
    my_tiles = opp_tiles = 0;
    if (jogadasPossiveis != null) {
      my_tiles = jogadasPossiveis.size();
    }
    jogadasPossiveis = grid.calculaJogadasPossiveisJogador(opp_color);
    if (jogadasPossiveis != null) {
      opp_tiles = jogadasPossiveis.size();
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

  private  double calculaFuncaoAvaliacaoMedio(Tabuleiro grid, Cor my_color) {
    int my_tiles = 0, opp_tiles = 0, i, j, k, my_front_tiles = 0, opp_front_tiles = 0, x, y;
    double p = 0, c = 0, l = 0, f = 0, d = 0;
    Cor opp_color;
    opp_color = Cor.getCorOposta(my_color);
    Posicao posicao = new Posicao(-1, -1);
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
        posicao.y = j;
        posicao.x = i;
        if (grid.getCor(posicao) == my_color) {
          d += V[i][j];
          my_tiles++;
        } else if (grid.getCor(posicao) == opp_color) {
          d -= V[i][j];
          opp_tiles++;
        }
        if (grid.getCor(posicao) != Cor.VAZIO) {
          for (k = 0; k < 8; k++) {
            x = i + X1[k];
            y = j + Y1[k];
            if (x >= 0 && x < 8 && y >= 0 && y < 8 && grid.getCor(posicao) == Cor.VAZIO) {
              if (grid.getCor(posicao) == my_color) {
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
    if (grid.getCor(new Posicao(0, 0)) == my_color) {
      my_tiles++;
    } else if (grid.getCor(new Posicao(0, 0)) == opp_color) {
      opp_tiles++;
    }
    if (grid.getCor(new Posicao(0, 7)) == my_color) {
      my_tiles++;
    } else if (grid.getCor(new Posicao(0, 7)) == opp_color) {
      opp_tiles++;
    }
    if (grid.getCor(new Posicao(7, 0)) == my_color) {
      my_tiles++;
    } else if (grid.getCor(new Posicao(7, 0)) == opp_color) {
      opp_tiles++;
    }
    if (grid.getCor(new Posicao(7, 7)) == my_color) {
      my_tiles++;
    } else if (grid.getCor(new Posicao(7, 7)) == opp_color) {
      opp_tiles++;
    }
    c = 25 * (my_tiles - opp_tiles);
    // final weighted score
    double score = (10 * p) + (801.724 * c) + (382.026 * l) + (74.396 * f) + (10 * d);
    return score;
  }

  private  Double calculaFuncaoAvaliacaoFacil(Tabuleiro estado, Cor cor) {

    // Calcula o número de peças do jogador no tabuleiro.
    Integer numeroPecas = 0;
    Posicao posicao = new Posicao(-1, -1);

    for (int y = 0; y < Tabuleiro.TAMANHO; y++) {
      for (int x = 0; x < Tabuleiro.TAMANHO; x++) {
        posicao.y = y;
        posicao.x = x;

        if (estado.getCor(posicao) == cor) {
          numeroPecas++;
        }
      }
    }

    // Peças seguras
    Integer numQuinas = estado.calculaNumeroQuinas(cor);
    Double result = (numeroPecas.doubleValue() + numQuinas.doubleValue());
    return result;
  }

  private  boolean devePodar(Nodo nodo) {

    Cor corNo = nodo.corJogador;
    Cor corOposta = Cor.getCorOposta(corNo);
    Nodo avaliado = nodo.antecessor;

    /* Caminha par cima na árvore para verificar se pode realizar a poda.*/
    while (avaliado != null) {

      if (avaliado.fnAvaliacao != null) {
        if (avaliado.corJogador == corOposta) {
          if (avaliado.fnAvaliacao(corOposta) > nodo.fnAvaliacao(corOposta)) {
            return true;
          }
        }
      }

      avaliado = avaliado.antecessor;
    }

    return false;
  }

  private  void setaFuncaoAvaliacaoNodo(Nodo nodo) {
    nodo.fnAvaliacao = new Double[2];
    if (dificuldade.equals("1")) {
      nodo.fnAvaliacao[0] = calculaFuncaoAvaliacaoFacil(nodo.estado, Cor.PRETO);
      nodo.fnAvaliacao[1] = calculaFuncaoAvaliacaoFacil(nodo.estado, Cor.BRANCO);
    } else if (dificuldade.equals("2")) {
      nodo.fnAvaliacao[0] = calculaFuncaoAvaliacaoMedio(nodo.estado, Cor.PRETO);
      nodo.fnAvaliacao[1] = calculaFuncaoAvaliacaoMedio(nodo.estado, Cor.BRANCO);
    } else {
      nodo.fnAvaliacao[0] = calculaFuncaoAvaliacaoDificil(nodo.estado, Cor.PRETO);
      nodo.fnAvaliacao[1] = calculaFuncaoAvaliacaoDificil(nodo.estado, Cor.BRANCO);
    }
  }

  private  Jogada calculaJogadaMinMaxPodaAB(Nodo nodo) {

    Jogada melhorJogada = null;

    Cor corJogador = nodo.corJogador;
    Tabuleiro estado = nodo.estado;

    // Atingiu profundidade maxima.
    if (isCondicaoParada(nodo)) {
      setaFuncaoAvaliacaoNodo(nodo);
      return null;
    }

    // Percorre todas as posicoes vazias em busca de jogadas(Nodos filhos).
    int totalFilhos = 0;
    for (Posicao lugarVazio : estado.getPosicoesVazias()) {

      Jogada jogada = estado.calculaJogada(lugarVazio, corJogador);

      // Se tem jogada pela pos. vazia então expande para esse nodo filho.
      if (jogada != null) {

        Nodo filho = new Nodo(nodo, jogada);

        // Caminha em profundidade na árvore.
        calculaJogadaMinMaxPodaAB(filho);

				// Apos calcular o valor do nodo filho verifica se o filho tem
        // um valor melhor p/ contribuir com o pai.
        if (nodo.fnAvaliacao == null
                || filho.fnAvaliacao(corJogador) > nodo.fnAvaliacao(corJogador)) {
          nodo.fnAvaliacao = filho.fnAvaliacao;
          melhorJogada = jogada;
          melhorJogada.evalScore = this.calculaFuncaoAvaliacaoDificil(nodo.estado, corJogador);
        }

        // Poda. Se sim, para de buscar nodos filhos.
        if (devePodar(nodo)) {
          break;
        }

        totalFilhos++;
      }
    }

		// Se o nodo nao tem filhos, então é folha.
    // Para esse nodo então, calcula uma função de avaliação.
    if (totalFilhos == 0) {
      setaFuncaoAvaliacaoNodo(nodo);
    }

    return melhorJogada;
  }

  public Jogada play(Tabuleiro tabuleiro, Cor cor) {    
    Jogada melhorJogada;
    Nodo raiz = new Nodo(tabuleiro, cor);

    int totalPecas = tabuleiro.calculaTotalPecas();
    double porcentagemPreenchida
            = ((double) totalPecas) / ((double) Tabuleiro.TAMANHO * Tabuleiro.TAMANHO);

    if (porcentagemPreenchida <= 0.6) {
      PROFUNDIDADE_MAX = PROFUNDIDADE_PADRAO;
    } else {
      PROFUNDIDADE_MAX = PROFUNDIDADE_PADRAO + 1;
    }

    melhorJogada = calculaJogadaMinMaxPodaAB(raiz);
    if(melhorJogada != null)
    	movementsMap.put(melhorJogada.posicaoVazia, melhorJogada.evalScore);
    return melhorJogada;
  }
  
}
