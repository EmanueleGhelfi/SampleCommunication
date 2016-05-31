package ClientPackage.View.GUIResources.customComponent;

import CommonModel.GameModel.Market.BuyableObject;
import CommonModel.GameModel.Market.BuyableWrapper;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
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


            GridPane gridPane = new GridPane();
            Label labelName = new Label(item.getUsername());
            Label labelInfo = new Label(item.getBuyableObject().getInfo());
            NumberSpinner numberSpinner = new NumberSpinner(0,1);
            numberSpinner.numberProperty().setValue(item.getCost());
            numberSpinner.numberProperty().addListener(new ChangeListener<Integer>() {
                @Override
                public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                    System.out.println("item "+item.getBuyableObject().getInfo()+" has changed to "+newValue);
                    item.setCost(newValue);
                }
            });

            if(!item.isOnSale()) {
                JFXCheckBox checkBox = new JFXCheckBox();
                checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        System.out.println("checked");
                        item.setOnSale(newValue);
                    }
                });
                checkBox.setSelected(item.isOnSale());
                gridPane.add(checkBox,3,0);
            }
            else{
                JFXButton jfxButton = new JFXButton("REMOVE");
                jfxButton.getStyleClass().add("removeButton");
                gridPane.add(jfxButton,3,0);
                jfxButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        System.out.println("pressed");
                    }
                });
            }

            ColumnConstraints col1 = new ColumnConstraints();
            col1.setPercentWidth(20);
            ColumnConstraints col2 = new ColumnConstraints();
            col2.setPercentWidth(40);
            ColumnConstraints col3 = new ColumnConstraints();
            col3.setPercentWidth(20);
            ColumnConstraints col4 = new ColumnConstraints();
            col4.setPercentWidth(30);

            gridPane.getColumnConstraints().addAll(col1,col2,col3,col4);
            gridPane.add(labelName,0,0);
            gridPane.add(labelInfo,1,0);
            gridPane.add(numberSpinner,2,0);


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
