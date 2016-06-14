package ClientPackage.View.GUIResources.customComponent;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.GUIResources.Class.MatchController;
import CommonModel.GameModel.Action.Action;
import CommonModel.GameModel.Action.MainActionBuildWithKingHelp;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * Created by Emanuele on 14/06/2016.
 */
public class FInishKingActionHandler implements EventHandler<ActionEvent>{

    private ClientController clientController;
    private MatchController matchController;

    public FInishKingActionHandler(ClientController clientController, MatchController matchController) {
        this.clientController = clientController;
        this.matchController = matchController;
    }


    @Override
    public void handle(ActionEvent event) {
        if(matchController.getBuildWithKingPhase()){
            Action action = new MainActionBuildWithKingHelp(matchController.getKingPathforBuild(),matchController.getPoliticCardforBuildWithKing());

            matchController.finishKingPhase();

            new Thread(()->{
                clientController.doAction(action);
            }).start();
            System.out.println("Doing action");
        }
        else{
            System.out.println("Not action");

        }

    }
}
