package ClientPackage.View.GUIResources.CustomComponent;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.GUIResources.Class.MatchController;
import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import Utilities.Class.Graphics;
import javafx.beans.property.BooleanProperty;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * Created by Emanuele on 19/06/2016.
 */
public class OldPermitCardHandler implements EventHandler<MouseEvent> {

    private final PermitCard permitCard;
    private final BooleanProperty needToSelectOldPermitCard;
    private final ClientController clientController;
    private final MatchController matchController;

    public OldPermitCardHandler(PermitCard permitCard, BooleanProperty needToSelectOldPermitCard, ClientController clientController, MatchController matchController) {
        this.permitCard = permitCard;
        this.needToSelectOldPermitCard = needToSelectOldPermitCard;
        this.clientController = clientController;
        this.matchController = matchController;

    }

    @Override
    public void handle(MouseEvent event) {
        Graphics.playSomeSound("Button");
        if (this.needToSelectOldPermitCard.getValue()) {
            this.clientController.onSelectOldPermitCard(this.permitCard);
            this.matchController.onSelectOldPermitCard();
        }
    }
}
