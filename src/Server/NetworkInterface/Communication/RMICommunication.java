package Server.NetworkInterface.Communication;

import CommonModel.GameModel.Action.Action;
import CommonModel.Snapshot.SnapshotToSend;
import Utilities.Exception.ActionNotPossibleException;
import RMIInterface.RMIClientHandler;
import RMIInterface.RMIClientInterface;
import Server.Controller.GameController;
import Server.Controller.GamesManager;
import Server.Model.User;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Emanuele on 09/05/2016.
 */
public class RMICommunication extends BaseCommunication implements RMIClientHandler, Serializable {

    private User user;
    private RMIClientInterface rmiClientInterface;
    private GamesManager gamesManager;
    private GameController gameController;

    //default
    public RMICommunication() {
    }

    public RMICommunication(String name) throws RemoteException {
        //super();
        gamesManager = GamesManager.getInstance();
        UnicastRemoteObject.exportObject(this,0);
    }

    @Override
    public boolean sendIP(String ip, String name) throws RemoteException {
        Registry registry = LocateRegistry.getRegistry(ip,1099);
        try {
            // get remote object from client
            rmiClientInterface = (RMIClientInterface) registry.lookup(name);
            return true;
        } catch (NotBoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean tryToSetName(String username) throws RemoteException {
        if(!gamesManager.userAlreadyPresent(username)){
            this.user.setUsername(username);
            gamesManager.addToGame(user);
            return true;
        }
        return false;
    }

    @Override
    public void test(Action electCouncilor) throws ActionNotPossibleException {
        electCouncilor.doAction(user.getGame(), user);
    }


    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void notifyGameStart() {
        //rmiClientInterface.onStart();
    }

    @Override
    public void sendSnapshot(SnapshotToSend snapshotToSend) {
        try {
            rmiClientInterface.sendSnapshot(snapshotToSend);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
