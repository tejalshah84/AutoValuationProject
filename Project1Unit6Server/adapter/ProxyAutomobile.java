/**
 * @author Tejal Shah
 * This class encapsulates the automobile class operations from the rest of the classes.
 * Operations in this class are only exposed through subclasses and interfaces.
 */
package adapter;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import database.DBCreateAuto;
import database.DBDeleteAuto;
import database.DBOperations;
import database.DBUpdateAuto;
import exception.AutoException;
import model.*;
import scale.EditOptions;
import util.FileIO;

public abstract class ProxyAutomobile {

	private static LinkedHashMap <String, Automobile> autoModels = new LinkedHashMap<String, Automobile>();
	
	public boolean buildAuto(Object file, String filetype){
		FileIO fio = new FileIO();
		Automobile a1 = fio.buildAutoObject(file, filetype);
		if (a1!=null){
			return addAuto(a1);
		}
		return false;
	}
	
	public boolean addAuto(Automobile a){
		Automobile a1 = findAutoModel(a.getAutomobileName());
		if (a!=null){
			DBCreateAuto dbo = new DBOperations();
			if (dbo.addAutoToDB(a)){
				System.out.println("Created Automobile " + a.getAutomobileName());
				if (a1==null){
					autoModels.put(a.getAutomobileName(), a);
				}
				return true;
			}
		}
		return false;
	}
	
	public void removeAuto(String modelName){
		Automobile a1 = findAutoModel(modelName);
		if(modelName!=null){
			DBDeleteAuto dbo = new DBOperations();
			if (dbo.removeAutoFromDB(modelName)){
				System.out.println("Deleted Automobile " + modelName);
				if (a1!=null){
					autoModels.remove(a1.getAutomobileName());
				}
			}
			else{
				System.out.println("Error Occurred while deleting auto");
			}
		}
		
	}
	
	public void updateAutoName(String modelName, String newName){
		Automobile a1 = findAutoModel(modelName);
		Automobile a2 = findAutoModel(newName);
		if (a1!=null && a2==null){
			DBUpdateAuto dbo = new DBOperations();
			if (dbo.updateAutomobileName(modelName, newName)){
				System.out.println("Updating Automobile Name for " + modelName);
				a1 = autoModels.remove(modelName);
				a1.setAutomobileName(newName);
				autoModels.put(a1.getAutomobileName(), a1);
			}
		}
		else if (a2!=null){
			System.out.println("Auto Name already in use");
		}
	}
	
	public void printAuto(String modelName){
		Automobile a1 = findAutoModel(modelName);
		
		if (a1!=null)
			a1.print();
		else
			System.out.println(modelName + " not found");
	}
	
	public void updateOptionSetName(String modelName, String optionSetName, String newName){
		Automobile a1 = findAutoModel(modelName);
		if (a1!=null){
			DBUpdateAuto dbo = new DBOperations();
			
			if (dbo.updateOptionSetName(modelName, optionSetName, newName)){
				System.out.println("Updating OptionSet Name for " + optionSetName);
				a1.updateOptionSet(optionSetName, newName);
			}
			else{
				System.out.println("OptionSet Name is already in use, choose another option");
			}
		}
		else{
			System.out.println("Automobile does not exist for optionset update");
		}
	}
	
	public void updateOptionPrice(String modelName, String optionSetName, String optionName, float newPrice){
		Automobile a1 = findAutoModel(modelName);
		if (a1!=null) {
			DBUpdateAuto dbo = new DBOperations();
			if (dbo.updateOptionPrice(modelName, optionSetName, optionName, newPrice)){
				System.out.println("Updating Option Price for " + optionName);
				a1.updatePriceOfOption(optionSetName, optionName, newPrice);
			}
		}
		else{
			System.out.println("Automobile does not exist for option price update");
		}
	}
	
	public void setChoice(String modelName, LinkedHashMap <String, String> choice){
		Automobile a1 = findAutoModel(modelName);
		if (a1!=null) {
			for(String key: choice.keySet()){
				a1.setOptionChoice(key, choice.get(key));
			}
		}
	}
	
	public void setChoice(String modelName, Properties choice){
		Automobile a1 = findAutoModel(modelName);
		if (a1!=null) {
			for(String key: choice.stringPropertyNames()){
				a1.setOptionChoice(key, choice.getProperty(key, ""));
			}
		}
	}
	
