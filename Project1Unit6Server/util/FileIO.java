/**
 * @author Tejal Shah
 * This class has three methods and no properties. It basically handles all the IO functions and throws
 * IO exceptions when relevant.
 */
 
package util;

import java.io.*;
import java.util.Properties;
import java.util.StringTokenizer;

import exception.AutoException;
import model.Automobile;

public class FileIO {
	
	//This method calls different parsers based on filetype
	public Automobile buildAutoObject(Object file, String filetype){
		
		if (filetype.equals("prop")){
			return parseProps((Properties) file);
		}
		else if (filetype.equals("txt")){
			return parseTxt((String) file);
		}
		return null;
	}

	//This method parses properties object into automobile
	private Automobile parseProps(Properties p){
		
		Automobile a = new Automobile();
		
			try {
				a.setAutomobileName(p.getProperty("CarName", ""));
				a.setBasePrice(Float.parseFloat(p.getProperty("CarBasePrice", "-1")));
				a.setAutomobileMake(p.getProperty("CarMake", ""));
				a.setAutomobileModel(p.getProperty("CarModel",""));
				
				for(String k: p.stringPropertyNames()){
					if (k.contains("OptionVal")){
						String opSetInd = String.valueOf(k.charAt(k.length()-2));
						String opInd = String.valueOf(k.charAt(k.length()-1));
						
						String opSet = p.getProperty("OptionSet" + opSetInd, "");
						String opName = p.getProperty(k, "");
						String opPrice = p.getProperty("OptionPrice" + opSetInd + opInd, "-1");
						a.setOption(opSet, opName, Float.parseFloat(opPrice));	
					}
				}
			} catch (NumberFormatException e){
				
			}
	
		return a;
	}
	
	//This method parses text configuration file into automobile
	private Automobile parseTxt(String filename){
		Automobile a = null;
		int lineCounter = 0;
		
		try {
			FileReader file = new FileReader(filename);
			BufferedReader buff = new BufferedReader(file);
			boolean eof = false;
			String optSetTempName="";
			
			while(!eof){
				String line = buff.readLine();
				if (line == null){
					eof = true;
				}
				else {
					lineCounter++;
					StringTokenizer st;
					
					try {
							if (lineCounter==1){
								if (line.matches("(@)([\\s*a-zA-Z]+[a-zA-Z0-9\\s]+)(/)(\\s*[+-]?[0-9]+[.]?[0-9]{0,2}\\s*)")){
									st = new StringTokenizer(line.trim().substring(line.lastIndexOf("@")+ 1),"/:()");
									a = new Automobile(st.nextToken().trim(), (st.hasMoreTokens()? Float.parseFloat(st.nextToken().trim()):-1));
								}
								else {
									try {
										throw new AutoException(6, filename);
									} catch (AutoException e) {
										String result = e.fix(e.errorNumber);
										a = new Automobile(result);
									}
								}
							}
							else {
							    //Get Option details
							    if (line.matches("([\\s*a-zA-Z[-]]+[a-zA-Z0-9[-]\\s]+)(/)(\\s*[+-]?[0-9]+[.]?[0-9]{0,2}\\s*)")){
									st = new StringTokenizer(line,"/");
									if(a!=null && st.countTokens()==2)
										a.setOption(optSetTempName, st.nextToken().trim(), (st.hasMoreTokens()? Float.parseFloat(st.nextToken().trim()):-1));
								}	
							    //Get Optionset name
								else if (line.matches("([a-zA-Z[/]\\s]+)(:)\\s*")){
									st = new StringTokenizer(line,":");
									optSetTempName = st.nextToken().trim();
									a.setOptionSet(optSetTempName);
								}
								else if(line.contains("**")){
									st = new StringTokenizer(line,":");
									st.nextToken();
									a.setAutomobileMake(st.nextToken().trim());
								}
								else if(line.contains("#")){
									st = new StringTokenizer(line,":");
									st.nextToken();
									a.setAutomobileModel(st.nextToken().trim());
								}
								else {
									optSetTempName = "";
								}
							}
							
					} catch(NullPointerException e) {
						System.out.println("Error:" + e.toString());
					} catch(NumberFormatException e) {   
						System.out.println("Error:" + e.toString() + lineCounter);
					}
				}//else	
			}//while
			buff.close();
		} catch (IOException e) {
				System.out.println("Error:" + e.toString());
		}
		return a;
	}
	
	
	//This method serializes the Automobile object	
	public void serializeAutomotive(Automobile a){
		
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("SerializeAutomotive.dat"));
			out.writeObject(a);
			out.close();
		} catch(Exception e){ 
			System.out.print("Error: " + e);
			System.exit(1);
		}
	}
	
	//This method deserializes the file into Automobile object
	public Automobile deserializeAutomotive(String f){
		Automobile a = null;
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
			a = (Automobile) in.readObject();
			in.close();
		} catch(Exception e){ 
			System.out.print("Error: " + e);
			System.exit(1);
		}
		return a;
	}
	
	
	
}
