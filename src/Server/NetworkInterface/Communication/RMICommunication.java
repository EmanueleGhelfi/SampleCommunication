package Server.NetworkInterface.Communication;

import CommonModel.GameModel.Action.Action;
import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.City.City;
import CommonModel.GameModel.Market.BuyableWrapper;
import CommonModel.Snapshot.SnapshotToSend;
import Server.Model.Map;
import Utilities.Exception.ActionNotPossibleException;
import RMIInterface.RMIClientHandler;
import RMIInterface.RMIClientInterface;
import Server.Controller.GamesManager;
import Server.Model.User;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Created by Emanuele on 09/05/2016.
 */
public class RMICommunication extends BaseCommunication implements RMIClientHandler, Serializable {

    private User user;
    private RMIClientInterface rmiClientInterface;
    private GamesManager gamesManager;

    public RMICommunication() {
    }

    public RMICommunication(String name) throws RemoteException {
        gamesManager = GamesManager.getInstance();
        UnicastRemoteObject.exportObject(this,0);
    }


    /** Overriding RMIClientHandler
     *
     * @param username
     * @return
     * @throws RemoteException
     */
    @Override
    public boolean tryToSetName(String username) throws RemoteException {
        if(!gamesManager.userAlreadyPresent(username)){
            this.user.setUsername(username);
            gamesManager.addToGame(user);
            return true;
        }
        return false;
    }

    /** Overriding RMIClientHandler
     *
     * @param electCouncilor
     * @throws ActionNotPossibleException
     */
    @Override
    public void test(Action electCouncilor) throws ActionNotPossibleException {
        electCouncilor.doAction(user.getGame(), user);
    }

    /** Overriding RMIClientHandler
     * Called by client when the remote object is exported
     * @param clientRMIService
     * @throws RemoteException
     */
    @Override
    public void sendRemoteClientObject(RMIClientInterface clientRMIService) throws RemoteException {
        rmiClientInterface = clientRMIService;
    }

    /** Overriding RMIClientHandler
     * Called when a user selects the Map
     * @param map map selected
     * @throws RemoteException
     */
    @Override
    public void sendMap(Map map) throws RemoteException {
        user.getGame().getGameController().setMap(map);
    }

    @Override
    public boolean sendBuyableObject(ArrayList<BuyableWrapper> buyableWrappers) throws RemoteException {
        return user.getGame().getGameController().onReceiveBuyableObject(buyableWrappers);
    }

    @Override
    public boolean buyObject(ArrayList<BuyableWrapper> buyableWrappers) throws RemoteException {
        return user.getGame().getGameController().onBuyObject(user,buyableWrappers);
    }

    @Override
    public void onRemoveItem(BuyableWrapper item) throws RemoteException {
        user.getGame().getGameController().onRemoveItem(item);
    }

    @Override
    public void onFinishSellPhase() throws RemoteException {
        user.getGameController().onFinishSellPhase(user);
    }

    @Override
    public void onFinishBuyPhase() throws RemoteException {
        user.getGameController().onFinishBuyPhase(user);

    }

    @Override
    public void getCityRewardBonus(City city1) throws RemoteException {
        System.out.println("get city reward bonus");
        user.getGameController().getCityRewardBonus(city1,user);
    }

    @Override
    public void onSelectPermitCard(PermitCard permitCard) throws RemoteException {
        user.getGameController().onSelectPermitCard(permitCard,user);
    }


    /** Overriding BaseCommunication
     *
     * @param snapshotToSend
     */
    @Override
    public void sendSnapshot(SnapshotToSend snapshotToSend) {
        try {
            rmiClientInterface.sendSnapshot(snapshotToSend);
        } catch (RemoteException e) {
            e.printStackTrace();
            user.setConnected(false);
        }
        catch (NullPointerException e){
            System.out.println("null pointer in rmi communication");
        }
    }

    /** Overriding BaseCommunication
     *  called when is the turn of the user
     */
    @Override
    public void changeRound() {
        //call is your round (with a notification)
        try {
            rmiClientInterface.isYourTurn();
        } catch (RemoteException e) {
            e.printStackTrace();
            user.setConnected(false);
        }
    }

    /** Overriding BaseCommunication
     * Called when sending all maps to user
     * @param availableMaps all maps available
     */
    @Override
    public void sendAvailableMap(ArrayList<Map> availableMaps) {
        try {
            if(rmiClientInterface!=null) {
                rmiClientInterface.sendMap(availableMaps);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            user.setConnected(false);
        }
    }

    @Override
    public void sendSelectedMap(SnapshotToSend snapshotToSend) {
        try {
            rmiClientInterface.gameInitialization(snapshotToSend);
            System.out.println("Sending map to: "+user.getUsername());
        } catch (RemoteException e) {
            e.printStackTrace();
            user.setConnected(false);
        }
    }

    @Override
    public void finishTurn() {
        try {
            System.out.println("user "+user.getUsername()+" has finished turn");
            rmiClientInterface.finishTurn();
        } catch (RemoteException e) {
            e.printStackTrace();
            user.setConnected(false);
        }
    }

    @Override
    public void sendStartMarket() {
        try {
            rmiClientInterface.onStartMarket();
        } catch (RemoteException e) {
            e.printStackTrace();
            user.setConnected(false);
        }
    }

    @Override
    public void sendStartBuyPhase() {
        try {
            rmiClientInterface.onStartBuyPhase();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disableMarketPhase() {
        try {
            rmiClientInterface.disableMarketPhase();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void selectPermitCard() {
        try {
            rmiClientInterface.selectPermitCard();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void selectCityRewardBonus(SnapshotToSend snapshotToSend) {
        try {
            rmiClientInterface.selectCityRewardBonus(snapshotToSend);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void setUser(User user) {
        this.user = user;
    }
}
