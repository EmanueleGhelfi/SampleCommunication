package ClientPackage.View.GUIResources.Class;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.GUIResources.customComponent.BuyListCell;
import ClientPackage.View.GUIResources.customComponent.CustomListCell;
import ClientPackage.View.GeneralView.GUIView;
import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import CommonModel.GameModel.Market.BuyableWrapper;
import CommonModel.Snapshot.SnapshotToSend;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import java.util.ArrayList;

/**
 * Created by Emanuele on 30/05/2016.
 */
public class ShopController implements BaseController {

    ClientController clientController;
    GUIView guiView;

    @FXML private JFXListView buyListView;
    @FXML private JFXListView sellListView;

    ArrayList<BuyableWrapper> sellList = new ArrayList<>();
    ArrayList<BuyableWrapper> buyList = new ArrayList<>();
    ShopController shopController=this;

    public void onBuy(ActionEvent actionEvent) {

    }

    public void onSell(ActionEvent actionEvent) {
        ArrayList<BuyableWrapper> realSaleList = new ArrayList<>();
        //TODO: look for checked
        for (BuyableWrapper buyableWrapper : sellList) {
            System.out.println("Costo:" + buyableWrapper.getCost());
            if(buyableWrapper.isOnSale()){
                realSaleList.add(buyableWrapper);
            }
        }

        if(realSaleList.size()>0){
            clientController.sendSaleItem(realSaleList);
        }
    }

    @Override
    public void updateView() {
        System.out.println("On update shopController");
        SnapshotToSend snapshotTosend= clientController.getSnapshot();
        sellList= new ArrayList<>();
        for (PoliticCard politicCard: snapshotTosend.getCurrentUser().getPoliticCards()) {
            sellList.add(new BuyableWrapper(politicCard,snapshotTosend.getCurrentUser().getUsername()));
        }

        for(PermitCard permitCard: snapshotTosend.getCurrentUser().getPermitCards()){
            sellList.add(new BuyableWrapper(permitCard,snapshotTosend.getCurrentUser().getUsername()));
        }

        sellListView.setItems(FXCollections.observableArrayList(sellList));
        sellListView.setCellFactory(new Callback<JFXListView, JFXListCell>() {
            @Override
            public JFXListCell call(JFXListView param) {
                return new CustomListCell(sellListView);
            }
        });

        sellListView.getStyleClass().add("jfx-list-view");
        sellListView.getStyleClass().add("mylistview");
        sellListView.autosize();

        buyList = snapshotTosend.getMarketList();

        buyListView.setItems(FXCollections.observableArrayList(buyList));
        sellListView.setCellFactory(new Callback<JFXListView, JFXListCell>() {
            @Override
            public JFXListCell call(JFXListView param) {
                return new BuyListCell(param,shopController);
            }
        });


    }

    @Override
    public void setClientController(ClientController clientController, GUIView guiView) {
        System.out.println("Set client controller");
        this.clientController = clientController;
        this.guiView = guiView;
        guiView.registerBaseController(this);
        updateView();
    }

    @Override
    public void setMyTurn(boolean myTurn, SnapshotToSend snapshot) {

    }

    public void addItemToBuy(BuyableWrapper item) {
    }

    public void removeItemToBuy(BuyableWrapper item) {
    }
}
