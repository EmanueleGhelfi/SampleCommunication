package ClientPackage.View.GUIResources.Class;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.GeneralView.GUIView;
import CommonModel.Snapshot.SnapshotToSend;

/**
 * Created by Giulio on 28/05/2016.
 */
public interface BaseController {

    public void updateView();

    public void setClientController(ClientController clientController, GUIView guiView);

    void setMyTurn(boolean myTurn, SnapshotToSend snapshot);

    void onStartMarket();

    void onStartBuyPhase();

    void onFinishMarket();

    void onResizeHeight(double height, double width);

    void onResizeWidth(double width, double height);

    void selectPermitCard();

    void selectCityRewardBonus();
}