	public void calculateTotalPrice(String modelName){
		Automobile a1 = findAutoModel(modelName);
		if (a1!=null) {
			System.out.println(String.format("%.0f", a1.getTotalPrice()));
		}
	}
	
	public void validateAuto(String modelName){
		
		Automobile a1 = findAutoModel(modelName);
		if (a1==null) return;
		
		boolean hasErrors = false;

		do {
			String ctxt = null;
			try{

				if (a1.getBasePrice() == -1){
					hasErrors = true;
					throw new AutoException(1, a1.getAutomobileName());
				}
				
				if(!a1.anyOptionSets()){
					hasErrors = true;
					throw new AutoException(2, a1.getAutomobileName());
				}
				
				if((ctxt = a1.missingOptionSetName())!=null){
					hasErrors = true;
					throw new AutoException(3, ctxt);
				}
				
				if((ctxt = a1.missingOptions()) != null){
					hasErrors = true;
					throw new AutoException(4, ctxt);
				}
				
				if((ctxt = a1.missingOptionPrice()) != null){
					hasErrors = true;
					throw new AutoException(5, ctxt);
				}
		
				hasErrors = false;
			} catch (AutoException e){
				String result = e.fix(e.errorNumber);
				fixError(e.errorNumber, e.errorContext, a1, result);
			}
		} while(hasErrors);	
	}
	
	
	private void fixError(int errNum, String ctxt, Automobile a1, String result){
		
		if(errNum == 1){
			a1.setBasePrice(Float.parseFloat(result));
			System.out.println("Fixed AutoBasePrice for "+ ctxt);
			System.out.println();
		}
		else if(errNum == 2){
			a1.setOptionSet(result);
			System.out.println("Added missing OptionSet for "+ ctxt);
			System.out.println();
		}
		else if(errNum == 3){
			String val = a1.findOptionSetNameByOption(ctxt);
			if(val.equals("")){
				a1.updateOptionSet(val, result);
			}
			System.out.println("Fixed missing OptionSet name for option "+ ctxt);
			System.out.println();
		}
		else if(errNum == 4){
			a1.setOption(ctxt, result, -1);
			System.out.println("Added missing option for optionset "+ ctxt);
			System.out.println();
		}
		else if(errNum == 5){
			String val = a1.findOptionSetNameByOption(ctxt);
			a1.updatePriceOfOption(val, ctxt, Float.parseFloat(result));
			System.out.println("Fixed option price for "+ ctxt);
			System.out.println();
		}
		else {
			System.out.println(result);
			System.out.println();
		}
	}
	
	
	public Automobile findAutoModel(String modelName){
		Set<Entry<String, Automobile>> entryset = autoModels.entrySet();
		Iterator<Entry<String, Automobile>> it = entryset.iterator();
		Entry<String, Automobile> mdlNm;
		
		while(it.hasNext()){
			mdlNm = it.next();
			if((mdlNm.getKey()).equals(modelName)){
				return mdlNm.getValue();
			}
		}
		
		return null;
	}
	
	public void updateOperationThreadStart(String threadName, String modelName, int op, String[] operands){
		EditOptions eo = new EditOptions(modelName, op, operands);
		Thread t = new Thread(eo, threadName);
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public Properties listofAutomobiles(){
		Properties p = new Properties();
		Set<Entry<String, Automobile>> entryset = autoModels.entrySet();
		Iterator<Entry<String, Automobile>> it = entryset.iterator();
		Entry<String, Automobile> mdlNm;
		int count = 0;
		
		while(it.hasNext()){
			mdlNm = it.next();
			p.setProperty(String.valueOf(++count), mdlNm.getKey());
		}
		p.setProperty("MaxNum", String.valueOf(count));
		System.out.println(p.getProperty("1"));
		return p;
	}
	
	public int getNumberofOptions(String modelName){
		Automobile a1 = findAutoModel(modelName);
		if (a1!=null) {
			return a1.getNumberOfOptions();
		}
		return 0;
	}
	
	public String getOptionSetName(String modelName, int i){
		Automobile a1 = findAutoModel(modelName);
		if (a1!=null) {
			return a1.getOptionSet(i);
		}
		return "";
	}
	
	public String[] getOptionValues(String modelName, String osName){
		Automobile a1 = findAutoModel(modelName);
		if (a1!=null) {
			return a1.getOptionSetValues(osName);
		}
		return null;
	}
	
	public void printChoices(String modelName){
		Automobile a1 = findAutoModel(modelName);
		if (a1!=null) {
			a1.printChoices();
		}
	}
}
