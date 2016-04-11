/**
 * @author Tejal Shah
 * Common layer in API that implements all the interfaces and inherits the implementation
 * from its superclass proxyAutomobile
 */
package adapter;

import scale.EditThread;
import adapter.AutoChoices;
import adapter.CreateAuto;
import adapter.FixAuto;
import adapter.UpdateAuto;

public class BuildAuto extends ProxyAutomobile implements CreateAuto, EditThread, UpdateAuto, FixAuto, AutoChoices{

}
