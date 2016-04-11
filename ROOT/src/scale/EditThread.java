/**
 * @author Tejal Shah
 * Interface implemented by BuildAuto, which inherits the actual implementation from ProxyAuto.
 * This defines a method to allow usage of editoptions class to create thread through buildauto interface.
 *
 */
package scale;


public interface EditThread{
	
	public void updateOperationThreadStart(String threadName, String modelName, int op, String[] operands);

}
