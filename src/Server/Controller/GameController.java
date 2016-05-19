package Server.Controller;

import CommonModel.GameModel.Action.Action;
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
            SnapshotToSend snapshotToSend = new SnapshotToSend(game, user);
            user.getBaseCommunication().sendSnapshot(snapshotToSend);
        }
    }

    public void onFinishRound(User user) {
        ArrayList<User> userArrayList = new ArrayList<>(game.getUsers());
        for(int cont = 0; cont < game.getUsers().size(); cont++){
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
