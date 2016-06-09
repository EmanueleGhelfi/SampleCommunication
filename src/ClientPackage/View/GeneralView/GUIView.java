package ClientPackage.View.GeneralView;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.GUIResources.Class.*;
import CommonModel.Snapshot.SnapshotToSend;
import Server.Model.Map;
import Utilities.Class.Constants;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
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
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
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
        scene = new Scene(screen);
        this.stage.setScene(scene);
        this.stage.show();
    }

    @Override
    public void showMap(ArrayList<Map> mapArrayList) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                System.out.println("show map page");
                FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.MAP_SELECTION_FXML));
                Parent screen = null;
                try {
                    screen = loader.load();
                    mapSelectionController = loader.getController();
                    mapSelectionController.setClientController(clientController);
                    if(maps!=null)
                        mapSelectionController.showMap(maps);
                    scene = new Scene(screen);
                    stage.setScene(scene);
                    stage.show();
                    maps = mapArrayList;
                    if(waitingController!=null){
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.MATCH_FXML));
                Parent screen = null;
                try {
                    screen = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                matchController = loader.getController();
                matchController.setClientController(clientController, baseView);
                System.out.println("called my turn "+myTurn);
                matchController.setMyTurn(myTurn, snapshotToSend);
                Scene scene = new Scene(screen);
                //stage= new Stage();
                stage.setScene(scene);
                stage.setMinHeight(600);
                stage.setMinWidth(1200);
                stage.show();
                //resizingWindow();
            }
        });
    }

    @Override
    public void turnFinished() {
        if(matchController!=null) {
            matchController.setMyTurn(false, clientController.getSnapshot());
        }
        myTurn=false;
        System.out.println("turn finished");
    }

    @Override
    public void isMyTurn(SnapshotToSend snapshot) {
        if(matchController!=null) {
            matchController.setMyTurn(true,clientController.getSnapshot());
        }
        myTurn=true;
        System.out.println("turn initialized");
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
        Platform.runLater(()->{
            baseControllerList.forEach(baseController -> {
                baseController.onStartMarket();
            });
        });
    }

    @Override
    public void onStartBuyPhase() {
        Platform.runLater(()->{
            baseControllerList.forEach(baseController -> baseController.onStartBuyPhase());
        });
    }

    @Override
    public void onFinishMarket() {
        baseControllerList.forEach(baseController -> baseController.onFinishMarket());
    }

    @Override
    public void selectPermitCard() {
        Platform.runLater(()->{
            baseControllerList.forEach(BaseController::selectPermitCard);
        });

    }

    public void registerBaseController(BaseController baseController){
        if (!baseControllerList.contains(baseController)) {
            baseControllerList.add(baseController);
        }
    }

    public void resizingWindow(){

        final ChangeListener<Number> listener = new ChangeListener<Number>() {
            final Timer timer = new Timer();
            TimerTask timerTask = null;
            final long delayTime = 200;

            double width= stage.getWidth();
            double height = stage.getHeight();

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(timerTask!=null){
                    timerTask.cancel();
                }

                timerTask=new TimerTask() {
                    @Override
                    public void run() {
                        System.out.println("Finished resize ehehehe");


                        if(stage.getHeight()==newValue.doubleValue()) {
                            if (!(stage.getWidth() < stage.getHeight() / 0.6 + 5 && stage.getWidth() > stage.getHeight() / 0.6 + 5)) {
                                System.out.println("cambiata width");
                                stage.setWidth(stage.getHeight() / 0.6);
                                for (BaseController baseController : baseControllerList) {
                                    baseController.onResizeHeight(stage.getHeight(),scene.getWidth());
                                    //baseController.onResizeWidth(newSceneHeight.doubleValue()*1.7784);
                                }
                            }
                        }

                        if(stage.getWidth()==newValue.doubleValue()) {
                            if (!(stage.getHeight() < stage.getWidth() * 0.6 + 5 && stage.getHeight() > stage.getWidth() * 0.6 - 5)) {
                                System.out.println("cambiata height");
                                stage.setHeight(stage.getWidth() * 0.6);
                                for (BaseController baseController : baseControllerList) {
                                    baseController.onResizeWidth(stage.getWidth(),stage.getHeight());
                                    //baseController.onResizeHeight(newSceneWidth.doubleValue()*0.5623);
                                }
                            }
                        }

                    }
                };
                timer.schedule(timerTask,delayTime);
            }
        };

        /*
        stage.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                System.out.println("Prima height: "+stage.getHeight()+" e deve essere "+newSceneWidth.doubleValue()*0.56);
                if(!(stage.getHeight()<newSceneWidth.doubleValue()*0.56+5 && stage.getHeight()>newSceneWidth.doubleValue()*0.56-5)) {
                    stage.setHeight(newSceneWidth.doubleValue() * 0.56);
                }
                //stage.heightProperty().multiply((newSceneWidth.doubleValue()-oldSceneWidth.doubleValue())*0.56);
                for (BaseController baseController : baseControllerList) {
                    baseController.onResizeWidth(newSceneWidth,stage.getHeight());
                    //baseController.onResizeHeight(newSceneWidth.doubleValue()*0.5623);
                }
                System.out.println("Height :"+stage.getHeight());
                System.out.println("Width: "+stage.getWidth());
            }
        });
        stage.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                System.out.println("called on resize height");
                if(!(stage.getWidth()<newSceneHeight.doubleValue()/0.56+5 && stage.getWidth()>newSceneHeight.doubleValue()/0.56-5)) {
                    stage.setWidth(newSceneHeight.doubleValue() / 0.56);
                }
               // stage.widthProperty().multiply((newSceneHeight.doubleValue()-oldSceneHeight.doubleValue())*1.7);
                for (BaseController baseController : baseControllerList) {
                    baseController.onResizeHeight(newSceneHeight,scene.getWidth());
                    //baseController.onResizeWidth(newSceneHeight.doubleValue()*1.7784);
                }
                System.out.println("Height :"+stage.getHeight());
                System.out.println("Width: "+stage.getWidth());
            }
        });
        stage.resizableProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                System.out.println("RESIZE "+newValue);
            }
        });
        */
        stage.widthProperty().addListener(listener);
        stage.heightProperty().addListener(listener);




    }

    public Scene getScene() {
        return scene;
    }

    public Stage getStage() {
        return stage;
    }
}
