package ClientPackage.Controller;

import ClientPackage.NetworkInterface.ClientService;
import ClientPackage.NetworkInterface.ClientFactoryService;
import ClientPackage.View.CLIResources.CLIColor;
import ClientPackage.View.GeneralView.BaseView;
import ClientPackage.View.GeneralView.FactoryView;
import CommonModel.GameModel.Action.*;
import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import CommonModel.GameModel.City.City;
import CommonModel.GameModel.City.CityName;
import CommonModel.GameModel.City.RegionName;
import CommonModel.GameModel.Council.King;
import CommonModel.GameModel.Market.BuyableWrapper;
import CommonModel.Snapshot.BaseUser;
import CommonModel.Snapshot.SnapshotToSend;
import Server.Model.Map;
import Utilities.Class.Constants;
import Utilities.Exception.ViewException;
import Utilities.Exception.ActionNotPossibleException;
import CommonModel.GameModel.Council.Councilor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by Emanuele on 09/05/2016.
 */

/**
 * This class manage event and transaction with server
 */
public class ClientController {

    private ClientService clientService;
    private BufferedReader inKeyboard;
    private BaseView baseView;
    private static ClientController clientController;
    private SnapshotToSend snapshot;
    private ArrayList<BaseUser> finalSnapshot = new ArrayList<>();

    private ClientController(){
    }

    public static ClientController getInstance(){
        if (clientController == null)
            clientController = new ClientController();
        return clientController;
    }

