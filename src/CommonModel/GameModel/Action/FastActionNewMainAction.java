package CommonModel.GameModel.Action;

import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;
import Server.Model.Game;
import Server.Model.User;

/**
 * Created by Giulio on 17/05/2016.
 */
public class FastActionNewMainAction extends Action {

    public FastActionNewMainAction() {
        this.type = Constants.FAST_ACTION;
    }

    @Override
    public void doAction(Game game, User user) throws ActionNotPossibleException {
        if(super.checkActionCounter(user)) {
            if (user.getHelpers() >= Constants.HELPER_LIMITATION_NEW_MAIN_ACTION) {
                user.setHelpers(user.getHelpers() - Constants.HELPER_LIMITATION_NEW_MAIN_ACTION);
                user.setMainActionCounter(user.getMainActionCounter() + Constants.MAIN_ACTION_ADDED);
                removeAction(game, user);
            } else {
                throw new ActionNotPossibleException();
            }
        }
    }

    @Override
    public String getType() {
        return type;
    }
}
