package CommonModel.GameModel.Bonus;

/**
 * Created by Giulio on 13/05/2016.
 */

import CommonModel.GameModel.ActionNotPossibleException;
import CommonModel.GameModel.City.Color;
import Server.Model.Game;
import Server.UserClasses.User;

/**
 * Carte nella plancia del re che dicono bonus per ogni colore della città
 */
public class ColorBonusCard implements GenericBonusCard {

    private Color color;
    private VictoryPointBonus victoryPointBonus;


    @Override
    public void getBonus(User user, Game game) throws ActionNotPossibleException {

    }
}
