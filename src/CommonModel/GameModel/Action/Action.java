package CommonModel.GameModel.Action;

import CommonModel.GameModel.ActionNotPossibleException;
import Server.Model.Game;
import Server.UserClasses.User;

import java.io.Serializable;

/**
 * Created by Emanuele on 16/05/2016.
 */
public interface Action extends Serializable {

    void doAction(Game game, User user) throws ActionNotPossibleException;
    String getType();
}
