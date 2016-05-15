package CommonModel.GameModel;

import Server.Model.Game;
import Server.UserClasses.User;

/**
 * Created by Giulio on 13/05/2016.
 */
public class RegionBonusCard implements GenericBonusCard{

    private Region region;
    private VictoryPointBonus victoryPointBonus;



    @Override
    public void getBonus(User user, Game game) throws ActionNotPossibleException {

    }
}
