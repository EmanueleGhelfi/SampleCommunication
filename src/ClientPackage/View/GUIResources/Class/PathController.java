package ClientPackage.View.GUIResources.Class;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.GeneralView.GUIView;
import CommonModel.GameModel.Bonus.Generic.Bonus;
import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.City.City;
import CommonModel.Snapshot.BaseUser;
import CommonModel.Snapshot.SnapshotToSend;
import Utilities.Class.CircularArrayList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by Giulio on 28/05/2016.
 */
public class PathController implements Initializable, BaseController{

    private int userCounter = 0;
    private int permitCardCounter = 0;
    private int emporiumCounter = 0;
    private ClientController clientController;
    private GUIView guiView;
    private CircularArrayList<BaseUser> userCircularArrayList = new CircularArrayList<>();
    private CircularArrayList<City> cityCircularArrayList = new CircularArrayList<>();
    private CircularArrayList<PermitCard> permitCardCircularArrayList = new CircularArrayList<>();
    private Image bonusImage;
    private String bonusName;
    @FXML private ImageView bonusImageView = new ImageView(bonusImage);
    @FXML GridPane nobilityPath;
    @FXML Label playerName;
    @FXML Text politicCardNumber;
    @FXML Text helperNumber;
    @FXML Text victoryPathNumber;
    @FXML Text moneyPathNumber;
    @FXML Text nobilityPathNumber;
    @FXML Label permitCardCity;
    @FXML Text emporiumCity;


    @Override
    public void initialize(URL location, ResourceBundle resources) {}


    @Override
    public void updateView() {
        System.out.println("update view in path controller");
        for (BaseUser baseUser : clientController.getSnapshot().getUsersInGame().values()) {
            if (!baseUser.getUsername().equals(clientController.getSnapshot().getCurrentUser().getUsername())) {
                userCircularArrayList.add(baseUser);
            }
        }
        populatePermitCardAndEmporiums();
        showInformation();
        populateNobilityPath();
    }

    @Override
    public void setClientController(ClientController clientController, GUIView guiView) {
        System.out.println("Set client controller");
        this.clientController = clientController;
        this.guiView = guiView;
        guiView.registerBaseController(this);
        updateView();
    }

    private void populatePermitCardAndEmporiums() {
        for (PermitCard permitCard : userCircularArrayList.get(userCounter).getPermitCards()){
            permitCardCircularArrayList.add(permitCard);
        }
        for (City city : userCircularArrayList.get(userCounter).getUsersEmporium()){
            cityCircularArrayList.add(city);
        }
    }

    @Override
    public void setMyTurn(boolean myTurn, SnapshotToSend snapshot) {
    }

    @Override
    public void onStartMarket() {

    }

    @Override
    public void onStartBuyPhase() {

    }

    @Override
    public void onFinishMarket() {

    }

    @Override
    public void onResizeHeight(double height, double width) {

    }

    @Override
    public void onResizeWidth(double width, double height) {

    }

    @Override
    public void selectPermitCard() {

    }

    @Override
    public void selectCityRewardBonus() {

    }

    @Override
    public void moveKing(ArrayList<City> baseController) {

    }

    public void nextPlayer(Event event) {
        userCounter++;
        emporiumCounter = 0;
        permitCardCounter = 0;
        populatePermitCardAndEmporiums();
        showInformation();
    }

    public void prevPlayer(Event event) {
        userCounter--;
        emporiumCounter = 0;
        permitCardCounter = 0;
        populatePermitCardAndEmporiums();
        showInformation();
    }

    private void showInformation() {
        playerName.setText(userCircularArrayList.get(userCounter).getUsername());
        politicCardNumber.setText(Integer.toString(userCircularArrayList.get(userCounter).getPoliticCardNumber()));
        helperNumber.setText(Integer.toString(userCircularArrayList.get(userCounter).getHelpers().size()));
        victoryPathNumber.setText(Integer.toString(userCircularArrayList.get(userCounter).getVictoryPathPosition()));
        moneyPathNumber.setText(Integer.toString(userCircularArrayList.get(userCounter).getCoinPathPosition()));
        nobilityPathNumber.setText(Integer.toString(userCircularArrayList.get(userCounter).getNobilityPathPosition().getPosition()));
        if(permitCardCircularArrayList.size()>0){
            permitCardCity.setText(permitCardCircularArrayList.get(permitCardCounter).getCityString());
        }
        if(cityCircularArrayList.size()>0) {
            emporiumCity.setText(cityCircularArrayList.get(emporiumCounter).getCityName().getCityName());
        }

    }

    public void nextPermitCard(Event event) {
        permitCardCounter++;
        showInformation();
    }

    public void prevPermitCard(Event event) {
        permitCardCounter--;
        showInformation();
    }

    public void prevEmporium(Event event) {
        emporiumCounter--;
        showInformation();
    }

    public void nextEmporium(Event event) {
        emporiumCounter++;
        showInformation();
    }

    private void populateNobilityPath() {
        for (int count = 0; count < clientController.getSnapshot().getNobilityPathPosition().length; count++){
            if(clientController.getSnapshot().getNobilityPathPosition()[count]!=null &&clientController.getSnapshot().getNobilityPathPosition()[count].getBonus()!=null ) {
                ArrayList<Bonus> bonusArrayList = clientController.getSnapshot().getNobilityPathPosition()[count].getBonus().getBonusArrayList();
                if (bonusArrayList.size() > 0) {
                    for (int counter = 0; counter < bonusArrayList.size(); counter++) {
                        bonusName = bonusArrayList.get(counter).getBonusName();
                        try {
                            bonusImage = new Image("/ClientPackage/View/GUIResources/Image/bonus/" + bonusName);
                            nobilityPath.add(bonusImageView, count + 1, counter);
                        } catch (IllegalArgumentException e) {
                            System.out.println("image not found in path controller");
                        }

                    }
                }
            }
        }
    }

}
