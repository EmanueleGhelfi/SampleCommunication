package CommonModel.GameModel.Bonus;

import CommonModel.GameModel.ActionNotPossibleException;
import Server.Model.Game;
import Server.UserClasses.User;

import java.util.Random;

/**
 * Created by Giulio on 13/05/2016.
 */
public class HelperBonus implements Bonus{

    private int helperNumber;

    public HelperBonus() {
        Random randomGenerator = new Random();
        helperNumber = randomGenerator.nextInt(5);
    }

    @Override
    public void getBonus(User user, Game game) throws ActionNotPossibleException {
        user.setHelpers(user.getHelpers()+helperNumber);
    }
}
