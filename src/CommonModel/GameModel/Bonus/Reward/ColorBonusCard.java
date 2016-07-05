package CommonModel.GameModel.Bonus.Reward;

import CommonModel.GameModel.Bonus.Generic.Bonus;
import CommonModel.GameModel.Bonus.SingleBonus.VictoryPointBonus;
import CommonModel.GameModel.City.Color;
import Server.Model.Game;
import Server.Model.User;
import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Giulio on 13/05/2016.
 */

/**
 * Carte nella plancia del re che dicono bonus per ogni colore della città
 */
public class ColorBonusCard implements RewardBonusCard, Serializable {

    private Color color;
    private VictoryPointBonus victoryPointBonus;

    public ColorBonusCard() {
    }

    public ColorBonusCard(Color color) {
        this.color = color;
        switch (color.getColor()) {
            case Constants.BLUE:
                this.victoryPointBonus = new VictoryPointBonus(Constants.BLUE_BONUS);
                break;
            case Constants.GREY:
                this.victoryPointBonus = new VictoryPointBonus(Constants.GREY_BONUS);
                break;
            case Constants.ORANGE:
                this.victoryPointBonus = new VictoryPointBonus(Constants.ORANGE_BONUS);
                break;
            case Constants.YELLOW:
                this.victoryPointBonus = new VictoryPointBonus(Constants.YELLOW_BONUS);
                break;
        }
    }

    @Override
    public void getBonus(User user, Game game) throws ActionNotPossibleException {
        if (this.victoryPointBonus != null) {
            this.victoryPointBonus.getBonus(user, game);
        }
    }

    @Override
    public String getBonusName() {
        return "ColorBonusCard";
    }

    @Override
    public ArrayList<Bonus> getBonusArrayList() {
        return null;
    }

    @Override
    public ArrayList<String> getBonusURL() {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<String> getBonusInfo() {
        ArrayList<String> toReturn = new ArrayList<>();
        toReturn.addAll(this.victoryPointBonus.getBonusInfo());
        return toReturn;
    }

}
