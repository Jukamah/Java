package com.v.im.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

import com.v.im.Main;

public class UserHandler implements Runnable {
	
	private String userName;
	
	private final Controller controller;
	private final Socket clientSocket;
	
	private BufferedReader reader;
	private PrintWriter writer;
	
	private boolean isConnected, isInit;

	public UserHandler(Socket clientSocket, Controller controller) {
		
		this.controller = controller;
		this.clientSocket = clientSocket;
		
		isConnected = false;
		isInit = false;
		
		try {
			reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			writer = new PrintWriter(clientSocket.getOutputStream(), true);
			
			initUser();
		} catch(IOException e) {
			close();
		}
	}
	
	// Same IOException from reader/writer so we can catch it on that call in the constructor
	public void initUser() throws IOException {
		// Read the attempted user name and if unique accept the user else reject
		String tryName;
			
		tryName = reader.readLine();
			
		List<User> lstUsers = controller.getUsers();
		boolean isUnique = true;

		for(User user : lstUsers) {
			if(user.getName().equals(tryName))
				isUnique = false;	
		}
			
		writer.println(isUnique);
			
		if(isUnique) {
			userName = tryName;
			controller.acceptUser(this);
				
			isConnected = true;
		} else
			controller.rejectUser(this);
		
		isInit = true;
	}
	
	public void reject() {
		try {
			reader.close();
			writer.close();
			clientSocket.close();
			
			isConnected = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			clientSocket.close();
		} catch(IOException e) {
			
		}
	}
	
	public void close(int caseType) {
		try {			
			switch(caseType) {
				case(Main.CASE_NORMAL):
					controller.removeUser(this, false);
				break;
				
				case(Main.CASE_SHUTDOWN):
					writer.println("close.shutdown");
				break;
				
				case(Main.CASE_KICKED):
					controller.removeUser(this, true);
				break;
			}
				
			clientSocket.close();
			reader.close();
			writer.close();
		} catch(IOException e) {
			
		}
	}
	
	public void kickUser(String reason) {
		writer.println("close.kick");
		writer.println(reason);
		
		close(Main.CASE_KICKED);
	}
	
	public String getName() {
		return userName;
	}
	
	// We can use isInit to see if the user has connected for the first time
	//		as the users are read on connection and subsequent reads are following an "update" command
	public void writeUpdateUsers(List<User> lstUsers) {
		if(isInit)
			writer.println("update");
		
		int len = lstUsers.size();
		
		writer.println(len);
		
		for(User user : lstUsers) {
			writer.println(user);
		}
	}
	
	// Write the <User> list to update as well as the user that was lost as well as if they were kicked
	public void writeUpdateUsers(List<User> lstUsers, String lostUser, boolean isKicked) {
		writer.println("update");
		
		int len = lstUsers.size();
		
		writer.println(len);
		
		writer.println(lostUser);
		
		if(isKicked)
			writer.println("true");
		else
			writer.println("false");
		
		for(User user : lstUsers) {
			writer.println(user);
		}
	}
	
	public void writeServerName(String serverName) {
		writer.println(serverName);
	}
	
	public void writeMessage(String fromUser, String msg) {
		writer.println("message");
		
		writer.println(fromUser);
		
		writer.println(msg);
	}
	
	public void writeMessage(String msg) {
		writer.println("server");
		
		writer.println(msg);
	}
	
	@Override
	public void run() {		
		while(isConnected) {
			try {
				String cmd = reader.readLine();
				
				switch(cmd) {
					case("message"):
						String msg = reader.readLine();
						
						controller.attemptMessage(userName, msg);
					break;
				
					case("close"):
						close(Main.CASE_NORMAL);
					break;
				}
			} catch(SocketException e) {
				reject();
				
				controller.removeUser(this, false);
			} catch(IOException e) {
				
			} catch(NullPointerException e) {
				
			}
		}
	}
}
