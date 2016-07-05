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

    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private User user;
    private final GamesManager gamesManager;
    private GameController gameController;

    public SocketCommunication(Socket socket) throws IOException {
        this.socket = socket;
        //Open the buffers
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.gamesManager = GamesManager.getInstance();
    }

    @Override
    public void sendSnapshot(SnapshotToSend snapshotToSend) {
        CommunicationInfo.SendCommunicationInfo(this.out, Constants.CODE_SNAPSHOT, snapshotToSend);
    }

    @Override
    public void changeRound() {
        CommunicationInfo.SendCommunicationInfo(this.out, Constants.CODE_YOUR_TURN, null);
    }

    @Override
    public void sendAvailableMap(ArrayList<Map> availableMaps) {
        CommunicationInfo.SendCommunicationInfo(this.out, Constants.CODE_JSON_TEST, availableMaps);
    }

    /**
     * when user select a map
     *
     * @param snapshotToSend
     */
    @Override
    public void sendSelectedMap(SnapshotToSend snapshotToSend) {
        CommunicationInfo.SendCommunicationInfo(this.out, Constants.CODE_INITIALIZE_GAME, snapshotToSend);
    }

    @Override
    public void finishTurn() {
        CommunicationInfo.SendCommunicationInfo(this.out, Constants.CODE_TURN_FINISHED, null);
    }

    @Override
    public void sendStartMarket() {
        CommunicationInfo.SendCommunicationInfo(this.out, Constants.CODE_START_MARKET, null);

    }

    @Override
    public void sendStartBuyPhase() {
        CommunicationInfo.SendCommunicationInfo(this.out, Constants.CODE_START_BUY_PHASE, null);
    }

    @Override
    public void disableMarketPhase() {
        CommunicationInfo.SendCommunicationInfo(this.out, Constants.CODE_FINISH_MARKET_PHASE, null);
    }

    @Override
    public void selectPermitCard() {
        CommunicationInfo.SendCommunicationInfo(this.out, Constants.CODE_SELECT_PERMIT_CARD, null);

    }

    @Override
    public void selectCityRewardBonus(SnapshotToSend snapshotToSend) {
        CommunicationInfo.SendCommunicationInfo(this.out, Constants.SELECT_CITY_REWARD_BONUS, snapshotToSend);
    }

    @Override
    public void moveKing(ArrayList<City> kingPath) {
        CommunicationInfo.SendCommunicationInfo(this.out, Constants.MOVE_KING, kingPath);
    }

    @Override
    public void sendMatchFinishedWithWin(ArrayList<BaseUser> finalSnapshot) {
        CommunicationInfo.SendCommunicationInfo(this.out, Constants.CODE_FINISH, finalSnapshot);
    }

    @Override
    public void ping() {

    }

    @Override
    public void selectOldPermitCard() {
        CommunicationInfo.SendCommunicationInfo(this.out, Constants.CODE_OLD_PERMIT_CARD_BONUS, null);
    }

    @Override
    public void sendUserDisconnect(String username) {
        CommunicationInfo.SendCommunicationInfo(this.out, Constants.CODE_USER_DISCONNECT, username);
    }


    @Override
    public void run() {
        String line;
        Gson gson = new GsonBuilder().registerTypeAdapter(Action.class, new InterfaceAdapter<Action>())
                .registerTypeAdapter(Bonus.class, new InterfaceAdapter<Bonus>())
                .excludeFieldsWithModifiers(Modifier.TRANSIENT)
                .create();
        System.out.println("Socket communication started");
        try {
            while ((line = this.in.readLine()) != null) {
                CommunicationInfo communicationInfo = CommunicationInfo.decodeCommunicationInfo(line);

                switch (communicationInfo.getCode()) {
                    case Constants.CODE_NAME:
                        String username = gson.fromJson(communicationInfo.getInfo(), String.class);
                        if (!this.gamesManager.userAlreadyPresent(username)) {
                            user.setUsername(username);
                            this.gamesManager.addToGame(this.user);
                            CommunicationInfo.SendCommunicationInfo(this.out, Constants.CODE_NAME, true);
                        } else {
                            CommunicationInfo.SendCommunicationInfo(this.out, Constants.CODE_NAME, false);
                        }
                        break;
                    case Constants.CODE_CHAT:
                        String message = gson.fromJson(communicationInfo.getInfo(), String.class);
                        System.out.println(message);
                        // user.OnMessage(message);
                        break;
                    case Constants.CODE_ACTION:
                        Action action = CommunicationInfo.getAction(communicationInfo.getInfo());
                        try {
                            this.user.getGame().getGameController().doAction(action, this.user);
                        } catch (ActionNotPossibleException e) {
                            // send exception to client
                            CommunicationInfo.SendCommunicationInfo(this.out, Constants.CODE_EXCEPTION, e.getMessage());
                        }
                        break;
                    case Constants.CODE_MAP:
                        Map map = gson.fromJson(communicationInfo.getInfo(), Map.class);
                        this.user.getGame().getGameController().setMap(map);
                        System.out.println("Map arrived in socket communication");
                        break;
                    case Constants.CODE_MARKET_SELL: {
                        ArrayList<BuyableWrapper> buyableWrappers = CommunicationInfo.getBuyableArray(communicationInfo.getInfo());
                        CommunicationInfo.SendCommunicationInfo(this.out, Constants.CODE_MARKET_SELL, this.user.getGame().getGameController().onReceiveBuyableObject(buyableWrappers));
                        break;
                    }

                    case Constants.CODE_MARKET_BUY:
                        ArrayList<BuyableWrapper> buyableWrappers = CommunicationInfo.getBuyableArray(communicationInfo.getInfo());
                        CommunicationInfo.SendCommunicationInfo(this.out, Constants.CODE_MARKET_BUY, this.user.getGame().getGameController().onBuyObject(this.user, buyableWrappers));
                        break;

                    case Constants.CODE_MARKET_REMOVE:
                        BuyableWrapper buyableWrapper = CommunicationInfo.getBuyableWrapper(communicationInfo.getInfo());
                        this.user.getGame().getGameController().onRemoveItem(buyableWrapper);
                        break;

                    case Constants.CODE_FINISH_SELL_PHASE:
                        this.user.getGameController().onFinishSellPhase(this.user);
                        break;

                    case Constants.CODE_FINISH_BUY_PHASE:
                        this.user.getGameController().onFinishBuyPhase(this.user);
                        break;
                    case Constants.CODE_CITY_REWARD_BONUS:
                        City city = gson.fromJson(communicationInfo.getInfo(), City.class);
                        try {
                            this.user.getGameController().getCityRewardBonus(city, this.user);
                        } catch (ActionNotPossibleException e) {
                            CommunicationInfo.SendCommunicationInfo(this.out, Constants.CODE_EXCEPTION, e.getMessage());
                        }
                        break;
                    case Constants.SELECT_PERMITCARD_BONUS: {
                        PermitCard permitCard = gson.fromJson(communicationInfo.getInfo(), PermitCard.class);
                        this.user.getGameController().onSelectPermitCard(permitCard, this.user);
                        break;
                    }
                    case Constants.CODE_FINISH_TURN:
                        this.user.getGameController().onUserPass(this.user);
                        break;
                    case Constants.CODE_OLD_PERMIT_CARD_BONUS:
                        PermitCard permitCard = gson.fromJson(communicationInfo.getInfo(), PermitCard.class);
                        this.user.getGameController().onSelectOldPermitCard(this.user, permitCard);
                        break;
                }
            }
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("User Disconnected: " + this.user.getUsername());
            this.user.setConnected(false);
            this.user.getGameController().onUserDisconnected(this.user);
            this.user.getGameController().cleanGame();
        }
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }
}
