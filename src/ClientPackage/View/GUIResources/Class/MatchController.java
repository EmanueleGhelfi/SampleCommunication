package ClientPackage.View.GUIResources.Class;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.GUIResources.customComponent.*;
import ClientPackage.View.GeneralView.GUIView;
import CommonModel.GameModel.Action.*;
import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import CommonModel.GameModel.City.*;
import CommonModel.GameModel.Council.Councilor;
import CommonModel.Snapshot.BaseUser;
import CommonModel.Snapshot.SnapshotToSend;
import Server.Model.User;
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
import javafx.scene.effect.ImageInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;


import java.awt.geom.AffineTransform;
import java.net.URL;
import java.util.*;

import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
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

    @FXML private HBox infoHBox;

    @FXML private Button helpButton;

    @FXML private JFXButton moreImg;
    @FXML private JFXButton lessImg;

    @FXML private HBox menuAllButtons;

    @FXML private ImageView imageInfo1;
    @FXML private ImageView imageInfo2;
    @FXML private ImageView imageInfo3;
    @FXML private ImageView imageInfo4;
    @FXML private ImageView imageInfo5;
    @FXML private ImageView imageInfo6;

    private SingleSelectionModel<Tab> selectionModel;

    private ImageView turnImage = new ImageView();
    private HBox handHBox = new HBox();

    //Images
    @FXML private ImageView kingImage;

    private JFXComboBox<String> usersComboBox;

    private BooleanProperty pulseBonus;
    private BooleanProperty stopPulsePermitCard = new SimpleBooleanProperty(false);
    private BooleanProperty stopPulseOldPermitCard = new SimpleBooleanProperty(false);

    //Phase
    private boolean needToSelectOldBonus = false;
    private BooleanProperty needToSelectPermitCard = new SimpleBooleanProperty(false);
    private BooleanProperty needToSelectOldPermitCard = new SimpleBooleanProperty(false);


    private HashMap<RegionName,ArrayList<ImageView>> councilHashMap = new HashMap<>();
    private ArrayList<ImageView> kingCouncil = new ArrayList<>();

    private ArrayList<PoliticCard> politicCardforBuildWithKing = new ArrayList<>();
    private ArrayList<City> kingPathforBuild = new ArrayList<>();

    private BooleanProperty buildWithKingPhase = new SimpleBooleanProperty(false);
    private BooleanProperty pulseCity = new SimpleBooleanProperty(false);

    //node list for more and other button
    private JFXNodesList moreActionNodeList = new JFXNodesList();
    // node list for old permit card
    private JFXNodesList oldPermitCardNodeList = new JFXNodesList();

    private ImageView boardImageView;

    private ProgressIndicator progressIndicator = new ProgressIndicator();

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
        backgroundImage.setImage(ImageLoader.getInstance().getImage(currentSnapshot.getMap().getRealMap()));
        backgroundImage.setPreserveRatio(true);
        backgroundImage.setCache(true);
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
        creteOldPermitCard();
        gridPane.add(oldPermitCardNodeList,2,2);
        GridPane.setHalignment(oldPermitCardNodeList, HPos.RIGHT);
        GridPane.setValignment(oldPermitCardNodeList, VPos.BOTTOM);
        GridPane.setMargin(oldPermitCardNodeList, new Insets(0, 20, 20, 0));
        ///oldPermitCardNodeList.visibleProperty().bind(nobilityPath.visibleProperty().not().and(hamburgerMenu.visibleProperty().not()));

        hamburgerMenu.prefWidthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(newValue.doubleValue()>0){
                    oldPermitCardNodeList.setVisible(false);
                }
                else{
                    oldPermitCardNodeList.setVisible(true);
                }
            }
        });
        //help();

        selectionModel = tabPane.getSelectionModel();
        tabPane.setStyle("-fx-background-image: url('/ClientPackage/View/GUIResources/Image/TabPaneMatchPattern.png')");

        populateHamburgerMenu();
        initHamburgerIcon();
        bottomPane.setVisible(false);
        nobilityPath.setVisible(false);
        kingPathforBuild.add(clientController.getSnapshot().getKing().getCurrentCity());
        createNodeList();
        setBoard();
        GridPane.setHalignment(nobilityPath, HPos.CENTER);
        GridPane.setValignment(nobilityPath, VPos.BOTTOM);
        createHBoxMenu();
        createLayers();

        /*
        Gauge gauge = new Gauge();
        gauge.setSkin(GaugeDesign);
        gauge.setPrefWidth(500);
        gauge.setPrefHeight(500);
        gauge.setStartFromZero(true);
        background.getChildren().add(gauge);
        */
    }

    private void createHBoxMenu() {

        ImageView moreImageView = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "/CouncilorButton.png"));
        ImageView lessImageView = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "/PathButton.png"));
        moreImageView.setFitWidth(60);
        moreImageView.setFitHeight(40);
        lessImageView.setFitWidth(60);
        lessImageView.setFitHeight(40);
        moreImg.setGraphic(moreImageView);
        lessImg.setGraphic(lessImageView);
        moreImg.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showMore();
            }
        });
        lessImg.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showLess();
            }
        });
    }

    private void createLayers() {
        boardImageView.toFront();
        infoHBox.toFront();
        turnImage.toFront();
        bottomPane.toFront();
        nobilityPath.toFront();
        moreActionNodeList.toFront();
        hamburgerMenu.toFront();
        hamburgerIcon.toFront();
        moreImg.toFront();
        lessImg.toFront();
        menuAllButtons.toFront();
    }

    private void setBoard() {
        boardImageView = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "/Board.png"));
        boardImageView.setOpacity(0.2);
        boardImageView.fitWidthProperty().bind(gridPane.widthProperty());
        boardImageView.setFitHeight(60);
        //boardImageView.fitHeightProperty().bind(background.heightProperty().divide(13));
        gridPane.add(boardImageView, 0, 0);
        GridPane.setValignment(boardImageView, VPos.TOP);
        GridPane.setColumnSpan(boardImageView, 3);
        //boardImageView.toBack();
        GridPane.setValignment(infoHBox, VPos.TOP);
        infoHBox.prefHeightProperty().bind(boardImageView.fitHeightProperty());
        infoHBox.prefWidthProperty().bind(gridPane.widthProperty().divide(2));
        //setImageInInfo();
        gridPane.add(turnImage, 0, 0);
        GridPane.setMargin(turnImage, new Insets(20, 0, 0, 20));
        turnImage.fitHeightProperty().bind(boardImageView.fitHeightProperty().multiply(2));
        turnImage.setPreserveRatio(true);
        turnImage.setStyle("-fx-effect: dropshadow(three-pass-box, black, 20, 0, 0, 0)");
        GridPane.setValignment(turnImage, VPos.TOP);
        GridPane.setHalignment(turnImage, HPos.CENTER);
    }

    private void setImageInInfo() {
        imageInfo1.setFitHeight(50);
        imageInfo2.setFitHeight(50);
        imageInfo3.setFitHeight(50);
        imageInfo4.setFitHeight(50);
        imageInfo5.setFitHeight(50);
        imageInfo6.setFitHeight(50);
        imageInfo1.setPreserveRatio(true);
        imageInfo2.setPreserveRatio(true);
        imageInfo3.setPreserveRatio(true);
        imageInfo4.setPreserveRatio(true);
        imageInfo5.setPreserveRatio(true);
        imageInfo6.setPreserveRatio(true);
    }

    private void creteOldPermitCard() {
        oldPermitCardNodeList.getChildren().clear();
        ImageView bagImage = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH+"bag.png"));
        bagImage.setCache(true);
        bagImage.setFitHeight(50);
        bagImage.setFitWidth(50);
        JFXButton bagButton = new JFXButton();
        bagButton.setGraphic(bagImage);
        bagButton.setPrefWidth(50);
        bagButton.setPrefHeight(50);
        bagButton.setButtonType(JFXButton.ButtonType.RAISED);
        bagButton.setBackground(new Background(new BackgroundFill(null,new CornerRadii(40),null)));
        bagButton.setTooltip(new Tooltip("Show old permit card"));
        bagButton.setRotate(180);
        oldPermitCardNodeList.addAnimatedNode(bagButton, (expanded) -> new ArrayList<KeyValue>(){{ add(
                new KeyValue(bagButton.rotateProperty(), expanded? 540:180 ,
                        Interpolator.EASE_BOTH));}});

        for(PermitCard permitCard: clientController.getSnapshot().getCurrentUser().getOldPermitCards()){
            StackPane permitStackPane = new StackPane();
            ImageView permitImage = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH+"PermitCard.png"));
            permitImage.setCache(true);
            permitImage.setFitHeight(80);
            permitImage.setFitWidth(80);
            Label infoLabel= new Label(permitCard.getInfo());
            infoLabel.setTextFill(Paint.valueOf("WHITE"));
            permitStackPane.getChildren().addAll(permitImage,infoLabel);
            StackPane.setAlignment(infoLabel,Pos.TOP_CENTER);
            createPermitCardBonusInStackPane(permitCard,permitStackPane,permitImage);
            permitStackPane.setRotate(180);
            permitImage.getStyleClass().add("myPermitCard");
            permitStackPane.setOnMouseClicked(new OldPermitCardHandler(permitCard,needToSelectOldPermitCard,clientController,this));
            oldPermitCardNodeList.addAnimatedNode(permitStackPane);
        }

        oldPermitCardNodeList.setRotate(180);

    }

    private void createPermitCardBonusInGridPane(PermitCard permitCard, GridPane permitGridPane,ImageView permitImage) {
        for (int i = 0; i < permitCard.getBonus().getBonusURL().size(); i++) {
            ImageView bonusImage = new ImageView(ImageLoader.getInstance().getImage(permitCard.getBonus().getBonusURL().get(i)));
            bonusImage.fitHeightProperty().bind(permitImage.fitHeightProperty().divide(4));
            bonusImage.setPreserveRatio(true);
            Label bonusLabel = new Label(permitCard.getBonus().getBonusInfo().get(i));
            bonusLabel.setTextFill(Paint.valueOf("WHITE"));
            permitGridPane.add(bonusImage, i+1, 1);
            permitGridPane.add(bonusLabel, i+1, 1);
            GridPane.setHalignment(bonusImage, HPos.CENTER);
            GridPane.setValignment(bonusImage, VPos.CENTER);
            GridPane.setHalignment(bonusLabel, HPos.CENTER);
            GridPane.setValignment(bonusLabel, VPos.CENTER);
            //permitGridPane.setAlignment(Pos.CENTER);
            /*
            Insets insets;
            switch (i) {
                case 0:
                    insets = new Insets(0, 0, 0, 10);
                    break;
                case 1:
                    insets = new Insets(0);
                    break;
                case 2:
                    insets = new Insets(0, 10, 0, 0);
                    break;
                default:
                    insets = new Insets(0);
            }
            */
            //GridPane.setMargin(bonusLabel, insets);
        }
    }


    private void createPermitCardBonusInStackPane(PermitCard permitCard, StackPane permitStackPane,ImageView permitImage) {
        for (int i = 0; i < permitCard.getBonus().getBonusURL().size(); i++) {
            ImageView bonusImage = new ImageView(ImageLoader.getInstance().getImage(permitCard.getBonus().getBonusURL().get(i)));
            bonusImage.fitHeightProperty().bind(permitImage.fitHeightProperty().divide(4));
            bonusImage.setPreserveRatio(true);
            Label bonusLabel = new Label(permitCard.getBonus().getBonusInfo().get(i));
            bonusLabel.setTextFill(Paint.valueOf("WHITE"));
            permitStackPane.getChildren().addAll(bonusImage, bonusLabel);
            Pos bonusPosition;
            Insets insets;
            switch (i) {
                case 0:
                    bonusPosition = Pos.CENTER_LEFT;
                    insets = new Insets(0, 0, 0, 5);
                    break;
                case 1:
                    bonusPosition = Pos.CENTER;
                    insets = new Insets(0);
                    break;
                case 2:
                    bonusPosition = Pos.CENTER_RIGHT;
                    insets = new Insets(0, 5, 0, 0);
                    break;
                default:
                    bonusPosition = Pos.CENTER;
                    insets = new Insets(0);
            }
            StackPane.setAlignment(bonusImage, bonusPosition);
            StackPane.setAlignment(bonusLabel, bonusPosition);
            StackPane.setMargin(bonusLabel, insets);
        }
    }


    private void createHand() {
        handHBox.getChildren().clear();
        handHBox.prefWidthProperty().bind(gridPane.prefWidthProperty());
        handHBox.prefHeightProperty().bind(gridPane.prefHeightProperty().divide(7));
        //handHBox.setMouseTransparent(true);
        handHBox.setAlignment(Pos.CENTER);
        for (PoliticCard politicCard : clientController.getSnapshot().getCurrentUser().getPoliticCards()) {
            ImageView politicCardImageView = new ImageView();
            politicCardImageView.setImage(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "/" + politicCard.getUrl() + ".png"));
            politicCardImageView.setCache(true);
            politicCardImageView.fitHeightProperty().bind(handHBox.prefHeightProperty());
            politicCardImageView.setPreserveRatio(true);
            handHBox.getChildren().add(politicCardImageView);
        }
        for (PermitCard permitCard : clientController.getSnapshot().getCurrentUser().getPermitCards()){
            GridPane permitGridPane = new GridPane();

            ColumnConstraints columnConstraints1 = new ColumnConstraints();
            ColumnConstraints columnConstraints2 = new ColumnConstraints();
            ColumnConstraints columnConstraints3 = new ColumnConstraints();
            ColumnConstraints columnConstraints4 = new ColumnConstraints();
            ColumnConstraints columnConstraints5 = new ColumnConstraints();
            columnConstraints1.setPercentWidth(5);
            columnConstraints2.setPercentWidth(30);
            columnConstraints3.setPercentWidth(30);
            columnConstraints4.setPercentWidth(30);
            columnConstraints5.setPercentWidth(5);
            permitGridPane.getColumnConstraints().addAll(columnConstraints1, columnConstraints2, columnConstraints3, columnConstraints4, columnConstraints5);
            gridPane.setAlignment(Pos.CENTER);
            ImageView permitCardImageView = new ImageView();
            permitCardImageView.setImage(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "/PermitCard.png"));
            permitCardImageView.fitHeightProperty().bind(handHBox.prefHeightProperty());
            permitCardImageView.setPreserveRatio(true);
            permitCardImageView.setCache(true);
            permitCardImageView.setCache(true);
            Label labelOfPermitCard = new Label();
            labelOfPermitCard.setText(permitCard.getCityString());
            labelOfPermitCard.setTextFill(Paint.valueOf("WHITE"));
            permitGridPane.add(labelOfPermitCard, 0, 0);
            permitGridPane.add(permitCardImageView, 0, 0);
            GridPane.setColumnSpan(labelOfPermitCard, 5);
            GridPane.setColumnSpan(permitCardImageView, 5);
            GridPane.setRowSpan(permitCardImageView, 2);
            GridPane.setValignment(labelOfPermitCard, VPos.BOTTOM);
            GridPane.setHalignment(labelOfPermitCard, HPos.CENTER);
            permitGridPane.setGridLinesVisible(true);
            createPermitCardBonusInGridPane(permitCard,permitGridPane,permitCardImageView);
            permitCardImageView.getStyleClass().add("myPermitCard");
            permitGridPane.setOnMouseClicked(new OldPermitCardHandler(permitCard,needToSelectOldPermitCard,clientController, this));
            handHBox.getChildren().add(permitGridPane);

            /*
            StackPane permitStackPane = new StackPane();
            permitStackPane.setAlignment(Pos.CENTER);
            ImageView permitCardImageView = new ImageView();
            permitCardImageView.setImage(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "/PermitCard.png"));
            permitCardImageView.fitHeightProperty().bind(handHBox.prefHeightProperty());
            permitCardImageView.setPreserveRatio(true);
            permitCardImageView.setCache(true);
            Label labelOfPermitCard = new Label();
            labelOfPermitCard.setText(permitCard.getCityString());
            labelOfPermitCard.setTextFill(Paint.valueOf("WHITE"));
            permitStackPane.getChildren().addAll(permitCardImageView, labelOfPermitCard);
            StackPane.setAlignment(labelOfPermitCard,Pos.TOP_CENTER);
            createPermitCardBonusInStackPane(permitCard,permitStackPane,permitCardImageView);
            permitCardImageView.getStyleClass().add("myPermitCard");
            permitStackPane.setOnMouseClicked(new OldPermitCardHandler(permitCard,needToSelectOldPermitCard,clientController, this));
            handHBox.getChildren().add(permitStackPane);
            */
        }

        GridPane.setValignment(handHBox, VPos.BOTTOM);
        GridPane.setHalignment(handHBox, HPos.CENTER);
        GridPane.setHalignment(handHBox, HPos.CENTER);
        GridPane.setColumnSpan(handHBox, 3);
    }


    private void createNodeList() {


        // FINISH KING
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
        ImageView imageMore = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH+"plusMenu.png"));
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
        ImageView imageChange = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH+"change.png"));
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
        ImageView helpImage = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH+"QuestionMark.png"));
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
                tabPane.setStyle("-fx-background-image: url('/ClientPackage/View/GUIResources/Image/TabPaneShopPattern.png')");
                shopController.displayHelp();
            }
        });
        helpButton.setTooltip(new Tooltip("Help"));


        moreActionNodeList.setSpacing(10);
        gridPane.setMargin(moreActionNodeList, new Insets(0,0,20,20));
        moreActionNodeList.addAnimatedNode(showMore, (expanded) -> new ArrayList<KeyValue>(){{ add(
                new KeyValue(showMore.rotateProperty(), expanded? 225:180 ,
                Interpolator.EASE_BOTH));}});

        moreActionNodeList.addAnimatedNode(changeTurnAction);

        moreActionNodeList.setRotate(180);


        // REGION

        for(RegionName regionName : RegionName.values()){
            moreActionNodeList.addAnimatedNode(getChangePermitCardButton(regionName));
        }


        moreActionNodeList.addAnimatedNode(helpButton);
        JFXButton shopButton = new JFXButton();
        ImageView shopButtonImageView = new ImageView(new Image(Constants.IMAGE_PATH + "/ShopButton.png"));
        shopButtonImageView.setFitHeight(50);
        shopButtonImageView.setFitWidth(50);
        shopButton.setPrefWidth(50);
        shopButton.setPrefHeight(50);
        shopButton.setGraphic(shopButtonImageView);
        shopButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selectionModel.selectNext();
                tabPane.setStyle("-fx-background-image: url('/ClientPackage/View/GUIResources/Image/TabPaneShopPattern.png')");
            }
        });
        moreActionNodeList.addAnimatedNode(shopButton);
        shopButton.setTooltip(new Tooltip("Go to shop"));

        gridPane.add(moreActionNodeList,0,2);
        GridPane.setHalignment(moreActionNodeList,HPos.LEFT);
        GridPane.setValignment(moreActionNodeList,VPos.BOTTOM);
        moreActionNodeList.visibleProperty().bind(nobilityPath.visibleProperty().not());
    }

    private JFXButton getChangePermitCardButton(RegionName regionName) {
        JFXButton jfxButton = new JFXButton();
        jfxButton.setTooltip(new Tooltip("Change permit card of "+regionName));
        jfxButton.setPrefHeight(50);
        jfxButton.setPrefWidth(50);
        ImageView regionImage = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH+""+regionName+".png"));
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
            if(!baseUser.getUsername().equals(clientController.getSnapshot().getCurrentUser().getUsername()) && !baseUser.isFakeUser())
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
        //hamburgerIcon.toFront();
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
               // hamburgerMenu.setVisible(false);
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
            createBonus(city1.getBonus().getBonusURL(),city1.getBonus().getBonusInfo(),city1);
        });

        /*
        clientController.getSnapshot().getUsersInGame().forEach((s, baseUser) -> {
            baseUser.getUsersEmporium().
        });
        */

        createKingImage(CityPosition.getX(clientController.getSnapshot().getKing().getCurrentCity()),CityPosition
                .getY(clientController.getSnapshot().getKing().getCurrentCity()));


    }

    private void createBonus(ArrayList<String> bonusURL, ArrayList<String> bonusInfo, City city1) {
        for(int i = 0; i< bonusURL.size();i++){
            ImageView imageView = new ImageView();
            System.out.println("bonus url "+bonusURL.get(i));
            imageView.setImage(ImageLoader.getInstance().getImage(bonusURL.get(i)));
            imageView.setCache(true);
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
        kingImage.setImage(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH+"Crown.png"));
        kingImage.setCache(true);
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
        politicCardforBuildWithKing.clear();
        PopOver popOver = new PopOver();
        VBox vBox = new VBox();
        //KING
        HBox kingHBox = new HBox();
        ArrayList<Councilor> kingCouncilors = new ArrayList<>(currentSnapshot.getKing().getCouncil().getCouncil());
        for (Councilor councilor: kingCouncilors) {
            ImageView coucilorImage = new ImageView();
            try {
                coucilorImage.setImage(ImageLoader.getInstance().getImage(councilor.getColor().getImageUrl()));
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
                    popOver.hide();
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
            Image cityImage = ImageLoader.getInstance().getImage("/ClientPackage/View/GUIResources/Image/City/"+city.getColor().getColor().toLowerCase()+".png");
            imageView.setImage(cityImage);
            imageView.setCache(true);
        }
        catch (Exception e){
            imageView.setImage(ImageLoader.getInstance().getImage("/ClientPackage/View/GUIResources/Image/City/blue.png"));
        }
        imageView.fitHeightProperty().bind(background.heightProperty().multiply(0.17));
        //imageView.fitWidthProperty().bind(background.widthProperty().divide(9));
        imageView.fitWidthProperty().bind(background.widthProperty().divide(11));
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

        ImageView cityName = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH+"City/Names/"+city.
                getCityName().getCityName()+""+city.getColor().getColor()+".png"));

        cityName.setCache(true);
        background.getChildren().add(cityName);
        cityName.layoutXProperty().bind(imageView.layoutXProperty());
        cityName.layoutYProperty().bind(imageView.layoutYProperty());
        cityName.fitWidthProperty().bind(imageView.fitWidthProperty());
        cityName.setPreserveRatio(true);

        HBox emporiumHBox = new HBox();
        emporiumHBox.setId(city.getCityName().getCityName());
        emporiumHBox.setMouseTransparent(true);
        background.getChildren().add(emporiumHBox);
        emporiumHBox.layoutXProperty().bind(background.widthProperty().multiply(CityPosition.getX(city)));
        emporiumHBox.layoutYProperty().bind(background.heightProperty().multiply(CityPosition.getY(city)).add(imageView.fitHeightProperty()).subtract(30));
        emporiumHBox.toFront();
        for (Map.Entry<String, BaseUser> userHashMap: clientController.getSnapshot().getUsersInGame().entrySet()){
            ImageView imageToAdd = new ImageView();
            imageToAdd.setId(userHashMap.getKey());
            imageToAdd.setMouseTransparent(true);
            System.out.println("imageid" + userHashMap.getKey());
            System.out.println("User color "+userHashMap.getValue().getUserColor().getColor());
            imageToAdd.setImage(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "/Emporia/" + userHashMap.getValue().getUserColor().getColor() + ".png"));
            imageToAdd.fitHeightProperty().bind(imageView.fitHeightProperty().divide(6));
            imageToAdd.setPreserveRatio(true);
            imageToAdd.setVisible(false);
            imageToAdd.setCache(true);
            emporiumHBox.getChildren().add(imageToAdd);
        }
        Graphics.addShadow(cityName);

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

        VBox cityInfoVBox = new VBox();
        HBox buttonHbox = new HBox();
        final PermitCard[] permitCardSelected = {null};
        //TODO: show button and other stuff

        ImageView cityName = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH+"City/Names/"+city.
                getCityName().getCityName()+""+city.getColor().getColor()+".png"));

        cityName.setFitWidth(150);
        cityName.setPreserveRatio(true);

        JFXComboBox<String> jfxComboBox = new JFXComboBox<>();
        for (PermitCard permitCard :
                clientController.getSnapshot().getCurrentUser().getPermitCards()) {
            jfxComboBox.getItems().add(permitCard.getCityString());
        }

        jfxComboBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                System.out.println("Selection changed to: "+newValue);
                permitCardSelected[0] = clientController.getSnapshot().getCurrentUser().getPermitCards().get(newValue.intValue());
            }
        });
        jfxComboBox.setPromptText("Seleziona la carta permesso");

        JFXButton jfxButton = new JFXButton("Compra emporio!");
        jfxButton.setBackground(new Background(new BackgroundFill(Paint.valueOf("D73D00"),null,null)));
        jfxButton.setTextFill(Paint.valueOf("WHITE"));

        jfxButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Action action = new MainActionBuildWithPermitCard(city, permitCardSelected[0]);
                clientController.doAction(action);
            }
        });

        buttonHbox.getChildren().addAll(jfxComboBox,jfxButton);
        buttonHbox.setSpacing(10);


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

        cityInfoVBox.setPadding(new Insets(20,20,20,20));
        cityInfoVBox.setSpacing(20);
        cityInfoVBox.getChildren().addAll(cityName,buttonHbox);
        cityInfoVBox.setAlignment(Pos.CENTER);
        popOver.setContentNode(cityInfoVBox);
        popOver.setTitle(city.getCityName().getCityName());
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
                        imageView.setImage(ImageLoader.getInstance().getImage(councilors.get(i).getColor().getImageUrl()));
                        imageView.setCache(true);
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
                imageView.setImage(ImageLoader.getInstance().getImage(councilor.getColor().getImageUrl()));
                imageView.setCache(true);
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
                ImageView permitImageView = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH+"PermitCard.png"));
                permitImageView.setCache(true);
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
                    imageViewBonus.setImage(ImageLoader.getInstance().getImage(permitCard.getBonus().getBonusURL().get(i)));
                    imageViewBonus.setCache(true);
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
        disableAllEffect();
        updateView();
    }

    private void disableAllEffect() {
        if(pulseBonus!=null && pulseCity!=null && needToSelectPermitCard!=null) {
            pulseBonus.setValue(false);
            //pulseCity.setValue(false);
            needToSelectPermitCard.setValue(false);
            needToSelectOldPermitCard.setValue(false);
            stopPulseOldPermitCard.setValue(true);
            stopPulsePermitCard.setValue(true);

            onSelectOldPermitCard();

            hidePermitCardHightLight(".visiblePermitCard",bottomPane);
        }
    }

    @Override
    public void onStartMarket() {
        selectionModel.selectNext();
        tabPane.setStyle("-fx-background-image: url('/ClientPackage/View/GUIResources/Image/TabPaneShopPattern.png')");
    }

    @Override
    public void onStartBuyPhase() {

    }

    @Override
    public void onFinishMarket() {
        selectionModel.selectFirst();
        tabPane.setStyle("-fx-background-image: url('/ClientPackage/View/GUIResources/Image/TabPaneMatchPattern.png')");
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
        nodes.forEach(node -> highlightPermitCard(node,stopPulsePermitCard));
        bottomPane.setVisible(true);
    }

    private void highlightPermitCard(Node node,BooleanProperty stopEffect) {
        int depth = 70; //Setting the uniform variable for the glow width and height
        DropShadow borderGlow= new DropShadow();
        borderGlow.setOffsetY(0f);
        borderGlow.setOffsetX(0f);
        borderGlow.setColor(Color.YELLOW);
        borderGlow.setWidth(depth);
        borderGlow.setHeight(depth);
        node.setEffect(borderGlow);
        Graphics.scaleTransitionEffectCycle(node,1.1f,1.1f,stopEffect);
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

    @Override
    public void selectOldPermitCardBonus() {
        needToSelectOldPermitCard.setValue(true);
        stopPulsePermitCard.setValue(false);
        Set<Node> permitCards = gridPane.lookupAll(".myPermitCard");
        permitCards.forEach(node-> highlightPermitCard(node,stopPulseOldPermitCard));
        hamburgerMenu.setVisible(false);
        nobilityPath.setVisible(false);
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
                    turnImage.setImage(new Image(Constants.IMAGE_PATH + "/turnYes1.png"));
                }
                else{
                    Graphics.notification("Turno finito!");
                    turnImage.setImage(new Image(Constants.IMAGE_PATH + "/turnNo1.png"));
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

        //setVisibleEmporia
        setEmporiaVisibility();

        // reprint user's info
        System.out.println("update view" +usersComboBox.getValue());
                populateField(clientController.getSnapshot().getUsersInGame().get(usersComboBox.getValue()));
        /*
                createPermitCard(coastHBox,clientController.getSnapshot().getVisiblePermitCards(),RegionName.COAST);
                createPermitCard(hillHBox,clientController.getSnapshot().getVisiblePermitCards(),RegionName.HILL);
                createPermitCard(mountainHBox,clientController.getSnapshot().getVisiblePermitCards(),RegionName.MOUNTAIN);
                */
        createHand();
        creteOldPermitCard();
        if(needToSelectOldPermitCard.get() && myTurn)
            selectOldPermitCardBonus();

    }

    private void setEmporiaVisibility() {
        for (Map.Entry<String, BaseUser> user : clientController.getSnapshot().getUsersInGame().entrySet()) {
            for (City city : user.getValue().getUsersEmporium()) {
                if (user.getValue().getUsersEmporium() != null && city != null) {
                    HBox cityHBox = (HBox) background.lookup("#" + city.getCityName().getCityName());
                    ImageView userEmporium = (ImageView) cityHBox.lookup("#" + user.getKey());
                    userEmporium.setVisible(true);
                }
            }
        }
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
                        imageViewBonus.setImage(ImageLoader.getInstance().getImage(permitCardTmp.getBonus().getBonusURL().get(j)));
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

    public void showMore() {
        System.out.println("PREMUTO");
        if(bottomPane.isVisible()){
            bottomPane.setVisible(false);
        }
        else {
            bottomPane.setVisible(true);
        }
    }

    public void showLess () {
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
        hidePermitCardHightLight(".visiblePermitCard",bottomPane);
        needToSelectPermitCard.setValue(false);
        stopPulsePermitCard.setValue(true);
        clientController.onSelectPermitCard(permitCard);
    }

    private void hidePermitCardHightLight(String selector,Pane container) {
        Set<Node> nodes =container.lookupAll(selector);
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

    public void onSelectOldPermitCard() {
        needToSelectOldPermitCard.setValue(false);
        stopPulsePermitCard.setValue(true);
        stopPulseOldPermitCard.setValue(true);
        hidePermitCardHightLight(".myPermitCard",gridPane);
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
        moreActionNodeList.setVisible(false);
    }

    public void showNodeList(){
        moreActionNodeList.setVisible(true);
    }

    public SingleSelectionModel<Tab> getSelectionModel() {
        return selectionModel;
    }
}
