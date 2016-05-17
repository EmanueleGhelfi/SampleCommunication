package CommonModel.GameModel.Action;

import Utilities.Exception.ActionNotPossibleException;
import Server.Model.Game;
import Server.Model.User;

/**
 * Created by Giulio on 17/05/2016.
 */
public class FastActionChangePermitCardWithHelper extends Action {

    public FastActionChangePermitCardWithHelper() {
        this.type = "FAST_ACTION";
    }

    @Override
    public void doAction(Game game, User user) throws ActionNotPossibleException {
        if (user.getHelpers()>1){
            user.setHelpers(user.getHelpers()-1);



        } else {
            throw new ActionNotPossibleException();
        }
    }
}
