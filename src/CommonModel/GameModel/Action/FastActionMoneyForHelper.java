package CommonModel.GameModel.Action;

import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;
import Server.Model.Game;
import Server.Model.User;

/**
 * Created by Giulio on 17/05/2016.
 */
public class FastActionMoneyForHelper extends Action {

    public FastActionMoneyForHelper() {
        this.actionType = Constants.FAST_ACTION;
    }

    @Override
    public void doAction(Game game, User user) throws ActionNotPossibleException {
        if(super.checkActionCounter(user)) {
            if (user.getCoinPathPosition() >= Constants.MONEY_LIMITATION_MONEY_FOR_HELPER) {
                game.getMoneyPath().goAhead(user, -Constants.MONEY_LIMITATION_MONEY_FOR_HELPER);
                user.addHelper();
                removeAction(game, user);
            } else {
                throw new ActionNotPossibleException(Constants.MONEY_EXCEPTION);
            }
        }
    }

    @Override
    public String toString() {
        return "[FAST ACTION] buy helper with "+Constants.MONEY_LIMITATION_MONEY_FOR_HELPER+" money!";
    }
}
