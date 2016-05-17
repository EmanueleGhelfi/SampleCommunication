package CommonModel.GameModel.Action;

import CommonModel.GameModel.ActionNotPossibleException;
import Server.Model.Game;
import Server.UserClasses.User;

/**
 * Created by Giulio on 17/05/2016.
 */
public class ChangePermitCardWithHelper extends Action {

    public ChangePermitCardWithHelper() {
        this.type = "FAST_ACTION";
    }

    @Override
    void doAction(Game game, User user) throws ActionNotPossibleException {
        if (user.getHelpers()>1){
            user.setHelpers(user.getHelpers()-1);



        } else {
            throw new ActionNotPossibleException();
        }
    }
}
