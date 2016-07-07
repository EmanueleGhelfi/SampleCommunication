package RMIInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

/** Is the class used for open the connection in RMI.
 * Created by Emanuele on 09/05/2016.
 */
public interface RMIListenerInterface extends Remote {

    String Connect() throws RemoteException;

}
