package ClientPackage.View.GUIResources.Class;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.GUIResources.customComponent.ImageLoader;
import Server.Model.Map;
import Utilities.Class.CircularArrayList;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by Giulio on 24/05/2016.
 */
public class MapSelectionController implements Initializable {

    private ClientController clientController;
    private CircularArrayList<Map> mapArrayList = new CircularArrayList<>();
    private CircularArrayList<Image> mapArrayListImage = new CircularArrayList<>();
    private int mapCounter;

    @FXML private ImageView prevImageView;
    @FXML private ImageView thisImageView;
    @FXML private ImageView nextImageView;
    @FXML private GridPane gridPaneBackground;
    @FXML private GridPane gridPaneSelection;
    private Image prevImage;
    private Image thisImage;
    private Image nextImage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        prevImageView.setImage(prevImage);
        thisImageView.setImage(thisImage);
        nextImageView.setImage(nextImage);
        initButton();
    }

    private void initButton() {
        JFXButton leftButton = new JFXButton();
        ImageView leftImage = new ImageView(ImageLoader.getInstance().getImage("/ClientPackage/View/GUIResources/Image/Left.png",gridPaneBackground.getWidth()/3,gridPaneBackground.getHeight()/3));
        leftButton.setGraphic(leftImage);
        JFXButton rightButton = new JFXButton();
        ImageView rightImage = new ImageView(ImageLoader.getInstance().getImage("/ClientPackage/View/GUIResources/Image/Right.png",gridPaneBackground.getWidth()/3,gridPaneBackground.getHeight()/3));
        rightButton.setGraphic(rightImage);
        gridPaneBackground.add(leftButton, 0, 1);
        gridPaneBackground.add(rightButton, 2, 1);
        leftButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                prevVisibleMap();
            }
        });
        rightButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                nextVisibleMap();
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
            this.mapArrayListImage.add(ImageLoader.getInstance().getImage(map.getMapPreview(),gridPaneBackground.getWidth(),gridPaneBackground.getHeight()));
        }

        prevImageView.fitHeightProperty().bind(gridPaneBackground.heightProperty().divide(5));
        prevImageView.setPreserveRatio(true);
        thisImageView.fitHeightProperty().bind(gridPaneBackground.heightProperty().divide(3.5));
        thisImageView.setPreserveRatio(true);
        nextImageView.fitHeightProperty().bind(gridPaneBackground.heightProperty().divide(5));
        nextImageView.setPreserveRatio(true);
        prevImage = this.mapArrayListImage.get(mapCounter - 1);
        thisImage = this.mapArrayListImage.get(mapCounter);
        nextImage = this.mapArrayListImage.get(mapCounter + 1);
        prevImageView.toBack();
        thisImageView.toFront();
        nextImageView.toBack();
        prevImageView.setImage(prevImage);
        thisImageView.setImage(thisImage);
        nextImageView.setImage(nextImage);

        /*
        prevImage = new Image(this.mapArrayList.get(mapCounter - 1).getMapPreview());
        thisImage = new Image(this.mapArrayList.get(mapCounter).getMapPreview());
        nextImage = new Image(this.mapArrayList.get(mapCounter + 1).getMapPreview());
        */
    }

    public void nextVisibleMap(){
        mapCounter++;
        showThreeMaps();
    }

    public void prevVisibleMap(){
        mapCounter--;
        showThreeMaps();
    }

    private void showThreeMaps() {
        prevImage = this.mapArrayListImage.get(mapCounter - 1);
        thisImage = this.mapArrayListImage.get(mapCounter);
        nextImage = this.mapArrayListImage.get(mapCounter + 1);
        prevImageView.setImage(prevImage);
        thisImageView.setImage(thisImage);
        nextImageView.setImage(nextImage);
        /*
        prevImage = new Image(mapArrayList.get(mapCounter - 1).getMapPreview());
        thisImage = new Image(mapArrayList.get(mapCounter).getMapPreview());
        nextImage = new Image(mapArrayList.get(mapCounter + 1).getMapPreview());
        prevImageView.setImage(prevImage);
        thisImageView.setImage(thisImage);
        nextImageView.setImage(nextImage);
        */
        System.out.println(mapArrayList.get(mapCounter - 1).getMapName());
        System.out.println(mapArrayList.get(mapCounter).getMapName() + " in mezzo");
        System.out.println(mapArrayList.get(mapCounter + 1).getMapName() + " mapcounter -> " + mapCounter);
    }

    public void takeImage(){
        clientController.sendMap(mapArrayList.get(mapCounter));
        System.out.println("BUTTON PRESSED");
    }

    public void setClientController(ClientController clientController){
        this.clientController = clientController;
    }

    public void chooseMap(ActionEvent actionEvent) {
        clientController.sendMap(mapArrayList.get(mapCounter));
    }
}
