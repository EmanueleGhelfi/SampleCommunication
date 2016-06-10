package CommonModel.GameModel.Bonus.Reward;

import CommonModel.GameModel.Bonus.Generic.Bonus;
import CommonModel.GameModel.Bonus.SingleBonus.VictoryPointBonus;
import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;
import CommonModel.GameModel.City.Region;
import Server.Model.Game;
import Server.Model.User;
import java.io.Serializable;
import java.util.ArrayList;

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
        victoryPointBonus = new VictoryPointBonus(Constants.REGION_BONUS);
    }

    @Override
    public void getBonus(User user, Game game) throws ActionNotPossibleException {
        victoryPointBonus.getBonus(user, game);
    }

    @Override
    public String getBonusName() {
        return "RegionBonusCard";
    }

    @Override
    public ArrayList<Bonus> getBonusArrayList() {
        return null;
    }

    @Override
    public String getBonusURL() {
        return null;
    }
}
