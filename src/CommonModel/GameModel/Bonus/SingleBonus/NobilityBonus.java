package CommonModel.GameModel.Bonus.SingleBonus;

import CommonModel.GameModel.Bonus.Generic.Bonus;
import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;
import Server.Model.Game;
import Server.Model.User;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Giulio on 13/05/2016.
 */
public class NobilityBonus implements Bonus,Serializable {

    private int nobilityNumber;

    public NobilityBonus() {
        Random randomGenerator = new Random();
        nobilityNumber = randomGenerator.nextInt(Constants.RANDOM_NOBILITY_FIRST_PARAMETER)+Constants.RANDOM_NOBILITY_SECOND_PARAMETER;
    }

    @Override
    public void getBonus(User user, Game game) throws ActionNotPossibleException {
        game.getNobilityPath().goAhead(user, nobilityNumber);
    }

    @Override
    public String getBonusName() {
        return "NobilityBonus";
    }

    @Override
    public ArrayList<Bonus> getBonusArrayList() {
        return null;
    }
}
