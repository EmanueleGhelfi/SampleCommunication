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

/**
 * Created by Emanuele on 09/05/2016.
 */

/**
 * This class manage event and transaction with server
 */
public class ClientController {

    private static ClientController clientController;
    private ClientService clientService;
    private BufferedReader inKeyboard;
    private BaseView baseView;
    private SnapshotToSend snapshot;
    private ArrayList<BaseUser> finalSnapshot = new ArrayList<>();

    private ClientController() {
    }

    public static ClientController getInstance() {
        if (ClientController.clientController == null)
            ClientController.clientController = new ClientController();
        return ClientController.clientController;
    }

    public void init() {
        try {
            String networkMethod;
            String uiMethod;
            this.inKeyboard = new BufferedReader(new InputStreamReader(System.in));
            networkMethod = this.getChoiceConnection(this.inKeyboard);
            String serverIP = this.getServerIP(this.inKeyboard);
            this.clientService = ClientFactoryService.getService(networkMethod, serverIP, this);
            if (this.clientService.Connect()) {
                uiMethod = this.getUIMethod(this.inKeyboard);
                this.baseView = FactoryView.getBaseView(uiMethod, this);
                this.baseView.initView();
            } else {
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

    private String getUIMethod(BufferedReader inKeyboard) {
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

    private void ReadName() {
        this.baseView.showLoginError();
    }

    public String getChoiceConnection(BufferedReader inKeyboard) throws IOException {
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

    public String getServerIP(BufferedReader inKeyboard) throws IOException {
        System.out.println("Inserisci IP GamesManager");
        String scelta = inKeyboard.readLine();
        return scelta;
    }

    /**
     * Called when the name is accepted
     *
     * @param result
     */
    public void onNameReceived(boolean result) {
        try {
            if (!result) {
                this.ReadName();
            } else {
                this.baseView.showWaitingForStart();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onSendLogin(String userName) {
        this.clientService.sendName(userName);
    }

    public void showMap(ArrayList<Map> mapArrayList) {
        this.baseView.showMap(mapArrayList);
    }

    public void sendMap(Map map) {
        this.clientService.sendMap(map);
    }

    public void gameInitialization(SnapshotToSend snapshotToSend) {
        this.snapshot = snapshotToSend;
        this.baseView.gameInitialization(snapshotToSend);
    }

    public void isMyTurn() {
        this.baseView.isMyTurn(this.snapshot);
    }

    public void mainActionElectCouncilor(Councilor councilor, King king, RegionName regionName) {
        Action action = new MainActionElectCouncilor(councilor, king, regionName);
        try {
            this.clientService.onAction(action);
        } catch (ActionNotPossibleException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void turnFinished() {
        this.baseView.turnFinished();
    }

    public void fastActionElectCouncilorWithHelper(Councilor councilor, King king, RegionName region, String councilType) {
        Action action = new FastActionElectCouncilorWithHelper(region, king, councilor, councilType);
        try {
            this.clientService.onAction(action);
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
        return this.snapshot;
    }

    public void setSnapshot(SnapshotToSend snapshot) {
        this.snapshot = snapshot;
        this.baseView.updateSnapshot();
    }

    public void mainActionBuyPermitCard(PermitCard permitCard, ArrayList<PoliticCard> politicCards) {
        Action action = new MainActionBuyPermitCard(politicCards, permitCard.getRetroType(), permitCard);
        try {
            this.clientService.onAction(action);
        } catch (ActionNotPossibleException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * called when doing a generic action
     *
     * @param action
     */
    public void doAction(Action action) {
        try {
            this.clientService.onAction(action);
            this.baseView.onActionDone(action);
        } catch (ActionNotPossibleException e) {
        } catch (RemoteException e) {
        }
    }

    public void sendSaleItem(ArrayList<BuyableWrapper> realSaleList) {
        this.clientService.sendSaleItem(realSaleList);
    }

    public void onBuy(ArrayList<BuyableWrapper> buyList) {
        this.clientService.onBuy(buyList);
    }

    public void removeItemFromMarket(BuyableWrapper item) {
        this.clientService.onRemoveItemFromMarket(item);
    }

    public void onStartMarket() {
        this.baseView.onStartMarket();
    }

    public void sendFinishSellPhase() {
        this.clientService.onFinishSellPhase();
    }

    public void onStartBuyPhase() {
        this.baseView.onStartBuyPhase();
    }

    public void sendFinishedBuyPhase() {
        this.clientService.sendFinishedBuyPhase();
    }

    public void onFinishBuyPhase() {
        this.baseView.onFinishMarket();
    }

    public void selectPermitCard() {
        this.baseView.selectPermitCard();
    }

    public void selectCityRewardBonus(SnapshotToSend snapshotToSend) {
        snapshot = snapshotToSend;
        this.baseView.selectCityRewardBonus();
    }

    public void getCityRewardBonus(City city1) {
        this.clientService.getCityRewardBonus(city1);
    }

    public void onSelectPermitCard(PermitCard permitCard) {
        this.clientService.onSelectPermitCard(permitCard);
    }

    public void onMoveKing(ArrayList<City> kingPath) {
        this.baseView.onMoveKing(kingPath);
    }

    public void onActionNotPossible(ActionNotPossibleException e) {
        this.baseView.onActionNotPossibleException(e);
    }

    public void onFinishTurn() {
        this.clientService.onFinishTurn();
    }

    public void sendMatchFinishedWithWin(ArrayList<BaseUser> finalSnapshot) {
        this.finalSnapshot = finalSnapshot;
        this.baseView.sendMatchFinishedWithWin();
    }

    public void selectOldPermitCardBonus() {
        this.baseView.selectOldPermitCardBonus();
    }

    public void onSelectOldPermitCard(PermitCard permitCard) {
        this.clientService.onSelectOldPermitCard(permitCard);
    }

    public ArrayList<BaseUser> getFinalSnapshot() {
        return this.finalSnapshot;
    }

    public boolean amIAWinner() {
        return this.snapshot.getCurrentUser().getUsername().equals(this.finalSnapshot.get(0).getUsername());
    }

    public BaseUser getUserWithString(String selectedItem) {
        for (BaseUser baseUser : this.finalSnapshot) {
            if (baseUser.getUsername().equals(selectedItem))
                return baseUser;
        }
        return null;
    }

    public String getUserPosition(String selectedItem) {
        for (int i = 0; i < this.finalSnapshot.size(); i++) {
            if (this.finalSnapshot.get(i).getUsername().equals(selectedItem)) {
                return Integer.toString(i + 1);
            }
        }
        return Integer.toString(-1);
    }

    public String getUserBuilding(String selectedItem) {
        String toReturn = "";
        for (BaseUser baseUser : this.finalSnapshot) {
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

    public ArrayList<String> populateListView(String username) {
        ArrayList<String> toReturn = new ArrayList<>();
        if (this.snapshot.getUsersInGame().get(username).getPermitCards().size() == 0)
            toReturn.add("");
        else {
            for (PermitCard permitCard : this.snapshot.getUsersInGame().get(username).getPermitCards()) {
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

    public void onUserDisconnect(String username) {
        this.baseView.onUserDisconnect(username);
    }
}
