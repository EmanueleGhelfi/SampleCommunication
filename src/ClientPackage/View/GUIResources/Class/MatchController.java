package ClientPackage.View.GUIResources.Class;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.GUIResources.customComponent.CityButton;
import ClientPackage.View.GUIResources.customComponent.CouncilorHandler;
import ClientPackage.View.GUIResources.customComponent.PermitCardHandler;
import ClientPackage.View.GUIResources.customComponent.SideNode;
import ClientPackage.View.GeneralView.GUIView;
import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.City.*;
import CommonModel.GameModel.Council.Councilor;
import CommonModel.Snapshot.SnapshotToSend;
import Utilities.Exception.CouncilNotFoundException;
import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.*;

import javafx.scene.text.Text;
import javafx.stage.Screen;
import org.controlsfx.control.HiddenSidesPane;
import org.controlsfx.control.PopOver;

import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by Giulio on 17/05/2016.
 */
public class MatchController implements Initializable, BaseController {

    private ClientController clientController;
    private boolean myTurn;
    private SnapshotToSend currentSnapshot;
    private PopOver popOver = new PopOver();
    private Pane paneOfPopup = new Pane();
    private JFXComboBox<String> councilorColorToChoose = new JFXComboBox<>();
    private String city;
    private GUIView guiView;
    @FXML private ImageView imageTest;
    @FXML Button buttonMain1;
    @FXML Button buttonMain2;
    @FXML BorderPane background;
    @FXML Label nobilityPathText;
    @FXML Label richPathText;
    @FXML Label helperText;
    @FXML Label victoryPathText;
    @FXML private HBox regionCoast;
    @FXML private HBox regionHill;
    @FXML private HBox regionMountain;
    @FXML private HBox regionKing;
    @FXML private HBox coastHBox;
    @FXML private HBox hillHBox;
    @FXML private HBox mountainHBox;
    @FXML private StackPane bottomPane;
    @FXML private GridPane path;
    @FXML private PathController pathController;
    private HiddenSidesPane hiddenSidesPane;


