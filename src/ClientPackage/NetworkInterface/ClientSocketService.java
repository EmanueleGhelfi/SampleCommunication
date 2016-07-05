package ClientPackage.NetworkInterface;

import ClientPackage.Controller.ClientController;
import CommonModel.GameModel.Action.Action;
import CommonModel.GameModel.Bonus.Generic.Bonus;
import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.City.City;
import CommonModel.GameModel.Market.BuyableWrapper;
import CommonModel.Snapshot.BaseUser;
import CommonModel.Snapshot.SnapshotToSend;
import Server.Model.Map;
import Utilities.Class.CommunicationInfo;
import Utilities.Class.Constants;
import Utilities.Class.InterfaceAdapter;
import Utilities.Exception.ActionNotPossibleException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Emanuele on 09/05/2016.
 */
public class ClientSocketService extends ClientService implements Runnable {

    private String hostname = Constants.SOCKET_IP;
    private final int port = Constants.SOCKET_PORT;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private final ClientController clientController;
    private final ExecutorService executorService;

    ClientSocketService(String serverIP, ClientController clientController) {
        this.hostname = serverIP;
        this.clientController = clientController;
        this.executorService = Executors.newCachedThreadPool();
    }

    @Override
    public boolean Connect() {
        try {
            this.socket = new Socket(this.hostname, this.port);
            this.out = new PrintWriter(this.socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            Thread thread = new Thread(this);
            thread.start();
            System.out.println("Thread started");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void sendName(String name) {
        CommunicationInfo.SendCommunicationInfo(this.out, Constants.CODE_NAME, name);
    }


    @Override
    public void onAction(Action action) {
        CommunicationInfo.SendCommunicationInfo(this.out, Constants.CODE_ACTION, action);
    }

    @Override
    public void sendMap(Map map) {
        CommunicationInfo.SendCommunicationInfo(this.out, Constants.CODE_MAP, map);
    }

    @Override
    public void sendSaleItem(ArrayList<BuyableWrapper> realSaleList) {
        CommunicationInfo.SendCommunicationInfo(this.out, Constants.CODE_MARKET_SELL, realSaleList);
    }

    @Override
    public void onBuy(ArrayList<BuyableWrapper> buyList) {
        CommunicationInfo.SendCommunicationInfo(this.out, Constants.CODE_MARKET_BUY, buyList);
    }

    @Override
    public void onRemoveItemFromMarket(BuyableWrapper item) {
        CommunicationInfo.SendCommunicationInfo(this.out, Constants.CODE_MARKET_REMOVE, item);
    }

    @Override
    public void onFinishSellPhase() {
        CommunicationInfo.SendCommunicationInfo(this.out, Constants.CODE_FINISH_SELL_PHASE, null);
    }

    @Override
    public void sendFinishedBuyPhase() {
        CommunicationInfo.SendCommunicationInfo(this.out, Constants.CODE_FINISH_BUY_PHASE, null);

    }

    @Override
    public void getCityRewardBonus(City city1) {
        CommunicationInfo.SendCommunicationInfo(this.out, Constants.CODE_CITY_REWARD_BONUS, city1);
    }

    @Override
    public void onSelectPermitCard(PermitCard permitCard) {
        CommunicationInfo.SendCommunicationInfo(this.out, Constants.SELECT_PERMITCARD_BONUS, permitCard);
    }

    @Override
    public void onFinishTurn() {
        CommunicationInfo.SendCommunicationInfo(this.out, Constants.CODE_FINISH_TURN, null);
    }

    @Override
    public void onSelectOldPermitCard(PermitCard permitCard) {
        CommunicationInfo.SendCommunicationInfo(this.out, Constants.CODE_OLD_PERMIT_CARD_BONUS, permitCard);
    }

    @Override
    public void run() {
        System.out.println("ClientSocketService Started");
        String line;
        try {
            while ((line = this.in.readLine()) != null) {
                // create a new runnable and use a executor service in order to execute this task
                class DecoderTask implements Runnable {
                    private final String lineToDecode;

                    private DecoderTask(String line) {
                        lineToDecode = line;
                    }

                    @Override
                    public void run() {
                        ClientSocketService.this.decodeInfo(this.lineToDecode);
                    }
                }
                DecoderTask decoderTask = new DecoderTask(line);
                this.executorService.execute(decoderTask);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void decodeInfo(String line) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Action.class, new InterfaceAdapter<Action>())
                .registerTypeAdapter(Bonus.class, new InterfaceAdapter<Bonus>())
                .excludeFieldsWithModifiers(Modifier.TRANSIENT)
                .create();
        CommunicationInfo communicationInfo = CommunicationInfo.decodeCommunicationInfo(line);
        switch (communicationInfo.getCode()) {
            case Constants.CODE_NAME:
                boolean result = gson.fromJson(communicationInfo.getInfo(), boolean.class);
                this.clientController.onNameReceived(result);
                break;
            case Constants.CODE_CHAT:
                String message = gson.fromJson(communicationInfo.getInfo(), String.class);
                System.out.println(message);
                break;
            case Constants.CODE_SNAPSHOT: {
                SnapshotToSend snapshotToSend = CommunicationInfo.getSnapshot(communicationInfo.getInfo());
                this.clientController.setSnapshot(snapshotToSend);
                break;
            }
            case Constants.CODE_JSON_TEST:
                ArrayList<Map> mapArrayList = gson.fromJson(communicationInfo.getInfo(), new TypeToken<ArrayList<Map>>() {
                }.getType());
                this.clientController.showMap(mapArrayList);
                break;
            case Constants.CODE_INITIALIZE_GAME:
                SnapshotToSend snapshotToSend = CommunicationInfo.getSnapshot(communicationInfo.getInfo());
                this.clientController.gameInitialization(snapshotToSend);
                break;
            case Constants.CODE_YOUR_TURN:
                this.clientController.isMyTurn();
                break;
            case Constants.CODE_TURN_FINISHED:
                this.clientController.turnFinished();
                break;
            case Constants.CODE_START_MARKET:
                this.clientController.onStartMarket();
                break;
            case Constants.CODE_START_BUY_PHASE:
                this.clientController.onStartBuyPhase();
                break;
            case Constants.CODE_FINISH_MARKET_PHASE:
                this.clientController.onFinishBuyPhase();
                break;
            case Constants.SELECT_CITY_REWARD_BONUS:
                this.clientController.selectCityRewardBonus(CommunicationInfo.getSnapshot(communicationInfo.getInfo()));
                break;
            case Constants.CODE_SELECT_PERMIT_CARD:
                this.clientController.selectPermitCard();
                break;
            case Constants.MOVE_KING: {
                ArrayList<City> kingPath = new ArrayList<>();
                Type listType = new TypeToken<ArrayList<City>>() {
                }.getType();
                kingPath = gson.fromJson(communicationInfo.getInfo(), listType);
                this.clientController.onMoveKing(kingPath);
                break;
            }
            case Constants.CODE_EXCEPTION:
                ActionNotPossibleException actionNotPossibleException = new ActionNotPossibleException(gson.fromJson(communicationInfo.getInfo(), String.class));
                this.clientController.onActionNotPossible(actionNotPossibleException);
                break;
            case Constants.CODE_FINISH:
                ArrayList<BaseUser> finalSnapshot = new ArrayList<>();
                Type listType = new TypeToken<ArrayList<BaseUser>>() {
                }.getType();
                finalSnapshot = gson.fromJson(communicationInfo.getInfo(), listType);
                this.clientController.sendMatchFinishedWithWin(finalSnapshot);
                break;
            case Constants.CODE_OLD_PERMIT_CARD_BONUS:
                this.clientController.selectOldPermitCardBonus();
                break;

            case Constants.CODE_USER_DISCONNECT:
                String username = gson.fromJson(communicationInfo.getInfo(), String.class);
                this.clientController.onUserDisconnect(username);
                break;
        }
    }
}
