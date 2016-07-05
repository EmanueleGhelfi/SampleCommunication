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
public class NobilityBonus implements Bonus, Serializable {

    private final int nobilityNumber;

    public NobilityBonus() {
        Random randomGenerator = new Random();
        this.nobilityNumber = randomGenerator.nextInt(Constants.RANDOM_NOBILITY_FIRST_PARAMETER) + Constants.RANDOM_NOBILITY_SECOND_PARAMETER;
    }

    @Override
    public void getBonus(User user, Game game) throws ActionNotPossibleException {
        game.getNobilityPath().goAhead(user, this.nobilityNumber);
    }

    @Override
    public String getBonusName() {
        return "NobilityBonus";
    }

    @Override
    public ArrayList<Bonus> getBonusArrayList() {
        return null;
    }

    @Override
    public ArrayList<String> getBonusURL() {
        ArrayList<String> toReturn = new ArrayList<String>();
        toReturn.add(Constants.IMAGE_PATH + "NobilityArrow.png");
        return toReturn;

    }

    @Override
    public ArrayList<String> getBonusInfo() {
        ArrayList<String> toReturn = new ArrayList<String>();
        toReturn.add(this.nobilityNumber + "");
        return toReturn;
    }


    @Override
    public String toString() {
        return "Nobility Path Bonus: +" + this.nobilityNumber;
    }
}
