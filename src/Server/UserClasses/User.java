package Server.UserClasses;

import Server.Communication.BaseCommunication;
import Server.Main.Server;

/**
 * Created by Emanuele on 11/05/2016.
 */
public class User {

    private BaseCommunication baseCommunication;

    private String userId;

    private Server server;

    public User(BaseCommunication baseCommunication, String userId, Server server) {
        this.baseCommunication = baseCommunication;
        this.userId = userId;
        this.server = server;
    }

    public void OnMessage(String string){
        server.OnMessage(string);
    }

    public BaseCommunication getBaseCommunication() {
        return baseCommunication;
    }
}
