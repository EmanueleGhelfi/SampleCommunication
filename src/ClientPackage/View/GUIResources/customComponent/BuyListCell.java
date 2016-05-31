package ClientPackage.View.GUIResources.customComponent;

import ClientPackage.View.GUIResources.Class.ShopController;
import CommonModel.GameModel.Market.BuyableWrapper;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

/**
 * Created by Emanuele on 30/05/2016.
 */
public class BuyListCell extends JFXListCell<BuyableWrapper> {


    JFXListView jfxListView;
    ShopController shopController;

    public BuyListCell(JFXListView listView, ShopController shopController) {
        this.jfxListView = listView;
        this.shopController = shopController;
    }

    @Override
    public void updateItem(BuyableWrapper item, boolean empty) {
        super.updateItem(item, empty);
        if(!empty) {
            BuyableWrapper currentObject = item;

            GridPane gridPane = new GridPane();
            Label labelName = new Label(currentObject.getUsername());
            Label labelCost = new Label(currentObject.getCost()+"");
            Label labelInfo = new Label(currentObject.getBuyableObject().getInfo());

            JFXCheckBox checkBox = new JFXCheckBox();
            checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    System.out.println("checked");
                    if(newValue){
                        shopController.addItemToBuy(item);
                    }
                    else{
                        shopController.removeItemToBuy(item);
                    }
                }
            });
            ColumnConstraints col1 = new ColumnConstraints();
            col1.setPercentWidth(20);
            ColumnConstraints col2 = new ColumnConstraints();
            col2.setPercentWidth(40);
            ColumnConstraints col3 = new ColumnConstraints();
            col3.setPercentWidth(20);
            ColumnConstraints col4 = new ColumnConstraints();
            col3.setPercentWidth(20);


            gridPane.getColumnConstraints().addAll(col1,col2,col3,col4);
            gridPane.add(labelName,0,0);
            gridPane.add(labelInfo,1,0);
            gridPane.add(labelCost,2,0);
            gridPane.add(checkBox,3,0);

            gridPane.setHgap(10);
            gridPane.setPrefHeight(40);
            gridPane.setPrefWidth(jfxListView.getWidth());
            setPrefHeight(40);
            setPrefWidth(jfxListView.getWidth());
            gridPane.setAlignment(Pos.CENTER);
            setGraphic(gridPane);

        }
        else {
            setGraphic(null);
        }
    }
}
