package ClientPackage.Controller;

import ClientPackage.NetworkInterface.ClientFactoryService;
import ClientPackage.NetworkInterface.ClientService;
import ClientPackage.View.CLIResources.CLIColor;
import ClientPackage.View.GeneralView.BaseView;
import ClientPackage.View.GeneralView.FactoryView;
import CommonModel.GameModel.Action.Action;
import CommonModel.GameModel.Action.FastActionElectCouncilorWithHelper;
import CommonModel.GameModel.Action.MainActionBuyPermitCard;
import CommonModel.GameModel.Action.MainActionElectCouncilor;
import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import CommonModel.GameModel.City.City;
import CommonModel.GameModel.City.CityName;
import CommonModel.GameModel.City.RegionName;
import CommonModel.GameModel.Council.Councilor;
import CommonModel.GameModel.Council.King;
import CommonModel.GameModel.Market.BuyableWrapper;
import CommonModel.Snapshot.BaseUser;
import CommonModel.Snapshot.SnapshotToSend;
import Server.Model.Map;
import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;
import Utilities.Exception.ViewException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

/** This class manage event and transaction with server.
 * Is the controller in MVC pattern. This is the class that performs the logical part in the client.
 * Created by Emanuele on 09/05/2016.
 */
public class ClientController {

    private static ClientController clientController;
    private ClientService clientService;
    private BufferedReader inKeyboard;
    private BaseView baseView;
    private SnapshotToSend snapshot;
    private ArrayList<BaseUser> finalSnapshot = new ArrayList<>();

    /** Constructor (private) to make it a Singleton
     */
    private ClientController() {
    }

    /** Method that allows the creation of a ClientController (using the Singleton pattern)
     * @return the singleton, instance of clientController
     */
    public static ClientController getInstance() {
        if (clientController == null)
            clientController = new ClientController();
        return clientController;
    }

