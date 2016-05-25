package ClientPackage.View.GUIResources.customComponent;

import ClientPackage.View.GUIResources.Class.MatchController;
import CommonModel.GameModel.City.City;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.cell.ImageGridCell;

/**
 * Created by Emanuele on 25/05/2016.
 */
public class CityButton extends Button implements EventHandler<MouseEvent>{

    private MatchController matchController;

    public CityButton(City city, MatchController matchController) {
        this.setOnMouseClicked(this);
        this.matchController = matchController;
    }

    @Override
    public void handle(MouseEvent event) {
    }
}
