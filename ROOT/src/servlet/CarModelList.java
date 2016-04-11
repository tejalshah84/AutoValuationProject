/**
 * @author Tejal Shah
 * This class is a servlet that fetches the list of all automobiles from the database
 * and displays it. Once the retrieval of the data is done, a request object is formed 
 * and passed to the ModelList.jsp view.
 */

package servlet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import client.CarModelOptionsIO;


@WebServlet("/CarModelList")
public class CarModelList extends HttpServlet {
	
	private ObjectOutputStream toServer;
	private ObjectInputStream fromServer;
	private Socket sock;
	
	//The doGet message connects to the server through socket and retireves
	//the list of all auto models and then forwards list to the ModelList.jsp view
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		if (openDBConnection()){
			CarModelOptionsIO cmoi = new CarModelOptionsIO();
			Properties p = cmoi.listofAutomobiles();
			try {
				if(p!=null){
					toServer.writeObject(p);
					p = (Properties) fromServer.readObject();
				}
			}
			catch (IOException e){
				System.out.println("Error writing to client socket");
			} catch (ClassNotFoundException e){
				System.out.println("Error reading from client socket");
			} catch (NullPointerException e){
				System.out.println("Null Pointer");
			}
			closeDBConnection();
			if (p!=null){
				p.remove("MaxNum");
				request.setAttribute("AutoList", p);
		        RequestDispatcher dispatcher = request.getRequestDispatcher("ModelList.jsp");
		        dispatcher.forward(request, response);
			}
		}
	}

	//There is no need for post method, it just reroutes to get method
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	//Method used by doGet to connect and communicate with server
	//Default socket client is not used because there is no need to create a new thread
	//The web container automatically creates new threads for each request.
	private boolean openDBConnection(){
		InetAddress local = null;
		try {
			local = InetAddress.getLocalHost();
			sock = new Socket(local.getHostAddress(), 850);
			toServer = new ObjectOutputStream(sock.getOutputStream());
			fromServer = new ObjectInputStream(sock.getInputStream());
		} catch (UnknownHostException e){
			System.err.println ("Identity Crisis!");
			System.exit(0);
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e){
			System.err.println("Error opening sock stream");
			return false;
		}
		return true;
	}
	
	//Method to close connection with server. Ideally there would be no need to 
	//open and close connections. A connection pool would be available for use.
	public void closeDBConnection(){
		try {
			Properties x = new Properties();
			x.setProperty("Operation","E");	
			toServer.writeObject(x);
			toServer = null;
			fromServer = null;
			sock.close();
		}
		catch (IOException e){
			System.err.println("Error closing socket");
		}       
	}

}
