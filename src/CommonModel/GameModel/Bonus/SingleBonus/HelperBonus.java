package CommonModel.GameModel.Bonus.SingleBonus;

import CommonModel.GameModel.Bonus.Generic.Bonus;
import Server.Model.Game;
import Server.Model.User;
import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Giulio on 13/05/2016.
 */
public class HelperBonus implements Bonus, Serializable {

    private int helperNumber;

    public HelperBonus() {
        Random randomGenerator = new Random();
        helperNumber = randomGenerator.nextInt(Constants.RANDOM_HELPER_FIRST_PARAMETER) + Constants.RANDOM_HELPER_SECOND_PARAMETER;
    }

    @Override
    public void getBonus(User user, Game game) throws ActionNotPossibleException {
        user.setHelpers(user.getHelpers().size() + helperNumber);
    }

    @Override
    public String getBonusName() {
        return "HelperBonus";
    }

    @Override
    public ArrayList<Bonus> getBonusArrayList() {
        return null;
    }

    @Override
    public ArrayList<String> getBonusURL() {
        ArrayList<String> toReturn = new ArrayList<String>();
        toReturn.add(Constants.IMAGE_PATH + "Helper.png");
        return toReturn;
    }

    @Override
    public ArrayList<String> getBonusInfo() {
        ArrayList<String> toReturn = new ArrayList<String>();
        toReturn.add(helperNumber + "");
        return toReturn;
    }

    @Override
    public String toString() {
        return "Helper bonus: +" + helperNumber;
    }
}
