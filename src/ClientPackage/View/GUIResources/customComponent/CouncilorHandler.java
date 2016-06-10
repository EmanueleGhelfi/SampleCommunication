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
        PopOver popOver = new PopOver();
        VBox vBox = new VBox();

        JFXComboBox<Label> jfxComboBox = new JFXComboBox<Label>();
        JFXButton mainActionButton = new JFXButton();
        JFXButton fastActionButton = new JFXButton();
        HBox hBoxSource = (HBox)event.getSource();

        for (PoliticColor politicColor: PoliticColor.values()){
            politicColors.add(politicColor);
            jfxComboBox.getItems().add(new Label(politicColor.getColor()));
        }
        jfxComboBox.setPromptText("Scegli il consigliere che vuoi aggiungere");
        mainActionButton.getStyleClass().add("button-raised");
        mainActionButton.setText("SCALZA GUADAGNANDO SOLDI!!!!");
        mainActionButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int index = jfxComboBox.getSelectionModel().getSelectedIndex();
                System.out.println("selected "+index);
                System.out.println("value in combobox "+jfxComboBox.getSelectionModel().getSelectedItem().getText());
                Councilor councilor = new Councilor(politicColors.get(index));
                clientController.mainActionElectCouncilor(councilor,king,region);
            }
        });


        fastActionButton.getStyleClass().add("button-raised");
        fastActionButton.setText("SCALZA CON AIUTANTI!!!");
        fastActionButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int index = jfxComboBox.getSelectionModel().getSelectedIndex();
                System.out.println("selected "+index);
                System.out.println("value in combobox "+jfxComboBox.getSelectionModel().getSelectedItem().getText());
                Councilor councilor = new Councilor(politicColors.get(index));
                clientController.fastActionElectCouncilorWithHelper(councilor,king,region,councilType);
            }
        });

        HBox hBox = new HBox(mainActionButton,fastActionButton);
        hBox.setSpacing(5);
        vBox.getChildren().add(jfxComboBox);
        vBox.getChildren().add(hBox);
        vBox.setPadding(new Insets(20.0,20.0,20.0,20.0));
        vBox.setSpacing(20);
        double targetX = event.getScreenX();
        double targetY = event.getScreenY();
        popOver.setContentNode(vBox);
        popOver.show(hBoxSource,targetX,targetY);
    }
}
