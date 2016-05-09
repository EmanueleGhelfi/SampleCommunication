package Listener;

import Interface.RMIClientHandler;
import Interface.RMIListenerInterface;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Emanuele on 09/05/2016.
 */
public class RMIListener extends UnicastRemoteObject implements RMIListenerInterface {

    protected RMIListener() throws RemoteException {
        super();
    }

    @Override
    public String Connect() {
        String name = "ClientHandler";
        try {
            RMIHandler rmiHandler = new RMIHandler();
            RMIClientHandler rmiClientHandler = (RMIClientHandler) UnicastRemoteObject.exportObject(rmiHandler,0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name,rmiClientHandler);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return name;
    }
}
