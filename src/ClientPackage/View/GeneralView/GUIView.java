package ClientPackage.View.GeneralView;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.GUIResources.Class.MatchController;
import ClientPackage.View.GUIResources.Class.LoginController;
import ClientPackage.View.GUIResources.Class.WaitingController;
import CommonModel.Snapshot.SnapshotToSend;
import Server.Model.Map;
import Utilities.Class.Constants;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Emanuele on 13/05/2016.
 */
public class GUIView extends Application implements BaseView {

    private LoginController loginController;
    private WaitingController waitingController;
    private MatchController matchController;
    private ClientController clientController;
    private ArrayList<Map> maps;

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
        // This initializes JavaFx application
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
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                System.out.println("show waiting for start");
                FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.WAITING_FXML));
                Parent screen = null;
                try {
                    screen = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                waitingController = loader.getController();
                waitingController.setClientController(clientController);
                if(maps!=null)
                    waitingController.showMap(maps);
                Scene scene = new Scene(screen);
                Stage testStage = new Stage();
                testStage.setScene(scene);
                testStage.show();
            }
        });
    }

    @Override
    public void showMap(ArrayList<Map> mapArrayList) {
        maps = mapArrayList;
        if(waitingController!=null){
            waitingController.showMap(maps);
        }
    }

    @Override
    public void gameInitialization(SnapshotToSend snapshotToSend) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.MATCH_FXML));
                Parent screen = null;
                try {
                    screen = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                matchController = loader.getController();
                matchController.setClientController(clientController);
                Scene scene = new Scene(screen);
                Stage testStage = new Stage();
                testStage.setScene(scene);
                testStage.show();
            }
        });
    }

    @Override
    public void turnFinished() {
        matchController.setMyTurn(false);
    }

    @Override
    public void isMyTurn() {
        matchController.setMyTurn(true);
    }
}
