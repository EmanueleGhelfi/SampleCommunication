package CommonModel.GameModel.Path;

import Server.Model.User;
import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;

import java.io.Serializable;

/**
 * Created by Giulio on 14/05/2016.
 */
public class MoneyPath implements Path, Serializable {

    private final int length = Constants.MONEY_PATH_LENGTH;

    public MoneyPath() {
    }

    @Override
    public void goAhead(User user, int value) throws ActionNotPossibleException {
        if (user.getCoinPathPosition() + value > length) {
            user.setCoinPathPosition(length);
        } else {
            if (user.getCoinPathPosition() + value < 0) {
                throw new ActionNotPossibleException(Constants.MONEY_PATH_EXCEPTION);
            } else {
                user.setCoinPathPosition(user.getCoinPathPosition() + value);
            }
        }
    }
}
