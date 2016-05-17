package CommonModel.GameModel.Action;

import Utilities.Exception.ActionNotPossibleException;
import CommonModel.GameModel.City.Region;
import CommonModel.GameModel.Council.Council;
import CommonModel.GameModel.Council.Councilor;
import CommonModel.GameModel.Council.King;
import Server.Model.Game;
import Server.Model.User;
import java.io.Serializable;

/**
 * Created by Emanuele on 16/05/2016.
 */
public class MainActionElectCouncilor extends Action implements Serializable {

    private Councilor councilorToAdd;
    private Region region;
    private King king;
    private final String type = "MAIN_ACTION"; // todo: create constants

    /**
     * Create MainActionElectCouncilor action
     * @param councilorToAdd
     * @param king null if you want to add councilor to region's council
     * @param region null if you want to add councilor to king's council
     */
    public MainActionElectCouncilor(Councilor councilorToAdd, King king, Region region) {
        this.councilorToAdd = councilorToAdd;
        this.king = king;
        this.region = region;
    }

    @Override
    public void doAction(Game game, User user) throws ActionNotPossibleException {
        Council council = null;
        if(king==null) {
            Region councilRegion = game.getRegion(region.getRegion());
            council = councilRegion.getCouncil();
        }
        else{
            council = game.getKing().getCouncil();
        }
        // and councilor to councilor to council (and remove the first councilor)
        council.add(councilorToAdd);
        game.getMoneyPath().goAhead(user,4);
        removeAction(game,user);
    }

    @Override
    public String getType() {
        return type;
    }
}
