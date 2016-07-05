package ClientPackage.View.GUIResources.CustomComponent;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.GUIResources.Class.MatchController;
import CommonModel.GameModel.Action.Action;
import CommonModel.GameModel.Action.MainActionBuildWithKingHelp;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import CommonModel.GameModel.City.City;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.ArrayList;

/**
 * Created by Emanuele on 14/06/2016.
 */
public class FinishKingMoveHandler implements EventHandler<ActionEvent> {

    private final ClientController clientController;
    private final MatchController matchController;

    public FinishKingMoveHandler(ClientController clientController, MatchController matchController) {
        this.clientController = clientController;
        this.matchController = matchController;
    }

    @Override
    public void handle(ActionEvent event) {
        if (this.matchController.getBuildWithKingPhase()) {
            ArrayList<City> citiesForBuildWithKing = (ArrayList<City>) this.matchController.getKingPathforBuild().clone();
            if (citiesForBuildWithKing.size() > 0) {
                if (!citiesForBuildWithKing.get(0).equals(this.clientController.getSnapshot().getKing().getCurrentCity())) {
                    citiesForBuildWithKing.add(0, this.clientController.getSnapshot().getKing().getCurrentCity());
                }
            }
            Action action = new MainActionBuildWithKingHelp(citiesForBuildWithKing, (ArrayList<PoliticCard>) this.matchController.getPoliticCardforBuildWithKing().clone());
            new Thread(() -> {
                this.clientController.doAction(action);
            }).start();
            this.matchController.finishKingPhase();
        } else {
        }

    }
}
