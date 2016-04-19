package util;

import java.util.Scanner;

public class Util {

	public static int readInteger(int min, int max) {
		
		Scanner scanner = new Scanner(System.in);
		String valueReaded;
		int value = -1;
		
		while(value < min || value > max) {

			// Lê a linha que o usuario entrou.
			valueReaded = scanner.nextLine();

			if (Util.isInteger(valueReaded))
				value = Integer.parseInt(valueReaded);
		}
		
		return value;
	}
	
	public static void readKey() {
		
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
	
	public static void separator() {
		System.out.println("-------------------------");
	}
}
