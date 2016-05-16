package CommonModel.GameModel.Action;

import Server.Model.Game;
import Server.UserClasses.User;

/**
 * Created by Emanuele on 16/05/2016.
 */
public interface Action {

    void doAction(Game game, User user);
    String getType();
}
