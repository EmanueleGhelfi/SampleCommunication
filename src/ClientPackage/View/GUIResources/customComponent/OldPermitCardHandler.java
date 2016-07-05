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

    private PermitCard permitCard;
    private  BooleanProperty needToSelectOldPermitCard;
    private ClientController clientController;
    private MatchController matchController;

    public OldPermitCardHandler(PermitCard permitCard, BooleanProperty needToSelectOldPermitCard, ClientController clientController, MatchController matchController) {
        this.permitCard = permitCard;
        this.needToSelectOldPermitCard = needToSelectOldPermitCard;
        this.clientController = clientController;
        this.matchController = matchController;

    }

    @Override
    public void handle(MouseEvent event) {
        Graphics.playSomeSound("Button");
        if(needToSelectOldPermitCard.getValue()){
            clientController.onSelectOldPermitCard(permitCard);
            matchController.onSelectOldPermitCard();
        }
    }
}
