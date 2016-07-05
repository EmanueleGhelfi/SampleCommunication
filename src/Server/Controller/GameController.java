package Server.Controller;

import CommonModel.GameModel.Action.Action;
import CommonModel.GameModel.Bonus.Generic.Bonus;
import CommonModel.GameModel.Bonus.SingleBonus.NobilityBonus;
import CommonModel.GameModel.Card.Deck.PermitDeck;
import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import CommonModel.GameModel.City.City;
import CommonModel.GameModel.City.Color;
import CommonModel.GameModel.City.RegionName;
import CommonModel.GameModel.Council.Helper;
import CommonModel.GameModel.Council.King;
import CommonModel.GameModel.Market.BuyableWrapper;
import CommonModel.Snapshot.BaseUser;
import CommonModel.Snapshot.SnapshotToSend;
import CommonModel.Snapshot.UserColor;
import Server.Model.FakeUser;
import Server.Model.Game;
import Server.Model.Map;
import Server.Model.User;
import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;
import Utilities.Exception.AlreadyPresentException;
import Utilities.Exception.MapsNotFoundException;

import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;

/**
 * Created by Emanuele on 13/05/2016.
 */
public class GameController implements Serializable {

    private Game game;
    private TimerTask timerTask;
    private Timer timer;
    private final int duration = Constants.GAME_TIMEOUT;
    private ArrayList<Map> availableMaps = new ArrayList<>();
    // initialized to users size, when 0 start market
    private int turnCounter;
    private final HashMap<User, Boolean> marketHashMap = new HashMap<>();
    private ArrayList<User> users = new ArrayList<>();
    private boolean sellPhase;
    private boolean buyPhase;
    private int nextUser;
    private int lastUser = -1;
    private Timer roundTimer = new Timer();
    private UserColor[] userColorSet;
    private FakeUser fakeUser;


    public GameController() {
    }

