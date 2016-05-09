package Client.Service;

import Interface.RMIClientHandler;
import Interface.RMIListenerInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by Emanuele on 09/05/2016.
 */
public class ClientRMIService extends ClientService {

    private String serverName;
    RMIListenerInterface rmiListenerInterface;
    Registry registry;
    private String rmiHandlerName;
    RMIClientHandler rmiClientHandler;


    @Override
    public void SendMessage(String message) {
        try {
            rmiClientHandler.sayHello();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean Connect() {
        try {
        rmiHandlerName = rmiListenerInterface.Connect();

            rmiClientHandler = (RMIClientHandler) registry.lookup(rmiHandlerName);
            System.out.println("Connected to server");
            return true;
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }

        return false;
    }

     ClientRMIService(String serverName) throws RemoteException, NotBoundException {
        this.serverName = serverName;
        registry = LocateRegistry.getRegistry();
        rmiListenerInterface = (RMIListenerInterface) registry.lookup(serverName);
    }


}
