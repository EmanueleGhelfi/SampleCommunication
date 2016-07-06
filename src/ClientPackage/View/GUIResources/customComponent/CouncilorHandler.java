package ClientPackage.View.GUIResources.CustomComponent;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.GUIResources.Class.MatchController;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticColor;
import CommonModel.GameModel.City.RegionName;
import CommonModel.GameModel.Council.Councilor;
import CommonModel.GameModel.Council.King;
import Utilities.Class.Constants;
import Utilities.Class.Graphics;
import Utilities.Class.Translator;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;

import java.util.ArrayList;

/**
 * Created by Emanuele on 28/05/2016.
 */
public class CouncilorHandler implements EventHandler<MouseEvent> {

    private final RegionName region;
    private final MatchController matchController;
    private final ClientController clientController;
    private final ArrayList<PoliticColor> politicColors = new ArrayList<PoliticColor>();
    private final King king;
    private final Node node;
    private final String councilType;
    private final PopOver popOver = new PopOver();

    public CouncilorHandler(Node node, RegionName region, MatchController matchController, ClientController clientController, King king) {
        this.region = region;
        this.matchController = matchController;
        this.clientController = clientController;
        this.king = king;
        this.node = node;
        if (king == null) {
            this.councilType = Constants.REGION_COUNCIL;
        } else {
            this.councilType = Constants.KING_COUNCIL;
        }
    }

    @Override
    public void handle(MouseEvent event) {
        Graphics.playSomeSound("Button");
        VBox vBox = new VBox();
        JFXComboBox<Label> jfxComboBox = new JFXComboBox<Label>();
        JFXButton mainActionButton = new JFXButton();
        JFXButton fastActionButton = new JFXButton();
        HBox hBoxSource = (HBox) event.getSource();
        this.politicColors.clear();
        for (PoliticColor politicColor : this.clientController.getSnapshot().getBank().showCouncilor()) {
            this.politicColors.add(politicColor);
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
                if (index != -1) {
                    Councilor councilor = new Councilor(CouncilorHandler.this.politicColors.get(index));
                    CouncilorHandler.this.clientController.mainActionElectCouncilor(councilor, CouncilorHandler.this.king, CouncilorHandler.this.region);
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
                if (index != -1) {
                    Councilor councilor = new Councilor(CouncilorHandler.this.politicColors.get(index));
                    CouncilorHandler.this.clientController.fastActionElectCouncilorWithHelper(councilor, CouncilorHandler.this.king, CouncilorHandler.this.region, CouncilorHandler.this.councilType);
                }
            }
        });
        fastActionButton.setPrefHeight(30);
        fastActionButton.setPrefWidth(120);
        mainActionButton.setPrefHeight(30);
        mainActionButton.setPrefWidth(120);
        HBox hBox = new HBox(mainActionButton, fastActionButton);
        hBox.setSpacing(5);
        vBox.getChildren().add(jfxComboBox);
        vBox.getChildren().add(hBox);
        vBox.setPadding(new Insets(20.0, 20.0, 20.0, 20.0));
        vBox.setSpacing(20);
        double targetX = event.getScreenX();
        double targetY = event.getScreenY();
        if (targetX / this.matchController.getBackground().getWidth() > 0.5) {
            this.popOver.setArrowLocation(ArrowLocation.RIGHT_TOP);
        }
        this.popOver.hide();
        this.popOver.setContentNode(vBox);
        this.popOver.show(hBoxSource, targetX, targetY);
    }
}
