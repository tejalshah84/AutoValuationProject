/**
 * @author Tejal Shah
 * This class represents the client thread. It handles the client session.
 */
package client;

import java.net.*;
import java.util.Properties;
import model.Automobile;

import java.io.*;

public class DefaultSocketClient extends Thread implements SocketClientInterface, SocketClientConstants {

	//Instance variables
	private ObjectOutputStream toServer;
	private ObjectInputStream fromServer;
	private Socket sock;
	private boolean sockopen = false;

	//Constructor
	public DefaultSocketClient(Socket s) {       
		this.sock = s;
		System.out.println("Client Socket connected...");
		sockopen =  openConnection();
	}

	//Run method of client thread
	public void run(){
		if (sockopen){
			handleSession();
		}
		closeSession();
	}

	//Method that creates object streams for communication with server
	public boolean openConnection(){
		try {
			toServer = new ObjectOutputStream(sock.getOutputStream());
			fromServer = new ObjectInputStream(sock.getInputStream());
		}
		catch (Exception e){
			if (DEBUG) System.err.println("Error opening sock stream");
			return false;
		}
		return true;
	}

	//Method that handles the various features that are available to the user
	public void handleSession(){
		CarModelOptionsIO cmio = new CarModelOptionsIO();
		SelectCarOption sco = new SelectCarOption();
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		String selection = "";
		cmio.displayMenu();

		while(true){
			
			try {
				selection = input.readLine();
				
				if(selection.equals("1")){
					Properties p = cmio.uploadConfigFile();
					if (p!=null){
						sendOutput(p);
						String success = (String) fromServer.readObject();
						if (success.equals("true")) 
							System.out.println("Automobile " + p.getProperty("CarName") + " configured successfully" + "\n");
						else 
							System.out.println("Error Occured while configuring Automobile" + p.getProperty("CarName"));
					}
				}
				else if(selection.equals("2")){
					Properties p = cmio.listofAutomobiles();
					sendOutput(p);
					p = (Properties) fromServer.readObject();
					p = cmio.requestAutomobile(p);
					if (p!=null){
						sendOutput(p);
						Automobile a = (Automobile) fromServer.readObject();
						sco.configureModel(a);
					}
				}
				else if(selection.equals("E")){
					Properties p = new Properties();
					p.setProperty("Operation","E");
					sendOutput(p);
					break;
				}
				else {
					System.out.println("Incorrect selection, try again");
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println();
			System.out.println("What would you like to do next?");
			cmio.displayMenu();
		}

	}       

	//Send objects to server
	public void sendOutput(Properties objOutput){
		try {
			if(objOutput!=null){
				toServer.writeObject(objOutput);
			}
		}
		catch (IOException e){
			if (DEBUG) System.out.println("Error writing to client socket");
		}
	}      

	//Close client session
	public void closeSession(){
		try {
			toServer = null;
			fromServer = null;
			sock.close();
		}
		catch (IOException e){
			if (DEBUG) System.err.println("Error closing socket");
		}       
	}

}
