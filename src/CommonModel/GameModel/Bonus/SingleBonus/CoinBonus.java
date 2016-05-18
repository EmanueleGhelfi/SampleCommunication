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
public class CoinBonus implements Bonus,Serializable {

    private int coinNumber;

    public CoinBonus() {
        Random random = new Random();
        coinNumber = random.nextInt(Constants.RANDOM_COIN_FIRST_PARAMETER)+Constants.RANDOM_COIN_SECOND_PARAMETER;
    }

    @Override
    public void getBonus(User user, Game game) throws ActionNotPossibleException {
        game.getMoneyPath().goAhead(user,coinNumber);
    }
}
