package ClientPackage.View.GUIResources.Class;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.GUIResources.CustomComponent.ImageLoader;
import ClientPackage.View.GeneralView.GUIView;
import CommonModel.GameModel.City.City;
import CommonModel.GameModel.Path.Position;
import CommonModel.Snapshot.SnapshotToSend;
import Utilities.Class.Constants;
import Utilities.Class.Graphics;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;

/**
 * Created by Giulio on 14/06/2016.
 */
public class NobilityPathController implements BaseController {

    private ImageView kingBonus;
    private ClientController clientController;
    private final ImageView backgroundImage = new ImageView();
    private final NobilityPathController nobilityPathController = this;
    private MatchController matchController;

    @FXML
    private Pane nobilityPath;


    private void createGridPane() {
        this.backgroundImage.fitWidthProperty().bind(this.nobilityPath.prefWidthProperty());
        this.backgroundImage.fitHeightProperty().bind(this.nobilityPath.prefHeightProperty());
        this.nobilityPath.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            }
        });
        this.nobilityPath.setOpacity(0.8);
        this.nobilityPath.prefHeightProperty().bind(this.matchController.getBackground().prefHeightProperty().divide(4));
        this.nobilityPath.prefWidthProperty().bind(this.matchController.getBackground().prefWidthProperty());
        this.backgroundImage.setImage(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "/NobilityAndBonusCard.png", this.nobilityPath.getPrefWidth(), this.nobilityPath.getPrefHeight()));
        this.backgroundImage.setCache(true);
        this.nobilityPath.getChildren().add(this.backgroundImage);
        this.createPath();
        this.placeBonusCard();
    }

    private void placeBonusCard() {
        ImageView greyBonus = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "/GreyBonusCard.png"));
        greyBonus.setCache(true);
        ImageView orangeBonus = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "/PinkBonusCard.png"));
        orangeBonus.setCache(true);
        ImageView blueBonus = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "/BlueBonusCard.png"));
        blueBonus.setCache(true);
        ImageView yellowBonus = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "/YellowBonusCard.png"));
        yellowBonus.setCache(true);
        this.kingBonus = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "/KingBonus1.png"));
        this.kingBonus.setCache(true);
        greyBonus.getTransforms().add(new Rotate(35, 0.0, 0.0, 0.0, Rotate.Z_AXIS));
        orangeBonus.getTransforms().add(new Rotate(35, 0.0, 0.0, 0.0, Rotate.Z_AXIS));
        blueBonus.getTransforms().add(new Rotate(35, 0.0, 0.0, 0.0, Rotate.Z_AXIS));
        yellowBonus.getTransforms().add(new Rotate(35, 0.0, 0.0, 0.0, Rotate.Z_AXIS));
        this.kingBonus.getTransforms().add(new Rotate(35, 0.0, 0.0, 0.0, Rotate.Z_AXIS));
        blueBonus.layoutXProperty().bind(this.nobilityPath.prefWidthProperty().multiply(0.7908));
        orangeBonus.layoutXProperty().bind(this.nobilityPath.prefWidthProperty().multiply(0.8458));
        greyBonus.layoutXProperty().bind(this.nobilityPath.prefWidthProperty().multiply(0.8983));
        yellowBonus.layoutXProperty().bind(this.nobilityPath.prefWidthProperty().multiply(0.9491));
        this.kingBonus.layoutXProperty().bind(this.nobilityPath.prefWidthProperty().multiply(0.9391));
        blueBonus.layoutYProperty().bind(this.nobilityPath.prefHeightProperty().multiply(0.552));
        orangeBonus.layoutYProperty().bind(this.nobilityPath.prefHeightProperty().multiply(0.5167));
        greyBonus.layoutYProperty().bind(this.nobilityPath.prefHeightProperty().multiply(0.4769));
        yellowBonus.layoutYProperty().bind(this.nobilityPath.prefHeightProperty().multiply(0.4284));
        this.kingBonus.layoutYProperty().bind(this.nobilityPath.prefHeightProperty().multiply(0.0));
        greyBonus.fitHeightProperty().bind(this.nobilityPath.prefHeightProperty().multiply(0.25));
        orangeBonus.fitHeightProperty().bind(this.nobilityPath.prefHeightProperty().multiply(0.25));
        blueBonus.fitHeightProperty().bind(this.nobilityPath.prefHeightProperty().multiply(0.25));
        yellowBonus.fitHeightProperty().bind(this.nobilityPath.prefHeightProperty().multiply(0.25));
        this.kingBonus.fitHeightProperty().bind(this.nobilityPath.prefHeightProperty().multiply(0.25));
        greyBonus.setPreserveRatio(true);
        orangeBonus.setPreserveRatio(true);
        blueBonus.setPreserveRatio(true);
        yellowBonus.setPreserveRatio(true);
        this.kingBonus.setPreserveRatio(true);
        Tooltip.install(greyBonus, new Tooltip("Bonus colore grigio"));
        Tooltip.install(orangeBonus, new Tooltip("Bonus colore arancione"));
        Tooltip.install(blueBonus, new Tooltip("Bonus colore blu"));
        Tooltip.install(yellowBonus, new Tooltip("Bonus colore giallo"));
        Tooltip.install(this.kingBonus, new Tooltip("Bonus del Re"));
        Graphics.bringUpImages(greyBonus, orangeBonus, yellowBonus, blueBonus, this.kingBonus);
        this.nobilityPath.getChildren().addAll(greyBonus, orangeBonus, blueBonus, yellowBonus, this.kingBonus);
    }

    private void createPath() {
        double relativeHeightPosition = 0.3656;
        double relativeWidthPosition = 0.0125;
        for (Position position : this.clientController.getSnapshot().getNobilityPathPosition()) {
            VBox positionHBox = new VBox();
            positionHBox.layoutXProperty().bind(this.nobilityPath.prefWidthProperty().multiply(relativeWidthPosition));
            positionHBox.layoutYProperty().bind(this.nobilityPath.prefHeightProperty().multiply(relativeHeightPosition));
            positionHBox.prefWidthProperty().bind(this.nobilityPath.prefWidthProperty().multiply(0.036));
            positionHBox.prefHeightProperty().bind(this.nobilityPath.prefHeightProperty().multiply(0.2694));
            positionHBox.setAlignment(Pos.CENTER);
            if (position != null) {
                for (int i = 0; i < position.getBonus().getBonusURL().size(); i++) {
                    StackPane internalStackPane = new StackPane();
                    ImageView bonusImage = new ImageView(ImageLoader.getInstance().getImage(position.getBonus().getBonusURL().get(i)));
                    bonusImage.setCache(true);
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
            this.nobilityPath.getChildren().add(positionHBox);
            relativeWidthPosition += 0.0352;
        }
    }


    public void setMatchController(MatchController matchController) {
        this.matchController = matchController;
        this.createGridPane();
    }

    @Override
    public void updateView() {
        this.kingBonus.setImage(new Image(Constants.IMAGE_PATH + "KingBonus" + this.clientController.getSnapshot().getKingBonusCards().peek().getOrder() + ".png"));
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

    @Override
    public void selectOldPermitCardBonus() {
    }
}
