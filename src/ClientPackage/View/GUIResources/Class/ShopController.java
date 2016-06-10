package ClientPackage.View.GUIResources.Class;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.GeneralView.GUIView;
import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import CommonModel.GameModel.Council.Helper;
import CommonModel.GameModel.Market.BuyableWrapper;
import CommonModel.Snapshot.SnapshotToSend;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;

/**
 * Created by Emanuele on 30/05/2016.
 */
public class ShopController implements BaseController, Initializable {

    ClientController clientController;
    GUIView guiView;

    private ImageView politicCardDeck = new ImageView();
    private ImageView permitCardDeck = new ImageView();
    private ImageView helperDeck = new ImageView();

    @FXML private JFXListView buyListView;
    @FXML private JFXListView sellListView;
    @FXML private BorderPane shop;
    @FXML private JFXButton sellButton;
    @FXML private JFXButton buyButton;
    @FXML private JFXButton finishButton;
    @FXML private Pane shopBackground;
    @FXML private TilePane sellPane;
    @FXML private TilePane buyPane;
    @FXML private ScrollPane sellScroll;

    private boolean sellPhase=false;
    private boolean buyPhase=false;
    private boolean confirming;

    ArrayList<BuyableWrapper> sellList = new ArrayList<>();
    ArrayList<BuyableWrapper> buyList = new ArrayList<>();
    ArrayList<BuyableWrapper> toBuy = new ArrayList<>();
    ArrayList<BuyableWrapper> trueList = new ArrayList<>();

    ShopController shopController=this;

    public void onBuy(ActionEvent actionEvent) {
        Runnable runnable = () -> {
            clientController.onBuy(toBuy);
            toBuy.clear();
        };
        new Thread(runnable).start();
    }

    public void onSell(ActionEvent actionEvent) {
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
        SnapshotToSend snapshotTosend= clientController.getSnapshot();
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

        for (BuyableWrapper buyableWrapper : sellList) {
            //sellPane.getChildren().add(addItems(buyableWrapper));
        }
        for (BuyableWrapper buyableWrapper : buyList) {
            buyPane.getChildren().add(addItems(buyableWrapper));
        }
    }

    @Override
    public void setClientController(ClientController clientController, GUIView guiView) {
        System.out.println("Set client controller");
        this.clientController = clientController;
        this.guiView = guiView;
        guiView.registerBaseController(this);
        //sellPane.prefWidthProperty().bind(sellScroll.widthProperty());
        //sellPane.prefHeightProperty().bind(sellScroll.heightProperty());
        updateView();
        onFinishMarket();
        detectClick();
        createDeck();
    }

    private void createDeck() {
        politicCardDeck.setImage(new Image("/ClientPackage/View/GUIResources/Image/PoliticCardDistorted.png"));
        permitCardDeck.setImage(new Image("/ClientPackage/View/GUIResources/Image/PermitCardsDistorted.png"));
        helperDeck.setImage(new Image("/ClientPackage/View/GUIResources/Image/HelperDistorted.png"));

        shopBackground.getChildren().addAll(politicCardDeck, permitCardDeck, helperDeck);

        politicCardDeck.layoutXProperty().bind(shopBackground.widthProperty().multiply(0.3883));
        politicCardDeck.layoutYProperty().bind(shopBackground.heightProperty().multiply(0.5612));
        permitCardDeck.layoutXProperty().bind(shopBackground.widthProperty().multiply(0.4591));
        permitCardDeck.layoutYProperty().bind(shopBackground.heightProperty().multiply(0.5552));
        helperDeck.layoutXProperty().bind(shopBackground.widthProperty().multiply(0.535));
        helperDeck.layoutYProperty().bind(shopBackground.heightProperty().multiply(0.5597));

        politicCardDeck.fitWidthProperty().bind(shopBackground.prefWidthProperty().divide(10));
        politicCardDeck.fitHeightProperty().bind(shopBackground.prefWidthProperty().divide(10));
        permitCardDeck.fitWidthProperty().bind(shopBackground.prefWidthProperty().divide(10));
        permitCardDeck.fitHeightProperty().bind(shopBackground.prefWidthProperty().divide(10));
        helperDeck.fitWidthProperty().bind(shopBackground.prefWidthProperty().divide(10));
        helperDeck.fitHeightProperty().bind(shopBackground.prefWidthProperty().divide(10));

    }

    private void detectClick() {
        shopBackground.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("event x and y "+event.getX()/shopBackground.getWidth()+" "+event.getY()/shopBackground.getHeight());
                System.out.println("Scene  "+event.getSceneX()+" "+event.getSceneY());
                System.out.println("Altro "+event.getScreenX()+" "+event.getScreenY());
            }
        });
    }

    @Override
    public void setMyTurn(boolean myTurn, SnapshotToSend snapshot) {

    }

    @Override
    public void onStartMarket() {
        sellPhase = true;
        buyPhase = false;
        buyButton.setDisable(true);
        finishButton.setDisable(false);
        sellButton.setDisable(false);
    }

    @Override
    public void onStartBuyPhase() {
        buyPhase=true;
        buyButton.setDisable(false);
        finishButton.setDisable(false);
        sellButton.setDisable(true);
    }

    @Override
    public void  onFinishMarket() {
        buyButton.setDisable(true);
        finishButton.setDisable(true);
        sellButton.setDisable(true);
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

    public void sendOnFinishMarket(ActionEvent actionEvent) {
        if(sellPhase){
            sellPhase=false;
            clientController.sendFinishSellPhase();
        }
        else if(buyPhase){

            buyPhase = false;
            clientController.sendFinishedBuyPhase();
        }
    }

    private GridPane addItems(BuyableWrapper information){
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
        button.setText("0");
        button.setStyle("-fx-background-color: wheat");
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
            image = new Image("/ClientPackage/View/GUIResources/Image/PermitCard.png");
            upperImage = new Image("/ClientPackage/View/GUIResources/Image/plusWhite.png");
            downerImage = new Image ("/ClientPackage/View/GUIResources/Image/minusWhite.png");

        } else {
            if (information.getBuyableObject() instanceof Helper) {
                image = new Image("/ClientPackage/View/GUIResources/Image/" + information.getBuyableObject().getUrl() + ".png");
                upperImage = new Image("/ClientPackage/View/GUIResources/Image/plusBlack.png");
                downerImage = new Image("/ClientPackage/View/GUIResources/Image/minusBlack.png");
            } else {
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
        //sellPane.setHgap(10);
        //sellPane.setVgap(10);
        System.out.println(" IN TEORIA STO AGGIUNGENDO ");
        return baseGridPane;
    }

    /*
    private void settingResources(BuyableWrapper wrapper, ){
        if (wrapper.getBuyableObject() instanceof PermitCard) {
            Label label = new Label();
            label.setText(wrapper.getBuyableObject().getUrl());
            baseGridPane.add(label, 0, 1);
            imageView.setImage(new Image("/ClientPackage/View/GUIResources/Image/PermitCard.png"));
        } else {
            System.out.println(buyableWrapper.getBuyableObject().getUrl() + " <- LUPIN");
            imageView.setImage(new Image("/ClientPackage/View/GUIResources/Image/"
                    + buyableWrapper.getBuyableObject().getUrl() + ".png"));
        }
    }
    */

    private void changePrice(Text text, boolean upper) {
        int textTaken = Integer.parseInt(text.getText());
        if (upper) {
            text.setText(Integer.toString((textTaken + 1)));
        } else {
            if (textTaken > 0)
                text.setText(Integer.toString((textTaken - 1)));
            else
                text.setText(Integer.toString((textTaken)));
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
