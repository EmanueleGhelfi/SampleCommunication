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
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Callback;

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

    private boolean sellPhase=false;
    private boolean buyPhase=false;

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
        sellPane.setPrefColumns(4);
        sellPane.setPrefRows(10);
        sellPane.setHgap(5);
        sellPane.setVgap(5);
        buyPane.setPrefColumns(4);
        buyPane.setPrefRows(10);
        buyPane.setHgap(5);
        buyPane.setVgap(5);
        sellPane.setAlignment(Pos.CENTER);
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
        baseGridPane.prefWidthProperty().bind(sellPane.widthProperty().divide(10));
        baseGridPane.prefHeightProperty().bind(sellPane.heightProperty().divide(10));
        AnchorPane pane = new AnchorPane();
        pane.prefWidthProperty().bind(baseGridPane.widthProperty());
        pane.prefHeightProperty().bind(baseGridPane.heightProperty().divide(2));
        ImageView imageView = new ImageView();
        if (information.getBuyableObject() instanceof PermitCard){
            Label label = new Label();
            label.setText(information.getBuyableObject().getUrl());
            baseGridPane.add(label, 0, 1);
            imageView.setImage(new Image("/ClientPackage/View/GUIResources/Image/PermitCard.png"));
        } else {
            System.out.println(information.getBuyableObject().getUrl() + " <- LUPIN");
            imageView.setImage(new Image("/ClientPackage/View/GUIResources/Image/"
                    + information.getBuyableObject().getUrl() + ".png"));
        }
        imageView.setId("ImageView");
        imageView.fitWidthProperty().bind(pane.widthProperty().divide(5));
        imageView.fitHeightProperty().bind(pane.heightProperty().divide(5));
        CheckBox checkBox = new CheckBox();
        checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue)
                    trueList.add(information);
                else
                    trueList.remove(information);
            }
        });
        pane.getChildren().addAll(imageView, checkBox);
        AnchorPane.setTopAnchor(checkBox, 0.0);
        AnchorPane.setLeftAnchor(checkBox, 0.0);
        baseGridPane.add(pane, 1, 1);
        AnchorPane useablePane = new AnchorPane();
        VBox vBox = new VBox();
        ImageView more = new ImageView(new Image("/ClientPackage/View/GUIResources/Image/More.png"));
        ImageView less = new ImageView(new Image("/ClientPackage/View/GUIResources/Image/Less.png"));
        vBox.getChildren().addAll(more, less);
        useablePane.getChildren().add(vBox);
        AnchorPane.setRightAnchor(vBox, 0.0);
        Text text = new Text();
        text.setText("0");
        more.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                changePrice(text, true);
            }
        });
        less.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                changePrice(text, false);
            }
        });
        useablePane.getChildren().add(text);
        AnchorPane.setLeftAnchor(text, 0.0);
        baseGridPane.add(useablePane, 1, 2);
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
