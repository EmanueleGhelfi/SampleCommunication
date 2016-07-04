package ClientPackage.View.GUIResources.Class;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.GUIResources.customComponent.ImageLoader;
import ClientPackage.View.GeneralView.GUIView;
import CommonModel.GameModel.Bonus.Generic.Bonus;
import CommonModel.GameModel.City.City;
import CommonModel.GameModel.Path.Position;
import CommonModel.Snapshot.SnapshotToSend;
import Utilities.Class.Constants;
import Utilities.Class.Graphics;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;

/**
 * Created by Giulio on 14/06/2016.
 */
public class NobilityPathController implements BaseController {

    @FXML private ImageView backgroundNobility;
    @FXML private Pane nobilityPath;
    @FXML private GridPane nobilityGridPane;

    private ImageView kingBonus;
    private ClientController clientController;
    private ImageView backgroundImage = new ImageView();
    private NobilityPathController nobilityPathController = this;
    private MatchController matchController;
    private GUIView guiView = new GUIView();


    private void createGridPane() {


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
        backgroundImage.setImage(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "/NobilityAndBonusCard.png",nobilityPath.getPrefWidth(),nobilityPath.getPrefHeight()));
        backgroundImage.setCache(true);

        nobilityPath.getChildren().add(backgroundImage);
        createPath();
        placeBonusCard();
    }

    private void placeBonusCard() {
        ImageView greyBonus = new ImageView(new Image(Constants.IMAGE_PATH+"/GreyBonusCard.png",true));
        greyBonus.setCache(true);
        ImageView orangeBonus = new ImageView(new Image(Constants.IMAGE_PATH+"/PinkBonusCard.png",true));
        orangeBonus.setCache(true);
        ImageView blueBonus = new ImageView(new Image(Constants.IMAGE_PATH+"/BlueBonusCard.png",true));
        blueBonus.setCache(true);
        ImageView yellowBonus = new ImageView(new Image(Constants.IMAGE_PATH+"/YellowBonusCard.png",true));
        yellowBonus.setCache(true);
        kingBonus = new ImageView(new Image(Constants.IMAGE_PATH+"/KingBonus1.png",true));
        kingBonus.setCache(true);

        greyBonus.getTransforms().add(new Rotate(35, 0.0, 0.0, 0.0, Rotate.Z_AXIS));
        orangeBonus.getTransforms().add(new Rotate(35, 0.0, 0.0, 0.0, Rotate.Z_AXIS));
        blueBonus.getTransforms().add(new Rotate(35, 0.0, 0.0, 0.0, Rotate.Z_AXIS));
        yellowBonus.getTransforms().add(new Rotate(35, 0.0, 0.0, 0.0, Rotate.Z_AXIS));
        kingBonus.getTransforms().add(new Rotate(35, 0.0, 0.0, 0.0, Rotate.Z_AXIS));

        /*
        greyBonus.setRotate(45);
        orangeBonus.setRotate(45);
        blueBonus.setRotate(45);
        yellowBonus.setRotate(45);
        kingBonus.setRotate(45);
        */


        blueBonus.layoutXProperty().bind(nobilityPath.prefWidthProperty().multiply(0.7908));
        orangeBonus.layoutXProperty().bind(nobilityPath.prefWidthProperty().multiply(0.8458));
        greyBonus.layoutXProperty().bind(nobilityPath.prefWidthProperty().multiply(0.8983));
        yellowBonus.layoutXProperty().bind(nobilityPath.prefWidthProperty().multiply(0.9491));
        kingBonus.layoutXProperty().bind(nobilityPath.prefWidthProperty().multiply(0.9391));

        blueBonus.layoutYProperty().bind(nobilityPath.prefHeightProperty().multiply(0.552));
        orangeBonus.layoutYProperty().bind(nobilityPath.prefHeightProperty().multiply(0.5167));
        greyBonus.layoutYProperty().bind(nobilityPath.prefHeightProperty().multiply(0.4769));
        yellowBonus.layoutYProperty().bind(nobilityPath.prefHeightProperty().multiply(0.4284));
        kingBonus.layoutYProperty().bind(nobilityPath.prefHeightProperty().multiply(0.0));

        greyBonus.fitHeightProperty().bind(nobilityPath.prefHeightProperty().multiply(0.25));
        orangeBonus.fitHeightProperty().bind(nobilityPath.prefHeightProperty().multiply(0.25));
        blueBonus.fitHeightProperty().bind(nobilityPath.prefHeightProperty().multiply(0.25));
        yellowBonus.fitHeightProperty().bind(nobilityPath.prefHeightProperty().multiply(0.25));
        kingBonus.fitHeightProperty().bind(nobilityPath.prefHeightProperty().multiply(0.25));

        greyBonus.setPreserveRatio(true);
        orangeBonus.setPreserveRatio(true);
        blueBonus.setPreserveRatio(true);
        yellowBonus.setPreserveRatio(true);
        kingBonus.setPreserveRatio(true);

        Tooltip.install(greyBonus, new Tooltip("Bonus colore grigio"));
        Tooltip.install(orangeBonus, new Tooltip("Bonus colore arancione"));
        Tooltip.install(blueBonus, new Tooltip("Bonus colore blu"));
        Tooltip.install(yellowBonus, new Tooltip("Bonus colore giallo"));
        Tooltip.install(kingBonus, new Tooltip("Bonus del Re"));


        Graphics.bringUpImages(greyBonus, orangeBonus, yellowBonus, blueBonus, kingBonus);


        nobilityPath.getChildren().addAll(greyBonus, orangeBonus, blueBonus, yellowBonus, kingBonus);
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
        kingBonus.setImage(new Image(Constants.IMAGE_PATH + "KingBonus" + clientController.getSnapshot().getKingBonusCards().firstElement().getOrder() + ".png"));
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
