package Server.Controller;

import CommonModel.GameModel.Action.Action;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticColor;
import CommonModel.GameModel.City.Color;
import CommonModel.GameModel.Path.Position;
import CommonModel.Snapshot.SnapshotToSend;
import Server.Model.Game;
import Server.Model.User;
import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Emanuele on 13/05/2016.
 */
public class GameController implements Serializable{

    private Game game;
    private TimerTask timerTask;
    private Timer timer;
    private int duration = Constants.GAME_TIMEOUT;

    public GameController() {
    }

    public GameController(Game game) {
        this.game = game;
        this.timer = new Timer();
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
        game.setStarted(true);
        for (User user: game.getUsers()){
            user.setHelpers(5);
            user.setCoinPathPosition(10);
            user.setMainActionCounter(0);
            user.setFastActionCounter(0);
            user.setNobilityPathPosition(game.getNobilityPath().getPosition()[10]);
            ArrayList<PoliticCard> politicCardArrayList = new ArrayList<>();
            for(PoliticColor color: PoliticColor.values()){
                politicCardArrayList.add(new PoliticCard(color, false));
            }
            user.setPoliticCards(politicCardArrayList);
            user.setVictoryPathPosition(10);
        }
        for (User user: game.getUsers()) {
            System.out.println("Sending to "+user.getUsername());
            user.notifyGameStart();
            initializeGame();
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

    public void initializeGame(){
        for(User user : game.getUsers()){
            System.out.println("GAMECONTROLLER -> Initializing Game, sending snapshot to: "+user.getUsername());
            SnapshotToSend snapshotToSend = new SnapshotToSend(game, user);
            user.getBaseCommunication().sendSnapshot(snapshotToSend);
        }
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
}
