package CommonModel.GameModel.Bonus.SingleBonus;

import CommonModel.GameModel.Bonus.Generic.Bonus;
import Server.Model.Game;
import Server.Model.User;
import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Giulio on 13/05/2016.
 */
public class OldPermitCardBonus implements Bonus, Serializable {

    @Override
    public void getBonus(User user, Game game) throws ActionNotPossibleException {

        if (user.getOldPermitCards().size() > 0 || user.getPermitCards().size() > 0) {
            user.addOptionalActionCounter();
            user.getBaseCommunication().selectOldPermitCard();
        }

    }

    @Override
    public String getBonusName() {
        return "OldPermitCardBonus";
    }

    @Override
    public ArrayList<Bonus> getBonusArrayList() {
        return null;
    }

    @Override
    public ArrayList<String> getBonusURL() {
        ArrayList<String> toReturn = new ArrayList<String>();
        toReturn.add(Constants.IMAGE_PATH + "OldPermitCardBonus.png");
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
        return "Old Permit Card Bonus";
    }
}
