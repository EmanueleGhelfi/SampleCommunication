package ClientPackage.View.GUIResources.Class;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.GUIResources.customComponent.*;
import ClientPackage.View.GeneralView.GUIView;
import CommonModel.GameModel.Action.*;
import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import CommonModel.GameModel.City.*;
import CommonModel.GameModel.City.Region;
import CommonModel.GameModel.Council.Councilor;
import CommonModel.Snapshot.BaseUser;
import CommonModel.Snapshot.SnapshotToSend;
import Utilities.Class.Constants;
import Utilities.Class.Graphics;
import Utilities.Exception.CouncilNotFoundException;
import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.*;

import javafx.scene.paint.Paint;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.util.Duration;
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
    @FXML private GridPane shop;
    @FXML private Pane nobilityPath;
    @FXML private ShopController shopController;
    @FXML private NobilityPathController nobilityPathController;
    private HiddenSidesPane hiddenSidesPane;
    private HiddenSidesPane nobilityHiddenSidesPane;
    @FXML private ImageView backgroundImage;
    @FXML private GridPane gridPane;
    @FXML private TabPane tabPane;

    //HAMBURGERMENU
    @FXML private JFXHamburger hamburgerIcon;
    @FXML private GridPane hamburgerMenu;
    @FXML private Label moneyLabel;
    @FXML private Label victoryLabel;
    @FXML private Label politicLabel;
    @FXML private Label helperLabel;
    @FXML private Label permitLabel;
    @FXML private Label nobilityLabel;

    @FXML private Button helpButton;

    private SingleSelectionModel<Tab> selectionModel;


    private HBox handHBox = new HBox();

    //Images
    @FXML private ImageView kingImage;

    private JFXComboBox<String> usersComboBox;

    private BooleanProperty pulseBonus;
    private BooleanProperty stopPulsePermitCard = new SimpleBooleanProperty(false);

    //Phase
    private boolean needToSelectOldBonus = false;
    private BooleanProperty needToSelectPermitCard = new SimpleBooleanProperty(false);


    private HashMap<RegionName,ArrayList<ImageView>> councilHashMap = new HashMap<>();
    private ArrayList<ImageView> kingCouncil = new ArrayList<>();

    private ArrayList<PoliticCard> politicCardforBuildWithKing = new ArrayList<>();
    private ArrayList<City> kingPathforBuild = new ArrayList<>();

    private BooleanProperty buildWithKingPhase = new SimpleBooleanProperty(false);
    private BooleanProperty pulseCity = new SimpleBooleanProperty(false);

    //node list for more and other button
    private JFXNodesList jfxNodesList = new JFXNodesList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        popOver.setContentNode(paneOfPopup);
    }

    public void setClientController(ClientController clientController, GUIView guiView) {
        this.clientController = clientController;
        this.guiView = guiView;
        hiddenSidesPane = new HiddenSidesPane();

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
        /*
        createPermitCard(coastHBox,clientController.getSnapshot().getVisiblePermitCards(),RegionName.COAST);
        createPermitCard(hillHBox,clientController.getSnapshot().getVisiblePermitCards(),RegionName.HILL);
        createPermitCard(mountainHBox,clientController.getSnapshot().getVisiblePermitCards(),RegionName.MOUNTAIN);
        */

        pulseCity.bind(buildWithKingPhase.not());
        createOverlay();
        initPermitButton();
        handleClick();
        createCity();
        gridPane.add(handHBox, 0, 2);
        createHand();
        //help();

        selectionModel = tabPane.getSelectionModel();

        populateHamburgerMenu();
        initHamburgerIcon();
        bottomPane.setVisible(false);
        nobilityPath.setVisible(false);
        kingPathforBuild.add(clientController.getSnapshot().getKing().getCurrentCity());
        createNodeList();

        GridPane.setHalignment(nobilityPath, HPos.CENTER);
        GridPane.setValignment(nobilityPath, VPos.BOTTOM);
    }

    private void createHand() {
        handHBox.getChildren().clear();
        handHBox.prefWidthProperty().bind(gridPane.prefWidthProperty());
        handHBox.prefHeightProperty().bind(gridPane.prefHeightProperty().divide(7));
        handHBox.setMouseTransparent(true);
        handHBox.setAlignment(Pos.CENTER);
        for (PoliticCard politicCard : clientController.getSnapshot().getCurrentUser().getPoliticCards()) {
            ImageView politicCardImageView = new ImageView();
            politicCardImageView.setImage(new Image(Constants.IMAGE_PATH + "/" + politicCard.getUrl() + ".png"));
            politicCardImageView.fitHeightProperty().bind(handHBox.prefHeightProperty());
            politicCardImageView.setPreserveRatio(true);
            handHBox.getChildren().add(politicCardImageView);
        }
        for (PermitCard permitCard : clientController.getSnapshot().getCurrentUser().getPermitCards()){
            StackPane permitStackPane = new StackPane();
            permitStackPane.setAlignment(Pos.CENTER);
            ImageView permitCardImageView = new ImageView();
            permitCardImageView.setImage(new Image(Constants.IMAGE_PATH + "/PermitCard.png"));
            permitCardImageView.fitHeightProperty().bind(handHBox.prefHeightProperty());
            permitCardImageView.setPreserveRatio(true);
            Label labelOfPermitCard = new Label();
            labelOfPermitCard.setText(permitCard.getCityString());
            permitStackPane.getChildren().addAll(permitCardImageView, labelOfPermitCard);
            handHBox.getChildren().add(permitStackPane);
        }

        GridPane.setValignment(handHBox, VPos.BOTTOM);
        GridPane.setHalignment(handHBox, HPos.CENTER);
        GridPane.setHalignment(handHBox, HPos.CENTER);
        GridPane.setColumnSpan(handHBox, 3);
    }


    private void createNodeList() {

        JFXButton finishKing = new JFXButton("FINISH");
        finishKing.setBackground(new Background(new BackgroundFill(Paint.valueOf("BLUE"),new CornerRadii(20),null)));
        finishKing.setButtonType(JFXButton.ButtonType.FLAT);
        finishKing.setTextFill(Paint.valueOf("WHITE"));
        finishKing.setOnAction(new FInishKingActionHandler(clientController,this));
        finishKing.disableProperty().bind(buildWithKingPhase.not());
        finishKing.setVisible(false);
        buildWithKingPhase.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    finishKing.setVisible(true);
                }
                else{
                    finishKing.setVisible(false);
                }
            }
        });
        gridPane.add(finishKing,2,2);
        //GridPane.setColumnSpan(finishKing,2);


        //NODE LIST

        JFXButton showMore = new JFXButton();
        ImageView imageMore = new ImageView(new Image(Constants.IMAGE_PATH+"plusMenu.png"));
        showMore.setGraphic(imageMore);
        showMore.setPrefHeight(50);
        showMore.setPrefWidth(50);
        imageMore.setFitHeight(30);
        imageMore.setFitWidth(30);
        showMore.setRotate(180);
        showMore.setButtonType(JFXButton.ButtonType.RAISED);
        showMore.setTooltip(new Tooltip("Show more action"));

        //change turn
        JFXButton changeTurnAction = new JFXButton();
        ImageView imageChange = new ImageView(new Image(Constants.IMAGE_PATH+"change.png"));
        imageChange.setFitHeight(50);
        imageChange.setFitWidth(50);
        changeTurnAction.setPrefHeight(50);
        changeTurnAction.setPrefWidth(50);
        changeTurnAction.setGraphic(imageChange);
        changeTurnAction.setButtonType(JFXButton.ButtonType.RAISED);
        changeTurnAction.setOnAction(new ChangeTurnHandler());
        showMore.setBackground(new Background(new BackgroundFill(Paint.valueOf("BLUE"),new CornerRadii(30),null)));
        changeTurnAction.setRotate(180);
        changeTurnAction.setTooltip(new Tooltip("Chage turn"));

        // HELP
        JFXButton helpButton = new JFXButton();
        ImageView helpImage = new ImageView(new Image(Constants.IMAGE_PATH+"QuestionMark.png"));
        helpImage.setFitHeight(50);
        helpImage.setFitWidth(50);
        helpImage.setFitHeight(50);
        helpImage.setFitWidth(50);
        helpButton.setPrefHeight(50);
        helpButton.setPrefWidth(50);
        helpButton.setGraphic(helpImage);
        helpButton.setRotate(180);
        helpButton.setBackground(new Background(new BackgroundFill(Paint.valueOf("WHITE"),new CornerRadii(30),null)));

        helpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
                selectionModel.selectNext();
                shopController.displayHelp();
            }
        });
        helpButton.setTooltip(new Tooltip("Help"));


        jfxNodesList.setSpacing(10);
        jfxNodesList.addAnimatedNode(showMore, (expanded) -> new ArrayList<KeyValue>(){{ add(
                new KeyValue(showMore.rotateProperty(), expanded? 225:180 ,
                Interpolator.EASE_BOTH));}});

        jfxNodesList.addAnimatedNode(changeTurnAction);

        jfxNodesList.setRotate(180);


        // REGION

        for(RegionName regionName : RegionName.values()){
            jfxNodesList.addAnimatedNode(getChangePermitCardButton(regionName));
        }


        jfxNodesList.addAnimatedNode(helpButton);
        gridPane.add(jfxNodesList,0,2);
        GridPane.setHalignment(jfxNodesList,HPos.RIGHT);
        GridPane.setValignment(jfxNodesList,VPos.TOP);
        jfxNodesList.visibleProperty().bind(nobilityPath.visibleProperty().not());
    }

    private JFXButton getChangePermitCardButton(RegionName regionName) {
        JFXButton jfxButton = new JFXButton();
        jfxButton.setTooltip(new Tooltip("Change permit card of "+regionName));
        jfxButton.setPrefHeight(50);
        jfxButton.setPrefWidth(50);
        ImageView regionImage = new ImageView(new Image(Constants.IMAGE_PATH+""+regionName+".png"));
        jfxButton.setGraphic(regionImage);
        //jfxButton.setTextFill(Paint.valueOf("WHITE"));
        jfxButton.setButtonType(JFXButton.ButtonType.RAISED);
        //jfxButton.setBackground(new Background(new BackgroundFill(Paint.valueOf("BLUE"),new CornerRadii(30),null)));
        jfxButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Action action = new FastActionChangePermitCardWithHelper(regionName);
                clientController.doAction(action);
            }
        });
        jfxButton.setRotate(180);
        return jfxButton;
    }

    private void populateHamburgerMenu() {
        usersComboBox = new JFXComboBox<>();
        clientController.getSnapshot().getUsersInGame().forEach((s, baseUser) -> {
            if(!(baseUser.getUsername().equals(clientController.getSnapshot().getCurrentUser().getUsername())))
                usersComboBox.getItems().add(baseUser.getUsername());
        });
        hamburgerMenu.add(usersComboBox,1,0);
        GridPane.setValignment(usersComboBox,VPos.CENTER);
        GridPane.setHalignment(usersComboBox,HPos.CENTER);
        usersComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                populateField(clientController.getSnapshot().getUsersInGame().get(newValue));
            }
        });
        usersComboBox.getSelectionModel().select(0);
        hamburgerMenu.prefHeightProperty().bind(gridPane.heightProperty());
    }

    private void populateField(BaseUser baseUser) {
        System.out.println("populate field");
        moneyLabel.setText(baseUser.getCoinPathPosition()+"");
        politicLabel.setText(baseUser.getPoliticCardNumber()+"");
        helperLabel.setText(baseUser.getHelpers().size()+"");
        nobilityLabel.setText(baseUser.getNobilityPathPosition().getPosition()+"");
        if(baseUser.getPermitCards().size()>0){
            permitLabel.setText(baseUser.getPermitCards().get(0).getCityString());
        }
        else{
            permitLabel.setText("No permit card");
        }
        victoryLabel.setText(baseUser.getVictoryPathPosition()+"");
    }

    private void initHamburgerIcon() {
        hamburgerMenu.setVisible(false);
        //hamburgerMenu.setPrefHeight(0);
        hamburgerMenu.setPrefWidth(0);
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
        hamburgerIcon.setTranslateX(0);
        //hamburgerIcon.setBackground(new Background(new BackgroundFill(Paint.valueOf("BLACK"),null,null)));
        final Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1000),
                new KeyValue(hamburgerMenu.prefWidthProperty(), 0)));

        List<Node> nodes = hamburgerMenu.getChildren();
        nodes.forEach(node -> {
            if(node instanceof ImageView) {
                ImageView imageView = (ImageView) node;
                timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), new KeyValue(imageView.scaleXProperty(), 0)));
                timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), new KeyValue(imageView.scaleYProperty(), 0)));
            }
            else{
                timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), new KeyValue(node.scaleXProperty(), 0)));
                timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), new KeyValue(node.scaleYProperty(), 0)));
            }
        });

        timeline.play();
        timeline.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //hamburgerMenu.setVisible(false);
            }
        });

    }

    private void openSlider() {


        hamburgerMenu.setVisible(true);
        hamburgerMenu.setScaleX(1);
        hamburgerMenu.setPrefWidth(0);
        final Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500),
                new KeyValue(hamburgerMenu.prefWidthProperty(), backgroundImage.getFitWidth()/5)));

        List<Node> nodes = hamburgerMenu.getChildren();
        nodes.forEach(node -> {
            if(node instanceof ImageView) {
                ImageView imageView = (ImageView) node;
                imageView.setScaleX(1);
                imageView.setScaleY(1);
                imageView.setFitHeight(0);
                imageView.setFitWidth(0);
                timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), new KeyValue(imageView.fitWidthProperty(), 40)));
                timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), new KeyValue(imageView.fitHeightProperty(), 40)));
                timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), new KeyValue(imageView.scaleXProperty(), 1)));
                timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), new KeyValue(imageView.scaleYProperty(), 1)));
            }
            else{
                node.setScaleX(0);
                node.setScaleY(0);
                timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), new KeyValue(node.scaleXProperty(), 1)));
                timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), new KeyValue(node.scaleYProperty(), 1)));
            }
        });

        timeline.play();

        //hamburgerMenu.setPrefWidth(backgroundImage.getFitWidth()/5);
        hamburgerMenu.setOpacity(0.9);
        //hamburgerMenu.setBackground(new Background(new BackgroundFill(Paint.valueOf("Black"),null,null)));
        //hamburgerIcon.setTranslateX(-hamburgerMenu.getPrefWidth());
    }

    private void createCity() {
/*
        //arkon
        CreateSingleCity(CityPosition.getX(currentSnapshot.getMap().getCity().get(0)),CityPosition.getY(currentSnapshot.getMap().getCity().get(0)),currentSnapshot.getMap().getCity().get(0));
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

        */
        clientController.getSnapshot().getMap().getCity().forEach(city1 -> {
            CreateSingleCity(CityPosition.getX(city1),CityPosition.getY(city1),city1);
                createBonus(city1.getBonus().getBonusURL(),city1.getBonus()
                        .getBonusInfo(),city1);
        });

        createKingImage(CityPosition.getX(clientController.getSnapshot().getKing().getCurrentCity()),CityPosition
                .getY(clientController.getSnapshot().getKing().getCurrentCity()));


    }

    private void createBonus(ArrayList<String> bonusURL, ArrayList<String> bonusInfo, City city1) {
        for(int i = 0; i< bonusURL.size();i++){
            ImageView imageView = new ImageView();
            System.out.println("bonus url "+bonusURL.get(i));
            imageView.setImage(new Image(bonusURL.get(i)));
            imageView.fitHeightProperty().bind(background.heightProperty().multiply(0.05));
            imageView.fitWidthProperty().bind(background.widthProperty().divide(30));
            imageView.getStyleClass().add(city1.getCityName().getCityName());
            Label singleBonusInfo = new Label(bonusInfo.get(i));
            singleBonusInfo.getStyleClass().add("bonusLabel");
            singleBonusInfo.setLabelFor(imageView);
            singleBonusInfo.setWrapText(true);
            DropShadow ds = new DropShadow(15, Color.BLACK);
            imageView.setEffect(ds);
            imageView.setPreserveRatio(true);
            background.getChildren().add(imageView);
            background.getChildren().add(singleBonusInfo);
            imageView.layoutXProperty().bind(background.widthProperty().multiply(CityPosition.getX(city1)).add(i*20));
            imageView.layoutYProperty().bind(background.heightProperty().multiply(CityPosition.getY(city1)));
            singleBonusInfo.layoutXProperty().bind(imageView.layoutXProperty().add(imageView.fitWidthProperty().divide(3)));
            singleBonusInfo.layoutYProperty().bind(imageView.layoutYProperty().add(imageView.fitHeightProperty().divide(3)));



            imageView.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    imageView.setScaleX(1.05);
                    imageView.setScaleY(1.05);
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

                    if(pulseBonus==null){

                    }
                    else {
                        pulseBonus.setValue(true);
                    }

                    System.out.println("click on bonus");

                    if(needToSelectOldBonus){
                        System.out.println("click on bonus");
                        new Thread(()->{
                            clientController.getCityRewardBonus(city1);
                        }).start();

                        needToSelectOldBonus=false;
                    }
                    //showPopoverOnCity(city,imageView);

                }
            });
        }

    }

    private void createKingImage(double x, double y) {
        kingImage = new ImageView();
        kingImage.setImage(new Image(Constants.IMAGE_PATH+"Crown.png"));
        kingImage.fitHeightProperty().bind(background.heightProperty().multiply(0.07));
        kingImage.fitWidthProperty().bind(background.widthProperty().divide(25));
        DropShadow ds = new DropShadow(15, Color.BLACK);
        kingImage.setEffect(ds);
        background.getChildren().add(kingImage);
        kingImage.layoutXProperty().bind(background.widthProperty().multiply(x));
        kingImage.layoutYProperty().bind(background.heightProperty().multiply(y).add(50));
        kingImage.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                kingImage.setScaleX(1.2);
                kingImage.setScaleY(1.2);
            }
        });
        kingImage.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                kingImage.setScaleY(1);
                kingImage.setScaleX(1);
            }
        });

        kingImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //showPopoverOnCity(city,imageView);
                showKingPopover(kingImage);
            }
        });

    }

    private void showKingPopover(ImageView imageView) {
        PopOver popOver = new PopOver();
        VBox vBox = new VBox();
        //KING
        HBox kingHBox = new HBox();
        ArrayList<Councilor> kingCouncilors = new ArrayList<>(currentSnapshot.getKing().getCouncil().getCouncil());
        for (Councilor councilor: kingCouncilors) {
            ImageView coucilorImage = new ImageView();
            try {
                coucilorImage.setImage(new Image(councilor.getColor().getImageUrl()));
                coucilorImage.fitWidthProperty().bind(popOver.prefWidthProperty().divide(10));
                coucilorImage.fitHeightProperty().bind(popOver.prefHeightProperty().divide(3));
                coucilorImage.setPreserveRatio(true);
            } catch (IllegalArgumentException e) {
            }
            kingHBox.getChildren().add(coucilorImage);
        }
        JFXCheckBox politicCardsCheckBox;
        VBox politicVBox = new VBox();
        for (PoliticCard politicCard : clientController.getSnapshot().getCurrentUser().getPoliticCards()) {
            politicCardsCheckBox = new JFXCheckBox();
            String stringa;
            if (politicCard.getPoliticColor() == null) {
                stringa = "MULTICOLOR";
            } else {
                stringa = politicCard.getPoliticColor().getColor();
            }
            politicCardsCheckBox.setText(stringa);
            politicCardsCheckBox.setId("JFXCheckBox");
            politicCardsCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if(newValue){
                        politicCardforBuildWithKing.add(politicCard);
                    }
                    else{
                        politicCardforBuildWithKing.remove(politicCard);
                    }
                }
            });
            politicVBox.getChildren().add(politicCardsCheckBox);
        }
        politicVBox.setSpacing(10);
        JFXButton jfxButton = new JFXButton();
        jfxButton.setButtonType(JFXButton.ButtonType.FLAT);
        jfxButton.setBackground(new Background(new BackgroundFill(Paint.valueOf("BLUE"),null,null)));
        jfxButton.setText("OK MAN");
        jfxButton.setTextFill(Paint.valueOf("WHITE"));
        jfxButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!buildWithKingPhase.getValue()) {
                    buildWithKingPhase.setValue(true);
                    startBuildWithKing();
                }
            }
        });

        vBox.getChildren().addAll(kingHBox,politicVBox,jfxButton);
        vBox.setSpacing(20);
        vBox.setPadding(new Insets(20,20,20,20));
        kingHBox.setAlignment(Pos.CENTER);
        popOver.setContentNode(vBox);
        popOver.show(imageView);

    }

    private void startBuildWithKing() {
        Set<Node> cities = background.lookupAll(".cityImage");
        kingPathforBuild.clear();
       // kingPathforBuild.add(clientController.getSnapshot().getKing().getCurrentCity());
        for(Node node : cities){
            Graphics.scaleTransitionEffectCycle(node,1.05f,1.05f,pulseCity);
        }
    }

    private void CreateSingleCity(double layoutX, double layoutY, City city) {
        //Circle castrum = new Circle();
        ImageView imageView = new ImageView();
        imageView.getStyleClass().add("cityImage");

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
                imageView.setScaleX(1.05);
                imageView.setScaleY(1.05);
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
                if(!buildWithKingPhase.get()) {
                    showPopoverOnCity(city, imageView);
                }
                else {
                    highlightCity(imageView,city);
                }
            }
        });
    }

    private void highlightCity(ImageView imageView, City city) {
        if(imageView.getEffect()==null) {
            int depth = 70; //Setting the uniform variable for the glow width and height
            DropShadow borderGlow = new DropShadow();
            borderGlow.setOffsetY(0f);
            borderGlow.setOffsetX(0f);
            borderGlow.setColor(Color.YELLOW);
            borderGlow.setWidth(depth);
            borderGlow.setHeight(depth);
            imageView.setEffect(borderGlow);
            kingPathforBuild.add(city);
        }
        else {
            kingPathforBuild.remove(city);
            imageView.setEffect(null);
        }
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

        if(CityPosition.getX(city)>0.5){
            if(CityPosition.getY(city)>0.5) {
                popOver.setArrowLocation(PopOver.ArrowLocation.RIGHT_BOTTOM);
            }
            else{
                popOver.setArrowLocation(PopOver.ArrowLocation.RIGHT_TOP);
            }
        }
        else{
            if(CityPosition.getY(city)>0.5) {
                popOver.setArrowLocation(PopOver.ArrowLocation.LEFT_BOTTOM);
            }
            else{
                popOver.setArrowLocation(PopOver.ArrowLocation.LEFT_TOP);
            }
        }
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
        shopController.setClientController(clientController,guiView);
        nobilityPathController.setClientController(clientController, guiView);
        nobilityPathController.setMatchController(this);
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
        //hiddenSidesPane = new HiddenSidesPane();
        VBox vbox= new VBox();
        HBox hbox1 = new HBox();
        HBox hbox2 = new HBox();
        for(RegionName regionName: RegionName.values()) {
            HBox hBox = new HBox();
            try {
                ArrayList<Councilor> councilors = currentSnapshot.getCouncil(regionName);
                ArrayList<ImageView> imageViews = new ArrayList<>();
                for (int i = 0; i< councilors.size();i++){
                    ImageView imageView = new ImageView();
                    try {
                        imageView.setImage(new Image(councilors.get(i).getColor().getImageUrl()));
                        imageView.fitWidthProperty().bind(background.prefWidthProperty().divide(10));
                        imageView.fitHeightProperty().bind(vbox.prefHeightProperty().divide(3));
                        imageView.setPreserveRatio(true);
                        imageViews.add(imageView);
                    }
                    catch (IllegalArgumentException e){
                        System.out.println("not found image in match controller"+councilors.get(i).getColor().getImageUrl());
                    }
                    hBox.getChildren().add(imageView);
                }
                councilHashMap.put(regionName,imageViews);
                hbox1.getChildren().add(hBox);
                //hBox.setPrefWidth(primaryScreenBounds.getWidth()/3);
                //hBox.prefWidthProperty().bind(hbox1.prefWidthProperty().divide(3));
                //hBox.setBackground(new Background(new BackgroundFill(Paint.valueOf("BLACK"),null,null),null,null));
                hBox.setOnMouseClicked(new CouncilorHandler(hBox,regionName,this,clientController,null));
            } catch (CouncilNotFoundException e) {
                e.printStackTrace();
            }
        }

        //hbox1.setPrefWidth(primaryScreenBounds.getWidth());
        hbox1.prefWidthProperty().bind(bottomPane.prefWidthProperty());
        hbox1.spacingProperty().bind(bottomPane.prefWidthProperty().divide(5));
        hbox1.setAlignment(Pos.CENTER);


        //KING
        HBox kingHBox = new HBox();
        ArrayList<Councilor> kingCouncilors = new ArrayList<>(currentSnapshot.getKing().getCouncil().getCouncil());
        for (Councilor councilor: kingCouncilors){
            ImageView imageView = new ImageView();
            try{
                imageView.setImage(new Image(councilor.getColor().getImageUrl()));
                imageView.fitWidthProperty().bind(background.prefWidthProperty().divide(10));
                imageView.fitHeightProperty().bind(vbox.prefHeightProperty().divide(3));
                imageView.setPreserveRatio(true);
                kingCouncil.add(imageView);
            }
            catch (IllegalArgumentException e){
                System.out.println("not found image match controller"+councilor.getColor().getImageUrl());
            }
            kingHBox.getChildren().add(imageView);
        }
        hbox2.getChildren().add(kingHBox);
        kingHBox.setAlignment(Pos.CENTER);
        kingHBox.setOnMouseClicked(new CouncilorHandler(hbox2,null,this,clientController,currentSnapshot.getKing()));
        //hbox2.setPrefWidth(primaryScreenBounds.getWidth());
        //hbox2.prefWidthProperty().bind(bottomPane.prefWidthProperty());
        hbox2.setAlignment(Pos.CENTER);


        // Permit Card
        HBox permitHBox= new HBox();

        for (RegionName regionName :RegionName.values()) {
            HBox hboxTmp = new HBox();
            hboxTmp.getStyleClass().add(regionName.name());
            currentSnapshot.getVisibleRegionPermitCard(regionName).forEach(permitCard -> {

                GridPane gridPane = new GridPane();
                gridPane.prefWidthProperty().bind(bottomPane.prefWidthProperty().divide(15));
                gridPane.prefHeightProperty().bind(bottomPane.prefHeightProperty().divide(2));
                Label label = new Label(permitCard.getCityString());
                label.getStyleClass().add("permitLabel");
                label.setTextFill(Paint.valueOf("WHITE"));
                ImageView permitImageView = new ImageView(new Image(Constants.IMAGE_PATH+"PermitCard.png"));
                permitImageView.setPreserveRatio(true);
                gridPane.add(permitImageView,0,0);
                gridPane.add(label,0,0);
                label.setFont(Font.font(8));
                GridPane.setColumnSpan(label,3);
                GridPane.setHalignment(label,HPos.CENTER);
                GridPane.setValignment(label,VPos.CENTER);
                GridPane.setRowSpan(permitImageView,3);
                GridPane.setColumnSpan(permitImageView,3);
                permitImageView.getStyleClass().add("visiblePermitCard");
                gridPane.getStyleClass().add("gridPanePermitCard");
                permitImageView.fitWidthProperty().bind(gridPane.prefWidthProperty());
                permitImageView.fitHeightProperty().bind(gridPane.prefHeightProperty());
                ArrayList<ImageView> imageViewArrayList = new ArrayList<ImageView>();
                ArrayList<Label> labelArrayList = new ArrayList<Label>();
                for(int i = 0; i<3; i++){
                    Label label1 = new Label();
                    ImageView imageView = new ImageView();
                    imageView.getStyleClass().add("permitCardBonus");
                    gridPane.add(imageView,i,2);
                    gridPane.add(label1,i,2);
                    imageViewArrayList.add(imageView);
                    labelArrayList.add(label1);
                    imageView.setVisible(false);
                    label1.setTextFill(Paint.valueOf("WHITE"));
                    label1.getStyleClass().add("bonusInfoLabel");
                    label1.setWrapText(true);
                    imageView.fitWidthProperty().bind(permitImageView.fitWidthProperty().divide(3));
                    imageView.fitHeightProperty().bind(permitImageView.fitHeightProperty().divide(3));
                    imageView.setPreserveRatio(true);
                    GridPane.setHalignment(imageView,HPos.CENTER);
                    GridPane.setValignment(imageView,VPos.CENTER);
                    GridPane.setHalignment(label1,HPos.CENTER);
                    GridPane.setValignment(label1,VPos.CENTER);
                }
                // bonus

                for(int i = 0 ; i< permitCard.getBonus().getBonusURL().size();i++){
                    // style class for change image
                    ImageView imageViewBonus = imageViewArrayList.get(i);
                    imageViewBonus.setImage(new Image(permitCard.getBonus().getBonusURL().get(i)));
                    imageViewBonus.setVisible(true);
                    Label bonusInfo = labelArrayList.get(i);
                    bonusInfo.setText(permitCard.getBonus().getBonusInfo().get(i));
                    bonusInfo.setVisible(true);

                }

                gridPane.setOnMouseClicked(new PermitCardHandler(permitCard,this,clientController,needToSelectPermitCard));

                PermitPopOverHandler permitPopOverHandler =
                        new PermitPopOverHandler(clientController,regionName,currentSnapshot
                                .getVisibleRegionPermitCard(regionName)
                                .indexOf(permitCard),this);
                gridPane.setOnMouseEntered(permitPopOverHandler);
                gridPane.setOnMouseExited(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        permitPopOverHandler.hide();
                    }
                });
                hboxTmp.getChildren().add(gridPane);
            });

            permitHBox.getChildren().addAll(hboxTmp);

        }

        permitHBox.spacingProperty().bind(background.prefWidthProperty().divide(6));
        permitHBox.setAlignment(Pos.CENTER);
        permitHBox.prefHeightProperty().bind(vbox.prefHeightProperty().divide(3));
        permitHBox.prefWidthProperty().bind(vbox.prefWidthProperty());
        hbox2.setAlignment(Pos.CENTER);
        //SideNode sideNode = new SideNode(10.0, Side.BOTTOM,hiddenSidesPane,hbox1,hbox2,permitHBox);
        //sideNode.setStyle("-fx-background-color: rgba(143,147,147,.25);");
        vbox.getChildren().addAll(hbox2,hbox1,permitHBox);
        vbox.spacingProperty().bind(bottomPane.prefHeightProperty().divide(8));
        StackPane.setMargin(vbox,new Insets(20,0,20,20));


        bottomPane.getChildren().clear();

        bottomPane.getChildren().add(vbox);
        vbox.prefWidthProperty().bind(bottomPane.prefWidthProperty());
        vbox.prefHeightProperty().bind(bottomPane.prefHeightProperty());
        bottomPane.prefHeightProperty().bind(background.prefHeightProperty().divide(5));
        bottomPane.prefWidthProperty().bind(background.prefWidthProperty());
        bottomPane.setOpacity(0.8);

    }



    public void setMyTurn(boolean value, SnapshotToSend snapshot) {

        myTurn = value;
        this.currentSnapshot = snapshot;
        turnFinished(myTurn);
        updateView();
    }

    @Override
    public void onStartMarket() {
        selectionModel.selectNext();
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

    @Override
    public void selectPermitCard() {
        needToSelectPermitCard.setValue(true);
        stopPulsePermitCard.setValue(false);
        Set<Node> nodes =bottomPane.lookupAll(".visiblePermitCard");
        nodes.forEach(this::highlightPermitCard);
        bottomPane.setVisible(true);

    }

    private void highlightPermitCard(Node node) {
        int depth = 70; //Setting the uniform variable for the glow width and height

        DropShadow borderGlow= new DropShadow();
        borderGlow.setOffsetY(0f);
        borderGlow.setOffsetX(0f);
        borderGlow.setColor(Color.YELLOW);
        borderGlow.setWidth(depth);
        borderGlow.setHeight(depth);
        node.setEffect(borderGlow);
        Graphics.scaleTransitionEffectCycle(node,1.1f,1.1f,stopPulsePermitCard);
    }

    @Override
    public void selectCityRewardBonus() {
        System.out.println("Select city reward bonus");
        needToSelectOldBonus=true;
        clientController.getSnapshot().getCurrentUser().getUsersEmporium().forEach(this::pulseBonus);
    }

    @Override
    public void moveKing(ArrayList<City> kingPath) {
        if(kingPath.size()>1) {
            Timeline timeline = new Timeline();
            kingImage.layoutXProperty().unbind();
            kingImage.layoutYProperty().unbind();
            // remove current king city
            kingPath.remove(0);

            kingPath.forEach(city1 -> {
                //path.getElements().add(new MoveTo(background.widthProperty().get()*CityPosition.getX(city1),background.heightProperty().get()*CityPosition.getY(city1)));
                KeyValue keyValueX = new KeyValue(kingImage.layoutXProperty(), background.getWidth() * CityPosition.getX(city1), Interpolator.EASE_BOTH);
                KeyValue keyValueY = new KeyValue(kingImage.layoutYProperty(), background.getHeight() * CityPosition.getY(city1), Interpolator.EASE_BOTH);
                KeyFrame keyFrame = new KeyFrame(Duration.millis(500 * kingPath.indexOf(city1)), keyValueX, keyValueY);
                timeline.getKeyFrames().add(keyFrame);
            });


            timeline.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    kingImage.layoutXProperty().bind(background.widthProperty().multiply(CityPosition.getX(kingPath.get(kingPath.size() - 1))));
                    kingImage.layoutYProperty().bind(background.heightProperty().multiply(CityPosition.getY(kingPath.get(kingPath.size() - 1))).add(50));
                }
            });

            timeline.play();
        }
        /*
        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(4000));
        pathTransition.setPath(path);
        pathTransition.setNode(kingImage);

        pathTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                kingImage.layoutXProperty().bind(background.widthProperty().multiply(CityPosition.getX(kingPath.get(kingPath.size()-1))));
                kingImage.layoutYProperty().bind(background.heightProperty().multiply(CityPosition.getY(kingPath.get(kingPath.size()-1))));
            }
        });


        pathTransition.play();
        */



    }

    private void pulseBonus(City city1) {
        if(pulseBonus==null) {
            pulseBonus = new SimpleBooleanProperty(false);
        }
        else {
            pulseBonus.setValue(false);
        }
        Set<Node> nodes =background.lookupAll("."+city1.getCityName().getCityName());
        nodes.forEach(node -> Graphics.scaleTransitionEffectCycle(node,1.2f,1.2f,pulseBonus));
    }

    private void turnFinished(boolean thisTurn) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if(thisTurn){
                    Graphics.notification("E' il tuo turno!");
                    turnText.setText("E' il tuo turno!");
                }
                else{
                    turnText.setText("Non è il tuo turno!");
                    Graphics.notification("Turno finito!");
                }

            }
        });

    }


    private void reprintCouncilor() {
        for (RegionName regionName : RegionName.values()) {
            ArrayList<Councilor> councilors = null;
            try {
                councilors = clientController.getSnapshot().getCouncil(regionName);
                ArrayList<ImageView> imageView = councilHashMap.get(regionName);
                for(int i = 0; i< councilors.size();i++){
                    imageView.get(i).setImage(new Image(councilors.get(i).getColor().getImageUrl()));
                }
            } catch (CouncilNotFoundException e) {
                e.printStackTrace();
            }

        }

        ArrayList<Councilor> kingCouncilArray = new ArrayList<>(clientController.getSnapshot().getKing().getCouncil().getCouncil());
        for (int i = 0; i<kingCouncil.size();i++) {
            kingCouncil.get(i).setImage(new Image(kingCouncilArray.get(i).getColor().getImageUrl()));
        }

    }


    @Override
    public void updateView() {
        this.currentSnapshot = clientController.getSnapshot();
                nobilityPathText.setText(currentSnapshot.getCurrentUser().getNobilityPathPosition().getPosition()+"");
                richPathText.setText(currentSnapshot.getCurrentUser().getCoinPathPosition()+"");
                helperText.setText(currentSnapshot.getCurrentUser().getHelpers().size()+"");
                victoryPathText.setText(currentSnapshot.getCurrentUser().getVictoryPathPosition()+"");
                mainActionText.setText(currentSnapshot.getCurrentUser().getMainActionCounter()+"");
                fastActionText.setText(currentSnapshot.getCurrentUser().getFastActionCounter()+"");

                //reprintCouncilor();

        // reprint councilor and permit card
                reprintCouncilor();
                reprintPermitCard();

        // reprint user's info
        System.out.println("update view" +usersComboBox.getValue());
                populateField(clientController.getSnapshot().getUsersInGame().get(usersComboBox.getValue()));
        /*
                createPermitCard(coastHBox,clientController.getSnapshot().getVisiblePermitCards(),RegionName.COAST);
                createPermitCard(hillHBox,clientController.getSnapshot().getVisiblePermitCards(),RegionName.HILL);
                createPermitCard(mountainHBox,clientController.getSnapshot().getVisiblePermitCards(),RegionName.MOUNTAIN);
                */
        createHand();
    }

    private void reprintPermitCard() {

        int counter=0;

        for(RegionName regionName: RegionName.values()){
            Node region = bottomPane.lookup("."+regionName.name());
            Set<Node> labels = region.lookupAll(".permitLabel");
            Set<Node> gridPanes = region.lookupAll(".gridPanePermitCard");
            Set<Node> imageViews = region.lookupAll(".visiblePermitCard");


            ArrayList<Node> labelsList = new ArrayList<>();
            labelsList.addAll(labels);

            ArrayList<Node> gridPanesList = new ArrayList<>();
            gridPanesList.addAll(gridPanes);

            /*ArrayList<Node> imageViewsList = new ArrayList<>();
            imageViewsList.addAll(imageViews);
            */



            for(int i = 0; i< labelsList.size();i++){
                Set<Node> bonusImages = gridPanesList.get(i).lookupAll(".permitCardBonus");
                Set<Node> bonusLabels = gridPanesList.get(i).lookupAll(".bonusInfoLabel");

                ArrayList<Node> bonusInfoArray = new ArrayList<>();
                bonusInfoArray.addAll(bonusLabels);

                ArrayList<Node> imageViewBonusList = new ArrayList<>();
                imageViewBonusList.addAll(bonusImages);


                PermitCard permitCardTmp = clientController.getSnapshot().getVisibleRegionPermitCard(regionName).get(i);
                Label label =(Label) labelsList.get(i);
                label.setText(permitCardTmp.getCityString());
                GridPane gridPane = (GridPane) gridPanesList.get(i);
                gridPane.setOnMouseClicked(new PermitCardHandler(permitCardTmp,this,clientController,needToSelectPermitCard));
                PermitPopOverHandler permitPopOverHandler = new PermitPopOverHandler(clientController,regionName,i,this);
                gridPane.setOnMouseEntered(permitPopOverHandler);
                gridPane.setOnMouseExited(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        permitPopOverHandler.hide();
                    }
                });
                for(int j = 0; j< imageViewBonusList.size();j++){
                    Label bonusLabel = (Label) bonusInfoArray.get(j);
                    ImageView imageViewBonus = (ImageView) imageViewBonusList.get(j);
                    if(j<permitCardTmp.getBonus().getBonusURL().size()) {
                        imageViewBonus.setVisible(true);
                        imageViewBonus.setImage(new Image(permitCardTmp.getBonus().getBonusURL().get(j)));
                        bonusLabel.setText(permitCardTmp.getBonus().getBonusInfo().get(j));
                        bonusLabel.setVisible(true);
                    }
                    else {
                        imageViewBonus.setVisible(false);
                        bonusLabel.setVisible(false);
                    }

                }

            }


        }


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

    public void showMore(ActionEvent actionEvent) {
        if(bottomPane.isVisible()){
            bottomPane.setVisible(false);
        }
        else {
            bottomPane.setVisible(true);
        }
    }

    public void showLess (ActionEvent actionEvent) {
        if(nobilityPath.isVisible()){
            handHBox.setVisible(true);
            nobilityPath.setVisible(false);

        }
        else {
            handHBox.setVisible(false);
            nobilityPath.setVisible(true);
        }
    }

    public void onSelectPermitCard(PermitCard permitCard) {
        hidePermitCardHightLight();
        needToSelectPermitCard.setValue(false);
        stopPulsePermitCard.setValue(true);
        clientController.onSelectPermitCard(permitCard);
    }

    private void hidePermitCardHightLight() {
        Set<Node> nodes =bottomPane.lookupAll(".visiblePermitCard");
        nodes.forEach(node -> node.setEffect(null));
    }

    public void finishKingPhase() {
        Set<Node> nodes = background.lookupAll(".cityImage");
        nodes.forEach(node->{
            node.setEffect(null);
        });
        politicCardforBuildWithKing.clear();
        kingPathforBuild.clear();
        buildWithKingPhase.set(false);
    }

    /*public void help(){
        RotateTransition rotateTransition = new RotateTransition(Duration.millis(3000), helpButton);
        helpButton.prefWidthProperty().bind(gridPane.prefWidthProperty().divide(10));
        helpButton.prefHeightProperty().bind(gridPane.prefHeightProperty().divide(10));
        helpButton.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                helpButton.setScaleX(1.2);
                helpButton.setScaleY(1.2);
            }
        });
        helpButton.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                rotateTransition.stop();
                helpButton.setScaleX(1);
                helpButton.setScaleY(1);
            }
        });
        helpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
                selectionModel.selectNext();
                shopController.displayHelp();
            }
        });
        helpButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                rotateTransition.setFromAngle(helpButton.getRotate());
                rotateTransition.setByAngle(helpButton.getRotate()+360);
                rotateTransition.setCycleCount(Animation.INDEFINITE);
                rotateTransition.play();
            }
        });
    }
    */

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

    public Pane getBackground() {
        return background;
    }

    public GridPane getGridPane() {
        return gridPane;
    }

    public ImageView getBackgroundImage() {
        return backgroundImage;
    }

    public boolean getBuildWithKingPhase() {
        return buildWithKingPhase.get();
    }

    public BooleanProperty buildWithKingPhaseProperty() {
        return buildWithKingPhase;
    }

    public boolean isMyTurn() {
        return myTurn;
    }

    public ArrayList<City> getKingPathforBuild() {
        return kingPathforBuild;
    }

    public ArrayList<PoliticCard> getPoliticCardforBuildWithKing() {
        return politicCardforBuildWithKing;
    }

    public void hideNodeList(){
        jfxNodesList.setVisible(false);
    }

    public void showNodeList(){
        jfxNodesList.setVisible(true);
    }
}
