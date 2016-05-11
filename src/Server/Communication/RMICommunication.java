package Server.Communication;

import Interface.RMIClientHandler;
import Interface.RMIClientInterface;
import Server.UserClasses.User;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Emanuele on 09/05/2016.
 */
public class RMICommunication extends BaseCommunication implements RMIClientHandler {

    private User user;
    private RMIClientInterface rmiClientInterface;

    public RMICommunication(String name) throws RemoteException {
        //super();
        UnicastRemoteObject.exportObject(this,0);
    }

    @Override
    public String sayHello() {
        System.out.println("Say hello");
        return "Hello!";
    }

    @Override
    public boolean sendIP(String ip, String name) throws RemoteException {
        Registry registry = LocateRegistry.getRegistry(ip,1099);
        try {
            rmiClientInterface = (RMIClientInterface) registry.lookup(name);
            return true;
        } catch (NotBoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void sendMessage(String message) {
        rmiClientInterface.OnMessage(message);
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }
}
