package Server.Communication;

import Server.UserClasses.User;

/**
 * Created by Emanuele on 09/05/2016.
 */
public abstract class BaseCommunication {
    public abstract void sendMessage(String message);

    public abstract void setUser (User user);
}
