package ClientPackage.View.GUIResources.Class;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.GUIResources.CustomComponent.*;
import ClientPackage.View.GeneralView.GUIView;
import CommonModel.GameModel.Action.*;
import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import CommonModel.GameModel.City.City;
import CommonModel.GameModel.City.RegionName;
import CommonModel.GameModel.Council.Councilor;
import CommonModel.Snapshot.BaseUser;
import CommonModel.Snapshot.SnapshotToSend;
import Utilities.Class.Constants;
import Utilities.Class.Graphics;
import Utilities.Class.Translator;
import Utilities.Exception.ActionNotPossibleException;
import Utilities.Exception.CouncilNotFoundException;
import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.TickLabelOrientation;
import eu.hansolo.medusa.skins.FlatSkin;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.util.Duration;
import org.controlsfx.control.HiddenSidesPane;
import org.controlsfx.control.PopOver;

import java.util.*;

/**
 * Created by Giulio on 17/05/2016.
 */
public class MatchController implements BaseController {

    private ClientController clientController;
    private boolean myTurn;
    private SnapshotToSend currentSnapshot;
    private PopOver popOver = new PopOver();
    private Pane paneOfPopup = new Pane();
    private JFXComboBox<String> councilorColorToChoose = new JFXComboBox<>();
    private String city;
    private GUIView guiView;
    private HiddenSidesPane hiddenSidesPane;
    private ImageView turnImage = new ImageView();
    private HBox handHBox = new HBox();
    private JFXComboBox<String> usersComboBox;
    private BooleanProperty pulseBonus;
    private BooleanProperty stopPulsePermitCard = new SimpleBooleanProperty(false);
    private BooleanProperty stopPulseOldPermitCard = new SimpleBooleanProperty(false);
    private BooleanProperty stopPulseBonus = new SimpleBooleanProperty();
    private boolean needToSelectOldBonus = false;
    private BooleanProperty needToSelectPermitCard = new SimpleBooleanProperty(false);
    private BooleanProperty needToSelectOldPermitCard = new SimpleBooleanProperty(false);
    private HashMap<RegionName, ArrayList<ImageView>> councilHashMap = new HashMap<>();
    private ArrayList<ImageView> kingCouncil = new ArrayList<>();
    private ArrayList<PoliticCard> politicCardforBuildWithKing = new ArrayList<>();
    private ArrayList<City> kingPathforBuild = new ArrayList<>();
    private BooleanProperty buildWithKingPhase = new SimpleBooleanProperty(false);
    private BooleanProperty pulseCity = new SimpleBooleanProperty(false);
    private JFXNodesList moreActionNodeList = new JFXNodesList();
    private JFXNodesList oldPermitCardNodeList = new JFXNodesList();
    private ImageView boardImageView;
    private ProgressIndicator progressIndicator = new ProgressIndicator();
    private Gauge timerIndicator = new Gauge();
    private Timer timer = new Timer();
    private SingleSelectionModel<Tab> selectionModel;

    @FXML
    private ImageView imageTest;
    @FXML
    private JFXButton coastPermitButton;
    @FXML
    private JFXButton hillPermitButton;
    @FXML
    private JFXButton mountainPermitButton;
    @FXML
    private Pane background;
    @FXML
    private Label nobilityPathText;
    @FXML
    private Label richPathText;
    @FXML
    private Label helperText;
    @FXML
    private Label victoryPathText;
    @FXML
    private Label fastActionText;
    @FXML
    private Label mainActionText;
    @FXML
    private HBox coastHBox;
    @FXML
    private HBox hillHBox;
    @FXML
    private HBox mountainHBox;
    @FXML
    private StackPane bottomPane;
    @FXML
    private GridPane path;
    @FXML
    private GridPane shop;
    @FXML
    private Pane nobilityPath;
    @FXML
    private ShopController shopController;
    @FXML
    private NobilityPathController nobilityPathController;
    @FXML
    private ImageView backgroundImage;
    @FXML
    private GridPane gridPane;
    @FXML
    private TabPane tabPane;
    @FXML
    private JFXHamburger hamburgerIcon;
    @FXML
    private GridPane hamburgerMenu;
    @FXML
    private Label moneyLabel;
    @FXML
    private Label victoryLabel;
    @FXML
    private Label politicLabel;
    @FXML
    private Label helperLabel;
    @FXML
    private Label nobilityLabel;
    @FXML
    private JFXListView<String> permitListView;
    @FXML
    private HBox infoHBox;
    @FXML
    private ImageView userColorImageView;
    @FXML
    private JFXButton moreImg;
    @FXML
    private JFXButton lessImg;
    @FXML
    private HBox menuAllButtons;
    @FXML
    private ImageView imageInfo1;
    @FXML
    private ImageView imageInfo2;
    @FXML
    private ImageView imageInfo3;
    @FXML
    private ImageView imageInfo4;
    @FXML
    private ImageView imageInfo5;
    @FXML
    private ImageView imageInfo6;
    @FXML
    private ImageView userMoneyImageView;
    @FXML
    private ImageView userVictoryImageView;
    @FXML
    private ImageView userNobilityImageView;
    @FXML
    private ImageView userPoliticImageView;
    @FXML
    private ImageView userHelperImageView;
    @FXML
    private ImageView userPermitImageView;
    @FXML
    private ImageView kingImage;

