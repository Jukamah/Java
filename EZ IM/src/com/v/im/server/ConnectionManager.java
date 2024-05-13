package com.v.im.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionManager implements Runnable {
	
	private final Controller serverController;
	private final ServerSocket serverSocket;

	public ConnectionManager(Controller serverController, ServerSocket serverSocket) {
		this.serverController = serverController;
		this.serverSocket = serverSocket;
	}
	
	// Thread to continuously accept sockets as long as the <ServerSocket> is open
	
	@Override
	public void run() {
		boolean isOpen = serverController.isOpen();
		
		while(isOpen) {
			try {
				Socket clientSocket = serverSocket.accept();				
				
				serverController.initUser(clientSocket);
			} catch(IOException e) {
				isOpen = false;
				
				serverController.close();
			}
			
			// Update continuously as the initial is not inside the loop
			isOpen = serverController.isOpen();
		}
	}
}