    public GameController(Game game) {
        this.game = game;
        timer = new Timer();
        try {
            this.availableMaps = Map.readAllMap();
        } catch (MapsNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void startTimer() {
        this.timerTask = new TimerTask() {
            @Override
            public void run() {
                GameController.this.notifyStarted();
            }
        };
    }

    /**
     * Called when init game
     */
    public void notifyStarted() {
        if (this.game.getUsers().size() == 2) {
            this.creatingFakeUser();
            //configurationForTwoPlayers();
        }
        this.users = new ArrayList<>(this.game.getUsers());
        this.game.setStarted(true);
        this.setDefaultStuff();
        // send map to first user
        for (User user : this.users) {
            if (user.isConnected()) {
                this.sendAvailableMap(user);
                break;
            }
        }

        this.startConnectedTimer();

    }

    private void creatingFakeUser() {
        this.fakeUser = new FakeUser();
        this.game.getUsersInGame().put(this.fakeUser.getUsername(), this.fakeUser);
        this.fakeUser.setUsername("FakeUser");
    }

    private void configurationForTwoPlayers() {
        ArrayList<PermitCard> permitCardArray = new ArrayList<>();
        for (Entry<RegionName, PermitDeck> permitDeck : this.game.getPermitDecks().entrySet()) {
            permitCardArray.add(permitDeck.getValue().getAndRemoveRandomPermitCard());
        }
        for (PermitCard permitCard : permitCardArray) {
            for (Character character : permitCard.getCityAcronimous()) {
                for (City city : this.game.getMap().getCity()) {
                    if (city.getCityName().getCityName().startsWith(character.toString().toUpperCase()) && !this.fakeUser.getUsersEmporium().contains(city))
                        this.fakeUser.addEmporium(city);
                }
            }
        }

    }

    private void setDefaultStuff() {
        int userCounter = 0;
        for (User user : this.users) {
            this.userColorSet = UserColor.values();
            user.setUserColor(this.colorAvailable());
            if (!(user instanceof FakeUser)) {
                user.setHelpers(Constants.DEFAULT_HELPER_COUNTER + userCounter);
                user.setCoinPathPosition(Constants.FIRST_INITIAL_POSITION_ON_MONEY_PATH + userCounter);
                user.setNobilityPathPosition(this.game.getNobilityPath().getPosition()[Constants.INITIAL_POSITION_ON_NOBILITY_PATH]);
                user.setVictoryPathPosition(Constants.INITIAL_POSITION_ON_VICTORY_PATH);
                userCounter++;
            }
            ArrayList<PoliticCard> politicCardArrayList = new ArrayList<>();
            for (int cont = 0; cont < Constants.DEFAULT_POLITIC_CARD_HAND; cont++) {
                politicCardArrayList.add(this.game.getPoliticCards().drawACard());
            }
            user.setPoliticCards(politicCardArrayList);


        }
    }

    private UserColor colorAvailable() {
        ArrayList<UserColor> shuffledUserColor = new ArrayList<>(Arrays.asList(UserColor.values()));
        Collections.shuffle(shuffledUserColor);
        for (UserColor userColor : shuffledUserColor) {
            boolean found = false;
            for (User user : this.game.getUsersInGame().values()) {
                if (user.getUserColor() != null && user.getUserColor().equals(userColor)) {
                    found = true;
                }
            }
            if (!found) {
                return userColor;
            }
        }
        return null;
    }

    public void cancelTimeout() {
        this.timer.cancel();
    }

    public void setTimeout() {
        if (this.timer == null) {
            this.timer = new Timer();
        } else {
            this.timer.cancel();
            this.timerTask = new TimerTask() {
                @Override
                public void run() {
                    GameController.this.notifyStarted();
                }
            };
            this.timer = new Timer();
        }
        this.timer.schedule(this.timerTask, this.duration);
    }


    /**
     * create snapshot and change round
     *
     * @param user user that has finished round
     */
    public void onFinishRound(User user) {
        this.cancelTimer();
        this.turnCounter--;
        System.out.println("on finish round called");
        user.getBaseCommunication().finishTurn();

        for (int cont = 0; cont < this.users.size(); cont++) {
            if (user.equals(this.users.get(cont))) {
                this.nextUser = cont + 1;
                while (!this.users.get(this.nextUser % this.game.getUsers().size()).isConnected() && this.nextUser % this.game.getUsers().size() != cont) {
                    System.out.println("user not connected " + this.users.get(this.nextUser % this.game.getUsers().size()));
                    this.turnCounter--;
                    this.nextUser++;
                }
                if (this.lastUser != this.nextUser % this.game.getUsers().size()) {
                    if (this.nextUser % this.game.getUsers().size() == cont) {
                        //onAllUserDisconnected();
                        this.startMarket();
                    } else {
                        if (this.turnCounter <= 0) {
                            System.out.println("Starting market");
                            this.startMarket();
                        } else {
                            System.out.println("change round : " + this.nextUser);
                            this.changeRound(this.nextUser);
                        }
                    }
                } else {
                    this.checkUserWhoWin();
                }
            }
        }
    }

    private void cancelTimer() {
        if (this.roundTimer != null) {
            this.roundTimer.cancel();
        }
    }

    private void changeRound(int nextUser) {
        ArrayList<User> userArrayList = new ArrayList<>(this.game.getUsers());
        userArrayList.get(nextUser % this.game.getUsers().size()).setMainActionCounter(Constants.MAIN_ACTION_POSSIBLE);
        userArrayList.get(nextUser % this.game.getUsers().size()).setFastActionCounter(Constants.FAST_ACTION_POSSIBLE);
        userArrayList.get(nextUser % this.game.getUsers().size()).drawCard();
        userArrayList.get(nextUser % this.game.getUsers().size()).getBaseCommunication().changeRound();
        this.sendSnapshotToAll();
        this.startRoundTimer(userArrayList.get(nextUser % this.game.getUsers().size()));
    }

    private void startRoundTimer(User user) {
        this.roundTimer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                user.getBaseCommunication().ping();
                GameController.this.onUserPass(user);
            }
        };
        this.roundTimer.schedule(timerTask, Constants.ROUND_DURATION);
    }

    private void startMarket() {
        this.sellPhase = true;
        this.marketHashMap.clear();

        this.users.forEach(user -> {
            Runnable runnable = () -> {
                this.sendStartMarket(user);
            };
            new Thread(runnable).start();

        });


    }

    private void startConnectedTimer() {
        Timer checkUserTimer = new Timer();
        TimerTask checkUserTimerTask = new TimerTask() {
            @Override
            public void run() {
                for (Iterator<User> iterator = GameController.this.users.iterator(); iterator.hasNext(); ) {
                    try {
                        iterator.next().getBaseCommunication().ping();
                    } catch (Exception e) {
                        System.out.println("Exception");
                        // todo: check
                        break;
                    }
                }
            }
        };
        checkUserTimer.scheduleAtFixedRate(checkUserTimerTask, 0, 30000);
    }

