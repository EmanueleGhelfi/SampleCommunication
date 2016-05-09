package Listener;

import Interface.RMIClientHandler;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Emanuele on 09/05/2016.
 */
public class RMIHandler extends UnicastRemoteObject implements RMIClientHandler {

    protected RMIHandler() throws RemoteException {
        super();
    }

    @Override
    public String sayHello() {
        return "Hello!";
    }
}
