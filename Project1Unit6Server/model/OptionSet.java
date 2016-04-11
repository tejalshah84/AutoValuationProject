/**
 * @author Tejal Shah
 * This class is an encapsulated class and holds an inner class that is encapsulated by the outer class
 * This class stores an arrays of options, which is a object defined by the inner class.
 */
package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

public class OptionSet implements Serializable {
	
		private static final long serialVersionUID = 1L;
		
		//instance variables
		private String name;
		private ArrayList<Option> opt = new ArrayList<Option>();
		private Option choice;
		
	
		//No-arg constructor
		protected OptionSet(){
			this.name = "";
		}
		
		//Constructor with args
		protected OptionSet(String n){
			this.name = n;
		}
		
		//Getter for optionset name
		protected String getOptionSetName(){
			return this.name;
		}
		
		//Setter for optionset name
		protected void setOptionSetName(String n){
			this.name = n;
		}
		
		// Getter for option index given option name
		protected int getOption(String n){
			for (Option element: this.opt){
				if(element.getOptionName().equals(n))
					return this.opt.indexOf(element);
			}
			return -1;
		}
		
		//Getter for option name given index
		protected String getOption(int index){
			return this.opt.get(index).getOptionName();
		}
		
		//Setter to add new option
		protected void setOption(String n, float p){
	
			if(findOption(n)!=-1) return;
			opt.add(new Option(n,p));
		}
		
		//Find an option given an option name
		protected int findOption(String n){
			
			for (Option element : this.opt) {
				if((element.name).equals(n))
					return opt.indexOf(element);
			}
			return -1;
		}
		
		//Update an option name given old and new values
		protected void updateOptionName(String oldName, String newName){
			
			int o = findOption(oldName);
			
			if (o==-1){ 
				System.out.println("Option Not Found for Name Update");
				return;
			}
			opt.get(o).setOptionName(newName);
			//System.out.println("Option Name Updated in " + this.getOptionSetName() + " to " + opt.get(o).getOptionName());
		}	
		
		//Update an option's price given option name and new price
		protected void updatePriceOfOption(String optionName, float newPrice){
			
			int o = findOption(optionName);
					
			if (o==-1){
				System.out.println("Option Not Found for Price Update");
				return;
			}
			opt.get(o).setOptionPrice(newPrice);
			//System.out.println("Option Price Updated for " + opt.get(o).getOptionName() + " to " + opt.get(o).getOptionPrice());	
		}
		
		//Delete an option from the option array
		protected void deleteOption(String n){		
			
			int o = findOption(n);
			if (o == -1) return;
			opt.remove(o);
		}
		
		//Convert optionset state to a string
		public String toString(){
			Iterator<Option> it = opt.iterator();
			StringBuilder sb = new StringBuilder();
			sb.append("\n").append(getOptionSetName()).append(":");
			
			while(it.hasNext()){
				sb.append("\n").append(it.next().toString());
			}
			return sb.toString();
		}
		
		//Print optionset state
		protected void print(){
			Iterator<Option> it = opt.iterator();
			System.out.println("\n"+ getOptionSetName()+ ":");
			
			while(it.hasNext()){
				  it.next().print();
			}
		}
		
		protected void printValues(){
			Iterator<Option> it = opt.iterator();
			System.out.println("\n"+ getOptionSetName()+ ": ");
			
			while(it.hasNext()){
				  System.out.print(it.next().getOptionName() + ", ");
			}
		}
		
		protected void printChoices(){
			System.out.println(this.getOptionSetName()+ ": " + this.choice.getOptionName() + " (" + this.choice.getOptionPrice() + ") ");
		}
		
		protected String[] getValues(){
			Iterator<Option> it = opt.iterator();
			String[] options = new String[opt.size()];
			int count=0;
			
			while(it.hasNext()){
				 options[count++] = it.next().getOptionName();
			}
			return options;
		}
		
		protected Properties getOptionValues(){
			Properties p = new Properties();
			for(int i=0; i<opt.size();i++){
				p.setProperty(opt.get(i).getOptionName(), String.valueOf(opt.get(i).getOptionPrice()));
			}
			return p;
		}
		
		//Check if there are any options for an optionset
		protected boolean hasOptions() {
			if (this.opt.size()==0) return false;
			else return true;
		}
		
		//Check if any options are missing a price value
		protected String checkOptionPrice(){
			
			for(Option element: this.opt){
				if(element.getOptionPrice()==-1)
					return element.getOptionName();
			} 
			return null;
		}
		
		//Get option choice
		protected Option getOptionChoice(){
			return this.choice;
		}
		
		//Set option choice
		protected void setOptionChoice(String optName){
			int o = findOption(optName);
			if(o==-1) return;
			else this.choice = this.opt.get(o);	
		}
	
		//Inner class option
		protected class Option implements Serializable {
			
			private static final long serialVersionUID = 1L;
			
			//instance variables
			private String name;
			private float price;
			
			//No-arg constructor
			protected Option(){
				this.name = "";
				this.price = -1;
			}
			
			//Constructor with args
			protected Option(String n){
				this.name = n;
				this.price = -1;
			}
			
			//Constructor with args
			protected Option(String n, float p){
				this.name = n;
				this.price = p;
			}
			
			//Getter for option name
			protected String getOptionName(){
				return this.name;
			}
			
			//Setter for option name
			protected void setOptionName(String n){
				this.name = n;
			}
			
			//Getter for option price
			protected float getOptionPrice(){
				return this.price;
			}
			
			//Setter for option price
			protected void setOptionPrice(float p){
				this.price = p;
			}
			
			//Convert option state to string
			public String toString(){
				return (getOptionName()+" / "+ String.format("%.0f", getOptionPrice())).toString();
			}
			
			//Print option state
			protected void print(){
				System.out.println(getOptionName()+" / "+ String.format("%.0f", getOptionPrice()));
			}
			
		}//class Option		


}//class OptionSet
