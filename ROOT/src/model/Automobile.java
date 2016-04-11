/**
 * @author Tejal Shah
 *This is the main model class that is the interface to the outside world. All interaction is
 *done through public methods of this class. This class also encapsulates the option and optionset
 *classes by not allowing direct access to their functionality.
 */

package model;

//class implements serializable so its state can be stored in file
import java.io.Serializable;
import java.util.Iterator;
import java.util.Properties;
import java.util.ArrayList;

public class Automobile implements Serializable {
		
		private static final long serialVersionUID = 1L;
		
		//instance variables
		private String make;
		private String model;
		private String name;
	    private float basePrice;
		private ArrayList<OptionSet> opset = new ArrayList<OptionSet>();
		
		//No-arg constructor
		public Automobile() {
			this.make = "";
			this.model = "";
			this.name = "";
			this.basePrice = -1;	
		} 
		
		//Constructor with args
		public Automobile(String n) {
			this.make = "";
			this.model = "";
			this.name = n;
			this.basePrice = -1;
		}
		
		//Constructor with args
		public Automobile(String n, float bPrice) {
			this.make = "";
			this.model = "";
			this.name = n;
			this.basePrice = bPrice;
		}
		 
		//Constructor with args
		public Automobile(String mk, String mo, String n, float bPrice) {
			this.make = "";
			this.model = "";
			this.name = n;
			this.basePrice = bPrice;
		}
		
		//Getter for Automobile make
		public String getAutomobileMake(){
			return this.make;
		}

		//Setter for Automobile make
		public void setAutomobileMake(String n){
			this.make = n;
		}

		//Getter for Automobile model
		public String getAutomobileModel(){
			return this.model;
		}

		//Setter for Automobile model
		public void setAutomobileModel(String n){
			this.model = n;
		}
		
		//Getter for Automobile name
		public String getAutomobileName(){
			return this.name;
		}
		
		//Setter for Automobile name
		public void setAutomobileName(String n){
			this.name = n;
		}
		
		//Getter for Automobile base price
		public float getBasePrice(){
			return this.basePrice;
		}
		
		//Setter for Automobile base price
		public void setBasePrice(float bPrice){
			this.basePrice = bPrice;
		}
		
		//Getter for optionset
		public String getOptionSet(int index){
			if (index < this.opset.size()){
				return this.opset.get(index).getOptionSetName();
			}
			return null;
		}
		
		//Getter for optionset
		public ArrayList<String> getOptionSet(){
			ArrayList<String> al = new ArrayList<String>();
			for (int i=0; i<this.opset.size(); i++){
				al.add(this.opset.get(i).getOptionSetName());
			}
			return al;
		}
		
		public int getNumberOfOptions(){
			return this.opset.size();
		}
		
		//Setter for OptionSet
		public void setOptionSet(String n){
			
			if (findOptionSet(n)!=-1) return;
			opset.add(new OptionSet(n));
		}
		
		//Method to find an optionset given a name
		private int findOptionSet(String n){
			for (OptionSet element : this.opset) {
				if((element.getOptionSetName()).equals(n)) {
					return opset.indexOf(element);
				}
			}
			return -1;
		}
		
		//Method to find optionset index given option name
		private int findOptionSetIndexByOption(String n){
			for (OptionSet element : this.opset) {
				if(element.findOption(n)!=-1){
					return opset.indexOf(element);
				}
			}
			return -1;
		}
		
		//Method to find optionset name given option name
		public String findOptionSetNameByOption(String n){

			for (OptionSet element : this.opset) {
				if(element.findOption(n)!=-1){
					return element.getOptionSetName();
				}
			}
			return null;
		}
		
		//Method to update optionset
		public void updateOptionSet(String oldName, String newName){
			
			for (OptionSet element : this.opset) {
				if((element.getOptionSetName()).equals(oldName)){
					try {
						Thread.currentThread();
						Thread.sleep((long)(3000*Math.random()));
					} catch(InterruptedException e) {
						System.out.println("Interrupted!");
					}
					element.setOptionSetName(newName);
					System.out.println("OptionSet Name Updated from " + oldName + " to " + element.getOptionSetName());
					return;
				}
			}
			System.out.println("OptionSet Not Found for Updating its name");
		}
		
		//Method to delete optionset
		public void deleteOptionSet(String n){
			int index = findOptionSet(n);
			if(index!=-1){
				opset.remove(index);
			}
		}
		
		public void printOptionSetValues(String n){
			int index = findOptionSet(n);
			if(index!=-1){
				opset.get(index).printValues();
			}
		}
		
		public String[] getOptionSetValues(String n){
			int index = findOptionSet(n);
			if(index!=-1){
				return opset.get(index).getValues();
			}
			return null;
		}
		
