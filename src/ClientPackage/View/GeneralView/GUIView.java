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
    private boolean myTurn = false;
    private Stage stage;
    private Scene scene;

    public GUIView(ClientController clientController) {
        this.clientController = clientController;
    }

    public GUIView(){
        clientController = ClientController.getInstance();
    }

    @Override
    public void init() throws Exception {
        super.init();
        System.out.println("EHIIIIII");
        this.clientController.setBaseView(this);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.LOGIN_FXML));
        Parent screen = loader.load();
        loginController = loader.getController();
        loginController.setClientController(clientController);
        scene = new Scene(screen);
        this.stage.setScene(scene);
        this.stage.show();
    }

    @Override
    public void initView() {
        // This initializes JavaFx application
        Application.launch();
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
                changeStage();
            }
        });
    }

    private void changeStage() {
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
        scene = new Scene(screen);
        this.stage.setScene(scene);
        this.stage.show();
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
                System.out.println("called my turn "+myTurn);
                matchController.setMyTurn(myTurn);
                Scene scene = new Scene(screen);
                //stage= new Stage();
                stage.setScene(scene);
                stage.show();
            }
        });
    }

    @Override
    public void turnFinished() {
        if(matchController!=null) {
            matchController.setMyTurn(false);
        }
        myTurn=false;
        System.out.println("turn finished");
    }

    @Override
    public void isMyTurn() {
        if(matchController!=null) {
            matchController.setMyTurn(true);
        }
        myTurn=true;
        System.out.println("turn initialized");
    }
}
