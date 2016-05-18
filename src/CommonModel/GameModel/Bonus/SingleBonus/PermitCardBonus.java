package CommonModel.GameModel.Bonus.SingleBonus;

import CommonModel.GameModel.Bonus.Generic.Bonus;
import CommonModel.GameModel.City.Region;
import Utilities.Exception.ActionNotPossibleException;
import Server.Model.Game;
import Server.Model.User;

import java.io.Serializable;

/**
 * Draw a permit card from the table
 * Created by Giulio on 13/05/2016.
 */
public class PermitCardBonus implements Bonus, Serializable {

    private Region region;

    public PermitCardBonus(Region region) {
        this.region = region;
    }

    //TODO
    @Override
    public void getBonus(User user, Game game) throws ActionNotPossibleException {

    }
}
