package CommonModel.GameModel.Action;

import CommonModel.GameModel.City.Region;
import CommonModel.GameModel.City.RegionName;
import CommonModel.GameModel.Council.Council;
import CommonModel.GameModel.Council.Councilor;
import CommonModel.GameModel.Council.King;
import Server.Model.Game;
import Server.Model.User;
import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;

import java.io.Serializable;

/**
 * Created by Emanuele on 16/05/2016.
 */
public class MainActionElectCouncilor extends Action {

    private final Councilor councilorToAdd;
    private final RegionName region;
    private final King king;

    /**
     * Create MainActionElectCouncilor action
     *
     * @param councilorToAdd
     * @param king           null if you want to add councilor to region's council
     * @param region         null if you want to add councilor to king's council
     */
    public MainActionElectCouncilor(Councilor councilorToAdd, King king, RegionName region) {
        this.councilorToAdd = councilorToAdd;
        this.king = king;
        this.region = region;
        actionType = Constants.MAIN_ACTION;
    }

    @Override
    public void doAction(Game game, User user) throws ActionNotPossibleException {
        Council council = null;
        if (checkActionCounter(user)) {
            if (this.king == null) {
                Region councilRegion = game.getRegion(this.region);
                council = councilRegion.getCouncil();
            } else {
                council = game.getKing().getCouncil();
            }
            // and councilor to councilor to council (and remove the first councilor)
            Councilor councilor = game.getBank().getCouncilor(this.councilorToAdd.getColor());
            council.add(councilor);
            game.getMoneyPath().goAhead(user, Constants.MONEY_EARNED_ELECT_COUNCILOR);
            this.removeAction(game, user);
        }
    }


    @Override
    public String toString() {
        String region = "";
        if (this.king == null) {
            region = "KING";
        } else {
            //TODO nullPointer
            region = this.region.getRegion();
        }
        return "[MAIN ACTION] Elect councilor for money. Councilor: " + this.councilorToAdd + " In region " + region;
    }
}
