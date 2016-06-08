package ClientPackage.View.GUIResources.Class;

import ClientPackage.Controller.ClientController;
import Server.Model.Map;
import Utilities.Class.CircularArrayList;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXSpinner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by Giulio on 16/05/2016.
 */
public class WaitingController {

    private ClientController clientController;
    private JFXSpinner jfxSpinner = new JFXSpinner();
    @FXML private StackPane background;

    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
        background.getChildren().add(jfxSpinner);
        StackPane.setAlignment(jfxSpinner, Pos.CENTER);
        jfxSpinner.radiusProperty().bind(background.widthProperty().divide(7));
    }

}
