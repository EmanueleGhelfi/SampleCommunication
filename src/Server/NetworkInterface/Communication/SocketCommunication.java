package Server.NetworkInterface.Communication;

import CommonModel.GameModel.Action.Action;
import CommonModel.GameModel.Bonus.Generic.Bonus;
import CommonModel.GameModel.City.Color;
import CommonModel.GameModel.Market.BuyableWrapper;
import CommonModel.Snapshot.SnapshotToSend;
import Server.Model.Map;
import Utilities.Class.CommunicationInfo;
import Utilities.Class.Constants;
import Server.Controller.GameController;
import Server.Controller.GamesManager;
import Server.Model.User;
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

/**
 * Created by Emanuele on 09/05/2016.
 */
public class SocketCommunication extends BaseCommunication implements Runnable {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private User user;
    private GamesManager gamesManager;
    private GameController gameController;

    public SocketCommunication(Socket socket) throws IOException {
        this.socket = socket;
        //Open the buffers
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        gamesManager = GamesManager.getInstance();
    }

    @Override
    public void sendSnapshot(SnapshotToSend snapshotToSend) {
        CommunicationInfo.SendCommunicationInfo(out,Constants.CODE_SNAPSHOT,snapshotToSend);
    }

    @Override
    public void changeRound() {
        CommunicationInfo.SendCommunicationInfo(out, Constants.CODE_YOUR_TURN, null);
    }

    @Override
    public void sendAvailableMap(ArrayList<Map> availableMaps) {
        CommunicationInfo.SendCommunicationInfo(out, Constants.CODE_JSON_TEST,availableMaps);
    }

    /**
     * when user select a map
     * @param snapshotToSend
     */
    @Override
    public void sendSelectedMap(SnapshotToSend snapshotToSend) {
        CommunicationInfo.SendCommunicationInfo(out,Constants.CODE_INITIALIZE_GAME,snapshotToSend);
    }

    @Override
    public void finishTurn() {
        CommunicationInfo.SendCommunicationInfo(out,Constants.CODE_TURN_FINISHED,null);
    }

    @Override
    public void sendStartMarket() {
        CommunicationInfo.SendCommunicationInfo(out,Constants.CODE_START_MARKET,null);

    }

    @Override
    public void sendStartBuyPhase() {
        CommunicationInfo.SendCommunicationInfo(out,Constants.CODE_START_BUY_PHASE,null);
    }

    @Override
    public void disableMarketPhase() {
        CommunicationInfo.SendCommunicationInfo(out,Constants.CODE_FINISH_MARKET_PHASE,null);
    }

    @Override
    public void selectPermitCard() {

    }

    @Override
    public void selectCityRewardBonus() {
        CommunicationInfo.SendCommunicationInfo(out,Constants.SELECT_CITY_REWARD_BONUS,null);
    }

    @Override
    public void run() {
        String line;
        Gson gson = new GsonBuilder().registerTypeAdapter(Action.class, new InterfaceAdapter<Action>())
                .registerTypeAdapter(Bonus.class,new InterfaceAdapter<Bonus>())
                .excludeFieldsWithModifiers(Modifier.TRANSIENT)
                .create();
        System.out.println("Socket communication started");
        try {
            while ((line = in.readLine()) != null) {
                CommunicationInfo communicationInfo = CommunicationInfo.decodeCommunicationInfo(line);

                switch (communicationInfo.getCode()) {
                    case Constants.CODE_NAME: {
                        String username = gson.fromJson(communicationInfo.getInfo(),String.class);
                        if(!gamesManager.userAlreadyPresent(username)) {
                            this.user.setUsername(username);
                            gamesManager.addToGame(user);
                            CommunicationInfo.SendCommunicationInfo(out, Constants.CODE_NAME, true);
                        }
                        else{
                            CommunicationInfo.SendCommunicationInfo(out, Constants.CODE_NAME, false);
                        }
                        break;
                    }
                    case Constants.CODE_CHAT: {
                        String message = gson.fromJson(communicationInfo.getInfo(), String.class);
                        System.out.println(message);
                       // user.OnMessage(message);
                        break;
                    }
                    case Constants.CODE_ACTION:{
                        System.out.println("CODE ACTION"+communicationInfo.getInfo());
                        Action action = CommunicationInfo.getAction(communicationInfo.getInfo());
                        System.out.println("Received action from user: "+action);
                        try {
                            user.getGame().getGameController().doAction(action,user);
                        } catch (ActionNotPossibleException e) {
                            //printing exception
                            System.out.println(e);
                            //TODO: manage exception
                        }
                        break;
                    }
                    case Constants.CODE_MAP:{
                        Map map = gson.fromJson(communicationInfo.getInfo(),Map.class);
                        user.getGame().getGameController().setMap(map);
                        System.out.println("Map arrived in socket communication");
                        break;
                    }
                    case Constants.CODE_MARKET_SELL:{
                        ArrayList<BuyableWrapper> buyableWrappers = CommunicationInfo.getBuyableArray(communicationInfo.getInfo());
                        CommunicationInfo.SendCommunicationInfo(out,Constants.CODE_MARKET_SELL,user.getGame().getGameController().onReceiveBuyableObject(buyableWrappers));
                        break;
                    }

                    case Constants.CODE_MARKET_BUY:{
                        ArrayList<BuyableWrapper> buyableWrappers = CommunicationInfo.getBuyableArray(communicationInfo.getInfo());
                        CommunicationInfo.SendCommunicationInfo(out,Constants.CODE_MARKET_BUY,user.getGame().getGameController().onBuyObject(user,buyableWrappers));
                        break;
                    }

                    case Constants.CODE_MARKET_REMOVE:{
                        BuyableWrapper buyableWrapper = CommunicationInfo.getBuyableWrapper(communicationInfo.getInfo());
                        user.getGame().getGameController().onRemoveItem(buyableWrapper);
                        break;
                    }

                    case Constants.CODE_FINISH_SELL_PHASE:{
                        user.getGameController().onFinishSellPhase(user);
                        break;
                    }

                    case Constants.CODE_FINISH_BUY_PHASE:{
                        user.getGameController().onFinishBuyPhase(user);
                        break;
                    }
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
            user.setConnected(false);
        }
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }
}
