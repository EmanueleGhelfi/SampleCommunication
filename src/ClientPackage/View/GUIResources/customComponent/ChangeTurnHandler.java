package ClientPackage.View.GUIResources.customComponent;

import ClientPackage.Controller.ClientController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Created by Emanuele on 17/06/2016.
 */
public class ChangeTurnHandler implements EventHandler<ActionEvent> {
    @Override
    public void handle(ActionEvent event) {
        ClientController clientController = ClientController.getInstance();
        clientController.onFinishTurn();
    }
}
