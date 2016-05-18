package CommonModel.GameModel.Path;

import Utilities.Exception.ActionNotPossibleException;
import Server.Model.User;

/**
 * Created by Giulio on 14/05/2016.
 */
public interface Path {

    /**
     * @param user
     * @param value
     */
    void goAhead(User user, int value) throws ActionNotPossibleException;

}
