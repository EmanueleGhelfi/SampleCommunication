package ClientPackage;

import Interface.RMIClientInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Emanuele on 09/05/2016.
 */
public class RMIClient extends UnicastRemoteObject implements RMIClientInterface {


    protected RMIClient() throws RemoteException {
        super();
    }

    @Override
    public void OnMessage(String message) {
        System.out.println(message);
    }
}
