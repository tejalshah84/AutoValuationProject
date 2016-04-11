/**
 * @author Tejal Shah
 * This class handles all the functions that are available to the client session. It creates all the
 * objects that need to be sent to the server by getting user input and processing it.
 */
package client;

import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;


public class CarModelOptionsIO {
	
	private Scanner input;

	//Display main menu to the user
	public void displayMenu(){
		StringBuilder sb = new StringBuilder();
		sb.append("Please select an option to proceed:").append("\n");
		sb.append("1: Upload Properties File").append("\n");
		sb.append("2: Configure a Car").append("\n");
		sb.append("E: Exit").append("\n");
		System.out.println(sb.toString());
	}
	
	//Prompt user for filename to be uploaded to create Automobile object
	//Create Properties object from the file
	public Properties uploadConfigFile(){
		input = new Scanner(System.in);
		boolean correctFile = false;
		
		while(!correctFile){
			System.out.println("Please enter property filename for Automobile Setup:");
			String filename = input.nextLine();
			String filetype = filename.substring(filename.lastIndexOf(".") + 1);
			
			if (filetype.equals("prop")){
				Properties props= new Properties();
				try {
					File file = new File(filename);
					FileInputStream in = new FileInputStream(file);
					correctFile = true;
					props.load(in);
					in.close();
					props.setProperty("Operation", "1");
					props.setProperty("filetype", filetype);
					return props;
				} catch (FileNotFoundException e) {
					System.out.println("Incorrect File Name -- File not found");
					correctFile = false;
				}catch (IOException e) {
					e.printStackTrace();
					correctFile = false;
				}
			}
		}
		return null;
	}
	
	//Create Object requesting server for list of all automobiles
	public Properties listofAutomobiles(){
		Properties props = new Properties();
		props.setProperty("Operation", "2");
		return props;
	}
	
	//Prompt user to specify which automobile they would like to configure choices for
	//Create object to request server for specific automobile
	public Properties requestAutomobile(Properties p){
		input = new Scanner(System.in);
		Properties temp = new Properties();
		int selection = 0;
		int count = Integer.parseInt(p.getProperty("MaxNum"));
		
		if(count == 0){
			System.out.println("No Automobiles available for selection");
			return null;
		}
		
		for(int i=1; i<=count; i++){
			System.out.println(i + ": " + p.getProperty(String.valueOf(i)));
		}
		
		while (selection<=0 || selection>count){
			System.out.println("Please input a value between 1 and " + count + ", to select an Automobile: ");
			String select = input.nextLine();
			selection = Integer.parseInt(select);
		}
		
		temp.setProperty("Get", p.getProperty(String.valueOf(selection)));
		temp.setProperty("Operation", "3");
		return temp;	
	}
		
}
