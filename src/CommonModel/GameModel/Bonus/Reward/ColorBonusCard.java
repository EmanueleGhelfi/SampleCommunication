package CommonModel.GameModel.Bonus.Reward;

/**
 * Created by Giulio on 13/05/2016.
 */

import CommonModel.GameModel.Bonus.SingleBonus.VictoryPointBonus;
import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;
import CommonModel.GameModel.City.Color;
import Server.Model.Game;
import Server.Model.User;

import java.io.Serializable;

/**
 * Carte nella plancia del re che dicono bonus per ogni colore della città
 */
public class ColorBonusCard implements RewardBonusCard,Serializable {

    private Color color;
    private VictoryPointBonus victoryPointBonus;

    public ColorBonusCard() {
    }

    public ColorBonusCard(Color color) {
        this.color = color;
        switch(color.getColor()){
            case Constants.BLUE:
                victoryPointBonus = new VictoryPointBonus(5);
                break;
            case Constants.GREY:
                victoryPointBonus = new VictoryPointBonus(12);
                break;
            case Constants.ORANGE:
                victoryPointBonus = new VictoryPointBonus(8);
                break;
            case Constants.YELLOW:
                victoryPointBonus = new VictoryPointBonus(20);
                break;
        }
    }

    @Override
    public void getBonus(User user, Game game) throws ActionNotPossibleException {
        victoryPointBonus.getBonus(user, game);
    }

}
