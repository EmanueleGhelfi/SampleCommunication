package CommonModel.GameModel.Bonus.Generic;

import Utilities.Exception.ActionNotPossibleException;
import Server.Model.Game;
import Server.Model.User;
import java.util.ArrayList;

/**
 * Created by Giulio on 13/05/2016.
 */
public interface Bonus {

    void getBonus(User user, Game game) throws ActionNotPossibleException;

    String getBonusName();

    ArrayList<Bonus> getBonusArrayList();

    ArrayList<String> getBonusURL();

    ArrayList<String> getBonusInfo();


}
