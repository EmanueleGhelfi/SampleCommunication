package CommonModel.GameModel.Action;

import CommonModel.GameModel.City.RegionName;
import Server.Model.Game;
import Server.Model.User;
import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;

/**
 * Created by Giulio on 17/05/2016.
 */
public class FastActionChangePermitCardWithHelper extends Action {

    private final RegionName region;

    public FastActionChangePermitCardWithHelper(RegionName region) {
        actionType = Constants.FAST_ACTION;
        this.region = region;
    }

    /**
     * change the visible permit card spending a helper
     *
     * @param game
     * @param user
     * @throws ActionNotPossibleException
     */
    @Override
    public void doAction(Game game, User user) throws ActionNotPossibleException {
        // check
        if (checkActionCounter(user) && this.region != null) {
            if (user.getHelpers().size() >= Constants.HELPER_LIMITATION_CHANGE_PERMIT_CARD) {
                user.setHelpers(user.getHelpers().size() - Constants.HELPER_LIMITATION_CHANGE_PERMIT_CARD);
                game.getPermitDeck(this.region).changePermitCardVisibile();
                this.removeAction(game, user);
            } else {
                throw new ActionNotPossibleException(Constants.HELPER_EXCEPTION);
            }
        } else {
            throw new ActionNotPossibleException("Region is null");
        }
    }

    @Override
    public String toString() {
        return "[FAST ACTION] Change permit card of region " + this.region + " with helper";
    }
}
