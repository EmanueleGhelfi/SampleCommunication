package Client.Service;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by Emanuele on 09/05/2016.
 */
public class FactoryService {

    public static ClientService getService(String method) throws RemoteException, NotBoundException {
        if(method.equals("RMI")) {

            return new ClientRMIService("server");
        }
        else{
            return new ClientSocketService();
        }


    }
}
