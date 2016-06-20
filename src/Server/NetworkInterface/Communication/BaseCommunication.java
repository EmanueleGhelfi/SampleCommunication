package Server.NetworkInterface.Communication;

import CommonModel.GameModel.City.City;
import CommonModel.Snapshot.SnapshotToSend;
import Server.Model.Map;
import Server.Model.User;

import java.util.ArrayList;

/**
 * Created by Emanuele on 09/05/2016.
 */
public abstract class BaseCommunication {

    public abstract void setUser (User user);

    public abstract void sendSnapshot(SnapshotToSend snapshotToSend);

    public abstract void changeRound();

    public abstract void sendAvailableMap(ArrayList<Map> availableMaps);

    public abstract void sendSelectedMap(SnapshotToSend snapshotToSend);

    public abstract void finishTurn();

    public abstract void sendStartMarket();

    public abstract void sendStartBuyPhase();

    public abstract void disableMarketPhase();

    public abstract void selectPermitCard();

    public abstract void selectCityRewardBonus(SnapshotToSend snapshotToSend);

    public abstract void moveKing(ArrayList<City> kingPath);

    public abstract void sendMatchFinishedWithWin(boolean win);

    public abstract void ping();

    public abstract void selectOldPermitCard();
}