		public Properties getOptionValues(String n){
			int index = findOptionSet(n);
			if(index!=-1){
				return opset.get(index).getOptionValues();
			}
			return null;
		}
		
		//Tostring method to convert Automobile state to string
		public String toString(){
			Iterator<OptionSet> it = opset.iterator();
			StringBuilder sb = new StringBuilder();
			sb.append("@ ").append(getAutomobileName()).append(" / ").append(String.format("%.0f", getBasePrice()));
			sb.append("\n").append("**Make: ").append(getAutomobileMake());
			sb.append("\n").append("#Model: ").append(getAutomobileModel());
			
			while(it.hasNext()){
				sb.append("\n").append(it.next().toString());
			}
			return sb.toString();
		}
		
		//Method to print Automobile state
		public void print(){
			Iterator<OptionSet> it = opset.iterator();
			System.out.println("@" + getAutomobileName() + " / " + String.format("%.0f", getBasePrice()));
			System.out.println("**Make: " + getAutomobileMake());
			System.out.println("#Model: " + getAutomobileModel());
			
			while(it.hasNext()){
				  it.next().print();
			}
		}
		
		//Method to set option in optionset context
		public void setOption(String osName, String n, float p) {
			int os = findOptionSet(osName);
			
			if (os == -1) {
				setOptionSet(osName);
				os = findOptionSet(osName);
			}
			opset.get(os).setOption(n, p);
		}
		
		//Method to update option name
		public void updateNameOfOption(String oldName, String newName) {
			int os = findOptionSetIndexByOption(oldName);

			if (os == -1){
				System.out.println("OptionSet Not Found for Option Name Update");
				return;
			}
			opset.get(os).updateOptionName(oldName, newName);
		}
		
		//Method to update option price
		public void updatePriceOfOption(String optionSetName, String optionName, float newPrice) {
			int os = findOptionSet(optionSetName);
			if (os == -1){
				System.out.println("OptionSet Not Found for Option Price Update");
				return;
			}
			opset.get(os).updatePriceOfOption(optionName, newPrice);
		}
		
		//Method to delete option from option set
		public void deleteOption(String n){
			int os = findOptionSetIndexByOption(n);
			if (os == -1) return;
			opset.get(os).deleteOption(n);
		}
		
		//Method to check if atleast one optionset is defined
		public boolean anyOptionSets(){
			if (opset.size() == 0) return false;
			else return true;
		}
		
		//Method to check if atleast one option is defined for each optionset
		public String missingOptions(){
				
			for(OptionSet element: this.opset){
				if(!element.hasOptions())
					return element.getOptionSetName();
			}
			return null;
		}
		
		//Method to check if Option price is missing
		public String missingOptionPrice(){
			
			String optPriceMiss;
			for(OptionSet element: this.opset){
				if ((optPriceMiss = element.checkOptionPrice())!=null)
					return optPriceMiss; 
			}		
			return null;
		}
		
		//Method to check if Optionset is missing name
		public String missingOptionSetName(){
		
			for(OptionSet element: this.opset){
				if((element.getOptionSetName()).equals("") && element.hasOptions()){
						return element.getOption(0);
				}
			}				
			return null;
		}
		
		//Method to find option choice selected at optionset level
		public String getOptionChoice(String optSetName){
			int os = findOptionSet(optSetName);
			
			if(os==-1) return null;
			else{
				return this.opset.get(os).getOptionChoice().getOptionName();
			}
		}
		
		//Method to get price of choice selected at optionset level
		public float getOptionChoicePrice(String optSetName){
			int os = findOptionSet(optSetName);
			
			if(os==-1) return 0;
			else{
				if(this.opset.get(os).getOptionChoice()!=null){
					float choicePrice = this.opset.get(os).getOptionChoice().getOptionPrice();
					return (choicePrice==-1? 0 : choicePrice);
				}
				return 0;
			}
		}
		
		//Method to set option choice for an optionset
		public void setOptionChoice(String optSetName, String optName){
			int os = findOptionSet(optSetName);
			
			if(os==-1) return;
			else{
				this.opset.get(os).setOptionChoice(optName);
			}
		}
		
		public void printChoices(){
			Iterator<OptionSet> it = opset.iterator();
			while(it.hasNext()){
				  it.next().printChoices();
			}
		}
		
		//Method to calculate total price for choices selected
		public float getTotalPrice(){
			
			float totalPrice = this.getBasePrice();
			
			for(OptionSet element: this.opset){
				totalPrice += getOptionChoicePrice(element.getOptionSetName());
			}
			
			return totalPrice;
		}
		
		
					
}//class Automobile

