package RMIInterface;

import CommonModel.GameModel.Action.Action;
import Utilities.Exception.ActionNotPossibleException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Emanuele on 09/05/2016.
 */
public interface RMIClientHandler extends Remote{

    boolean sendIP(String ip, String name) throws RemoteException;

    boolean tryToSetName(String username) throws RemoteException;

    void test(Action electCouncilor) throws ActionNotPossibleException, RemoteException;
}
