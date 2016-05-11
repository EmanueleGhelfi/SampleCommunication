package Client.Service;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by Emanuele on 09/05/2016.
 */
public class FactoryService {

    public static ClientService getService(String method, String serverIP) throws RemoteException, NotBoundException {
        if(method.equals("RMI")) {

            return new ClientRMIService("server", serverIP);
        }
        else{
            return new ClientSocketService(serverIP);
        }


    }
}
