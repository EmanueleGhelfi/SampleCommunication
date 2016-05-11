package ClientPackage.Service;

import ClientPackage.Client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by Emanuele on 09/05/2016.
 */
public class FactoryService {

    public static ClientService getService(String method, String serverIP, Client client) throws RemoteException, NotBoundException {
        if(method.equals("RMI")) {

            return new ClientRMIService("server", serverIP,client);
        }
        else{
            return new ClientSocketService(serverIP,client);
        }


    }
}
