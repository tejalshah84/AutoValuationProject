/**
 * @author Tejal Shah
 * This class inherits all methods from proxyauto and overrides the update optionset and option
 * value methods so they can be syncronized to handle multiple threads.
 * Additionally this class implements runnable and defines a run method that calls on the update
 * methods to execute said operations.
 */
package scale;

import adapter.ProxyAutomobile;
import model.Automobile;

public class EditOptions extends ProxyAutomobile implements Runnable, EditThread {
	
	//Instance variables
	private String modelName = "";
	private String currName = "", newName = "", optionSetName = "", optionName = "";
	private float newPrice = -1;
	private int operation = 0;
	
	//No Arg Constructor
	public EditOptions(){
	}
	
	//Constructor with args
	public EditOptions(String modelName){
		this.modelName = modelName;
	}
	
	//Constructor with args
	public EditOptions(String modelName, int op, String[] operands){
		this.modelName = modelName;
		this.operation = op;
		this.initEditOptions(operands);
	}
	
	private void initEditOptions(String[] operands){
		if (this.operation==1){
			this.currName = operands[0];
			this.newName = operands[1];
		}
		else if (this.operation==2){
			this.optionSetName = operands[0];
			this.optionName = operands[1];
			try {
				this.newPrice = Float.parseFloat(operands[2]);
			}
			catch (NumberFormatException e){
			}
		}
		else { 
			this.operation = 0;
		}
	}

	@Override
	public void run() {
		switch(operation){
			case 1: updateOptionSetName(modelName, currName, newName); break;
			case 2: updateOptionPrice(modelName, optionSetName, optionName, newPrice); break;
			default: {
				System.out.println("Insufficient or incorrect arguements provided for making update");
			}
		}
	}
	
	//Synchronized version of Update OptionSet Name Method
	@ Override
	public void updateOptionSetName(String modelName, String optionSetName, String newName){
		Automobile a1 = findAutoModel(modelName);
		
		if (a1!=null){
			synchronized (a1){
				System.out.println("********Entered Thread: " + Thread.currentThread().getName());
				a1.updateOptionSet(optionSetName, newName);
				randomWait();
				printAuto(a1.getAutomobileName());
				System.out.println("********Existed Thread: " + Thread.currentThread().getName());
			}
		}
	}
	
	//Synchronized version of Update Option Price Method
	@ Override
	public void updateOptionPrice(String modelName, String optionSetName, String optionName, float newPrice){
		Automobile a1 = findAutoModel(modelName);
		if (a1!=null) {
			synchronized (a1){
				System.out.println("********Entered Thread: " + Thread.currentThread().getName());
				a1.updatePriceOfOption(optionSetName, optionName, newPrice);
				randomWait();
				printAuto(a1.getAutomobileName());
				System.out.println("********Existed Thread: " + Thread.currentThread().getName());
			}
		}
	}
	
	//Implement random wait to check atomicity of operation
	private void randomWait() {
		try {
			Thread.currentThread();
			Thread.sleep((long)(3000*Math.random()));
		} catch(InterruptedException e) {
			System.out.println("Interrupted!");
		}
	}
}
