package CommonModel.GameModel.Bonus.Reward;

import CommonModel.GameModel.Bonus.SingleBonus.VictoryPointBonus;
import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;
import CommonModel.GameModel.City.Region;
import Server.Model.Game;
import Server.Model.User;

import java.io.Serializable;

/**
 * Created by Giulio on 13/05/2016.
 */
public class RegionBonusCard implements RewardBonusCard, Serializable {

    private Region region;
    private VictoryPointBonus victoryPointBonus;

    public RegionBonusCard() {
    }

    public RegionBonusCard(Region region) {
        this.region = region;
        victoryPointBonus = new VictoryPointBonus(Constants.FIVE);
    }

    @Override
    public void getBonus(User user, Game game) throws ActionNotPossibleException {
        victoryPointBonus.getBonus(user, game);
    }

}
