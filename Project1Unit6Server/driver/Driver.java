/**
 * @author Tejal Shah
 *This is the driver class responsible for taking config file and converting it to Automobile object
 *and then serializing and deserializing it. Finally, the object state is printed. This class only
 *interacts with Automobile class and FileIO class.
 */
package driver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import adapter.BuildAuto;
import adapter.CreateAuto;
import adapter.UpdateAuto;
import database.DBSetup;
import server.DefaultSocketClient;

public class Driver {

	private static boolean keepRunning = true;
	
	//Method to register DB driver and setup DB with schema and tables
	private static void initializeDBConn(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} 
		
		DBSetup temp = new DBSetup();
		temp.loadDBSetupFile();
		temp.setupDB();
	} 
	
	public static void main(String [] args){
		CreateAuto ca = new BuildAuto();
		UpdateAuto ua = new BuildAuto();
		
		//Setup DB
		initializeDBConn();
		
		//Execute CRUD operations
		ca.buildAuto("FocusWagonZTW.txt", "txt");
		ua.updateOptionSetName("Focus Wagon ZTW", "Color", "Shade");
		ua.updateOptionPrice("Focus Wagon ZTW", "Shade", "Infra-Red Clearcoat", 15);
		ua.updateAutoName("Focus Wagon ZTW", "Focus Wagon");
		ua.removeAuto("Focus Wagon");
		
		//Run server to listen for client connections
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(850);
			System.out.println("Server Started....");
		} catch (IOException e) {
			System.err.println("Could not listen on port: 850.");
			System.exit(1);
		}

		while (keepRunning){
			DefaultSocketClient clientSocket = null;
			try {
				System.out.println("Waiting for new client connection....");
				Socket socket = serverSocket.accept();
				clientSocket = new DefaultSocketClient(socket);
				clientSocket.start();
				System.out.println("Client thread created.....");

			} catch (IOException e) {
				System.err.println("Client Connection Accept failed.");
				System.exit(1);
			}
		}
	    
	    try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	private static void stopServer(){
		keepRunning = false;
	}
}
