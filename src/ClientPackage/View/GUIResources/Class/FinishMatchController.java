package ClientPackage.View.GUIResources.Class;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.GUIResources.customComponent.ImageLoader;
import ClientPackage.View.GeneralView.GUIView;
import CommonModel.Snapshot.BaseUser;
import Utilities.Class.Constants;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.controlsfx.control.PopOver;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by Giulio on 25/06/2016.
 */
public class FinishMatchController implements Initializable {

    private ClientController clientController;
    private GUIView baseView;
    private ImageView winnerOrLoser;
    private ScrollPane scrollRanking;
    private JFXListView<String> ranking;
    private ArrayList<String> usernameRanking;
    private PopOver innerPopOver;
    private ImageView innerImage;
    private Pane innerPaneWhereShow = new Pane();
    private ImageView backgroundImage;

    @FXML GridPane gridPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setClientController(ClientController clientController, GUIView baseView) {
        this.clientController = clientController;
        this.baseView = baseView;
        displayFinalScreen();
    }

    private void displayFinalScreen() {
        backgroundImage = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "/LoginBackground.png"));
        gridPane.add(backgroundImage, 0, 0);
        GridPane.setColumnSpan(backgroundImage, 2);
        GridPane.setRowSpan(backgroundImage, 2);
        backgroundImage.fitWidthProperty().bind(gridPane.widthProperty());
        backgroundImage.fitHeightProperty().bind(gridPane.heightProperty());

        gridPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("event x and y "+event.getX()/gridPane.getWidth()+" "+event.getY()/gridPane.getHeight());
            }
        });
        innerImage = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "/InnKeeper.png"));
        innerPaneWhereShow.setPrefWidth(10);
        innerPaneWhereShow.setPrefHeight(10);

        for (BaseUser baseUser : clientController.getFinalSnapshot()) {
            usernameRanking.add(baseUser.getUsername());
        }
        ranking.setItems(FXCollections.observableArrayList(usernameRanking));
        if(clientController.amIAWinner()) {
            winnerOrLoser = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "/YouWin.png"));
        } else {
            winnerOrLoser = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "/GameOver.png"));
        }
        ranking.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                displayInfo(ranking.getSelectionModel().getSelectedItem());
            }
        });
        ranking.setStyle("-fx-background-color: transparent");
        gridPane.add(innerImage, 0, 0);
        GridPane.setRowSpan(innerImage, 3);
        gridPane.add(innerImage, 0, 0);
        GridPane.setRowSpan(innerImage, 3);
        gridPane.add(ranking, 1, 1);
        gridPane.add(winnerOrLoser, 0, 0);
        GridPane.setRowSpan(winnerOrLoser, 3);
        GridPane.setColumnSpan(winnerOrLoser, 3);
        winnerOrLoser.toBack();
        backgroundImage.toBack();
        gridPane.getChildren().addAll(innerImage, innerPaneWhereShow, ranking, winnerOrLoser);
    }

    private void displayInfo(String selectedItem) {
        BaseUser baseUser = clientController.getUserWithString(selectedItem);
        StackPane popOverStackPane = new StackPane();
        popOverStackPane.setAlignment(Pos.CENTER);
        Text innerText = new Text();
        innerText.setText("Il giocatore " + baseUser.getUsername() + " si è posizionato " + clientController.getUserPosition(selectedItem) + "°\n"
            + "Ecco le sue posizioni:\n" + baseUser.getVictoryPathPosition() + "\t nel percorso della vittoria\n"
            + baseUser.getNobilityPathPosition().getPosition() + "\t nel percorso della nobiltà\n" + baseUser.getCoinPathPosition()
            + "\t nel percorso della ricchezza\n" + "Aveva" + baseUser.getHelpers() + " aiutanti e" + baseUser.getPoliticCardNumber() + " carte politica\n"
            + "Ha edificato in " + clientController.getUserBuilding(selectedItem));

        popOverStackPane.getChildren().add(innerText);
        innerPopOver.setContentNode(popOverStackPane);
        innerPopOver.show(innerPaneWhereShow);
    }

}