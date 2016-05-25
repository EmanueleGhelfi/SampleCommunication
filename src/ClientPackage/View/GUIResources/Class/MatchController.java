package ClientPackage.View.GUIResources.Class;

import ClientPackage.Controller.ClientController;
import CommonModel.Snapshot.SnapshotToSend;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;

import java.util.Optional;

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

    public void mainActionElectCouncilor(){
        String parameter = null;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("ELEGGI IL CONSIGLIERE");
        alert.setHeaderText(null);
        alert.setContentText("Dai");
        ButtonType buttonBlack = new ButtonType("BLACK");
        ButtonType buttonWhite = new ButtonType("WHITE");
        alert.getButtonTypes().setAll(buttonBlack, buttonWhite);
        Optional<ButtonType> result = alert.showAndWait();
        switch (result.get().getText()){
            case "BLACK":
                parameter = "BLACK";
                break;
            case "WHITE":
                parameter = "WHITE";
                break;
        }
        clientController.mainActionElectCouncilor(parameter);

    }

    public void mainActionBuyPermitCard(){

    }

    public void mainActionBuildWithPermitCard(){

    }

    public void mainActionBuildWithKingHelp(){

    }

    public void fastActionChangePermitCardWithHelper(){
    }

    public void fastActionElectCouncilorWithHelper(){
        clientController.fastActionElectCouncilorWithHelper();
    }

    public void fastActionMoneyForHelper(){

    }

    public void fastActionNewMainAction(){

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
