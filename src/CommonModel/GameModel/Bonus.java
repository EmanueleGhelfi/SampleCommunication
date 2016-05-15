package CommonModel.GameModel;

import Server.Model.Game;
import Server.UserClasses.User;

/**
 * Created by Giulio on 13/05/2016.
 */
public interface Bonus {

    void getBonus(User user, Game game) throws ActionNotPossibleException;

}
