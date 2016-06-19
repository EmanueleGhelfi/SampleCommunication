package ClientPackage.View.GUIResources.Class;

import ClientPackage.Controller.ClientController;
import Server.Model.Map;
import Utilities.Class.CircularArrayList;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private int mapCounter;

    @FXML private Text jsonTest;
    @FXML private ImageView coastImage;
    @FXML private GridPane gridPane;
    @FXML private ImageView prevImageView;
    @FXML private ImageView thisImageView;
    @FXML private ImageView nextImageView;
    @FXML private BorderPane borderPaneBackground;
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
        ImageView leftImage = new ImageView(new Image("/ClientPackage/View/GUIResources/Image/Left.png"));
        leftButton.setGraphic(leftImage);

        JFXButton rightButton = new JFXButton();
        ImageView rightImage = new ImageView(new Image("/ClientPackage/View/GUIResources/Image/Right.png"));
        rightButton.setGraphic(rightImage);

        borderPaneBackground.setLeft(leftButton);
        borderPaneBackground.setRight(rightButton);

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


    }

    public void showMap(ArrayList<Map> mapArrayList) {
        for (Map map : mapArrayList) {
            this.mapArrayList.add(map);
        }
        thisImage = new Image(mapArrayList.get(0).getMapPreview());
        thisImageView.setImage(thisImage);
    }

    public void nextVisibleMap(){
        mapCounter++;
        System.out.println(mapArrayList.get(mapCounter - 1).getMapName());
        System.out.println(mapArrayList.get(mapCounter).getMapName() + " in mezzo");
        System.out.println(mapArrayList.get(mapCounter + 1).getMapName() + " mapcounter -> " + mapCounter);
    }

    public void prevVisibleMap(){
        mapCounter--;
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
