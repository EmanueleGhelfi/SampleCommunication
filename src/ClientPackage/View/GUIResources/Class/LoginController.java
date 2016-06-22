package ClientPackage.View.GUIResources.Class;

/**
 * Created by Emanuele on 13/05/2016.
 */

import ClientPackage.Controller.ClientController;
import Server.Model.Map;
import Utilities.Class.Graphics;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Paint;

import java.awt.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Manage the JavaFX View and user input
 */
public class LoginController {

    private ClientController clientController;
    @FXML private JFXTextField usernameText;
    @FXML private Label errorText;
    @FXML private JFXButton button;

    public void onButtonLoginPressed(ActionEvent actionEvent) {
        System.out.println("On button login pressed");
        if (!usernameText.getText().isEmpty()) {
            clientController.onSendLogin(usernameText.getText());
        } else {
            showLoginError("Username non valido");
        }
    }

    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
        errorText.setVisible(false);
        usernameText.setVisible(false);
        button.setVisible(false);
        startTimeout();
    }

    private void startTimeout() {
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        Graphics.fadeTransitionEffect(errorText, 0, 1, 3000);
                        Graphics.fadeTransitionEffect(usernameText, 0, 1, 1000);
                        Graphics.fadeTransitionEffect(button, 0, 1, 1000);
                        usernameText.setVisible(true);
                        button.setVisible(true);
                    }
                },
                1000
        );
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        Graphics.fadeTransitionEffect(errorText, 0, 1, 3000);
                        errorText.setVisible(true);
                    }
                },
                3000
        );
    }

    public void showLoginError(String error) {
        errorText.setText(error);
        errorText.setTextFill(Paint.valueOf("red"));
    }

}
