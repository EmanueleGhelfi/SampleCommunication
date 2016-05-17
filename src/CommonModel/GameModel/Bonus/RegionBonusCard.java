package CommonModel.GameModel.Bonus;

import Utilities.Exception.ActionNotPossibleException;
import CommonModel.GameModel.City.Region;
import Server.Model.Game;
import Server.Model.User;

/**
 * Created by Giulio on 13/05/2016.
 */
public class RegionBonusCard implements GenericBonusCard {

    private Region region;
    private VictoryPointBonus victoryPointBonus;

    public RegionBonusCard(Region region) {
        this.region = region;
        victoryPointBonus = new VictoryPointBonus(5);
    }

    @Override
    public void getBonus(User user, Game game) throws ActionNotPossibleException {
        victoryPointBonus.getBonus(user, game);
    }

}
