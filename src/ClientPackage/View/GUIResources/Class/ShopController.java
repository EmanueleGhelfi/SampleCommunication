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
import javafx.collections.FXCollections;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Emanuele on 30/05/2016.
 */
public class ShopController implements BaseController {

    ClientController clientController;
    GUIView guiView;

    @FXML private JFXListView buyListView;
    @FXML private JFXListView sellListView;
    @FXML private BorderPane shop;
    @FXML private JFXButton sellButton;
    @FXML private JFXButton buyButton;
    @FXML private  JFXButton finishButton;


    private boolean sellPhase=false;
    private boolean buyPhase=false;

    ArrayList<BuyableWrapper> sellList = new ArrayList<>();
    ArrayList<BuyableWrapper> buyList = new ArrayList<>();
    ShopController shopController=this;

    public void onBuy(ActionEvent actionEvent) {

        Runnable runnable = () -> {
            System.out.println("in shopController"+buyList);
            clientController.onBuy(buyList);
        };
        new Thread(runnable).start();

    }

    public void onSell(ActionEvent actionEvent) {
            Runnable runnable = () -> {
                ArrayList<BuyableWrapper> realSaleList = new ArrayList<>();
                for (BuyableWrapper buyableWrapper : sellList) {
                    System.out.println("Costo:" + buyableWrapper.getCost());
                    if(buyableWrapper.isOnSale()){
                        System.out.println("found on sale");
                        realSaleList.add(buyableWrapper);
                    }
                }
                if(realSaleList.size()>0) {
                    System.out.println("in shopController" + buyList);
                    clientController.sendSaleItem(realSaleList);
                }
            };
            new Thread(runnable).start();
    }

    @Override
    public void updateView() {
        System.out.println("On update shopController");
        SnapshotToSend snapshotTosend= clientController.getSnapshot();
        sellList= new ArrayList<>();
        buyList = snapshotTosend.getMarketList();

        /*
        for(BuyableWrapper buyableWrapper: buyList){
            if(buyableWrapper.getUsername().equals(snapshotTosend.getCurrentUser().getUsername())){
                sellList.add(buyableWrapper);
                buyList.remove(buyableWrapper);
            }
        }
        */



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

        sellListView.getItems().clear();
        sellListView.setItems(FXCollections.observableArrayList(sellList));
        sellListView.setCellFactory(new Callback<JFXListView, JFXListCell>() {
            @Override
            public JFXListCell call(JFXListView param) {
                return new SellListCell(sellListView,clientController);
            }
        });

        sellListView.getStyleClass().add("jfx-list-view");
        sellListView.getStyleClass().add("mylistview");
        sellListView.autosize();
        sellListView.refresh();



        buyListView.getItems().clear();
        buyListView.setItems(FXCollections.observableArrayList(buyList));
        buyListView.setCellFactory(new Callback<JFXListView, JFXListCell>() {
            @Override
            public JFXListCell call(JFXListView param) {
                return new BuyListCell(param,shopController);
            }
        });

        buyListView.getStyleClass().add("jfx-list-view");
        buyListView.getStyleClass().add("mylistview");
        buyListView.autosize();
        buyListView.refresh();


    }

    @Override
    public void setClientController(ClientController clientController, GUIView guiView) {
        System.out.println("Set client controller");
        this.clientController = clientController;
        this.guiView = guiView;
        guiView.registerBaseController(this);

        manageUI();

        updateView();
    }

    //OLD
    private void manageUI() {
        /*
        sellListView.prefWidthProperty().bind(shopBackground.widthProperty().divide(3));
        sellListView.prefHeightProperty().bind(shopBackground.heightProperty().divide(1.2));


        buyListView.prefWidthProperty().bind(shopBackground.widthProperty().divide(3));
        buyListView.prefHeightProperty().bind(shopBackground.heightProperty().divide(1.2));
        */
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

    public void addItemToBuy(BuyableWrapper item) {
        if(!buyList.contains(item)){
            buyList.add(item);
        }

    }

    public void removeItemToBuy(BuyableWrapper item) {
        if(buyList.contains(item)){
            buyList.remove(item);
        }
    }

    public void onFinishMarket(ActionEvent actionEvent) {
        if(sellPhase){
            sellPhase=false;
            clientController.sendFinishSellPhase();
        }
        else if(buyPhase){

            buyPhase = false;
            clientController.sendFinishedBuyPhase();
        }
    }
}
