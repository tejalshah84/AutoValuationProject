/**
 * @author Tejal Shah
 * Interface that exposes the methods that allow deleting Automobile object
 * from the DB. This interface is used by ProxyAuto's deleteAuto method 
 * and implemented by DBOperations class.
 */
package database;


public interface DBDeleteAuto {
	public boolean removeAutoFromDB(String modelName);
}
