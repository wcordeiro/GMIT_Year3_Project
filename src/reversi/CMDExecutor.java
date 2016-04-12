package reversi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CMDExecutor {
	
	

	public CMDExecutor() {
		super();
		// TODO Auto-generated constructor stub
	}

	public synchronized String execCommand(final String commandLine) throws IOException {  
		  
	    boolean success = false;  
	    String result = new String();  
	  
	    Process p;  
	    BufferedReader input;  
	    StringBuffer cmdOut = new StringBuffer();  
	    String lineOut = null;  
	    int numberOfOutline = 0;  
	    //Boolean cdCommand = false;
	  
	    try {  
	  
	        p = Runtime.getRuntime().exec(commandLine);  
	  
	        input = new BufferedReader(new InputStreamReader(p.getInputStream()));  
	  
	        while ((lineOut = input.readLine()) != null) {  
	            if (numberOfOutline > 0) {  
	                cmdOut.append("\n");  
	            }  
	            cmdOut.append(lineOut);  
	            numberOfOutline++;  
	        }  
	        
	        result = cmdOut.toString();  
	  
	        success = true;  
	  
	        input.close();  
	          
	    } catch (IOException e) {  
	        result = String.format("Falha ao executar comando %s. Erro: %s", commandLine, e.toString());  
	    }  
	  
	    // Throw exception  
	    if (!success) {  
	        throw new IOException(result);  
	    }  
	  
	    return result;  
	  
	}
}
