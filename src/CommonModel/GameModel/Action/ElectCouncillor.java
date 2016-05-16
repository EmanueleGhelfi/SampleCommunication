package CommonModel.GameModel.Action;

import CommonModel.GameModel.City.Region;
import CommonModel.GameModel.Council;
import CommonModel.GameModel.Councilor;
import CommonModel.GameModel.King;
import Server.Model.Game;
import Server.UserClasses.User;

/**
 * Created by Emanuele on 16/05/2016.
 */
public class ElectCouncillor implements Action {

    private Councilor councilorToAdd;
    private Region region;
    private King king;
    private final String type = "MAIN_ACTION"; // todo: create constants

    /**
     * Create ElectCouncillor action
     * @param councilorToAdd
     * @param king null if you want to add councillor to region's council
     * @param region null if you want to add councillor to king's council
     */
    public ElectCouncillor(Councilor councilorToAdd, King king, Region region) {
        this.councilorToAdd = councilorToAdd;
        this.king = king;
        this.region = region;
    }

    @Override
    public void doAction(Game game, User user) {
        Council council = null;
        if(king==null) {
            Region councilRegion = game.getRegion(region.getRegion());
            council = councilRegion.getCouncil();
        }
        else{
            council = game.getKing().getCouncil();
        }
        // and councilor to councilor to council (and remove the first councillor)
        council.add(councilorToAdd);
        user.setCoinPathPosition(user.getCoinPathPosition()+4);
    }

    @Override
    public String getType() {
        return type;
    }
}
