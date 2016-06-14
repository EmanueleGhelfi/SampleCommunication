package ClientPackage.View.GUIResources.Class;

import ClientPackage.Controller.ClientController;
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

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
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

    @FXML private GridPane shop;
    @FXML private Pane paneBackground;
    @FXML private ScrollPane sellScroll;

    @Override
    public void setClientController(ClientController clientController, GUIView guiView) {
        this.clientController = clientController;
        this.guiView = guiView;
        guiView.registerBaseController(this);
        backgroundImage.setImage(new Image("/ClientPackage/View/GUIResources/Image/ShopBackground.jpg"));
        setBackground();
        updateView();
        createDeck();
        onFinishMarket();
    }

    private void setBackground() {
        paneBackground.getChildren().add(backgroundImage);
        backgroundImage.setPreserveRatio(true);
        backgroundImage.fitHeightProperty().bind(shop.heightProperty());
        backgroundImage.fitWidthProperty().bind(shop.widthProperty());
        shop.prefWidthProperty().bind(paneBackground.prefWidthProperty());
        shop.prefHeightProperty().bind(paneBackground.prefHeightProperty());
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
        paneWhereShowPopOver.layoutXProperty().bind(paneBackground.prefWidthProperty().multiply(0.4937));
        paneWhereShowPopOver.layoutYProperty().bind(paneBackground.prefHeightProperty().multiply(0.2254));
        paneWhereShowPopOver.prefWidthProperty().bind(paneBackground.prefWidthProperty().multiply(0.01));
        paneWhereShowPopOver.prefHeightProperty().bind(paneBackground.prefHeightProperty().multiply(0.01));
        paneBackground.getChildren().add(paneWhereShowPopOver);
        innerButton = new Button();
        innerButton.setStyle("-fx-background-color: transparent");
        innerButton.layoutXProperty().bind(paneBackground.prefWidthProperty().multiply(0.3504));
        innerButton.layoutYProperty().bind(paneBackground.prefHeightProperty().multiply(0.1455));
        innerButton.prefWidthProperty().bind(paneBackground.prefWidthProperty().multiply(0.5208 - 0.3604));
        innerButton.prefHeightProperty().bind(paneBackground.prefHeightProperty().multiply(0.5530 - 0.1455));
        paneBackground.getChildren().addAll(innerButton);
    }


    public void onBuy() {
        Runnable runnable = () -> {
            clientController.onBuy(toBuy);
            toBuy.clear();
        };
        new Thread(runnable).start();
    }

    public void onSell() {
        Runnable runnable = () -> {
            if (trueList.size() > 0) {
                clientController.sendSaleItem(trueList);
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
        politicCardDeck.setImage(new Image("/ClientPackage/View/GUIResources/Image/PoliticCardDistorted.png"));
        permitCardDeck.setImage(new Image("/ClientPackage/View/GUIResources/Image/PermitCardsDistorted.png"));
        helperDeck.setImage(new Image("/ClientPackage/View/GUIResources/Image/HelperDistorted.png"));
        paneBackground.getChildren().addAll(politicCardDeck, permitCardDeck, helperDeck);
        politicCardDeck.layoutXProperty().bind(paneBackground.widthProperty().multiply(0.3883));
        politicCardDeck.layoutYProperty().bind(paneBackground.heightProperty().multiply(0.5612));
        permitCardDeck.layoutXProperty().bind(paneBackground.widthProperty().multiply(0.4591));
        permitCardDeck.layoutYProperty().bind(paneBackground.heightProperty().multiply(0.5552));
        helperDeck.layoutXProperty().bind(paneBackground.widthProperty().multiply(0.535));
        helperDeck.layoutYProperty().bind(paneBackground.heightProperty().multiply(0.5597));
        politicCardDeck.fitWidthProperty().bind(paneBackground.widthProperty().divide(10));
        politicCardDeck.fitHeightProperty().bind(paneBackground.heightProperty().divide(10));
        permitCardDeck.fitWidthProperty().bind(paneBackground.widthProperty().divide(10));
        permitCardDeck.fitHeightProperty().bind(paneBackground.heightProperty().divide(10));
        helperDeck.fitWidthProperty().bind(paneBackground.widthProperty().divide(10));
        helperDeck.fitHeightProperty().bind(paneBackground.heightProperty().divide(10));
    }

    private void settingDeckActions() {
        politicCardDeck.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ImageView imageClicked = (ImageView) event.getTarget();
                shopPopOver(imageClicked, PoliticCard.class);
            }
        });
        permitCardDeck.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ImageView imageClicked = (ImageView) event.getTarget();
                shopPopOver(imageClicked, PermitCard.class);
            }
        });
        helperDeck.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ImageView imageClicked = (ImageView) event.getTarget();
                shopPopOver(imageClicked, Helper.class);
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
        popOver.setContentNode(sellPane);
        popOver.prefWidthProperty().bind(paneBackground.prefHeightProperty().divide(10));
        popOver.prefHeightProperty().bind(paneBackground.prefWidthProperty().divide(10));
        popOver.show(imageClicked);
    }

    private void sellToInnerKeeper() {
        innerPopOver = new PopOver();
        VBox innerVBox = new VBox();
        Label innerLabel = new Label("Sei sicuro?");
        JFXButton innerPopOverButton = new JFXButton("OK");
        innerVBox.getChildren().addAll(innerLabel, innerPopOverButton);
        innerPopOver.setContentNode(innerVBox);
        innerPopOver.show(paneWhereShowPopOver);
        innerPopOverButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println(trueList);
                onSell();
                clientController.sendFinishSellPhase();
            }
        });
    }

    private void createBuyingPopOver() {
        innerPopOver = new PopOver();
        GridPane buyingSessionGridPane = new GridPane();
        buyingSessionGridPane.setAlignment(Pos.CENTER);
        buyingSessionGridPane.add(buyPane, 0, 0);
        GridPane.setColumnSpan(buyPane, 2);
        ImageView finishShop = new ImageView(new Image(Constants.IMAGE_PATH + "/Check.png"));
        finishShop.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                onBuy();
                toBuy.clear();
                clientController.sendFinishedBuyPhase();
            }
        });
        ImageView buyIt = new ImageView(new Image(Constants.IMAGE_PATH + "/Cart.png"));
        buyIt.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                onBuy();
                toBuy.clear();
            }
        });
        buyingSessionGridPane.add(finishShop, 0, 1);
        buyingSessionGridPane.add(buyIt, 1, 1);
        innerPopOver.setContentNode(buyingSessionGridPane);
        innerPopOver.show(paneWhereShowPopOver);
    }

    private void nothingToDoPopOver() {
        if (paneWhereShowPopOver != null) {
            PopOver popOverToShow = new PopOver();
            Pane internalPopOverPane = new Pane();
            internalPopOverPane.getChildren().add(new Label("Bottega chiusa, viandante!"));
            popOverToShow.setContentNode(internalPopOverPane);

            popOverToShow.show(paneWhereShowPopOver);
        }
    }

    @Override
    public void onStartMarket() {
        Graphics.notification("Start Market");
        System.out.println(sellList +  " SELL LIST");
        System.out.println(trueList +  " TRUE LIST");
        System.out.println(buyList +  " BUY LIST");
        System.out.println(toBuy +  " TOBUY LIST");
        buyList.clear();
        sellList.clear();
        temporarySellList.clear();
        trueList.clear();
        settingDeckActions();
        innerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                sellToInnerKeeper();
            }
        });
    }

    @Override
    public void onStartBuyPhase() {
        System.out.println(clientController.getSnapshot().getMarketList() + " GUARDA QUI PER FAVORE");
        System.out.println(sellList +  " SELL LIST");
        System.out.println(trueList +  " TRUE LIST");
        System.out.println(buyList +  " BUY LIST");
        System.out.println(toBuy +  " TOBUY LIST");
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
        System.out.println(sellList +  " SELL LIST");
        System.out.println(trueList +  " TRUE LIST");
        System.out.println(buyList +  " BUY LIST");
        System.out.println(toBuy +  " TOBUY LIST");
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
        ImageView itemBackground = new ImageView();
        if (information.getBuyableObject() instanceof PermitCard)
            itemBackground = new ImageView(new Image(Constants.IMAGE_PATH + "/PermitCard.png"));
        else if (information.getBuyableObject() instanceof Helper)
            itemBackground = new ImageView(new Image(Constants.IMAGE_PATH + "/Helper.png"));
        else if (information.getBuyableObject() instanceof  PoliticCard)
            itemBackground = new ImageView(new Image(Constants.IMAGE_PATH + information.getBuyableObject().getUrl() + ".png"));
        baseGridPane.getChildren().add(itemBackground);
        GridPane.setColumnSpan(itemBackground, 3);
        GridPane.setRowSpan(itemBackground, 3);
        JFXButton button = new JFXButton();
        button.setTextFill(Paint.valueOf("WHITE"));
        button.setText(Integer.toString(information.getCost()));
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toBuy.add(information);
            }
        });
        baseGridPane.add(button, 1, 1);
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
        if (information.isOnSale()){
            buttonToSell.setText("REMOVE");
            System.out.println(information.getBuyableObject().getUrl() + " ADDED");
        } else {
            buttonToSell.setText("0");
            System.out.println(information.getBuyableObject().getUrl() + " REMOVED");
        }
        buttonToSell.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!buttonToSell.getText().equals("REMOVE")) {
                    trueList.add(information);
                    information.setOnSale(true);
                    buttonToSell.setText("REMOVE");
                }
                else{
                    trueList.remove(information);
                    information.setOnSale(false);
                    buttonToSell.setText("0");
                }
            }
        });
        Image itemOnSaleImage = null;
        Image upperImage;
        Image downerImage;
        ImageView itemOnSaleImageView = new ImageView(itemOnSaleImage);
        if (information.getBuyableObject() instanceof PermitCard){
            Label label = new Label();
            label.setText(information.getBuyableObject().getUrl());
            baseGridPane.add(label, 0, 1);
            itemOnSaleImage = new Image("/ClientPackage/View/GUIResources/Image/PermitCard.png");
            upperImage = new Image("/ClientPackage/View/GUIResources/Image/plusWhite.png");
            downerImage = new Image ("/ClientPackage/View/GUIResources/Image/minusWhite.png");

        } else {
            if (information.getBuyableObject() instanceof Helper) {
                itemOnSaleImage = new Image("/ClientPackage/View/GUIResources/Image/" + information.getBuyableObject().getUrl() + ".png");
                upperImage = new Image("/ClientPackage/View/GUIResources/Image/plusBlack.png");
                downerImage = new Image("/ClientPackage/View/GUIResources/Image/minusBlack.png");
            } else {
                itemOnSaleImage = new Image("/ClientPackage/View/GUIResources/Image/" + information.getBuyableObject().getUrl() + ".png");
                upperImage = new Image("/ClientPackage/View/GUIResources/Image/plusWhite.png");
                downerImage = new Image ("/ClientPackage/View/GUIResources/Image/minusWhite.png");
            }
        }
        ImageView upper = new ImageView(upperImage);
        ImageView downer = new ImageView(downerImage);
        upper.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (Integer.parseInt(buttonToSell.getText()) - 1 < 19)
                    buttonToSell.setText(Integer.toString(Integer.parseInt(buttonToSell.getText()) + 1));
            }
        });
        downer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (Integer.parseInt(buttonToSell.getText()) - 1 > 0)
                    buttonToSell.setText(Integer.toString(Integer.parseInt(buttonToSell.getText()) - 1));
                else
                    buttonToSell.setText(Integer.toString(0));
            }
        });
        itemOnSaleImageView.setImage(itemOnSaleImage);
        itemOnSaleImageView.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                upper.setVisible(true);
                downer.setVisible(true);
                buttonToSell.setVisible(true);
                ColorAdjust colorAdjust = new ColorAdjust();
                colorAdjust.setBrightness(-0.5);
                itemOnSaleImageView.setEffect(colorAdjust);
            }
        });
        baseGridPane.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                itemOnSaleImageView.setEffect(null);
            }
        });
        baseGridPane.add(itemOnSaleImageView, 0, 0);
        GridPane.setColumnSpan(itemOnSaleImageView, 3);
        GridPane.setRowSpan(itemOnSaleImageView, 3);
        baseGridPane.add(upper, 0, 1);
        baseGridPane.add(buttonToSell, 1, 1);
        baseGridPane.add(downer, 2, 1);
        upper.setPreserveRatio(false);
        upper.setFitWidth(40);
        upper.setFitHeight(40);
        downer.setPreserveRatio(false);
        downer.setFitWidth(40);
        downer.setFitHeight(40);
        GridPane.setHalignment(upper, HPos.CENTER);
        GridPane.setHalignment(downer, HPos.CENTER);
        GridPane.setHalignment(buttonToSell, HPos.CENTER);
        GridPane.setValignment(upper, VPos.CENTER);
        GridPane.setValignment(downer, VPos.CENTER);
        GridPane.setValignment(buttonToSell, VPos.CENTER);
        baseGridPane.prefHeightProperty().bind(itemOnSaleImageView.fitWidthProperty().multiply(0.8));
        baseGridPane.prefWidthProperty().bind(itemOnSaleImageView.fitWidthProperty().multiply(0.8));
        return baseGridPane;
    }
}