    private HashMap<RegionName,ArrayList<ImageView>> councilHashMap = new HashMap<>();
    private ArrayList<ImageView> kingCouncil = new ArrayList<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        popOver.setContentNode(paneOfPopup);
    }

    public void setClientController(ClientController clientController, GUIView guiView) {
        this.clientController = clientController;
        this.guiView = guiView;
        hiddenSidesPane = new HiddenSidesPane();
        mainActionBuildWithPermitCard();
        currentSnapshot = clientController.getSnapshot();
        guiView.registerBaseController(this);
        pathController.setClientController(clientController,guiView);
        System.out.println("setting image");
        background.setStyle("-fx-background-image: url('"+currentSnapshot.getMap().getMapPreview()+"')");
        //createArray();
        createPermitCard(coastHBox,clientController.getSnapshot().getVisiblePermitCards(),RegionName.COAST);
        createPermitCard(hillHBox,clientController.getSnapshot().getVisiblePermitCards(),RegionName.HILL);
        createPermitCard(mountainHBox,clientController.getSnapshot().getVisiblePermitCards(),RegionName.MOUNTAIN);
        createOverlay();
    }

    private void createOverlay() {
        if(bottomPane.getChildren().contains(hiddenSidesPane)) {
            bottomPane.getChildren().remove(hiddenSidesPane);
        }
        hiddenSidesPane = new HiddenSidesPane();
        HBox hbox1 = new HBox();
        HBox hbox2 = new HBox();
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        for(RegionName regionName: RegionName.values()) {
            HBox hBox = new HBox();
            try {
                ArrayList<Councilor> councilors = currentSnapshot.getCouncil(regionName);
                ArrayList<ImageView> imageViews = new ArrayList<>();
                for (int i = 0; i< councilors.size();i++){
                    ImageView imageView = new ImageView();
                    try {
                        imageView.setImage(new Image(councilors.get(i).getColor().getImageUrl()));
                        imageView.setFitHeight(50.0);
                        imageView.setFitWidth(50.0);
                        imageViews.add(imageView);
                    }
                    catch (IllegalArgumentException e){
                        System.out.println("not found image in match controller"+councilors.get(i).getColor().getImageUrl());
                    }
                    hBox.getChildren().add(imageView);
                }
                councilHashMap.put(regionName,imageViews);
                hbox1.getChildren().add(hBox);
                hBox.setPrefWidth(primaryScreenBounds.getWidth()/3);
                hBox.setOnMouseClicked(new CouncilorHandler(hBox,regionName,this,clientController,null));
            } catch (CouncilNotFoundException e) {
                e.printStackTrace();
            }
        }

        hbox1.setPrefWidth(primaryScreenBounds.getWidth());
        hbox1.setAlignment(Pos.CENTER);

        ArrayList<Councilor> kingCouncilors = new ArrayList<>(currentSnapshot.getKing().getCouncil().getCouncil());
        for (Councilor councilor: kingCouncilors){
            ImageView imageView = new ImageView();
            try{
                imageView.setImage(new Image(councilor.getColor().getImageUrl()));
                imageView.setFitHeight(50.0);
                imageView.setFitWidth(50.0);
                kingCouncil.add(imageView);
            }
            catch (IllegalArgumentException e){
                System.out.println("not found image match controller"+councilor.getColor().getImageUrl());
            }
            hbox2.getChildren().add(imageView);
        }
        hbox2.setOnMouseClicked(new CouncilorHandler(hbox2,null,this,clientController,currentSnapshot.getKing()));
        hbox2.setPrefWidth(primaryScreenBounds.getWidth());
        hbox2.setAlignment(Pos.CENTER);
        SideNode sideNode = new SideNode(10.0, Side.BOTTOM,hiddenSidesPane,hbox1,hbox2);
        sideNode.setStyle("-fx-background-color: rgba(143,147,147,.25);");
        hiddenSidesPane.setTop(sideNode);

        bottomPane.getChildren().add(hiddenSidesPane);

    }

    private void createPermitCard(HBox regionHBox, HashMap<RegionName,ArrayList<PermitCard>> permitDeck,RegionName regionName) {
        Set<Node> imageViews = regionHBox.lookupAll("#permitCard");
        int i = 0;
        for (Node node: imageViews) {
            node.setOnMouseClicked(new PermitCardHandler(permitDeck.get(regionName).get(i),this,clientController));
            i++;
        }

        i=0;
        for (Node node: regionHBox.lookupAll("#cityNames")){
            Label label = (Label) node;
            label.setText(permitDeck.get(regionName).get(i).getCityString());
            i++;
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
                CityButton cityButton = new CityButton(city, this);
                cityPane.getChildren().add(cityButton);
                cityButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {

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


    public void fastActionMoneyForHelper(){

    }

    public void fastActionNewMainAction(){

    }

    public void setMyTurn(boolean value, SnapshotToSend snapshot) {
        myTurn = value;
        turnFinished(myTurn);
        updateView();
    }

    private void turnFinished(boolean thisTurn) {
        boolean myTurnValue= !thisTurn;
        buttonMain1.setDisable(myTurnValue);
    }


    private void reprintCouncilor() {
        for (RegionName regionName : RegionName.values()) {
            ArrayList<Councilor> councilors = null;
            try {
                councilors = currentSnapshot.getCouncil(regionName);
                ArrayList<ImageView> imageView = councilHashMap.get(regionName);
                for(int i = 0; i< councilors.size();i++){
                    imageView.get(i).setImage(new Image(councilors.get(i).getColor().getImageUrl()));
                }
            } catch (CouncilNotFoundException e) {
                e.printStackTrace();
            }

        }

        ArrayList<Councilor> kingCouncilArray = new ArrayList<>(currentSnapshot.getKing().getCouncil().getCouncil());
        for (int i = 0; i<kingCouncil.size();i++) {
            kingCouncil.get(i).setImage(new Image(kingCouncilArray.get(i).getColor().getImageUrl()));
        }

    }

    private void fillCircle(List<Circle> circles, ArrayList<Councilor> council) {

        for (int i = 0 ; i<circles.size();i++){
            System.out.println(council.get(i).getColor().name());
            circles.get(i).fillProperty().setValue(Paint.valueOf(council.get(i).getColor().name()));
        }
    }


    @Override
    public void updateView() {
        this.currentSnapshot = clientController.getSnapshot();
        System.out.println("on update snapshot <- Match controller");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                nobilityPathText.setText(currentSnapshot.getCurrentUser().getNobilityPathPosition().getPosition()+"");
                richPathText.setText(currentSnapshot.getCurrentUser().getCoinPathPosition()+"");
                helperText.setText(currentSnapshot.getCurrentUser().getHelpers()+"");
                victoryPathText.setText(currentSnapshot.getCurrentUser().getVictoryPathPosition()+"");

                reprintCouncilor();
                createPermitCard(coastHBox,clientController.getSnapshot().getVisiblePermitCards(),RegionName.COAST);
                createPermitCard(hillHBox,clientController.getSnapshot().getVisiblePermitCards(),RegionName.HILL);
                createPermitCard(mountainHBox,clientController.getSnapshot().getVisiblePermitCards(),RegionName.MOUNTAIN);
            }
        });
    }
}
