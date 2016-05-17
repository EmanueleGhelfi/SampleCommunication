package CommonModel.GameModel.Path;

import Utilities.Exception.ActionNotPossibleException;
import Server.Model.User;

/**
 * Created by Giulio on 14/05/2016.
 */
public class MoneyPath implements Path {

    private final int length = 21;

    @Override
    public void goAhead(User user, int value) throws ActionNotPossibleException {
        if(user.getCoinPathPosition()+value>length){
            user.setCoinPathPosition(length);
        }
        else{
            if(user.getCoinPathPosition()+value<0){
                throw new ActionNotPossibleException();
            }
            else {
                user.setCoinPathPosition(user.getCoinPathPosition()+value);
            }
        }

    }
}
