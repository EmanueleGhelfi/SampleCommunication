package ClientPackage.View.GUIResources.Class;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.GUIResources.CustomComponent.ImageLoader;
import Server.Model.Map;
import Utilities.Class.CircularArrayList;
import Utilities.Class.Graphics;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by Giulio on 24/05/2016.
 */
public class MapSelectionController implements Initializable {

    private ClientController clientController;
    private final CircularArrayList<Map> mapArrayList = new CircularArrayList<>();
    private final CircularArrayList<Image> mapArrayListImage = new CircularArrayList<>();
    private int mapCounter;

    @FXML
    private ImageView prevImageView;
    @FXML
    private ImageView thisImageView;
    @FXML
    private ImageView nextImageView;
    @FXML
    private GridPane gridPaneBackground;
    @FXML
    private GridPane gridPaneSelection;
    private Image prevImage;
    private Image thisImage;
    private Image nextImage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.prevImageView.setImage(this.prevImage);
        this.thisImageView.setImage(this.thisImage);
        this.nextImageView.setImage(this.nextImage);
        this.initButton();
    }

    private void initButton() {
        JFXButton leftButton = new JFXButton();
        ImageView leftImage = new ImageView(ImageLoader.getInstance().getImage("/ClientPackage/View/GUIResources/Image/Left.png", this.gridPaneBackground.getWidth() / 3, this.gridPaneBackground.getHeight() / 3));
        leftButton.setGraphic(leftImage);
        JFXButton rightButton = new JFXButton();
        ImageView rightImage = new ImageView(ImageLoader.getInstance().getImage("/ClientPackage/View/GUIResources/Image/Right.png", this.gridPaneBackground.getWidth() / 3, this.gridPaneBackground.getHeight() / 3));
        rightButton.setGraphic(rightImage);
        this.gridPaneBackground.add(leftButton, 0, 1);
        this.gridPaneBackground.add(rightButton, 2, 1);
        leftButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MapSelectionController.this.prevVisibleMap();
            }
        });
        rightButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MapSelectionController.this.nextVisibleMap();
            }
        });
        GridPane.setHalignment(leftButton, HPos.CENTER);
        GridPane.setValignment(leftButton, VPos.CENTER);
        GridPane.setHalignment(rightButton, HPos.CENTER);
        GridPane.setValignment(rightButton, VPos.CENTER);

    }

    public void showMap(ArrayList<Map> mapArrayList) {
        for (Map map : mapArrayList) {
            this.mapArrayList.add(map);
            mapArrayListImage.add(ImageLoader.getInstance().getImage(map.getMapPreview(), this.gridPaneBackground.getWidth(), this.gridPaneBackground.getHeight()));
        }

        this.prevImageView.fitHeightProperty().bind(this.gridPaneBackground.heightProperty().divide(5));
        this.prevImageView.setPreserveRatio(true);
        this.thisImageView.fitHeightProperty().bind(this.gridPaneBackground.heightProperty().divide(3.5));
        this.thisImageView.setPreserveRatio(true);
        this.nextImageView.fitHeightProperty().bind(this.gridPaneBackground.heightProperty().divide(5));
        this.nextImageView.setPreserveRatio(true);
        this.prevImage = mapArrayListImage.get(this.mapCounter - 1);
        this.thisImage = mapArrayListImage.get(this.mapCounter);
        this.nextImage = mapArrayListImage.get(this.mapCounter + 1);
        this.prevImageView.toBack();
        this.thisImageView.toFront();
        this.nextImageView.toBack();
        this.prevImageView.setImage(this.prevImage);
        this.thisImageView.setImage(this.thisImage);
        this.nextImageView.setImage(this.nextImage);
    }

    public void nextVisibleMap() {
        this.mapCounter++;
        this.showThreeMaps();
    }

    public void prevVisibleMap() {
        this.mapCounter--;
        this.showThreeMaps();
    }

    private void showThreeMaps() {
        this.prevImage = mapArrayListImage.get(this.mapCounter - 1);
        this.thisImage = mapArrayListImage.get(this.mapCounter);
        this.nextImage = mapArrayListImage.get(this.mapCounter + 1);
        this.prevImageView.setImage(this.prevImage);
        this.thisImageView.setImage(this.thisImage);
        this.nextImageView.setImage(this.nextImage);
    }

    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
    }

    public void chooseMap(ActionEvent actionEvent) {
        Graphics.playSomeSound("Button");
        this.clientController.sendMap(this.mapArrayList.get(this.mapCounter));
    }
}
