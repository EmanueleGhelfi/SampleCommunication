package CommonModel.GameModel.Action;

import Utilities.Exception.ActionNotPossibleException;
import CommonModel.GameModel.City.Region;
import CommonModel.GameModel.Council.Council;
import CommonModel.GameModel.Council.Councilor;
import CommonModel.GameModel.Council.King;
import Server.Model.Game;
import Server.Model.User;

/**
 * Created by Giulio on 17/05/2016.
 */
public class FastActionCorruptWithHelper extends Action {

    private Region region;
    private King king;
    private Councilor councilor;

    public FastActionCorruptWithHelper(Region region, King king, Councilor councilor) {
        this.type = "FAST_ACTION";
        this.region = region;
        this.king = king;
        this.councilor = councilor;
    }

    @Override
    public void doAction(Game game, User user) throws ActionNotPossibleException {
        Council council;
        if (user.getHelpers()>1){
            user.setHelpers(user.getHelpers()-1);
            if (king == null) {
                Region councilRegion = game.getRegion(region.getRegion());
                council = councilRegion.getCouncil();
            } else {
                council = king.getCouncil();
            }
            council.add(councilor);
            removeAction(game,user);
        } else {
            throw new ActionNotPossibleException();
        }
    }
}
