package CommonModel.GameModel.Bonus;

/**
 * Created by Giulio on 13/05/2016.
 */

import Utilities.Exception.ActionNotPossibleException;
import CommonModel.GameModel.City.Color;
import Server.Model.Game;
import Server.Model.User;

/**
 * Carte nella plancia del re che dicono bonus per ogni colore della città
 */
public class ColorBonusCard implements GenericBonusCard {

    private Color color;
    private VictoryPointBonus victoryPointBonus;

    public ColorBonusCard(Color color) {
        this.color = color;
        switch(color.getColor()){
            case "blue":
                victoryPointBonus = new VictoryPointBonus(5);
                break;
            case "grey":
                victoryPointBonus = new VictoryPointBonus(12);
                break;
            case "orange":
                victoryPointBonus = new VictoryPointBonus(8);
                break;
            case "yellow":
                victoryPointBonus = new VictoryPointBonus(20);
                break;
        }
    }

    @Override
    public void getBonus(User user, Game game) throws ActionNotPossibleException {
        victoryPointBonus.getBonus(user, game);
    }
}
