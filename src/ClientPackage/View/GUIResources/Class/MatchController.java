package ClientPackage.View.GUIResources.Class;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.GUIResources.customComponent.CityButton;
import ClientPackage.View.GUIResources.customComponent.CouncilorHandler;
import ClientPackage.View.GUIResources.customComponent.PermitCardHandler;
import ClientPackage.View.GUIResources.customComponent.SideNode;
import ClientPackage.View.GeneralView.GUIView;
import CommonModel.GameModel.Action.*;
import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.City.*;
import CommonModel.GameModel.Council.Councilor;
import CommonModel.Snapshot.BaseUser;
import CommonModel.Snapshot.SnapshotToSend;
import Utilities.Exception.CouncilNotFoundException;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import com.sun.rowset.internal.Row;
import javafx.application.Platform;
import com.jfoenix.controls.JFXComboBox;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
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
    private PermitCard permitCardSelected = null;
    //BUTTONs
    @FXML JFXButton coastPermitButton;
    @FXML JFXButton hillPermitButton;
    @FXML JFXButton mountainPermitButton;

    //LABELS

    @FXML Pane background;
    @FXML Label nobilityPathText;
    @FXML Label richPathText;
    @FXML Label helperText;
    @FXML Label victoryPathText;
    @FXML Label fastActionText;
    @FXML Label mainActionText;
    @FXML Label turnText;

    //HBOX
    @FXML private HBox regionCoast;
    @FXML private HBox regionHill;
    @FXML private HBox regionMountain;
    @FXML private HBox regionKing;
    @FXML private HBox coastHBox;
    @FXML private HBox hillHBox;
    @FXML private HBox mountainHBox;
    @FXML private StackPane bottomPane;

    // Include
    @FXML private GridPane path;
    @FXML private PathController pathController;
    @FXML private BorderPane shop;
    @FXML private ShopController shopController;
    private HiddenSidesPane hiddenSidesPane;
    @FXML private ImageView backgroundImage;
    @FXML private GridPane gridPane;
    @FXML private TabPane tabPane;

    @FXML private JFXHamburger hamburgerIcon;
    @FXML private GridPane hamburgerMenu;



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
        initController();

        System.out.println("setting image");
        backgroundImage.setImage(new Image(currentSnapshot.getMap().getMapPreview()));
        backgroundImage.setPreserveRatio(true);
        backgroundImage.fitHeightProperty().bind(gridPane.heightProperty());
        backgroundImage.fitWidthProperty().bind(gridPane.widthProperty());
        gridPane.prefWidthProperty().bind(background.prefWidthProperty());
        gridPane.prefHeightProperty().bind(background.prefHeightProperty());



        backgroundImage.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) {
                System.out.println("changed to "+newValue.getWidth()+" "+" "+newValue.getHeight());
                background.setPrefWidth(newValue.getWidth());
                background.setPrefHeight(newValue.getHeight());
               // gridPane.setPrefSize(background.getPrefWidth(),background.getMaxHeight());

            }
        });

        /*
        backgroundImage.boundsInLocalProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) {
                background.setPrefWidth(newValue.getWidth());
                background.setPrefHeight(newValue.getHeight());
            }
        });
        */

        backgroundImage.fitWidthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                System.out.println("image: "+newValue.doubleValue());
                System.out.println("background "+ background.getWidth() );
            }
        });



        //background.setStyle("-fx-background-image: url('"+currentSnapshot.getMap().getMapPreview()+"')");
        //createArray();
        createPermitCard(coastHBox,clientController.getSnapshot().getVisiblePermitCards(),RegionName.COAST);
        createPermitCard(hillHBox,clientController.getSnapshot().getVisiblePermitCards(),RegionName.HILL);
        createPermitCard(mountainHBox,clientController.getSnapshot().getVisiblePermitCards(),RegionName.MOUNTAIN);
        createOverlay();
        initPermitButton();
        handleClick();
        createCity();
        populateHamburgerMenu();
        initHamburgerIcon();
    }

    private void populateHamburgerMenu() {
        JFXComboBox<String> users = new JFXComboBox<>();
        clientController.getSnapshot().getUsersInGame().forEach((s, baseUser) -> {
            users.getItems().add(baseUser.getUsername());
        });
        hamburgerMenu.add(users,1,0);
        GridPane.setValignment(users,VPos.CENTER);
        GridPane.setHalignment(users,HPos.CENTER);
    }

    private void initHamburgerIcon() {
        HamburgerSlideCloseTransition burgerTask = new HamburgerSlideCloseTransition(hamburgerIcon);
        burgerTask.setRate(-1);
        hamburgerIcon.addEventHandler(MouseEvent.MOUSE_PRESSED, (e)->{

            burgerTask.setRate(burgerTask.getRate()*-1);
            if(burgerTask.getRate()==1){
                openSlider();
            }
            else{
                closeSlider();
            }
            burgerTask.play();
        });
    }

    private void closeSlider() {
        hamburgerMenu.setPrefHeight(0);
        hamburgerMenu.setPrefWidth(0);
        hamburgerMenu.setVisible(false);
        hamburgerIcon.setTranslateX(0);
        //hamburgerIcon.setBackground(new Background(new BackgroundFill(Paint.valueOf("BLACK"),null,null)));

    }

    private void openSlider() {

        hamburgerMenu.setVisible(true);
        hamburgerMenu.setPrefHeight(backgroundImage.getFitHeight());
        hamburgerMenu.setPrefWidth(backgroundImage.getFitWidth()/5);
        //hamburgerMenu.setBackground(new Background(new BackgroundFill(Paint.valueOf("Black"),null,null)));
        //hamburgerIcon.setTranslateX(-hamburgerMenu.getPrefWidth());
    }

    private void createCity() {

        //arkon
        CreateSingleCity(0.15,0.13,currentSnapshot.getMap().getCity().get(0));
        //burgen
        CreateSingleCity(0.11,0.43, currentSnapshot.getMap().getCity().get(1));
        //castrum
        CreateSingleCity(0.27,0.25, currentSnapshot.getMap().getCity().get(2));
        //dorful
        CreateSingleCity(0.27,0.49, currentSnapshot.getMap().getCity().get(3));
        //esti
        CreateSingleCity(0.17,0.69, currentSnapshot.getMap().getCity().get(4));
        //framek
        CreateSingleCity(0.41,0.18, currentSnapshot.getMap().getCity().get(5));
        //graden
        CreateSingleCity(0.41,0.40, currentSnapshot.getMap().getCity().get(6));
        //hellar
        CreateSingleCity(0.43,0.65, currentSnapshot.getMap().getCity().get(7));
        //Indur
        CreateSingleCity(0.55,0.2,currentSnapshot.getMap().getCity().get(8));
        //Kultos
        CreateSingleCity(0.7,0.17,currentSnapshot.getMap().getCity().get(10));
        //Lyram
        CreateSingleCity(0.66,0.44, currentSnapshot.getMap().getCity().get(11));
        //Merkatim
        CreateSingleCity(0.64,0.69, currentSnapshot.getMap().getCity().get(12));
        //Naris
        CreateSingleCity(0.80,0.32, currentSnapshot.getMap().getCity().get(13));
        //Osium
        CreateSingleCity(0.78,0.59, currentSnapshot.getMap().getCity().get(14));
        //Juvelar
        CreateSingleCity(0.54,0.48,currentSnapshot.getMap().getCity().get(9));


    }

    private void CreateSingleCity(double layoutX, double layoutY, City city) {
        //Circle castrum = new Circle();
        ImageView imageView = new ImageView();

        //castrum.setFill(Paint.valueOf("BLACK"));
        //castrum.radiusProperty().bind(background.widthProperty().divide(20));
        System.out.println(city.getCityName().toString().toLowerCase());
        try {
            imageView.setImage(new Image("/ClientPackage/View/GUIResources/Image/City/"+city.getColor().getColor().toLowerCase()+".png"));
        }
        catch (Exception e){
            imageView.setImage(new Image("/ClientPackage/View/GUIResources/Image/City/blue.png"));
        }
        imageView.fitHeightProperty().bind(background.heightProperty().multiply(0.17));
        imageView.fitWidthProperty().bind(background.widthProperty().divide(9));
        background.getChildren().add(imageView);
        imageView.layoutXProperty().bind(background.widthProperty().multiply(layoutX));
        imageView.layoutYProperty().bind(background.heightProperty().multiply(layoutY));
        imageView.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                imageView.setScaleX(1.2);
                imageView.setScaleY(1.2);
            }
        });
        imageView.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                imageView.setScaleY(1);
                imageView.setScaleX(1);
            }
        });

        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                showPopoverOnCity(city,imageView);
            }
        });
    }

    private void showPopoverOnCity(City city, ImageView imageView) {
        PopOver popOver = new PopOver();

        //TODO: show button and other stuff
        GridPane grid = new GridPane();
        ColumnConstraints columnConstraints = new ColumnConstraints();
        ColumnConstraints columnConstraints1 = new ColumnConstraints();
        columnConstraints.setPercentWidth(50);
        columnConstraints1.setPercentWidth(50);
        RowConstraints rowConstraints = new RowConstraints();
        RowConstraints rowConstraints1 = new RowConstraints();
        RowConstraints rowConstraints2 = new RowConstraints();
        rowConstraints.setPercentHeight(20);
        rowConstraints1.setPercentHeight(40);
        rowConstraints2.setPercentHeight(40);
        grid.getColumnConstraints().addAll(columnConstraints,columnConstraints1);
        grid.getRowConstraints().addAll(rowConstraints,rowConstraints1,rowConstraints2);
        Label cityLabel = new Label(city.getCityName().getCityName());
        grid.add(cityLabel,0,0);
        GridPane.setHalignment(cityLabel,HPos.CENTER);
        GridPane.setValignment(cityLabel,VPos.CENTER);
        GridPane.setColumnSpan(cityLabel,2);

        /*
        TreeItem<String> treeItem = new TreeItem<>("Utenti con empori nella città:");
        for (BaseUser baseUser : clientController.getSnapshot().getUsersInGame().values()) {
            if(baseUser.getUsersEmporium().contains(city)){
                System.out.println("Found emporium");
                TreeItem<String> user = new TreeItem<>(baseUser.getUsername());
                treeItem.getChildren().add(user);
            }
            else{
                System.out.println("emporium not found");
                TreeItem<String> user = new TreeItem<>(baseUser.getUsername());
                treeItem.getChildren().add(user);
            }
        }
        TreeView<String> treeView = new TreeView<>(treeItem);
        grid.add(treeView,0,1);
        GridPane.setColumnSpan(treeView,2);
        */
        JFXComboBox<String> jfxComboBox = new JFXComboBox<>();
        for (PermitCard permitCard :
                clientController.getSnapshot().getCurrentUser().getPermitCards()) {
            jfxComboBox.getItems().add(permitCard.getCityString());
        }

        jfxComboBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                System.out.println("Selection changed to: "+newValue);
                permitCardSelected = clientController.getSnapshot().getCurrentUser().getPermitCards().get(newValue.intValue());
            }
        });
        jfxComboBox.setPromptText("Seleziona la carta permesso");
        grid.add(jfxComboBox,0,2);

        JFXButton jfxButton = new JFXButton("Compra emporio!");
        jfxButton.getStyleClass().add("buyEmporiumButton");

        jfxButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Action action = new MainActionBuildWithPermitCard(city,permitCardSelected);
                clientController.doAction(action);
            }
        });

        grid.add(jfxButton,1,2);
        //treeView.setPrefHeight(backgroundImage.getFitHeight()/7);
        grid.setPrefSize(backgroundImage.getFitWidth()/3,backgroundImage.getFitHeight()/3);

        popOver.setContentNode(grid);
        popOver.show(imageView);


    }

    private void handleClick() {
        background.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("event x and y "+event.getX()/background.getWidth()+" "+event.getY()/background.getHeight());
                System.out.println("Scene  "+event.getSceneX()+" "+event.getSceneY());
                System.out.println("Altro "+event.getScreenX()+" "+event.getScreenY());
            }
        });
    }

    private void initController() {
        pathController.setClientController(clientController,guiView);
        shopController.setClientController(clientController,guiView);
    }

    private void initPermitButton() {
        coastPermitButton.setOnMouseClicked(new PermitButtonHandler(RegionName.COAST));
        hillPermitButton.setOnMouseClicked(new PermitButtonHandler(RegionName.HILL));
        mountainPermitButton.setOnMouseClicked(new PermitButtonHandler(RegionName.MOUNTAIN));
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
                        imageView.setFitHeight(10.0);
                        imageView.setFitWidth(10.0);
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
                imageView.setFitHeight(10.0);
                imageView.setFitWidth(10.0);
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
        this.currentSnapshot = snapshot;
        turnFinished(myTurn);
        updateView();
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
    public void onResizeHeight(double height, double width) {

    }

    @Override
    public void onResizeWidth(double width, double height) {

    }

    private void turnFinished(boolean thisTurn) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if(thisTurn){
                    turnText.setText("E' il tuo turno!");
                }
                else{
                    turnText.setText("Non è il tuo turno!");
                }

            }
        });

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
                nobilityPathText.setText(currentSnapshot.getCurrentUser().getNobilityPathPosition().getPosition()+"");
                richPathText.setText(currentSnapshot.getCurrentUser().getCoinPathPosition()+"");
                helperText.setText(currentSnapshot.getCurrentUser().getHelpers().size()+"");
                victoryPathText.setText(currentSnapshot.getCurrentUser().getVictoryPathPosition()+"");
                mainActionText.setText(currentSnapshot.getCurrentUser().getMainActionCounter()+"");
                fastActionText.setText(currentSnapshot.getCurrentUser().getFastActionCounter()+"");

                reprintCouncilor();
                createPermitCard(coastHBox,clientController.getSnapshot().getVisiblePermitCards(),RegionName.COAST);
                createPermitCard(hillHBox,clientController.getSnapshot().getVisiblePermitCards(),RegionName.HILL);
                createPermitCard(mountainHBox,clientController.getSnapshot().getVisiblePermitCards(),RegionName.MOUNTAIN);

    }

    /**
     * Called on click on helper image
     * @param event
     */
    public void buyHelper(Event event) {
        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Action action = new FastActionMoneyForHelper();
                clientController.doAction(action);
            }
        };

        String buttonText = "Compra aiutanti";
        String infoLabel = "Azione veloce: Compra un aiutante per tre monete!";

        showDefaultPopOver(eventHandler,infoLabel,buttonText,(Node)event.getSource());
    }

    public void showDefaultPopOver(EventHandler<MouseEvent> eventHandler, String infoLabel, String buttonText, Node source){
        PopOver popOver = new PopOver();
        VBox vBox = new VBox();
        JFXButton jfxButton = new JFXButton();
        jfxButton.getStyleClass().add("button-raised-second");
        jfxButton.setText(buttonText);
        jfxButton.setOnMouseClicked(eventHandler);
        Label label = new Label();
        label.setText(infoLabel);
        vBox.getChildren().add(label);
        vBox.getChildren().add(jfxButton);
        vBox.setSpacing(30);
        vBox.setPadding(new Insets(40,40,40,40));
        popOver.setContentNode(vBox);
        popOver.show(source);
    }

    public void buyMainAction(Event event) {
        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Action action = new FastActionNewMainAction();
                clientController.doAction(action);
            }
        };

        String buttonText = "Compra Azione!";
        String infoLabel = "Azione veloce: Ottieni un azione principale per 3 aiutanti!!";

        showDefaultPopOver(eventHandler,infoLabel,buttonText,(Node)event.getSource());
    }

    private class PermitButtonHandler implements EventHandler<MouseEvent>{

        private RegionName regionName;

        PermitButtonHandler(RegionName regionName) {
            this.regionName = regionName;
        }

        @Override
        public void handle(MouseEvent event) {
            Action action = new FastActionChangePermitCardWithHelper(regionName);
            clientController.doAction(action);
        }
    }

    public void setPermitCardSelected(PermitCard permitCardSelected) {
        this.permitCardSelected = permitCardSelected;
    }
}
