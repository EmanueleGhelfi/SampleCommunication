package RMIInterface;

import CommonModel.GameModel.Action.Action;
import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.City.City;
import CommonModel.GameModel.Market.BuyableWrapper;
import Server.Model.Map;
import Utilities.Exception.ActionNotPossibleException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/** This class acts as a bridge between the server's methods and the client via RMI.
 * Created by Emanuele on 09/05/2016.
 */
public interface RMIClientHandler extends Remote {

    boolean tryToSetName(String username) throws RemoteException;

    void action(Action action) throws ActionNotPossibleException, RemoteException;

    void sendRemoteClientObject(RMIClientInterface clientRMIService) throws RemoteException;

    void sendMap(Map map) throws RemoteException;

    boolean sendBuyableObject(ArrayList<BuyableWrapper> buyableWrappers) throws RemoteException;

    boolean buyObject(ArrayList<BuyableWrapper> buyableWrappers) throws RemoteException;

    void onRemoveItem(BuyableWrapper item) throws RemoteException;

    void onFinishSellPhase() throws RemoteException;

    void onFinishBuyPhase() throws RemoteException;

    void getCityRewardBonus(City city1) throws RemoteException, ActionNotPossibleException;

    void onSelectPermitCard(PermitCard permitCard) throws RemoteException;

    void finishRound() throws RemoteException;

    void onSelectOldPermitCard(PermitCard permitCard) throws RemoteException;
}
