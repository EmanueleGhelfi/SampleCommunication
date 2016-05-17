package CommonModel.GameModel.Action;

import CommonModel.GameModel.ActionNotPossibleException;
import Server.Model.Game;
import Server.UserClasses.User;

import java.io.Serializable;

/**
 * Created by Emanuele on 16/05/2016.
 */
public abstract class Action implements Serializable {


    protected String type;

    public abstract void doAction(Game game, User user) throws ActionNotPossibleException;
    void removeAction(Game game,User user){
        switch (type) {
            case "MAIN_ACTION":
                user.setMainActionCounter(user.getMainActionCounter()-1);
                break;
            case "FAST_ACTION":
                user.setFastActionCounter(user.getFastActionCounter()-1);
                break;
        }
    };

    String getType(){
        return type;
    }
}
