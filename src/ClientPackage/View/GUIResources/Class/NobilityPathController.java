package ClientPackage.View.GUIResources.Class;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.GeneralView.GUIView;
import Utilities.Class.Constants;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Giulio on 14/06/2016.
 */
public class NobilityPathController implements Initializable {

    @FXML private ImageView backgroundNobility;
    @FXML private GridPane nobilityPath;
    private ImageView backgroundImage;
    private NobilityPathController nobilityPathController = this;
    private MatchController matchController = new MatchController();
    private GUIView guiView = new GUIView();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createNobilityPathPane();
    }

    private void createNobilityPathPane() {
        backgroundImage.setImage(new Image(Constants.IMAGE_PATH + "/NobilityPathSmall.png"));
        backgroundImage.fitWidthProperty().bind(nobilityPath.widthProperty());
        backgroundImage.fitHeightProperty().bind(nobilityPath.heightProperty());
        GridPane.setColumnSpan(backgroundImage, 3);
        GridPane.setRowSpan(backgroundImage, 3);
        nobilityPath.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("event x and y "+event.getX()/ nobilityPath.getWidth()+" "+event.getY()/ nobilityPath.getHeight());
            }
        });
        nobilityPath.setOpacity(0.8);
        nobilityPath.prefHeightProperty().bind(matchController.getBackground().prefHeightProperty().divide(5));
        nobilityPath.prefWidthProperty().bind(matchController.getBackground().prefWidthProperty());
        nobilityPath.setAlignment(Pos.TOP_CENTER);
        nobilityPath.setStyle("-fx-background-color: red");
        matchController.getGridPane().add(nobilityPath, 2, 0);
        GridPane.setColumnSpan(nobilityPath, 3);
    }


    public void setClientController(ClientController clientController, GUIView guiView) {
        this.matchController = matchController;
        this.guiView = guiView;
    }

    public void setMatchController(MatchController matchController) {
        this.matchController = matchController;
    }
}
