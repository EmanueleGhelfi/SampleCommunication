package CommonModel.GameModel.Action;

import CommonModel.GameModel.City.Region;
import Utilities.Exception.ActionNotPossibleException;
import Server.Model.Game;
import Server.Model.User;

/**
 * Created by Giulio on 17/05/2016.
 */
public class FastActionChangePermitCardWithHelper extends Action {

    private Region region;

    public FastActionChangePermitCardWithHelper(Region region) {
        this.type = "FAST_ACTION";
        this.region = region;
    }

    /** change the visible permit card spending a helper
     * @param game
     * @param user
     * @throws ActionNotPossibleException
     */
    @Override
    public void doAction(Game game, User user) throws ActionNotPossibleException {
        if (user.getHelpers()>1){
            user.setHelpers(user.getHelpers()-1);
            game.getPermitDeck(region).changePermitCardVisibile();
            removeAction(game, user);
        } else {
            throw new ActionNotPossibleException();
        }
    }
}
