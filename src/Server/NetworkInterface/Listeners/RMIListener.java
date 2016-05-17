package Server.NetworkInterface.Listeners;

import Interface.RMIClientHandler;
import Interface.RMIListenerInterface;
import Server.NetworkInterface.Communication.BaseCommunication;
import Server.NetworkInterface.Communication.RMICommunication;
import Server.Controller.GamesManager;
import Server.Model.User;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Emanuele on 09/05/2016.
 */
public class RMIListener implements RMIListenerInterface {

    private int clientNumber = 0;
    private GamesManager gamesManager;

    public RMIListener(GamesManager gamesManager) throws RemoteException {
        //super();
        UnicastRemoteObject.exportObject(this,0);
        this.gamesManager = gamesManager;
    }

    @Override
    public String Connect() {
        String name = "ClientHandler"+clientNumber;

        System.out.println("ClientPackage connected in rmi");
        try {
            RMIClientHandler rmiHandler = new RMICommunication(name);
            //RMIClientHandler rmiClientHandler = (RMIClientHandler) UnicastRemoteObject.exportObject(rmiHandler,0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name,rmiHandler);
            User user = new User((BaseCommunication) rmiHandler, gamesManager);
            ((BaseCommunication) rmiHandler).setUser(user);
            gamesManager.AddToUsers(user);
            clientNumber++;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return name;
    }
}
