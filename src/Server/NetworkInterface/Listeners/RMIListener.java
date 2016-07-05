package Server.NetworkInterface.Listeners;

import RMIInterface.RMIClientHandler;
import RMIInterface.RMIListenerInterface;
import Server.Controller.GamesManager;
import Server.Model.User;
import Server.NetworkInterface.Communication.BaseCommunication;
import Server.NetworkInterface.Communication.RMICommunication;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Emanuele on 09/05/2016.
 */
public class RMIListener implements RMIListenerInterface {

    private int clientNumber;
    private final GamesManager gamesManager;

    public RMIListener(GamesManager gamesManager) throws RemoteException {
        UnicastRemoteObject.exportObject(this, 0);
        this.gamesManager = gamesManager;
    }

    @Override
    public String Connect() {
        String name = "ClientHandler" + this.clientNumber;
        System.out.println("ClientPackage connected in RMI");
        try {
            RMIClientHandler rmiHandler = new RMICommunication(name);
            //RMIClientHandler rmiClientHandler = (RMIClientHandler) UnicastRemoteObject.exportObject(rmiHandler,0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, rmiHandler);
            User user = new User((BaseCommunication) rmiHandler, this.gamesManager);
            ((BaseCommunication) rmiHandler).setUser(user);
            this.gamesManager.AddToUsers(user);
            this.clientNumber++;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return name;
    }
}
