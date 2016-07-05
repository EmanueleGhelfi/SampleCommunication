package ClientPackage.View.GUIResources.Class;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.GUIResources.CustomComponent.ImageLoader;
import ClientPackage.View.GeneralView.GUIView;
import CommonModel.Snapshot.BaseUser;
import Utilities.Class.Constants;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
    private ImageView winnerOrLoser = new ImageView();
    private ImageView kingOrJester = new ImageView();
    private ImageView innerImage;
    private final JFXListView<String> ranking = new JFXListView<>();
    private final ArrayList<String> usernameRanking = new ArrayList<>();
    private PopOver innerPopOver;
    private PopOver popOverOfTheImage;
    private final Pane innerPaneWhereShow = new Pane();
    private ImageView backgroundImage;

    @FXML
    private StackPane rootPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setClientController(ClientController clientController, GUIView baseView) {
        this.clientController = clientController;
        this.baseView = baseView;
        this.displayFinalScreen();
    }

    private void displayFinalScreen() {
        this.backgroundImage = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "/FinalBackground.png"));
        this.backgroundImage.fitWidthProperty().bind(this.rootPane.widthProperty());
        this.backgroundImage.fitHeightProperty().bind(this.rootPane.heightProperty());
        this.rootPane.getChildren().add(this.backgroundImage);
        this.innerImage = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "/InnKeeper.png"));
        this.innerImage.fitHeightProperty().bind(this.backgroundImage.fitHeightProperty().divide(2));
        this.innerImage.setPreserveRatio(true);
        this.rootPane.getChildren().add(this.innerImage);
        StackPane.setAlignment(this.innerImage, Pos.BOTTOM_LEFT);
        for (BaseUser baseUser : this.clientController.getFinalSnapshot()) {
            this.usernameRanking.add(baseUser.getUsername());
        }
        this.ranking.setItems(FXCollections.observableArrayList(this.usernameRanking));
        this.ranking.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                FinishMatchController.this.displayInfo(FinishMatchController.this.ranking.getSelectionModel().getSelectedItem());
            }
        });
        this.ranking.setStyle("-fx-background-color: transparent");
        this.ranking.prefWidthProperty().bind(this.rootPane.prefWidthProperty().divide(12));
        this.rootPane.getChildren().add(this.ranking);
        StackPane.setAlignment(this.ranking, Pos.BOTTOM_RIGHT);
        this.winnerOrLoser.fitWidthProperty().bind(this.backgroundImage.fitWidthProperty().multiply(0.3542));
        this.winnerOrLoser.setPreserveRatio(true);
        this.rootPane.getChildren().add(this.winnerOrLoser);
        StackPane.setAlignment(this.winnerOrLoser, Pos.BOTTOM_CENTER);
        this.kingOrJester.fitWidthProperty().bind(this.backgroundImage.fitWidthProperty().multiply(0.1149));
        this.kingOrJester.setPreserveRatio(true);
        this.rootPane.getChildren().add(this.kingOrJester);
        StackPane.setAlignment(this.kingOrJester, Pos.CENTER);
        if (this.clientController.amIAWinner()) {
            this.winnerOrLoser = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "/YouWin2.png"));
            this.kingOrJester = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "/KingThrone.png"));
            this.popOverOfTheImage = new PopOver();
            StackPane stackPaneOfTheImage = new StackPane();
            Text textOfImage = new Text();
            textOfImage.setText("Felice della tua vittoria " + this.clientController.getSnapshot().getCurrentUser() + ".\n" +
                    "Il mio cor si sollazza al saper che ho un erede di cotanta bravura.");
            textOfImage.setFont(Font.font("Papyrus", FontWeight.BOLD, FontPosture.ITALIC, 15));
            stackPaneOfTheImage.getChildren().add(textOfImage);
            StackPane.setAlignment(textOfImage, Pos.CENTER);
            this.popOverOfTheImage.setContentNode(stackPaneOfTheImage);
            this.popOverOfTheImage.show(this.kingOrJester);
        }
        if (!this.clientController.amIAWinner()) {
            this.winnerOrLoser = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "/GameOver2.png"));
            this.kingOrJester = new ImageView(ImageLoader.getInstance().getImage(Constants.IMAGE_PATH + "/Jester.png"));
            this.popOverOfTheImage = new PopOver();
            StackPane stackPaneOfTheImage = new StackPane();
            Text textOfImage = new Text();
            textOfImage.setText("Se vincer non saprai\nPrima o poi tu perirai\nForse meglio cambiar gioco\nPerchè bravo lo sei poco\n");
            textOfImage.setFont(Font.font("Papyrus", FontWeight.BOLD, FontPosture.ITALIC, 15));
            stackPaneOfTheImage.getChildren().add(textOfImage);
            StackPane.setAlignment(textOfImage, Pos.CENTER);
            this.popOverOfTheImage.setContentNode(textOfImage);
            this.popOverOfTheImage.show(this.kingOrJester);
        }
    }

    private void displayPopOverOfImage(boolean win) {
        this.winnerOrLoser.fitWidthProperty().bind(this.backgroundImage.fitWidthProperty().multiply(0.3542));
        this.winnerOrLoser.setPreserveRatio(true);
        this.rootPane.getChildren().add(this.winnerOrLoser);
        StackPane.setAlignment(this.winnerOrLoser, Pos.BOTTOM_CENTER);
        this.kingOrJester.fitWidthProperty().bind(this.backgroundImage.fitWidthProperty().multiply(0.1149));
        this.kingOrJester.setPreserveRatio(true);
        this.rootPane.getChildren().add(this.kingOrJester);
        StackPane.setAlignment(this.kingOrJester, Pos.CENTER);
        this.popOverOfTheImage = new PopOver();
        StackPane stackPaneOfTheImage = new StackPane();
        Text textOfImage = new Text();
        if (win)
            textOfImage.setText("Felice della tua vittoria " + this.clientController.getSnapshot().getCurrentUser() + ".\n" +
                    "Il mio cor si sollazza al saper che ho un erede di cotanta bravura.");
        else
            textOfImage.setText("Se vincer non saprai\nPrima o poi tu perirai\nForse meglio cambiar gioco\nPerchè bravo lo sei poco\n");
        textOfImage.setFont(Font.font("Papyrus", FontWeight.BOLD, FontPosture.ITALIC, 15));
        stackPaneOfTheImage.getChildren().add(textOfImage);
        StackPane.setAlignment(textOfImage, Pos.CENTER);
        this.popOverOfTheImage.setContentNode(stackPaneOfTheImage);
        this.popOverOfTheImage.show(this.kingOrJester);
    }

    private void displayInfo(String selectedItem) {
        BaseUser baseUser = this.clientController.getUserWithString(selectedItem);
        StackPane popOverStackPane = new StackPane();
        popOverStackPane.setAlignment(Pos.CENTER);
        Text innerText = new Text();
        innerText.setText("Vò narrando delle gesta di " + baseUser.getUsername() + ".\nSi posizionò " + this.clientController.getUserPosition(selectedItem) + "° nella maggior gara del nuovo anno.\n"
                + "Riuscì ad ottenere molti scudi prestigiosi dalle sue " + baseUser.getVictoryPathPosition() + " vittorie.\n"
                + "Conobbe vari nobili città dove gli vennero donati prestigiosi premi. In particolare, rimasto molto affascinato, si fermò nel " + baseUser.getNobilityPathPosition().getPosition() + "° posto.\n"
                + "Riuscì ad ottenere grandi ricompense, fino ad arrivare a " + baseUser.getCoinPathPosition() + " monete d'oro.\n"
                + "Conobbi anche tutti i suoi " + baseUser.getHelpers().size() + " servitori ed aiutanti, a lui molto cari.\n"
                + "Grande personaggio fu questo " + baseUser.getUsername() + ". Mi ricorderò sempre di quel giorno che mi fece vedere le sue " + baseUser.getPoliticCardNumber()
                + " prestigiose carte politiche con cui poteva soddisfare qualsiasi consiglio.\n"
                + "In tutto il mondo è noto il suo nome. Ovunque si sa che il magnifico " + baseUser.getUsername() + " riuscì a costruire empori in molte città, come\n"
                + this.clientController.getUserBuilding(selectedItem) + "\n" + "Grande uomo fu " + baseUser.getUsername() + ", scaltro nel gioco quanto intelligente nelle azioni.");
        innerText.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 15));
        popOverStackPane.getChildren().add(innerText);
        this.innerPopOver = new PopOver();
        this.innerPopOver.setContentNode(popOverStackPane);
        this.innerPopOver.show(this.innerImage);
    }

}