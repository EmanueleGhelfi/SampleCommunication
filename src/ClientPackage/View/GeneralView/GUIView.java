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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Emanuele on 13/05/2016.
 */
public class GUIView extends Application implements BaseView {

    private LoginController loginController;
    private WaitingController waitingController;
    private FinishMatchController finishMatchController;
    private MapSelectionController mapSelectionController;
    private MatchController matchController;
    private final ArrayList<BaseController> baseControllerList = new ArrayList<>();
    private final ClientController clientController;
    private ArrayList<Map> maps;
    private boolean myTurn;
    private Stage stage;
    private Scene scene;

    public GUIView(ClientController clientController) {
        this.clientController = clientController;
    }

    public GUIView() {
        this.clientController = ClientController.getInstance();
    }

    @Override
    public void init() throws Exception {
        super.init();
        clientController.setBaseView(this);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource(Constants.LOGIN_FXML));
        Parent screen = loader.load();
        this.loginController = loader.getController();
        this.loginController.setClientController(this.clientController);
        primaryStage.getIcons().add(new Image("/ClientPackage/View/GUIResources/Image/Icon.png"));
        primaryStage.setTitle("COFfee");
        this.scene = new Scene(screen);
        this.stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
        stage.setScene(this.scene);
        stage.show();
        this.stage.setMinHeight(550);
        this.stage.setMinWidth(600);
    }

    @Override
    public void initView() {
        Application.launch();
    }

    @Override
    public void showLoginError() {
        Platform.runLater(() -> {
            this.loginController.showLoginError("Username già scelto");
        });

    }

    @Override
    public void showWaitingForStart() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                GUIView.this.changeStage();
            }
        });
    }

    private void changeStage() {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource(Constants.WAITING_FXML));
        Parent screen = null;
        try {
            screen = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.waitingController = loader.getController();
        this.waitingController.setClientController(this.clientController);
        this.scene = new Scene(screen);
        stage.setScene(this.scene);
        stage.show();
        this.stage.setMinHeight(600);
        this.stage.setMinWidth(550);
    }

    @Override
    public void showMap(ArrayList<Map> mapArrayList) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                GUIView.this.stage.setResizable(true);

                FXMLLoader loader = new FXMLLoader(this.getClass().getResource(Constants.MAP_SELECTION_FXML));
                Parent screen = null;
                try {
                    screen = loader.load();
                    GUIView.this.mapSelectionController = loader.getController();
                    GUIView.this.mapSelectionController.setClientController(GUIView.this.clientController);
                    if (GUIView.this.maps != null)
                        GUIView.this.mapSelectionController.showMap(GUIView.this.maps);
                    GUIView.this.scene = new Scene(screen);
                    GUIView.this.stage.setScene(GUIView.this.scene);
                    GUIView.this.stage.show();
                    GUIView.this.stage.setMinHeight(600);
                    GUIView.this.stage.setMinWidth(800);
                    GUIView.this.maps = mapArrayList;
                    if (GUIView.this.waitingController != null) {
                        GUIView.this.mapSelectionController.showMap(GUIView.this.maps);
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
                GUIView.this.stage.setResizable(true);
                FXMLLoader loader = new FXMLLoader(this.getClass().getResource(Constants.MATCH_FXML));
                Parent screen = null;
                try {
                    screen = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                GUIView.this.matchController = loader.getController();
                GUIView.this.matchController.setClientController(GUIView.this.clientController, baseView);
                GUIView.this.matchController.setMyTurn(GUIView.this.myTurn, snapshotToSend);
                Scene scene = new Scene(screen);
                GUIView.this.stage.setScene(scene);
                GUIView.this.stage.setMinHeight(600);
                GUIView.this.stage.setMinWidth(1200);
                GUIView.this.stage.show();
                GUIView.this.stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
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

        if (this.matchController != null) {
            Platform.runLater(() -> {
                this.matchController.setMyTurn(false, this.clientController.getSnapshot());
            });
        }
        this.myTurn = false;
    }

    @Override
    public void isMyTurn(SnapshotToSend snapshot) {
        if (this.matchController != null) {
            Platform.runLater(() -> {
                this.matchController.setMyTurn(true, this.clientController.getSnapshot());
            });
        }
        this.myTurn = true;
    }

    @Override
    public void updateSnapshot() {
        for (BaseController baseController : this.baseControllerList) {
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
            this.baseControllerList.forEach(baseController -> {
                baseController.onStartMarket();
            });
        });
    }

    @Override
    public void onStartBuyPhase() {
        Platform.runLater(() -> {
            this.baseControllerList.forEach(baseController -> baseController.onStartBuyPhase());
        });
    }

    @Override
    public void onFinishMarket() {
        this.baseControllerList.forEach(baseController -> baseController.onFinishMarket());
    }

    @Override
    public void selectPermitCard() {
        Platform.runLater(() -> {
            this.baseControllerList.forEach(BaseController::selectPermitCard);
        });

    }

    @Override
    public void selectCityRewardBonus() {
        Platform.runLater(() -> {
            this.baseControllerList.forEach(BaseController::selectCityRewardBonus);
        });

    }

    @Override
    public void onMoveKing(ArrayList<City> kingPath) {
        Platform.runLater(() -> {
            this.baseControllerList.forEach(baseController -> baseController.moveKing(kingPath));
        });
    }

    @Override
    public void onActionNotPossibleException(ActionNotPossibleException e) {
        Platform.runLater(() -> {
            Graphics.playSomeSound("Error");
            Alert dlg = this.createAlert(AlertType.ERROR);
            dlg.setTitle("ERRORE NELLA MOSSA!");
            dlg.getDialogPane().setContentText("Azione non possibile!");
            this.configureSampleDialog(dlg, e.getMessage());
            this.showDialog(dlg);
        });
    }

    @Override
    public void sendMatchFinishedWithWin() {
        GUIView baseView = this;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                GUIView.this.stage.setResizable(true);
                FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/ClientPackage/View/GUIResources/FXML/FinishMatchFXML.fxml"));
                Parent screen = null;
                try {
                    screen = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                GUIView.this.finishMatchController = loader.getController();
                GUIView.this.finishMatchController.setClientController(GUIView.this.clientController, baseView);
                Scene scene = new Scene(screen);
                GUIView.this.stage.setScene(scene);
                GUIView.this.stage.setMinHeight(600);
                GUIView.this.stage.setMinWidth(1200);
                GUIView.this.stage.show();
                GUIView.this.stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
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
        this.baseControllerList.forEach(baseController -> baseController.selectOldPermitCardBonus());
    }

    @Override
    public void onActionDone(Action action) {
    }

    @Override
    public void onUserDisconnect(String username) {
        Graphics.notification("User: " + username + " is offline");
    }

    public synchronized void registerBaseController(BaseController baseController) {
        if (!this.baseControllerList.contains(baseController)) {
            this.baseControllerList.add(baseController);
        }
    }

    public void resizingWindow() {
        ChangeListener<Number> listener = new ChangeListener<Number>() {
            final Timer timer = new Timer();
            final long delayTime = 200;
            TimerTask timerTask;
            double width = GUIView.this.stage.getWidth();
            double height = GUIView.this.stage.getHeight();

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (this.timerTask != null) {
                    this.timerTask.cancel();
                }
                this.timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        if (GUIView.this.stage.getHeight() == newValue.doubleValue()) {
                            if (!(GUIView.this.stage.getWidth() < GUIView.this.stage.getHeight() / 0.6 + 5 && GUIView.this.stage.getWidth() > GUIView.this.stage.getHeight() / 0.6 + 5)) {
                                System.out.println("cambiata width");
                                GUIView.this.stage.setWidth(GUIView.this.stage.getHeight() / 0.6);
                                for (BaseController baseController : GUIView.this.baseControllerList) {
                                    baseController.onResizeHeight(GUIView.this.stage.getHeight(), GUIView.this.scene.getWidth());
                                }
                            }
                        }
                        if (GUIView.this.stage.getWidth() == newValue.doubleValue()) {
                            if (!(GUIView.this.stage.getHeight() < GUIView.this.stage.getWidth() * 0.6 + 5 && GUIView.this.stage.getHeight() > GUIView.this.stage.getWidth() * 0.6 - 5)) {
                                System.out.println("cambiata height");
                                GUIView.this.stage.setHeight(GUIView.this.stage.getWidth() * 0.6);
                                for (BaseController baseController : GUIView.this.baseControllerList) {
                                    baseController.onResizeWidth(GUIView.this.stage.getWidth(), GUIView.this.stage.getHeight());
                                    //baseController.onResizeHeight(newSceneWidth.doubleValue()*0.5623);
                                }
                            }
                        }

                    }
                };
                this.timer.schedule(this.timerTask, this.delayTime);
            }
        };
        this.stage.widthProperty().addListener(listener);
        this.stage.heightProperty().addListener(listener);

    }

    public Scene getScene() {
        return this.scene;
    }

    private Alert createAlert(AlertType type) {

        return new Alert(type, "");
    }

    private void configureSampleDialog(Dialog<?> dlg, String header) {
        dlg.initStyle(StageStyle.UNDECORATED);
        dlg.setHeaderText(header);
    }

    private void showDialog(Dialog<?> dlg) {
        dlg.show();
        dlg.resultProperty().addListener(o -> System.out.println("Result is: " + dlg.getResult()));
    }

}
