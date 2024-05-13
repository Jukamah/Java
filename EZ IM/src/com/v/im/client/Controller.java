package com.v.im.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.swing.JOptionPane;

import com.v.im.Main;

public class Controller {
	
	private Main client;
	
	private Socket socket;
	
	private BufferedReader reader;
	private PrintWriter writer;
	
	private boolean isConnected;
	
	public Controller(Main client) {
		this.client = client;
		
		socket = new Socket();
		isConnected = false;
	}

	// Attempt a connection, mainly used to initialize a client i.e. checking information and allowing or rejecting before finalizing
	public void attemptConnect(String displayName, String serverAddress, int serverPort) {
		if(isConnected)
			return;
		
		InetSocketAddress attemptAddress = new InetSocketAddress(serverAddress, serverPort);
		
		try {
			socket.connect(attemptAddress);
			
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(socket.getOutputStream(), true);
			
			writer.println(displayName);
			
			String read = reader.readLine();
			boolean isAccepted = read.equals("true");
			
			if(isAccepted) {
				client.setServerName(reader.readLine(), false);
				
				int lstUsersLen = Integer.parseInt(reader.readLine());
				
				for(int i = 0; i < lstUsersLen; i++) {
					String user = reader.readLine();
					
					if(user.equals(displayName))
						client.addUser(user);
					else
						client.addUser(user, true);
				}
				
				isConnected = true;
				new Thread(new Client(client, socket, reader, writer).setController(this)).start();
				client.goLive(displayName);
			} else {
				socket.close();
				reader.close();
				writer.close();
				
				JOptionPane.showMessageDialog(client, "A connection with that user name already exists.");
			}
			

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void closeConnection(int caseType) {
		if(!isConnected)
			return;
		
		isConnected = false;

		client.disconnectAsClient(caseType, null);
	}
	
	public void closeConnection(int caseType, String reason) {
		if(!isConnected)
			return;
		
		isConnected = false;

		client.disconnectAsClient(caseType, reason);
	}
}
