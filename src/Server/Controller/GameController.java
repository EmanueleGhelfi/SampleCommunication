package Server.Controller;

import CommonModel.GameModel.Action.Action;
import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import CommonModel.GameModel.City.RegionName;
import CommonModel.GameModel.Council.King;
import CommonModel.GameModel.Market.BuyableObject;
import CommonModel.GameModel.Market.BuyableWrapper;
import CommonModel.Snapshot.SnapshotToSend;
import Server.Model.Game;
import Server.Model.Map;
import Server.Model.User;
import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;
import Utilities.Exception.AlreadyPresentException;
import Utilities.Exception.MapsNotFoundException;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Emanuele on 13/05/2016.
 */
public class GameController implements Serializable{

    private Game game;
    private TimerTask timerTask;
    private Timer timer;
    private int duration = Constants.GAME_TIMEOUT;
    private ArrayList<Map> availableMaps = new ArrayList<>();

    public GameController() {
    }

    public GameController(Game game) {
        this.game = game;
        this.timer = new Timer();
        try {
            availableMaps = Map.readAllMap();
        } catch (MapsNotFoundException e) {
            System.out.println(e);
        }
    }

    public void startTimer() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                notifyStarted();
            }
        };
    }

    /**
     * Called when init game
     */
    public void notifyStarted() {
        int userCounter = 0;
        game.setStarted(true);
        for (User user: game.getUsers()){
            user.setHelpers(Constants.DEFAULT_HELPER_COUNTER + userCounter);
            user.setCoinPathPosition(Constants.FIRST_INITIAL_POSITION_ON_MONEY_PATH + userCounter);
            user.setNobilityPathPosition(game.getNobilityPath().getPosition()[Constants.INITIAL_POSITION_ON_NOBILITY_PATH]);
            user.setVictoryPathPosition(Constants.INITIAL_POSITION_ON_VICTORY_PATH);

            ArrayList<PoliticCard> politicCardArrayList = new ArrayList<>();
            for(int cont = 0; cont < Constants.DEFAULT_POLITIC_CARD_HAND; cont++){
                politicCardArrayList.add(game.getPoliticCards().drawACard());
            }
            user.setPoliticCards(politicCardArrayList);

            for (int i = 0; i<2;i++){
                user.addPermitCard(game.getPermitDeck(RegionName.HILL).getPermitCardVisible(i));
            }


            userCounter++;
        }

        /*
        for (User user: game.getUsers()) {
            System.out.println("Sending to "+user.getUsername());
            //user.notifyGameStart();
            initializeGame(user);
        }
        */

        // send map to first user
        ArrayList<User> users = new ArrayList<>(game.getUsers());
        sendAvailableMap(users.get(0));

    }

    public void cancelTimeout() {
        timer.cancel();
    }

    public void setTimeout() {
        if(timer==null){
            timer = new Timer();
        }
        else{
            timer.cancel();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    notifyStarted();
                }
            };
            timer = new Timer();
        }
        timer.schedule(timerTask,duration);
    }

    /*
    public void initializeGame(User user){
            System.out.println("GAMECONTROLLER -> Initializing Game, sending snapshot to: "+user.getUsername());
            SnapshotToSend snapshotToSend = new SnapshotToSend(game, user);
            user.getBaseCommunication().sendSnapshot(snapshotToSend);
    }
    */

    /**
     * create snapshot and change round
     * @param user user that has finished round
     */
    //TODO: manage disconnection
    public void onFinishRound(User user) {
        System.out.println("on finish round called");
        user.getBaseCommunication().finishTurn();
        ArrayList<User> userArrayList = new ArrayList<>(game.getUsers());
        for(int cont = 0; cont < game.getUsers().size(); cont++){
            System.out.println("GAMECONTROLLER <- Sending Snapshot to :" + userArrayList.get(cont).getUsername());
            SnapshotToSend snapshotToSend = new SnapshotToSend(game, user);
            user.getBaseCommunication().sendSnapshot(snapshotToSend);
            if(user.equals(userArrayList.get(cont))){
                int nextUser = cont+1;
                while (!userArrayList.get((nextUser)%game.getUsers().size()).isConnected() || nextUser%game.getUsers().size()==cont){
                    System.out.println("user not connected "+ userArrayList.get((nextUser)%game.getUsers().size()));
                    nextUser++;
                }
                if((nextUser%game.getUsers().size())==cont){
                    onAllUserDisconnected();
                }
                else {
                    userArrayList.get((nextUser) % game.getUsers().size()).setMainActionCounter(Constants.MAIN_ACTION_POSSIBLE);
                    userArrayList.get((nextUser) % game.getUsers().size()).setFastActionCounter(Constants.FAST_ACTION_POSSIBLE);
                    userArrayList.get((nextUser) % game.getUsers().size()).getBaseCommunication().changeRound();
                }
            }
        }
    }

    private void onAllUserDisconnected() {
        System.out.println("All user Disconnected! I don't know what to do");
    }

    public void doAction(Action action, User user) throws ActionNotPossibleException {
        action.doAction(game,user);
    }

    private void sendAvailableMap(User userToAdd) {
        userToAdd.getBaseCommunication().sendAvailableMap(availableMaps);
    }

    public void setMap(Map map) {
        System.out.println("selected map"+map.getMapName());
        System.out.println("available map"+availableMaps);
        if(availableMaps.contains(map)){
            System.out.println("MAP PRESENT");
            for (Map mapToSelect : availableMaps) {
                if(mapToSelect.equals(map)){
                    game.setMap(map);
                    game.setKing(new King(map.getCity().get(0)));
                    for (User user: game.getUsers()) {
                        SnapshotToSend snapshotToSend = new SnapshotToSend(game,user);
                        // init game
                        user.getBaseCommunication().sendSelectedMap(snapshotToSend);
                    }
                    selectFirstPlayer();
                    break;
                }
            }
        }
        else{
            System.out.println("MAP NOT PRESENT");
        }
    }

    private void selectFirstPlayer() {
        ArrayList<User> users = new ArrayList<>(game.getUsers());
       // users.get(0).getBaseCommunication().send
        users.get(0).setMainActionCounter(Constants.MAIN_ACTION_POSSIBLE);
        users.get(0).setFastActionCounter(Constants.FAST_ACTION_POSSIBLE);
        users.get(0).getBaseCommunication().changeRound();


        for(int i = 1;i< users.size();i++){
            users.get(i).getBaseCommunication().finishTurn();

        }

        sendSnapshotToAll();
    }

    public void sendSnapshotToAll() {
        for (User user : game.getUsers()) {
            SnapshotToSend snapshotToSend = new SnapshotToSend(game, user);
            user.getBaseCommunication().sendSnapshot(snapshotToSend);
        }
    }

    public boolean onReceiveBuyableObject(ArrayList<BuyableWrapper> buyableWrappers) {
        for (BuyableWrapper buyableWrapper: buyableWrappers) {
            try {
                game.addBuyableWrapper(buyableWrapper);
            } catch (AlreadyPresentException e) {
                System.out.println("L'oggetto è già in vendita!");
            }
        }
        System.out.println("Dopo aver aggiunto: "+buyableWrappers);
        sendSnapshotToAll();
        return true;

    }

    public boolean onBuyObject(User user, BuyableWrapper[] buyableWrappers) {
        int counter = 0;
        for (BuyableWrapper buyableWrapper : buyableWrappers) {
            try {
                game.getMoneyPath().goAhead(user, buyableWrapper.getCost());
                if(buyableWrapper.getBuyableObject() instanceof PermitCard){
                    System.out.println("found permit card");
                    game.getUser(buyableWrapper.getUsername()).removePermitCard((PermitCard) buyableWrapper.getBuyableObject());
                    user.addPermitCard((PermitCard) buyableWrapper.getBuyableObject());
                }
                else if(buyableWrapper.getBuyableObject() instanceof PoliticCard){
                    System.out.println("found politic card");
                    game.getUser(buyableWrapper.getUsername()).removePoliticCard((PoliticCard) buyableWrapper.getBuyableObject());
                    user.addPoliticCard((PoliticCard)buyableWrapper.getBuyableObject());
                }
                counter++;
            } catch (ActionNotPossibleException e) {

            }

        }

        if(counter==buyableWrappers.length){
            return true;
        }
        else return false;
    }
}
