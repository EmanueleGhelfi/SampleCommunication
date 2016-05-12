package Server.Managers;

import Server.UserClasses.User;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Emanuele on 11/05/2016.
 */
public class Game {

    /**
     * True if game is full (There game is started and the players are playing)
     */
    private boolean started;

    /**
     * All users in the game with their name
     */

    private Timer timer;
    private TimerTask timerTask;

    private int duration = 20000;
    private HashMap<String,User> usersInGame = new HashMap<>();

    public Game() {
        this.started = false;
        timerTask = new TimerTask() {
            @Override
            public void run() {
                notifyStarted();
            }
        };
    }

    private void notifyStarted() {
        started=true;
        for (User user: usersInGame.values()) {
            System.out.println("Sending to "+user.getUsername());
            user.notifyGameStart();
        }
    }

    public boolean isStarted() {
        return started;
    }


    public boolean addUserToGame(User userToAdd) {
        System.out.println("ADDING A USER TO A GAME "+userToAdd);
        if(!usersInGame.containsKey(userToAdd.getUsername())){
            usersInGame.put(userToAdd.getUsername(),userToAdd);
            if(usersInGame.size()>=2 && usersInGame.size()<4){
                setTimeout();
            }
            else if(usersInGame.size()>=2){
                cancelTimeout();
                notifyStarted();
            }
            return true;
        }
        return false;
    }

    private void cancelTimeout() {
        System.out.println("Cancelled timeout");
        timer.cancel();
    }

    private void setTimeout() {
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

    public void OnMessage(String message) {
        System.out.println("In Game: received "+message);
        for (User user: usersInGame.values()) {
            System.out.println("sending a message to "+user.getUsername());
            user.getBaseCommunication().sendMessage(message);
        }
    }
}
