package CommonModel.GameModel.Bonus.SingleBonus;

import CommonModel.GameModel.Bonus.Generic.Bonus;
import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;
import Server.Model.Game;
import Server.Model.User;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Get two city bonus (also the ones used) but not the ones with nobility bonus
 * Created by Giulio on 13/05/2016.
 */
public class OldPermitCardBonus implements Bonus,Serializable {

    //TODO teso
    @Override
    public void getBonus(User user, Game game) throws ActionNotPossibleException {

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
    public String getBonusURL() {
        return Constants.IMAGE_PATH+"PermitCard.png";
    }
}
