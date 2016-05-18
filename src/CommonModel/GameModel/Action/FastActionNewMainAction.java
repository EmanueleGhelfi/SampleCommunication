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
        if (user.getHelpers()>3) {
            user.setHelpers(user.getHelpers() - 3);
            user.setMainActionCounter(user.getMainActionCounter() + 1);
            removeAction(game,user);
        } else {
            throw new ActionNotPossibleException();
        }
    }

    @Override
    public String getType() {
        return type;
    }
}
