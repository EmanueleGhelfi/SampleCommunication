package Server.Communication;

import Interface.RMIClientHandler;
import Interface.RMIClientInterface;
import Server.Managers.Game;
import Server.Managers.GameManager;
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
    private GameManager gameManager;

    public RMICommunication(String name) throws RemoteException {
        //super();
        gameManager = GameManager.getInstance();
        UnicastRemoteObject.exportObject(this,0);
    }


    // receive a message from client
    @Override
    public void OnMessage(String message) {
        user.OnMessage(message);
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
    public boolean tryToSetName(String username) throws RemoteException {
        if(!gameManager.userAlreadyPresent(username)){
            this.user.setUsername(username);
            gameManager.addToGame(user);
            return true;
        }

        return false;
    }

    // send a message to client
    @Override
    public void sendMessage(String message) {
        try {
            rmiClientInterface.OnMessage(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }
}
