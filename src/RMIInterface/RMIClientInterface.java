package RMIInterface;

import CommonModel.Snapshot.SnapshotToSend;
import Server.Model.Map;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/** This is the RMI client interface who is used to bind client's methods to server.
 * Created by Emanuele on 09/05/2016.
 */
public interface RMIClientInterface extends Remote {

    void sendSnapshot(SnapshotToSend snapshotToSend) throws RemoteException;

    void sendMap(ArrayList<Map> mapArrayList) throws RemoteException;

    void gameInitialization(SnapshotToSend snapshotToSend) throws  RemoteException;

    void isYourTurn() throws RemoteException;

    void finishTurn() throws RemoteException;

    void onStartMarket() throws RemoteException;

    void onStartBuyPhase() throws RemoteException;

    void disableMarketPhase() throws RemoteException;

    void selectPermitCard() throws RemoteException;

    void selectCityRewardBonus() throws RemoteException;
}
