package ClientPackage.View.GUIResources.customComponent;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.GUIResources.Class.MatchController;
import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.PopOver;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Emanuele on 26/05/2016.
 */
public class PermitCardHandler implements EventHandler<MouseEvent> {

    PermitCard permitCard;
    MatchController matchController;
    ClientController clientController;
    PopOver popOver;
    ArrayList<PoliticCard> politicCards = new ArrayList<>();

    public PermitCardHandler(PermitCard permitCard, MatchController matchController, ClientController clientController) {
        this.permitCard = permitCard;
        this.matchController = matchController;
        this.clientController = clientController;

    }

    @Override
    public void handle(MouseEvent event) {
        politicCards = (ArrayList<PoliticCard>)  clientController.getSnapshot().getCurrentUser().getPoliticCards().clone();
        popOver = new PopOver();
        Pane paneOfPopup = new Pane();
        GridPane imageView = (GridPane) event.getSource();
        JFXCheckBox politicCardsCheckBox;
        VBox vBox = new VBox();
        for (PoliticCard politicCard: politicCards){
            politicCardsCheckBox = new JFXCheckBox();
            System.out.println(politicCard);
            String stringa;
            if(politicCard.getPoliticColor()==null){
                stringa="MULTICOLOR";
            }
            else{
                stringa=politicCard.getPoliticColor().getColor();
            }

            politicCardsCheckBox.setText(stringa);

            politicCardsCheckBox.setId("JFXCheckBox");
            vBox.getChildren().add(politicCardsCheckBox);
        }
        JFXButton jfxButton = new JFXButton();
        jfxButton.setButtonType(JFXButton.ButtonType.FLAT);
        jfxButton.setText("OK MAN");
        jfxButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ArrayList<PoliticCard> politicCardSelected = new ArrayList<PoliticCard>();
                Set<Node> jfxCheckBoxArrayList = (Set<Node>) vBox.lookupAll("#JFXCheckBox");
                for (Node node : jfxCheckBoxArrayList) {
                    JFXCheckBox jfxCheckBoxTempTemp = (JFXCheckBox) node;
                    if (jfxCheckBoxTempTemp.isSelected()) {
                        politicCardSelected.add(findPoliticCard(jfxCheckBoxTempTemp.getText()));
                    }
                }
                clientController.mainActionBuyPermitCard(permitCard,politicCardSelected);
            }
        });
        vBox.getChildren().add(jfxButton);
        vBox.setSpacing(10.0);
        vBox.setPadding(new Insets(10.0,10.0,10.0,10.0));
        paneOfPopup.getChildren().add(vBox);

        popOver.setContentNode(paneOfPopup);
        popOver.show(imageView);
    }

    private PoliticCard findPoliticCard(String politicCardType) {
        for (PoliticCard politicCard: politicCards) {
            if(politicCardType.equals("MULTICOLOR")){
                if(politicCard.isMultiColor()){
                    politicCards.remove(politicCard);
                    return politicCard;
                }
            }
            else{
                if (politicCard.getPoliticColor().getColor().equals(politicCardType)){
                    politicCards.remove(politicCard);
                    return politicCard;
                }
            }

        }
        return null;
    }
}
