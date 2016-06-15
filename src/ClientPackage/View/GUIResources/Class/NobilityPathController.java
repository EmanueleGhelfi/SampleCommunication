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
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;

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
    private MatchController matchController;
    private GUIView guiView = new GUIView();


    private void createGridPane() {

        backgroundImage.setImage(new Image(Constants.IMAGE_PATH + "/NobilityAndBonusCard.png"));
        backgroundImage.fitWidthProperty().bind(nobilityPath.prefWidthProperty());
        backgroundImage.fitHeightProperty().bind(nobilityPath.prefHeightProperty());
        nobilityPath.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("event x and y "+event.getX()/ backgroundImage.getFitWidth()+" "+event.getY()/ backgroundImage.getFitHeight());
            }
        });
        nobilityPath.setOpacity(0.8);
        nobilityPath.prefHeightProperty().bind(matchController.getBackground().prefHeightProperty().divide(4));
        nobilityPath.prefWidthProperty().bind(matchController.getBackground().prefWidthProperty());
        nobilityPath.getChildren().add(backgroundImage);
        createPath();
    }

    private void createPath() {
        double relativeHeightPosition = 0.3656;
        double relativeWidthPosition = 0.0125;
        for (Position position : clientController.getSnapshot().getNobilityPathPosition()) {
            VBox positionHBox = new VBox();
            positionHBox.layoutXProperty().bind(nobilityPath.prefWidthProperty().multiply(relativeWidthPosition));
            positionHBox.layoutYProperty().bind(nobilityPath.prefHeightProperty().multiply(relativeHeightPosition));
            positionHBox.prefWidthProperty().bind(nobilityPath.prefWidthProperty().multiply(0.036));
            positionHBox.prefHeightProperty().bind(nobilityPath.prefHeightProperty().multiply(0.2694));
            positionHBox.setAlignment(Pos.CENTER);
            if (position != null) {
                for (int i = 0; i < position.getBonus().getBonusURL().size(); i++) {
                    StackPane internalStackPane = new StackPane();
                    System.out.println(position.getPosition() + " E' LA POSITION CON " + position.getBonus().getBonusURL().size() + " BONUS");
                    ImageView bonusImage = new ImageView(new Image(position.getBonus().getBonusURL().get(i)));
                    internalStackPane.getChildren().add(bonusImage);
                    bonusImage.setPreserveRatio(true);
                    bonusImage.fitWidthProperty().bind(positionHBox.prefWidthProperty().divide(position.getBonus().getBonusURL().size()));
                    bonusImage.fitHeightProperty().bind(positionHBox.prefHeightProperty());
                    internalStackPane.prefWidthProperty().bind(bonusImage.fitWidthProperty());
                    internalStackPane.prefHeightProperty().bind(bonusImage.fitHeightProperty());
                    Label price = new Label(position.getBonus().getBonusInfo().get(i));
                    price.setTextFill(Paint.valueOf("WHITE"));
                    internalStackPane.getChildren().add(price);
                    StackPane.setAlignment(internalStackPane, Pos.CENTER);
                    StackPane.setAlignment(price, Pos.CENTER);
                    positionHBox.getChildren().add(internalStackPane);
                }
            }
            nobilityPath.getChildren().add(positionHBox);
            relativeWidthPosition += 0.0352;
        }
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
