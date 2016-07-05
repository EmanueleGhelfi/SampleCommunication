package ClientPackage.View.GUIResources.Class;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.GUIResources.CustomComponent.ImageLoader;
import ClientPackage.View.GeneralView.GUIView;
import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import CommonModel.GameModel.City.City;
import CommonModel.GameModel.Council.Helper;
import CommonModel.GameModel.Market.BuyableWrapper;
import CommonModel.Snapshot.SnapshotToSend;
import Utilities.Class.Constants;
import Utilities.Class.Graphics;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXButton.ButtonType;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.controlsfx.control.PopOver;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Emanuele on 30/05/2016.
 */
public class ShopController implements BaseController {

    private ClientController clientController;
    private GUIView guiView;
    private final ImageView backgroundImage = new ImageView();
    private final ImageView politicCardDeck = new ImageView();
    private final ImageView permitCardDeck = new ImageView();
    private final ImageView helperDeck = new ImageView();
    private final TilePane buyPane = new TilePane();
    private final TilePane sellPane = new TilePane();
    private final ScrollPane buyScrollPane = new ScrollPane();
    private final ScrollPane sellScrollPane = new ScrollPane();
    private final VBox buyVBox = new VBox();
    private Button innerButton;
    private PopOver innerPopOver;
    private Pane paneWhereShowPopOver = new Pane();
    private ArrayList<BuyableWrapper> sellList = new ArrayList<>();
    private final ArrayList<BuyableWrapper> temporarySellList = new ArrayList<>();
    private ArrayList<BuyableWrapper> buyList = new ArrayList<>();
    private final ArrayList<BuyableWrapper> toBuy = new ArrayList<>();
    private final ArrayList<BuyableWrapper> trueList = new ArrayList<>();
    private final ShopController shopController = this;
    private ImageView buyIt = new ImageView();
    private ImageView finishShop = new ImageView();
    private final JFXButton finishShopButton = new JFXButton();
    private final JFXButton buyItButton = new JFXButton("");
    private boolean marketPhase;
    private boolean sellPhase;
    private boolean buyPhase;
    private MatchController matchController;

    @FXML
    private GridPane shop;
    @FXML
    private Pane paneBackground;

    @Override
    public void setClientController(ClientController clientController, GUIView guiView) {
        this.clientController = clientController;
        this.guiView = guiView;
        guiView.registerBaseController(this);
        this.backgroundImage.setImage(ImageLoader.getInstance().getImage("/ClientPackage/View/GUIResources/Image/ShopBackground.png", this.backgroundImage.getFitWidth(), this.backgroundImage.getFitWidth()));
        this.setBackground();
        this.updateView();
        this.createDeck();
        this.setArrow();
        this.setMarketPane();
    }

