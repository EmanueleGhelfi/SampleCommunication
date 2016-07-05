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
import org.controlsfx.control.PopOver.ArrowLocation;

import java.util.*;
import java.util.Map.Entry;

/**
 * Created by Giulio on 17/05/2016.
 */
public class MatchController implements BaseController {

    private ClientController clientController;
    private boolean myTurn;
    private SnapshotToSend currentSnapshot;
    private final PopOver popOver = new PopOver();
    private final Pane paneOfPopup = new Pane();
    private final JFXComboBox<String> councilorColorToChoose = new JFXComboBox<>();
    private String city;
    private GUIView guiView;
    private HiddenSidesPane hiddenSidesPane;
    private final ImageView turnImage = new ImageView();
    private final HBox handHBox = new HBox();
    private JFXComboBox<String> usersComboBox;
    private BooleanProperty pulseBonus;
    private final BooleanProperty stopPulsePermitCard = new SimpleBooleanProperty(false);
    private final BooleanProperty stopPulseOldPermitCard = new SimpleBooleanProperty(false);
    private final BooleanProperty stopPulseBonus = new SimpleBooleanProperty();
    private boolean needToSelectOldBonus;
    private final BooleanProperty needToSelectPermitCard = new SimpleBooleanProperty(false);
    private final BooleanProperty needToSelectOldPermitCard = new SimpleBooleanProperty(false);
    private final HashMap<RegionName, ArrayList<ImageView>> councilHashMap = new HashMap<>();
    private final ArrayList<ImageView> kingCouncil = new ArrayList<>();
    private final ArrayList<PoliticCard> politicCardforBuildWithKing = new ArrayList<>();
    private final ArrayList<City> kingPathforBuild = new ArrayList<>();
    private final BooleanProperty buildWithKingPhase = new SimpleBooleanProperty(false);
    private final BooleanProperty pulseCity = new SimpleBooleanProperty(false);
    private final JFXNodesList moreActionNodeList = new JFXNodesList();
    private final JFXNodesList oldPermitCardNodeList = new JFXNodesList();
    private ImageView boardImageView;
    private final ProgressIndicator progressIndicator = new ProgressIndicator();
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
        this.hiddenSidesPane = new HiddenSidesPane();
        this.currentSnapshot = clientController.getSnapshot();
        guiView.registerBaseController(this);
        this.initController();
        this.backgroundImage.setImage(ImageLoader.getInstance().getImage(this.currentSnapshot.getMap().getRealMap()));
        this.backgroundImage.setPreserveRatio(true);
        this.backgroundImage.setCache(true);
        this.backgroundImage.fitHeightProperty().bind(this.gridPane.heightProperty());
        this.backgroundImage.fitWidthProperty().bind(this.gridPane.widthProperty());
        this.gridPane.prefWidthProperty().bind(this.background.prefWidthProperty());
        this.gridPane.prefHeightProperty().bind(this.background.prefHeightProperty());
        this.backgroundImage.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) {
                MatchController.this.background.setPrefWidth(newValue.getWidth());
                MatchController.this.background.setPrefHeight(newValue.getHeight());
                // gridPane.setPrefSize(background.getPrefWidth(),background.getMaxHeight());

            }
        });
        this.backgroundImage.fitWidthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            }
        });
        this.createGauge();
        this.createOpponentTooltip();
        this.pulseCity.bind(this.buildWithKingPhase.not());
        this.createOverlay();
        this.initPermitButton();
        this.handleClick();
        this.createCity();
        this.gridPane.add(this.handHBox, 0, 1);
        this.createHand();
        this.creteOldPermitCard();
        this.gridPane.add(this.oldPermitCardNodeList, 2, 2);
        GridPane.setHalignment(this.oldPermitCardNodeList, HPos.RIGHT);
        GridPane.setValignment(this.oldPermitCardNodeList, VPos.BOTTOM);
        GridPane.setMargin(this.oldPermitCardNodeList, new Insets(0, 20, 20, 0));
        this.oldPermitCardNodeList.onContextMenuRequestedProperty().addListener(new ChangeListener<EventHandler<? super ContextMenuEvent>>() {
            @Override
            public void changed(ObservableValue<? extends EventHandler<? super ContextMenuEvent>> observable, EventHandler<? super ContextMenuEvent> oldValue, EventHandler<? super ContextMenuEvent> newValue) {
                Graphics.playSomeSound("PlusButton");
            }
        });
        this.hamburgerMenu.prefWidthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.doubleValue() > 0) {
                    MatchController.this.oldPermitCardNodeList.setVisible(false);
                } else {
                    MatchController.this.oldPermitCardNodeList.setVisible(true);
                }
            }
        });
        this.tabPane.setStyle("-fx-background-image: url('/ClientPackage/View/GUIResources/Image/TabPaneMatchPattern.png')");
        this.selectionModel = this.tabPane.getSelectionModel();
        this.initHamburgerIcon();
        this.bottomPane.setVisible(false);
        this.nobilityPath.setVisible(false);
        this.kingPathforBuild.add(clientController.getSnapshot().getKing().getCurrentCity());
        this.createNodeList();
        this.setBoard();
        this.populateHamburgerMenu();
        GridPane.setHalignment(this.nobilityPath, HPos.CENTER);
        GridPane.setValignment(this.nobilityPath, VPos.BOTTOM);
        this.createHBoxMenu();
        this.createLayers();
        this.placeRegionBonus();
    }

    private void createOpponentTooltip() {
        Tooltip.install(this.userColorImageView, new Tooltip("Colore"));
        Tooltip.install(this.userMoneyImageView, new Tooltip("Posizione sul percorso della ricchezza"));
        Tooltip.install(this.userVictoryImageView, new Tooltip("Posizione sul percorso della vittoria"));
        Tooltip.install(this.userNobilityImageView, new Tooltip("Posizione sul percorso della nobiltà"));
        Tooltip.install(this.userPoliticImageView, new Tooltip("Carte politiche"));
        Tooltip.install(this.userHelperImageView, new Tooltip("Aiutanti"));
        Tooltip.install(this.userPermitImageView, new Tooltip("Città su cui può edificare"));
    }

    private void placeRegionBonus() {
        ImageView coastImage = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "/CoastBonusCard.png", this.background.getWidth() * 0.89, this.background.getHeight() * 0.82));
        ImageView hillImage = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "/HillBonusCard.png", this.background.getWidth() * 0.89, this.background.getHeight() * 0.82));
        ImageView mountainImage = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "/MountainBonusCard.png", this.background.getWidth() * 0.89, this.background.getHeight() * 0.82));
        coastImage.setCache(true);
        hillImage.setCache(true);
        mountainImage.setCache(true);
        coastImage.layoutXProperty().bind(this.background.widthProperty().multiply(0.089));
        coastImage.layoutYProperty().bind(this.background.heightProperty().multiply(0.82));
        hillImage.layoutXProperty().bind(this.background.widthProperty().multiply(0.447));
        hillImage.layoutYProperty().bind(this.background.heightProperty().multiply(0.82));
        mountainImage.layoutXProperty().bind(this.background.widthProperty().multiply(0.80));
        mountainImage.layoutYProperty().bind(this.background.heightProperty().multiply(0.82));
        coastImage.fitWidthProperty().bind(this.background.widthProperty().divide(20));
        hillImage.fitWidthProperty().bind(this.background.widthProperty().divide(20));
        mountainImage.fitWidthProperty().bind(this.background.widthProperty().divide(20));
        coastImage.setPreserveRatio(true);
        hillImage.setPreserveRatio(true);
        mountainImage.setPreserveRatio(true);
        Graphics.addShadow(coastImage);
        Graphics.addShadow(hillImage);
        Graphics.addShadow(mountainImage);
        Graphics.bringUpImages(coastImage, hillImage, mountainImage);
        this.background.getChildren().addAll(coastImage, hillImage, mountainImage);
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
        this.moreImg.setGraphic(moreImageView);
        this.lessImg.setGraphic(lessImageView);
        Tooltip.install(this.moreImg, new Tooltip("Carte permesso e consiglieri"));
        Tooltip.install(this.lessImg, new Tooltip("Percorso della nobiltà"));
        this.moreImg.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Graphics.playSomeSound("Button");
                MatchController.this.showMore();
            }
        });
        this.lessImg.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Graphics.playSomeSound("Button");
                MatchController.this.showLess();
            }
        });
        this.menuAllButtons.getChildren().setAll(this.hamburgerIcon, this.moreImg, this.lessImg);
        this.menuAllButtons.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
    }

    private void createLayers() {
        this.boardImageView.toFront();
        this.infoHBox.toFront();
        this.timerIndicator.toFront();
        this.turnImage.toFront();
        this.bottomPane.toFront();
        this.nobilityPath.toFront();
        this.moreActionNodeList.toFront();
        this.hamburgerMenu.toFront();
        this.hamburgerIcon.toFront();
        this.moreImg.toFront();
        this.lessImg.toFront();
        this.menuAllButtons.toFront();
    }

    private void setBoard() {
        this.boardImageView = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "/Board.png", this.gridPane.getWidth(), 60));
        this.boardImageView.setOpacity(0.2);
        this.boardImageView.fitWidthProperty().bind(this.gridPane.widthProperty());
        this.boardImageView.setFitHeight(60);
        this.boardImageView.setMouseTransparent(true);
        this.gridPane.add(this.boardImageView, 0, 0);
        GridPane.setValignment(this.boardImageView, VPos.TOP);
        GridPane.setColumnSpan(this.boardImageView, 3);
        GridPane.setValignment(this.infoHBox, VPos.TOP);
        this.infoHBox.prefHeightProperty().bind(this.boardImageView.fitHeightProperty());
        this.infoHBox.prefWidthProperty().bind(this.gridPane.widthProperty().divide(2));
        this.setImageInInfo();
        this.gridPane.add(this.turnImage, 0, 0);
        this.turnImage.fitWidthProperty().bind(this.background.widthProperty().multiply(0.07));
        this.turnImage.setPreserveRatio(true);
        this.turnImage.setStyle("-fx-effect: dropshadow(three-pass-box, black, 20, 0, 0, 0)");
        GridPane.setValignment(this.turnImage, VPos.CENTER);
        GridPane.setHalignment(this.turnImage, HPos.CENTER);
    }

    private void createGauge() {
        this.timerIndicator = new Gauge();
        this.timerIndicator.setSkin(new FlatSkin(this.timerIndicator));
        this.timerIndicator.setTitle("Mancano ancora");
        this.timerIndicator.setUnit("Secondi");
        this.timerIndicator.setDecimals(0);
        this.timerIndicator.setValueColor(Color.WHITE);
        this.timerIndicator.setTitleColor(Color.WHITE);
        this.timerIndicator.setSubTitleColor(Color.WHITE);
        this.timerIndicator.setBarColor(Color.rgb(0, 214, 215));
        this.timerIndicator.setNeedleColor(Color.WHITE);
        this.timerIndicator.setThresholdColor(Color.rgb(204, 0, 0));
        this.timerIndicator.setTickLabelColor(Color.rgb(151, 151, 151));
        this.timerIndicator.setTickMarkColor(Color.BLACK);
        this.timerIndicator.setTickLabelOrientation(TickLabelOrientation.ORTHOGONAL);
        this.gridPane.add(this.timerIndicator, 0, 0);
        this.timerIndicator.setKeepAspect(true);
        GridPane.setHalignment(this.timerIndicator, HPos.CENTER);
        GridPane.setValignment(this.timerIndicator, VPos.CENTER);
        this.timerIndicator.prefWidthProperty().bind(this.background.widthProperty().multiply(0.1));
    }

    private void setImageInInfo() {
        Tooltip.install(this.imageInfo1, new Tooltip("Aiutanti"));
        Tooltip.install(this.imageInfo2, new Tooltip("Posizione sul percorso della ricchezza"));
        Tooltip.install(this.imageInfo3, new Tooltip("Posizione sul percorso della vittoria"));
        Tooltip.install(this.imageInfo4, new Tooltip("Posizione sul percorso della nobiltà"));
        Tooltip.install(this.imageInfo5, new Tooltip("Azioni veloci disponibili"));
        Tooltip.install(this.imageInfo6, new Tooltip("Azioni principali disponibili"));
    }

    private void creteOldPermitCard() {
        this.oldPermitCardNodeList.getChildren().clear();
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
        this.oldPermitCardNodeList.addAnimatedNode(bagButton, expanded -> new ArrayList<KeyValue>() {{
            this.add(
                    new KeyValue(bagButton.rotateProperty(), expanded ? 540 : 180,
                            Interpolator.EASE_BOTH));
        }});

        for (PermitCard permitCard : this.clientController.getSnapshot().getCurrentUser().getOldPermitCards()) {
            StackPane permitStackPane = new StackPane();
            ImageView permitImage = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "PermitCard.png", 80, 80));
            permitImage.setCache(true);
            permitImage.setFitHeight(80);
            permitImage.setFitWidth(80);
            Label infoLabel = new Label(permitCard.getInfo());
            infoLabel.setTextFill(Paint.valueOf("WHITE"));
            permitStackPane.getChildren().addAll(permitImage, infoLabel);
            StackPane.setAlignment(infoLabel, Pos.TOP_CENTER);
            this.createPermitCardBonusInStackPane(permitCard, permitStackPane, permitImage);
            permitStackPane.setRotate(180);
            permitImage.getStyleClass().add("myPermitCard");
            permitStackPane.setOnMouseClicked(new OldPermitCardHandler(permitCard, this.needToSelectOldPermitCard, this.clientController, this));
            this.oldPermitCardNodeList.addAnimatedNode(permitStackPane);
        }
        this.oldPermitCardNodeList.setRotate(180);
        for (Node node : this.oldPermitCardNodeList.getChildren()) {
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
        this.handHBox.getChildren().clear();
        this.handHBox.prefWidthProperty().bind(this.gridPane.prefWidthProperty());
        this.handHBox.prefHeightProperty().bind(this.gridPane.prefHeightProperty().divide(7));
        this.handHBox.setAlignment(Pos.BOTTOM_CENTER);
        for (PoliticCard politicCard : this.clientController.getSnapshot().getCurrentUser().getPoliticCards()) {
            ImageView politicCardImageView = new ImageView();
            politicCardImageView.setImage(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "/" + politicCard.getUrl() + ".png"));
            politicCardImageView.setCache(true);
            politicCardImageView.fitHeightProperty().bind(this.handHBox.prefHeightProperty());
            politicCardImageView.setPreserveRatio(true);
            this.handHBox.getChildren().add(politicCardImageView);
        }
        for (PermitCard permitCard : this.clientController.getSnapshot().getCurrentUser().getPermitCards()) {
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
            permitGridPane.prefHeightProperty().bind(this.handHBox.heightProperty());
            permitCardImageView.fitHeightProperty().bind(this.handHBox.prefHeightProperty());
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
            this.createPermitCardBonusInGridPane(permitCard, permitGridPane, permitCardImageView);
            permitCardImageView.getStyleClass().add("myPermitCard");
            permitGridPane.setOnMouseClicked(new OldPermitCardHandler(permitCard, this.needToSelectOldPermitCard, this.clientController, this));
            this.handHBox.getChildren().add(permitGridPane);
            GridPane.setMargin(labelOfPermitCard, new Insets(10, 0, 0, 0));
            labelOfPermitCard.toFront();
        }
        GridPane.setValignment(this.handHBox, VPos.BOTTOM);
        GridPane.setHalignment(this.handHBox, HPos.CENTER);
        GridPane.setColumnSpan(this.handHBox, 3);
        GridPane.setRowSpan(this.handHBox, 2);
    }


    private void createNodeList() {
        JFXButton finishKing = new JFXButton("FINISH");
        finishKing.setBackground(new Background(new BackgroundFill(Paint.valueOf("BLUE"), new CornerRadii(20), null)));
        finishKing.setButtonType(JFXButton.ButtonType.FLAT);
        finishKing.setTextFill(Paint.valueOf("WHITE"));
        finishKing.getStyleClass().add("button-raised");
        finishKing.setOnAction(new FinishKingMoveHandler(this.clientController, this));
        finishKing.disableProperty().bind(this.buildWithKingPhase.not());
        finishKing.setVisible(false);
        BooleanProperty animation = new SimpleBooleanProperty();
        this.buildWithKingPhase.addListener(new ChangeListener<Boolean>() {
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
        this.gridPane.add(finishKing, 2, 2);
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
                Graphics.playSomeSound("Button");
                SingleSelectionModel<Tab> selectionModel = MatchController.this.tabPane.getSelectionModel();
                selectionModel.selectNext();
                MatchController.this.tabPane.setStyle("-fx-background-image: url('/ClientPackage/View/GUIResources/Image/TabPaneShopPattern.png')");
                MatchController.this.shopController.displayHelp();
            }
        });
        helpButton.setTooltip(new Tooltip("Aiuto"));
        this.moreActionNodeList.setSpacing(10);
        GridPane.setMargin(this.moreActionNodeList, new Insets(0, 0, 20, 20));
        this.moreActionNodeList.addAnimatedNode(showMore, expanded -> new ArrayList<KeyValue>() {{
            this.add(
                    new KeyValue(showMore.rotateProperty(), expanded ? 225 : 180,
                            Interpolator.EASE_BOTH));
        }});
        this.moreActionNodeList.addAnimatedNode(changeTurnAction);
        this.moreActionNodeList.setRotate(180);
        for (RegionName regionName : RegionName.values()) {
            this.moreActionNodeList.addAnimatedNode(this.getChangePermitCardButton(regionName));
        }
        this.moreActionNodeList.addAnimatedNode(helpButton);
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
                Graphics.playSomeSound("Button");
                MatchController.this.selectionModel.selectNext();
                MatchController.this.tabPane.setStyle("-fx-background-image: url('/ClientPackage/View/GUIResources/Image/TabPaneShopPattern.png')");
            }
        });
        this.moreActionNodeList.addAnimatedNode(shopButton);
        shopButton.setTooltip(new Tooltip("Negozio"));
        this.gridPane.add(this.moreActionNodeList, 0, 2);
        GridPane.setHalignment(this.moreActionNodeList, HPos.LEFT);
        GridPane.setValignment(this.moreActionNodeList, VPos.BOTTOM);
        for (Node node : this.moreActionNodeList.getChildren())
            Graphics.addShadow(node);
        this.moreActionNodeList.visibleProperty().bind(this.nobilityPath.visibleProperty().not());
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
                Graphics.playSomeSound("Button");
                Action action = new FastActionChangePermitCardWithHelper(regionName);
                MatchController.this.clientController.doAction(action);
            }
        });
        jfxButton.setRotate(180);
        return jfxButton;
    }

    private void populateHamburgerMenu() {
        this.usersComboBox = new JFXComboBox<>();
        this.clientController.getSnapshot().getUsersInGame().forEach((s, baseUser) -> {
            if (!baseUser.getUsername().equals(this.clientController.getSnapshot().getCurrentUser().getUsername()) && !baseUser.isFakeUser())
                this.usersComboBox.getItems().add(baseUser.getUsername());
        });
        this.hamburgerMenu.add(this.usersComboBox, 1, 0);
        GridPane.setValignment(this.usersComboBox, VPos.CENTER);
        GridPane.setHalignment(this.usersComboBox, HPos.CENTER);
        this.usersComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                MatchController.this.populateField(MatchController.this.clientController.getSnapshot().getUsersInGame().get(newValue));
            }
        });
        this.usersComboBox.getSelectionModel().select(0);
        this.hamburgerMenu.prefHeightProperty().bind(this.gridPane.heightProperty().subtract(this.boardImageView.fitHeightProperty()));
        GridPane.setValignment(this.hamburgerMenu, VPos.BOTTOM);
    }

    private void populateField(BaseUser baseUser) {
        this.moneyLabel.setText(baseUser.getCoinPathPosition() + "");
        this.politicLabel.setText(baseUser.getPoliticCardNumber() + "");
        this.helperLabel.setText(baseUser.getHelpers().size() + "");
        this.victoryLabel.setText(baseUser.getNobilityPathPosition().getPosition() + "");
        this.permitListView.getItems().clear();
        this.permitListView.setItems(FXCollections.observableArrayList(this.clientController.populateListView(baseUser.getUsername())));
        this.nobilityLabel.setText(baseUser.getVictoryPathPosition() + "");
        this.userColorImageView.setImage(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "/Emporia/" + this.clientController.getSnapshot().getUsersInGame().get(baseUser.getUsername()).getUserColor().getColor() + ".png", 41, 53));
    }

    private void initHamburgerIcon() {
        this.hamburgerMenu.setVisible(false);
        this.hamburgerMenu.setPrefWidth(0);
        HamburgerSlideCloseTransition burgerTask = new HamburgerSlideCloseTransition(this.hamburgerIcon);
        burgerTask.setRate(-1);
        Tooltip.install(this.hamburgerIcon, new Tooltip("Info degli altri giocatori"));
        this.hamburgerIcon.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            burgerTask.setRate(burgerTask.getRate() * -1);
            if (burgerTask.getRate() == 1) {
                this.openSlider();
            } else {
                this.closeSlider();
            }
            Graphics.playSomeSound("Button");
            burgerTask.play();
        });
    }

    private void closeSlider() {
        this.hamburgerIcon.setTranslateX(0);
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1000),
                new KeyValue(this.hamburgerMenu.prefWidthProperty(), 0, Interpolator.EASE_OUT)));

        List<Node> nodes = this.hamburgerMenu.getChildren();
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
        this.hamburgerMenu.setVisible(true);
        this.hamburgerMenu.setScaleX(1);
        this.hamburgerMenu.setPrefWidth(0);
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500),
                new KeyValue(this.hamburgerMenu.prefWidthProperty(), this.backgroundImage.getFitWidth() / 5, Interpolator.EASE_OUT)));
        List<Node> nodes = this.hamburgerMenu.getChildren();
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
        this.hamburgerMenu.setOpacity(0.9);
    }

    private void createCity() {
        this.clientController.getSnapshot().getMap().getCity().forEach(city1 -> {
            this.createSingleCity(CityPosition.getX(city1), CityPosition.getY(city1), city1);
            if (!city1.getColor().getColor().equals(Constants.PURPLE)) {
                this.createBonus(city1.getBonus().getBonusURL(), city1.getBonus().getBonusInfo(), city1);
            }
        });
        this.createKingImage(CityPosition.getX(this.clientController.getSnapshot().getKing().getCurrentCity()), CityPosition
                .getY(this.clientController.getSnapshot().getKing().getCurrentCity()));
    }

    private void createBonus(ArrayList<String> bonusURL, ArrayList<String> bonusInfo, City city1) {
        for (int i = 0; i < bonusURL.size(); i++) {
            ImageView imageView = new ImageView();
            imageView.setImage(ImageLoader.getInstance().getImage(bonusURL.get(i)));
            imageView.setCache(true);
            imageView.fitHeightProperty().bind(this.background.heightProperty().multiply(0.05));
            imageView.fitWidthProperty().bind(this.background.widthProperty().divide(30));
            imageView.getStyleClass().add(city1.getCityName().getCityName());
            Label singleBonusInfo = new Label(bonusInfo.get(i));
            singleBonusInfo.setMouseTransparent(true);
            singleBonusInfo.getStyleClass().add("bonusLabel");
            singleBonusInfo.setLabelFor(imageView);
            singleBonusInfo.setWrapText(true);
            DropShadow ds = new DropShadow(15, Color.BLACK);
            imageView.setEffect(ds);
            imageView.setPreserveRatio(true);
            this.background.getChildren().add(imageView);
            this.background.getChildren().add(singleBonusInfo);
            imageView.layoutXProperty().bind(this.background.widthProperty().multiply(CityPosition.getX(city1)).add(i * 20));
            imageView.layoutYProperty().bind(this.background.heightProperty().multiply(CityPosition.getY(city1)));
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

                    if (MatchController.this.pulseBonus == null) {

                    } else {
                        MatchController.this.pulseBonus.setValue(true);
                    }
                    if (MatchController.this.needToSelectOldBonus) {
                        Graphics.playSomeSound("Button");
                        new Thread(() -> {
                            MatchController.this.clientController.getCityRewardBonus(city1);
                        }).start();
                        MatchController.this.needToSelectOldBonus = false;
                    }
                }
            });
        }
        Set<Node> labelWhereCycle = this.background.lookupAll(".bonusLabel");
        labelWhereCycle.forEach(Node::toFront);
    }

    private void createKingImage(double x, double y) {
        this.kingImage = new ImageView();
        this.kingImage.setImage(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "Crown.png", this.background.getHeight() * 0.07, this.background.getWidth() / 25));
        this.kingImage.setCache(true);
        this.kingImage.fitHeightProperty().bind(this.background.heightProperty().multiply(0.07));
        this.kingImage.fitWidthProperty().bind(this.background.widthProperty().divide(25));
        DropShadow ds = new DropShadow(15, Color.BLACK);
        this.kingImage.setEffect(ds);
        this.background.getChildren().add(this.kingImage);
        this.kingImage.layoutXProperty().bind(this.background.widthProperty().multiply(x).add(this.background.widthProperty().divide(20)));
        this.kingImage.layoutYProperty().bind(this.background.heightProperty().multiply(y));
        this.kingImage.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                MatchController.this.kingImage.setScaleX(1.2);
                MatchController.this.kingImage.setScaleY(1.2);
            }
        });
        this.kingImage.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                MatchController.this.kingImage.setScaleY(1);
                MatchController.this.kingImage.setScaleX(1);
            }
        });
        this.kingImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //showPopoverOnCity(city,imageView);
                Graphics.playSomeSound("Button");
                MatchController.this.showKingPopover(MatchController.this.kingImage);
            }
        });
    }

    private void showKingPopover(ImageView imageView) {
        this.politicCardforBuildWithKing.clear();
        PopOver popOver = new PopOver();
        VBox vBox = new VBox();
        HBox kingHBox = new HBox();
        ArrayList<Councilor> kingCouncilors = new ArrayList<>(this.currentSnapshot.getKing().getCouncil().getCouncil());
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
        for (PoliticCard politicCard : this.clientController.getSnapshot().getCurrentUser().getPoliticCards()) {
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
                        MatchController.this.politicCardforBuildWithKing.add(politicCard);
                    } else {
                        MatchController.this.politicCardforBuildWithKing.remove(politicCard);
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
                if (!MatchController.this.buildWithKingPhase.getValue()) {
                    popOver.hide();
                    MatchController.this.buildWithKingPhase.setValue(true);
                    MatchController.this.startBuildWithKing();
                    Graphics.playSomeSound("Button");
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
        Set<Node> cities = this.background.lookupAll(".cityImage");
        this.kingPathforBuild.clear();
        for (Node node : cities) {
            Graphics.scaleTransitionEffectCycle(node, 1.05f, 1.05f, this.pulseCity);
        }
    }

    private void createSingleCity(double layoutX, double layoutY, City city) {
        ImageView imageView = new ImageView();
        imageView.getStyleClass().add("cityImage");
        Image cityImage = ImageLoader.getInstance().getImage("/ClientPackage/View/GUIResources/Image/City/" + city.getColor().getColor().toLowerCase() + ".png", this.background.getHeight() * 0.17, this.background.getWidth() / 11);
        imageView.setImage(cityImage);
        imageView.setCache(true);
        imageView.fitHeightProperty().bind(this.background.heightProperty().multiply(0.17));
        imageView.fitWidthProperty().bind(this.background.widthProperty().divide(11));
        this.background.getChildren().add(imageView);
        imageView.layoutXProperty().bind(this.background.widthProperty().multiply(layoutX));
        imageView.layoutYProperty().bind(this.background.heightProperty().multiply(layoutY));
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
                if (!MatchController.this.buildWithKingPhase.get()) {
                    Graphics.playSomeSound("Button");
                    MatchController.this.showPopoverOnCity(city, imageView);
                } else {
                    Graphics.playSomeSound("Button");
                    MatchController.this.highlightCity(imageView, city);
                }
            }
        });
        ImageView cityName = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "City/Names/" + city.
                getCityName().getCityName() + "" + city.getColor().getColor() + ".png", imageView.getFitWidth(), imageView.getFitHeight()));
        cityName.setCache(true);
        cityName.setMouseTransparent(true);
        this.background.getChildren().add(cityName);
        cityName.layoutXProperty().bind(imageView.layoutXProperty());
        cityName.layoutYProperty().bind(imageView.layoutYProperty());
        cityName.fitWidthProperty().bind(imageView.fitWidthProperty());
        cityName.setPreserveRatio(true);
        HBox emporiumHBox = new HBox();
        emporiumHBox.setId(city.getCityName().getCityName());
        this.background.getChildren().add(emporiumHBox);
        emporiumHBox.layoutXProperty().bind(this.background.widthProperty().multiply(CityPosition.getX(city)));
        emporiumHBox.layoutYProperty().bind(this.background.heightProperty().multiply(CityPosition.getY(city)).add(imageView.fitHeightProperty()).subtract(30));
        emporiumHBox.toFront();
        for (Entry<String, BaseUser> userHashMap : this.clientController.getSnapshot().getUsersInGame().entrySet()) {
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
            this.kingPathforBuild.add(city);
        } else {
            this.kingPathforBuild.remove(city);
            imageView.setEffect(null);
        }
    }

    private void showPopoverOnCity(City city, ImageView imageView) {
        PopOver popOver = new PopOver();
        VBox cityInfoVBox = new VBox();
        HBox buttonHbox = new HBox();
        PermitCard[] permitCardSelected = {null};
        ImageView cityName = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "City/Names/" + city.
                getCityName().getCityName() + "" + city.getColor().getColor() + ".png"));
        cityName.setFitWidth(150);
        cityName.setPreserveRatio(true);
        JFXComboBox<String> jfxComboBox = new JFXComboBox<>();
        for (PermitCard permitCard :
                this.clientController.getSnapshot().getCurrentUser().getPermitCards()) {
            jfxComboBox.getItems().add(permitCard.getCityString());
        }
        jfxComboBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.intValue() != -1) {
                    permitCardSelected[0] = MatchController.this.clientController.getSnapshot().getCurrentUser().getPermitCards().get(newValue.intValue());
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
                Graphics.playSomeSound("Button");
                if (permitCardSelected[0] != null) {
                    Action action = new MainActionBuildWithPermitCard(city, permitCardSelected[0]);
                    MatchController.this.clientController.doAction(action);
                } else {
                    MatchController.this.guiView.onActionNotPossibleException(new ActionNotPossibleException("You need to select a Permit card!"));
                }
            }
        });
        buttonHbox.getChildren().addAll(jfxComboBox, jfxButton);
        buttonHbox.setSpacing(10);
        if (CityPosition.getX(city) > 0.5) {
            if (CityPosition.getY(city) > 0.5) {
                popOver.setArrowLocation(ArrowLocation.RIGHT_BOTTOM);
            } else {
                popOver.setArrowLocation(ArrowLocation.RIGHT_TOP);
            }
        } else {
            if (CityPosition.getY(city) > 0.5) {
                popOver.setArrowLocation(ArrowLocation.LEFT_BOTTOM);
            } else {
                popOver.setArrowLocation(ArrowLocation.LEFT_TOP);
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
        this.background.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            }
        });
    }

    private void initController() {
        this.shopController.setClientController(this.clientController, this.guiView);
        this.shopController.setMatchController(this);
        this.nobilityPathController.setClientController(this.clientController, this.guiView);
        this.nobilityPathController.setMatchController(this);
    }

    private void initPermitButton() {
        this.coastPermitButton.setOnMouseClicked(new PermitButtonHandler(RegionName.COAST));
        this.hillPermitButton.setOnMouseClicked(new PermitButtonHandler(RegionName.HILL));
        this.mountainPermitButton.setOnMouseClicked(new PermitButtonHandler(RegionName.MOUNTAIN));
    }

    private void createOverlay() {
        if (this.bottomPane.getChildren().contains(this.hiddenSidesPane)) {
            this.bottomPane.getChildren().remove(this.hiddenSidesPane);
        }
        VBox vbox = new VBox();
        HBox hbox1 = new HBox();
        HBox hbox2 = new HBox();
        for (RegionName regionName : RegionName.values()) {
            HBox hBox = new HBox();
            try {
                ArrayList<Councilor> councilors = this.currentSnapshot.getCouncil(regionName);
                ArrayList<ImageView> imageViews = new ArrayList<>();
                for (int i = 0; i < councilors.size(); i++) {
                    ImageView imageView = new ImageView();
                    try {
                        imageView.setImage(ImageLoader.getInstance().getImage(councilors.get(i).getColor().getImageUrl(), this.background.getWidth() / 10, vbox.getPrefHeight() / 3));
                        imageView.setCache(true);
                        imageView.fitWidthProperty().bind(this.background.prefWidthProperty().divide(10));
                        imageView.fitHeightProperty().bind(vbox.prefHeightProperty().divide(3));
                        imageView.setPreserveRatio(true);
                        imageViews.add(imageView);
                    } catch (IllegalArgumentException e) {
                    }
                    hBox.getChildren().add(imageView);
                }
                this.councilHashMap.put(regionName, imageViews);
                hbox1.getChildren().add(hBox);
                hBox.setOnMouseClicked(new CouncilorHandler(hBox, regionName, this, this.clientController, null));
            } catch (CouncilNotFoundException e) {
                e.printStackTrace();
            }
        }
        hbox1.prefWidthProperty().bind(this.bottomPane.prefWidthProperty());
        hbox1.spacingProperty().bind(this.bottomPane.prefWidthProperty().divide(5));
        hbox1.setAlignment(Pos.CENTER);
        HBox kingHBox = new HBox();
        ArrayList<Councilor> kingCouncilors = new ArrayList<>(this.currentSnapshot.getKing().getCouncil().getCouncil());
        for (Councilor councilor : kingCouncilors) {
            ImageView imageView = new ImageView();
            try {
                imageView.setImage(ImageLoader.getInstance().getImage(councilor.getColor().getImageUrl()));
                imageView.setCache(true);
                imageView.fitWidthProperty().bind(this.background.prefWidthProperty().divide(10));
                imageView.fitHeightProperty().bind(vbox.prefHeightProperty().divide(3));
                imageView.setPreserveRatio(true);
                this.kingCouncil.add(imageView);
            } catch (IllegalArgumentException e) {
            }
            kingHBox.getChildren().add(imageView);
        }
        hbox2.getChildren().add(kingHBox);
        kingHBox.setAlignment(Pos.CENTER);
        kingHBox.setOnMouseClicked(new CouncilorHandler(hbox2, null, this, this.clientController, this.currentSnapshot.getKing()));
        hbox2.setAlignment(Pos.CENTER);
        HBox permitHBox = new HBox();
        for (RegionName regionName : RegionName.values()) {
            HBox hboxTmp = new HBox();
            hboxTmp.getStyleClass().add(regionName.name());
            this.currentSnapshot.getVisibleRegionPermitCard(regionName).forEach(permitCard -> {
                GridPane gridPane = new GridPane();
                gridPane.prefWidthProperty().bind(this.bottomPane.prefWidthProperty().divide(15));
                gridPane.prefHeightProperty().bind(this.bottomPane.prefHeightProperty().divide(2));
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
                gridPane.setOnMouseClicked(new PermitCardHandler(permitCard, this, this.clientController, this.needToSelectPermitCard));
                PermitPopOverHandler permitPopOverHandler =
                        new PermitPopOverHandler(this.clientController, regionName, this.currentSnapshot
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
        permitHBox.spacingProperty().bind(this.background.prefWidthProperty().divide(6));
        permitHBox.setAlignment(Pos.CENTER);
        permitHBox.prefHeightProperty().bind(vbox.prefHeightProperty().divide(3));
        permitHBox.prefWidthProperty().bind(vbox.prefWidthProperty());
        hbox2.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(hbox2, hbox1, permitHBox);
        vbox.spacingProperty().bind(this.bottomPane.prefHeightProperty().divide(8));
        StackPane.setMargin(vbox, new Insets(20, 0, 20, 20));
        this.bottomPane.getChildren().clear();
        this.bottomPane.getChildren().add(vbox);
        vbox.prefWidthProperty().bind(this.bottomPane.prefWidthProperty());
        vbox.prefHeightProperty().bind(this.bottomPane.prefHeightProperty());
        this.bottomPane.prefHeightProperty().bind(this.background.prefHeightProperty().divide(5));
        this.bottomPane.prefWidthProperty().bind(this.background.prefWidthProperty());
        this.bottomPane.setOpacity(0.8);
    }

    @Override
    public void setMyTurn(boolean value, SnapshotToSend snapshot) {
        this.myTurn = value;
        currentSnapshot = snapshot;
        this.turnFinished(this.myTurn);
        this.disableAllEffect();
        this.updateView();
    }

    private void disableAllEffect() {
        if (this.pulseBonus != null) {
            this.pulseBonus.setValue(true);
        }
        if (this.pulseCity != null) {
            this.finishKingPhase();
        }
        if (this.needToSelectPermitCard != null) {
            this.needToSelectPermitCard.setValue(false);
            this.needToSelectOldPermitCard.setValue(false);
            this.stopPulseOldPermitCard.setValue(true);
            this.stopPulsePermitCard.setValue(true);
            this.onSelectOldPermitCard();
            this.hidePermitCardHightLight(".visiblePermitCard", this.bottomPane);
        }
    }

    @Override
    public void onStartMarket() {
        this.selectionModel.selectNext();
        if (this.tabPane.getStyle().contains("-fx-background-image: url('/ClientPackage/View/GUIResources/Image/TabPaneMatchPattern.png')")) {
            this.tabPane.setStyle(this.tabPane.getStyle().replace("-fx-background-image: url('/ClientPackage/View/GUIResources/Image/TabPaneMatchPattern.png')",
                    "-fx-background-image: url('/ClientPackage/View/GUIResources/Image/TabPaneShopPattern.png')"));
        }
    }

    @Override
    public void onStartBuyPhase() {
    }

    @Override
    public void onFinishMarket() {
        this.selectionModel.selectFirst();
        if (this.tabPane.getStyle().contains("-fx-background-image: url('/ClientPackage/View/GUIResources/Image/TabPaneShopPattern.png'")) {
            this.tabPane.setStyle(this.tabPane.getStyle().replace("-fx-background-image: url('/ClientPackage/View/GUIResources/Image/TabPaneShopPattern.png')",
                    "-fx-background-image: url('/ClientPackage/View/GUIResources/Image/TabPaneMatchPattern.png')"));
        }
    }

    @Override
    public void onResizeHeight(double height, double width) {
    }

    @Override
    public void onResizeWidth(double width, double height) {
    }

    @Override
    public void selectPermitCard() {
        this.needToSelectPermitCard.setValue(true);
        this.stopPulsePermitCard.setValue(false);
        Set<Node> nodes = this.bottomPane.lookupAll(".visiblePermitCard");
        nodes.forEach(node -> this.highlightPermitCard(node, this.stopPulsePermitCard));
        this.bottomPane.setVisible(true);
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
        this.needToSelectOldBonus = true;
        this.stopPulseBonus.setValue(false);
        this.clientController.getSnapshot().getCurrentUser().getUsersEmporium().forEach(this::pulseBonus);
    }

    @Override
    public void moveKing(ArrayList<City> kingPath) {
        if (kingPath.size() > 1) {
            Timeline timeline = new Timeline();
            this.kingImage.layoutXProperty().unbind();
            this.kingImage.layoutYProperty().unbind();
            kingPath.remove(0);
            kingPath.forEach(city1 -> {
                KeyValue keyValueX = new KeyValue(this.kingImage.layoutXProperty(), this.background.getWidth() * CityPosition.getX(city1), Interpolator.EASE_BOTH);
                KeyValue keyValueY = new KeyValue(this.kingImage.layoutYProperty(), this.background.getHeight() * CityPosition.getY(city1), Interpolator.EASE_BOTH);
                KeyFrame keyFrame = new KeyFrame(Duration.millis(900 * kingPath.indexOf(city1)), keyValueX, keyValueY);
                timeline.getKeyFrames().add(keyFrame);
            });
            timeline.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    MatchController.this.kingImage.layoutXProperty().bind(MatchController.this.background.widthProperty().multiply(CityPosition.getX(kingPath.get(kingPath.size() - 1))).add(MatchController.this.background.widthProperty().divide(20)));
                    MatchController.this.kingImage.layoutYProperty().bind(MatchController.this.background.heightProperty().multiply(CityPosition.getY(kingPath.get(kingPath.size() - 1))));
                }
            });
            timeline.play();
        }
    }

    @Override
    public void selectOldPermitCardBonus() {
        this.needToSelectOldPermitCard.setValue(true);
        this.stopPulsePermitCard.setValue(false);
        Set<Node> permitCards = this.gridPane.lookupAll(".myPermitCard");
        permitCards.forEach(node -> this.highlightPermitCard(node, this.stopPulseOldPermitCard));
        this.oldPermitCardNodeList.animateList();
        this.handHBox.setVisible(true);
        this.hamburgerMenu.setVisible(false);
        this.nobilityPath.setVisible(false);
    }

    private void pulseBonus(City city1) {
        if (!city1.getColor().getColor().equals(Constants.PURPLE)) {
            if (this.pulseBonus == null) {
                this.pulseBonus = new SimpleBooleanProperty(false);
            } else {
                this.pulseBonus.setValue(false);
            }
            Set<Node> nodes = this.background.lookupAll("." + city1.getCityName().getCityName());
            nodes.forEach(node -> Graphics.scaleTransitionEffectCycle(node, 1.2f, 1.2f, this.pulseBonus));
        }
    }

    private void turnFinished(boolean thisTurn) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (thisTurn) {
                    MatchController.this.startTimer();
                    Graphics.notification("E' il tuo turno!");
                    MatchController.this.turnImage.setImage(new Image(Constants.IMAGE_PATH + "/turnYes1.png"));
                    Tooltip.install(MatchController.this.turnImage, new Tooltip("E' il tuo turno"));
                } else {
                    MatchController.this.cancelTimer();
                    Graphics.notification("Turno finito!");
                    MatchController.this.turnImage.setImage(new Image(Constants.IMAGE_PATH + "/turnNo1.png"));
                    Tooltip.install(MatchController.this.turnImage, new Tooltip("Non è il tuo turno"));
                }

            }
        });
    }

    private void cancelTimer() {
        if (this.timer != null) {
            this.timer.cancel();
        }
        this.timerIndicator.setValue(0);
    }

    private void startTimer() {
        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(new TimerProgress(this.timerIndicator, this, this.clientController), 0, 1000);
    }


    private void reprintCouncilor() {
        for (RegionName regionName : RegionName.values()) {
            ArrayList<Councilor> councilors = null;
            try {
                councilors = this.clientController.getSnapshot().getCouncil(regionName);
                ArrayList<ImageView> imageView = this.councilHashMap.get(regionName);
                for (int i = 0; i < councilors.size(); i++) {
                    imageView.get(i).setImage(ImageLoader.getInstance().getImage(councilors.get(i).getColor().getImageUrl(), imageView.get(i).getBoundsInLocal().getWidth(), imageView.get(i).getBoundsInParent().getHeight()));
                }
            } catch (CouncilNotFoundException e) {
                e.printStackTrace();
            }

        }
        ArrayList<Councilor> kingCouncilArray = new ArrayList<>(this.clientController.getSnapshot().getKing().getCouncil().getCouncil());
        for (int i = 0; i < this.kingCouncil.size(); i++) {
            this.kingCouncil.get(i).setImage(ImageLoader.getInstance().getImage(kingCouncilArray.get(i).getColor().getImageUrl(), this.kingCouncil.get(i).getFitWidth(), this.kingCouncil.get(i).getFitHeight()));
        }
    }


    @Override
    public void updateView() {
        currentSnapshot = this.clientController.getSnapshot();
        this.nobilityPathText.setText(this.currentSnapshot.getCurrentUser().getNobilityPathPosition().getPosition() + "");
        this.richPathText.setText(this.currentSnapshot.getCurrentUser().getCoinPathPosition() + "");
        this.helperText.setText(this.currentSnapshot.getCurrentUser().getHelpers().size() + "");
        this.victoryPathText.setText(this.currentSnapshot.getCurrentUser().getVictoryPathPosition() + "");
        this.mainActionText.setText(this.currentSnapshot.getCurrentUser().getMainActionCounter() + "");
        this.fastActionText.setText(this.currentSnapshot.getCurrentUser().getFastActionCounter() + "");
        this.reprintCouncilor();
        this.reprintPermitCard();
        this.setEmporiaVisibility();
        this.populateField(this.clientController.getSnapshot().getUsersInGame().get(this.usersComboBox.getValue()));
        this.createHand();
        this.creteOldPermitCard();
        if (this.needToSelectOldPermitCard.get() && this.myTurn)
            this.selectOldPermitCardBonus();
    }

    private void setEmporiaVisibility() {
        for (Entry<String, BaseUser> user : this.clientController.getSnapshot().getUsersInGame().entrySet()) {
            for (City city : user.getValue().getUsersEmporium()) {
                if (user.getValue().getUsersEmporium() != null && city != null) {
                    HBox cityHBox = (HBox) this.background.lookup("#" + city.getCityName().getCityName());
                    ImageView userEmporium = (ImageView) cityHBox.lookup("#" + user.getKey());
                    userEmporium.setVisible(true);
                }
            }
        }
    }


    private void reprintPermitCard() {
        for (RegionName regionName : RegionName.values()) {
            Node region = this.bottomPane.lookup("." + regionName.name());
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
                PermitCard permitCardTmp = this.clientController.getSnapshot().getVisibleRegionPermitCard(regionName).get(i);
                Label label = (Label) labelsList.get(i);
                label.setText(permitCardTmp.getCityString());
                GridPane gridPane = (GridPane) gridPanesList.get(i);
                gridPane.setOnMouseClicked(new PermitCardHandler(permitCardTmp, this, this.clientController, this.needToSelectPermitCard));
                PermitPopOverHandler permitPopOverHandler = new PermitPopOverHandler(this.clientController, regionName, i, this);
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

    /**
     * Called on click on helper image
     *
     * @param event
     */
    public void buyHelper(Event event) {
        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Graphics.playSomeSound("Button");
                Action action = new FastActionMoneyForHelper();
                MatchController.this.clientController.doAction(action);
            }
        };

        String buttonText = "Compra Aiutanti";
        String infoLabel = "Azione veloce: Compra un aiutante per tre monete!";
        this.showDefaultPopOver(eventHandler, infoLabel, buttonText, (Node) event.getSource());
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
                Graphics.playSomeSound("Button");
                Action action = new FastActionNewMainAction();
                MatchController.this.clientController.doAction(action);
            }
        };
        String buttonText = "Compra Azione";
        String infoLabel = "Azione veloce: Ottieni un azione principale per 3 aiutanti";
        this.showDefaultPopOver(eventHandler, infoLabel, buttonText, (Node) event.getSource());
    }

    public void showMore() {
        if (this.bottomPane.isVisible()) {
            this.bottomPane.setVisible(false);
        } else {
            this.bottomPane.setVisible(true);
        }
    }

    public void showLess() {
        if (this.nobilityPath.isVisible()) {
            this.handHBox.setVisible(true);
            this.nobilityPath.setVisible(false);
        } else {
            this.handHBox.setVisible(false);
            this.nobilityPath.setVisible(true);
        }
    }

    public void onSelectPermitCard(PermitCard permitCard) {
        this.hidePermitCardHightLight(".visiblePermitCard", this.bottomPane);
        this.needToSelectPermitCard.setValue(false);
        this.stopPulsePermitCard.setValue(true);
        this.clientController.onSelectPermitCard(permitCard);
    }

    private void hidePermitCardHightLight(String selector, Pane container) {
        Set<Node> nodes = container.lookupAll(selector);
        nodes.forEach(node -> node.setEffect(null));
    }

    public void finishKingPhase() {
        Set<Node> nodes = this.background.lookupAll(".cityImage");
        nodes.forEach(node -> {
            node.setEffect(null);
        });
        this.politicCardforBuildWithKing.clear();
        this.kingPathforBuild.clear();
        this.buildWithKingPhase.set(false);
    }

    public void onSelectOldPermitCard() {
        this.needToSelectOldPermitCard.setValue(false);
        this.stopPulsePermitCard.setValue(true);
        this.stopPulseOldPermitCard.setValue(true);
        this.hidePermitCardHightLight(".myPermitCard", this.gridPane);
    }

    public Pane getBackground() {
        return this.background;
    }

    public boolean getBuildWithKingPhase() {
        return this.buildWithKingPhase.get();
    }

    public ArrayList<City> getKingPathforBuild() {
        return this.kingPathforBuild;
    }

    public ArrayList<PoliticCard> getPoliticCardforBuildWithKing() {
        return this.politicCardforBuildWithKing;
    }

    private class PermitButtonHandler implements EventHandler<MouseEvent> {

        private final RegionName regionName;

        PermitButtonHandler(RegionName regionName) {
            this.regionName = regionName;
        }

        @Override
        public void handle(MouseEvent event) {
            Action action = new FastActionChangePermitCardWithHelper(this.regionName);
            MatchController.this.clientController.doAction(action);
        }
    }

}
