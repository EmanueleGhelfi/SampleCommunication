package ClientPackage.View.GUIResources.Class;

import ClientPackage.Controller.ClientController;
import Server.Model.Map;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by Giulio on 16/05/2016.
 */
public class WaitingController implements Initializable {

    private ClientController clientController;
    @FXML
    private TextArea jsonTest;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

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
        int cont = 0;
        for (Map map: mapArrayList) {
            System.out.println("MAP NUMBER " + cont + "\n");
            jsonTest.setText(map.getCity() + " -> CITY\n");
            jsonTest.setText(map.getMapName() + " -> MAP NAME\n");
            jsonTest.setText(map.getMapPreview() + " -> MAP PREVIEW\n");
            jsonTest.setText(map.getLinks() + " -> LINKS\n");
            cont++;
        }
    }

}
