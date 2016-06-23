package Server.NetworkInterface.Communication;

import CommonModel.GameModel.City.City;
import CommonModel.Snapshot.SnapshotToSend;
import Server.Model.Map;
import Server.Model.User;

import java.util.ArrayList;

/**
 * Created by Giulio on 16/06/2016.
 */
public class FakeCommunication extends BaseCommunication {
    @Override
    public void setUser(User user) {

    }

    @Override
    public void sendSnapshot(SnapshotToSend snapshotToSend) {

    }

    @Override
    public void changeRound() {

    }

    @Override
    public void sendAvailableMap(ArrayList<Map> availableMaps) {

    }

    @Override
    public void sendSelectedMap(SnapshotToSend snapshotToSend) {

    }

    @Override
    public void finishTurn() {

    }

    @Override
    public void sendStartMarket() {

    }

    @Override
    public void sendStartBuyPhase() {


    }

    @Override
    public void disableMarketPhase() {

    }

    @Override
    public void selectPermitCard() {

    }

    @Override
    public void selectCityRewardBonus(SnapshotToSend snapshotToSend) {

    }

    @Override
    public void moveKing(ArrayList<City> kingPath) {

    }

    @Override
    public void sendMatchFinishedWithWin(boolean win) {

    }

    @Override
    public void ping() {

    }

    @Override
    public void selectOldPermitCard() {

    }
}
