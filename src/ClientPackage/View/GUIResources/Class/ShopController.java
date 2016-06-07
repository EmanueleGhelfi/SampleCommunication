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
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.util.Callback;
import org.controlsfx.control.spreadsheet.Grid;

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
    @FXML private GridPane gridPane;

    private ScrollPane sellPane;
    private ScrollPane buyPane;


    private boolean sellPhase=false;
    private boolean buyPhase=false;

    ArrayList<BuyableWrapper> sellList = new ArrayList<>();
    ArrayList<BuyableWrapper> buyList = new ArrayList<>();
    ArrayList<BuyableWrapper> toBuy = new ArrayList<>();
    ShopController shopController=this;

    public void onBuy(ActionEvent actionEvent) {

        Runnable runnable = () -> {
            System.out.println("in shopController"+buyList);
            clientController.onBuy(toBuy);
            toBuy.clear();
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
        updateList();
        manageUI();

/*
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
        buyListView.refresh();
        */


    }

    private void updateList() {
        sellList= new ArrayList<>();
        SnapshotToSend snapshotTosend = clientController.getSnapshot();
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
    }

    @Override
    public void setClientController(ClientController clientController, GUIView guiView) {
        System.out.println("Set client controller");
        this.clientController = clientController;
        this.guiView = guiView;
        guiView.registerBaseController(this);

        manageUI();

        updateView();

        onFinishMarket();
    }

    //NEW
    private void manageUI() {
        sellPane = new ScrollPane();
        GridPane gridPane = new GridPane();
        double columnNumber = 4;
        double rowNumber = Math.ceil(sellList.size()/columnNumber);
        for(int i = 0; i< columnNumber;i++){
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPercentWidth(100/columnNumber);
            gridPane.getColumnConstraints().add(i,columnConstraints);
            for(int j = 0; j<rowNumber;j++){
                RowConstraints rowConstraints = new RowConstraints();
                rowConstraints.setPercentHeight(100/rowNumber);
                gridPane.getRowConstraints().add(rowConstraints);
                createCell(i,j);
            }
        }

    }

    private void createCell(int i, int j) {
        GridPane gridPaneInternal = new GridPane();
        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(5);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(70);
        RowConstraints row3 = new RowConstraints();
        row3.setPercentHeight(10);
        RowConstraints row4 = new RowConstraints();
        row4.setPercentHeight(10);
        ColumnConstraints columnConstraints= new ColumnConstraints();
        columnConstraints.setPercentWidth(5);


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
}
