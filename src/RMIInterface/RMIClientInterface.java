package RMIInterface;

import CommonModel.Snapshot.SnapshotToSend;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Emanuele on 09/05/2016.
 */
public interface RMIClientInterface extends Remote {

    void sendSnapshot(SnapshotToSend snapshotToSend) throws RemoteException;
}
