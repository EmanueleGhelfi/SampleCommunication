package CommonModel.GameModel.Bonus.Generic;

import Server.Model.Game;
import Server.Model.User;
import Utilities.Exception.ActionNotPossibleException;

import java.util.ArrayList;

/**
 * Created by Giulio on 13/05/2016.
 */
public interface Bonus {

    /** Getter
     * @param user is the user that gets the bonus
     * @param game is the game
     * @throws ActionNotPossibleException is the exception raised
     */
    void getBonus(User user, Game game) throws ActionNotPossibleException;

    /** Getter
     * @return the bonus name
     */
    String getBonusName();

    /** Getter
     * @return the bonus array list
     */
    ArrayList<Bonus> getBonusArrayList();

    /** Getter
     * @return the bonus url
     */
    ArrayList<String> getBonusURL();

    /** Getter
     * @return the bonus info
     */
    ArrayList<String> getBonusInfo();


}
