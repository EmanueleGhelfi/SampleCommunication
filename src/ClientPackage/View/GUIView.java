package ClientPackage.View;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Emanuele on 13/05/2016.
 */
public class GUIView extends Application implements BaseView {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ClientPackage/View/GUIResources/sample.fxml"));
        Parent screen = loader.load();
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
}
