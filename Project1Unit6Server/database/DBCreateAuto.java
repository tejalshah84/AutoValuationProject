/**
 * @author Tejal Shah
 * Interface that exposes the methods that allow adding Automobile objects
 * to the DB. This interface is used by ProxyAuto's addAuto method.
 */
package database;

import model.Automobile;

public interface DBCreateAuto {
	public boolean addAutoToDB(Automobile a);
}
