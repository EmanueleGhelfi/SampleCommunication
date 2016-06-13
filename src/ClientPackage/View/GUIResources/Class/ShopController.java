package ClientPackage.View.GUIResources.Class;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.GeneralView.GUIView;
import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
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

    ClientController clientController;
    GUIView guiView;

    private ImageView backgroundImage = new ImageView();

    private ImageView politicCardDeck = new ImageView();
    private ImageView permitCardDeck = new ImageView();
    private ImageView helperDeck = new ImageView();

    @FXML private GridPane shop;
    @FXML private Pane imagePane;
    //@FXML private Pane shop;
    //@FXML private AnchorPane shopAnchor;
    //@FXML private TilePane sellPane;
    //@FXML private TilePane buyPane;
    private TilePane buyPane = new TilePane();
    private TilePane sellPane = new TilePane();
    @FXML private ScrollPane sellScroll;
    private Button innerButton;
    private PopOver innerPopOver;
    private Pane paneWhereShowPopOver = new Pane();
    private boolean confirming = true;

    ArrayList<BuyableWrapper> sellList = new ArrayList<>();
    ArrayList<BuyableWrapper> temporarySellList = new ArrayList<>();
    ArrayList<BuyableWrapper> buyList = new ArrayList<>();
    ArrayList<BuyableWrapper> toBuy = new ArrayList<>();
    ArrayList<BuyableWrapper> trueList = new ArrayList<>();

    ShopController shopController=this;

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
            }
        };
        new Thread(runnable).start();
    }

    @Override
    public void updateView() {
        System.out.println("On update shopController");
        SnapshotToSend snapshotTosend = clientController.getSnapshot();
        updateList();
    }

    private void updateList() {
        sellList= new ArrayList<>();
        SnapshotToSend snapshotTosend = clientController.getSnapshot();
        buyList = snapshotTosend.getMarketList();

        for(BuyableWrapper buyableWrapper: buyList){
            if(buyableWrapper.getUsername().equals(snapshotTosend.getCurrentUser().getUsername())){
                sellList.add(buyableWrapper);
                buyList.remove(buyableWrapper);
            }
        }

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

        for (BuyableWrapper buyableWrapper : temporarySellList) {
            sellPane.getChildren().add(addSalableItems(buyableWrapper));
        }

        for (BuyableWrapper buyableWrapper : buyList) {
            buyPane.getChildren().add(addBuyableItems(buyableWrapper));
        }
    }

    @Override
    public void setClientController(ClientController clientController, GUIView guiView) {
        System.out.println("Set client controller");
        this.clientController = clientController;
        this.guiView = guiView;
        guiView.registerBaseController(this);
        backgroundImage.setImage(new Image("/ClientPackage/View/GUIResources/Image/ShopBackground.jpg"));
        imagePane.getChildren().add(backgroundImage);
        backgroundImage.setPreserveRatio(true);
        backgroundImage.fitHeightProperty().bind(shop.heightProperty());
        backgroundImage.fitWidthProperty().bind(shop.widthProperty());
        shop.prefWidthProperty().bind(imagePane.prefWidthProperty());
        shop.prefHeightProperty().bind(imagePane.prefHeightProperty());
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
                imagePane.setPrefWidth(newValue.getWidth());
                imagePane.setPrefHeight(newValue.getHeight());
                // gridPane.setPrefSize(background.getPrefWidth(),background.getMaxHeight());

            }
        });
        GridPane.setRowSpan(backgroundImage, 2);
        GridPane.setColumnSpan(backgroundImage, 2);

        paneWhereShowPopOver = new Pane();
        paneWhereShowPopOver.layoutXProperty().bind(imagePane.prefWidthProperty().multiply(0.4937));
        paneWhereShowPopOver.layoutYProperty().bind(imagePane.prefHeightProperty().multiply(0.2254));
        paneWhereShowPopOver.prefWidthProperty().bind(imagePane.prefWidthProperty().multiply(0.01));
        paneWhereShowPopOver.prefHeightProperty().bind(imagePane.prefHeightProperty().multiply(0.01));
        imagePane.getChildren().add(paneWhereShowPopOver);

        updateView();
        detectClick();
        createDeck();
        onFinishMarket();
    }

    private void createDeck() {
        politicCardDeck.setImage(new Image("/ClientPackage/View/GUIResources/Image/PoliticCardDistorted.png"));
        permitCardDeck.setImage(new Image("/ClientPackage/View/GUIResources/Image/PermitCardsDistorted.png"));
        helperDeck.setImage(new Image("/ClientPackage/View/GUIResources/Image/HelperDistorted.png"));

        imagePane.getChildren().addAll(politicCardDeck, permitCardDeck, helperDeck);

        politicCardDeck.layoutXProperty().bind(imagePane.widthProperty().multiply(0.3883));
        politicCardDeck.layoutYProperty().bind(imagePane.heightProperty().multiply(0.5612));
        permitCardDeck.layoutXProperty().bind(imagePane.widthProperty().multiply(0.4591));
        permitCardDeck.layoutYProperty().bind(imagePane.heightProperty().multiply(0.5552));
        helperDeck.layoutXProperty().bind(imagePane.widthProperty().multiply(0.535));
        helperDeck.layoutYProperty().bind(imagePane.heightProperty().multiply(0.5597));


        politicCardDeck.fitWidthProperty().bind(imagePane.widthProperty().divide(10));
        politicCardDeck.fitHeightProperty().bind(imagePane.heightProperty().divide(10));
        permitCardDeck.fitWidthProperty().bind(imagePane.widthProperty().divide(10));
        permitCardDeck.fitHeightProperty().bind(imagePane.heightProperty().divide(10));
        helperDeck.fitWidthProperty().bind(imagePane.widthProperty().divide(10));
        helperDeck.fitHeightProperty().bind(imagePane.heightProperty().divide(10));

        innerButton = new Button();
        innerButton.setStyle("-fx-background-color: transparent");
        innerButton.layoutXProperty().bind(imagePane.prefWidthProperty().multiply(0.3504));
        innerButton.layoutYProperty().bind(imagePane.prefHeightProperty().multiply(0.1455));
        innerButton.prefWidthProperty().bind(imagePane.prefWidthProperty().multiply(0.5208 - 0.3604));
        innerButton.prefHeightProperty().bind(imagePane.prefHeightProperty().multiply(0.5530 - 0.1455));
        innerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                talkToInnerKeeper();
            }
        });
        imagePane.getChildren().addAll(innerButton);
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

    private void talkToInnerKeeper() {
        innerPopOver = new PopOver();
        VBox innerVBox = new VBox();
        Label innerLabel = new Label("Sei sicuro?");
        JFXButton innerPopOverButton = new JFXButton("OK");
        innerVBox.getChildren().addAll(innerLabel, innerPopOverButton);
        innerPopOver.setContentNode(innerButton);
        innerPopOver.show(paneWhereShowPopOver);
        innerPopOverButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                innerPopOver.hide();
                onSell();
                clientController.sendFinishSellPhase();
            }
        });
    }

    private void goToBuyingSession() {
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
            }
        });
        innerPopOver.setContentNode(buyPane);
        innerPopOver.show(paneWhereShowPopOver);
    }

    private void shopPopOver(ImageView imageClicked, Class objectClass) {
        temporarySellList.clear();
        sellList.forEach(buyableWrapper -> {
            if (buyableWrapper.getBuyableObject().getClass().equals(objectClass)){
                temporarySellList.add(buyableWrapper);
            }
        });
        PopOver popOver = new PopOver();
        populateSellAndBuyPane();
        popOver.setContentNode(sellPane);
        popOver.prefWidthProperty().bind(imagePane.prefHeightProperty().divide(10));
        popOver.prefHeightProperty().bind(imagePane.prefWidthProperty().divide(10));
        popOver.show(imageClicked);
    }

    private void detectClick() {
        imagePane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("event x and y "+event.getX()/ shop.getWidth()+" "+event.getY()/ shop.getHeight());
                System.out.println("Scene  "+event.getSceneX()+" "+event.getSceneY());
                System.out.println("Altro "+event.getScreenX()+" "+event.getScreenY());
            }
        });
    }

    @Override
    public void setMyTurn(boolean myTurn, SnapshotToSend snapshot) {

    }


    //TODO inizia fase vendita
    @Override
    public void onStartMarket() {
        Graphics.notification("Start Market");
        settingDeckActions();
        innerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                talkToInnerKeeper();
            }
        });
    }

    //TODO inizia fase compraggio
    @Override
    public void onStartBuyPhase() {
        goToBuyingSession();
        settingDeckActions();
        politicCardDeck.setOnMouseClicked(null);
        permitCardDeck.setOnMouseClicked(null);
        helperDeck.setOnMouseClicked(null);
        Graphics.notification("Start Buy Phase");
    }

    //TODO finisco tutto e disabilito
    @Override
    public void  onFinishMarket() {
        Graphics.notification("Finish Market");
        politicCardDeck.setOnMouseClicked(null);
        permitCardDeck.setOnMouseClicked(null);
        helperDeck.setOnMouseClicked(null);
        innerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                nothingToDo();
            }
        });

    }

    private void nothingToDo() {
        if (paneWhereShowPopOver != null) {
            PopOver popOverToShow = new PopOver();
            Pane internalPopOverPane = new Pane();
            internalPopOverPane.getChildren().add(new Label("Bottega chiusa, viandante!"));
            popOverToShow.setContentNode(internalPopOverPane);

            popOverToShow.show(paneWhereShowPopOver);
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

    }

    @Override
    public void selectCityRewardBonus() {

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

    private GridPane addBuyableItems(BuyableWrapper buyableWrapper) {
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
        JFXButton button = new JFXButton();
        button.setText(Integer.toString(buyableWrapper.getCost()));
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toBuy.add(buyableWrapper);
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
        JFXButton button = new JFXButton();
        button.setTextFill(Paint.valueOf("WHITE"));
        button.setText("0");
        button.setVisible(false);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (confirming) {
                    trueList.add(information);
                    button.setText("REMOVE");
                    confirming = false;
                }
                else{
                    trueList.remove(information);
                    button.setText("0");
                    confirming = true;
                }
            }
        });
        Image image = null;
        Image upperImage = null;
        Image downerImage = null;
        ImageView imageView = new ImageView(image);
        if (information.getBuyableObject() instanceof PermitCard){
            Label label = new Label();
            label.setText(information.getBuyableObject().getUrl());
            baseGridPane.add(label, 0, 1);
            imageView.setId("PermitCard");
            image = new Image("/ClientPackage/View/GUIResources/Image/PermitCard.png");
            upperImage = new Image("/ClientPackage/View/GUIResources/Image/plusWhite.png");
            downerImage = new Image ("/ClientPackage/View/GUIResources/Image/minusWhite.png");

        } else {
            if (information.getBuyableObject() instanceof Helper) {
                image = new Image("/ClientPackage/View/GUIResources/Image/" + information.getBuyableObject().getUrl() + ".png");
                imageView.setId("Helper");
                upperImage = new Image("/ClientPackage/View/GUIResources/Image/plusBlack.png");
                downerImage = new Image("/ClientPackage/View/GUIResources/Image/minusBlack.png");
            } else {
                imageView.setId("PoliticCard");
                image = new Image("/ClientPackage/View/GUIResources/Image/" + information.getBuyableObject().getUrl() + ".png");
                upperImage = new Image("/ClientPackage/View/GUIResources/Image/plusWhite.png");
                downerImage = new Image ("/ClientPackage/View/GUIResources/Image/minusWhite.png");
            }
        }
        ImageView upper = new ImageView(upperImage);
        ImageView downer = new ImageView(downerImage);
        upper.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (Integer.parseInt(button.getText()) - 1 < 19)
                    button.setText(Integer.toString(Integer.parseInt(button.getText()) + 1));
            }
        });
        downer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (Integer.parseInt(button.getText()) - 1 > 0)
                    button.setText(Integer.toString(Integer.parseInt(button.getText()) - 1));
                else
                    button.setText(Integer.toString(0));
            }
        });
        upper.setVisible(false);
        downer.setVisible(false);
        imageView.setImage(image);
        imageView.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                upper.setVisible(true);
                downer.setVisible(true);
                button.setVisible(true);
                ColorAdjust colorAdjust = new ColorAdjust();
                colorAdjust.setBrightness(-0.5);
                imageView.setEffect(colorAdjust);
            }
        });
        baseGridPane.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                upper.setVisible(false);
                downer.setVisible(false);
                button.setVisible(false);
                imageView.setEffect(null);
            }
        });
        baseGridPane.add(imageView, 0, 0);
        GridPane.setColumnSpan(imageView, 3);
        GridPane.setRowSpan(imageView, 3);
        baseGridPane.add(upper, 0, 1);
        baseGridPane.add(button, 1, 1);
        baseGridPane.add(downer, 2, 1);
        upper.setPreserveRatio(false);
        upper.setFitWidth(40);
        upper.setFitHeight(40);
        downer.setPreserveRatio(false);
        downer.setFitWidth(40);
        downer.setFitHeight(40);
        GridPane.setHalignment(upper, HPos.CENTER);
        GridPane.setHalignment(downer, HPos.CENTER);
        GridPane.setHalignment(button, HPos.CENTER);
        GridPane.setValignment(upper, VPos.CENTER);
        GridPane.setValignment(downer, VPos.CENTER);
        GridPane.setValignment(button, VPos.CENTER);
        baseGridPane.prefHeightProperty().bind(imageView.fitWidthProperty().multiply(0.8));
        baseGridPane.prefWidthProperty().bind(imageView.fitWidthProperty().multiply(0.8));
        return baseGridPane;
    }

}
