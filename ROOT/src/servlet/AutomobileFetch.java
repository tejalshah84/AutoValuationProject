/**
 * @author Tejal Shah
 * This class is a servlet that fetches a particular automobile from the database using
 * doGet method and then calls the AutoOptionList.jsp to display the available options
 * for that automobile. This servlet also contains the doPost method to handle the choices
 * is selected by the user in the AutoOptionList.jsp and process them to calculate the price.
 */

package servlet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import client.SelectCarOption;
import model.Automobile;


@WebServlet("/AutomobileFetch")
public class AutomobileFetch extends HttpServlet {
	private ObjectOutputStream toServer;
	private ObjectInputStream fromServer;
	private Socket sock;

	//Invoked when model selected in the ModelList.jsp form. It connects to server and retrieves
	//the automobile object specified by the user. Then it calls the AutoOptionList.jsp view
	//to display the available options for that automobile.
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		LinkedHashMap<String, Object> temp=null;
		if (openDBConnection()){
			SelectCarOption sco = new SelectCarOption();
			Properties p = sco.sendAutoRequest((request.getParameterValues("AutoList")[0]).replaceAll("_"," "));
			try {
				if(p!=null){
					toServer.writeObject(p);
					Automobile a = (Automobile) fromServer.readObject();
					closeDBConnection();
					if (a!=null){
						temp = processAutomobile(a);
						session.setAttribute("AutoName", temp.get("AutoName"));
						request.setAttribute("AutoName", temp.get("AutoName"));
						session.setAttribute("BasePrice", temp.get("BasePrice"));
						request.setAttribute("BasePrice", temp.get("BasePrice"));
						session.setAttribute("OptionSet", temp.get("OptionSet"));
						request.setAttribute("OptionSet", temp.get("OptionSet"));
						String[] os = (String[]) temp.get("OptionSet");
						for(int i=0; i<os.length;i++){
							session.setAttribute(os[i], temp.get(os[i]));
							request.setAttribute(os[i], temp.get(os[i]));
						}
						request.setAttribute("Error", "");
					}
					else {
						request.setAttribute("Error", "Automobile could not be found");
					}
				}
			}
			catch (IOException e){
				System.out.println("Error writing to client socket");
			} catch (ClassNotFoundException e){
				System.out.println("Error reading from client socket");
			} catch (NullPointerException e){
				System.out.println("Null Pointer");
			}

			if (temp!=null){
				RequestDispatcher dispatcher = request.getRequestDispatcher("AutoOptionList.jsp");
				dispatcher.forward(request, response);
			}
		}
	}

	//The doPost method is invoked when form is submitted with choice selection in AutoOptionList.jsp form.
	//When submitted, this method processes the choice selection and calculates the price.
	//The calculated price against options is displayed by calling DisplayTotal.jsp view.
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		Properties p = new Properties();
		Properties p1 = new Properties();
		float sum = 0;
		if(session!=null && request!=null){
			try{	
				Map<String, String[]> formData = request.getParameterMap();
		        Iterator<Entry<String, String[]>> it = formData.entrySet().iterator();
		        while (it.hasNext()) {
		           Entry<String, String[]> entry = (Entry<String, String[]>) it.next();
		            String paramName = ((String) entry.getKey()).split("=")[0];
		            paramName = paramName.substring(0, paramName.length()-2).replaceAll("_"," ");
		            String[] paramValues = (String[]) entry.getValue();
		            String choice = paramValues[0].replaceAll("_", " ");
		            p.setProperty(paramName, choice);
		            String price = ((Properties) session.getAttribute(paramName)).getProperty(choice);
					sum += Float.parseFloat(price);
					p1.setProperty(choice, String.format("%.0f", Float.parseFloat(price)));
		        }

				sum += (float) session.getAttribute("BasePrice");
				request.setAttribute("AutoName", session.getAttribute("AutoName"));
				request.setAttribute("BasePrice", (String.format("%.0f", (float)session.getAttribute("BasePrice"))));
				request.setAttribute("Choices", p);
				request.setAttribute("Prices", p1);
				request.setAttribute("Total Cost", String.format("%.0f", sum));
				request.setAttribute("Error", "");
			}
			catch (NullPointerException e){
				System.out.println("Null Pointer");
			} catch (NumberFormatException e){
				System.out.println("Number Format");
			}
		}
		else {
			request.setAttribute("Error", "Error occured while Automobile configuration");
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher("DisplayTotal.jsp");
		dispatcher.forward(request, response);
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

	//Method to help doGet method process the Automobile to make it easier to
	//display via AutoOptionList.jsp. It creates arrays and properties of options.
	public LinkedHashMap<String, Object> processAutomobile(Automobile a){
		LinkedHashMap<String, Object> temp = new LinkedHashMap<String, Object>();
		int len = a.getNumberOfOptions();
		String[] osNames = new String[len];
		temp.put("AutoName", a.getAutomobileName());
		temp.put("BasePrice", new Float(a.getBasePrice()));
		for(int i=0; i<len;i++){
			osNames[i] = a.getOptionSet(i);
			temp.put(osNames[i], a.getOptionValues(osNames[i]));
		}
		temp.put("OptionSet", osNames);
		return temp;
	}

}
