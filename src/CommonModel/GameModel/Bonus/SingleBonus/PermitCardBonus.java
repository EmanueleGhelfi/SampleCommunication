package CommonModel.GameModel.Bonus.SingleBonus;

import CommonModel.GameModel.Bonus.Generic.Bonus;
import Server.Model.Game;
import Server.Model.User;
import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Draw a permit card from the table
 * Created by Giulio on 13/05/2016.
 */
public class PermitCardBonus implements Bonus, Serializable {


    public PermitCardBonus() {
    }

    @Override
    public void getBonus(User user, Game game) throws ActionNotPossibleException {
        user.addOptionalActionCounter();
        user.getBaseCommunication().selectPermitCard();
    }

    @Override
    public String getBonusName() {
        return "PermitCardBonus";
    }

    @Override
    public ArrayList<Bonus> getBonusArrayList() {
        return null;
    }

    @Override
    public ArrayList<String> getBonusURL() {
        ArrayList<String> toReturn = new ArrayList<String>();
        toReturn.add(Constants.IMAGE_PATH + "NewPermitCardBonus.png");
        return toReturn;

    }

    @Override
    public ArrayList<String> getBonusInfo() {
        ArrayList<String> toReturn = new ArrayList<String>();
        toReturn.add("");
        return toReturn;
    }

    @Override
    public String toString() {
        return "Draw Permit Card from Table";
    }
}
