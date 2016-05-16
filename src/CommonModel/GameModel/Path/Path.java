package CommonModel.GameModel.Path;

import CommonModel.GameModel.ActionNotPossibleException;
import Server.UserClasses.User;

/**
 * Created by Giulio on 14/05/2016.
 */
public interface Path {

    /**
     *
     * @param user
     * @param value
     */
    void goAhead(User user, int value) throws ActionNotPossibleException;

}
