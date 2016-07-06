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
public class MainActionElectCouncilor extends Action implements Serializable {

    private Councilor councilorToAdd;
    private RegionName region;
    private King king;

    /**
     * Create MainActionElectCouncilor action
     *
     * @param councilorToAdd
     * @param king           @nullable if you want to add councilor to region's council
     * @param region         @nullable if you want to add councilor to king's council
     */
    public MainActionElectCouncilor(Councilor councilorToAdd, King king, RegionName region) {
        this.councilorToAdd = councilorToAdd;
        this.king = king;
        this.region = region;
        this.actionType = Constants.MAIN_ACTION;
    }

    @Override
    public void doAction(Game game, User user) throws ActionNotPossibleException {
        Council council = null;
        if (super.checkActionCounter(user)) {
            if (king == null) {
                Region councilRegion = game.getRegion(region);
                council = councilRegion.getCouncil();
            } else {
                council = game.getKing().getCouncil();
            }
            // and councilor to councilor to council (and remove the first councilor)
            Councilor councilor = game.getBank().getCouncilor(councilorToAdd.getColor());
            council.add(councilor);
            game.getMoneyPath().goAhead(user, Constants.MONEY_EARNED_ELECT_COUNCILOR);
            removeAction(game, user);
        }
    }


    @Override
    public String toString() {
        String region = "";
        if (king == null) {
            region = "KING";
        } else {
           if(this.region!=null) {
               region = this.region.getRegion();
           }
        }
        return "[MAIN ACTION] Elect councilor for money. Councilor: " + councilorToAdd + " In region " + region;
    }
}