    private void setArrow() {
        JFXButton backArrow = new JFXButton();
        backArrow.setTooltip(new Tooltip("Mappa"));
        this.paneBackground.getChildren().add(backArrow);
        ImageView arrowImageView = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "/Arrow.png", this.backgroundImage.getFitWidth() / 6, this.backgroundImage.getFitHeight() / 6));
        arrowImageView.setCache(true);
        arrowImageView.setPreserveRatio(true);
        backArrow.setGraphic(arrowImageView);
        arrowImageView.fitWidthProperty().bind(this.backgroundImage.fitWidthProperty().divide(6));
        arrowImageView.fitHeightProperty().bind(this.backgroundImage.fitHeightProperty().divide(6));
        backArrow.prefWidthProperty().bind(this.backgroundImage.fitWidthProperty().divide(6));
        backArrow.prefHeightProperty().bind(this.backgroundImage.fitHeightProperty().divide(6));
        backArrow.layoutXProperty().bind(this.paneBackground.widthProperty().multiply(0.3964));
        backArrow.layoutYProperty().bind(this.paneBackground.heightProperty().multiply(0.7619));
        backArrow.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // simulate finish market becuse it change background
                Graphics.playSomeSound("Button");
                ShopController.this.matchController.onFinishMarket();
            }
        });
        BooleanProperty stopPulse = new SimpleBooleanProperty(false);
        backArrow.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stopPulse.setValue(false);
                Graphics.scaleTransitionEffectCycle(backArrow, 1.2f, 1.2f, stopPulse);
            }
        });
        backArrow.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stopPulse.setValue(true);
            }
        });
    }

    private void setMarketPane() {
        this.sellPane.setPrefColumns(2);
        this.sellScrollPane.prefHeightProperty().bind(this.paneBackground.heightProperty().divide(2));
        this.sellScrollPane.prefWidthProperty().bind(this.paneBackground.widthProperty().divide(3));
        this.sellScrollPane.setContent(this.sellPane);
        this.buyPane.setPrefColumns(2);
        this.buyScrollPane.prefHeightProperty().bind(this.paneBackground.widthProperty().divide(3));
        this.buyScrollPane.prefWidthProperty().bind(this.paneBackground.heightProperty().divide(3));
        this.buyScrollPane.setContent(this.buyPane);
        this.finishShop = new ImageView(new Image(Constants.IMAGE_PATH + "/Check.png"));
        this.buyIt = new ImageView(new Image(Constants.IMAGE_PATH + "/Cart.png"));
        this.finishShopButton.setGraphic(this.finishShop);
        this.buyItButton.setGraphic(this.buyIt);
        HBox bottomHBox = new HBox();
        bottomHBox.setAlignment(Pos.CENTER);
        bottomHBox.getChildren().addAll(this.finishShopButton, this.buyItButton);
        this.buyVBox.getChildren().addAll(this.buyScrollPane, bottomHBox);
    }

    private void setBackground() {
        this.paneBackground.getChildren().add(this.backgroundImage);
        this.backgroundImage.setPreserveRatio(true);
        this.backgroundImage.fitHeightProperty().bind(this.shop.heightProperty());
        this.backgroundImage.fitWidthProperty().bind(this.shop.widthProperty());
        this.shop.prefWidthProperty().bind(this.paneBackground.prefWidthProperty());
        this.shop.prefHeightProperty().bind(this.paneBackground.prefHeightProperty());
        this.paneBackground.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            }
        });
        this.backgroundImage.fitWidthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            }
        });
        this.backgroundImage.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) {
                ShopController.this.paneBackground.setPrefWidth(newValue.getWidth());
                ShopController.this.paneBackground.setPrefHeight(newValue.getHeight());

            }
        });
        GridPane.setRowSpan(this.backgroundImage, 2);
        GridPane.setColumnSpan(this.backgroundImage, 2);
        this.paneWhereShowPopOver = new Pane();
        this.paneWhereShowPopOver.layoutXProperty().bind(this.paneBackground.prefWidthProperty().multiply(0.4957));
        this.paneWhereShowPopOver.layoutYProperty().bind(this.paneBackground.prefHeightProperty().multiply(0.3045));
        this.paneWhereShowPopOver.prefWidthProperty().bind(this.paneBackground.prefWidthProperty().multiply(0.01));
        this.paneWhereShowPopOver.prefHeightProperty().bind(this.paneBackground.prefHeightProperty().multiply(0.01));
        this.paneBackground.getChildren().add(this.paneWhereShowPopOver);
        this.innerButton = new Button();
        this.innerButton.setStyle("-fx-background-color: transparent; -fx-cursor: crosshair;");
        this.innerButton.layoutXProperty().bind(this.paneBackground.prefWidthProperty().multiply(0.3504));
        this.innerButton.layoutYProperty().bind(this.paneBackground.prefHeightProperty().multiply(0.1455));
        this.innerButton.prefWidthProperty().bind(this.paneBackground.prefWidthProperty().multiply(0.5208 - 0.3604));
        this.innerButton.prefHeightProperty().bind(this.paneBackground.prefHeightProperty().multiply(0.5530 - 0.1455));
        this.paneBackground.getChildren().addAll(this.innerButton);
    }


    private void onBuy() {
        Runnable runnable = () -> {
            this.clientController.onBuy((ArrayList<BuyableWrapper>) this.toBuy.clone());
            this.toBuy.clear();
        };
        new Thread(runnable).start();
    }

    private void onSell() {
        Runnable runnable = () -> {
            if (this.trueList.size() > 0) {
                this.clientController.sendSaleItem((ArrayList<BuyableWrapper>) this.trueList.clone());
                this.trueList.clear();
            }
        };
        new Thread(runnable).start();
    }

    @Override
    public void updateView() {
        this.updateList();
    }

    private void updateList() {
        this.sellList = new ArrayList<>();
        SnapshotToSend snapshotTosend = this.clientController.getSnapshot();
        this.buyList = snapshotTosend.getMarketList();
        for (PoliticCard politicCard : snapshotTosend.getCurrentUser().getPoliticCards()) {
            BuyableWrapper buyableWrapperTmp = new BuyableWrapper(politicCard, snapshotTosend.getCurrentUser().getUsername());
            this.sellList.add(buyableWrapperTmp);
        }
        for (PermitCard permitCard : snapshotTosend.getCurrentUser().getPermitCards()) {
            BuyableWrapper buyableWrapperTmp = new BuyableWrapper(permitCard, snapshotTosend.getCurrentUser().getUsername());
            this.sellList.add(buyableWrapperTmp);
        }
        for (Helper helper : snapshotTosend.getCurrentUser().getHelpers()) {
            BuyableWrapper buyableWrapper = new BuyableWrapper(helper, snapshotTosend.getCurrentUser().getUsername());
            this.sellList.add(buyableWrapper);
        }
        for (Iterator<BuyableWrapper> itr = this.buyList.iterator(); itr.hasNext(); ) {
            BuyableWrapper buyableWrapper = itr.next();
            if (buyableWrapper.getUsername().equals(snapshotTosend.getCurrentUser().getUsername())) {
                this.sellList.remove(buyableWrapper);
                this.sellList.add(buyableWrapper);
                itr.remove();
            }
        }
        this.populateSellAndBuyPane();
    }

    private void populateSellAndBuyPane() {
        this.sellPane.getChildren().clear();
        this.buyPane.getChildren().clear();
        for (BuyableWrapper buyableWrapper : this.temporarySellList) {
            this.sellPane.getChildren().add(this.addSalableItems(buyableWrapper));
        }
        for (BuyableWrapper buyableWrapper : this.buyList) {
            this.buyPane.getChildren().add(this.addBuyableItems(buyableWrapper));
        }
    }

    private void createDeck() {
        this.politicCardDeck.setImage(ImageLoader.getInstance().getImage("/ClientPackage/View/GUIResources/Image/PoliticCardDistorted.png"));
        this.permitCardDeck.setImage(ImageLoader.getInstance().getImage("/ClientPackage/View/GUIResources/Image/PermitCardsDistorted.png"));
        this.helperDeck.setImage(ImageLoader.getInstance().getImage("/ClientPackage/View/GUIResources/Image/HelperDistorted.png"));
        this.paneBackground.getChildren().addAll(this.politicCardDeck, this.permitCardDeck, this.helperDeck);
        this.politicCardDeck.layoutXProperty().bind(this.paneBackground.widthProperty().multiply(0.3645));
        this.politicCardDeck.layoutYProperty().bind(this.paneBackground.heightProperty().multiply(0.58));
        this.permitCardDeck.layoutXProperty().bind(this.paneBackground.widthProperty().multiply(0.4437));
        this.permitCardDeck.layoutYProperty().bind(this.paneBackground.heightProperty().multiply(0.58));
        this.helperDeck.layoutXProperty().bind(this.paneBackground.widthProperty().multiply(0.5218));
        this.helperDeck.layoutYProperty().bind(this.paneBackground.heightProperty().multiply(0.6));
        this.politicCardDeck.fitWidthProperty().bind(this.paneBackground.widthProperty().divide(10));
        this.politicCardDeck.fitHeightProperty().bind(this.paneBackground.heightProperty().divide(10));
        this.permitCardDeck.fitWidthProperty().bind(this.paneBackground.widthProperty().divide(10));
        this.permitCardDeck.fitHeightProperty().bind(this.paneBackground.heightProperty().divide(10));
        this.helperDeck.fitWidthProperty().bind(this.paneBackground.widthProperty().divide(15));
        this.helperDeck.fitHeightProperty().bind(this.paneBackground.heightProperty().divide(15));
        Tooltip.install(this.politicCardDeck, new Tooltip("Carte politica in vendita"));
        Tooltip.install(this.permitCardDeck, new Tooltip("Carte permesso in vendita"));
        Tooltip.install(this.helperDeck, new Tooltip("Aiutanti in vendita"));
        Graphics.addBorder(this.politicCardDeck);
        Graphics.addBorder(this.helperDeck);
        Graphics.addBorder(this.permitCardDeck);
    }

    private void settingDeckActions() {
        this.politicCardDeck.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (ShopController.this.sellPhase) {
                    Graphics.playSomeSound("Button");
                    ImageView imageClicked = (ImageView) event.getTarget();
                    ShopController.this.shopPopOver(imageClicked, PoliticCard.class);
                }
            }
        });
        this.permitCardDeck.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (ShopController.this.sellPhase) {
                    Graphics.playSomeSound("Button");
                    ImageView imageClicked = (ImageView) event.getTarget();
                    ShopController.this.shopPopOver(imageClicked, PermitCard.class);
                }
            }
        });
        this.helperDeck.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Graphics.playSomeSound("Button");
                if (ShopController.this.sellPhase) {
                    ImageView imageClicked = (ImageView) event.getTarget();
                    ShopController.this.shopPopOver(imageClicked, Helper.class);
                }
            }
        });
    }

    private void shopPopOver(ImageView imageClicked, Class objectClass) {
        this.temporarySellList.clear();
        this.sellList.forEach(buyableWrapper -> {
            if (buyableWrapper.getBuyableObject().getClass().equals(objectClass)) {
                this.temporarySellList.add(buyableWrapper);
            }
        });
        this.populateSellAndBuyPane();
        PopOver popOver = new PopOver();
        popOver.setContentNode(this.sellScrollPane);
        popOver.show(imageClicked);
    }

    private void sellToInnerKeeper() {
        this.innerPopOver = new PopOver();
        VBox innerVBox = new VBox();
        Label innerLabel = new Label("Sei sicuro di voler vendere questi oggetti?");
        JFXButton innerPopOverButton = new JFXButton("Sì, mi assumo ogni responsabilità!");
        innerPopOverButton.setBackground(new Background(new BackgroundFill(Paint.valueOf("#FF512D"), null, null)));
        innerPopOverButton.setTextFill(Paint.valueOf("WHITE"));
        innerVBox.getChildren().addAll(innerLabel, innerPopOverButton);
        innerVBox.setPadding(new Insets(20, 20, 20, 20));
        this.innerPopOver.setContentNode(innerVBox);
        this.innerPopOver.show(this.paneWhereShowPopOver);
        innerPopOverButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Graphics.playSomeSound("Button");
                ShopController.this.innerPopOver.hide();
                ShopController.this.onSell();
                ShopController.this.clientController.sendFinishSellPhase();
                ShopController.this.waitingForBuying();
            }
        });
    }

    private void waitingForBuying() {
        this.sellPhase = false;
        this.innerPopOver = new PopOver();
        StackPane stackPane = new StackPane(new Label("Aspetta il tuo turno, villano."));
        stackPane.setPadding(new Insets(20, 20, 20, 20));
        this.innerPopOver.setContentNode(stackPane);
        this.innerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Graphics.playSomeSound("Button");
                ShopController.this.innerPopOver.show(ShopController.this.paneWhereShowPopOver);
            }
        });

    }

    private void createBuyingPopOver() {
        this.innerPopOver = new PopOver();
        if (this.buyPhase) {
            this.finishShopButton.setGraphic(this.finishShop);
            this.finishShopButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Graphics.playSomeSound("Button");
                    ShopController.this.innerPopOver.hide();
                    ShopController.this.onBuy();
                    //toBuy.clear();
                    ShopController.this.buyPhase = false;
                    ShopController.this.clientController.sendFinishedBuyPhase();
                }
            });
            this.buyItButton.setGraphic(this.buyIt);
            this.buyItButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Graphics.playSomeSound("Button");
                    ShopController.this.onBuy();
                    //toBuy.clear();
                }
            });
            this.buyIt.setFitHeight(40);
            this.buyIt.setFitWidth(40);
            this.buyIt.setPreserveRatio(true);
            this.finishShop.setFitWidth(40);
            this.finishShop.setFitHeight(40);
            this.innerPopOver.setContentNode(this.buyVBox);
            this.innerPopOver.show(this.paneWhereShowPopOver);
        } else {
            this.nothingToDoPopOver();
        }
    }

    private void nothingToDoPopOver() {
        if (this.paneWhereShowPopOver != null) {
            PopOver popOverToShow = new PopOver();
            Pane internalPopOverPane = new Pane();
            internalPopOverPane.getChildren().add(new Label("Bottega chiusa, viandante!"));
            internalPopOverPane.setPadding(new Insets(20, 20, 20, 20));
            popOverToShow.setContentNode(internalPopOverPane);
            popOverToShow.show(this.paneWhereShowPopOver);
        }
    }

    @Override
    public void onStartMarket() {
        Graphics.notification("Start Market");
        this.marketPhase = true;
        this.sellPhase = true;
        this.buyList.clear();
        this.sellList.clear();
        this.toBuy.clear();
        this.temporarySellList.clear();
        this.trueList.clear();
        this.settingDeckActions();
        this.updateView();
        this.innerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (ShopController.this.sellPhase)
                    Graphics.playSomeSound("Button");
                ShopController.this.sellToInnerKeeper();
            }
        });
    }

    @Override
    public void onStartBuyPhase() {
        this.buyPhase = true;
        this.createBuyingPopOver();
        this.settingDeckActions();
        this.politicCardDeck.setOnMouseClicked(null);
        this.permitCardDeck.setOnMouseClicked(null);
        this.helperDeck.setOnMouseClicked(null);
        this.innerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Graphics.playSomeSound("Button");
                ShopController.this.createBuyingPopOver();
            }
        });
        Graphics.notification("Start Buy Phase");
    }

    @Override
    public void onFinishMarket() {
        Graphics.notification("Finish Market");
        this.buyList.clear();
        this.sellList.clear();
        this.temporarySellList.clear();
        this.trueList.clear();
        this.politicCardDeck.setOnMouseClicked(null);
        this.permitCardDeck.setOnMouseClicked(null);
        this.helperDeck.setOnMouseClicked(null);
        this.innerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Graphics.playSomeSound("Button");
                ShopController.this.nothingToDoPopOver();
            }
        });
    }

    @Override
    public void setMyTurn(boolean myTurn, SnapshotToSend snapshot) {
    }

    @Override
    public void onResizeHeight(double height, double width) {
    }

    @Override
    public void onResizeWidth(double width, double height) {
    }

    @Override
    public void selectPermitCard() {
    }

    @Override
    public void selectCityRewardBonus() {
    }

    @Override
    public void moveKing(ArrayList<City> baseController) {
    }

    @Override
    public void selectOldPermitCardBonus() {
    }

    public void addItemToBuy(BuyableWrapper item) {
        if (!this.toBuy.contains(item)) {
            this.toBuy.add(item);
        }
    }

    public void removeItemToBuy(BuyableWrapper item) {
        if (this.toBuy.contains(item)) {
            this.toBuy.remove(item);
        }
    }

    private GridPane addBuyableItems(BuyableWrapper information) {
        GridPane baseGridPane = new GridPane();
        baseGridPane.setAlignment(Pos.CENTER);
        ColumnConstraints columnConstraints1 = new ColumnConstraints();
        ColumnConstraints columnConstraints2 = new ColumnConstraints();
        ColumnConstraints columnConstraints3 = new ColumnConstraints();
        columnConstraints1.setPercentWidth(25);
        columnConstraints2.setPercentWidth(50);
        columnConstraints3.setPercentWidth(25);
        RowConstraints rowConstraints1 = new RowConstraints();
        RowConstraints rowConstraints2 = new RowConstraints();
        RowConstraints rowConstraints3 = new RowConstraints();
        rowConstraints1.setPercentHeight(20);
        rowConstraints2.setPercentHeight(60);
        rowConstraints3.setPercentHeight(20);
        baseGridPane.getColumnConstraints().addAll(columnConstraints1, columnConstraints2, columnConstraints3);
        baseGridPane.getRowConstraints().addAll(rowConstraints1, rowConstraints2, rowConstraints3);
        ImageView itemBackground = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + information.getBuyableObject().getUrl() + ".png"));
        itemBackground.fitWidthProperty().bind(this.buyScrollPane.widthProperty().divide(2));
        itemBackground.setPreserveRatio(true);
        baseGridPane.getChildren().add(itemBackground);
        GridPane.setColumnSpan(itemBackground, 3);
        GridPane.setRowSpan(itemBackground, 3);
        JFXButton button = new JFXButton();
        button.setTextFill(Paint.valueOf("WHITE"));
        button.setButtonType(ButtonType.FLAT);
        button.setText(Integer.toString(information.getCost()));
        button.setBackground(new Background(new BackgroundFill(Paint.valueOf("4F6161"), new CornerRadii(20), null)));
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Graphics.playSomeSound("Button");
                ShopController.this.toBuy.add(information);
            }
        });
        Label label = new Label(information.getBuyableObject().getInfo());
        if (information.getBuyableObject() instanceof PermitCard) {
            baseGridPane.add(label, 1, 0);
            GridPane.setHalignment(label, HPos.CENTER);
            GridPane.setValignment(label, VPos.CENTER);
        }
        label.setVisible(true);
        label.setTextFill(Paint.valueOf("WHITE"));
        baseGridPane.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                label.setVisible(true);
                button.setVisible(true);
                ColorAdjust colorAdjust = new ColorAdjust();
                colorAdjust.setBrightness(-0.5);
                itemBackground.setEffect(colorAdjust);
            }
        });
        baseGridPane.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                button.setVisible(false);
                itemBackground.setEffect(null);
            }
        });
        baseGridPane.add(button, 1, 1);
        GridPane.setHalignment(button, HPos.CENTER);
        GridPane.setValignment(button, VPos.CENTER);
        baseGridPane.prefWidthProperty().bind(itemBackground.fitWidthProperty());
        baseGridPane.prefHeightProperty().bind(itemBackground.fitHeightProperty());
        return baseGridPane;
    }

    private GridPane addSalableItems(BuyableWrapper information) {
        GridPane baseGridPane = new GridPane();
        baseGridPane.setAlignment(Pos.CENTER);
        ColumnConstraints columnConstraints1 = new ColumnConstraints();
        ColumnConstraints columnConstraints2 = new ColumnConstraints();
        ColumnConstraints columnConstraints3 = new ColumnConstraints();
        columnConstraints1.setPercentWidth(25);
        columnConstraints2.setPercentWidth(50);
        columnConstraints3.setPercentWidth(25);
        RowConstraints rowConstraints1 = new RowConstraints();
        RowConstraints rowConstraints2 = new RowConstraints();
        RowConstraints rowConstraints3 = new RowConstraints();
        rowConstraints1.setPercentHeight(20);
        rowConstraints2.setPercentHeight(60);
        rowConstraints3.setPercentHeight(20);
        baseGridPane.getColumnConstraints().addAll(columnConstraints1, columnConstraints2, columnConstraints3);
        baseGridPane.getRowConstraints().addAll(rowConstraints1, rowConstraints2, rowConstraints3);
        JFXButton buttonToSell = new JFXButton();
        buttonToSell.setTextFill(Paint.valueOf("WHITE"));
        buttonToSell.setBackground(new Background(new BackgroundFill(Paint.valueOf("3D4248"), new CornerRadii(20), null)));
        buttonToSell.setButtonType(ButtonType.FLAT);
        if (information.isOnSale()) {
            buttonToSell.setText("RIMUOVI");
        } else {
            buttonToSell.setText("0");
        }
        Image itemOnSaleImage;
        Image upperImage;
        Image downerImage;
        ImageView itemOnSaleImageView = new ImageView();

        Label label = new Label("");
        label.setTextFill(Paint.valueOf("WHITE"));
        itemOnSaleImage = ImageLoader.getInstance().getImage("/ClientPackage/View/GUIResources/Image/" + information.getBuyableObject().getUrl() + ".png");
        if (information.getBuyableObject() instanceof PermitCard) {
            this.sellPane.setVgap(20);
            upperImage = ImageLoader.getInstance().getImage("/ClientPackage/View/GUIResources/Image/plusWhite.png");
            downerImage = ImageLoader.getInstance().getImage("/ClientPackage/View/GUIResources/Image/minusWhite.png");
            label.setText(information.getBuyableObject().getInfo());
        } else {
            this.sellPane.setVgap(0);
            if (information.getBuyableObject() instanceof Helper) {
                upperImage = ImageLoader.getInstance().getImage("/ClientPackage/View/GUIResources/Image/plusBlack.png");
                downerImage = ImageLoader.getInstance().getImage("/ClientPackage/View/GUIResources/Image/minusBlack.png");
            } else {
                upperImage = ImageLoader.getInstance().getImage("/ClientPackage/View/GUIResources/Image/plusWhite.png");
                downerImage = ImageLoader.getInstance().getImage("/ClientPackage/View/GUIResources/Image/minusWhite.png");
            }
        }
        ImageView upper = new ImageView(upperImage);
        ImageView downer = new ImageView(downerImage);
        JFXButton plusButton = new JFXButton("");
        plusButton.setGraphic(upper);
        JFXButton minusButton = new JFXButton("");
        minusButton.setGraphic(downer);
        JFXButton sellButton = new JFXButton("VENDI");
        sellButton.setTextFill(Paint.valueOf("WHITE"));
        sellButton.setButtonType(ButtonType.RAISED);
        sellButton.setBackground(new Background(new BackgroundFill(Paint.valueOf("4F6161"), null, null)));
        sellButton.prefWidthProperty().bind(itemOnSaleImageView.fitWidthProperty().divide(3));
        sellButton.setVisible(false);
        EventHandler<ActionEvent> onSell = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Graphics.playSomeSound("Button");
                if (!buttonToSell.getText().equals("RIMUOVI")) {
                    information.setOnSale(true);
                    information.setCost(Integer.parseInt(buttonToSell.getText()));
                    ShopController.this.trueList.add(information);
                    buttonToSell.setText("RIMUOVI");
                    sellButton.setVisible(false);
                } else {
                    if (ShopController.this.trueList.contains(information)) {
                        ShopController.this.trueList.remove(information);
                    } else {
                        ShopController.this.clientController.removeItemFromMarket(information);
                    }
                    information.setOnSale(false);
                    buttonToSell.setText("0");
                    sellButton.setVisible(true);
                }
            }
        };
        sellButton.setOnAction(onSell);
        buttonToSell.setOnAction(onSell);
        plusButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Graphics.playSomeSound("Button");
                if (buttonToSell.getText() != "RIMUOVI") {
                    if (Integer.parseInt(buttonToSell.getText()) - 1 < 19)
                        buttonToSell.setText(Integer.toString(Integer.parseInt(buttonToSell.getText()) + 1));
                }
            }
        });
        minusButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Graphics.playSomeSound("Button");
                if (buttonToSell.getText() != "RIMUOVI") {
                    if (Integer.parseInt(buttonToSell.getText()) - 1 > 0)
                        buttonToSell.setText(Integer.toString(Integer.parseInt(buttonToSell.getText()) - 1));
                    else
                        buttonToSell.setText(Integer.toString(0));
                }
            }
        });
        itemOnSaleImageView.setImage(itemOnSaleImage);
        baseGridPane.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!information.isOnSale()) {
                    sellButton.setVisible(true);
                    plusButton.setVisible(true);
                    minusButton.setVisible(true);
                }

                buttonToSell.setVisible(true);
                ColorAdjust colorAdjust = new ColorAdjust();
                colorAdjust.setBrightness(-0.5);
                itemOnSaleImageView.setEffect(colorAdjust);
            }
        });
        baseGridPane.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                sellButton.setVisible(false);
                itemOnSaleImageView.setEffect(null);
                plusButton.setVisible(false);
                minusButton.setVisible(false);
                buttonToSell.setVisible(false);
                plusButton.setVisible(false);
                minusButton.setVisible(false);
            }
        });
        baseGridPane.add(itemOnSaleImageView, 0, 0);
        GridPane.setColumnSpan(itemOnSaleImageView, 3);
        GridPane.setRowSpan(itemOnSaleImageView, 3);
        baseGridPane.add(plusButton, 2, 1);
        baseGridPane.add(buttonToSell, 1, 1);
        baseGridPane.add(minusButton, 0, 1);
        baseGridPane.add(sellButton, 1, 2);
        GridPane.setHalignment(sellButton, HPos.CENTER);
        GridPane.setValignment(sellButton, VPos.CENTER);
        upper.setPreserveRatio(false);
        upper.setFitWidth(30);
        upper.setFitHeight(30);
        downer.setPreserveRatio(false);
        downer.setFitWidth(30);
        downer.setFitHeight(30);
        GridPane.setHalignment(upper, HPos.CENTER);
        GridPane.setHalignment(downer, HPos.CENTER);
        GridPane.setHalignment(buttonToSell, HPos.CENTER);
        GridPane.setValignment(upper, VPos.CENTER);
        GridPane.setValignment(downer, VPos.CENTER);
        GridPane.setValignment(buttonToSell, VPos.CENTER);
        baseGridPane.add(label, 1, 0);
        GridPane.setValignment(label, VPos.CENTER);
        GridPane.setHalignment(label, HPos.CENTER);
        itemOnSaleImageView.setEffect(null);
        plusButton.setVisible(false);
        minusButton.setVisible(false);
        buttonToSell.setVisible(false);
        plusButton.setVisible(false);
        minusButton.setVisible(false);
        label.setVisible(true);
        itemOnSaleImageView.fitWidthProperty().bind(this.sellScrollPane.widthProperty().divide(2));
        itemOnSaleImageView.fitHeightProperty().bind(this.sellScrollPane.heightProperty().divide(2));
        itemOnSaleImageView.setPreserveRatio(false);
        baseGridPane.prefWidthProperty().bind(itemOnSaleImageView.fitWidthProperty().multiply(0.6));
        baseGridPane.prefHeightProperty().bind(itemOnSaleImageView.fitHeightProperty().multiply(0.6));
        return baseGridPane;
    }

    void displayHelp() {
        PopOver helpPopOver = new PopOver();
        String helpString = "Benvenuto!\n" +
                "In basso puoi osservare quali carte politiche hai in mano e le tue carte permesso.\n" +
                "Le info personali sono posizionate a sinistra.\n" +
                "Le info avversarie sono visibili dal menù a destra\n" +
                "Per compiere un'azione principale o veloce basta selezionare l'oggetto inerente. Per esempio:\n" +
                "puoi acquistare una nuova azione principale cliccando sulla stella nelle tue info,\n" +
                "se invece vuoi cambiare le carte permesso visibili basta cliccarci sopra.\n" +
                "Io sono il mercante, e nella fase di mercato gestirò il mercato.\n" +
                "Per vendere clicca sulle immagini sul bancone e poi dimmelo. Per comprare chiedi a me.\n" +
                "Buon proseguimento!";
        StackPane stackPaneForHelp = new StackPane();
        Text textForHelp = new Text();
        textForHelp.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 15));
        textForHelp.setText(helpString);
        stackPaneForHelp.getChildren().add(textForHelp);
        StackPane.setAlignment(textForHelp, Pos.CENTER);
        stackPaneForHelp.setPadding(new Insets(20));
        helpPopOver.setContentNode(stackPaneForHelp);
        helpPopOver.show(this.paneWhereShowPopOver);
    }

    public void setMatchController(MatchController matchController) {
        this.matchController = matchController;
    }
}
