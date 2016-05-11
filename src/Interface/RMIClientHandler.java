package Interface;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Emanuele on 09/05/2016.
 */
public interface RMIClientHandler extends Remote{

    String sayHello() throws RemoteException;

    boolean sendIP(String ip, String name) throws RemoteException;
}
