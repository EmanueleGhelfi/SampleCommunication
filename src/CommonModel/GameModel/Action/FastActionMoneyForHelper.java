package CommonModel.GameModel.Action;

import Utilities.Exception.ActionNotPossibleException;
import Server.Model.Game;
import Server.Model.User;

/**
 * Created by Giulio on 17/05/2016.
 */
public class FastActionMoneyForHelper extends Action {

    public FastActionMoneyForHelper() {
        this.type = "FAST_ACTION";
    }

    @Override
    public void doAction(Game game, User user) throws ActionNotPossibleException {
        if (user.getVictoryPathPosition()>3){
            game.getVictoryPath().goAhead(user, -3);
            user.setHelpers(user.getHelpers()+1);
            removeAction(game,user);
        } else {
            throw new ActionNotPossibleException();
        }
    }

}
