package Server.NetworkInterface.Communication;

import CommonModel.GameModel.Action.Action;
import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.City.City;
import CommonModel.GameModel.Market.BuyableWrapper;
import CommonModel.Snapshot.BaseUser;
import CommonModel.Snapshot.SnapshotToSend;
import RMIInterface.RMIClientHandler;
import RMIInterface.RMIClientInterface;
import Server.Controller.GamesManager;
import Server.Model.Map;
import Server.Model.User;
import Utilities.Exception.ActionNotPossibleException;

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
        this.gamesManager = GamesManager.getInstance();
        UnicastRemoteObject.exportObject(this, 0);
    }


    /**
     * Overriding RMIClientHandler
     *
     * @param username
     * @return
     * @throws RemoteException
     */
    @Override
    public boolean tryToSetName(String username) throws RemoteException {
        if (!this.gamesManager.userAlreadyPresent(username)) {
            user.setUsername(username);
            this.gamesManager.addToGame(this.user);
            return true;
        }
        return false;
    }

    /**
     * Overriding RMIClientHandler
     *
     * @param electCouncilor
     * @throws ActionNotPossibleException
     */
    @Override
    public void test(Action electCouncilor) throws ActionNotPossibleException {
        electCouncilor.doAction(this.user.getGame(), this.user);
    }

    /**
     * Overriding RMIClientHandler
     * Called by client when the remote object is exported
     *
     * @param clientRMIService
     * @throws RemoteException
     */
    @Override
    public void sendRemoteClientObject(RMIClientInterface clientRMIService) throws RemoteException {
        this.rmiClientInterface = clientRMIService;
    }

    /**
     * Overriding RMIClientHandler
     * Called when a user selects the Map
     *
     * @param map map selected
     * @throws RemoteException
     */
    @Override
    public void sendMap(Map map) throws RemoteException {
        this.user.getGame().getGameController().setMap(map);
    }

    @Override
    public boolean sendBuyableObject(ArrayList<BuyableWrapper> buyableWrappers) throws RemoteException {
        return this.user.getGame().getGameController().onReceiveBuyableObject(buyableWrappers);
    }

    @Override
    public boolean buyObject(ArrayList<BuyableWrapper> buyableWrappers) throws RemoteException {
        return this.user.getGame().getGameController().onBuyObject(this.user, buyableWrappers);
    }

    @Override
    public void onRemoveItem(BuyableWrapper item) throws RemoteException {
        this.user.getGame().getGameController().onRemoveItem(item);
    }

    @Override
    public void onFinishSellPhase() throws RemoteException {
        this.user.getGameController().onFinishSellPhase(this.user);
    }

    @Override
    public void onFinishBuyPhase() throws RemoteException {
        this.user.getGameController().onFinishBuyPhase(this.user);

    }

    @Override
    public void getCityRewardBonus(City city1) throws RemoteException, ActionNotPossibleException {
        System.out.println("get city reward bonus");
        this.user.getGameController().getCityRewardBonus(city1, this.user);
    }

    @Override
    public void onSelectPermitCard(PermitCard permitCard) throws RemoteException {
        this.user.getGameController().onSelectPermitCard(permitCard, this.user);
    }

    @Override
    public void finishRound() throws RemoteException {
        this.user.getGameController().onUserPass(this.user);

    }

    @Override
    public void onSelectOldPermitCard(PermitCard permitCard) {
        this.user.getGameController().onSelectOldPermitCard(this.user, permitCard);
    }


    /**
     * Overriding BaseCommunication
     *
     * @param snapshotToSend
     */
    @Override
    public void sendSnapshot(SnapshotToSend snapshotToSend) {
        try {
            this.rmiClientInterface.sendSnapshot(snapshotToSend);
        } catch (RemoteException e) {
            this.user.setConnected(false);
        } catch (NullPointerException e) {
            System.out.println("null pointer in rmi communication");
        }
    }

    /**
     * Overriding BaseCommunication
     * called when is the turn of the user
     */
    @Override
    public void changeRound() {
        //call is your round (with a notification)
        try {
            this.rmiClientInterface.isYourTurn();
        } catch (RemoteException e) {
            // e.printStackTrace();
            this.user.setConnected(false);
            this.user.getGameController().onFinishRound(this.user);
        }
    }

    /**
     * Overriding BaseCommunication
     * Called when sending all maps to user
     *
     * @param availableMaps all maps available
     */
    @Override
    public void sendAvailableMap(ArrayList<Map> availableMaps) {
        try {
            if (this.rmiClientInterface != null) {
                this.rmiClientInterface.sendMap(availableMaps);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            this.user.setConnected(false);
            this.user.getGameController().changeMasterUser();
        }
    }

    @Override
    public void sendSelectedMap(SnapshotToSend snapshotToSend) {
        try {
            this.rmiClientInterface.gameInitialization(snapshotToSend);
            System.out.println("Sending map to: " + this.user.getUsername());
        } catch (RemoteException e) {
            e.printStackTrace();
            this.user.setConnected(false);
        }
    }

    @Override
    public void finishTurn() {
        try {
            System.out.println("user " + this.user.getUsername() + " has finished turn");
            this.rmiClientInterface.finishTurn();
        } catch (RemoteException e) {
            //e.printStackTrace();
            this.user.setConnected(false);
        }
    }

    @Override
    public void sendStartMarket() {
        try {
            this.rmiClientInterface.onStartMarket();
        } catch (RemoteException e) {
            this.user.setConnected(false);
        }
    }

    @Override
    public void sendStartBuyPhase() {
        try {
            this.rmiClientInterface.onStartBuyPhase();
        } catch (RemoteException e) {
        }
    }

    @Override
    public void disableMarketPhase() {
        try {
            this.rmiClientInterface.disableMarketPhase();
        } catch (RemoteException e) {
        }
    }

    @Override
    public void selectPermitCard() {
        try {
            this.rmiClientInterface.selectPermitCard();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void selectCityRewardBonus(SnapshotToSend snapshotToSend) {
        try {
            this.rmiClientInterface.selectCityRewardBonus(snapshotToSend);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void moveKing(ArrayList<City> kingPath) {
        try {
            this.rmiClientInterface.moveKing(kingPath);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMatchFinishedWithWin(ArrayList<BaseUser> finalSnapshot) {
        try {
            this.rmiClientInterface.sendMatchFinishedWithWin(finalSnapshot);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ping() {
        if (this.user.isConnected()) {
            try {
                this.rmiClientInterface.ping();
            } catch (RemoteException e) {
                if (this.user.isConnected()) {
                    this.user.setConnected(false);
                    this.user.getGameController().onUserDisconnected(this.user);
                    this.user.getGameController().cleanGame();
                }
            }
        }
    }

    @Override
    public void selectOldPermitCard() {
        try {
            this.rmiClientInterface.selectOldPermiCard();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendUserDisconnect(String username) {
        try {
            this.rmiClientInterface.onUserDisconnect(username);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void setUser(User user) {
        this.user = user;
    }
}
