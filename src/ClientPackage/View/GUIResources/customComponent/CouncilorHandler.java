package ClientPackage.View.GUIResources.customComponent;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.GUIResources.Class.MatchController;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticColor;
import CommonModel.GameModel.City.Region;
import CommonModel.GameModel.City.RegionName;
import CommonModel.GameModel.Council.Council;
import CommonModel.GameModel.Council.Councilor;
import CommonModel.GameModel.Council.King;
import Utilities.Class.Constants;
import Utilities.Class.Graphics;
import Utilities.Class.Translator;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.PopOver;

import java.util.ArrayList;

/**
 * Created by Emanuele on 28/05/2016.
 */
public class CouncilorHandler implements EventHandler<MouseEvent> {
    RegionName region;

    MatchController matchController;
    ClientController clientController;
    ArrayList<PoliticColor> politicColors = new ArrayList<PoliticColor>();
    King king;
    Node node;
    String councilType;
    PopOver popOver = new PopOver();


    public CouncilorHandler(Node node, RegionName region, MatchController matchController, ClientController clientController, King king) {
        this.region=region;
        this.matchController = matchController;
        this.clientController = clientController;
        this.king = king;
        this.node = node;
        if(king==null){
            councilType = Constants.REGION_COUNCIL;
        }
        else{
            councilType = Constants.KING_COUNCIL;
        }
    }

    @Override
    public void handle(MouseEvent event) {
        Graphics.playSomeSound("Button");

        VBox vBox = new VBox();

        JFXComboBox<Label> jfxComboBox = new JFXComboBox<Label>();
        JFXButton mainActionButton = new JFXButton();
        JFXButton fastActionButton = new JFXButton();
        HBox hBoxSource = (HBox)event.getSource();

        /*
        for (PoliticColor politicColor: PoliticColor.values()){
            politicColors.add(politicColor);
            jfxComboBox.getItems().add(new Label(politicColor.getColor()));
        }
        */

        politicColors.clear();
        for(PoliticColor politicColor: clientController.getSnapshot().getBank().showCouncilor()){
            politicColors.add(politicColor);
            jfxComboBox.getItems().add(new Label(Translator.translatingToIta(politicColor.getColor())));
        }
        jfxComboBox.setPromptText("Scegli il consigliere che vuoi aggiungere");
        mainActionButton.getStyleClass().add("button-raised");
        mainActionButton.setText("GUADAGNA SOLDI");
        mainActionButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Graphics.playSomeSound("Button");
                int index = jfxComboBox.getSelectionModel().getSelectedIndex();
                if(index!=-1) {
                    System.out.println("value in combobox " + Translator.translatingToEng(jfxComboBox.getSelectionModel().getSelectedItem().getText()));
                    Councilor councilor = new Councilor(politicColors.get(index));
                    clientController.mainActionElectCouncilor(councilor, king, region);
                }
            }
        });


        fastActionButton.getStyleClass().add("button-raised");
        fastActionButton.setText("USA AIUTANTI");
        fastActionButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Graphics.playSomeSound("Button");
                int index = jfxComboBox.getSelectionModel().getSelectedIndex();
                if(index!=-1) {
                    System.out.println("value in combobox " + Translator.translatingToEng(jfxComboBox.getSelectionModel().getSelectedItem().getText()));

                    Councilor councilor = new Councilor(politicColors.get(index));
                    clientController.fastActionElectCouncilorWithHelper(councilor, king, region, councilType);
                }
            }
        });

        fastActionButton.setPrefHeight(30);
        fastActionButton.setPrefWidth(120);
        mainActionButton.setPrefHeight(30);
        mainActionButton.setPrefWidth(120);

        HBox hBox = new HBox(mainActionButton,fastActionButton);
        hBox.setSpacing(5);
        vBox.getChildren().add(jfxComboBox);
        vBox.getChildren().add(hBox);
        vBox.setPadding(new Insets(20.0,20.0,20.0,20.0));
        vBox.setSpacing(20);
        double targetX = event.getScreenX();
        double targetY = event.getScreenY();
        if(targetX/matchController.getBackground().getWidth()>0.5){
            popOver.setArrowLocation(PopOver.ArrowLocation.RIGHT_TOP);
        }
        popOver.hide();
        popOver.setContentNode(vBox);
        popOver.show(hBoxSource,targetX,targetY);
    }
}
