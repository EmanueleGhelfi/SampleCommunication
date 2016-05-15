package Server.Controller;

import Server.Model.Game;
import Server.UserClasses.User;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Emanuele on 13/05/2016.
 */
public class GameController {

    private Game game;
    private TimerTask timerTask;
    private Timer timer;
    private int duration = 20000;

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
}