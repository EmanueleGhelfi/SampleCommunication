package RMIInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Emanuele on 09/05/2016.
 */
public interface RMIListenerInterface extends Remote {

    String Connect() throws RemoteException;
}
