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

    public ElectCouncillor(Councilor councilorToAdd, King king, Region region) {
        this.councilorToAdd = councilorToAdd;
        this.king = king;
        this.region = region;
    }

    @Override
    public void doAction(Game game, User user) {
         Region councilregion = game.getRegion(region.getRegion());
        Council council = councilregion.getCouncil();
        council.add(councilorToAdd);
    }

    @Override
    public String getType() {
        return type;
    }
}
