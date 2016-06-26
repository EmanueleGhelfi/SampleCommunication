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
    private int port = Constants.SOCKET_PORT;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private ClientController clientController;
    private ExecutorService executorService;

    ClientSocketService(String serverIP, ClientController clientController){
        hostname = serverIP;
        this.clientController = clientController;
        executorService = Executors.newCachedThreadPool();
    }

    @Override
    public boolean Connect() {
        try {
            socket = new Socket(hostname,port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
        CommunicationInfo.SendCommunicationInfo(out, Constants.CODE_NAME,name);
    }


    @Override
    public void onAction(Action action) {
        CommunicationInfo.SendCommunicationInfo(out, Constants.CODE_ACTION, action);
    }

    @Override
    public void sendMap(Map map) {
        CommunicationInfo.SendCommunicationInfo(out, Constants.CODE_MAP, map);
    }

    @Override
    public void sendSaleItem(ArrayList<BuyableWrapper> realSaleList) {
        CommunicationInfo.SendCommunicationInfo(out,Constants.CODE_MARKET_SELL,realSaleList);
    }

    @Override
    public void onBuy(ArrayList<BuyableWrapper> buyList) {
        CommunicationInfo.SendCommunicationInfo(out,Constants.CODE_MARKET_BUY,buyList);
    }

    @Override
    public void onRemoveItemFromMarket(BuyableWrapper item) {
        CommunicationInfo.SendCommunicationInfo(out,Constants.CODE_MARKET_REMOVE,item);
    }

    @Override
    public void onFinishSellPhase() {
        CommunicationInfo.SendCommunicationInfo(out,Constants.CODE_FINISH_SELL_PHASE,null);
    }

    @Override
    public void sendFinishedBuyPhase() {
        CommunicationInfo.SendCommunicationInfo(out,Constants.CODE_FINISH_BUY_PHASE,null);

    }

    @Override
    public void getCityRewardBonus(City city1) {
        CommunicationInfo.SendCommunicationInfo(out,Constants.CODE_CITY_REWARD_BONUS,city1);
    }

    @Override
    public void onSelectPermitCard(PermitCard permitCard) {
        CommunicationInfo.SendCommunicationInfo(out,Constants.SELECT_PERMITCARD_BONUS,permitCard);
    }

    @Override
    public void onFinishTurn() {
        CommunicationInfo.SendCommunicationInfo(out,Constants.CODE_FINISH_TURN,null);
    }

    @Override
    public void onSelectOldPermitCard(PermitCard permitCard) {
        CommunicationInfo.SendCommunicationInfo(out,Constants.CODE_OLD_PERMIT_CARD_BONUS,permitCard);
    }

    @Override
    public void run() {
        System.out.println("ClientSocketService Started");
        String line;
        try {
            while ((line = in.readLine())!=null){
                // create a new runnable and use a executor service in order to execute this task
                class DecoderTask implements Runnable{
                    private String lineToDecode;
                    private DecoderTask(String line) {
                        this.lineToDecode = line;
                    }
                    @Override
                    public void run() {
                        decodeInfo(lineToDecode);
                    }
                }
                DecoderTask decoderTask = new DecoderTask(line);
                executorService.execute(decoderTask);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void decodeInfo(String line){
        Gson gson = new GsonBuilder().registerTypeAdapter(Action.class, new InterfaceAdapter<Action>())
                .registerTypeAdapter(Bonus.class,new InterfaceAdapter<Bonus>())
                .excludeFieldsWithModifiers(Modifier.TRANSIENT)
                .create();
        CommunicationInfo communicationInfo = CommunicationInfo.decodeCommunicationInfo(line);
        switch (communicationInfo.getCode()){
            case Constants.CODE_NAME:{
                boolean result =  gson.fromJson(communicationInfo.getInfo(),boolean.class);
                clientController.onNameReceived(result);
                break;
            }
            case Constants.CODE_CHAT:{
                String message = gson.fromJson(communicationInfo.getInfo(),String.class);
                System.out.println(message);
                break;
            }
            case Constants.CODE_SNAPSHOT:{
                SnapshotToSend snapshotToSend = CommunicationInfo.getSnapshot(communicationInfo.getInfo());
                //SnapshotToSend snapshotToSend = gson.fromJson(communicationInfo.getInfo(),SnapshotToSend.class);
                clientController.setSnapshot(snapshotToSend);
                break;
            }
            case Constants.CODE_JSON_TEST:{
                ArrayList<Map> mapArrayList = gson.fromJson(communicationInfo.getInfo(), new TypeToken<ArrayList<Map>>(){}.getType());
                System.out.println("EILA");
                clientController.showMap(mapArrayList);
                break;
            }
            case Constants.CODE_INITIALIZE_GAME:{
                SnapshotToSend snapshotToSend = CommunicationInfo.getSnapshot(communicationInfo.getInfo());
                clientController.gameInitialization(snapshotToSend);
                break;
            }
            case Constants.CODE_YOUR_TURN:{
                clientController.isMyTurn();
                break;
            }
            case Constants.CODE_TURN_FINISHED:{
                clientController.turnFinished();
                break;
            }
            case Constants.CODE_START_MARKET:{
                clientController.onStartMarket();
                break;
            }
            case Constants.CODE_START_BUY_PHASE:{
                clientController.onStartBuyPhase();
                break;
            }
            case Constants.CODE_FINISH_MARKET_PHASE:{
                clientController.onFinishBuyPhase();
                break;
            }
            case Constants.SELECT_CITY_REWARD_BONUS:{
                clientController.selectCityRewardBonus(CommunicationInfo.getSnapshot(communicationInfo.getInfo()));
                break;
            }
            case Constants.CODE_SELECT_PERMIT_CARD:{
                clientController.selectPermitCard();
                break;
            }
            case Constants.MOVE_KING:{
                ArrayList<City> kingPath = new ArrayList<>();
                Type listType = new TypeToken<ArrayList<City>>(){}.getType();
                kingPath = gson.fromJson(communicationInfo.getInfo(),listType);
                clientController.onMoveKing(kingPath);
                break;
            }
            case Constants.CODE_EXCEPTION:{
                ActionNotPossibleException actionNotPossibleException = new ActionNotPossibleException(communicationInfo.getInfo());
                clientController.onActionNotPossible(actionNotPossibleException);
                break;
            }
            case Constants.CODE_FINISH:{
                ArrayList<BaseUser> finalSnapshot = new ArrayList<>();
                Type listType = new TypeToken<ArrayList<BaseUser>>(){}.getType();
                finalSnapshot = gson.fromJson(communicationInfo.getInfo(),listType);
                clientController.sendMatchFinishedWithWin(finalSnapshot);
                break;
            }
            case Constants.CODE_OLD_PERMIT_CARD_BONUS:{
                clientController.selectOldPermitCardBonus();
                break;
            }
        }
    }
}
