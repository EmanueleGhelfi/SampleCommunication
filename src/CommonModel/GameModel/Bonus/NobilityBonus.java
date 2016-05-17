package CommonModel.GameModel.Bonus;

import Utilities.Exception.ActionNotPossibleException;
import Server.Model.Game;
import Server.Model.User;

import java.util.Random;

/**
 * Created by Giulio on 13/05/2016.
 */
public class NobilityBonus implements Bonus {

    int nobilityNumber;

    public NobilityBonus() {
        Random randomGenerator = new Random();
        nobilityNumber = randomGenerator.nextInt(5);
    }

    @Override
    public void getBonus(User user, Game game) throws ActionNotPossibleException {
        game.getNobilityPath().goAhead(user, nobilityNumber);
    }
}
