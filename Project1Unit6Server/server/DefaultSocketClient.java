/**
 * @author Tejal Shah
 * This class represents the thread that is created for each new user that logs in.
 * As instance of this class is created, this class's run method handles the user session.
 */
package server;

import java.net.*;
import java.util.Properties;
import java.io.*;

public class DefaultSocketClient extends Thread implements SocketClientInterface, SocketClientConstants {

	//Instance variables
	private ObjectOutputStream toClient;
	private ObjectInputStream fromClient;
	private Socket sock;
	private static int clientCount = 0;
	private int clientThreadId = 0;
	private boolean sockopen = false;

	//Constructor
	public DefaultSocketClient(Socket s) {       
		this.sock = s;
		clientCount++;
		clientThreadId = clientCount;
		System.out.println("Client Socket:" + clientThreadId);
		sockopen = openConnection();
	}

	//Thread's run method handles user session
	public void run(){
		if (sockopen){
			handleSession();
		}
		closeSession();
	}

	//Open Socket Object Streams to handle connections
	public boolean openConnection(){
		try {
			toClient = new ObjectOutputStream(sock.getOutputStream());
			fromClient = new ObjectInputStream(sock.getInputStream());
		}
		catch (Exception e){
			if (DEBUG) System.err.println("Error opening sock stream");
			return false;
		}
		return true;
	}

	//Main body of thread that executes all Auto functions
	public void handleSession(){
		AutoServer as = new BuildCarModelOptions();
		String opInput = "";
		Properties p;

		try {
			p = (Properties) fromClient.readObject();
			while (true){
				opInput = p.getProperty("Operation");
				
				//Build Auto Object from Property file
				if(opInput.equals("1")){
					sendOutput(String.valueOf(as.buildAuto(p, p.getProperty("filetype"))));
				}
				//Send Client list of all automobiles
				else if (opInput.equals("2")){
					System.out.println("sent list");
					sendOutput(as.listofAutomobiles());
				}
				//Send client requested automobile object
				else if(opInput.equals("3")){
					sendOutput(as.findAutoModel(p.getProperty("Get")));
				}
				//Client exiting
				else if(opInput.equals("E")){
					break;
				}
				System.out.println("Waiting for next request from client...");
				p = (Properties) fromClient.readObject();
				System.out.println("Received next request from client...");
			}
		}
		catch (IOException e){
			if (DEBUG) System.out.println ("Handling session");
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}       

	//Send client requested objects
	public void sendOutput(Object objOutput){
		try {
			if(objOutput!=null){
				toClient.writeObject(objOutput);
			}
		}
		catch (IOException e){
			if (DEBUG) System.out.println 
			("Error writing to client socket");
		}
	}      

	//Close client session
	public void closeSession(){
		try {
			toClient = null;
			fromClient = null;
			sock.close();
			System.out.println("Socket closed: " + clientThreadId);
		}
		catch (IOException e){
			if (DEBUG) System.err.println("Error closing socket");
		}       
	}

}
