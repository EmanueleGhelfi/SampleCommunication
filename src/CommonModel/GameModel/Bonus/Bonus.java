package CommonModel.GameModel.Bonus;

import Utilities.Exception.ActionNotPossibleException;
import Server.Model.Game;
import Server.Model.User;

/**
 * Created by Giulio on 13/05/2016.
 */
public interface Bonus {

    void getBonus(User user, Game game) throws ActionNotPossibleException;

}
