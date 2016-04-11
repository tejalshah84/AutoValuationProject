/**
 * @author Tejal Shah
 * Interface that exposes the methods that allow updating Automobile properties
 * in the DB. This interface is used by ProxyAuto's update methods and implemented
 * by DBOperations class.
 */
package database;


public interface DBUpdateAuto {
	public boolean updateAutomobileName(String modelName, String newName);
	public boolean updateOptionSetName(String modelName, String osName, String newName);
	public boolean updateOptionPrice(String modelName, String osName, String oName, float price);
}
