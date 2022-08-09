package net;
/*	Server.java
 * 	EDIT:	7/8/2022
 * 	DESC:	Thread for ServerSocket type, also contains computational functions for demonstration
 * */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
	
	private ServerSocket server;
	private Socket client;
	
	private BufferedWriter writer;
	private BufferedReader reader;
	
	public Server() {
		//System.out.println("SERVER :: CONSTRUCTED\n");
	}
	
	@Override
	public void run() {
		try {
			// Socket stuff, open port for listening, wait for connection
			server = new ServerSocket(4999);
			//System.out.println("SERVER :: LISTENING");
			
			client = server.accept();
			//System.out.println("SERVER :: CLIENT CONNECTED\n");
			
			// Connected, time to read
			reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
			
			int temp = reader.read();
			//System.out.println("SERVER :: READ FROM CLIENT :: " + temp + "\n");
			
			// Read and computed, time to write
			writer = new BufferedWriter(new PrintWriter(client.getOutputStream()));
			
			writer.write(ServerFunc.mathFactorial(temp));
			writer.flush();
			//System.out.println("SERVER :: WRITE TO CLIENT :: " + ServerFunc.mathFactorial(temp) + "\n");
		} catch(IOException e) {
			System.out.println("SERVER :: CATCH :: " + e);
		}
	}
}

class ServerFunc {
	
	// Factorial (only integers) for demonstration (methods handled server-side)
	public static int mathFactorial(int in) {
		//System.out.println("FUNC :: FACTORIAL :: ATTEMPT");
		
		int temp;
		
		if(in > 0)
			temp = in;
		else
			temp = -1;
		
		//System.out.println("FUNC :: FACTORIAL :: IF :: PASSED WITH :: " + temp);
		
		for(int i = in - 1; i > 0; i--) {
			//System.out.println("FUNC :: FACTORIAL :: FOR :: i = " + i + " WITH :: " + temp);
			temp *= i;
		}
		
		//System.out.println("FUNC :: FACTORIAL :: FOR :: PASSED WITH :: " + temp + "\n");
		
		return temp;
	}
}
