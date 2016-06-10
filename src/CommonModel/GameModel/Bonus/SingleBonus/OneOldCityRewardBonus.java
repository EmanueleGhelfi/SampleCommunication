package CommonModel.GameModel.Bonus.SingleBonus;

import CommonModel.GameModel.Bonus.Generic.Bonus;
import Server.Model.Game;
import Server.Model.User;
import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Get one city bonus (also the ones used) but not the ones with nobility bonus
 * Created by Giulio on 18/05/2016.
 */
public class OneOldCityRewardBonus implements Bonus,Serializable {

    //TODO teso
    @Override
    public void getBonus(User user, Game game) throws ActionNotPossibleException {
        user.getBaseCommunication().selectCityRewardBonus();
    }

    @Override
    public String getBonusName() {
        return "OneOldCityRewardBonus";
    }

    @Override
    public ArrayList<Bonus> getBonusArrayList() {
        return null;
    }

    @Override
    public ArrayList<String> getBonusURL() {

        ArrayList<String> toReturn = new ArrayList<String>();
        toReturn.add(Constants.IMAGE_PATH+"OneOldCityRewardBonus.png");
        return toReturn;
    }

    @Override
    public ArrayList<String> getBonusInfo() {
        ArrayList<String> toReturn = new ArrayList<String>();
        toReturn.add("");
        return toReturn;
    }
}
