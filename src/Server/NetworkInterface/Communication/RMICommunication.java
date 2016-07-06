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
import Utilities.Class.InternalLog;
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
        gamesManager = GamesManager.getInstance();
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
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        if (!gamesManager.userAlreadyPresent(username)) {
            this.user.setUsername(username);
            gamesManager.addToGame(user);
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
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        electCouncilor.doAction(user.getGame(), user);
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
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        rmiClientInterface = clientRMIService;
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
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        user.getGame().getGameController().setMap(map);
    }

    @Override
    public boolean sendBuyableObject(ArrayList<BuyableWrapper> buyableWrappers) throws RemoteException {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        return user.getGame().getGameController().onReceiveBuyableObject(buyableWrappers);
    }

    @Override
    public boolean buyObject(ArrayList<BuyableWrapper> buyableWrappers) throws RemoteException {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        return user.getGame().getGameController().onBuyObject(user, buyableWrappers);
    }

    @Override
    public void onRemoveItem(BuyableWrapper item) throws RemoteException {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        user.getGame().getGameController().onRemoveItem(item);
    }

    @Override
    public void onFinishSellPhase() throws RemoteException {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        user.getGameController().onFinishSellPhase(user);
    }

    @Override
    public void onFinishBuyPhase() throws RemoteException {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        user.getGameController().onFinishBuyPhase(user);

    }

    @Override
    public void getCityRewardBonus(City city1) throws RemoteException, ActionNotPossibleException {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        user.getGameController().getCityRewardBonus(city1, user);
    }

    @Override
    public void onSelectPermitCard(PermitCard permitCard) throws RemoteException {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        user.getGameController().onSelectPermitCard(permitCard, user);
    }

    @Override
    public void finishRound() throws RemoteException {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        user.getGameController().onUserPass(user);

    }

    @Override
    public void onSelectOldPermitCard(PermitCard permitCard) {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        user.getGameController().onSelectOldPermitCard(user, permitCard);
    }

    /**
     * Overriding BaseCommunication
     *
     * @param snapshotToSend
     */
    @Override
    public void sendSnapshot(SnapshotToSend snapshotToSend) {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        try {
            rmiClientInterface.sendSnapshot(snapshotToSend);
        } catch (RemoteException e) {
            user.setConnected(false);
        } catch (NullPointerException e) {
        }
    }

    /**
     * Overriding BaseCommunication
     * called when is the turn of the user
     */
    @Override
    public void changeRound() {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        //call is your round (with a notification)
        try {
            rmiClientInterface.isYourTurn();
        } catch (RemoteException e) {
            // e.printStackTrace();
            user.setConnected(false);
            user.getGameController().onFinishRound(user);
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
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        try {
            if (rmiClientInterface != null) {
                rmiClientInterface.sendMap(availableMaps);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            user.setConnected(false);
            user.getGameController().changeMasterUser();
        }
    }

    @Override
    public void sendSelectedMap(SnapshotToSend snapshotToSend) {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        try {
            rmiClientInterface.gameInitialization(snapshotToSend);
        } catch (RemoteException e) {
            e.printStackTrace();
            user.setConnected(false);
        }
    }

    @Override
    public void finishTurn() {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        try {
            rmiClientInterface.finishTurn();
        } catch (RemoteException e) {
            user.setConnected(false);
        }
    }

    @Override
    public void sendStartMarket() {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        try {
            rmiClientInterface.onStartMarket();
        } catch (RemoteException e) {
            user.setConnected(false);
        }
    }

    @Override
    public void sendStartBuyPhase() {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        try {
            rmiClientInterface.onStartBuyPhase();
        } catch (RemoteException e) {
        }
    }

    @Override
    public void disableMarketPhase() {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        try {
            rmiClientInterface.disableMarketPhase();
        } catch (RemoteException e) {
        }
    }

    @Override
    public void selectPermitCard() {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        try {
            rmiClientInterface.selectPermitCard();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void selectCityRewardBonus(SnapshotToSend snapshotToSend) {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        try {
            rmiClientInterface.selectCityRewardBonus(snapshotToSend);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void moveKing(ArrayList<City> kingPath) {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        try {
            rmiClientInterface.moveKing(kingPath);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMatchFinishedWithWin(ArrayList<BaseUser> finalSnapshot) {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        try {
            rmiClientInterface.sendMatchFinishedWithWin(finalSnapshot);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ping() {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        if (user.isConnected()) {
            try {
                rmiClientInterface.ping();
            } catch (RemoteException e) {
                if (user.isConnected()) {
                    user.setConnected(false);
                    user.getGameController().onUserDisconnected(user);
                    user.getGameController().cleanGame();
                }
            }
        }
    }

    @Override
    public void selectOldPermitCard() {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        try {
            rmiClientInterface.selectOldPermiCard();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendUserDisconnect(String username) {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        try {
            rmiClientInterface.onUserDisconnect(username);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }
}
