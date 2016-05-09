package Client;

import Interface.RMIClientHandler;
import Interface.RMIClientInterface;
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
    void SendMessage(String message) {
        rmiClientHandler.sayHello();
    }

    @Override
    void Connect() {
        rmiHandlerName = rmiListenerInterface.Connect();
        try {
            rmiClientHandler = (RMIClientHandler) registry.lookup(rmiHandlerName);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }

    public ClientRMIService() throws RemoteException, NotBoundException {
        registry = LocateRegistry.getRegistry();
        rmiListenerInterface = (RMIListenerInterface) registry.lookup(serverName);
    }


}
