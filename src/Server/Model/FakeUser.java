package Server.Model;

import Server.NetworkInterface.Communication.BaseCommunication;
import Server.NetworkInterface.Communication.FakeCommunication;

/**
 * Created by Giulio on 16/06/2016.
 */
public class FakeUser extends User {

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public BaseCommunication getBaseCommunication() {
        return new FakeCommunication();
    }
}
