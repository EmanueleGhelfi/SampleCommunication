package CommonModel.GameModel.Bonus;

import CommonModel.GameModel.ActionNotPossibleException;
import CommonModel.GameModel.Bonus.Bonus;
import CommonModel.GameModel.Bonus.VictoryPointBonus;
import Server.Model.Game;
import Server.UserClasses.User;

/**
 * Created by Giulio on 13/05/2016.
 */
public class KingBonusCard implements Bonus {

    private VictoryPointBonus victoryPointBonus;
    private int order; //from 1 to 5


    @Override
    public void getBonus(User user, Game game) throws ActionNotPossibleException {

    }
}
