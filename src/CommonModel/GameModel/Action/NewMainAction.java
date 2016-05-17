package CommonModel.GameModel.Action;

import CommonModel.GameModel.ActionNotPossibleException;
import Server.Model.Game;
import Server.UserClasses.User;

/**
 * Created by Giulio on 17/05/2016.
 */
public class NewMainAction extends Action {

    public NewMainAction() {
        this.type = "FAST_ACTION";
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
