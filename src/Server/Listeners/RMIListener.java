package Server.Listeners;

import Interface.RMIClientHandler;
import Interface.RMIListenerInterface;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Emanuele on 09/05/2016.
 */
public class RMIListener implements RMIListenerInterface {

    public RMIListener() throws RemoteException {
        //super();
        UnicastRemoteObject.exportObject(this,0);
    }

    @Override
    public String Connect() {
        String name = "ClientHandler";
        System.out.println("Client connected in rmi");
        try {
            RMIClientHandler rmiHandler = new RMIHandler(name);
            //RMIClientHandler rmiClientHandler = (RMIClientHandler) UnicastRemoteObject.exportObject(rmiHandler,0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name,rmiHandler);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return name;
    }
}
