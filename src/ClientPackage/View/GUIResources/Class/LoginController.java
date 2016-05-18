package ClientPackage.View.GUIResources.Class;

/**
 * Created by Emanuele on 13/05/2016.
 */

import ClientPackage.Controller.ClientController;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Manage the JavaFX View and user input
 */
public class LoginController {

    private ClientController clientController;
    @FXML private JFXTextField usernameText;
    @FXML private Label errorText;

    public void onButtonLoginPressed(ActionEvent actionEvent) {
        System.out.println("On button login pressed");
        clientController.onSendLogin(usernameText.getText());
    }

    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
    }
    public void showLoginError() {
        errorText.setText("ERRORE LOGIN");
    }
}
