package Client;

import Interface.RMIListenerInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by Emanuele on 09/05/2016.
 */
public class ClientMain {

    public static void main(String[] args){
        Client client = new Client();
        client.Start();
    }
}
