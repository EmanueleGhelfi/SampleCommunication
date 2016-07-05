package CommonModel.GameModel.Action;

import Server.Model.Game;
import Server.Model.User;
import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;

/**
 * Created by Giulio on 17/05/2016.
 */
public class FastActionNewMainAction extends Action {

    public FastActionNewMainAction() {
        actionType = Constants.FAST_ACTION;
    }

    @Override
    public void doAction(Game game, User user) throws ActionNotPossibleException {
        if (checkActionCounter(user)) {
            if (user.getHelpers().size() >= Constants.HELPER_LIMITATION_NEW_MAIN_ACTION) {
                user.setHelpers(user.getHelpers().size() - Constants.HELPER_LIMITATION_NEW_MAIN_ACTION);
                user.setMainActionCounter(user.getMainActionCounter() + Constants.MAIN_ACTION_ADDED);
                this.removeAction(game, user);
            } else {
                throw new ActionNotPossibleException(Constants.HELPER_EXCEPTION);
            }
        }
    }

    @Override
    public String toString() {
        return "[FAST ACTION] Buy main action for: " + Constants.HELPER_LIMITATION_NEW_MAIN_ACTION;
    }
}
