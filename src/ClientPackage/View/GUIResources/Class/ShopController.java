package ClientPackage.View.GUIResources.Class;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.GUIResources.customComponent.ImageLoader;
import ClientPackage.View.GeneralView.GUIView;
import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticColor;
import CommonModel.GameModel.City.City;
import CommonModel.GameModel.Council.Helper;
import CommonModel.GameModel.Market.BuyableWrapper;
import CommonModel.Snapshot.SnapshotToSend;
import Utilities.Class.Constants;
import Utilities.Class.Graphics;
import com.jfoenix.controls.JFXButton;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.*;
import javafx.scene.control.*;
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
    private ImageView backgroundImage = new ImageView();
    private ImageView politicCardDeck = new ImageView();
    private ImageView permitCardDeck = new ImageView();
    private ImageView helperDeck = new ImageView();
    private TilePane buyPane = new TilePane();
    private TilePane sellPane = new TilePane();
    private ScrollPane buyScrollPane = new ScrollPane();
    private ScrollPane sellScrollPane = new ScrollPane();
    private VBox buyVBox = new VBox();
    private VBox sellVBox = new VBox();

    private Button innerButton;
    private PopOver innerPopOver;
    private Pane paneWhereShowPopOver = new Pane();
    private boolean confirming = true;
    private ArrayList<BuyableWrapper> sellList = new ArrayList<>();
    private ArrayList<BuyableWrapper> temporarySellList = new ArrayList<>();
    private ArrayList<BuyableWrapper> buyList = new ArrayList<>();
    private ArrayList<BuyableWrapper> toBuy = new ArrayList<>();
    private ArrayList<BuyableWrapper> trueList = new ArrayList<>();
    private ShopController shopController = this;

    private ImageView buyIt = new ImageView();
    private ImageView finishShop = new ImageView();
    JFXButton finishShopButton = new JFXButton();
    JFXButton buyItButton = new JFXButton("");

    @FXML private GridPane shop;
    @FXML private Pane paneBackground;

    private boolean marketPhase = false;
    private boolean sellPhase = false;
    private boolean buyPhase = false;



    @Override
    public void setClientController(ClientController clientController, GUIView guiView) {
        this.clientController = clientController;
        this.guiView = guiView;
        guiView.registerBaseController(this);
        backgroundImage.setImage(ImageLoader.getInstance().getImage("/ClientPackage/View/GUIResources/Image/ShopBackground.png"));
        setBackground();
        updateView();
        createDeck();
        //onFinishMarket();
        setArrow();
        setMarketPane();

    }

    private void setArrow() {
        JFXButton backArrow = new JFXButton();
        ImageView arrowImageView = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "/Arrow.png"));
        arrowImageView.layoutXProperty().bind(paneBackground.widthProperty().multiply(0.3188));
        arrowImageView.layoutYProperty().bind(paneBackground.heightProperty().multiply(0.7767));
        backArrow.setGraphic(arrowImageView);
        backArrow.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //TODO
            }
        });
    }

    private void setMarketPane() {
        sellPane.setPrefColumns(2);
        /*sellPane.prefWidthProperty().bind(paneBackground.widthProperty().divide(3));
        sellPane.prefHeightProperty().bind(paneBackground.heightProperty().divide(3));
        */
        sellScrollPane.prefHeightProperty().bind(paneBackground.heightProperty().divide(2));
        sellScrollPane.prefWidthProperty().bind(paneBackground.widthProperty().divide(3));
        //sellScrollPane.setPadding(new Insets(20));
        sellScrollPane.setContent(sellPane);


        buyPane.setPrefColumns(2);
        /*buyPane.prefWidthProperty().bind(paneBackground.widthProperty().divide(3));
        buyPane.prefHeightProperty().bind(paneBackground.heightProperty().divide(3));
        */
        buyScrollPane.prefHeightProperty().bind(paneBackground.widthProperty().divide(3));
        buyScrollPane.prefWidthProperty().bind(paneBackground.heightProperty().divide(3));
        //buyScrollPane.setPadding(new Insets(20));
        buyScrollPane.setContent(buyPane);
        finishShop = new ImageView(new Image(Constants.IMAGE_PATH + "/Check.png"));
        buyIt = new ImageView(new Image(Constants.IMAGE_PATH + "/Cart.png"));
        finishShopButton.setGraphic(finishShop);
        buyItButton.setGraphic(buyIt);
        HBox bottomHBox = new HBox();
        bottomHBox.setAlignment(Pos.CENTER);
        bottomHBox.getChildren().addAll(finishShopButton, buyItButton);
        buyVBox.getChildren().addAll(buyScrollPane, bottomHBox);
    }

    private void setBackground() {
        paneBackground.getChildren().add(backgroundImage);
        backgroundImage.setPreserveRatio(true);
        backgroundImage.fitHeightProperty().bind(shop.heightProperty());
        backgroundImage.fitWidthProperty().bind(shop.widthProperty());
        shop.prefWidthProperty().bind(paneBackground.prefWidthProperty());
        shop.prefHeightProperty().bind(paneBackground.prefHeightProperty());

        paneBackground.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("event x and y "+event.getX()/paneBackground.getWidth()+" "+event.getY()/paneBackground.getHeight());
            }
        });
        backgroundImage.fitWidthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                System.out.println("image: "+newValue.doubleValue());
                System.out.println("background "+ shop.getWidth() );
            }
        });
        backgroundImage.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) {
                System.out.println("changed to "+newValue.getWidth()+" "+" "+newValue.getHeight());
                paneBackground.setPrefWidth(newValue.getWidth());
                paneBackground.setPrefHeight(newValue.getHeight());

            }
        });
        GridPane.setRowSpan(backgroundImage, 2);
        GridPane.setColumnSpan(backgroundImage, 2);
        paneWhereShowPopOver = new Pane();
        paneWhereShowPopOver.layoutXProperty().bind(paneBackground.prefWidthProperty().multiply(0.4957));
        paneWhereShowPopOver.layoutYProperty().bind(paneBackground.prefHeightProperty().multiply(0.3045));
        paneWhereShowPopOver.prefWidthProperty().bind(paneBackground.prefWidthProperty().multiply(0.01));
        paneWhereShowPopOver.prefHeightProperty().bind(paneBackground.prefHeightProperty().multiply(0.01));
        paneBackground.getChildren().add(paneWhereShowPopOver);
        innerButton = new Button();
        innerButton.setStyle("-fx-background-color: transparent; -fx-cursor: crosshair;");
        innerButton.layoutXProperty().bind(paneBackground.prefWidthProperty().multiply(0.3504));
        innerButton.layoutYProperty().bind(paneBackground.prefHeightProperty().multiply(0.1455));
        innerButton.prefWidthProperty().bind(paneBackground.prefWidthProperty().multiply(0.5208 - 0.3604));
        innerButton.prefHeightProperty().bind(paneBackground.prefHeightProperty().multiply(0.5530 - 0.1455));
        paneBackground.getChildren().addAll(innerButton);
    }


    private void onBuy()
    {
        Runnable runnable = () -> {
            clientController.onBuy((ArrayList<BuyableWrapper>)toBuy.clone());
            toBuy.clear();
        };
        new Thread(runnable).start();
    }

    private void onSell() {
        Runnable runnable = () -> {
            if (trueList.size() > 0) {
                clientController.sendSaleItem((ArrayList<BuyableWrapper>) trueList.clone());
                trueList.clear();
            }
        };
        new Thread(runnable).start();
    }

    @Override
    public void updateView() {
        System.out.println("On update shopController");
        updateList();
    }

    private void updateList() {
        sellList = new ArrayList<>();
        SnapshotToSend snapshotTosend = clientController.getSnapshot();
        buyList = snapshotTosend.getMarketList();


        for (PoliticCard politicCard: snapshotTosend.getCurrentUser().getPoliticCards()) {
            BuyableWrapper buyableWrapperTmp = new BuyableWrapper(politicCard,snapshotTosend.getCurrentUser().getUsername());
            sellList.add(buyableWrapperTmp);
        }

        for(PermitCard permitCard: snapshotTosend.getCurrentUser().getPermitCards()){
            BuyableWrapper buyableWrapperTmp = new BuyableWrapper(permitCard,snapshotTosend.getCurrentUser().getUsername());
            sellList.add(buyableWrapperTmp);
        }

        for(Helper helper: snapshotTosend.getCurrentUser().getHelpers()){
            BuyableWrapper buyableWrapper = new BuyableWrapper(helper,snapshotTosend.getCurrentUser().getUsername());
            sellList.add(buyableWrapper);
        }

        for(Iterator<BuyableWrapper> itr = buyList.iterator(); itr.hasNext();){
            BuyableWrapper buyableWrapper = itr.next();
            if(buyableWrapper.getUsername().equals(snapshotTosend.getCurrentUser().getUsername())){
                sellList.remove(buyableWrapper);
                sellList.add(buyableWrapper);
                itr.remove();
            }
        }

        populateSellAndBuyPane();
    }

    private void populateSellAndBuyPane() {
        sellPane.getChildren().clear();
        buyPane.getChildren().clear();

        for (BuyableWrapper buyableWrapper : temporarySellList) {
            sellPane.getChildren().add(addSalableItems(buyableWrapper));
        }

        for (BuyableWrapper buyableWrapper : buyList) {
            buyPane.getChildren().add(addBuyableItems(buyableWrapper));
        }
    }

    private void createDeck() {
        politicCardDeck.setImage(ImageLoader.getInstance().getImage("/ClientPackage/View/GUIResources/Image/PoliticCardDistorted.png"));
        permitCardDeck.setImage(new Image("/ClientPackage/View/GUIResources/Image/PermitCardsDistorted.png",true));
        helperDeck.setImage(new Image("/ClientPackage/View/GUIResources/Image/HelperDistorted.png",true));
        paneBackground.getChildren().addAll(politicCardDeck, permitCardDeck, helperDeck);
        politicCardDeck.layoutXProperty().bind(paneBackground.widthProperty().multiply(0.3973));
        politicCardDeck.layoutYProperty().bind(paneBackground.heightProperty().multiply(0.5612));
        permitCardDeck.layoutXProperty().bind(paneBackground.widthProperty().multiply(0.4691));
        permitCardDeck.layoutYProperty().bind(paneBackground.heightProperty().multiply(0.5552));
        helperDeck.layoutXProperty().bind(paneBackground.widthProperty().multiply(0.545));
        helperDeck.layoutYProperty().bind(paneBackground.heightProperty().multiply(0.5597));
        politicCardDeck.fitWidthProperty().bind(paneBackground.widthProperty().divide(10));
        politicCardDeck.fitHeightProperty().bind(paneBackground.heightProperty().divide(10));
        permitCardDeck.fitWidthProperty().bind(paneBackground.widthProperty().divide(10));
        permitCardDeck.fitHeightProperty().bind(paneBackground.heightProperty().divide(10));
        helperDeck.fitWidthProperty().bind(paneBackground.widthProperty().divide(10));
        helperDeck.fitHeightProperty().bind(paneBackground.heightProperty().divide(10));

        // add effect
        Graphics.addBorder(politicCardDeck);
        Graphics.addBorder(helperDeck);
        Graphics.addBorder(permitCardDeck);


    }

    private void settingDeckActions() {
        politicCardDeck.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(sellPhase) {
                    ImageView imageClicked = (ImageView) event.getTarget();
                    shopPopOver(imageClicked, PoliticCard.class);
                }
            }
        });
        permitCardDeck.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(sellPhase) {
                    ImageView imageClicked = (ImageView) event.getTarget();
                    shopPopOver(imageClicked, PermitCard.class);
                }
            }
        });
        helperDeck.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(sellPhase) {
                    ImageView imageClicked = (ImageView) event.getTarget();
                    shopPopOver(imageClicked, Helper.class);
                }
            }
        });
    }

    private void shopPopOver(ImageView imageClicked, Class objectClass) {
        temporarySellList.clear();
        sellList.forEach(buyableWrapper -> {
            if (buyableWrapper.getBuyableObject().getClass().equals(objectClass)){
                temporarySellList.add(buyableWrapper);
            }
        });
        populateSellAndBuyPane();
        PopOver popOver = new PopOver();
        popOver.setContentNode(sellScrollPane);
        popOver.show(imageClicked);
    }

    private void sellToInnerKeeper() {
        innerPopOver = new PopOver();
        VBox innerVBox = new VBox();
        Label innerLabel = new Label("Sei sicuro di voler vendere questi oggetti?");
        JFXButton innerPopOverButton = new JFXButton("Sì, mi assumo ogni responsabilità!");
        innerPopOverButton.setBackground(new Background(new BackgroundFill(Paint.valueOf("#FF512D"),null,null)));
        innerPopOverButton.setTextFill(Paint.valueOf("WHITE"));
        innerVBox.getChildren().addAll(innerLabel, innerPopOverButton);
        innerVBox.setPadding(new Insets(20,20,20,20));
        innerPopOver.setContentNode(innerVBox);
        innerPopOver.show(paneWhereShowPopOver);
        innerPopOverButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                innerPopOver.hide();
                onSell();
                clientController.sendFinishSellPhase();
                waitingForBuying();
            }
        });
    }

    private void waitingForBuying() {
        sellPhase=false;
        innerPopOver = new PopOver();
        StackPane stackPane = new StackPane(new Label("Aspetta il tuo turno, villano."));
        stackPane.setPadding(new Insets(20,20,20,20));
        innerPopOver.setContentNode(stackPane);
        innerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                innerPopOver.show(paneWhereShowPopOver);
            }
        });

    }

    private void createBuyingPopOver() {
        innerPopOver = new PopOver();
        /*
        GridPane buyingSessionGridPane = new GridPane();
        buyingSessionGridPane.setAlignment(Pos.CENTER);
        buyingSessionGridPane.add(buyPane, 0, 0);
        GridPane.setColumnSpan(buyPane, 2);
        */

        if(buyPhase) {
            finishShopButton.setGraphic(finishShop);
            finishShopButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    innerPopOver.hide();
                    onBuy();
                    //toBuy.clear();
                    buyPhase = false;
                    clientController.sendFinishedBuyPhase();
                }
            });

            buyItButton.setGraphic(buyIt);
            buyItButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    onBuy();
                    //toBuy.clear();
                }
            });

            buyIt.setFitHeight(40);
            buyIt.setFitWidth(40);
            buyIt.setPreserveRatio(true);
            finishShop.setFitWidth(40);
            finishShop.setFitHeight(40);
        /*
        buyingSessionGridPane.add(finishShopButton, 0, 1);
        buyingSessionGridPane.add(buyItButton, 1, 1);
        buyingSessionGridPane.prefWidthProperty().bind(paneBackground.heightProperty().divide(10));
        buyingSessionGridPane.prefHeightProperty().bind(paneBackground.widthProperty().divide(10));
        innerPopOver.setContentNode(buyingSessionGridPane);
        */
            innerPopOver.setContentNode(buyVBox);
            innerPopOver.show(paneWhereShowPopOver);
        }
        else{
            nothingToDoPopOver();
        }

    }

    private void nothingToDoPopOver() {
        if (paneWhereShowPopOver != null) {
            PopOver popOverToShow = new PopOver();
            Pane internalPopOverPane = new Pane();
            internalPopOverPane.getChildren().add(new Label("Bottega chiusa, viandante!"));
            internalPopOverPane.setPadding(new Insets(20,20,20,20));
            popOverToShow.setContentNode(internalPopOverPane);
            popOverToShow.show(paneWhereShowPopOver);
        }
    }

    @Override
    public void onStartMarket() {

        Graphics.notification("Start Market");
        marketPhase=true;
        sellPhase=true;
        buyList.clear();
        sellList.clear();
        toBuy.clear();
        temporarySellList.clear();
        trueList.clear();
        settingDeckActions();
        updateView();
        innerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(sellPhase)
                    sellToInnerKeeper();
            }
        });
    }

    @Override
    public void onStartBuyPhase() {
       buyPhase=true;
        createBuyingPopOver();
        settingDeckActions();
        politicCardDeck.setOnMouseClicked(null);
        permitCardDeck.setOnMouseClicked(null);
        helperDeck.setOnMouseClicked(null);
        innerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                    createBuyingPopOver();
            }
        });
        Graphics.notification("Start Buy Phase");
    }

    @Override
    public void  onFinishMarket() {
        Graphics.notification("Finish Market");
        buyList.clear();
        sellList.clear();
        temporarySellList.clear();
        trueList.clear();
        politicCardDeck.setOnMouseClicked(null);
        permitCardDeck.setOnMouseClicked(null);
        helperDeck.setOnMouseClicked(null);
        innerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                nothingToDoPopOver();
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
        System.out.println("adding item :"+item);
        if(!toBuy.contains(item)){
            toBuy.add(item);
        }
    }

    public void removeItemToBuy(BuyableWrapper item) {
        if(toBuy.contains(item)){
            toBuy.remove(item);
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
        itemBackground.fitWidthProperty().bind(buyScrollPane.widthProperty().divide(2));
        itemBackground.setPreserveRatio(true);
        baseGridPane.getChildren().add(itemBackground);
        GridPane.setColumnSpan(itemBackground, 3);
        GridPane.setRowSpan(itemBackground, 3);
        JFXButton button = new JFXButton();
        button.setTextFill(Paint.valueOf("WHITE"));
        button.setButtonType(JFXButton.ButtonType.FLAT);
        button.setText(Integer.toString(information.getCost()));
        button.setBackground(new Background(new BackgroundFill(Paint.valueOf("4F6161"),new CornerRadii(20),null)));
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toBuy.add(information);
            }
        });

        Label label = new Label(information.getBuyableObject().getInfo());
        if(information.getBuyableObject() instanceof PermitCard){
            baseGridPane.add(label,1,0);
            GridPane.setHalignment(label,HPos.CENTER);
            GridPane.setValignment(label,VPos.CENTER);
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
               // label.setVisible(false);
                itemBackground.setEffect(null);
            }
        });
        baseGridPane.add(button, 1, 1);
        GridPane.setHalignment(button,HPos.CENTER);
        GridPane.setValignment(button,VPos.CENTER);
        baseGridPane.prefWidthProperty().bind(itemBackground.fitWidthProperty());
        baseGridPane.prefHeightProperty().bind(itemBackground.fitHeightProperty());
        return baseGridPane;
    }

    private GridPane addSalableItems(BuyableWrapper information){
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
        buttonToSell.setBackground(new Background(new BackgroundFill(Paint.valueOf("3D4248"),new CornerRadii(20),null)));
        buttonToSell.setButtonType(JFXButton.ButtonType.FLAT);
        if (information.isOnSale()){
            buttonToSell.setText("REMOVE");
            System.out.println(information.getBuyableObject().getUrl() + " ADDED");
        } else {
            buttonToSell.setText("0");
            System.out.println(information.getBuyableObject().getUrl() + " REMOVED");
        }

        Image itemOnSaleImage = null;
        Image upperImage;
        Image downerImage;
        ImageView itemOnSaleImageView = new ImageView();

        Label label = new Label("");
        label.setTextFill(Paint.valueOf("WHITE"));
        itemOnSaleImage = ImageLoader.getInstance().getImage("/ClientPackage/View/GUIResources/Image/" + information.getBuyableObject().getUrl() + ".png");
        if (information.getBuyableObject() instanceof PermitCard){
            sellPane.setVgap(20);
            upperImage = ImageLoader.getInstance().getImage("/ClientPackage/View/GUIResources/Image/plusWhite.png");
            downerImage = ImageLoader.getInstance().getImage("/ClientPackage/View/GUIResources/Image/minusWhite.png");
            label.setText(information.getBuyableObject().getInfo());
        } else {
            sellPane.setVgap(0);
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
        JFXButton sellButton = new JFXButton("Sell!");
        sellButton.setTextFill(Paint.valueOf("WHITE"));
        sellButton.setButtonType(JFXButton.ButtonType.RAISED);
        sellButton.setBackground(new Background(new BackgroundFill(Paint.valueOf("4F6161"),null,null)));
        sellButton.prefWidthProperty().bind(itemOnSaleImageView.fitWidthProperty().divide(3));
        sellButton.setVisible(false);

        sellButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!buttonToSell.getText().equals("REMOVE")) {
                    information.setOnSale(true);
                    information.setCost(Integer.parseInt(buttonToSell.getText()));
                    trueList.add(information);
                    buttonToSell.setText("REMOVE");
                    sellButton.setVisible(false);
                }
                else{
                    trueList.remove(information);
                    information.setOnSale(false);
                    buttonToSell.setText("0");
                    sellButton.setVisible(true);
                }
            }
        });

        buttonToSell.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!buttonToSell.getText().equals("REMOVE")) {
                    information.setOnSale(true);
                    information.setCost(Integer.parseInt(buttonToSell.getText()));
                    trueList.add(information);
                    buttonToSell.setText("REMOVE");
                    sellButton.setVisible(false);

                }
                else{
                    trueList.remove(information);
                    information.setOnSale(false);
                    buttonToSell.setText("0");
                    sellButton.setVisible(true);
                }
            }
        });

        plusButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(buttonToSell.getText()!="REMOVE") {
                    if (Integer.parseInt(buttonToSell.getText()) - 1 < 19)
                        buttonToSell.setText(Integer.toString(Integer.parseInt(buttonToSell.getText()) + 1));
                }
            }
        });
        minusButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(buttonToSell.getText()!="REMOVE") {
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
                if(!information.isOnSale()) {
                    sellButton.setVisible(true);
                }
                plusButton.setVisible(true);
                minusButton.setVisible(true);

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
        baseGridPane.add(sellButton,1,2);
        GridPane.setHalignment(sellButton,HPos.CENTER);
        GridPane.setValignment(sellButton,VPos.CENTER);
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
        baseGridPane.add(label,1, 0);
        GridPane.setValignment(label,VPos.CENTER);
        GridPane.setHalignment(label,HPos.CENTER);
        itemOnSaleImageView.setEffect(null);
        plusButton.setVisible(false);
        minusButton.setVisible(false);
        buttonToSell.setVisible(false);
        plusButton.setVisible(false);
        minusButton.setVisible(false);
        label.setVisible(true);
        itemOnSaleImageView.fitWidthProperty().bind(sellScrollPane.widthProperty().divide(2));
        itemOnSaleImageView.fitHeightProperty().bind(sellScrollPane.heightProperty().divide(2));
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
                /*
                "Per scalzare un consigliere puoi cliccare sopra il consiglio.\n" +
                "Per acquistare una tessera permesso scegli quella che vuoi e clicca." +
                "Per costruire un emporio basta cliccare sulla città.\n" +
                "Per costruire con l'aiuto del Re basta cliccare sopra la corona." +
                */
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
        helpPopOver.show(paneWhereShowPopOver);
    }
}
