package ClientPackage.View.GUIResources.customComponent;

import CommonModel.GameModel.Market.BuyableObject;
import CommonModel.GameModel.Market.BuyableWrapper;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import java.math.BigDecimal;

/**
 * Created by Emanuele on 30/05/2016.
 */
public class CustomListCell extends JFXListCell<BuyableWrapper> {

    BuyableWrapper buyableObject;
    JFXListView jfxListView;

    public CustomListCell(JFXListView listView) {
        this.jfxListView = listView;

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
            NumberSpinner numberSpinner = new NumberSpinner(0,1);
            numberSpinner.numberProperty().setValue(item.getCost());
            numberSpinner.numberProperty().addListener(new ChangeListener<Integer>() {
                @Override
                public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                    System.out.println("item "+item.getBuyableObject().getInfo()+" has changed to "+newValue);
                    item.setCost(newValue);
                }
            });

            JFXCheckBox checkBox = new JFXCheckBox();
            checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    System.out.println("checked");
                    item.setOnSale(newValue);
                }
            });
            ColumnConstraints col1 = new ColumnConstraints();
            col1.setPercentWidth(25);
            ColumnConstraints col2 = new ColumnConstraints();
            col2.setPercentWidth(25);
            ColumnConstraints col3 = new ColumnConstraints();
            col3.setPercentWidth(25);
            ColumnConstraints col4 = new ColumnConstraints();
            col4.setPercentWidth(10);
            ColumnConstraints col5 = new ColumnConstraints();
            col5.setPercentWidth(10);
            gridPane.getColumnConstraints().addAll(col1,col2,col3,col4,col5);
            gridPane.add(labelName,0,0);
            gridPane.add(labelCost,1,0);
            gridPane.add(labelInfo,2,0);
            gridPane.add(numberSpinner,3,0);
            gridPane.add(checkBox,4,0);

            gridPane.setVgap(10);
            gridPane.setPrefHeight(30);
            gridPane.setPrefWidth(jfxListView.getWidth());


            setGraphic(gridPane);
        }
        else {
            setGraphic(null);
        }
    }
}
