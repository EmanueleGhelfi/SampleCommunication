package Server.Model;

import Server.NetworkInterface.Communication.BaseCommunication;
import Server.NetworkInterface.Communication.FakeCommunication;

import java.util.ArrayList;

/**
 * Created by Giulio on 16/06/2016.
 */
public class FakeUser extends User {

    public FakeUser() {
        usersEmporium = new ArrayList<>();
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public BaseCommunication getBaseCommunication() {
        return new FakeCommunication();
    }

    @Override
    public int getPoliticCardSize() {
        return 0;
    }

}
