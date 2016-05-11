package Server.Communication;

import Interface.RMIClientHandler;
import Server.Communication.BaseCommunication;
import Server.UserClasses.User;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Emanuele on 09/05/2016.
 */
public class RMICommunication extends BaseCommunication implements RMIClientHandler {

    private User user;

    protected RMICommunication(String name) throws RemoteException {
        //super();
        UnicastRemoteObject.exportObject(this,0);
    }

    @Override
    public String sayHello() {
        System.out.println("Say hello");
        return "Hello!";
    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }
}
