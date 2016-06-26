package ClientPackage.View.GUIResources.Class;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.GeneralView.GUIView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Giulio on 25/06/2016.
 */
public class FinishMatchController implements Initializable {

    private ClientController clientController;
    private GUIView baseView;

    @FXML Label textWin;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setClientController(ClientController clientController, GUIView baseView, boolean win) {
        this.clientController = clientController;
        this.baseView = baseView;
        if (win){
            textWin.setText("VINTO");
        } else {
            textWin.setText("PERSO");
        }
    }


}
