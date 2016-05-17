package CommonModel.GameModel.Action;

import CommonModel.GameModel.ActionNotPossibleException;
import CommonModel.GameModel.City.Region;
import CommonModel.GameModel.Council;
import CommonModel.GameModel.Councilor;
import CommonModel.GameModel.King;
import Server.Model.Game;
import Server.UserClasses.User;

/**
 * Created by Giulio on 17/05/2016.
 */
public class CorruptWithHelper extends Action {

    private Region region;
    private King king;
    private Councilor councilor;

    public CorruptWithHelper(Region region, King king, Councilor councilor) {
        this.type = "FAST_ACTION";
        this.region = region;
        this.king = king;
        this.councilor = councilor;
    }

    @Override
    void doAction(Game game, User user) throws ActionNotPossibleException {
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
