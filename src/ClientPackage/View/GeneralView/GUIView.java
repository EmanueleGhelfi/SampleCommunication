package ClientPackage.View.GeneralView;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.GUIResources.Class.*;
import CommonModel.GameModel.Action.Action;
import CommonModel.GameModel.City.City;
import CommonModel.Snapshot.SnapshotToSend;
import Server.Model.Map;
import Utilities.Class.Constants;
import Utilities.Class.Graphics;
import Utilities.Exception.ActionNotPossibleException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Emanuele on 13/05/2016.
 */
public class GUIView extends Application implements BaseView {

    private LoginController loginController;
    private WaitingController waitingController;
    private FinishMatchController finishMatchController;
    private MapSelectionController mapSelectionController;
    private MatchController matchController;
    private ArrayList<BaseController> baseControllerList = new ArrayList<>();
    private ClientController clientController;
    private ArrayList<Map> maps;
    private boolean myTurn = false;
    private Stage stage;
    private Scene scene;

    public GUIView(ClientController clientController) {
        this.clientController = clientController;
    }

    public GUIView() {
        clientController = ClientController.getInstance();
    }

    @Override
    public void init() throws Exception {
        super.init();
        this.clientController.setBaseView(this);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.LOGIN_FXML));
        Parent screen = loader.load();
        loginController = loader.getController();
        loginController.setClientController(clientController);
        primaryStage.getIcons().add(new Image("/ClientPackage/View/GUIResources/Image/Icon.png"));
        primaryStage.setTitle("COFfee");
        scene = new Scene(screen);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
        this.stage.setScene(scene);
        this.stage.show();
        stage.setMinHeight(550);
        stage.setMinWidth(600);
    }

    @Override
    public void initView() {
        Application.launch();
    }

    @Override
    public void showLoginError() {
        Platform.runLater(() -> {
            loginController.showLoginError("Username già scelto");
        });

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.WAITING_FXML));
        Parent screen = null;
        try {
            screen = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        waitingController = loader.getController();
        waitingController.setClientController(clientController);
        scene = new Scene(screen);
        this.stage.setScene(scene);
        this.stage.show();
        stage.setMinHeight(600);
        stage.setMinWidth(550);
    }

    @Override
    public void showMap(ArrayList<Map> mapArrayList) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                stage.setResizable(true);

                FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.MAP_SELECTION_FXML));
                Parent screen = null;
                try {
                    screen = loader.load();
                    mapSelectionController = loader.getController();
                    mapSelectionController.setClientController(clientController);
                    if (maps != null)
                        mapSelectionController.showMap(maps);
                    scene = new Scene(screen);
                    stage.setScene(scene);
                    stage.show();
                    stage.setMinHeight(600);
                    stage.setMinWidth(800);
                    maps = mapArrayList;
                    if (waitingController != null) {
                        mapSelectionController.showMap(maps);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void gameInitialization(SnapshotToSend snapshotToSend) {
        GUIView baseView = this;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                stage.setResizable(true);
                FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.MATCH_FXML));
                Parent screen = null;
                try {
                    screen = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                matchController = loader.getController();
                matchController.setClientController(clientController, baseView);
                matchController.setMyTurn(myTurn, snapshotToSend);
                Scene scene = new Scene(screen);
                stage.setScene(scene);
                stage.setMinHeight(600);
                stage.setMinWidth(1200);
                stage.show();
                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        Platform.exit();
                        System.exit(0);
                    }
                });
            }
        });
    }

    @Override
    public void turnFinished() {

        if (matchController != null) {
            Platform.runLater(() -> {
                matchController.setMyTurn(false, clientController.getSnapshot());
            });
        }
        myTurn = false;
    }

    @Override
    public void isMyTurn(SnapshotToSend snapshot) {
        if (matchController != null) {
            Platform.runLater(() -> {
                matchController.setMyTurn(true, clientController.getSnapshot());
            });
        }
        myTurn = true;
    }

    @Override
    public void updateSnapshot() {
        for (BaseController baseController : baseControllerList) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    baseController.updateView();
                }
            });
        }
    }

    @Override
    public void onStartMarket() {
        Platform.runLater(() -> {
            baseControllerList.forEach(baseController -> {
                baseController.onStartMarket();
            });
        });
    }

    @Override
    public void onStartBuyPhase() {
        Platform.runLater(() -> {
            baseControllerList.forEach(baseController -> baseController.onStartBuyPhase());
        });
    }

    @Override
    public void onFinishMarket() {
        baseControllerList.forEach(baseController -> baseController.onFinishMarket());
    }

    @Override
    public void selectPermitCard() {
        Platform.runLater(() -> {
            baseControllerList.forEach(BaseController::selectPermitCard);
        });

    }

    @Override
    public void selectCityRewardBonus() {
        Platform.runLater(() -> {
            baseControllerList.forEach(BaseController::selectCityRewardBonus);
        });

    }

    @Override
    public void onMoveKing(ArrayList<City> kingPath) {
        Platform.runLater(() -> {
            baseControllerList.forEach(baseController -> baseController.moveKing(kingPath));
        });
    }

    @Override
    public void onActionNotPossibleException(ActionNotPossibleException e) {
        Platform.runLater(() -> {
            Graphics.playSomeSound("Error");
            Alert dlg = createAlert(Alert.AlertType.ERROR);
            dlg.setTitle("ERRORE NELLA MOSSA!");
            dlg.getDialogPane().setContentText("Azione non possibile!");
            configureSampleDialog(dlg, e.getMessage());
            showDialog(dlg);
        });
    }

    @Override
    public void sendMatchFinishedWithWin() {
        GUIView baseView = this;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                stage.setResizable(true);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ClientPackage/View/GUIResources/FXML/FinishMatchFXML.fxml"));
                Parent screen = null;
                try {
                    screen = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finishMatchController = loader.getController();
                finishMatchController.setClientController(clientController, baseView);
                Scene scene = new Scene(screen);
                stage.setScene(scene);
                stage.setMinHeight(600);
                stage.setMinWidth(1200);
                stage.show();
                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        Platform.exit();
                        System.exit(0);
                    }
                });
            }
        });
    }

    @Override
    public void selectOldPermitCardBonus() {
        baseControllerList.forEach(baseController -> baseController.selectOldPermitCardBonus());
    }

    @Override
    public void onActionDone(Action action) {
    }

    @Override
    public void onUserDisconnect(String username) {
        Graphics.notification("User: " + username + " is offline");
    }

    public synchronized void registerBaseController(BaseController baseController) {
        if (!baseControllerList.contains(baseController)) {
            baseControllerList.add(baseController);
        }
    }

    public Scene getScene() {
        return scene;
    }

    private Alert createAlert(Alert.AlertType type) {

        return new Alert(type, "");
    }

    private void configureSampleDialog(Dialog<?> dlg, String header) {
        dlg.initStyle(StageStyle.UNDECORATED);
        dlg.setHeaderText(header);
    }

    private void showDialog(Dialog<?> dlg) {
        dlg.show();
    }

}
