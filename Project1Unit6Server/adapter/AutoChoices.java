/**
 * @author Tejal Shah
 * Interface that allows user to setup choices and get total price
 */
package adapter;

import java.util.Properties;

public interface AutoChoices {

	public void setChoice(String modelName, Properties choice);
	public void calculateTotalPrice(String modelName);
	public int getNumberofOptions(String modelName);
	public String getOptionSetName(String modelName, int i);
	public String[] getOptionValues(String modelName, String osName);
	public void printChoices(String modelName);
}
