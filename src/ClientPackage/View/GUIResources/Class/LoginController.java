package ClientPackage.View.GUIResources.Class;

/**
 * Created by Emanuele on 13/05/2016.
 */

import ClientPackage.Controller.ClientController;
import Utilities.Class.Graphics;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Manage the JavaFX View and user input
 */
public class LoginController {

    private ClientController clientController;
    @FXML
    private JFXTextField usernameText;
    @FXML
    private Label errorText;
    @FXML
    private JFXButton button;

    public void onButtonLoginPressed(ActionEvent actionEvent) {
        Graphics.playSomeSound("Button");
        if (!this.usernameText.getText().isEmpty()) {
            this.clientController.onSendLogin(this.usernameText.getText());
        } else {
            this.showLoginError("Username non valido");
        }
    }

    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
        this.errorText.setVisible(false);
        this.usernameText.setVisible(false);
        this.button.setVisible(false);
        this.startTimeout();
        this.button.getStyleClass().add("button-raised");
    }

    private void startTimeout() {
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        Graphics.fadeTransitionEffect(LoginController.this.errorText, 0, 1, 3000);
                        Graphics.fadeTransitionEffect(LoginController.this.usernameText, 0, 1, 1000);
                        Graphics.fadeTransitionEffect(LoginController.this.button, 0, 1, 1000);
                        LoginController.this.usernameText.setVisible(true);
                        LoginController.this.button.setVisible(true);
                    }
                },
                1000
        );
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        Graphics.fadeTransitionEffect(LoginController.this.errorText, 0, 1, 3000);
                        LoginController.this.errorText.setVisible(true);
                    }
                },
                3000
        );
    }

    public void showLoginError(String error) {
        this.errorText.setText(error);
        this.errorText.setTextFill(Paint.valueOf("red"));
    }

}
