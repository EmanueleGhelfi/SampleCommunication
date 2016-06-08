package ClientPackage.View.GUIResources.Class;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.GUIResources.customComponent.BuyListCell;
import ClientPackage.View.GUIResources.customComponent.SellListCell;
import ClientPackage.View.GeneralView.GUIView;
import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import CommonModel.GameModel.Council.Helper;
import CommonModel.GameModel.Market.BuyableWrapper;
import CommonModel.Snapshot.SnapshotToSend;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.util.Callback;
import jfxtras.scene.control.ListSpinner;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;

import static sun.management.snmp.jvminstr.JvmThreadInstanceEntryImpl.ThreadStateMap.Byte0.runnable;

/**
 * Created by Emanuele on 30/05/2016.
 */
public class ShopController implements BaseController, Initializable {

    ClientController clientController;
    GUIView guiView;

    @FXML private JFXListView buyListView;
    @FXML private JFXListView sellListView;
    @FXML private BorderPane shop;
    @FXML private JFXButton sellButton;
    @FXML private JFXButton buyButton;
    @FXML private JFXButton finishButton;
    @FXML private GridPane background;
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
            sellPane.getChildren().add(addItems(buyableWrapper));
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
        sellPane.prefWidthProperty().bind(sellScroll.widthProperty());
        sellPane.prefHeightProperty().bind(sellScroll.heightProperty());
        updateView();
        onFinishMarket();
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
        ImageView upper = new ImageView(new Image("/ClientPackage/View/GUIResources/Image/plus.png"));
        ImageView downer = new ImageView(new Image("/ClientPackage/View/GUIResources/Image/minus.png"));
        JFXButton button = new JFXButton();
        button.setText("0");
        button.setStyle("-fx-background-color: wheat");
        upper.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (Integer.parseInt(button.getText()) - 1 < 20)
                button.setText(Integer.toString(Integer.parseInt(button.getText()) + 1));
            }
        });
        downer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (Integer.parseInt(button.getText()) - 1 > 0)
                    button.setText(Integer.toString(Integer.parseInt(button.getText()) - 1));
            }
        });
        upper.setVisible(false);
        downer.setVisible(false);
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
        ImageView imageView = new ImageView(image);
        if (information.getBuyableObject() instanceof PermitCard){
            Label label = new Label();
            label.setText(information.getBuyableObject().getUrl());
            baseGridPane.add(label, 0, 1);
            image = new Image("/ClientPackage/View/GUIResources/Image/PermitCard.png");

        } else {
            image = new Image("/ClientPackage/View/GUIResources/Image/" + information.getBuyableObject().getUrl() + ".png");
        }
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
        sellPane.setHgap(50);
        sellPane.setVgap(50);
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
