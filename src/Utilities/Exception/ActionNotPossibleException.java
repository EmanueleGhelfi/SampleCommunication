package Utilities.Exception;

import java.io.Serializable;

/**
 * Created by Emanuele on 15/05/2016.
 */
public class ActionNotPossibleException extends Exception implements Serializable {

    public ActionNotPossibleException() {
    }



    public ActionNotPossibleException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
