package ClientPackage.View.GUIResources.Class;

import ClientPackage.Controller.ClientController;
import CommonModel.Snapshot.SnapshotToSend;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.controlsfx.control.PopOver;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by Giulio on 17/05/2016.
 */
public class MatchController implements Initializable{

    private ClientController clientController;
    private boolean myTurn;
    private SnapshotToSend currentSnapshot;
    private PopOver popOver = new PopOver();
    private Pane paneOfPopup = new Pane();
    private JFXComboBox<String> councilorColorToChoose = new JFXComboBox<>();
    @FXML private ImageView imageTest;
    @FXML Button buttonMain1;
    @FXML Button buttonMain2;
    @FXML AnchorPane background;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        popOver.setContentNode(paneOfPopup);
    }

    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
        councilorColorToChoose.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                clientController.mainActionElectCouncilor(councilorColorToChoose.getSelectionModel().getSelectedItem());
            }
        });
        currentSnapshot = clientController.getSnapshot();
        System.out.println("setting image");
        background.setStyle("-fx-background-image: url('"+currentSnapshot.getMap().getMapPreview()+"')");
    }



    public void mainActionElectCouncilor(){

        ArrayList<String> colorList = new ArrayList<>();
        colorList.add("WHITE");
        colorList.add("BLACK");

        ObservableList<String> observableColorList = FXCollections.observableArrayList(colorList);
        councilorColorToChoose.setItems(observableColorList);

        paneOfPopup.getChildren().addAll(councilorColorToChoose);
        popOver.show(imageTest);

        /*
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
        */
    }

    public void mainActionBuyPermitCard(){
        popOver = new PopOver();
        popOver.show(buttonMain2);
    }

    public void mainActionBuildWithPermitCard(){
        popOver = new PopOver();
        clientController.getSnapshot();
        ArrayList<String>
        for (:) {

        }


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
