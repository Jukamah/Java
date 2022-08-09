package net;
import java.util.Scanner;

/*	Driver.java
 * 	EDIT:	7/8/2022
 * 	DESC:	Houses main thread used to demonstrate Client.java and Server.java 
 * 	NOTE:	Due to writing and factorial, any argument greater than 5 will result in an overflow 
 * 			(long can be used but this is solely to demonstrate two-way single Client to Server communication)
 * */

public class Driver {
	
	public static void main(String[] args) {
		System.out.print("Input integer to factorial: ");
		
		Scanner scanner = new Scanner(System.in);
		
		Server server = new Server();
		Client client = new Client(scanner.nextInt());
		
		server.start();
		client.start();
	}
	
	

}
