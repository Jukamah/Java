package net;
/*	Client.java
 * 	EDIT:	7/8/2022
 * 	DESC:	Thread for Socket type, connect, send, and receieve demonstration
 * */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends Thread {
	
	private Socket socket;
	
	private BufferedWriter writer;
	private BufferedReader reader;
	
	// Integer to write to server for handling
	private int A = 0;
	
	public Client(int A) {
		//System.out.println("CLIENT :: CONSTRUCTED\n");
		
		this.A = A;
	}

	@Override
	public void run() {
		try {
			// Try to connect
			socket = new Socket("localhost", 4999);
			//System.out.println("CLIENT :: CONNECTION ATTEMPT");
			
			// Connected successfully 
			if(socket.isConnected())
				//System.out.println("CLIENT :: CONNECTION SUCCESS");
			
			// Write to server
			writer = new BufferedWriter(new PrintWriter(socket.getOutputStream()));

			writer.write(A);
			writer.flush();
			//System.out.println("CLIENT :: WROTE TO SERVER :: " + A);
			
			// Read from server
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			int ret = reader.read();

			//System.out.println("CLIENT :: READ FROM SERVER :: " + ret);
			
			System.out.println("Input\t:: int A\t= " + A);
			System.out.println("Output\t:: int ret\t= " + ret);
		} catch (IOException e) {
			System.out.println("CLIENT :: CATCH :: " + e);
		}
	}
}
