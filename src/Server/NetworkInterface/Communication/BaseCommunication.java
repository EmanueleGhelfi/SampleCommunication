package Server.NetworkInterface.Communication;

import CommonModel.Snapshot.SnapshotToSend;
import Server.Model.Map;
import Server.Model.User;

import java.util.ArrayList;

/**
 * Created by Emanuele on 09/05/2016.
 */
public abstract class BaseCommunication {

    public abstract void setUser (User user);

    public abstract void sendSnapshot(SnapshotToSend snapshotToSend);

    public abstract void changeRound();

    public abstract void sendAvailableMap(ArrayList<Map> availableMaps);

    public abstract void sendSelectedMap(SnapshotToSend snapshotToSend);
}
