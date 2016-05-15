package CommonModel.GameModel;

/**
 * Created by Giulio on 13/05/2016.
 */

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
