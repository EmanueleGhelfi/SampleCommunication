package ClientPackage.View.GUIResources.customComponent;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.GUIResources.Class.MatchController;
import CommonModel.GameModel.Action.Action;
import CommonModel.GameModel.Action.MainActionBuildWithKingHelp;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import CommonModel.GameModel.City.City;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import jfxtras.scene.layout.GridPane;

import java.util.ArrayList;

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
            //Action action = new MainActionBuildWithKingHelp((ArrayList<City>)matchController.getKingPathforBuild().clone(),(ArrayList<PoliticCard>)matchController.getPoliticCardforBuildWithKing().clone());

            ArrayList<City> citiesForBuildWithKing = (ArrayList<City>) matchController.getKingPathforBuild().clone();
            if((citiesForBuildWithKing.size())>0){
                if(!citiesForBuildWithKing.get(0).equals(clientController.getSnapshot().getKing().getCurrentCity())){
                    citiesForBuildWithKing.add(0,clientController.getSnapshot().getKing().getCurrentCity());
                }
            }

            Action action = new MainActionBuildWithKingHelp((ArrayList<City>)matchController.getKingPathforBuild()
                    .clone(),(ArrayList<PoliticCard>)matchController.getPoliticCardforBuildWithKing().clone());


            System.out.println("KING PATH "+matchController.getKingPathforBuild());


            new Thread(()->{
                clientController.doAction(action);
            }).start();
            System.out.println("Doing action");
            matchController.finishKingPhase();
        }
        else{
            System.out.println("Not action");

        }

    }
}
