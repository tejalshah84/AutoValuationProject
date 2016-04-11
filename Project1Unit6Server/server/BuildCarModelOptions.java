/**
 * @author Tejal Shah
 * Implements AutoServer functions. This class almost acts like an adapter by rerouting
 * all function behavior to BuildAuto class.
 */
package server;

import java.util.Properties;
import adapter.BuildAuto;
import model.Automobile;

public class BuildCarModelOptions implements AutoServer {

	private AutoServer ba = new BuildAuto();

	//This method calls the buildAuto method in proxy by passing properyt file
	public boolean buildAuto(Object file, String filetype){

		boolean temp = ba.buildAuto(file, filetype);
		return temp;
	}

	//Obtain list of all automobiles in the system as a properties object
	public Properties listofAutomobiles() {
		return ba.listofAutomobiles();
	}

	//Find Automobile with a specific name
	public Automobile findAutoModel(String modelName){
		return ba.findAutoModel(modelName);
	}
	
	public boolean addAuto(Automobile a){
		return ba.addAuto(a);
	}

}
