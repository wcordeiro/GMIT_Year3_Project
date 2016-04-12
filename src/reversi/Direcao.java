package reversi;

import java.util.Arrays;
import java.util.List;

public enum Direcao {

	BAIXO, 
	CIMA,
	ESQUERDA,
	DIREITA,
	CIMA_DIREITA,
	CIMA_ESQUERDA,
	BAIXO_DIREITA,
	BAIXO_ESQUERDA;
	
	public static List<Direcao> getAll() {

		return Arrays.asList(Direcao.values());
	}
}
