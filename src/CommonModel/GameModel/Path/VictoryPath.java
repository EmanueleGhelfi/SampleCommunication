package CommonModel.GameModel.Path;

import Utilities.Exception.ActionNotPossibleException;
import Server.Model.User;

/**
 * Created by Giulio on 14/05/2016.
 */
public class VictoryPath implements Path {

    private final int length = 100;

    public VictoryPath() {
    }

    @Override
    public void goAhead(User user, int value) throws ActionNotPossibleException {
        if(user.getVictoryPathPosition()+value>length){
            user.setVictoryPathPosition(length);
        }
        else{
            if(user.getVictoryPathPosition()+value<0){
                throw new ActionNotPossibleException();
            }
            else {
                user.setVictoryPathPosition(user.getVictoryPathPosition()+value);
            }
        }

    }
}
