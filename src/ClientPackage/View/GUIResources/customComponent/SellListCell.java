package ClientPackage.View.GUIResources.CustomComponent;

import ClientPackage.Controller.ClientController;
import CommonModel.GameModel.Market.BuyableWrapper;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 * Created by Emanuele on 30/05/2016.
 */
public class SellListCell extends JFXListCell<BuyableWrapper> {

    private final ClientController clientController;
    private final JFXListView jfxListView;
    private BuyableWrapper buyableObject;

    public SellListCell(JFXListView listView, ClientController clientController) {
        jfxListView = listView;
        this.clientController = clientController;
    }

    @Override
    public void updateItem(BuyableWrapper item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty) {
            GridPane gridPane = new GridPane();
            Label labelName = new Label(item.getUsername());
            Label labelInfo = new Label(item.getBuyableObject().getInfo());
            NumberSpinner numberSpinner = new NumberSpinner(0, 1);
            numberSpinner.numberProperty().setValue(item.getCost());
            numberSpinner.numberProperty().addListener(new ChangeListener<Integer>() {
                @Override
                public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                    item.setCost(newValue);
                }
            });
            numberSpinner.setPrefWidth(50);
            if (!item.isOnSale()) {
                JFXCheckBox checkBox = new JFXCheckBox();
                checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        item.setOnSale(newValue);
                    }
                });
                checkBox.setSelected(item.isOnSale());
                gridPane.add(checkBox, 3, 0);
            } else {
                JFXButton jfxButton = new JFXButton("REMOVE");
                jfxButton.getStyleClass().add("removeButton");
                gridPane.add(jfxButton, 3, 0);
                jfxButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Runnable runnable = () -> {
                            SellListCell.this.clientController.removeItemFromMarket(item);
                        };
                        new Thread(runnable).start();

                    }
                });
            }
            gridPane.add(labelName, 0, 0);
            gridPane.add(labelInfo, 1, 0);
            gridPane.add(numberSpinner, 2, 0);
            gridPane.setPadding(new Insets(20, 20, 20, 20));
            gridPane.setHgap(20);
            gridPane.setAlignment(Pos.CENTER);
            this.setGraphic(gridPane);
        } else {
            this.setGraphic(null);
        }
    }
}
