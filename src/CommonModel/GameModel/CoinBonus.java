package CommonModel.GameModel;

import Server.Model.Game;
import Server.UserClasses.User;

import java.util.Random;

/**
 * Created by Giulio on 13/05/2016.
 */
public class CoinBonus implements Bonus {

    private int coinNumber;

    public CoinBonus() {
        Random random = new Random();
        coinNumber = random.nextInt(6)+1;
    }

    @Override
    public void getBonus(User user, Game game) throws ActionNotPossibleException {

        game.getMoneyPath().goAhead(user,coinNumber);

    }
}
