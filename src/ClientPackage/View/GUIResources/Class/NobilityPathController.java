package ClientPackage.View.GUIResources.Class;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.GeneralView.GUIView;
import CommonModel.GameModel.Bonus.Generic.Bonus;
import CommonModel.GameModel.City.City;
import CommonModel.GameModel.Path.Position;
import CommonModel.Snapshot.SnapshotToSend;
import Utilities.Class.Constants;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.util.ArrayList;

/**
 * Created by Giulio on 14/06/2016.
 */
public class NobilityPathController implements BaseController {

    @FXML private ImageView backgroundNobility;
    @FXML private Pane nobilityPath;
    @FXML private GridPane nobilityGridPane;

    private ClientController clientController;
    private ImageView backgroundImage = new ImageView();
    private NobilityPathController nobilityPathController = this;
    private MatchController matchController = new MatchController();
    private GUIView guiView = new GUIView();


    private void createGridPane() {
        backgroundImage.setImage(new Image(Constants.IMAGE_PATH + "/NobilityAndBonusCard.png"));
        backgroundImage.fitWidthProperty().bind(nobilityPath.widthProperty());
        backgroundImage.fitHeightProperty().bind(nobilityPath.heightProperty());
        nobilityPath.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("event x and y "+event.getX()/ nobilityPath.getWidth()+" "+event.getY()/ nobilityPath.getHeight());
            }
        });
        nobilityPath.setOpacity(0.8);
        nobilityPath.prefHeightProperty().bind(matchController.getBackground().prefHeightProperty().divide(4));
        nobilityPath.prefWidthProperty().bind(matchController.getBackground().prefWidthProperty());

        createPath();

    }

    private void createPath() {
        /*
        for (Position position : clientController.getSnapshot().getNobilityPathPosition()) {
            HBox positionHBox = new HBox();
            positionHBox.setAlignment(Pos.CENTER);
            for (Bonus bonus : ) {

            }
        }
        */

    }


    public void setMatchController(MatchController matchController) {
        this.matchController = matchController;
        createGridPane();
    }

    @Override
    public void updateView() {

    }

    @Override
    public void setClientController(ClientController clientController, GUIView guiView) {
        this.clientController = clientController;
        guiView.registerBaseController(this);
    }

    @Override
    public void setMyTurn(boolean myTurn, SnapshotToSend snapshot) {

    }

    @Override
    public void onStartMarket() {

    }

    @Override
    public void onStartBuyPhase() {

    }

    @Override
    public void onFinishMarket() {

    }

    @Override
    public void onResizeHeight(double height, double width) {

    }

    @Override
    public void onResizeWidth(double width, double height) {

    }

    @Override
    public void selectPermitCard() {

    }

    @Override
    public void selectCityRewardBonus() {

    }

    @Override
    public void moveKing(ArrayList<City> kingPath) {

    }
}
