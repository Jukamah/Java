package com.v.im.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import com.v.im.Main;

public class Client implements Runnable {
	
	private Main client;
	
	private Controller controller;
	
	private Socket socket;
	private BufferedReader reader;
	private PrintWriter writer;
	
	private boolean isConnected;
	
	public Client(Main client, Socket socket, BufferedReader reader, PrintWriter writer) {
		this.client = client;
		
		this.socket = socket;
		this.reader = reader;
		this.writer = writer;
		
		isConnected = true;
		
		client.setController(this);
	}
	
	
	// Different disconnection types will be handled different for the purpose of 
	//													needing to know if we need to do something
	//													as well as for the UI itself
	public void close(int caseType, String sub) {
		try {
			isConnected = false;			
			
			switch(caseType) {
				case(Main.CASE_NORMAL):
					sendCommand("close");
					controller.closeConnection(caseType);
				break;
				
				case(Main.CASE_SHUTDOWN):
					controller.closeConnection(caseType);
				break;
				
				case(Main.CASE_KICKED):
					controller.closeConnection(caseType, sub);
				break;
			}
				
			socket.close();
			reader.close();
			writer.close();
	
			reader = null;
			writer = null;
			socket = null;
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public Client setController(Controller controller) {
		this.controller = controller;
		
		return this;
	}
	
	public int getPort() {
		return socket.getPort();
	}
	
	public void sendCommand(String cmd) {
		writer.println("close");
	}
	
	public void sendMessage(String msg) {
		writer.println("message");
		
		writer.println(msg);
	}
	
	@Override
	public void run() {
		while(isConnected) {
			try {
				String cmd = reader.readLine();

				switch(cmd) {
					case("message"):
						String fromUser = reader.readLine();
						String msg = reader.readLine();
						
						client.receiveMessage(fromUser, msg);
					break;
					
					case("close.shutdown"):
						close(Main.CASE_SHUTDOWN, null);
					break;
					
					case("close.kick"):
						String reason = reader.readLine();
						
						close(Main.CASE_KICKED, reason);
					break;
					
					case("update"):
						int len = Integer.parseInt(reader.readLine());
						String[] newUsers = new String[len];
						
						// If the new array of users length exceeds our historic someone has connected
						//											if it is less someone has disconnected
						if(len > client.getUserCount()) {
							for(int i = 0; i < len; i++) {
								newUsers[i] = reader.readLine();
							}
						
							client.updateUsers(newUsers);
						} else {
							String lostUser = reader.readLine();
							
							boolean isKicked = reader.readLine().equals("true");
							
							for(int i = 0; i < len; i++) {
								newUsers[i] = reader.readLine();
							}
						
							client.updateUsers(newUsers, lostUser, isKicked);
						}
					break;
				}
			} catch(IOException e) {
				controller.closeConnection(Main.CASE_UNEXPECTED);
			} catch(NullPointerException e) {
				
			}
		}
	}

}
