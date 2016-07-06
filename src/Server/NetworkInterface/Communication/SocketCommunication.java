package Server.NetworkInterface.Communication;

import CommonModel.GameModel.Action.Action;
import CommonModel.GameModel.Bonus.Generic.Bonus;
import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.City.City;
import CommonModel.GameModel.Market.BuyableWrapper;
import CommonModel.Snapshot.BaseUser;
import CommonModel.Snapshot.SnapshotToSend;
import Server.Controller.GameController;
import Server.Controller.GamesManager;
import Server.Model.Map;
import Server.Model.User;
import Utilities.Class.CommunicationInfo;
import Utilities.Class.Constants;
import Utilities.Class.InterfaceAdapter;
import Utilities.Class.InternalLog;
import Utilities.Exception.ActionNotPossibleException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Modifier;
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
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        CommunicationInfo.SendCommunicationInfo(out, Constants.CODE_SNAPSHOT, snapshotToSend);
    }

    @Override
    public void changeRound() {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        CommunicationInfo.SendCommunicationInfo(out, Constants.CODE_YOUR_TURN, null);
    }

    @Override
    public void sendAvailableMap(ArrayList<Map> availableMaps) {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        CommunicationInfo.SendCommunicationInfo(out, Constants.CODE_JSON_TEST, availableMaps);
    }

    /**
     * when user select a map
     *
     * @param snapshotToSend
     */
    @Override
    public void sendSelectedMap(SnapshotToSend snapshotToSend) {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        CommunicationInfo.SendCommunicationInfo(out, Constants.CODE_INITIALIZE_GAME, snapshotToSend);
    }

    @Override
    public void finishTurn() {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        CommunicationInfo.SendCommunicationInfo(out, Constants.CODE_TURN_FINISHED, null);
    }

    @Override
    public void sendStartMarket() {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        CommunicationInfo.SendCommunicationInfo(out, Constants.CODE_START_MARKET, null);

    }

    @Override
    public void sendStartBuyPhase() {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        CommunicationInfo.SendCommunicationInfo(out, Constants.CODE_START_BUY_PHASE, null);
    }

    @Override
    public void disableMarketPhase() {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        CommunicationInfo.SendCommunicationInfo(out, Constants.CODE_FINISH_MARKET_PHASE, null);
    }

    @Override
    public void selectPermitCard() {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        CommunicationInfo.SendCommunicationInfo(out, Constants.CODE_SELECT_PERMIT_CARD, null);

    }

    @Override
    public void selectCityRewardBonus(SnapshotToSend snapshotToSend) {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        CommunicationInfo.SendCommunicationInfo(out, Constants.CODE_SELECT_CITY_REWARD_BONUS, snapshotToSend);
    }

    @Override
    public void moveKing(ArrayList<City> kingPath) {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        CommunicationInfo.SendCommunicationInfo(out, Constants.CODE_MOVE_KING, kingPath);
    }

    @Override
    public void sendMatchFinishedWithWin(ArrayList<BaseUser> finalSnapshot) {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        CommunicationInfo.SendCommunicationInfo(out, Constants.CODE_FINISH, finalSnapshot);
    }

    @Override
    public void ping() {

    }

    @Override
    public void selectOldPermitCard() {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        CommunicationInfo.SendCommunicationInfo(out, Constants.CODE_OLD_PERMIT_CARD_BONUS, null);
    }

    @Override
    public void sendUserDisconnect(String username) {
        InternalLog.loggingSituation(this.getClass().getName(), new Object(){}.getClass().getEnclosingMethod().getName());
        CommunicationInfo.SendCommunicationInfo(out, Constants.CODE_USER_DISCONNECT, username);
    }


    @Override
    public void run() {
        String line;
        Gson gson = new GsonBuilder().registerTypeAdapter(Action.class, new InterfaceAdapter<Action>())
                .registerTypeAdapter(Bonus.class, new InterfaceAdapter<Bonus>())
                .excludeFieldsWithModifiers(Modifier.TRANSIENT)
                .create();
        try {
            while ((line = in.readLine()) != null) {
                CommunicationInfo communicationInfo = CommunicationInfo.decodeCommunicationInfo(line);
                System.out.println("CODE " + communicationInfo.getCode());
                switch (communicationInfo.getCode()) {
                    case Constants.CODE_NAME: {
                        String username = gson.fromJson(communicationInfo.getInfo(), String.class);
                        if (!gamesManager.userAlreadyPresent(username)) {
                            this.user.setUsername(username);
                            gamesManager.addToGame(user);
                            CommunicationInfo.SendCommunicationInfo(out, Constants.CODE_NAME, true);
                        } else {
                            CommunicationInfo.SendCommunicationInfo(out, Constants.CODE_NAME, false);
                        }
                        break;
                    }
                    case Constants.CODE_CHAT: {
                        String message = gson.fromJson(communicationInfo.getInfo(), String.class);
                        break;
                    }
                    case Constants.CODE_ACTION: {
                        Action action = CommunicationInfo.getAction(communicationInfo.getInfo());
                        try {
                            user.getGame().getGameController().doAction(action, user);
                        } catch (ActionNotPossibleException e) {
                            // send exception to client
                            CommunicationInfo.SendCommunicationInfo(out, Constants.CODE_EXCEPTION, e.getMessage());
                        }
                        break;
                    }
                    case Constants.CODE_MAP: {
                        Map map = gson.fromJson(communicationInfo.getInfo(), Map.class);
                        user.getGame().getGameController().setMap(map);
                        break;
                    }
                    case Constants.CODE_MARKET_SELL: {
                        ArrayList<BuyableWrapper> buyableWrappers = CommunicationInfo.getBuyableArray(communicationInfo.getInfo());
                        CommunicationInfo.SendCommunicationInfo(out, Constants.CODE_MARKET_SELL, user.getGame().getGameController().onReceiveBuyableObject(buyableWrappers));
                        break;
                    }

                    case Constants.CODE_MARKET_BUY: {
                        ArrayList<BuyableWrapper> buyableWrappers = CommunicationInfo.getBuyableArray(communicationInfo.getInfo());
                        CommunicationInfo.SendCommunicationInfo(out, Constants.CODE_MARKET_BUY, user.getGame().getGameController().onBuyObject(user, buyableWrappers));
                        break;
                    }

                    case Constants.CODE_MARKET_REMOVE: {
                        BuyableWrapper buyableWrapper = CommunicationInfo.getBuyableWrapper(communicationInfo.getInfo());
                        user.getGame().getGameController().onRemoveItem(buyableWrapper);
                        break;
                    }

                    case Constants.CODE_FINISH_SELL_PHASE: {
                        user.getGameController().onFinishSellPhase(user);
                        break;
                    }

                    case Constants.CODE_FINISH_BUY_PHASE: {
                        user.getGameController().onFinishBuyPhase(user);
                        break;
                    }
                    case Constants.CODE_CITY_REWARD_BONUS: {
                        City city = gson.fromJson(communicationInfo.getInfo(), City.class);
                        try {
                            user.getGameController().getCityRewardBonus(city, user);
                        } catch (ActionNotPossibleException e) {
                            CommunicationInfo.SendCommunicationInfo(out, Constants.CODE_EXCEPTION, e.getMessage());
                        }
                        break;
                    }
                    case Constants.CODE_SELECT_PERMITCARD_BONUS: {
                        PermitCard permitCard = gson.fromJson(communicationInfo.getInfo(), PermitCard.class);
                        user.getGameController().onSelectPermitCard(permitCard, user);
                        break;
                    }
                    case Constants.CODE_FINISH_TURN: {
                        user.getGameController().onUserPass(user);
                        break;
                    }
                    case Constants.CODE_OLD_PERMIT_CARD_BONUS: {
                        PermitCard permitCard = gson.fromJson(communicationInfo.getInfo(), PermitCard.class);
                        user.getGameController().onSelectOldPermitCard(user, permitCard);
                        break;
                    }
                }
            }
        } catch (IOException e) {
            user.setConnected(false);
            user.getGameController().onUserDisconnected(user);
            user.getGameController().cleanGame();
        }
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }
}
