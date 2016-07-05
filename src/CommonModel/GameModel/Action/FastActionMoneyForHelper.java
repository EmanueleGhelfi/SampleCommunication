package CommonModel.GameModel.Action;

import Server.Model.Game;
import Server.Model.User;
import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;

/**
 * Created by Giulio on 17/05/2016.
 */
public class FastActionMoneyForHelper extends Action {

    public FastActionMoneyForHelper() {
        actionType = Constants.FAST_ACTION;
    }

    @Override
    public void doAction(Game game, User user) throws ActionNotPossibleException {
        if (checkActionCounter(user)) {
            if (user.getCoinPathPosition() >= Constants.MONEY_LIMITATION_MONEY_FOR_HELPER) {
                game.getMoneyPath().goAhead(user, -Constants.MONEY_LIMITATION_MONEY_FOR_HELPER);
                user.addHelper();
                this.removeAction(game, user);
            } else {
                throw new ActionNotPossibleException(Constants.MONEY_EXCEPTION);
            }
        }
    }

    @Override
    public String toString() {
        return "[FAST ACTION] buy helper with " + Constants.MONEY_LIMITATION_MONEY_FOR_HELPER + " money!";
    }
}
