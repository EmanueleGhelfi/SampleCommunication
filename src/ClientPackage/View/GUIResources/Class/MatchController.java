package ClientPackage.View.GUIResources.Class;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.GUIResources.customComponent.CityButton;
import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import CommonModel.GameModel.City.City;
import CommonModel.GameModel.Council.Council;
import CommonModel.GameModel.Council.Councilor;
import CommonModel.Snapshot.SnapshotToSend;
import Utilities.Exception.CouncilNotFoundException;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.text.Text;
import org.controlsfx.control.PopOver;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by Giulio on 17/05/2016.
 */
public class MatchController implements Initializable {

    private ClientController clientController;
    private boolean myTurn;
    //@FXML Button buttonMain1;
    //@FXML AnchorPane background;
    private SnapshotToSend currentSnapshot;
    private PopOver popOver = new PopOver();
    private Pane paneOfPopup = new Pane();
    private JFXComboBox<String> councilorColorToChoose = new JFXComboBox<>();
    private String city;
    @FXML private ImageView imageTest;
    @FXML Button buttonMain1;
    @FXML Button buttonMain2;
    @FXML AnchorPane background;
    @FXML Label nobilityPathText;
    @FXML Label richPathText;
    @FXML Label helperText;
    @FXML Label victoryPathText;
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
        mainActionBuildWithPermitCard();
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



    public void mainActionBuildWithPermitCard(){


        for (City city: clientController.getSnapshot().getMap().getCity()) {
            Pane cityPane = (Pane) background.lookup("#"+city.getCityName().getCityName());
            System.out.println(city.getCityName().getCityName());
            if (cityPane != null) {
                System.out.println("DIO PORCONE");
                CityButton cityButton = new CityButton(city, this);
                cityPane.getChildren().add(cityButton);
                cityButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        System.out.println("EHILAAAAAAAAAAAAAAAAAAAAA");

                        popOver = new PopOver();
                        paneOfPopup.getChildren().add(new Text("TESTING.."));
                        JFXListView<String> list = new JFXListView<String>();
                        ArrayList<String> ehehe = new ArrayList<String>();
                        for (PermitCard permitCard : clientController.getSnapshot().getCurrentUser().getPermitCards()) {
                            String temporaryChar = null;
                            for (Character character : permitCard.getCityAcronimous()) {
                                if (character.equals(city.getCityName().getCityName().charAt(0))) {
                                    temporaryChar = temporaryChar + permitCard.getCityAcronimous().toString();
                                }
                            }
                        }
                        list.setItems(FXCollections.observableArrayList(ehehe));
                        paneOfPopup.getChildren().add(list);
                        popOver.setContentNode(paneOfPopup);
                        popOver.show(cityPane);
                    }
                });
            }
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
                    coastCouncil = snapshot.getCouncil(CommonModel.GameModel.City.Region.COAST);
                    fillCircle(circlesCoast,coastCouncil);
                    hillCouncil = snapshot.getCouncil(CommonModel.GameModel.City.Region.HILL);
                    fillCircle(circlesHill,hillCouncil);
                    mountainCouncil = snapshot.getCouncil(CommonModel.GameModel.City.Region.MOUNTAIN);
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

    public void mainActionBuyPermitCard(Event event) {
        popOver.setContentNode(paneOfPopup);
        Pane pane = (Pane) event.getTarget();
        Label cityLabel = (Label) pane.lookup("#label");
        clientController.mainActionBuyPermitCard(cityLabel.getText());
        JFXCheckBox politicCardsCheckBox;
        VBox vBox = new VBox();
        for (PoliticCard politicCard: clientController.getSnapshot().getCurrentUser().getPoliticCards()){
            politicCardsCheckBox = new JFXCheckBox();
            politicCardsCheckBox.setText(politicCard.getPoliticColor().name());
            vBox.getChildren().add(politicCardsCheckBox);
        }
        paneOfPopup.getChildren().add(vBox);
        popOver.show(cityLabel);
    }
}