    @Override
    public void setClientController(ClientController clientController, GUIView guiView) {
        this.clientController = clientController;
        this.guiView = guiView;
        hiddenSidesPane = new HiddenSidesPane();
        currentSnapshot = clientController.getSnapshot();
        guiView.registerBaseController(this);
        initController();
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
                background.setPrefWidth(newValue.getWidth());
                background.setPrefHeight(newValue.getHeight());
                // gridPane.setPrefSize(background.getPrefWidth(),background.getMaxHeight());

            }
        });
        backgroundImage.fitWidthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            }
        });
        createGauge();
        createOpponentTooltip();
        pulseCity.bind(buildWithKingPhase.not());
        createOverlay();
        initPermitButton();
        handleClick();
        createCity();
        gridPane.add(handHBox, 0, 1);
        createHand();
        creteOldPermitCard();
        gridPane.add(oldPermitCardNodeList, 2, 2);
        GridPane.setHalignment(oldPermitCardNodeList, HPos.RIGHT);
        GridPane.setValignment(oldPermitCardNodeList, VPos.BOTTOM);
        GridPane.setMargin(oldPermitCardNodeList, new Insets(0, 20, 20, 0));
        oldPermitCardNodeList.onContextMenuRequestedProperty().addListener(new ChangeListener<EventHandler<? super ContextMenuEvent>>() {
            @Override
            public void changed(ObservableValue<? extends EventHandler<? super ContextMenuEvent>> observable, EventHandler<? super ContextMenuEvent> oldValue, EventHandler<? super ContextMenuEvent> newValue) {
                Graphics.playSomeSound("PlusButton");
            }
        });
        hamburgerMenu.prefWidthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.doubleValue() > 0) {
                    oldPermitCardNodeList.setVisible(false);
                } else {
                    oldPermitCardNodeList.setVisible(true);
                }
            }
        });
        tabPane.setStyle("-fx-background-image: url('/ClientPackage/View/GUIResources/Image/TabPaneMatchPattern.png')");
        selectionModel = tabPane.getSelectionModel();
        initHamburgerIcon();
        bottomPane.setVisible(false);
        nobilityPath.setVisible(false);
        kingPathforBuild.add(clientController.getSnapshot().getKing().getCurrentCity());
        createNodeList();
        setBoard();
        populateHamburgerMenu();
        GridPane.setHalignment(nobilityPath, HPos.CENTER);
        GridPane.setValignment(nobilityPath, VPos.BOTTOM);
        createHBoxMenu();
        createLayers();
        placeRegionBonus();
    }

    private void createOpponentTooltip() {
        Tooltip.install(userColorImageView, new Tooltip("Colore"));
        Tooltip.install(userMoneyImageView, new Tooltip("Posizione sul percorso della ricchezza"));
        Tooltip.install(userVictoryImageView, new Tooltip("Posizione sul percorso della vittoria"));
        Tooltip.install(userNobilityImageView, new Tooltip("Posizione sul percorso della nobiltà"));
        Tooltip.install(userPoliticImageView, new Tooltip("Carte politiche"));
        Tooltip.install(userHelperImageView, new Tooltip("Aiutanti"));
        Tooltip.install(userPermitImageView, new Tooltip("Città su cui può edificare"));
    }

    private void placeRegionBonus() {
        ImageView coastImage = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "/CoastBonusCard.png", background.getWidth() * 0.89, background.getHeight() * 0.82));
        ImageView hillImage = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "/HillBonusCard.png", background.getWidth() * 0.89, background.getHeight() * 0.82));
        ImageView mountainImage = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "/MountainBonusCard.png", background.getWidth() * 0.89, background.getHeight() * 0.82));
        coastImage.setCache(true);
        hillImage.setCache(true);
        mountainImage.setCache(true);
        coastImage.layoutXProperty().bind(background.widthProperty().multiply(0.089));
        coastImage.layoutYProperty().bind(background.heightProperty().multiply(0.82));
        hillImage.layoutXProperty().bind(background.widthProperty().multiply(0.447));
        hillImage.layoutYProperty().bind(background.heightProperty().multiply(0.82));
        mountainImage.layoutXProperty().bind(background.widthProperty().multiply(0.80));
        mountainImage.layoutYProperty().bind(background.heightProperty().multiply(0.82));
        coastImage.fitWidthProperty().bind(background.widthProperty().divide(20));
        hillImage.fitWidthProperty().bind(background.widthProperty().divide(20));
        mountainImage.fitWidthProperty().bind(background.widthProperty().divide(20));
        coastImage.setPreserveRatio(true);
        hillImage.setPreserveRatio(true);
        mountainImage.setPreserveRatio(true);
        Graphics.addShadow(coastImage);
        Graphics.addShadow(hillImage);
        Graphics.addShadow(mountainImage);
        Graphics.bringUpImages(coastImage, hillImage, mountainImage);
        background.getChildren().addAll(coastImage, hillImage, mountainImage);
        Tooltip.install(coastImage, new Tooltip("Bonus della regione"));
        Tooltip.install(hillImage, new Tooltip("Bonus della regione"));
        Tooltip.install(mountainImage, new Tooltip("Bonus della regione"));
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
        Tooltip.install(moreImg, new Tooltip("Carte permesso e consiglieri"));
        Tooltip.install(lessImg, new Tooltip("Percorso della nobiltà"));
        moreImg.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Graphics.playSomeSound(Constants.BUTTON);
                showMore();
            }
        });
        lessImg.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Graphics.playSomeSound(Constants.BUTTON);
                showLess();
            }
        });
        menuAllButtons.getChildren().setAll(hamburgerIcon, moreImg, lessImg);
        menuAllButtons.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
    }

    private void createLayers() {
        boardImageView.toFront();
        infoHBox.toFront();
        timerIndicator.toFront();
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
        boardImageView = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "/Board.png", gridPane.getWidth(), 60));
        boardImageView.setOpacity(0.2);
        boardImageView.fitWidthProperty().bind(gridPane.widthProperty());
        boardImageView.setFitHeight(60);
        boardImageView.setMouseTransparent(true);
        gridPane.add(boardImageView, 0, 0);
        GridPane.setValignment(boardImageView, VPos.TOP);
        GridPane.setColumnSpan(boardImageView, 3);
        GridPane.setValignment(infoHBox, VPos.TOP);
        infoHBox.prefHeightProperty().bind(boardImageView.fitHeightProperty());
        infoHBox.prefWidthProperty().bind(gridPane.widthProperty().divide(2));
        setImageInInfo();
        gridPane.add(turnImage, 0, 0);
        turnImage.fitWidthProperty().bind(background.widthProperty().multiply(0.07));
        turnImage.setPreserveRatio(true);
        turnImage.setStyle("-fx-effect: dropshadow(three-pass-box, black, 20, 0, 0, 0)");
        GridPane.setValignment(turnImage, VPos.CENTER);
        GridPane.setHalignment(turnImage, HPos.CENTER);
    }

    private void createGauge() {
        timerIndicator = new Gauge();
        timerIndicator.setSkin(new FlatSkin(timerIndicator));
        timerIndicator.setTitle("Mancano ancora");
        timerIndicator.setUnit("Secondi");
        timerIndicator.setDecimals(0);
        timerIndicator.setValueColor(Color.WHITE);
        timerIndicator.setTitleColor(Color.WHITE);
        timerIndicator.setSubTitleColor(Color.WHITE);
        timerIndicator.setBarColor(Color.rgb(0, 214, 215));
        timerIndicator.setNeedleColor(Color.WHITE);
        timerIndicator.setThresholdColor(Color.rgb(204, 0, 0));
        timerIndicator.setTickLabelColor(Color.rgb(151, 151, 151));
        timerIndicator.setTickMarkColor(Color.BLACK);
        timerIndicator.setTickLabelOrientation(TickLabelOrientation.ORTHOGONAL);
        gridPane.add(timerIndicator, 0, 0);
        timerIndicator.setKeepAspect(true);
        GridPane.setHalignment(timerIndicator, HPos.CENTER);
        GridPane.setValignment(timerIndicator, VPos.CENTER);
        timerIndicator.prefWidthProperty().bind(background.widthProperty().multiply(0.1));
    }

    private void setImageInInfo() {
        Tooltip.install(imageInfo1, new Tooltip("Aiutanti"));
        Tooltip.install(imageInfo2, new Tooltip("Posizione sul percorso della ricchezza"));
        Tooltip.install(imageInfo3, new Tooltip("Posizione sul percorso della vittoria"));
        Tooltip.install(imageInfo4, new Tooltip("Posizione sul percorso della nobiltà"));
        Tooltip.install(imageInfo5, new Tooltip("Azioni veloci disponibili"));
        Tooltip.install(imageInfo6, new Tooltip("Azioni principali disponibili"));
    }

    private void creteOldPermitCard() {
        oldPermitCardNodeList.getChildren().clear();
        ImageView bagImage = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "bag.png", 50, 50));
        bagImage.setCache(true);
        bagImage.setFitHeight(50);
        bagImage.setFitWidth(50);
        JFXButton bagButton = new JFXButton();
        bagButton.setGraphic(bagImage);
        bagButton.setPrefWidth(50);
        bagButton.setPrefHeight(50);
        bagButton.setButtonType(JFXButton.ButtonType.RAISED);
        bagButton.setBackground(new Background(new BackgroundFill(null, new CornerRadii(40), null)));
        bagButton.setTooltip(new Tooltip("Vecchie carte permesso"));
        bagButton.setRotate(180);
        oldPermitCardNodeList.addAnimatedNode(bagButton, (expanded) -> new ArrayList<KeyValue>() {{
            add(
                    new KeyValue(bagButton.rotateProperty(), expanded ? 540 : 180,
                            Interpolator.EASE_BOTH));
        }});

        for (PermitCard permitCard : clientController.getSnapshot().getCurrentUser().getOldPermitCards()) {
            StackPane permitStackPane = new StackPane();
            ImageView permitImage = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "PermitCard.png", 80, 80));
            permitImage.setCache(true);
            permitImage.setFitHeight(80);
            permitImage.setFitWidth(80);
            Label infoLabel = new Label(permitCard.getInfo());
            infoLabel.setTextFill(Paint.valueOf("WHITE"));
            permitStackPane.getChildren().addAll(permitImage, infoLabel);
            StackPane.setAlignment(infoLabel, Pos.TOP_CENTER);
            createPermitCardBonusInStackPane(permitCard, permitStackPane, permitImage);
            permitStackPane.setRotate(180);
            permitImage.getStyleClass().add("myPermitCard");
            permitStackPane.setOnMouseClicked(new OldPermitCardHandler(permitCard, needToSelectOldPermitCard, clientController, this));
            oldPermitCardNodeList.addAnimatedNode(permitStackPane);
        }
        oldPermitCardNodeList.setRotate(180);
        for (Node node : oldPermitCardNodeList.getChildren()) {
            Graphics.addShadow(node);
        }

    }

    private void createPermitCardBonusInGridPane(PermitCard permitCard, GridPane permitGridPane, ImageView permitImage) {
        for (int i = 0; i < permitCard.getBonus().getBonusURL().size(); i++) {
            ImageView bonusImage = new ImageView(ImageLoader.getInstance().getImage(permitCard.getBonus().getBonusURL().get(i)));
            bonusImage.fitHeightProperty().bind(permitImage.fitHeightProperty().divide(4));
            bonusImage.setPreserveRatio(true);
            Label bonusLabel = new Label(permitCard.getBonus().getBonusInfo().get(i));
            bonusLabel.setTextFill(Paint.valueOf("WHITE"));
            permitGridPane.add(bonusImage, i + 1, 1);
            permitGridPane.add(bonusLabel, i + 1, 1);
            GridPane.setHalignment(bonusImage, HPos.CENTER);
            GridPane.setValignment(bonusImage, VPos.CENTER);
            GridPane.setHalignment(bonusLabel, HPos.CENTER);
            GridPane.setValignment(bonusLabel, VPos.CENTER);
        }
    }


    private void createPermitCardBonusInStackPane(PermitCard permitCard, StackPane permitStackPane, ImageView permitImage) {
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
        handHBox.setAlignment(Pos.BOTTOM_CENTER);
        for (PoliticCard politicCard : clientController.getSnapshot().getCurrentUser().getPoliticCards()) {
            ImageView politicCardImageView = new ImageView();
            politicCardImageView.setImage(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "/" + politicCard.getUrl() + ".png"));
            politicCardImageView.setCache(true);
            politicCardImageView.fitHeightProperty().bind(handHBox.prefHeightProperty());
            politicCardImageView.setPreserveRatio(true);
            handHBox.getChildren().add(politicCardImageView);
        }
        for (PermitCard permitCard : clientController.getSnapshot().getCurrentUser().getPermitCards()) {
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
            ImageView permitCardImageView = new ImageView();
            permitCardImageView.setImage(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "/PermitCard.png"));
            permitGridPane.prefHeightProperty().bind(handHBox.heightProperty());
            permitCardImageView.fitHeightProperty().bind(handHBox.prefHeightProperty());
            permitCardImageView.setPreserveRatio(true);
            permitCardImageView.setCache(true);
            permitCardImageView.setCache(true);
            Label labelOfPermitCard = new Label();
            labelOfPermitCard.setText(permitCard.getCityString());
            labelOfPermitCard.setTextFill(Paint.valueOf("WHITE"));
            permitGridPane.add(labelOfPermitCard, 0, 0);
            permitGridPane.add(permitCardImageView, 0, 0);
            GridPane.setColumnSpan(permitCardImageView, 5);
            GridPane.setRowSpan(permitCardImageView, 2);
            GridPane.setColumnSpan(labelOfPermitCard, 5);
            GridPane.setValignment(labelOfPermitCard, VPos.TOP);
            GridPane.setHalignment(labelOfPermitCard, HPos.CENTER);
            createPermitCardBonusInGridPane(permitCard, permitGridPane, permitCardImageView);
            permitCardImageView.getStyleClass().add("myPermitCard");
            permitGridPane.setOnMouseClicked(new OldPermitCardHandler(permitCard, needToSelectOldPermitCard, clientController, this));
            handHBox.getChildren().add(permitGridPane);
            GridPane.setMargin(labelOfPermitCard, new Insets(10, 0, 0, 0));
            labelOfPermitCard.toFront();
        }
        GridPane.setValignment(handHBox, VPos.BOTTOM);
        GridPane.setHalignment(handHBox, HPos.CENTER);
        GridPane.setColumnSpan(handHBox, 3);
        GridPane.setRowSpan(handHBox, 2);
    }


    private void createNodeList() {
        JFXButton finishKing = new JFXButton("FINISH");
        finishKing.setBackground(new Background(new BackgroundFill(Paint.valueOf("BLUE"), new CornerRadii(20), null)));
        finishKing.setButtonType(JFXButton.ButtonType.FLAT);
        finishKing.setTextFill(Paint.valueOf("WHITE"));
        finishKing.getStyleClass().add(Constants.STYLE_BUTTON);
        finishKing.setOnAction(new FinishKingMoveHandler(clientController, this));
        finishKing.disableProperty().bind(buildWithKingPhase.not());
        finishKing.setVisible(false);
        BooleanProperty animation = new SimpleBooleanProperty();
        buildWithKingPhase.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    finishKing.setVisible(true);
                    Graphics.scaleTransitionEffectCycle(finishKing, 1.2f, 1.2f, animation);
                    animation.setValue(false);
                } else {
                    finishKing.setVisible(false);
                    animation.setValue(true);
                }
            }
        });
        gridPane.add(finishKing, 2, 2);
        JFXButton showMore = new JFXButton();
        ImageView imageMore = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "plusMenu.png"));
        showMore.setGraphic(imageMore);
        showMore.setPrefHeight(50);
        showMore.setPrefWidth(50);
        imageMore.setFitHeight(30);
        imageMore.setFitWidth(30);
        showMore.setRotate(180);
        showMore.setButtonType(JFXButton.ButtonType.RAISED);
        showMore.setTooltip(new Tooltip("Altre azioni"));
        JFXButton changeTurnAction = new JFXButton();
        ImageView imageChange = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "change.png", 50, 50));
        imageChange.setFitHeight(50);
        imageChange.setFitWidth(50);
        changeTurnAction.setPrefHeight(50);
        changeTurnAction.setPrefWidth(50);
        changeTurnAction.setGraphic(imageChange);
        changeTurnAction.setButtonType(JFXButton.ButtonType.RAISED);
        changeTurnAction.setOnAction(new ChangeTurnHandler(this));
        showMore.setBackground(new Background(new BackgroundFill(Paint.valueOf("BLUE"), new CornerRadii(30), null)));
        changeTurnAction.setRotate(180);
        changeTurnAction.setTooltip(new Tooltip("Passa"));
        JFXButton helpButton = new JFXButton();
        ImageView helpImage = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "QuestionMark.png", 50, 50));
        helpImage.setFitHeight(50);
        helpImage.setFitWidth(50);
        helpImage.setFitHeight(50);
        helpImage.setFitWidth(50);
        helpButton.setPrefHeight(50);
        helpButton.setPrefWidth(50);
        helpButton.setGraphic(helpImage);
        helpButton.setRotate(180);
        helpButton.setBackground(new Background(new BackgroundFill(Paint.valueOf("WHITE"), new CornerRadii(30), null)));
        helpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Graphics.playSomeSound(Constants.BUTTON);
                SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
                selectionModel.selectNext();
                tabPane.setStyle("-fx-background-image: url('/ClientPackage/View/GUIResources/Image/TabPaneShopPattern.png')");
                shopController.displayHelp();
            }
        });
        helpButton.setTooltip(new Tooltip("Aiuto"));
        moreActionNodeList.setSpacing(10);
        GridPane.setMargin(moreActionNodeList, new Insets(0, 0, 20, 20));
        moreActionNodeList.addAnimatedNode(showMore, (expanded) -> new ArrayList<KeyValue>() {{
            add(
                    new KeyValue(showMore.rotateProperty(), expanded ? 225 : 180,
                            Interpolator.EASE_BOTH));
        }});
        moreActionNodeList.addAnimatedNode(changeTurnAction);
        moreActionNodeList.setRotate(180);
        for (RegionName regionName : RegionName.values()) {
            moreActionNodeList.addAnimatedNode(getChangePermitCardButton(regionName));
        }
        moreActionNodeList.addAnimatedNode(helpButton);
        JFXButton shopButton = new JFXButton();
        ImageView shopButtonImageView = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "/ShopButton.png", 50, 50));
        shopButtonImageView.setFitHeight(50);
        shopButtonImageView.setFitWidth(50);
        shopButton.setPrefWidth(50);
        shopButton.setPrefHeight(50);
        shopButton.setGraphic(shopButtonImageView);
        shopButton.setRotate(180);
        shopButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Graphics.playSomeSound(Constants.BUTTON);
                selectionModel.selectNext();
                tabPane.setStyle("-fx-background-image: url('/ClientPackage/View/GUIResources/Image/TabPaneShopPattern.png')");
            }
        });
        moreActionNodeList.addAnimatedNode(shopButton);
        shopButton.setTooltip(new Tooltip("Negozio"));
        gridPane.add(moreActionNodeList, 0, 2);
        GridPane.setHalignment(moreActionNodeList, HPos.LEFT);
        GridPane.setValignment(moreActionNodeList, VPos.BOTTOM);
        for (Node node : moreActionNodeList.getChildren())
            Graphics.addShadow(node);
        moreActionNodeList.visibleProperty().bind(nobilityPath.visibleProperty().not());
    }

    private JFXButton getChangePermitCardButton(RegionName regionName) {
        JFXButton jfxButton = new JFXButton();
        jfxButton.setTooltip(new Tooltip("Cambia le carte permesso della regione " + Translator.translatingToIta(regionName.getRegion())));
        jfxButton.setPrefHeight(50);
        jfxButton.setPrefWidth(50);
        ImageView regionImage = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "" + regionName + ".png"));
        jfxButton.setGraphic(regionImage);
        jfxButton.setButtonType(JFXButton.ButtonType.RAISED);
        jfxButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Graphics.playSomeSound(Constants.BUTTON);
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
            if (!baseUser.getUsername().equals(clientController.getSnapshot().getCurrentUser().getUsername()) && !baseUser.isFakeUser())
                usersComboBox.getItems().add(baseUser.getUsername());
        });
        hamburgerMenu.add(usersComboBox, 1, 0);
        GridPane.setValignment(usersComboBox, VPos.CENTER);
        GridPane.setHalignment(usersComboBox, HPos.CENTER);
        usersComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                populateField(clientController.getSnapshot().getUsersInGame().get(newValue));
            }
        });
        usersComboBox.getSelectionModel().select(0);
        hamburgerMenu.prefHeightProperty().bind(gridPane.heightProperty().subtract(boardImageView.fitHeightProperty()));
        GridPane.setValignment(hamburgerMenu, VPos.BOTTOM);
    }

    private void populateField(BaseUser baseUser) {
        moneyLabel.setText(baseUser.getCoinPathPosition() + "");
        politicLabel.setText(baseUser.getPoliticCardNumber() + "");
        helperLabel.setText(baseUser.getHelpers().size() + "");
        victoryLabel.setText(baseUser.getNobilityPathPosition().getPosition() + "");
        permitListView.getItems().clear();
        permitListView.setItems(FXCollections.observableArrayList(clientController.populateListView(baseUser.getUsername())));
        nobilityLabel.setText(baseUser.getVictoryPathPosition() + "");
        userColorImageView.setImage(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "/Emporia/" + clientController.getSnapshot().getUsersInGame().get(baseUser.getUsername()).getUserColor().getColor() + ".png", 41, 53));
    }

    private void initHamburgerIcon() {
        hamburgerMenu.setVisible(false);
        hamburgerMenu.setPrefWidth(0);
        HamburgerSlideCloseTransition burgerTask = new HamburgerSlideCloseTransition(hamburgerIcon);
        burgerTask.setRate(-1);
        Tooltip.install(hamburgerIcon, new Tooltip("Info degli altri giocatori"));
        hamburgerIcon.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
            burgerTask.setRate(burgerTask.getRate() * -1);
            if (burgerTask.getRate() == 1) {
                openSlider();
            } else {
                closeSlider();
            }
            Graphics.playSomeSound(Constants.BUTTON);
            burgerTask.play();
        });
    }

    private void closeSlider() {
        hamburgerIcon.setTranslateX(0);
        final Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1000),
                new KeyValue(hamburgerMenu.prefWidthProperty(), 0, Interpolator.EASE_OUT)));

        List<Node> nodes = hamburgerMenu.getChildren();
        nodes.forEach(node -> {
            if (node instanceof ImageView) {
                ImageView imageView = (ImageView) node;
                timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), new KeyValue(imageView.scaleXProperty(), 0)));
                timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), new KeyValue(imageView.scaleYProperty(), 0)));
            } else {
                timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), new KeyValue(node.scaleXProperty(), 0)));
                timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), new KeyValue(node.scaleYProperty(), 0)));
            }
        });
        timeline.play();
        timeline.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            }
        });
    }

    private void openSlider() {
        Graphics.playSomeSound("PlusButton");
        hamburgerMenu.setVisible(true);
        hamburgerMenu.setScaleX(1);
        hamburgerMenu.setPrefWidth(0);
        final Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500),
                new KeyValue(hamburgerMenu.prefWidthProperty(), backgroundImage.getFitWidth() / 5, Interpolator.EASE_OUT)));
        List<Node> nodes = hamburgerMenu.getChildren();
        nodes.forEach(node -> {
            if (node instanceof ImageView) {
                ImageView imageView = (ImageView) node;
                imageView.setScaleX(1);
                imageView.setScaleY(1);
                imageView.setFitHeight(0);
                imageView.setFitWidth(0);
                timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), new KeyValue(imageView.fitWidthProperty(), 40)));
                timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), new KeyValue(imageView.fitHeightProperty(), 40)));
                timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), new KeyValue(imageView.scaleXProperty(), 1)));
                timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), new KeyValue(imageView.scaleYProperty(), 1)));
            } else {
                node.setScaleX(0);
                node.setScaleY(0);
                timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), new KeyValue(node.scaleXProperty(), 1)));
                timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500), new KeyValue(node.scaleYProperty(), 1)));
            }
        });
        timeline.play();
        hamburgerMenu.setOpacity(0.9);
    }

    private void createCity() {
        clientController.getSnapshot().getMap().getCity().forEach(city1 -> {
            createSingleCity(CityPosition.getX(city1), CityPosition.getY(city1), city1);
            if (!city1.getColor().getColor().equals(Constants.PURPLE)) {
                createBonus(city1.getBonus().getBonusURL(), city1.getBonus().getBonusInfo(), city1);
            }
        });
        createKingImage(CityPosition.getX(clientController.getSnapshot().getKing().getCurrentCity()), CityPosition
                .getY(clientController.getSnapshot().getKing().getCurrentCity()));
    }

    private void createBonus(ArrayList<String> bonusURL, ArrayList<String> bonusInfo, City city1) {
        for (int i = 0; i < bonusURL.size(); i++) {
            ImageView imageView = new ImageView();
            imageView.setImage(ImageLoader.getInstance().getImage(bonusURL.get(i)));
            imageView.setCache(true);
            imageView.fitHeightProperty().bind(background.heightProperty().multiply(0.05));
            imageView.fitWidthProperty().bind(background.widthProperty().divide(30));
            imageView.getStyleClass().add(city1.getCityName().getCityName());
            Label singleBonusInfo = new Label(bonusInfo.get(i));
            singleBonusInfo.setMouseTransparent(true);
            singleBonusInfo.getStyleClass().add("bonusLabel");
            singleBonusInfo.setLabelFor(imageView);
            singleBonusInfo.setWrapText(true);
            DropShadow ds = new DropShadow(15, Color.BLACK);
            imageView.setEffect(ds);
            imageView.setPreserveRatio(true);
            background.getChildren().add(imageView);
            background.getChildren().add(singleBonusInfo);
            imageView.layoutXProperty().bind(background.widthProperty().multiply(CityPosition.getX(city1)).add(i * 20));
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

                    if (pulseBonus == null) {

                    } else {
                        pulseBonus.setValue(true);
                    }
                    if (needToSelectOldBonus) {
                        Graphics.playSomeSound(Constants.BUTTON);
                        new Thread(() -> {
                            clientController.getCityRewardBonus(city1);
                        }).start();
                        needToSelectOldBonus = false;
                    }
                }
            });
        }
        Set<Node> labelWhereCycle = background.lookupAll(".bonusLabel");
        labelWhereCycle.forEach(Node::toFront);
    }

    private void createKingImage(double x, double y) {
        kingImage = new ImageView();
        kingImage.setImage(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "Crown.png", background.getHeight() * 0.07, background.getWidth() / 25));
        kingImage.setCache(true);
        kingImage.fitHeightProperty().bind(background.heightProperty().multiply(0.07));
        kingImage.fitWidthProperty().bind(background.widthProperty().divide(25));
        DropShadow ds = new DropShadow(15, Color.BLACK);
        kingImage.setEffect(ds);
        background.getChildren().add(kingImage);
        kingImage.layoutXProperty().bind(background.widthProperty().multiply(x).add(background.widthProperty().divide(20)));
        kingImage.layoutYProperty().bind(background.heightProperty().multiply(y));
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
                Graphics.playSomeSound(Constants.BUTTON);
                showKingPopover(kingImage);
            }
        });
    }

    private void showKingPopover(ImageView imageView) {
        politicCardforBuildWithKing.clear();
        PopOver popOver = new PopOver();
        VBox vBox = new VBox();
        HBox kingHBox = new HBox();
        ArrayList<Councilor> kingCouncilors = new ArrayList<>(currentSnapshot.getKing().getCouncil().getCouncil());
        for (Councilor councilor : kingCouncilors) {
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
                stringa = "Multicolor";
            } else {
                stringa = Translator.translatingToIta(politicCard.getPoliticColor().getColor());
            }
            politicCardsCheckBox.setText(stringa);
            politicCardsCheckBox.setId("JFXCheckBox");
            politicCardsCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (newValue) {
                        politicCardforBuildWithKing.add(politicCard);
                    } else {
                        politicCardforBuildWithKing.remove(politicCard);
                    }
                    Graphics.playSomeSound("Tick");
                }
            });
            politicVBox.getChildren().add(politicCardsCheckBox);
        }
        politicVBox.setSpacing(10);
        JFXButton jfxButton = new JFXButton();
        jfxButton.setBackground(new Background(new BackgroundFill(Paint.valueOf("#FF5100"), null, null)));
        jfxButton.setButtonType(JFXButton.ButtonType.RAISED);
        jfxButton.setTextFill(Paint.valueOf("WHITE"));
        jfxButton.setText("OKAY");
        jfxButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!buildWithKingPhase.getValue()) {
                    popOver.hide();
                    buildWithKingPhase.setValue(true);
                    startBuildWithKing();
                    Graphics.playSomeSound(Constants.BUTTON);
                }
            }
        });
        vBox.getChildren().addAll(kingHBox, politicVBox, jfxButton);
        VBox.setMargin(jfxButton, new Insets(0, 0, 0, 12));
        vBox.setSpacing(20);
        vBox.setPadding(new Insets(20, 20, 20, 20));
        kingHBox.setAlignment(Pos.CENTER);
        popOver.setContentNode(vBox);
        popOver.show(imageView);
    }

    private void startBuildWithKing() {
        Set<Node> cities = background.lookupAll(".cityImage");
        kingPathforBuild.clear();
        for (Node node : cities) {
            Graphics.scaleTransitionEffectCycle(node, 1.05f, 1.05f, pulseCity);
        }
    }

    private void createSingleCity(double layoutX, double layoutY, City city) {
        ImageView imageView = new ImageView();
        imageView.getStyleClass().add("cityImage");
        Image cityImage = ImageLoader.getInstance().getImage("/ClientPackage/View/GUIResources/Image/City/" + city.getColor().getColor().toLowerCase() + ".png", background.getHeight() * 0.17, background.getWidth() / 11);
        imageView.setImage(cityImage);
        imageView.setCache(true);
        imageView.fitHeightProperty().bind(background.heightProperty().multiply(0.17));
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
                if (!buildWithKingPhase.get()) {
                    Graphics.playSomeSound(Constants.BUTTON);
                    showPopoverOnCity(city, imageView);
                } else {
                    Graphics.playSomeSound(Constants.BUTTON);
                    highlightCity(imageView, city);
                }
            }
        });
        ImageView cityName = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "City/Names/" + city.
                getCityName().getCityName() + "" + city.getColor().getColor() + ".png", imageView.getFitWidth(), imageView.getFitHeight()));
        cityName.setCache(true);
        cityName.setMouseTransparent(true);
        background.getChildren().add(cityName);
        cityName.layoutXProperty().bind(imageView.layoutXProperty());
        cityName.layoutYProperty().bind(imageView.layoutYProperty());
        cityName.fitWidthProperty().bind(imageView.fitWidthProperty());
        cityName.setPreserveRatio(true);
        HBox emporiumHBox = new HBox();
        emporiumHBox.setId(city.getCityName().getCityName());
        background.getChildren().add(emporiumHBox);
        emporiumHBox.layoutXProperty().bind(background.widthProperty().multiply(CityPosition.getX(city)));
        emporiumHBox.layoutYProperty().bind(background.heightProperty().multiply(CityPosition.getY(city)).add(imageView.fitHeightProperty()).subtract(30));
        emporiumHBox.toFront();
        for (Map.Entry<String, BaseUser> userHashMap : clientController.getSnapshot().getUsersInGame().entrySet()) {
            ImageView imageToAdd = new ImageView();
            imageToAdd.setId(userHashMap.getKey());
            imageToAdd.setImage(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "/Emporia/" + userHashMap.getValue().getUserColor().getColor() + ".png", imageView.getFitHeight(), imageView.getFitWidth()));
            imageToAdd.fitHeightProperty().bind(imageView.fitHeightProperty().divide(6));
            imageToAdd.setPreserveRatio(true);
            imageToAdd.setVisible(false);
            imageToAdd.setCache(true);
            emporiumHBox.getChildren().add(imageToAdd);
            if (userHashMap.getKey().equals("FakeUser"))
                Tooltip.install(imageToAdd, new Tooltip("Emporio aggiuntivo"));
            else
                Tooltip.install(imageToAdd, new Tooltip("Emporio di " + userHashMap.getKey()));
        }
        Graphics.addShadow(cityName);
    }

    private void highlightCity(ImageView imageView, City city) {
        if (imageView.getEffect() == null) {
            int depth = 70; //Setting the uniform variable for the glow width and height
            DropShadow borderGlow = new DropShadow();
            borderGlow.setOffsetY(0f);
            borderGlow.setOffsetX(0f);
            borderGlow.setColor(Color.YELLOW);
            borderGlow.setWidth(depth);
            borderGlow.setHeight(depth);
            imageView.setEffect(borderGlow);
            kingPathforBuild.add(city);
        } else {
            kingPathforBuild.remove(city);
            imageView.setEffect(null);
        }
    }

    private void showPopoverOnCity(City city, ImageView imageView) {
        PopOver popOver = new PopOver();
        VBox cityInfoVBox = new VBox();
        HBox buttonHbox = new HBox();
        final PermitCard[] permitCardSelected = {null};
        ImageView cityName = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "City/Names/" + city.
                getCityName().getCityName() + "" + city.getColor().getColor() + ".png"));
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
                if (newValue.intValue() != -1) {
                    permitCardSelected[0] = clientController.getSnapshot().getCurrentUser().getPermitCards().get(newValue.intValue());
                }
            }
        });
        jfxComboBox.setPromptText("Seleziona la carta permesso");
        JFXButton jfxButton = new JFXButton("Compra emporio");
        jfxButton.setBackground(new Background(new BackgroundFill(Paint.valueOf("D73D00"), null, null)));
        jfxButton.setTextFill(Paint.valueOf("WHITE"));
        jfxButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Graphics.playSomeSound(Constants.BUTTON);
                if (permitCardSelected[0] != null) {
                    Action action = new MainActionBuildWithPermitCard(city, permitCardSelected[0]);
                    clientController.doAction(action);
                } else {
                    guiView.onActionNotPossibleException(new ActionNotPossibleException("You need to select a Permit card!"));
                }
            }
        });
        buttonHbox.getChildren().addAll(jfxComboBox, jfxButton);
        buttonHbox.setSpacing(10);
        if (CityPosition.getX(city) > 0.5) {
            if (CityPosition.getY(city) > 0.5) {
                popOver.setArrowLocation(PopOver.ArrowLocation.RIGHT_BOTTOM);
            } else {
                popOver.setArrowLocation(PopOver.ArrowLocation.RIGHT_TOP);
            }
        } else {
            if (CityPosition.getY(city) > 0.5) {
                popOver.setArrowLocation(PopOver.ArrowLocation.LEFT_BOTTOM);
            } else {
                popOver.setArrowLocation(PopOver.ArrowLocation.LEFT_TOP);
            }
        }
        cityInfoVBox.setPadding(new Insets(20, 20, 20, 20));
        cityInfoVBox.setSpacing(20);
        cityInfoVBox.getChildren().addAll(cityName, buttonHbox);
        cityInfoVBox.setAlignment(Pos.CENTER);
        popOver.setContentNode(cityInfoVBox);
        popOver.setTitle(city.getCityName().getCityName());
        popOver.show(imageView);
    }

    private void handleClick() {
        background.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            }
        });
    }

    private void initController() {
        shopController.setClientController(clientController, guiView);
        shopController.setMatchController(this);
        nobilityPathController.setClientController(clientController, guiView);
        nobilityPathController.setMatchController(this);
    }

    private void initPermitButton() {
        coastPermitButton.setOnMouseClicked(new PermitButtonHandler(RegionName.COAST));
        hillPermitButton.setOnMouseClicked(new PermitButtonHandler(RegionName.HILL));
        mountainPermitButton.setOnMouseClicked(new PermitButtonHandler(RegionName.MOUNTAIN));
    }

    private void createOverlay() {
        if (bottomPane.getChildren().contains(hiddenSidesPane)) {
            bottomPane.getChildren().remove(hiddenSidesPane);
        }
        VBox vbox = new VBox();
        HBox hbox1 = new HBox();
        HBox hbox2 = new HBox();
        for (RegionName regionName : RegionName.values()) {
            HBox hBox = new HBox();
            try {
                ArrayList<Councilor> councilors = currentSnapshot.getCouncil(regionName);
                ArrayList<ImageView> imageViews = new ArrayList<>();
                for (int i = 0; i < councilors.size(); i++) {
                    ImageView imageView = new ImageView();
                    try {
                        imageView.setImage(ImageLoader.getInstance().getImage(councilors.get(i).getColor().getImageUrl(), background.getWidth() / 10, vbox.getPrefHeight() / 3));
                        imageView.setCache(true);
                        imageView.fitWidthProperty().bind(background.prefWidthProperty().divide(10));
                        imageView.fitHeightProperty().bind(vbox.prefHeightProperty().divide(3));
                        imageView.setPreserveRatio(true);
                        imageViews.add(imageView);
                    } catch (IllegalArgumentException e) {
                    }
                    hBox.getChildren().add(imageView);
                }
                councilHashMap.put(regionName, imageViews);
                hbox1.getChildren().add(hBox);
                hBox.setOnMouseClicked(new CouncilorHandler(hBox, regionName, this, clientController, null));
            } catch (CouncilNotFoundException e) {
                e.printStackTrace();
            }
        }
        hbox1.prefWidthProperty().bind(bottomPane.prefWidthProperty());
        hbox1.spacingProperty().bind(bottomPane.prefWidthProperty().divide(5));
        hbox1.setAlignment(Pos.CENTER);
        HBox kingHBox = new HBox();
        ArrayList<Councilor> kingCouncilors = new ArrayList<>(currentSnapshot.getKing().getCouncil().getCouncil());
        for (Councilor councilor : kingCouncilors) {
            ImageView imageView = new ImageView();
            try {
                imageView.setImage(ImageLoader.getInstance().getImage(councilor.getColor().getImageUrl()));
                imageView.setCache(true);
                imageView.fitWidthProperty().bind(background.prefWidthProperty().divide(10));
                imageView.fitHeightProperty().bind(vbox.prefHeightProperty().divide(3));
                imageView.setPreserveRatio(true);
                kingCouncil.add(imageView);
            } catch (IllegalArgumentException e) {
            }
            kingHBox.getChildren().add(imageView);
        }
        hbox2.getChildren().add(kingHBox);
        kingHBox.setAlignment(Pos.CENTER);
        kingHBox.setOnMouseClicked(new CouncilorHandler(hbox2, null, this, clientController, currentSnapshot.getKing()));
        hbox2.setAlignment(Pos.CENTER);
        HBox permitHBox = new HBox();
        for (RegionName regionName : RegionName.values()) {
            HBox hboxTmp = new HBox();
            hboxTmp.getStyleClass().add(regionName.name());
            currentSnapshot.getVisibleRegionPermitCard(regionName).forEach(permitCard -> {
                GridPane gridPane = new GridPane();
                gridPane.prefWidthProperty().bind(bottomPane.prefWidthProperty().divide(15));
                gridPane.prefHeightProperty().bind(bottomPane.prefHeightProperty().divide(2));
                Label label = new Label(permitCard.getCityString());
                label.getStyleClass().add("permitLabel");
                label.setTextFill(Paint.valueOf("WHITE"));
                ImageView permitImageView = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "PermitCard.png"));
                permitImageView.setCache(true);
                permitImageView.setPreserveRatio(true);
                gridPane.add(permitImageView, 0, 0);
                gridPane.add(label, 0, 0);
                label.setFont(Font.font(8));
                GridPane.setColumnSpan(label, 3);
                GridPane.setHalignment(label, HPos.CENTER);
                GridPane.setValignment(label, VPos.CENTER);
                GridPane.setRowSpan(permitImageView, 3);
                GridPane.setColumnSpan(permitImageView, 3);
                permitImageView.getStyleClass().add("visiblePermitCard");
                gridPane.getStyleClass().add("gridPanePermitCard");
                permitImageView.fitWidthProperty().bind(gridPane.prefWidthProperty());
                permitImageView.fitHeightProperty().bind(gridPane.prefHeightProperty());
                ArrayList<ImageView> imageViewArrayList = new ArrayList<ImageView>();
                ArrayList<Label> labelArrayList = new ArrayList<Label>();
                for (int i = 0; i < 3; i++) {
                    Label label1 = new Label();
                    ImageView imageView = new ImageView();
                    imageView.getStyleClass().add("permitCardBonus");
                    gridPane.add(imageView, i, 2);
                    gridPane.add(label1, i, 2);
                    imageViewArrayList.add(imageView);
                    labelArrayList.add(label1);
                    imageView.setVisible(false);
                    label1.setTextFill(Paint.valueOf("WHITE"));
                    label1.getStyleClass().add("bonusInfoLabel");
                    label1.setWrapText(true);
                    imageView.fitWidthProperty().bind(permitImageView.fitWidthProperty().divide(3));
                    imageView.fitHeightProperty().bind(permitImageView.fitHeightProperty().divide(3));
                    imageView.setPreserveRatio(true);
                    GridPane.setHalignment(imageView, HPos.CENTER);
                    GridPane.setValignment(imageView, VPos.CENTER);
                    GridPane.setHalignment(label1, HPos.CENTER);
                    GridPane.setValignment(label1, VPos.CENTER);
                }
                for (int i = 0; i < permitCard.getBonus().getBonusURL().size(); i++) {
                    ImageView imageViewBonus = imageViewArrayList.get(i);
                    imageViewBonus.setImage(ImageLoader.getInstance().getImage(permitCard.getBonus().getBonusURL().get(i)));
                    imageViewBonus.setCache(true);
                    imageViewBonus.setVisible(true);
                    Label bonusInfo = labelArrayList.get(i);
                    bonusInfo.setText(permitCard.getBonus().getBonusInfo().get(i));
                    bonusInfo.setVisible(true);
                }
                gridPane.setOnMouseClicked(new PermitCardHandler(permitCard, this, clientController, needToSelectPermitCard));
                PermitPopOverHandler permitPopOverHandler =
                        new PermitPopOverHandler(clientController, regionName, currentSnapshot
                                .getVisibleRegionPermitCard(regionName)
                                .indexOf(permitCard), this);
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
        vbox.getChildren().addAll(hbox2, hbox1, permitHBox);
        vbox.spacingProperty().bind(bottomPane.prefHeightProperty().divide(8));
        StackPane.setMargin(vbox, new Insets(20, 0, 20, 20));
        bottomPane.getChildren().clear();
        bottomPane.getChildren().add(vbox);
        vbox.prefWidthProperty().bind(bottomPane.prefWidthProperty());
        vbox.prefHeightProperty().bind(bottomPane.prefHeightProperty());
        bottomPane.prefHeightProperty().bind(background.prefHeightProperty().divide(5));
        bottomPane.prefWidthProperty().bind(background.prefWidthProperty());
        bottomPane.setOpacity(0.8);
    }

    @Override
    public void setMyTurn(boolean value, SnapshotToSend snapshot) {
        myTurn = value;
        this.currentSnapshot = snapshot;
        turnFinished(myTurn);
        disableAllEffect();
        updateView();
    }

    private void disableAllEffect() {
        if (pulseBonus != null) {
            pulseBonus.setValue(true);
        }
        if (pulseCity != null) {
            finishKingPhase();
        }
        if (needToSelectPermitCard != null) {
            needToSelectPermitCard.setValue(false);
            needToSelectOldPermitCard.setValue(false);
            stopPulseOldPermitCard.setValue(true);
            stopPulsePermitCard.setValue(true);
            onSelectOldPermitCard();
            hidePermitCardHightLight(".visiblePermitCard", bottomPane);
        }
    }

    @Override
    public void onStartMarket() {
        selectionModel.selectNext();
        if (tabPane.getStyle().contains("-fx-background-image: url('/ClientPackage/View/GUIResources/Image/TabPaneMatchPattern.png')")) {
            tabPane.setStyle(tabPane.getStyle().replace("-fx-background-image: url('/ClientPackage/View/GUIResources/Image/TabPaneMatchPattern.png')",
                    "-fx-background-image: url('/ClientPackage/View/GUIResources/Image/TabPaneShopPattern.png')"));
        }
    }

    @Override
    public void onStartBuyPhase() {
    }

    @Override
    public void onFinishMarket() {
        selectionModel.selectFirst();
        if (tabPane.getStyle().contains("-fx-background-image: url('/ClientPackage/View/GUIResources/Image/TabPaneShopPattern.png'")) {
            tabPane.setStyle(tabPane.getStyle().replace("-fx-background-image: url('/ClientPackage/View/GUIResources/Image/TabPaneShopPattern.png')",
                    "-fx-background-image: url('/ClientPackage/View/GUIResources/Image/TabPaneMatchPattern.png')"));
        }
    }

    @Override
    public void selectPermitCard() {
        needToSelectPermitCard.setValue(true);
        stopPulsePermitCard.setValue(false);
        Set<Node> nodes = bottomPane.lookupAll(".visiblePermitCard");
        nodes.forEach(node -> highlightPermitCard(node, stopPulsePermitCard));
        bottomPane.setVisible(true);
    }

    private void highlightPermitCard(Node node, BooleanProperty stopEffect) {
        int depth = 70; //Setting the uniform variable for the glow width and height
        DropShadow borderGlow = new DropShadow();
        borderGlow.setOffsetY(0f);
        borderGlow.setOffsetX(0f);
        borderGlow.setColor(Color.YELLOW);
        borderGlow.setWidth(depth);
        borderGlow.setHeight(depth);
        node.setEffect(borderGlow);
        Graphics.scaleTransitionEffectCycle(node, 1.1f, 1.1f, stopEffect);
    }

    @Override
    public void selectCityRewardBonus() {
        needToSelectOldBonus = true;
        stopPulseBonus.setValue(false);
        clientController.getSnapshot().getCurrentUser().getUsersEmporium().forEach(this::pulseBonus);
    }

    @Override
    public void moveKing(ArrayList<City> kingPath) {
        if (kingPath.size() > 1) {
            Timeline timeline = new Timeline();
            kingImage.layoutXProperty().unbind();
            kingImage.layoutYProperty().unbind();
            kingPath.remove(0);
            kingPath.forEach(city1 -> {
                KeyValue keyValueX = new KeyValue(kingImage.layoutXProperty(), background.getWidth() * CityPosition.getX(city1), Interpolator.EASE_BOTH);
                KeyValue keyValueY = new KeyValue(kingImage.layoutYProperty(), background.getHeight() * CityPosition.getY(city1), Interpolator.EASE_BOTH);
                KeyFrame keyFrame = new KeyFrame(Duration.millis(900 * kingPath.indexOf(city1)), keyValueX, keyValueY);
                timeline.getKeyFrames().add(keyFrame);
            });
            timeline.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    kingImage.layoutXProperty().bind(background.widthProperty().multiply(CityPosition.getX(kingPath.get(kingPath.size() - 1))).add(background.widthProperty().divide(20)));
                    kingImage.layoutYProperty().bind(background.heightProperty().multiply(CityPosition.getY(kingPath.get(kingPath.size() - 1))));
                }
            });
            timeline.play();
        }
    }

    @Override
    public void selectOldPermitCardBonus() {
        needToSelectOldPermitCard.setValue(true);
        stopPulsePermitCard.setValue(false);
        Set<Node> permitCards = gridPane.lookupAll(".myPermitCard");
        permitCards.forEach(node -> highlightPermitCard(node, stopPulseOldPermitCard));
        oldPermitCardNodeList.animateList();
        handHBox.setVisible(true);
        hamburgerMenu.setVisible(false);
        nobilityPath.setVisible(false);
    }

    private void pulseBonus(City city1) {
        if (!city1.getColor().getColor().equals(Constants.PURPLE)) {
            if (pulseBonus == null) {
                pulseBonus = new SimpleBooleanProperty(false);
            } else {
                pulseBonus.setValue(false);
            }
            Set<Node> nodes = background.lookupAll("." + city1.getCityName().getCityName());
            nodes.forEach(node -> Graphics.scaleTransitionEffectCycle(node, 1.2f, 1.2f, pulseBonus));
        }
    }

    private void turnFinished(boolean thisTurn) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (thisTurn) {
                    startTimer();
                    Graphics.notification("E' il tuo turno!", true);
                    turnImage.setImage(new Image(Constants.IMAGE_PATH + "/turnYes1.png"));
                    Tooltip.install(turnImage, new Tooltip("E' il tuo turno"));
                } else {
                    cancelTimer();
                    Graphics.notification("Turno finito!", true);
                    turnImage.setImage(new Image(Constants.IMAGE_PATH + "/turnNo1.png"));
                    Tooltip.install(turnImage, new Tooltip("Non è il tuo turno"));
                }

            }
        });
    }

    private void cancelTimer() {
        if (timer != null) {
            timer.cancel();
        }
        timerIndicator.setValue(0);
    }

    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerProgress(timerIndicator, this, clientController), 0, 1000);
    }


    private void reprintCouncilor() {
        for (RegionName regionName : RegionName.values()) {
            ArrayList<Councilor> councilors = null;
            try {
                councilors = clientController.getSnapshot().getCouncil(regionName);
                ArrayList<ImageView> imageView = councilHashMap.get(regionName);
                for (int i = 0; i < councilors.size(); i++) {
                    imageView.get(i).setImage(ImageLoader.getInstance().getImage(councilors.get(i).getColor().getImageUrl(), imageView.get(i).getBoundsInLocal().getWidth(), imageView.get(i).getBoundsInParent().getHeight()));
                }
            } catch (CouncilNotFoundException e) {
                e.printStackTrace();
            }

        }
        ArrayList<Councilor> kingCouncilArray = new ArrayList<>(clientController.getSnapshot().getKing().getCouncil().getCouncil());
        for (int i = 0; i < kingCouncil.size(); i++) {
            kingCouncil.get(i).setImage(ImageLoader.getInstance().getImage(kingCouncilArray.get(i).getColor().getImageUrl(), kingCouncil.get(i).getFitWidth(), kingCouncil.get(i).getFitHeight()));
        }
    }


    @Override
    public void updateView() {
        this.currentSnapshot = clientController.getSnapshot();
        nobilityPathText.setText(currentSnapshot.getCurrentUser().getNobilityPathPosition().getPosition() + "");
        richPathText.setText(currentSnapshot.getCurrentUser().getCoinPathPosition() + "");
        helperText.setText(currentSnapshot.getCurrentUser().getHelpers().size() + "");
        victoryPathText.setText(currentSnapshot.getCurrentUser().getVictoryPathPosition() + "");
        mainActionText.setText(currentSnapshot.getCurrentUser().getMainActionCounter() + "");
        fastActionText.setText(currentSnapshot.getCurrentUser().getFastActionCounter() + "");
        reprintCouncilor();
        reprintPermitCard();
        setEmporiaVisibility();
        populateField(clientController.getSnapshot().getUsersInGame().get(usersComboBox.getValue()));
        createHand();
        creteOldPermitCard();
        if (needToSelectOldPermitCard.get() && myTurn)
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
        for (RegionName regionName : RegionName.values()) {
            Node region = bottomPane.lookup("." + regionName.name());
            Set<Node> labels = region.lookupAll(".permitLabel");
            Set<Node> gridPanes = region.lookupAll(".gridPanePermitCard");
            ArrayList<Node> labelsList = new ArrayList<>();
            labelsList.addAll(labels);
            ArrayList<Node> gridPanesList = new ArrayList<>();
            gridPanesList.addAll(gridPanes);
            for (int i = 0; i < labelsList.size(); i++) {
                Set<Node> bonusImages = gridPanesList.get(i).lookupAll(".permitCardBonus");
                Set<Node> bonusLabels = gridPanesList.get(i).lookupAll(".bonusInfoLabel");
                ArrayList<Node> bonusInfoArray = new ArrayList<>();
                bonusInfoArray.addAll(bonusLabels);
                ArrayList<Node> imageViewBonusList = new ArrayList<>();
                imageViewBonusList.addAll(bonusImages);
                PermitCard permitCardTmp = clientController.getSnapshot().getVisibleRegionPermitCard(regionName).get(i);
                Label label = (Label) labelsList.get(i);
                label.setText(permitCardTmp.getCityString());
                GridPane gridPane = (GridPane) gridPanesList.get(i);
                gridPane.setOnMouseClicked(new PermitCardHandler(permitCardTmp, this, clientController, needToSelectPermitCard));
                PermitPopOverHandler permitPopOverHandler = new PermitPopOverHandler(clientController, regionName, i, this);
                gridPane.setOnMouseEntered(permitPopOverHandler);
                gridPane.setOnMouseExited(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        permitPopOverHandler.hide();
                    }
                });
                for (int j = 0; j < imageViewBonusList.size(); j++) {
                    Label bonusLabel = (Label) bonusInfoArray.get(j);
                    ImageView imageViewBonus = (ImageView) imageViewBonusList.get(j);
                    if (j < permitCardTmp.getBonus().getBonusURL().size()) {
                        imageViewBonus.setVisible(true);
                        imageViewBonus.setImage(ImageLoader.getInstance().getImage(permitCardTmp.getBonus().getBonusURL().get(j), imageViewBonus.getFitWidth(), imageViewBonus.getFitHeight()));
                        bonusLabel.setText(permitCardTmp.getBonus().getBonusInfo().get(j));
                        bonusLabel.setVisible(true);
                    } else {
                        imageViewBonus.setVisible(false);
                        bonusLabel.setVisible(false);
                    }
                }
            }
        }
    }

    public void buyHelper(Event event) {
        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Graphics.playSomeSound(Constants.BUTTON);
                Action action = new FastActionMoneyForHelper();
                clientController.doAction(action);
            }
        };

        String buttonText = "Compra Aiutanti";
        String infoLabel = "Azione veloce: Compra un aiutante per tre monete!";
        showDefaultPopOver(eventHandler, infoLabel, buttonText, (Node) event.getSource());
    }

    public void showDefaultPopOver(EventHandler<MouseEvent> eventHandler, String infoLabel, String buttonText, Node source) {
        PopOver popOver = new PopOver();
        VBox vBox = new VBox();
        JFXButton jfxButton = new JFXButton(buttonText);
        jfxButton.setBackground(new Background(new BackgroundFill(Paint.valueOf("#FF5100"), null, null)));
        jfxButton.setButtonType(JFXButton.ButtonType.RAISED);
        jfxButton.setTextFill(Paint.valueOf("WHITE"));
        jfxButton.setOnMouseClicked(eventHandler);
        Label label = new Label();
        label.setText(infoLabel);
        vBox.getChildren().add(label);
        vBox.getChildren().add(jfxButton);
        vBox.setSpacing(30);
        vBox.setPadding(new Insets(40, 40, 40, 40));
        vBox.setAlignment(Pos.CENTER);
        popOver.setContentNode(vBox);
        popOver.show(source);
    }

    public void buyMainAction(Event event) {
        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Graphics.playSomeSound(Constants.BUTTON);
                Action action = new FastActionNewMainAction();
                clientController.doAction(action);
            }
        };
        String buttonText = "Compra Azione";
        String infoLabel = "Azione veloce: Ottieni un azione principale per 3 aiutanti";
        showDefaultPopOver(eventHandler, infoLabel, buttonText, (Node) event.getSource());
    }

    public void showMore() {
        if (bottomPane.isVisible()) {
            bottomPane.setVisible(false);
        } else {
            bottomPane.setVisible(true);
        }
    }

    public void showLess() {
        if (nobilityPath.isVisible()) {
            handHBox.setVisible(true);
            nobilityPath.setVisible(false);
        } else {
            handHBox.setVisible(false);
            nobilityPath.setVisible(true);
        }
    }

    public void onSelectPermitCard(PermitCard permitCard) {
        hidePermitCardHightLight(".visiblePermitCard", bottomPane);
        needToSelectPermitCard.setValue(false);
        stopPulsePermitCard.setValue(true);
        clientController.onSelectPermitCard(permitCard);
    }

    private void hidePermitCardHightLight(String selector, Pane container) {
        Set<Node> nodes = container.lookupAll(selector);
        nodes.forEach(node -> node.setEffect(null));
    }

    public void finishKingPhase() {
        Set<Node> nodes = background.lookupAll(".cityImage");
        nodes.forEach(node -> {
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
        hidePermitCardHightLight(".myPermitCard", gridPane);
    }

    public Pane getBackground() {
        return background;
    }

    public boolean getBuildWithKingPhase() {
        return buildWithKingPhase.get();
    }

    public ArrayList<City> getKingPathforBuild() {
        return kingPathforBuild;
    }

    public ArrayList<PoliticCard> getPoliticCardforBuildWithKing() {
        return politicCardforBuildWithKing;
    }

    private class PermitButtonHandler implements EventHandler<MouseEvent> {

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

}