    private void sendStartMarket(User user) {
        user.getBaseCommunication().sendStartMarket();
    }

    private synchronized void onAllUserDisconnected() {
        this.roundTimer.cancel();
        GamesManager.getInstance().cancelThisGame(this.game, this);
    }

    public void doAction(Action action, User user) throws ActionNotPossibleException {
        action.doAction(this.game, user);
    }

    /**
     * send available map to client
     *
     * @param user
     */
    private void sendAvailableMap(User user) {
        user.getBaseCommunication().sendAvailableMap(this.availableMaps);
    }

    /* set map and init game called by client*/
    public void setMap(Map map) {
        if (this.availableMaps.contains(map)) {
            Map mapToFind = this.findMap(map);
            this.game.setMap(mapToFind);
            this.game.setKing(new King(map.getCity().get(0), this.game.getBank()));
            this.setKingPosition(this.game, mapToFind);
            for (User user : this.game.getUsers()) {
                SnapshotToSend snapshotToSend = new SnapshotToSend(this.game, user);
                // init game
                user.getBaseCommunication().sendSelectedMap(snapshotToSend);
            }
            this.sendFinishMarketToAll();


            //count true user
            long trueUser = this.game.getUsers().stream().filter(user -> !(user instanceof FakeUser)).count();

            if (trueUser == 2) {
                this.configurationForTwoPlayers();
            }

            this.selectFirstPlayer();


            //TODO: READD TEN EMPORIUMS

            /*
            for (User user :
                    users) {
                int cont=0;
                for (City city : game.getMap().getCity()) {
                    if (cont<9) {
                        if (!(user instanceof FakeUser)) {
                            user.addEmporium(city);
                            cont++;
                        }
                    }

                }
            }
            */

        } else {
            System.out.println("MAP NOT PRESENT");
        }
    }

    private void setKingPosition(Game game, Map mapToFind) {
        for (City city : mapToFind.getCity()) {
            if (city.getColor().equals(Color.PURPLE)) {
                game.setKing(new King(city, game.getBank()));
                break;
            }
        }
    }

    private Map findMap(Map map) {
        for (Map mapToSelect : this.availableMaps) {
            if (map.equals(mapToSelect)) {
                return mapToSelect;
            }
        }
        return null;
    }

    /**
     * disable market phase in all user
     */
    private synchronized void sendFinishMarketToAll() {
        new Thread(() -> {
            for (User user : this.users) {
                user.getBaseCommunication().disableMarketPhase();
            }
        }).start();

    }

