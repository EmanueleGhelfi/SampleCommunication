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
public class VictoryPointBonus implements Bonus,Serializable {

    private int victoryPoint;

    /** Random victory point generator
     */
    public VictoryPointBonus() {
        Random randomGenerator = new Random();
        this.victoryPoint = randomGenerator.nextInt(Constants.RANDOM_VICTORY_FIRST_PARAMETER)+Constants.RANDOM_VICTORY_SECOND_PARAMETER;
    }

    /** Fixed victory point generator
     * @param victoryPoint
     */
    public VictoryPointBonus(int victoryPoint){
        this.victoryPoint = victoryPoint;
    }

    @Override
    public void getBonus(User user, Game game) throws ActionNotPossibleException {
        game.getVictoryPath().goAhead(user, victoryPoint);
    }
}
