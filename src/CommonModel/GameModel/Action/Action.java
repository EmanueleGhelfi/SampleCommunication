package CommonModel.GameModel.Action;

import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;
import Server.Model.Game;
import Server.Model.User;
import java.io.Serializable;

/**
 * Created by Emanuele on 16/05/2016.
 */
public abstract class Action implements Serializable {

    protected String type;

    public abstract void doAction(Game game, User user) throws ActionNotPossibleException;

    void removeAction(Game game,User user){
        switch (type) {
            case Constants.MAIN_ACTION:
                user.setMainActionCounter(user.getMainActionCounter()-1);
                break;
            case Constants.FAST_ACTION:
                user.setFastActionCounter(user.getFastActionCounter()-1);
                break;
        }
    }

    String getType(){
        return type;
    }
}
