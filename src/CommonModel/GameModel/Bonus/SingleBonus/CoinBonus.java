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
public class CoinBonus implements Bonus, Serializable {

    private final int coinNumber;

    public CoinBonus() {
        Random random = new Random();
        this.coinNumber = random.nextInt(Constants.RANDOM_COIN_FIRST_PARAMETER) + Constants.RANDOM_COIN_SECOND_PARAMETER;
    }

    @Override
    public void getBonus(User user, Game game) throws ActionNotPossibleException {
        game.getMoneyPath().goAhead(user, this.coinNumber);
    }

    @Override
    public String getBonusName() {
        return "CoinBonus";
    }

    @Override
    public ArrayList<Bonus> getBonusArrayList() {
        return null;
    }

    @Override
    public ArrayList<String> getBonusURL() {

        ArrayList<String> toReturn = new ArrayList<String>();
        toReturn.add(Constants.IMAGE_PATH + "Money.png");
        return toReturn;
    }

    @Override
    public ArrayList<String> getBonusInfo() {
        ArrayList<String> toReturn = new ArrayList<String>();
        toReturn.add(this.coinNumber + "");
        return toReturn;
    }

    @Override
    public String toString() {
        return "Coin Bonus: +" + this.coinNumber;
    }
}
