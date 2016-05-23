package RMIInterface;

import ClientPackage.NetworkInterface.ClientRMIService;
import CommonModel.GameModel.Action.Action;
import CommonModel.Snapshot.SnapshotToSend;
import Server.Model.Map;
import Utilities.Exception.ActionNotPossibleException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Emanuele on 09/05/2016.
 */
public interface RMIClientHandler extends Remote{

    boolean tryToSetName(String username) throws RemoteException;

    void test(Action electCouncilor) throws ActionNotPossibleException, RemoteException;

    void sendRemoteClientObject(RMIClientInterface clientRMIService) throws RemoteException;

    void sendMap(Map map) throws RemoteException;

}
