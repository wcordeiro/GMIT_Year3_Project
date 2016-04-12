package util;

import java.util.Scanner;

public class Util {

	public static int leInteiro(int min, int max) {
		
		Scanner scanner = new Scanner(System.in);
		String valorLido;
		int valorInteiro = -1;
		
		while(valorInteiro < min || valorInteiro > max) {

			// Lê a linha que o usuario entrou.
			valorLido = scanner.nextLine();

			if (Util.isInteger(valorLido))
				valorInteiro = Integer.parseInt(valorLido);
		}
		
		return valorInteiro;
	}
	
	public static void leTecla() {
		
		Scanner scanner = new Scanner(System.in);
		scanner.nextLine();
	}
	
	public static boolean isInteger(String string) {
	    try {
	        Integer.parseInt(string.trim()); 
	    } 
	    catch(NumberFormatException ex) { 
	        return false; 
	    }

	    return true;
	}
	
	public static void separador() {
		System.out.println("-------------------------");
	}
}
