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
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
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
    private ImageView kingOrJester;
    private JFXListView<String> ranking = new JFXListView<>();
    private ArrayList<String> usernameRanking = new ArrayList<>();
    private PopOver innerPopOver;
    private ImageView innerImage;
    private Pane innerPaneWhereShow = new Pane();
    private ImageView backgroundImage;
    private Pane backgroundPane = new Pane();

    @FXML private StackPane rootPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setClientController(ClientController clientController, GUIView baseView) {
        this.clientController = clientController;
        this.baseView = baseView;
        displayFinalScreen();
    }

    private void displayFinalScreen() {
        backgroundImage = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "/FinalBackground.png"));
        backgroundImage.fitWidthProperty().bind(rootPane.widthProperty());
        backgroundImage.fitHeightProperty().bind(rootPane.heightProperty());
        rootPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("event x and y "+event.getX()/rootPane.getWidth()+" "+event.getY()/rootPane.getHeight());
            }
        });
        rootPane.getChildren().add(backgroundImage);


        innerImage = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "/InnKeeper.png"));
        innerImage.fitHeightProperty().bind(backgroundImage.fitHeightProperty().divide(2));
        innerImage.setPreserveRatio(true);
        rootPane.getChildren().add(innerImage);
        StackPane.setAlignment(innerImage, Pos.BOTTOM_LEFT);


        for (BaseUser baseUser : clientController.getFinalSnapshot()) {
            usernameRanking.add(baseUser.getUsername());
        }
        ranking.setItems(FXCollections.observableArrayList(usernameRanking));
        ranking.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                displayInfo(ranking.getSelectionModel().getSelectedItem());
            }
        });
        ranking.setStyle("-fx-background-color: transparent");
        ranking.prefWidthProperty().bind(rootPane.widthProperty().divide(15));
        rootPane.getChildren().add(ranking);
        StackPane.setAlignment(ranking, Pos.BOTTOM_RIGHT);
        winnerOrLoser.fitWidthProperty().bind(backgroundImage.fitWidthProperty().multiply(0.3542));
        winnerOrLoser.setPreserveRatio(true);
        StackPane.setAlignment(winnerOrLoser, Pos.BOTTOM_CENTER);
        rootPane.getChildren().add(winnerOrLoser);
        kingOrJester.fitWidthProperty().bind(backgroundImage.fitWidthProperty().multiply(0.1149));
        kingOrJester.setPreserveRatio(true);
        rootPane.getChildren().add(kingOrJester);
        StackPane.setAlignment(kingOrJester, Pos.CENTER);

        winnerOrLoser.toBack();
        kingOrJester.toBack();
        ranking.toBack();
        backgroundImage.toBack();

        if(clientController.amIAWinner()) {
            winnerOrLoser = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "/YouWin2.png"));
            kingOrJester = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "/KingThrone.png"));
            displayPopOverOfImage(true);
        } else {
            winnerOrLoser = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "/GameOver2.png"));
            kingOrJester = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "/Jester.png"));
            displayPopOverOfImage(false);
        }

    }

    private void displayPopOverOfImage(boolean win) {
        PopOver popOver = new PopOver();
        StackPane stackPaneOfTheImage = new StackPane();
        Text textOfImage = new Text();
        if (win)
            textOfImage.setText("Felice della tua vittoria " + clientController.getSnapshot().getCurrentUser() + ".\n" +
                    "Il mio cor si sollazza al saper che ho un erede di cotanta bravura.");
        else
            textOfImage.setText("Se vincer non saprai\nPrima o poi tu perirai\nForse meglio cambiar gioco\nPerchè bravo lo sei poco\n");
        textOfImage.setFont(Font.font("Papyrus", FontWeight.BOLD, FontPosture.ITALIC, 15));
        stackPaneOfTheImage.getChildren().add(textOfImage);
        StackPane.setAlignment(textOfImage, Pos.CENTER);
        popOver.setContentNode(stackPaneOfTheImage);
        popOver.show(kingOrJester);
    }

    private void displayInfo(String selectedItem) {
        BaseUser baseUser = clientController.getUserWithString(selectedItem);
        StackPane popOverStackPane = new StackPane();
        popOverStackPane.setAlignment(Pos.CENTER);
        Text innerText = new Text();
        innerText.setText("Vò narrando delle gesta di " + baseUser.getUsername() + ".\nSi posizionò " + clientController.getUserPosition(selectedItem) + "° nella maggior gara del nuovo anno.\n"
                + "Riuscì ad ottenere molti scudi prestigiosi dalle sue " + baseUser.getVictoryPathPosition() + " vittorie.\n"
                + "Conobbe vari nobili città dove gli vennero donati prestigiosi premi. In particolare, rimasto molto affascinato, si fermò nel " + baseUser.getNobilityPathPosition().getPosition() + "° posto.\n"
                + "Riuscì ad ottenere grandi ricompense, fino ad arrivare a " + baseUser.getCoinPathPosition() + " monete d'oro.\n"
                + "Conobbi anche tutti i suoi " + baseUser.getHelpers().size() + " servitori ed aiutanti, a lui molto cari.\n"
                + "Grande personaggio fu questo " + baseUser.getUsername() + ". Mi ricorderò sempre di quel giorno che mi fece vedere le sue " + baseUser.getPoliticCardNumber()
                + " prestigiose carte politiche con cui poteva soddisfare qualsiasi consiglio.\n"
                + "In tutto il mondo è noto il suo nome. Ovunque si sa che il magnifico " + baseUser.getUsername() + " riuscì a costruire empori in molte città, come\n"
                + clientController.getUserBuilding(selectedItem) +"\n" + "Grande uomo fu " + baseUser.getUsername() + ", scaltro nel gioco quanto intelligente nelle azioni.");
        innerText.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 15));
        popOverStackPane.getChildren().add(innerText);
        innerPopOver = new PopOver();
        innerPopOver.setContentNode(popOverStackPane);
        innerPopOver.show(innerImage);
    }



}