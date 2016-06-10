package CommonModel.GameModel.Bonus.SingleBonus;

import CommonModel.GameModel.Bonus.Generic.Bonus;
import Utilities.Exception.ActionNotPossibleException;
import Server.Model.Game;
import Server.Model.User;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Giulio on 14/05/2016.
 */
public class TwoOldCityRewardBonus implements Bonus, Serializable {

    @Override
    public void getBonus(User user, Game game) throws ActionNotPossibleException {
    }

    @Override
    public String getBonusName() {
        return "TwoOldCityRewardBonus";
    }

    @Override
    public ArrayList<Bonus> getBonusArrayList() {
        return null;
    }

    @Override
    public String getBonusURL() {
        return null;
    }

    @Override
    public String getBonusInfo() {
        return null;
    }

}
