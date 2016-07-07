package Server.NetworkInterface.Communication;

import CommonModel.GameModel.City.City;
import CommonModel.Snapshot.BaseUser;
import CommonModel.Snapshot.SnapshotToSend;
import Server.Model.Map;
import Server.Model.User;

import java.util.ArrayList;

/** This class is designed to abstract the communication.
 * In this way we can have methods in common between client connected in RMI and Socket connected with the implementation of this class.
 * Created by Emanuele on 09/05/2016.
 */
public abstract class BaseCommunication {

    /** Set the user into the game
     * @param user is the user that must be set
     */
    public abstract void setUser(User user);

    /** Send snapshot to client
     * @param snapshotToSend is the snapshot to send
     */
    public abstract void sendSnapshot(SnapshotToSend snapshotToSend);

    /** Send that is changing round to client
     */
    public abstract void changeRound();

    /** Send available map to client
     * @param availableMaps are the maps available
     */
    public abstract void sendAvailableMap(ArrayList<Map> availableMaps);

    /** Send the selected map to client
     * @param snapshotToSend is the first snapshot to send
     */
    public abstract void sendSelectedMap(SnapshotToSend snapshotToSend);

    /** Send that turn is finished
     */
    public abstract void finishTurn();

    /** Send starting market
     */
    public abstract void sendStartMarket();

    /** Send starting the buy phase
     */
    public abstract void sendStartBuyPhase();

    /** Send that is disable the market phase
     */
    public abstract void disableMarketPhase();

    /** Send that a permit card must be selected
     */
    public abstract void selectPermitCard();

    /** Send that a city bonus must be chosen
     * @param snapshotToSend is the snapshot
     */
    public abstract void selectCityRewardBonus(SnapshotToSend snapshotToSend);

    /** Send the move of the king
     * @param kingPath is the city where king moves
     */
    public abstract void moveKing(ArrayList<City> kingPath);

    /** Send that match is finished
     * @param finalSnapshot is the rank
     */
    public abstract void sendMatchFinishedWithWin(ArrayList<BaseUser> finalSnapshot);

    /** Test RMI connection
     */
    public abstract void ping();

    /** Select an old permit card
     */
    public abstract void selectOldPermitCard();

    /** Send that an user is offline
     * @param username is the user disconnected
     */
    public abstract void sendUserDisconnect(String username);
}
