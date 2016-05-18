package CommonModel.GameModel.Bonus.SingleBonus;

import CommonModel.GameModel.Bonus.Generic.Bonus;
import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;
import Server.Model.Game;
import Server.Model.User;
import java.io.Serializable;
import java.util.Random;

/**
 * Created by Giulio on 13/05/2016.
 */
public class HelperBonus implements Bonus,Serializable {

    private int helperNumber;

    public HelperBonus() {
        Random randomGenerator = new Random();
        helperNumber = randomGenerator.nextInt(Constants.RANDOM_HELPER_FIRST_PARAMETER)+ Constants.RANDOM_HELPER_SECOND_PARAMETER;
    }

    //TODO second parameter?

    @Override
    public void getBonus(User user, Game game) throws ActionNotPossibleException {
        user.setHelpers(user.getHelpers()+helperNumber);
    }
}
