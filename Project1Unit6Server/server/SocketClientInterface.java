/**
 * @author Tejal Shah
 *
 */
package server;


public interface SocketClientInterface {
	boolean openConnection();
    void handleSession();
    void closeSession();
}
