package CommonModel.GameModel.Path;

import Server.Model.User;
import Utilities.Exception.ActionNotPossibleException;

/**
 * Created by Giulio on 14/05/2016.
 */
public interface Path {

    /** It is the generic method with which it is allowed to go forward in the path implemented by the various classes
     * @param user is the user that go ahead
     * @param value is the value of the go ahead
     * @exception ActionNotPossibleException is the exception raised
     */
    void goAhead(User user, int value) throws ActionNotPossibleException;

}