    private void selectFirstPlayer() {

        ArrayList<User> users = new ArrayList<>(this.game.getUsers());
        this.turnCounter = users.size();
        // select first user

        for (User user : users) {
            if (user.isConnected()) {
                this.changeRound(users.indexOf(user));
                this.nextUser = users.indexOf(user);
                break;
            }
        }


        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).isConnected() && i != this.nextUser)
                users.get(i).getBaseCommunication().finishTurn();
        }

        this.sendSnapshotToAll();
    }

    public synchronized void sendSnapshotToAll() {
        new Thread(() -> {
            for (User user : this.game.getUsers()) {
                if (user.isConnected()) {
                    SnapshotToSend snapshotToSend = new SnapshotToSend(this.game, user);
                    user.getBaseCommunication().sendSnapshot(snapshotToSend);
                }
            }
        }).start();
    }

    // called by client when receive an object to sell
    public boolean onReceiveBuyableObject(ArrayList<BuyableWrapper> buyableWrappers) {
        for (BuyableWrapper buyableWrapper : buyableWrappers) {
            try {
                this.game.addBuyableWrapper(buyableWrapper);
            } catch (AlreadyPresentException e) {
                System.out.println("L'oggetto è già in vendita!");
            }
        }
        return true;

    }

    // called by client when receive object to buy
    public boolean onBuyObject(User user, ArrayList<BuyableWrapper> buyableWrappers) {
        int counter = 0;
        for (BuyableWrapper buyableWrapper : buyableWrappers) {
            try {
                this.game.getMoneyPath().goAhead(user, -buyableWrapper.getCost());
                this.game.getMoneyPath().goAhead(this.game.getUser(buyableWrapper.getUsername()), buyableWrapper.getCost());
                this.game.removeFromMarketList(buyableWrapper);
                if (buyableWrapper.getBuyableObject() instanceof PermitCard) {
                    this.game.getUser(buyableWrapper.getUsername()).removePermitCardDefinitevely((PermitCard) buyableWrapper.getBuyableObject());
                    user.addPermitCard((PermitCard) buyableWrapper.getBuyableObject());
                } else if (buyableWrapper.getBuyableObject() instanceof PoliticCard) {
                    this.game.getUser(buyableWrapper.getUsername()).removePoliticCard((PoliticCard) buyableWrapper.getBuyableObject());
                    user.addPoliticCard((PoliticCard) buyableWrapper.getBuyableObject());
                } else if (buyableWrapper.getBuyableObject() instanceof Helper) {
                    this.game.getUser(buyableWrapper.getUsername()).removeHelper((Helper) buyableWrapper.getBuyableObject());
                    user.addHelper();
                }
                counter++;
            } catch (ActionNotPossibleException e) {

            }
        }
        this.sendSnapshotToAll();

        return counter == buyableWrappers.size();
    }

    public synchronized void onRemoveItem(BuyableWrapper item) {
        this.game.removeFromMarketList(item);
        this.sendSnapshotToAll();
    }

    public void onFinishSellPhase(User user) {
        long finishedUser = 0;
        if (this.sellPhase) {
            this.marketHashMap.put(user, true);

            finishedUser = this.marketHashMap.entrySet().stream()
                    .filter(Entry::getValue)
                    .count();

            long connectedUser = this.users.stream()
                    .filter(BaseUser::isConnected)
                    .count();

            if (finishedUser >= connectedUser) {
                this.sellPhase = false;
                this.marketHashMap.clear();
                this.startBuyPhase();
            } else {
                System.out.println("No : " + finishedUser + " " + connectedUser);
            }
        }
    }

    private void startBuyPhase() {
        this.sendSnapshotToAll();
        this.buyPhase = true;
        this.selectRandomUser();
    }

    private void selectRandomUser() {
        Random random = new Random();
        int userNumber = 0;
        boolean found = false;
        while (!found) {
            userNumber = random.nextInt(this.users.size());
            if ((!this.marketHashMap.containsKey(this.users.get(userNumber)) || !this.marketHashMap.get(this.users.get(userNumber)))
                    && this.users.get(userNumber).isConnected() && !(this.users.get(userNumber) instanceof FakeUser)) {
                found = true;
            }
        }
        System.out.println("Sending start buy phase to " + this.users.get(userNumber));
        this.users.get(userNumber).getBaseCommunication().sendStartBuyPhase();
    }

    public void onFinishBuyPhase(User user) {
        if (this.buyPhase) {
            this.sendSnapshotToAll();
            this.marketHashMap.put(user, true);

            long finishedUser = this.marketHashMap.entrySet().stream()
                    .filter(Entry::getValue)
                    .count();

            long connectedUser = this.users.stream()
                    .filter(BaseUser::isConnected)
                    .count();

            if (finishedUser < connectedUser) {
                this.selectRandomUser();
            } else {
                this.marketHashMap.clear();
                this.buyPhase = false;
                this.sendFinishMarketToAll();
                this.turnCounter = this.users.size();
                this.changeRound(this.nextUser);
            }
        }
    }

    public void getCityRewardBonus(City city1, User user) throws ActionNotPossibleException {
        City city = this.game.getCity(city1);

        if (this.checkBonusCorrect(city, user)) {
            try {
                if (!city.getColor().getColor().equals(Constants.PURPLE))
                    city.getBonus(user, this.game);
            } catch (ActionNotPossibleException e) {

            }

            user.decrementOptionalActionCounter();

            this.sendSnapshotToAll();
        } else {
            user.getBaseCommunication().selectCityRewardBonus(new SnapshotToSend(this.game, user));
            throw new ActionNotPossibleException(Constants.CITY_REWARD_BONUS_INCORRECT);
        }

    }

    private boolean checkBonusCorrect(City city, User user) {
        ArrayList<Bonus> bonusArrayList = city.getBonus().getBonusArrayList();
        for (Bonus bonus : bonusArrayList) {
            if (bonus instanceof NobilityBonus) {
                return false;
            }
        }

        for (City city1 : user.getUsersEmporium()) {
            if (city1.equals(city)) {
                return true;
            }
        }
        return false;
    }

    public void onSelectPermitCard(PermitCard permitCard, User user) {
        PermitDeck permitDeck = this.game.getPermitDeck(permitCard.getRetroType());
        try {
            PermitCard permitCardTrue = permitDeck.getAndRemovePermitCardVisible(permitCard);
            permitCardTrue.getBonus().getBonus(user, this.game);
            user.addPermitCard(permitCardTrue);
            user.decrementOptionalActionCounter();
        } catch (ActionNotPossibleException e) {
            e.printStackTrace();
        }
        this.sendSnapshotToAll();
    }

    public void changeMasterUser() {
        // send map to first user
        for (User user : this.users) {
            if (user.isConnected()) {
                this.sendAvailableMap(user);
            }
        }
    }

    public void startingLastRound() {
        this.lastUser = this.nextUser % this.game.getUsers().size();
    }

    public void checkUserWhoWin() {
        ArrayList<User> firstNobilityPathUserToReward = new ArrayList<>(this.users);
        ArrayList<User> secondNobilityPathUserToReward = new ArrayList<>(this.users);
        ArrayList<User> userMaxPermitCard = new ArrayList<>(this.users);
        ArrayList<User> userMaxHelper = new ArrayList<>(this.users);
        User userToRewardMaxPermitCard = new User();
        User userWithMaxHelperAndPoliticCard = new User();

        firstNobilityPathUserToReward.remove(this.fakeUser);
        secondNobilityPathUserToReward.remove(this.fakeUser);
        userMaxPermitCard.remove(this.fakeUser);
        userMaxHelper.remove(this.fakeUser);
        this.users.remove(this.fakeUser);


        this.sortingOnNobiliy(firstNobilityPathUserToReward);
        this.sortingOnNobiliy(secondNobilityPathUserToReward);
        this.sortingOnPermit(userMaxPermitCard);
        this.sortingOnHelper(userMaxHelper);

        //for (User user : firstNobilityPathUserToReward) {
        for (Iterator<User> itr = firstNobilityPathUserToReward.iterator(); itr.hasNext(); ) {
            User userUsed = itr.next();
            if (firstNobilityPathUserToReward.get(0).getNobilityPathPosition().getPosition() > userUsed.getNobilityPathPosition().getPosition()) {
                itr.remove();
            }
        }

        secondNobilityPathUserToReward.removeAll(firstNobilityPathUserToReward);
        //for (User user : secondNobilityPathUserToReward) {
        for (Iterator<User> itr = secondNobilityPathUserToReward.iterator(); itr.hasNext(); ) {
            User userUsed = itr.next();
            if (secondNobilityPathUserToReward.get(0).getNobilityPathPosition().getPosition() > userUsed.getNobilityPathPosition().getPosition()) {
                itr.remove();
            }
        }

        //for (User user : secondNobilityPathUserToReward) {
        for (Iterator<User> itr = userMaxPermitCard.iterator(); itr.hasNext(); ) {
            User userUsed = itr.next();
            if (userMaxPermitCard.get(0).getPermitCards().size() > userUsed.getPermitCards().size()) {
                itr.remove();
            }
        }

        // for (User user : secondNobilityPathUserToReward) {
        for (Iterator<User> itr = secondNobilityPathUserToReward.iterator(); itr.hasNext(); ) {
            User userUsed = itr.next();
            if (userMaxHelper.get(0).getHelpers().size() + userMaxHelper.get(0).getPoliticCardSize() > userUsed.getHelpers().size() + userUsed.getPoliticCardSize()) {
                itr.remove();
            }
        }


        firstNobilityPathUserToReward.forEach(user -> user.setVictoryPathPosition(user.getVictoryPathPosition() + 5));
        secondNobilityPathUserToReward.forEach(user -> user.setVictoryPathPosition(user.getVictoryPathPosition() + 2));
        userToRewardMaxPermitCard.setVictoryPathPosition(userToRewardMaxPermitCard.getVictoryPathPosition() + 3);

        ArrayList<User> userWhoWin = new ArrayList<>(this.users);
        userWhoWin = this.checkFirst(userWhoWin);

        if (userWhoWin.size() > 1) {
            for (User user : this.users) {
                if (user.equals(userWithMaxHelperAndPoliticCard)) {
                    user.setVictoryPathPosition(user.getVictoryPathPosition() + 3);
                }
            }
            userWhoWin = this.checkFirst(userWhoWin);
        }

        this.sortingOnWin(this.users);

        ArrayList<BaseUser> finalSnapshot = new ArrayList<>();
        for (User userInArray : this.users) {
            finalSnapshot.add(new BaseUser(userInArray));
        }

        for (User user : this.users) {
            user.getBaseCommunication().sendMatchFinishedWithWin(finalSnapshot);
        }
    }

    private void sortingOnWin(ArrayList<User> arrayList) {
        Collections.sort(arrayList, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                if (o1.getVictoryPathPosition() > o2.getVictoryPathPosition())
                    return -1;
                if (o2.getVictoryPathPosition() > o1.getVictoryPathPosition())
                    return 1;
                else
                    return 0;
            }
        });
    }

    private void sortingOnHelper(ArrayList<User> arrayList) {
        Collections.sort(arrayList, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                if (o1.getHelpers().size() + o1.getPoliticCardSize() > o2.getHelpers().size() + o2.getPoliticCardSize())
                    return -1;
                if (o1.getHelpers().size() + o1.getPoliticCardSize() < o2.getHelpers().size() + o2.getPoliticCardSize())
                    return 1;
                else
                    return 0;
            }
        });
    }

    private void sortingOnPermit(ArrayList<User> arrayList) {
        Collections.sort(arrayList, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                if (o1.getPermitCards().size() > o2.getPermitCards().size())
                    return -1;
                if (o1.getPermitCards().size() < o2.getPermitCards().size())
                    return 1;
                else
                    return 0;
            }
        });
    }

    private void sortingOnNobiliy(ArrayList<User> arrayList) {
        Collections.sort(arrayList, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                if (o1.getNobilityPathPosition().getPosition() > o2.getNobilityPathPosition().getPosition())
                    return -1;
                if (o1.getNobilityPathPosition().getPosition() < o2.getNobilityPathPosition().getPosition())
                    return 1;
                else
                    return 0;
            }
        });
    }

    private ArrayList<User> checkFirst(ArrayList<User> arrayList) {
        Collections.sort(arrayList, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                if (o1.getVictoryPathPosition() > o2.getVictoryPathPosition())
                    return -1;
                if (o1.getVictoryPathPosition() < o2.getVictoryPathPosition())
                    return 1;
                else
                    return 0;
            }
        });

        for (User user : arrayList) {
            for (Iterator<User> itr = arrayList.iterator(); itr.hasNext(); ) {
                User userUsed = itr.next();
                if (arrayList.get(0).getVictoryPathPosition() > userUsed.getVictoryPathPosition()) {
                    itr.remove();
                }
            }
        }
        return arrayList;
    }


    public void onUserPass(User user) {

        if (this.users.indexOf(user) == this.nextUser % this.users.size() && !this.buyPhase && !this.sellPhase) {
            user.setMainActionCounter(0);
            user.setFastActionCounter(0);
            user.decrementOptionalActionCounter();
        }
    }

    public void onUserDisconnected(User user) {

        this.users.forEach(user1 -> {
            if (user1.isConnected()) {
                user1.getBaseCommunication().sendUserDisconnect(user.getUsername());
            }
        });

        if (this.buyPhase) {
            this.onFinishBuyPhase(user);
        } else {
            if (this.sellPhase)
                this.onFinishSellPhase(user);
        }
    }

    public void onSelectOldPermitCard(User user, PermitCard permitCard) {
        try {
            System.out.println("on select old permit card");
            permitCard.getBonus().getBonus(user, this.game);
            user.decrementOptionalActionCounter();

        } catch (ActionNotPossibleException e) {
            e.printStackTrace();
        }
        this.sendSnapshotToAll();

    }

    public boolean userConnectedRoutine() {
        for (User user : this.users) {
            if (user.isConnected()) {
                return true;
            }
        }
        return false;
    }

    public synchronized void cleanGame() {
        if (!this.userConnectedRoutine()) {

            this.roundTimer.cancel();
            this.users.clear();
            GamesManager.getInstance().cancelThisGame(this.game, this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        GameController that = (GameController) o;

        return this.fakeUser != null ? this.fakeUser.equals(that.fakeUser) : that.fakeUser == null;

    }

    @Override
    public int hashCode() {
        return this.fakeUser != null ? this.fakeUser.hashCode() : 0;
    }
}
