package ClientPackage.View.GUIResources.customComponent;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.GUIResources.Class.MatchController;
import Utilities.Class.Graphics;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Created by Emanuele on 17/06/2016.
 */
public class ChangeTurnHandler implements EventHandler<ActionEvent> {

    private MatchController matchController;

    public ChangeTurnHandler(MatchController matchController) {
        this.matchController=matchController;
    }

    @Override
    public void handle(ActionEvent event) {
        Graphics.playSomeSound("Button");
        ClientController clientController = ClientController.getInstance();
        clientController.onFinishTurn();
    }
}
