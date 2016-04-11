/**
 * @author Tejal Shah
 * Interface that allows user to create and print auto. This layer hides all the details of File IO
 * and Automobile classes.
 */
package adapter;

import model.Automobile;

public interface CreateAuto {
	public boolean buildAuto(Object file, String filetype);
	public void printAuto(String Modelname);
	public boolean addAuto(Automobile a);
}