    /** Initialize the game asking user for a choice in Newtork, IP and UI.
     */
    public void init() {
        try {
            String networkMethod;
            String uiMethod;
            inKeyboard = new BufferedReader(new InputStreamReader(System.in));
            networkMethod = getChoiceConnection(inKeyboard);
            String serverIP = getServerIP(inKeyboard);
            clientService = ClientFactoryService.getService(networkMethod, serverIP, this);
            if (clientService.Connect()) {
                uiMethod = getUIMethod(inKeyboard);
                baseView = FactoryView.getBaseView(uiMethod, this);
                baseView.initView();
            } else {
                System.out.println("Non connesso");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (ViewException e) {
            e.printStackTrace();
        }
    }

    /** You can choose the UI method
     * @param inKeyboard is the standardIn
     * @return the method choosen
     * @throws IOException raised by using wrongly the inKeyboard
     */
    private String getUIMethod(BufferedReader inKeyboard) throws IOException {
        String method = "";
        System.out.println("Quale UI vuoi utilizzare? \n 1. GUI \n 2. CLI");
        while (method.equals("")) {
            int choice;
            try {
                choice = Integer.parseInt(inKeyboard.readLine());
            } catch (Exception e) {
                System.out.println(CLIColor.ANSI_RED + "Syntax error, starting default choice: GUI" + CLIColor.ANSI_RESET);
                choice = 1;
            }
            switch (choice) {
                case 1:
                    method = Constants.GUI;
                    break;
                case 2:
                    method = Constants.CLI;
                    break;
                default:
                    method = Constants.GUI;
                    break;
            }
        }
        return method;
    }

    /** Shows login error
     * @throws IOException from the server
     */
    private void ReadName() throws IOException {
        baseView.showLoginError();
    }

    /** You can choose the Network method of the connection
     * @param inKeyboard is the standardIn
     * @return the method choosen
     * @throws IOException raised by using wrongly the inKeyboard
     */
    public String getChoiceConnection(BufferedReader inKeyboard) throws IOException {
        String method;
        System.out.println("Inserisci metodo comunicazione\n 1. RMI \n 2. Socket \n (So che non sai cosa sono ma metti una cosa a caso)");
        String scelta = inKeyboard.readLine();
        switch (scelta){
            case ("1"): {
                method = Constants.RMI;
                break;
            }
            case ("2"): {
                method = Constants.SOCKET;
                break;
            }
            default:{
                System.out.println(CLIColor.ANSI_RED + "Syntax error, starting default choice: SOCKET" + CLIColor.ANSI_RESET);
                method = Constants.SOCKET;
                break;
            }
        }
        return method;
    }

    /** Gets the IP that you want to connect to.
     * @param inKeyboard is the standardIn
     * @return the method choosen
     * @throws IOException raised by using wrongly the inKeyboard
     */
    public String getServerIP(BufferedReader inKeyboard) throws IOException {
        System.out.println("Inserisci IP GamesManager");
        String scelta = inKeyboard.readLine();
        return scelta;
    }

    /** Called when the name is accepted
     * @param result is the result from server if the name choosen is good
     */
    public void onNameReceived(boolean result) {
        try {
            if (!result) {
                ReadName();
            } else {
                baseView.showWaitingForStart();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Bridge to send server the name choosen
     * @param userName is the name choosen in login screen
     */
    public void onSendLogin(String userName) {
        clientService.sendName(userName);
    }

    /** Bridge between server and view, used to show the map to choose
     * @param mapArrayList contains every Map
     */
    public void showMap(ArrayList<Map> mapArrayList) {
        baseView.showMap(mapArrayList);
    }

    /** Send the map choosen to server
     * @param map is the map choosen in the client
     */
    public void sendMap(Map map) {
        clientService.sendMap(map);
    }

    /** Set the snapshot received and send it to the view to set first things.
     * @param snapshotToSend is the first snapshot received
     */
    public void gameInitialization(SnapshotToSend snapshotToSend) {
        snapshot = snapshotToSend;
        baseView.gameInitialization(snapshotToSend);
    }

    /** Notify to the view that is my turn
     */
    public void isMyTurn() {
        baseView.isMyTurn(snapshot);
    }

    /** Send to server that a main action is done with its parameters.
     * @param councilor is the councilor that i want to elect
     * @param king if the council is the king one
     * @param regionName if the council is one of the regions
     */
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

    /** Send that the turn is finished
     */
    public void turnFinished() {
        baseView.turnFinished();
    }

    /** Send to server that a fast action is done with its parameters.
     * @param councilor is the councilor that i want to elect
     * @param king if the council is the king one
     * @param region if the council is one of the regions
     * @param councilType is the type of the council, if region or king one
     */
    public void fastActionElectCouncilorWithHelper(Councilor councilor, King king, RegionName region, String councilType) {
        Action action = new FastActionElectCouncilorWithHelper(region, king, councilor, councilType);
        try {
            clientService.onAction(action);
        } catch (ActionNotPossibleException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /** Sets the baseView
     * @param baseView is the baseView to set
     */
    public void setBaseView(BaseView baseView) {
        this.baseView = baseView;
    }

    /** Getter
     * @return the snapshot
     */
    public SnapshotToSend getSnapshot() {
        return snapshot;
    }

    /** Setter and update the snapshot;
     * @param snapshot is the snapshot to set;
     */
    public void setSnapshot(SnapshotToSend snapshot) {
        this.snapshot = snapshot;
        baseView.updateSnapshot();
    }

    /** Send to server that a main action is done with its parameters.
     * @param permitCard is the permit card to buy
     * @param politicCards are the politic card that satisfy the council
     */
    public void mainActionBuyPermitCard(PermitCard permitCard, ArrayList<PoliticCard> politicCards) {
        Action action = new MainActionBuyPermitCard(politicCards, permitCard.getRetroType(), permitCard);
        try {
            clientService.onAction(action);
        } catch (ActionNotPossibleException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /** Called when doing a generic action
     * @param action is the generic action
     */
    public void doAction(Action action) {
        try {
            clientService.onAction(action);
            baseView.onActionDone(action);
        } catch (ActionNotPossibleException e) {
        } catch (RemoteException e) {
        }
    }

    /** Bridge used to send the item on sale in the market phase
     * @param realSaleList are the real sale list
     */
    public void sendSaleItem(ArrayList<BuyableWrapper> realSaleList) {
        clientService.sendSaleItem(realSaleList);
    }

    /** Bridge used to send the item that can be bought on market phase
     * @param buyList is the buy list
     */
    public void onBuy(ArrayList<BuyableWrapper> buyList) {
        clientService.onBuy(buyList);
    }

    /** Bridge used to communicate items that are to be remove.
     * @param item is the item list
     */
    public void removeItemFromMarket(BuyableWrapper item) {
        clientService.onRemoveItemFromMarket(item);
    }

    /** Bridge used to communicate that market has started
     */
    public void onStartMarket() {
        baseView.onStartMarket();
    }

    /** Bridge used to communicates that is finished the sell phase
     */
    public void sendFinishSellPhase() {
        clientService.onFinishSellPhase();
    }

    /** Bridge used to communicates that is started the buy phase
     */
    public void onStartBuyPhase() {
        baseView.onStartBuyPhase();
    }

    /** Bridge that communicates that is finished the buy phase to the server
     */
    public void sendFinishedBuyPhase() {
        clientService.sendFinishedBuyPhase();
    }

    /** Bridge that communicates that is finished the buy phase to the view
     */
    public void onFinishBuyPhase() {
        baseView.onFinishMarket();
    }

    /** Bridge that communicates user to select the permit card
     */
    public void selectPermitCard() {
        baseView.selectPermitCard();
    }

    /** Bridge used to set the snapshot and select the city reward bonus
     * @param snapshotToSend is the snapshot to set
     */
    public void selectCityRewardBonus(SnapshotToSend snapshotToSend) {
        this.snapshot = snapshotToSend;
        baseView.selectCityRewardBonus();
    }

    /** Bridge used to send to server the city where i want take the bonus
     * @param city1 is the city choosen
     */
    public void getCityRewardBonus(City city1) {
        clientService.getCityRewardBonus(city1);
    }

    /** Bridge used to communicate server the permit card selected
     * @param permitCard is the permit card choosen
     */
    public void onSelectPermitCard(PermitCard permitCard) {
        clientService.onSelectPermitCard(permitCard);
    }

    /** Bridge used to communicates client that king can be moved
     * @param kingPath is the path where move king
     */
    public void onMoveKing(ArrayList<City> kingPath) {
        baseView.onMoveKing(kingPath);
    }

    /** Bridge that communicates client that action is not possible
     * @param e is the exception
     */
    public void onActionNotPossible(ActionNotPossibleException e) {
        baseView.onActionNotPossibleException(e);
    }

    /** Bridge that communicates server the finish of the turn
     */
    public void onFinishTurn() {
        clientService.onFinishTurn();
    }

    /** Bridge used to communicate the match is finished
     * @param finalSnapshot the arraylist sorted with the ranking
     */
    public void sendMatchFinishedWithWin(ArrayList<BaseUser> finalSnapshot) {
        this.finalSnapshot = finalSnapshot;
        baseView.sendMatchFinishedWithWin();
    }

    /** Bridge used to communicate view the old permit card selected
     */
    public void selectOldPermitCardBonus() {
        baseView.selectOldPermitCardBonus();
    }

    /** Bridge used to communicate server the old permit card choosen
     * @param permitCard is the permit card choosen
     */
    public void onSelectOldPermitCard(PermitCard permitCard) {
        clientService.onSelectOldPermitCard(permitCard);
    }

    /** Getter
     * @return the last snapshot
     */
    public ArrayList<BaseUser> getFinalSnapshot() {
        return finalSnapshot;
    }

    /** Returns if i have won the match
     * @return the value if i am a winner
     */
    public boolean amIAWinner() {
        return snapshot.getCurrentUser().getUsername().equals(finalSnapshot.get(0).getUsername());
    }

    /** Gets the user information
     * @param selectedItem is the username
     * @return the user with that username
     */
    public BaseUser getUserWithString(String selectedItem) {
        for (BaseUser baseUser : finalSnapshot) {
            if (baseUser.getUsername().equals(selectedItem))
                return baseUser;
        }
        return null;
    }

    /** Gets the user position in the rank
     * @param selectedItem is the username
     * @return the position in ranking
     */
    public String getUserPosition(String selectedItem) {
        for (int i = 0; i < finalSnapshot.size(); i++) {
            if (finalSnapshot.get(i).getUsername().equals(selectedItem)) {
                return Integer.toString(i + 1);
            }
        }
        return Integer.toString(-1);
    }

    /** Gets the user building where an user has build
     * @param selectedItem is the username
     * @return the string with all the emporia
     */
    public String getUserBuilding(String selectedItem) {
        String toReturn = "";
        for (BaseUser baseUser : finalSnapshot) {
            if (baseUser.getUsername().equals(selectedItem)) {
                for (int i = 0; i < baseUser.getUsersEmporium().size(); i++) {
                    if (toReturn == "") {
                        toReturn = baseUser.getUsersEmporium().get(i).getCityName().getCityName();
                    } else {
                        toReturn = toReturn.concat(", " + baseUser.getUsersEmporium().get(i).getCityName().getCityName());
                    }
                }
            }
        }
        return toReturn;
    }

    /** Returns the city where an user can build
     * @param username is the username chosen
     * @return the list of the city where an user can build
     */
    public ArrayList<String> populateListView(String username) {
        ArrayList<String> toReturn = new ArrayList<>();
        if (snapshot.getUsersInGame().get(username).getPermitCards().size() == 0)
            toReturn.add("");
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

    /** Bridge used to communicate view that an user has disconnected
     * @param username is the username of the user
     */
    public void onUserDisconnect(String username) {
        baseView.onUserDisconnect(username);
    }
}
