package CommonModel.GameModel.Bonus.SingleBonus;

import CommonModel.GameModel.Bonus.Generic.Bonus;
import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;
import Server.Model.Game;
import Server.Model.User;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Giulio on 13/05/2016.
 */
public class NewMainAction implements Bonus,Serializable {

    public NewMainAction() {
    }

    @Override
    public void getBonus(User user, Game game) throws ActionNotPossibleException {
        user.setMainActionCounter(user.getMainActionCounter()+ Constants.RANDOM_MAINACTION_FIRST_PARAMETER);
    }

    @Override
    public String getBonusName() {
        return null;
    }

    @Override
    public ArrayList<Bonus> getBonusArrayList() {
        return null;
    }

}
