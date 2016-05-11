package Server.UserClasses;

import Server.Communication.BaseCommunication;

/**
 * Created by Emanuele on 11/05/2016.
 */
public class User {

    private BaseCommunication baseCommunication;

    private String userId;

    public User(BaseCommunication baseCommunication, String userId) {
        this.baseCommunication = baseCommunication;
        this.userId = userId;
    }

}
