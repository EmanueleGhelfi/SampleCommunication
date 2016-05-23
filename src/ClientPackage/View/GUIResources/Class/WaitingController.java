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
 * Created by Giulio on 16/05/2016.
 */
public class WaitingController implements Initializable {

    private ClientController clientController;
    private CircularArrayList<Map> mapArrayList = new CircularArrayList<>();
    private int mapCounter;

    @FXML private Text jsonTest;
    @FXML private ImageView coastImage;
    @FXML private GridPane gridPane;
    @FXML private ImageView prevImageView;
    @FXML private ImageView thisImageView;
    @FXML private ImageView nextImageView;
    @FXML private Image prevImage;
    @FXML private Image thisImage;
    @FXML private Image nextImage;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        prevImageView.setImage(prevImage);
        thisImageView.setImage(thisImage);
        nextImageView.setImage(nextImage);
    }

    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
    }

    public void firstMainAction(ActionEvent actionEvent) {
        clientController.createAction();
    }

    public void secondMainAction(ActionEvent actionEvent) {
        clientController.createSecondAction();
    }

    public void showMap(ArrayList<Map> mapArrayList) {
        for (Map map : mapArrayList) {
            this.mapArrayList.add(map);
        }
        //prevImage = new Image(mapArrayList.get(mapArrayList.size() - 1).getMapPreview());
        //thisImage = new Image(mapArrayList.get(0).getMapPreview());
        //thisImage = new Image(mapArrayList.get(1).getMapPreview());
    }

    public void nextVisibleMap(){
        mapCounter++;
        //prevImage = new Image(mapArrayList.get(mapCounter - 1).getMapPreview());
        //thisImage = new Image(mapArrayList.get(mapCounter).getMapPreview());
        //thisImage = new Image(mapArrayList.get(mapCounter + 1).getMapPreview());
        System.out.println(mapArrayList.get(mapCounter - 1).getMapName());
        System.out.println(mapArrayList.get(mapCounter).getMapName() + " in mezzo");
        System.out.println(mapArrayList.get(mapCounter + 1).getMapName() + " mapcounter -> " + mapCounter);
    }

    public void prevVisibleMap(){
        mapCounter--;
        //prevImage = new Image(mapArrayList.get(mapCounter - 1).getMapPreview());
        //thisImage = new Image(mapArrayList.get(mapCounter).getMapPreview());
        //thisImage = new Image(mapArrayList.get(mapCounter + 1).getMapPreview());
        System.out.println(mapArrayList.get(mapCounter - 1).getMapName());
        System.out.println(mapArrayList.get(mapCounter).getMapName() + " in mezzo");
        System.out.println(mapArrayList.get(mapCounter + 1).getMapName() + " mapcounter -> " + mapCounter);
    }

    public void takeImage(){
        clientController.sendMap(mapArrayList.get(mapCounter));
        System.out.println("BUTTON PRESSED");
    }

}
