/**
 * @author Tejal Shah
 * Interface implemented by BuildAuto and BuildCarModelOptions classes. It defines the three
 *  basic features that the server provides to the client.
 */
package server;

import java.util.Properties;
import model.Automobile;

public interface AutoServer {

	public boolean buildAuto(Object file, String filetype);
	public Properties listofAutomobiles();
	public Automobile findAutoModel(String modelName);
	public boolean addAuto(Automobile a);

}
