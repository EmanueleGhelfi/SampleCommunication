package ClientPackage.View.GeneralView;

import ClientPackage.Controller.ClientController;
import CommonModel.GameModel.City.City;
import CommonModel.Snapshot.SnapshotToSend;
import Server.Model.Map;
import Utilities.Exception.ActionNotPossibleException;

import java.util.ArrayList;

/**
 * Created by Emanuele on 13/05/2016.
 */
//TODO
public class CLIView implements BaseView {

    public CLIView(ClientController clientController) {
        // to implement
    }

    @Override
    public void initView() {
        System.out.println("CLI Started correctly");
    }

    @Override
    public void showLoginError() {
    }

    @Override
    public void showWaitingForStart() {
    }

    @Override
    public void showMap(ArrayList<Map> mapArrayList) {

    }

    @Override
    public void gameInitialization(SnapshotToSend snapshotToSend) {

    }

    @Override
    public void turnFinished() {

    }

    @Override
    public void isMyTurn(SnapshotToSend snapshot) {

    }

    @Override
    public void updateSnapshot() {

    }

    @Override
    public void onStartMarket() {

    }

    @Override
    public void onStartBuyPhase() {

    }

    @Override
    public void onFinishMarket() {

    }

    @Override
    public void selectPermitCard() {

    }

    @Override
    public void selectCityRewardBonus() {

    }

    @Override
    public void onMoveKing(ArrayList<City> kingPath) {

    }

    @Override
    public void onActionNotPossibleException(ActionNotPossibleException e) {

    }

    @Override
    public void sendMatchFinishedWithWin(boolean win) {

    }

    @Override
    public void selectOldPermitCardBonus() {

    }

}
