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
 * Created by Giulio on 17/05/2016.
 * <p>
 * Elect a councilor spending one helper (fast move)
 */
public class FastActionElectCouncilorWithHelper extends Action {

    private RegionName region;
    private King king;
    private Councilor councilor;
    private String councilType;

    public FastActionElectCouncilorWithHelper(RegionName region, King king, Councilor councilor, String councilType) {
        actionType = Constants.FAST_ACTION;
        this.region = region;
        this.king = king;
        this.councilor = councilor;
        this.councilType = councilType;
    }

    public FastActionElectCouncilorWithHelper() {
    }

    @Override
    public void doAction(Game game, User user) throws ActionNotPossibleException {
        Council council = null;
        if (checkActionCounter(user)) {
            if (user.getHelpers().size() >= Constants.HELPER_LIMITATION_ELECT_COUNCILOR && this.councilor != null) {
                user.setHelpers(user.getHelpers().size() - Constants.HELPER_LIMITATION_ELECT_COUNCILOR);
                if (this.councilType.equals(Constants.REGION_COUNCIL)) {
                    Region councilRegion = game.getRegion(this.region);
                    council = councilRegion.getCouncil();
                } else {
                    if (this.councilType.equals(Constants.KING_COUNCIL)) {
                        council = game.getKing().getCouncil();
                    } else throw new ActionNotPossibleException(Constants.COUNCIL_NOT_PRESENT_EXCEPTION);
                }
                Councilor councilorToAdd = game.getBank().getCouncilor(this.councilor.getColor());
                council.add(councilorToAdd);
                this.removeAction(game, user);
            } else {
                if (this.councilor != null)
                    throw new ActionNotPossibleException(Constants.HELPER_EXCEPTION);
                else throw new ActionNotPossibleException("Councilor is null!");
            }
        }
    }


    @Override
    public String toString() {
        String toReturn = "[FAST ACTION] ";
        toReturn += "Elect a counilor with helper! \n" +
                "Councilor: " + this.councilor;
        if (this.councilType.equals(Constants.REGION_COUNCIL)) {
            toReturn += "\n Council of " + this.region;
        } else {
            toReturn += "Council of King";
        }

        return toReturn;
    }

}
