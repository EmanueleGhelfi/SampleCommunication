package Server.Model;

import Server.NetworkInterface.Communication.BaseCommunication;
import Server.NetworkInterface.Communication.FakeCommunication;

import java.util.ArrayList;

/** This class is used for the addition of the rule within the game if there are only two players.
 * In this case it is built in case other emporiums on the map .
 * Created by Giulio on 16/06/2016.
 */
public class FakeUser extends User {

    /** Constructor
     */
    public FakeUser() {
        this.usersEmporium = new ArrayList<>();
    }

    /** Getter
     * @return false
     */
    @Override
    public boolean isConnected() {
        return false;
    }

    /** Getter
     * @return the fake communication
     */
    @Override
    public BaseCommunication getBaseCommunication() {
        return new FakeCommunication();
    }

    /** Getter
     * @return 0
     */
    @Override
    public int getPoliticCardSize() {
        return 0;
    }

}
