package ClientPackage.View.GUIResources.CustomComponent;

import ClientPackage.View.GUIResources.Class.ShopController;
import CommonModel.GameModel.Market.BuyableWrapper;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 * Created by Emanuele on 30/05/2016.
 */
public class BuyListCell extends JFXListCell<BuyableWrapper> {

    private final JFXListView jfxListView;
    private final ShopController shopController;

    public BuyListCell(JFXListView listView, ShopController shopController) {
        jfxListView = listView;
        this.shopController = shopController;
    }

    @Override
    public void updateItem(BuyableWrapper item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty) {
            BuyableWrapper currentObject = item;
            GridPane gridPane = new GridPane();
            Label labelName = new Label(currentObject.getUsername());
            Label labelCost = new Label(currentObject.getCost() + "");
            Label labelInfo = new Label(currentObject.getBuyableObject().getInfo());
            JFXCheckBox checkBox = new JFXCheckBox();
            checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (newValue) {
                        BuyListCell.this.shopController.addItemToBuy(item);
                    } else {
                        BuyListCell.this.shopController.removeItemToBuy(item);
                    }
                }
            });
            gridPane.add(labelName, 0, 0);
            gridPane.add(labelInfo, 1, 0);
            gridPane.add(labelCost, 2, 0);
            gridPane.add(checkBox, 3, 0);
            gridPane.setPadding(new Insets(20, 20, 20, 20));
            gridPane.setAlignment(Pos.CENTER);
            gridPane.setHgap(20);
            this.setGraphic(gridPane);
        } else {
            this.setGraphic(null);
        }
    }
}
