package CommonModel.GameModel.Bonus;

import CommonModel.GameModel.ActionNotPossibleException;
import Server.Model.Game;
import Server.UserClasses.User;

import java.util.Random;

/**
 * Created by Giulio on 13/05/2016.
 */
public class VictoryPointBonus implements Bonus {

    int victoryPoint;

    /** Random victory point generator
     */
    public VictoryPointBonus() {
        Random randomGenerator = new Random();
        this.victoryPoint = randomGenerator.nextInt(4)+1;
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
