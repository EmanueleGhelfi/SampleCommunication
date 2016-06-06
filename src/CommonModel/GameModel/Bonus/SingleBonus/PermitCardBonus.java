package CommonModel.GameModel.Bonus.SingleBonus;

import CommonModel.GameModel.Bonus.Generic.Bonus;
import Utilities.Exception.ActionNotPossibleException;
import Server.Model.Game;
import Server.Model.User;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Draw a permit card from the table
 * Created by Giulio on 13/05/2016.
 */
public class PermitCardBonus implements Bonus, Serializable {


    public PermitCardBonus() {
    }

    //TODO teso
    @Override
    public void getBonus(User user, Game game) throws ActionNotPossibleException {
    }

    @Override
    public String getBonusName() {
        return "PermitCardBonus";
    }

    @Override
    public ArrayList<Bonus> getBonusArrayList() {
        return null;
    }
}
