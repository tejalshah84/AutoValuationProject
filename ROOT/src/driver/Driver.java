/**
 * @author Tejal Shah
 * This is the client driver that creates one client thread that handles the entire session.
 */
package driver;


import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import client.DefaultSocketClient;

public class Driver {

	public static void main(String [] args){

		InetAddress local = null;
		try {
			local = InetAddress.getLocalHost();
			Socket client = new Socket(local.getHostAddress(), 850);
			DefaultSocketClient ds = new DefaultSocketClient(client);
			ds.start();
			ds.join();
		} catch (UnknownHostException e){
			System.err.println ("Identity Crisis!");
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
