package ClientPackage.View.GUIResources.Class;

import ClientPackage.Controller.ClientController;
import CommonModel.GameModel.Council.Council;
import CommonModel.GameModel.Council.Councilor;
import CommonModel.Snapshot.SnapshotToSend;
import Utilities.Exception.CouncilNotFoundException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import java.util.Optional;

/**
 * Created by Giulio on 17/05/2016.
 */
public class MatchController implements Initializable {

    private ClientController clientController;
    private boolean myTurn;
    @FXML Button buttonMain1;
    @FXML AnchorPane background;
    private SnapshotToSend currentSnapshot;

    @FXML
    Label nobilityPathText;

    @FXML
    Label richPathText;

    @FXML
    Label helperText;

    @FXML
    Label victoryPathText;

    @FXML private HBox regionCoast;
    @FXML private HBox regionHill;
    @FXML private HBox regionMountain;
    @FXML private HBox regionKing;

    private List<Circle> circlesCoast = new ArrayList<>();
    private List<Circle> circlesHill= new ArrayList<>();
    private List<Circle> circlesMountain= new ArrayList<>();
    private List<Circle> circlesKing= new ArrayList<>();

    private ArrayList<Councilor> coastCouncil = new ArrayList<>();
    private ArrayList<Councilor> hillCouncil = new ArrayList<>();
    private ArrayList<Councilor> mountainCouncil = new ArrayList<>();
    private ArrayList<Councilor> kingCouncil = new ArrayList<>();




    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
        currentSnapshot = clientController.getSnapshot();
        System.out.println("setting image");
        background.setStyle("-fx-background-image: url('"+currentSnapshot.getMap().getMapPreview()+"')");
        createArray();
    }

    private void createArray() {
        for (Node node : regionCoast.getChildren()) {
            circlesCoast.add((Circle) node);
        }

        for (Node node : regionHill.getChildren()) {
            circlesHill.add((Circle) node);
        }

        for (Node node : regionMountain.getChildren()) {
            circlesMountain.add((Circle) node);
        }

        for (Node node : regionKing.getChildren()) {
            circlesKing.add((Circle) node);
        }
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

    public void setMyTurn(boolean value, SnapshotToSend snapshot) {
        myTurn = value;
        turnFinished(myTurn);
       updateSnapshot(snapshot);
    }

    public void turnFinished(boolean thisTurn) {
        boolean myTurnValue= !thisTurn;
        buttonMain1.setDisable(myTurnValue);
    }

    public void updateSnapshot(SnapshotToSend snapshot) {
        System.out.println("on update snapshot <- Match controller");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                nobilityPathText.setText(snapshot.getCurrentUser().getNobilityPathPosition().getPosition()+"");
                richPathText.setText(snapshot.getCurrentUser().getCoinPathPosition()+"");
                helperText.setText(snapshot.getCurrentUser().getHelpers()+"");
                victoryPathText.setText(snapshot.getCurrentUser().getVictoryPathPosition()+"");

                try {
                    coastCouncil = snapshot.getCouncil(CommonModel.GameModel.City.RegionName.COAST);
                    fillCircle(circlesCoast,coastCouncil);
                    hillCouncil = snapshot.getCouncil(CommonModel.GameModel.City.RegionName.HILL);
                    fillCircle(circlesHill,hillCouncil);
                    mountainCouncil = snapshot.getCouncil(CommonModel.GameModel.City.RegionName.MOUNTAIN);
                    fillCircle(circlesMountain,mountainCouncil);
                    kingCouncil = new ArrayList<Councilor>(snapshot.getKing().getCouncil().getCouncil());
                    fillCircle(circlesKing,kingCouncil);
                } catch (CouncilNotFoundException e) {
                    e.printStackTrace();
                }


            }
        });

    }

    private void fillCircle(List<Circle> circles, ArrayList<Councilor> council) {

        for (int i = 0 ; i<circles.size();i++){
            System.out.println(council.get(i).getColor().name());
            circles.get(i).fillProperty().setValue(Paint.valueOf(council.get(i).getColor().name()));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {




    }
}
