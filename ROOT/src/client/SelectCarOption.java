/**
 * @author Tejal Shah
 * This class handles the whole option configuration part once an automobile has been 
 * received from the server. The class prompts users for their choice and calculates price
 * for it without going back to the server by using local copy of automobile object.
 */
package client;

import java.util.Properties;
import java.util.Scanner;

import adapter.AutoChoices;
import adapter.BuildAuto;
import adapter.CreateAuto;
import model.Automobile;

public class SelectCarOption {
	CreateAuto ca = new BuildAuto();
	AutoChoices ac = new BuildAuto();
	private Scanner input;

	//Use configured automobile object to determine choice and calculate price
	public void configureModel(Automobile a){
		if(a!=null){
			if(ca.addAuto(a)){
				int t = ac.getNumberofOptions(a.getAutomobileName());
				getUserChoice(t, a.getAutomobileName());
				System.out.println("For below listed selectons for " + a.getAutomobileName() + ": ");
				System.out.print("Total Price:");
				ac.calculateTotalPrice(a.getAutomobileName());
				System.out.println("Base Price:" + String.format("%.0f", a.getBasePrice()));
				ac.printChoices(a.getAutomobileName());
			}
		}
	}

	//Get user choice via console input and store in automobile object locally
	public void getUserChoice(int len, String modelName){
		input = new Scanner(System.in);
		Properties choices = new Properties();
		int c = 0;

		System.out.println("Configure Options for Automobile: " + modelName);

		for(int i=0; i<len; i++){
			String osName = ac.getOptionSetName(modelName, i);
			String[] opts = ac.getOptionValues(modelName, osName);
			System.out.println("Select " + osName + " Options: " );

			for(int j=0; j<opts.length; j++){
				System.out.println((j+1) + ": " + opts[j]); 
			}

			String choice = input.nextLine();
			c = Integer.parseInt(choice);

			while (c<=0 || c>opts.length){
				System.out.println("Incorrect choice selection, enter number between 1 and " + opts.length);
				choice = input.nextLine();
				c = Integer.parseInt(choice);	
			}
			choices.setProperty(osName, opts[c-1]);
		}

		ac.setChoice(modelName, choices);
	}
	
	public Properties sendAutoRequest(String autoName){
		Properties temp = new Properties();
		temp.setProperty("Get", autoName);
		temp.setProperty("Operation", "3");
		return temp;
	}
}
