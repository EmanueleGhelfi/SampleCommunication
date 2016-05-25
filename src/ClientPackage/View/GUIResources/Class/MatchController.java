package ClientPackage.View.GUIResources.Class;

import ClientPackage.Controller.ClientController;
import CommonModel.Snapshot.SnapshotToSend;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

/**
 * Created by Giulio on 17/05/2016.
 */
public class MatchController {

    private ClientController clientController;
    private boolean myTurn;
    @FXML Button buttonMain1;
    @FXML
    AnchorPane background;
    private SnapshotToSend currentSnapshot;

    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
        currentSnapshot = clientController.getSnapshot();
        System.out.println("setting image");
        background.setStyle("-fx-background-image: url('"+currentSnapshot.getMap().getMapPreview()+"')");
    }

    public void main1(){
        clientController.main1();
    }

    public void main2(){


    }

    public void main3(){

    }

    public void main4(){

    }

    public void fast1(){
        clientController.fast1();
    }

    public void fast2(){

    }

    public void fast3(){

    }

    public void fast4(){

    }

    public void setMyTurn(boolean value) {
        myTurn = value;
        turnFinished(myTurn);
    }

    public void turnFinished(boolean thisTurn) {
        boolean myTurnValue= !thisTurn;
        buttonMain1.setDisable(myTurnValue);
    }
}
