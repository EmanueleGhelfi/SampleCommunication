package ClientPackage.View.GUIResources.Class;

import ClientPackage.Controller.ClientController;
import Server.Model.Map;
import Utilities.Class.CircularArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private Image prevImage;
    private Image thisImage;
    private Image nextImage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        prevImageView.setImage(prevImage);
        thisImageView.setImage(thisImage);
        nextImageView.setImage(nextImage);
    }

    public void showMap(ArrayList<Map> mapArrayList) {
        for (Map map : mapArrayList) {
            this.mapArrayList.add(map);
        }
        prevImage = new Image(mapArrayList.get(mapCounter - 1).getMapPreview());
        thisImage = new Image(mapArrayList.get(mapCounter).getMapPreview());
        nextImage = new Image(mapArrayList.get(mapCounter + 1).getMapPreview());
        prevImageView.setImage(prevImage);
        thisImageView.setImage(thisImage);
        nextImageView.setImage(nextImage);
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
        prevImage = new Image(mapArrayList.get(mapCounter - 1).getMapPreview());
        thisImage = new Image(mapArrayList.get(mapCounter).getMapPreview());
        nextImage = new Image(mapArrayList.get(mapCounter + 1).getMapPreview());
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
