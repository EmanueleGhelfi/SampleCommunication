package ClientPackage.View.GUIResources.customComponent;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.GUIResources.Class.MatchController;
import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import Utilities.Class.Graphics;
import Utilities.Class.Translator;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
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
    PopOver popOver = new PopOver();
    ArrayList<PoliticCard> politicCards = new ArrayList<>();
    BooleanProperty needToSelectPermitCard;

    public PermitCardHandler(PermitCard permitCard, MatchController matchController, ClientController clientController, BooleanProperty needToSelectPermitCard) {
        this.permitCard = permitCard;
        this.matchController = matchController;
        this.clientController = clientController;
        this.needToSelectPermitCard= needToSelectPermitCard;
    }

    @Override
    public void handle(MouseEvent event) {
        Graphics.playSomeSound("Button");
        if(!needToSelectPermitCard.get()) {
            politicCards = (ArrayList<PoliticCard>) clientController.getSnapshot().getCurrentUser().getPoliticCards().clone();
            if (popOver.isShowing()) {
                popOver.hide();
            }
            popOver = new PopOver();
            popOver.setArrowLocation(PopOver.ArrowLocation.LEFT_CENTER);
            Pane paneOfPopup = new Pane();
            GridPane imageView = (GridPane) event.getSource();
            double targetX = event.getScreenX();
            double targetY = event.getScreenY();
            JFXCheckBox politicCardsCheckBox;
            VBox vBox = new VBox();
            for (PoliticCard politicCard : politicCards) {
                politicCardsCheckBox = new JFXCheckBox();
                politicCardsCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        Graphics.playSomeSound("Tick");
                    }
                });
                System.out.println(politicCard);
                String stringa;
                if (politicCard.getPoliticColor() == null) {
                    stringa = "Multicolor";
                } else {
                    stringa = Translator.translatingToIta(politicCard.getPoliticColor().getColor());
                }

                politicCardsCheckBox.setText(stringa);
                politicCardsCheckBox.setId("JFXCheckBox");
                vBox.getChildren().add(politicCardsCheckBox);
            }
            JFXButton jfxButton = new JFXButton();
            jfxButton.setButtonType(JFXButton.ButtonType.FLAT);
            jfxButton.setText("OKAY");
            jfxButton.getStyleClass().add("button-raised");
            jfxButton.setAlignment(Pos.CENTER);
            //TODO
            jfxButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Graphics.playSomeSound("Button");
                    ArrayList<PoliticCard> politicCardSelected = new ArrayList<PoliticCard>();
                    Set<Node> jfxCheckBoxArrayList = vBox.lookupAll("#JFXCheckBox");
                    for (Node node : jfxCheckBoxArrayList) {
                        JFXCheckBox jfxCheckBoxTempTemp = (JFXCheckBox) node;
                        if (jfxCheckBoxTempTemp.isSelected()) {
                            politicCardSelected.add(findPoliticCard(Translator.translatingToEng(jfxCheckBoxTempTemp.getText())));
                        }
                    }
                    popOver.hide();
                    clientController.mainActionBuyPermitCard(permitCard, politicCardSelected);
                }
            });
            vBox.getChildren().add(jfxButton);
            VBox.setMargin(jfxButton, new Insets(0, 0, 0, 30));
            vBox.setSpacing(10.0);
            vBox.setPadding(new Insets(10.0, 10.0, 10.0, 10.0));
            paneOfPopup.getChildren().add(vBox);
            if(event.getX()/matchController.getBackground().getWidth()>0.5){
                popOver.setArrowLocation(PopOver.ArrowLocation.RIGHT_TOP);
            }
            popOver.setContentNode(paneOfPopup);
            popOver.show(imageView, targetX, targetY);
        }
        else {
           matchController.onSelectPermitCard(permitCard);
        }
    }

    private PoliticCard findPoliticCard(String politicCardType) {
        for (PoliticCard politicCard: politicCards) {
            if(politicCardType.equals("Multicolor")){
                if(politicCard.isMultiColor()){
                    politicCards.remove(politicCard);
                    return politicCard;
                }
            }
            else{
                if (politicCard.getPoliticColor()!=null && politicCard.getPoliticColor().getColor().equals(politicCardType)){
                    politicCards.remove(politicCard);
                    return politicCard;
                }
            }

        }
        return null;
    }
}
