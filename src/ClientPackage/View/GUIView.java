package ClientPackage.View;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.GUIResources.GUIController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Emanuele on 13/05/2016.
 */
public class GUIView extends Application implements BaseView {

    private GUIController guiController;
    private ClientController clientController;

    public GUIView(ClientController clientController) {
        this.clientController = clientController;

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ClientPackage/View/GUIResources/sample.fxml"));
        Parent screen = loader.load();
        guiController = loader.getController();
        guiController.setClientController(clientController);
        Scene scene = new Scene(screen);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    @Override
    public void initView() {
        // This initialize JavaFx application
        Application.launch();
        try {
            start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void showLoginError() {
        guiController.showLoginError();
    }

    @Override
    public void showWaitingForStart() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ClientPackage/View/GUIResources/Test.fxml"));
        Parent screen = null;
        try {
            screen = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        guiController = loader.getController();
        guiController.setClientController(clientController);
        Scene scene = new Scene(screen);
        Stage testStage = new Stage();
        testStage.setScene(scene);
        testStage.show();
    }
}
