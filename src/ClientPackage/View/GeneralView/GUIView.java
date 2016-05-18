package ClientPackage.View.GeneralView;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.GUIResources.Class.LoginController;
import ClientPackage.View.GUIResources.Class.WaitingController;
import Utilities.Class.Constants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Created by Emanuele on 13/05/2016.
 */
public class GUIView extends Application implements BaseView {

    private LoginController loginController;
    private ClientController clientController;

    public GUIView(ClientController clientController) {
        this.clientController = clientController;
    }

    public GUIView(){
        clientController = ClientController.getInstance();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.LOGIN_FXML));
        Parent screen = loader.load();
        loginController = loader.getController();
        loginController.setClientController(clientController);
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
        loginController.showLoginError();
    }

    @Override
    public void showWaitingForStart() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.WAITING_FXML));
        Parent screen = null;
        try {
            screen = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        WaitingController waitingController = loader.getController();
        waitingController.setClientController(clientController);
        Scene scene = new Scene(screen);
        Stage testStage = new Stage();
        testStage.setScene(scene);
        testStage.show();
    }
}