    public void init(){
        try {
            String networkMethod;
            String uiMethod;
            inKeyboard = new BufferedReader(new InputStreamReader(System.in));
            networkMethod = getChoiceConnection(inKeyboard);
            String serverIP = getServerIP(inKeyboard);
            clientService = ClientFactoryService.getService(networkMethod,serverIP,this);
            if(clientService.Connect()) {
                uiMethod = getUIMethod(inKeyboard);
                baseView = FactoryView.getBaseView(uiMethod,this);
                baseView.initView();
            }
            else{
                System.out.println("Not connected, sorry");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (ViewException e) {
            e.printStackTrace();
        }
    }

    private String getUIMethod(BufferedReader inKeyboard) throws IOException {
        String method = "";
        System.out.println("Quale UI vuoi utilizzare? \n 1. GUI \n 2. CLI");
        while(method.equals("")) {
            int choice;
            try {
                choice = Integer.parseInt(inKeyboard.readLine());
            }
            catch (Exception e){
                System.out.println(CLIColor.ANSI_RED+"Syntax error, starting default choice: GUI"+CLIColor.ANSI_RESET);
                choice=1;
            }
            switch (choice) {
                case 1:
                    method = Constants.GUI;
                    break;
                case 2:
                    method = Constants.CLI;
                    break;
                default:
                    System.out.println("Scelta non valida");
            }
        }
        return method;
    }

    private void ReadName() throws IOException {
        baseView.showLoginError();
    }

    public String getChoiceConnection (BufferedReader inKeyboard) throws IOException {
        String method;
        System.out.println("Inserisci metodo comunicazione\n 1. RMI \n 2. Socket \n (So che non sai cosa sono ma metti una cosa a caso)");
        String scelta = inKeyboard.readLine();
        if (scelta.equals("1")) {
            method = Constants.RMI;
        } else {
            method = Constants.SOCKET;
        }
        return method;
    }

    public String getServerIP (BufferedReader inKeyboard) throws IOException {
        System.out.println("Inserisci IP GamesManager");
        String scelta = inKeyboard.readLine();
        return scelta;
    }

    /**
     * Called when the name is accepted
     * @param result
     */
    public void onNameReceived(boolean result) {
       try {
           if (!result) {
               ReadName();
           } else {
               baseView.showWaitingForStart();
           }
       }catch (Exception e ){
           e.printStackTrace();
       }
    }

    public void onSendLogin(String userName) {
        clientService.sendName(userName);
    }

    public void setSnapshot(SnapshotToSend snapshot) {
        this.snapshot = snapshot;
        baseView.updateSnapshot();
    }



    public void showMap(ArrayList<Map> mapArrayList) {
        baseView.showMap(mapArrayList);
    }

    public void sendMap(Map map) {
        clientService.sendMap(map);
    }

    public void gameInitialization(SnapshotToSend snapshotToSend) {
        snapshot = snapshotToSend;
        baseView.gameInitialization(snapshotToSend);
    }

    public void isMyTurn() {
        baseView.isMyTurn(snapshot);
    }

    public void mainActionElectCouncilor(Councilor councilor, King king, RegionName regionName) {
        Action action = new MainActionElectCouncilor(councilor, king, regionName);
        try {
            clientService.onAction(action);
        } catch (ActionNotPossibleException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void turnFinished() {
        baseView.turnFinished();
    }

    public void fastActionElectCouncilorWithHelper(Councilor councilor, King king, RegionName region, String councilType) {
        Action action = new FastActionElectCouncilorWithHelper(region,king,councilor,councilType);
        try {
            clientService.onAction(action);
        } catch (ActionNotPossibleException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void setBaseView(BaseView baseView) {
        this.baseView = baseView;
    }

    public SnapshotToSend getSnapshot() {
        return snapshot;
    }

    public void mainActionBuyPermitCard(String text) {
        //Action action = new MainActionBuyPermitCard(snapshot.getVisiblePermitCards().get(text));
    }

    public void mainActionBuyPermitCard(PermitCard permitCard, ArrayList<PoliticCard> politicCards) {
        Action action = new MainActionBuyPermitCard(politicCards,permitCard.getRetroType(),permitCard);
        try {
            clientService.onAction(action);
        } catch (ActionNotPossibleException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    /**
     * called when doing a generic action
     * @param action
     */
    public void doAction(Action action) {
        try {
            clientService.onAction(action);
            baseView.onActionDone(action);
        } catch (ActionNotPossibleException e) {
        } catch (RemoteException e) {
        }
    }

    public void sendSaleItem(ArrayList<BuyableWrapper> realSaleList) {
        clientService.sendSaleItem(realSaleList);
    }

    public void onBuy(ArrayList<BuyableWrapper> buyList) {
        clientService.onBuy(buyList);
    }

    public void removeItemFromMarket(BuyableWrapper item) {
        clientService.onRemoveItemFromMarket(item);
    }

    public void onStartMarket() {
        baseView.onStartMarket();
    }

    public void sendFinishSellPhase() {
        clientService.onFinishSellPhase();
    }

    public void onStartBuyPhase() {
        baseView.onStartBuyPhase();
    }

    public void sendFinishedBuyPhase() {
        clientService.sendFinishedBuyPhase();
    }


    public void onFinishBuyPhase() {
        baseView.onFinishMarket();
    }

    public void selectPermitCard() {
        baseView.selectPermitCard();
    }

    public void selectCityRewardBonus(SnapshotToSend snapshotToSend) {
        this.snapshot=snapshotToSend;
        baseView.selectCityRewardBonus();
    }

    public void getCityRewardBonus(City city1) {
        clientService.getCityRewardBonus(city1);
    }

    public void onSelectPermitCard(PermitCard permitCard) {
        clientService.onSelectPermitCard(permitCard);
    }

    public void onMoveKing(ArrayList<City> kingPath) {
        baseView.onMoveKing(kingPath);
    }

    public void onActionNotPossible(ActionNotPossibleException e) {
        baseView.onActionNotPossibleException(e);
    }

    public void onFinishTurn() {
        clientService.onFinishTurn();
    }

    public void sendMatchFinishedWithWin(ArrayList<BaseUser> finalSnapshot) {
        this.finalSnapshot = finalSnapshot;
        baseView.sendMatchFinishedWithWin();
    }

    public void selectOldPermitCardBonus() {
        baseView.selectOldPermitCardBonus();
    }

    public void onSelectOldPermitCard(PermitCard permitCard) {
        clientService.onSelectOldPermitCard(permitCard);
    }

    public ArrayList<BaseUser> getFinalSnapshot() {
        return finalSnapshot;
    }

    public boolean amIAWinner() {
        if (snapshot.getCurrentUser().getUsername().equals(finalSnapshot.get(0).getUsername())){
            return true;
        }
        return false;
    }

    public BaseUser getUserWithString(String selectedItem) {
        for (BaseUser baseUser : finalSnapshot) {
            if (baseUser.getUsername().equals(selectedItem))
                return baseUser;
        }
        return null;
    }

    public String getUserPosition(String selectedItem) {
        for (int i = 0; i < finalSnapshot.size(); i++) {
            if (finalSnapshot.get(i).getUsername().equals(selectedItem)){
                return Integer.toString(i);
            }
        }
        return Integer.toString(-1);
    }

    public String getUserBuilding(String selectedItem) {
        String toReturn = "";
        for (BaseUser baseUser : finalSnapshot) {
            if (baseUser.getUsername().equals(selectedItem)){
                for (int i = 0; i < baseUser.getUsersEmporium().size(); i++) {
                    if (toReturn == ""){
                        toReturn = baseUser.getUsersEmporium().get(i).getCityName().getCityName();
                    } else {
                        toReturn = toReturn.concat(", " + baseUser.getUsersEmporium().get(i).getCityName().getCityName());
                    }
                }
            }
        }
        return toReturn;
    }

    public ArrayList<String> populateListView(String username) {
        ArrayList<String> toReturn = new ArrayList<>();
        if (snapshot.getUsersInGame().get(username).getPermitCards().size() == 0)
            toReturn.add("NO CITY WHERE BUILD");
        else {
            for (PermitCard permitCard : snapshot.getUsersInGame().get(username).getPermitCards()) {
                for (Character character : permitCard.getCityAcronimous()) {
                    for (CityName cityName : CityName.values()) {
                        if (cityName.getCityName().startsWith(character.toString()) && !toReturn.contains(cityName.getCityName()))
                            toReturn.add(cityName.getCityName());
                    }
                }
            }
        }
        return toReturn;
    }
}
