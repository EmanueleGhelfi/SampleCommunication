package CommonModel.GameModel.Bonus.SingleBonus;

import CommonModel.GameModel.Bonus.Generic.Bonus;
import CommonModel.GameModel.City.City;
import CommonModel.Snapshot.SnapshotToSend;
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
public class OneOldCityRewardBonus implements Bonus, Serializable {

    @Override
    public void getBonus(User user, Game game) throws ActionNotPossibleException {
        if (user.getUsersEmporium().size() > 0) {
            if (checkBonusType(user.getUsersEmporium())) {
                user.addOptionalActionCounter();
                user.getBaseCommunication().selectCityRewardBonus(new SnapshotToSend(game, user));
            }
        }
    }

    private boolean checkBonusType(ArrayList<City> usersEmporium) {
        boolean toReturn = true;
        for (City city : usersEmporium) {
            toReturn = true;
            for (Bonus bonus : city.getBonus().getBonusArrayList()) {
                if (bonus instanceof NobilityBonus) {
                    toReturn = false;
                }
            }
            if (toReturn)
                return true;
        }

        return toReturn;

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
        toReturn.add(Constants.IMAGE_PATH + "OneOldCityRewardBonus.png");
        return toReturn;
    }

    @Override
    public ArrayList<String> getBonusInfo() {
        ArrayList<String> toReturn = new ArrayList<String>();
        toReturn.add("");
        return toReturn;
    }

    @Override
    public String toString() {
        return "OneOldCityRewardBonus";
    }
}
