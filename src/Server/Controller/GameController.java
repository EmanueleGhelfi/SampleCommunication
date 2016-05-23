package Server.Controller;

import CommonModel.GameModel.Action.Action;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticColor;
import CommonModel.Snapshot.SnapshotToSend;
import Server.Model.Game;
import Server.Model.Map;
import Server.Model.User;
import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;
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

    public void notifyStarted() {
        int userCounter = 0;
        game.setStarted(true);
        for (User user: game.getUsers()){
            user.setMainActionCounter(Constants.DEFAULT_MAIN_ACTION_COUNTER);
            user.setFastActionCounter(Constants.DEFAULT_FAST_ACTION_COUNTER);
            user.setHelpers(Constants.DEFAULT_HELPER_COUNTER + userCounter);
            user.setCoinPathPosition(Constants.FIRST_INITIAL_POSITION_ON_MONEY_PATH + userCounter);
            user.setNobilityPathPosition(game.getNobilityPath().getPosition()[Constants.INITIAL_POSITION_ON_NOBILITY_PATH]);
            user.setVictoryPathPosition(Constants.INITIAL_POSITION_ON_VICTORY_PATH);

            ArrayList<PoliticCard> politicCardArrayList = new ArrayList<>();
            for(int cont = 0; cont < Constants.DEFAULT_POLITIC_CARD_HAND; cont++){
                politicCardArrayList.add(game.getPoliticCards().drawACard());
                System.out.println("MatchController notify game started <- " + cont + " "
                        + politicCardArrayList.get(cont));
            }
            user.setPoliticCards(politicCardArrayList);

            userCounter++;
        }
        for (User user: game.getUsers()) {
            System.out.println("Sending to "+user.getUsername());
            //user.notifyGameStart();
            initializeGame(user);
        }
    }

    public void cancelTimeout() {
        System.out.println("Cancelled timeout");
        timer.cancel();
    }

    public void setTimeout() {
        if(timer==null){
            System.out.println("Started timeout for the first time");
            timer = new Timer();
        }
        else{
            System.out.println("Restarted timeout");
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

    public void initializeGame(User user){
            System.out.println("GAMECONTROLLER -> Initializing Game, sending snapshot to: "+user.getUsername());
            SnapshotToSend snapshotToSend = new SnapshotToSend(game, user);
            user.getBaseCommunication().sendSnapshot(snapshotToSend);
    }

    /**
     * create snapshot and change round
     * @param user user that has finished round
     */
    public void onFinishRound(User user) {
        ArrayList<User> userArrayList = new ArrayList<>(game.getUsers());
        for(int cont = 0; cont < game.getUsers().size(); cont++){
            System.out.println("GAMECONTROLLER <- Sending Snapshot to :" + userArrayList.get(cont).getUsername());
            SnapshotToSend snapshotToSend = new SnapshotToSend(game, user);
            user.getBaseCommunication().sendSnapshot(snapshotToSend);
            if(user.equals(userArrayList.get(cont))){
                userArrayList.get((cont+1)%game.getUsers().size()).setMainActionCounter(Constants.MAIN_ACTION_POSSIBLE);
                userArrayList.get((cont+1)%game.getUsers().size()).setFastActionCounter(Constants.FAST_ACTION_POSSIBLE);
                userArrayList.get((cont+1)%game.getUsers().size()).getBaseCommunication().changeRound();
            }
        }
    }

    public void doAction(Action action, User user) throws ActionNotPossibleException {
        action.doAction(game,user);
    }

    public void sendAvailableMap(User userToAdd) {
        userToAdd.getBaseCommunication().sendAvailableMap(availableMaps);
    }

    public void setMap(Map map) {
        System.out.println(map);
        System.out.println(availableMaps);
        if(availableMaps.contains(map)){
            System.out.println("MAP PRESENT");
            for (Map mapToSelect : availableMaps) {
                if(mapToSelect.equals(map)){
                    game.setMap(map);
                    for (User user: game.getUsers()) {
                        SnapshotToSend snapshotToSend = new SnapshotToSend(game,user);
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

    }
}
