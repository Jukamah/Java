package com.v.im.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.v.im.Main;

public class Controller {
	
	private List<User> lstUsers;
	
	private boolean isOpen;
	
	private final Main server;
	
	private ServerSocket serverSocket;
	private ConnectionManager connectionManager;
	
	private String serverName;
	
	public Controller(Main server, String serverName, int port, String displayName) {
		this.serverName = serverName;
		lstUsers = new ArrayList<User>();
		
		isOpen = false;
		
		this.server = server;
		
		try {
			serverSocket = new ServerSocket();
			
			serverSocket.setReuseAddress(true);
			
			InetSocketAddress address = new InetSocketAddress(InetAddress.getLocalHost(), port);
			
			serverSocket.bind(address);
			
			String strPort = Integer.toString(serverSocket.getLocalPort());
			
			// Fill information for Main
			server.setServerName(serverName, true);
			server.sendToConsole("Server created on port "  + strPort, true);
			server.goLive(displayName);
			server.setController(this);
			
			addUser(displayName);
			
			isOpen = true;
			connectionManager = new ConnectionManager(this, serverSocket);
			new Thread(connectionManager).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		if(!isOpen)
			return;
		
		try {			
			for(User user : lstUsers) {
				if(!user.isServer())
					user.userHandler.close(Main.CASE_SHUTDOWN);
			}
			
			serverSocket.close();

			lstUsers.clear();
			
			connectionManager = null;
			
			isOpen = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Access internal boolean isOpen (Sockets will return true as connected if bound regardless of connection
	//										i.e. until closed they will remain open
	//												we can use this exception and boolean to handle unexpected disconnections
	public boolean isOpen() {
		return isOpen;
	}
	
	public int getPort() {
		return serverSocket.getLocalPort();
	}
	
	public List<User> getUsers() {
		return lstUsers;
	}
	
	// User and server will need to exchange information
	// Here can check if the user's display name is available
	public void initUser(Socket clientSocket) {
		new UserHandler(clientSocket, this);
	}
	
	// No exceptions were thrown and user passed all checks (unique name)
	public void acceptUser(UserHandler userHandler) {		
		addUser(userHandler);
		
		new Thread(userHandler).start();
	}
	
	// Close all I/O for the user (reader, writer, and socket)
	public void rejectUser(UserHandler userHandler) {
		userHandler.reject();
		
		userHandler = null;
	}
	
	// Add user by <String> User Name (This is solely for adding self to the user list when hosting
	//										as we do not need to send ourselves messages over a socket)
	public void addUser(String userName) {
		lstUsers.add(new User(userName));
		server.addUser(userName);
	}
	
	// Add user by their <UserHandler> User Handler
	public void addUser(UserHandler userHandler) {
		lstUsers.add(new User(userHandler));
		server.addUser(userHandler.getName());
		
		userHandler.writeServerName(serverName);
		
		for(User user : lstUsers) {
			if(!user.isServer())
				user.userHandler.writeUpdateUsers(lstUsers);
		}
	}
	
	// Remove a user by their handler
	public void removeUser(UserHandler userHandler, boolean isKicked) {
		String attemptUserName = userHandler.getName();
		User attemptUser = null;
		
		// Transforming list of type <User> to <String> via toString()
		//		then we can use contains() to check if the user is found
		//			if they aren't return as there is nothing left to do
		if(!lstUsers.stream().map(User::toString).toList().contains(attemptUserName))
			return;
		
		for(User user : lstUsers) {
			if(user.getName().equals(attemptUserName))
				attemptUser = user;
		}
		
		lstUsers.remove(attemptUser);
		
		// Transform <String>List to String[] (only iteration is done on this return so no other functionality is needed)
		server.updateUsers(lstUsers.stream().map(User::toString).toArray(size -> new String[lstUsers.size()]), attemptUserName, isKicked);
		
		// Tell remaining users a user has been removed (The above tells ourselves they have been removed if we are host)
		for(User user : lstUsers) {
			if(!user.isServer())
				user.userHandler.writeUpdateUsers(lstUsers, attemptUserName, false);
		}
		
		// Release
		userHandler = null;
	}

	// Send a message to clients and who its sender is
	public void sendMessageToClients(String fromUser, String msg) {
		for(User user : lstUsers) {
			if(!user.isServer())
				user.userHandler.writeMessage(fromUser, msg);
		}
		
		server.receiveMessage(fromUser, msg);
	}
	
	// Attempt to send a message (server-side mute check will happen here
	//								client-side will receive the message but filter then)
	public void attemptMessage(String userName, String str) {
		sendMessageToClients(userName, str);
	}
	
	// When receiving a message from said user their message will no longer be posted and echoed to connected clients
	//															they will also be notified when they are muted
	//															and on every message they try sending
	public void muteUser(String userID) {
		
	}
	
	// Overload if given a reason (reason displayed to client in a dialog)
	public void muteUser(String userID, String reason) {
		
	}
	
	// Unmutes user, allowing future messages to be echoed back to clients
	public void unmuteUser(String userID) {
		
	}
	
	// Closes users socket after sending them the kick (They will be notified with a dialog)
	public void kickUser(String userID) {
		UserHandler userHandler = null;
		
		for(User user : lstUsers) {
			if(user.getName().equals(userID))
				userHandler = user.userHandler;
		}
		
		userHandler.kickUser("None");
	}
	
	// Overload if given a reason
	public void kickUser(String userID, String reason) {
		UserHandler userHandler = null;
		
		for(User user : lstUsers) {
			if(user.getName().equals(userID))
				userHandler = user.userHandler;
		}
		
		userHandler.kickUser(reason);
	}
}
