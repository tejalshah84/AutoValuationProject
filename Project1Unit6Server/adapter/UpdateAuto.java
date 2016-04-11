/**
 * @author Tejal Shah
 *
 */
package adapter;

public interface UpdateAuto {
	
	public void updateOptionSetName(String modelName, String optionSetName, String newName);
	public void updateOptionPrice(String modelName, String optionSetName, String optionName, float newPrice);
	public void updateAutoName(String modelName, String newName);
	public void removeAuto(String modelName);
}
