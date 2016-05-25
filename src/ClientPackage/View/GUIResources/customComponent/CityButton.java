package ClientPackage.View.GUIResources.customComponent;

import CommonModel.GameModel.City.City;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

/**
 * Created by Emanuele on 25/05/2016.
 */
public class CityButton extends Button implements EventHandler<? super T>{

    public CityButton(City city) {
        this.addEventHandler(this);
    }


    @Override
    public void handle(Event event) {

    }
}
