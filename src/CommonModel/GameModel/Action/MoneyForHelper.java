package CommonModel.GameModel.Action;

import CommonModel.GameModel.ActionNotPossibleException;
import Server.Model.Game;
import Server.UserClasses.User;

/**
 * Created by Giulio on 17/05/2016.
 */
public class MoneyForHelper extends Action {

    public MoneyForHelper() {
        this.type = "FAST_ACTION";
    }

    @Override
    void doAction(Game game, User user) throws ActionNotPossibleException {
        if (user.getVictoryPathPosition()>3){
            user.setVictoryPathPosition(user.getVictoryPathPosition()-3);
            user.setHelpers(user.getHelpers()+1);
            removeAction(game,user);
        } else {
            throw new ActionNotPossibleException();
        }
    }

}
