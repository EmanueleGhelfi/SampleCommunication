package ClientPackage.View.GUIResources.Class;

import ClientPackage.Controller.ClientController;
import CommonModel.Snapshot.SnapshotToSend;

/**
 * Created by Giulio on 28/05/2016.
 */
public interface BaseController {

    public void updateView();

    public void setClientController(ClientController clientController);

    void setMyTurn(boolean myTurn, SnapshotToSend snapshot);
}
