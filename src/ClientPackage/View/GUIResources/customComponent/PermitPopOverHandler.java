package ClientPackage.View.GUIResources.customComponent;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.GUIResources.Class.MatchController;
import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.City.RegionName;
import Utilities.Class.Constants;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import org.controlsfx.control.PopOver;

/**
 * Created by Emanuele on 16/06/2016.
 */
public class PermitPopOverHandler implements EventHandler<MouseEvent> {

    private ClientController clientController;
    private int order;
    private RegionName regionName;
    private MatchController matchController;
    PopOver popOverZoom = new PopOver();


    public PermitPopOverHandler(ClientController clientController, RegionName regionName, int i, MatchController matchController) {
        this.clientController = clientController;
        this.regionName = regionName;
        this.order = i;
        this.matchController = matchController;
    }

    @Override
    public void handle(MouseEvent event) {
        PermitCard permitCard = clientController.getSnapshot().getVisiblePermitCards().get(regionName).get(order);
        GridPane popOverGridPane = new GridPane();
        Label popOverLabel = new Label(permitCard.getCityString());
        popOverLabel.getStyleClass().add("permitLabelPopover");
        ImageView popOverImageView = new ImageView(new Image(Constants.IMAGE_PATH+"PermitCard.png"));
        popOverLabel.setTextFill(Paint.valueOf("WHITE"));
        popOverImageView.setPreserveRatio(true);
        popOverGridPane.add(popOverImageView, 0, 0);
        popOverGridPane.add(popOverLabel, 0, 0);
        GridPane.setColumnSpan(popOverLabel,3);
        GridPane.setHalignment(popOverLabel,HPos.CENTER);
        GridPane.setValignment(popOverLabel,VPos.CENTER);
        GridPane.setRowSpan(popOverImageView,3);
        GridPane.setColumnSpan(popOverImageView,3);
        popOverImageView.fitWidthProperty().bind(popOverGridPane.prefWidthProperty());
        popOverImageView.fitHeightProperty().bind(popOverGridPane.prefHeightProperty());
        popOverGridPane.prefWidthProperty().bind(matchController.getBackground().widthProperty().divide(5));
        popOverGridPane.prefHeightProperty().bind(matchController.getBackground().heightProperty().divide(5));
        for(int i = 0; i<permitCard.getBonus().getBonusURL().size();i++) {
            ImageView imageViewBonusPopOver = new ImageView(new Image(permitCard.getBonus().getBonusURL().get(i)));
            imageViewBonusPopOver.getStyleClass().add("permitBonusPopover");
            Label bonusInfoPopOver = new Label(permitCard.getBonus().getBonusInfo().get(i));
            bonusInfoPopOver.setTextFill(Paint.valueOf("WHITE"));
            bonusInfoPopOver.setWrapText(true);
            popOverGridPane.add(imageViewBonusPopOver, i, 2);
            popOverGridPane.add(bonusInfoPopOver, i, 2);
            imageViewBonusPopOver.fitWidthProperty().bind(popOverImageView.fitWidthProperty().divide(4));
            imageViewBonusPopOver.fitHeightProperty().bind(popOverImageView.fitHeightProperty().divide(4));
            imageViewBonusPopOver.setPreserveRatio(true);
            GridPane.setHalignment(imageViewBonusPopOver, HPos.CENTER);
            GridPane.setValignment(imageViewBonusPopOver, VPos.CENTER);
            GridPane.setHalignment(bonusInfoPopOver, HPos.CENTER);
            GridPane.setValignment(bonusInfoPopOver, VPos.CENTER);
        }

        HBox hBox = new HBox(popOverGridPane);
        hBox.setPadding(new Insets(20,20,20,20));
        popOverZoom.prefWidthProperty().bind(hBox.widthProperty());
        popOverZoom.setContentNode(hBox);
        popOverZoom.show((Node)event.getSource());
    }

    public void hide() {
        popOverZoom.hide();
    }
}
