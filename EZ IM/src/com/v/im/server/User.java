package com.v.im.server;

public class User {
	
	private String displayName;
	
	public UserHandler userHandler;
	
	private boolean isServer;
	
	public User(UserHandler userHandler) {
		this.displayName = userHandler.getName();
		this.userHandler = userHandler;
		
		isServer = false;
	}
	
	// For when the user is the server client
	public User(String displayName) {
		this.displayName = displayName;
		
		isServer = true;
	}
	
	public String getName() {
		return displayName;
	}
	
	public boolean isServer() {
		return isServer || (userHandler == null);
	}
	
	@Override
	public String toString() {
		return displayName;
	}

}
