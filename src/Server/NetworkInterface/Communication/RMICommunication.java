package Server.NetworkInterface.Communication;

import ClientPackage.NetworkInterface.ClientRMIService;
import CommonModel.GameModel.Action.Action;
import CommonModel.GameModel.Market.BuyableObject;
import CommonModel.GameModel.Market.BuyableWrapper;
import CommonModel.Snapshot.SnapshotToSend;
import Server.Model.Map;
import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;
import RMIInterface.RMIClientHandler;
import RMIInterface.RMIClientInterface;
import Server.Controller.GameController;
import Server.Controller.GamesManager;
import Server.Model.User;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
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
    public boolean buyObject(BuyableWrapper... buyableWrappers) throws RemoteException {
        user.getGame().getGameController().onBuyObject(user,buyableWrappers);
        return false;
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
            rmiClientInterface.sendMap(availableMaps);
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
    public void setUser(User user) {
        this.user = user;
    }
}
