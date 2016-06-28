package Server.Controller;

import CommonModel.GameModel.Action.Action;
import CommonModel.GameModel.Bonus.Generic.Bonus;
import CommonModel.GameModel.Bonus.SingleBonus.NobilityBonus;
import CommonModel.GameModel.Card.Deck.PermitDeck;
import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import CommonModel.GameModel.City.City;
import CommonModel.GameModel.City.CityName;
import CommonModel.GameModel.City.RegionName;
import CommonModel.GameModel.Council.Helper;
import CommonModel.GameModel.Council.King;
import CommonModel.GameModel.Market.BuyableObject;
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
import javafx.scene.control.ContentDisplay;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.time.Duration;
import java.util.*;

/**
 * Created by Emanuele on 13/05/2016.
 */
public class GameController implements Serializable{

    private Game game;
    private TimerTask timerTask;
    private Timer timer;
    private int duration = Constants.GAME_TIMEOUT;
    private ArrayList<Map> availableMaps = new ArrayList<>();
    // initialized to users size, when 0 start market
    private int turnCounter =0;
    private HashMap<User,Boolean> marketHashMap = new HashMap<>();
    private ArrayList<User> users = new ArrayList<>();
    private boolean sellPhase=false;
    private boolean buyPhase = false;
    private int nextUser;
    private int lastUser = -1;
    private Timer roundTimer = new Timer();
    private UserColor[] userColorSet;
    private FakeUser fakeUser;


    public GameController() {
    }

    public GameController(Game game) {
        this.game = game;
        this.timer = new Timer();
        try {
            availableMaps = Map.readAllMap();
        } catch (MapsNotFoundException e) {
            System.out.println(e);
        }
    }

    public void startTimer() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                notifyStarted();
            }
        };
    }

    /**
     * Called when init game
     */
    public void notifyStarted() {
        if (game.getUsers().size() == 2){
            creatingFakeUser();
            //configurationForTwoPlayers();
        }
        users = new ArrayList<>(game.getUsers());
        game.setStarted(true);
        setDefaultStuff();
        // send map to first user
        for(User user: users) {
            if(user.isConnected()) {
                sendAvailableMap(user);
                break;
            }
        }

        startConnectedTimer();

    }

    private void creatingFakeUser() {
        fakeUser = new FakeUser();
        game.getUsersInGame().put(fakeUser.getUsername(), fakeUser);
        fakeUser.setUsername("FakeUser");
    }

    private void configurationForTwoPlayers() {
        ArrayList<PermitCard> permitCardArray = new ArrayList<>();
        for (java.util.Map.Entry<RegionName, PermitDeck> permitDeck : game.getPermitDecks().entrySet()) {
            permitCardArray.add(permitDeck.getValue().getAndRemoveRandomPermitCard());
        }
        for (PermitCard permitCard : permitCardArray) {
            for (Character character : permitCard.getCityAcronimous()){
                for (City city : game.getMap().getCity()) {
                    if (city.getCityName().getCityName().startsWith(character.toString().toUpperCase()) && !fakeUser.getUsersEmporium().contains(city))
                        fakeUser.addEmporium(city);
                }
            }
        }

    }

    private void setDefaultStuff() {
        int userCounter = 0;
        for (User user: users) {
            userColorSet = UserColor.values();
            user.setUserColor(colorAvailable());
            if (!(user instanceof FakeUser)) {
                user.setHelpers(Constants.DEFAULT_HELPER_COUNTER + userCounter);
                user.setCoinPathPosition(Constants.FIRST_INITIAL_POSITION_ON_MONEY_PATH + userCounter);
                user.setNobilityPathPosition(game.getNobilityPath().getPosition()[Constants.INITIAL_POSITION_ON_NOBILITY_PATH]);
                user.setVictoryPathPosition(Constants.INITIAL_POSITION_ON_VICTORY_PATH);
                userCounter++;
            }
            ArrayList<PoliticCard> politicCardArrayList = new ArrayList<>();
            for (int cont = 0; cont < Constants.DEFAULT_POLITIC_CARD_HAND; cont++) {
                politicCardArrayList.add(game.getPoliticCards().drawACard());
            }
            user.setPoliticCards(politicCardArrayList);


        }
    }

    private UserColor colorAvailable() {
        ArrayList<UserColor> shuffledUserColor = new ArrayList<>(Arrays.asList(UserColor.values()));
        Collections.shuffle(shuffledUserColor);
        for(UserColor userColor: shuffledUserColor){
            boolean found = false;
            for(User user: game.getUsersInGame().values()){
                if(user.getUserColor()!= null && user.getUserColor().equals(userColor)){
                    found=true;
                }
            }
            if(!found){
                return userColor;
            }
        }
        return null;
    }

    public void cancelTimeout() {
        timer.cancel();
    }

    public void setTimeout() {
        if(timer==null){
            timer = new Timer();
        }
        else{
            timer.cancel();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    notifyStarted();
                }
            };
            timer = new Timer();
        }
        timer.schedule(timerTask,duration);
    }


    /**
     * create snapshot and change round
     * @param user user that has finished round
     */
    public void onFinishRound(User user) {
        cancelTimer();
        turnCounter--;
        System.out.println("on finish round called");
        user.getBaseCommunication().finishTurn();

        for(int cont = 0; cont < users.size(); cont++){
            System.out.println("GAMECONTROLLER <- Sending Snapshot to :" + users.get(cont).getUsername());
            SnapshotToSend snapshotToSend = new SnapshotToSend(game, user);
            user.getBaseCommunication().sendSnapshot(snapshotToSend);
            if(user.equals(users.get(cont))){
                nextUser = cont+1;
                while (!users.get((nextUser)%game.getUsers().size()).isConnected() && nextUser%game.getUsers().size()!=cont){
                    System.out.println("user not connected "+ users.get((nextUser)%game.getUsers().size()));
                    turnCounter--;
                    nextUser++;
                }
                if (lastUser != nextUser % game.getUsers().size()) {
                    if ((nextUser % game.getUsers().size()) == cont) {
                        onAllUserDisconnected();
                    } else {
                        if (turnCounter <= 0) {
                            System.out.println("Starting market");
                            startMarket();
                        } else {
                            System.out.println("change round : " + nextUser);
                            changeRound(nextUser);
                        }
                    }
                } else {
                    checkUserWhoWin();
                }
            }
        }
    }

    private void cancelTimer() {
        if(roundTimer!=null){
            roundTimer.cancel();
        }
    }

    private void changeRound(int nextUser) {
        System.out.println("Change round");
        ArrayList<User> userArrayList = new ArrayList<>(game.getUsers());
        userArrayList.get((nextUser) % game.getUsers().size()).setMainActionCounter(Constants.MAIN_ACTION_POSSIBLE);
        userArrayList.get((nextUser) % game.getUsers().size()).setFastActionCounter(Constants.FAST_ACTION_POSSIBLE);
        userArrayList.get((nextUser)%game.getUsers().size()).drawCard();
        userArrayList.get((nextUser) % game.getUsers().size()).getBaseCommunication().changeRound();
        startRoundTimer(userArrayList.get((nextUser)%game.getUsers().size()));
    }

    private void startRoundTimer(User user) {
        roundTimer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                onUserPass(user);
            }
        };
        roundTimer.schedule(timerTask, Constants.ROUND_DURATION);
    }

    private void startMarket() {
        sellPhase = true;
        marketHashMap.clear();

        users.forEach(user -> {
            Runnable runnable = ()-> {
                sendStartMarket(user);
            };
            new Thread(runnable).start();

        });



    }

    private void startConnectedTimer() {
        Timer checkUserTimer = new Timer();
        TimerTask checkUserTimerTask = new TimerTask() {
            @Override
            public void run() {
                for(Iterator<User> iterator = users.iterator(); iterator.hasNext();){
                    iterator.next().getBaseCommunication().ping();
                }
            }
        };
        checkUserTimer.scheduleAtFixedRate(checkUserTimerTask,0,30000);
    }

    private void sendStartMarket(User user) {
        user.getBaseCommunication().sendStartMarket();
    }

    private void onAllUserDisconnected() {
        roundTimer.cancel();
        users.clear();
        GamesManager.getInstance().cancelThisGame(game, this);
    }

    public void doAction(Action action, User user) throws ActionNotPossibleException {
        action.doAction(game,user);
    }

    /** send available map to client
     *
     * @param user
     */
    private void sendAvailableMap(User user) {
        user.getBaseCommunication().sendAvailableMap(availableMaps);
    }

    /* set map and init game called by client*/
    public void setMap(Map map) {
        if(availableMaps.contains(map)){
            Map mapToFind = findMap(map);
            game.setMap(mapToFind);
            game.setKing(new King(map.getCity().get(0),game.getBank()));
            setKingPosition(game,mapToFind);
            for (User user: game.getUsers()) {
                SnapshotToSend snapshotToSend = new SnapshotToSend(game,user);
                // init game
                user.getBaseCommunication().sendSelectedMap(snapshotToSend);
            }
            sendFinishMarketToAll();


            if(game.getUsers().size()==2){
                configurationForTwoPlayers();
            }

            selectFirstPlayer();



            //TODO: READD TEN EMPORIUMS

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

        }

        else{
            System.out.println("MAP NOT PRESENT");
        }
    }

    private void setKingPosition(Game game, Map mapToFind) {
        for(City city: mapToFind.getCity()){
            if(city.getColor().equals(CommonModel.GameModel.City.Color.PURPLE)){
                game.setKing(new King(city,game.getBank()));
                break;
            }
        }
    }

    private Map findMap(Map map) {
        for (Map mapToSelect: availableMaps){
            if(map.equals(mapToSelect)){
                return mapToSelect;
            }
        }
        return null;
    }

    /** disable market phase in all user */
    private void sendFinishMarketToAll() {
        new Thread(()->{
            for (User user:users){
                user.getBaseCommunication().disableMarketPhase();
            }
        }).start();

    }

    private void selectFirstPlayer() {

        ArrayList<User> users = new ArrayList<>(game.getUsers());
        turnCounter=users.size();
       // select first user

        for(User user: users){
            if(user.isConnected()){
                changeRound(users.indexOf(user));
                nextUser = users.indexOf(user);
                break;
            }
        }


        for(int i = 0;i< users.size();i++){
            if(users.get(i).isConnected() && i!=nextUser)
            users.get(i).getBaseCommunication().finishTurn();
        }

        sendSnapshotToAll();
    }

    public synchronized void sendSnapshotToAll() {
        new Thread(()-> {
            for (User user : game.getUsers()) {
                if(user.isConnected()) {
                    SnapshotToSend snapshotToSend = new SnapshotToSend(game, user);
                    user.getBaseCommunication().sendSnapshot(snapshotToSend);
                }
            }
        }).start();
    }

    // called by client when receive an object to sell
    public boolean onReceiveBuyableObject(ArrayList<BuyableWrapper> buyableWrappers) {
        for (BuyableWrapper buyableWrapper: buyableWrappers) {
            try {
                game.addBuyableWrapper(buyableWrapper);
            } catch (AlreadyPresentException e) {
                System.out.println("L'oggetto è già in vendita!");
            }
        }
        //TODO: check if it's ok
        //sendSnapshotToAll();
        return true;

    }

    // called by client when receive object to buy
    public boolean onBuyObject(User user, ArrayList<BuyableWrapper> buyableWrappers) {
        int counter = 0;
        for (BuyableWrapper buyableWrapper : buyableWrappers) {
            try {
                game.getMoneyPath().goAhead(user, -buyableWrapper.getCost());
                game.getMoneyPath().goAhead(game.getUser(buyableWrapper.getUsername()),buyableWrapper.getCost());
                game.removeFromMarketList(buyableWrapper);
                if(buyableWrapper.getBuyableObject() instanceof PermitCard){
                    game.getUser(buyableWrapper.getUsername()).removePermitCardDefinitevely((PermitCard) buyableWrapper.getBuyableObject());
                    user.addPermitCard((PermitCard) buyableWrapper.getBuyableObject());
                }
                else if(buyableWrapper.getBuyableObject() instanceof PoliticCard){
                    game.getUser(buyableWrapper.getUsername()).removePoliticCard((PoliticCard) buyableWrapper.getBuyableObject());
                    user.addPoliticCard((PoliticCard)buyableWrapper.getBuyableObject());
                }
                else if(buyableWrapper.getBuyableObject() instanceof Helper){
                    game.getUser(buyableWrapper.getUsername()).removeHelper((Helper)buyableWrapper.getBuyableObject());
                    user.addHelper();
                }
                counter++;
            } catch (ActionNotPossibleException e) {

            }
        }
        sendSnapshotToAll();

        if(counter==buyableWrappers.size()){
            return true;
        }
        else return false;
    }

    public void onRemoveItem(BuyableWrapper item) {
        game.removeFromMarketList(item);
        sendSnapshotToAll();
    }

    public void onFinishSellPhase(User user) {
        long finishedUser=0;
        if(sellPhase) {
            marketHashMap.put(user, true);

            finishedUser = marketHashMap.entrySet().stream()
                    .filter(java.util.Map.Entry::getValue)
                    .count();

            long connectedUser = users.stream()
                    .filter(BaseUser::isConnected)
                    .count();

            if (finishedUser >= connectedUser) {
                sellPhase=false;
                marketHashMap.clear();
                startBuyPhase();
            }
            else{
                System.out.println("No : "+finishedUser + " "+connectedUser);
            }
        }
    }

    private void startBuyPhase() {
        sendSnapshotToAll();
        buyPhase = true;
        selectRandomUser();
    }

    private void selectRandomUser() {
        Random random = new Random();
        int userNumber =0;
        boolean found = false;
        while (!found) {
            userNumber = random.nextInt(users.size());
            if((!marketHashMap.containsKey(users.get(userNumber)) || !marketHashMap.get(users.get(userNumber)))
                    && users.get(userNumber).isConnected() && !(users.get(userNumber) instanceof FakeUser)){
                found = true;
            }
        }
        System.out.println("Sending start buy phase to "+users.get(userNumber));
        users.get(userNumber).getBaseCommunication().sendStartBuyPhase();
    }

    public void onFinishBuyPhase(User user) {
        if(buyPhase){
            sendSnapshotToAll();
            marketHashMap.put(user,true);

            long finishedUser = marketHashMap.entrySet().stream()
                    .filter(java.util.Map.Entry::getValue)
                    .count();

            long connectedUser = users.stream()
                    .filter(BaseUser::isConnected)
                    .count();

            if(finishedUser<connectedUser) {
                selectRandomUser();
            }
            else {
                marketHashMap.clear();
                buyPhase=false;
                sendFinishMarketToAll();
                turnCounter=users.size();
                changeRound(nextUser);
            }
        }
    }

    public void getCityRewardBonus(City city1, User user) throws ActionNotPossibleException {
            City city = game.getCity(city1);

            if(checkBonusCorrect(city,user)) {
                try {
                    if (!city.getColor().getColor().equals(Constants.PURPLE))
                        city.getBonus(user, game);
                }
                catch (ActionNotPossibleException e){

                }

                user.decrementOptionalActionCounter();

                sendSnapshotToAll();
            }
            else{
                user.getBaseCommunication().selectCityRewardBonus(new SnapshotToSend(game, user));
                throw new ActionNotPossibleException(Constants.CITY_REWARD_BONUS_INCORRECT);
            }

    }

    private boolean checkBonusCorrect(City city, User user) {
        ArrayList<Bonus> bonusArrayList = city.getBonus().getBonusArrayList();
        for(Bonus bonus:bonusArrayList){
            if(bonus instanceof NobilityBonus){
                return false;
            }
        }

        for(City city1: user.getUsersEmporium()){
            if(city1.equals(city)){
                return true;
            }
        }
        return false;
    }

    public void onSelectPermitCard(PermitCard permitCard, User user) {
        PermitDeck permitDeck = game.getPermitDeck(permitCard.getRetroType());
        try {
            PermitCard permitCardTrue = permitDeck.getAndRemovePermitCardVisible(permitCard);
            permitCardTrue.getBonus().getBonus(user,game);
            user.addPermitCard(permitCardTrue);
            user.decrementOptionalActionCounter();
        } catch (ActionNotPossibleException e) {
            e.printStackTrace();
        }
        sendSnapshotToAll();
    }

    public void changeMasterUser() {
        // send map to first user
        for (User user : users) {
            if (user.isConnected()) {
                sendAvailableMap(user);
            }
        }
    }
    public void startingLastRound() {
        lastUser = nextUser % game.getUsers().size();
    }

    public void checkUserWhoWin(){
        ArrayList<User> firstNobilityPathUserToReward = new ArrayList<>(users);
        ArrayList<User> secondNobilityPathUserToReward = new ArrayList<>(users);
        ArrayList<User> userMaxPermitCard = new ArrayList<>(users);
        ArrayList<User> userMaxHelper = new ArrayList<>(users);
        User userToRewardMaxPermitCard = new User();
        User userWithMaxHelperAndPoliticCard = new User();

        firstNobilityPathUserToReward.remove(fakeUser);
        secondNobilityPathUserToReward.remove(fakeUser);
        userMaxPermitCard.remove(fakeUser);
        userMaxHelper.remove(fakeUser);
        users.remove(fakeUser);


        sortingOnNobiliy(firstNobilityPathUserToReward);
        sortingOnNobiliy(secondNobilityPathUserToReward);
        sortingOnPermit(userMaxPermitCard);
        sortingOnHelper(userMaxHelper);

        //for (User user : firstNobilityPathUserToReward) {
            for(Iterator<User> itr = firstNobilityPathUserToReward.iterator(); itr.hasNext();) {
                User userUsed = itr.next();
                if (firstNobilityPathUserToReward.get(0).getNobilityPathPosition().getPosition() > userUsed.getNobilityPathPosition().getPosition()){
                    itr.remove();
                }
            }

        secondNobilityPathUserToReward.removeAll(firstNobilityPathUserToReward);
        //for (User user : secondNobilityPathUserToReward) {
            for(Iterator<User> itr = secondNobilityPathUserToReward.iterator(); itr.hasNext();) {
                User userUsed = itr.next();
                if (secondNobilityPathUserToReward.get(0).getNobilityPathPosition().getPosition() > userUsed.getNobilityPathPosition().getPosition()){
                    itr.remove();
                }
            }

        //for (User user : secondNobilityPathUserToReward) {
            for(Iterator<User> itr = userMaxPermitCard.iterator(); itr.hasNext();) {
                User userUsed = itr.next();
                if (userMaxPermitCard.get(0).getPermitCards().size() > userUsed.getPermitCards().size()){
                    itr.remove();
                }
            }

       // for (User user : secondNobilityPathUserToReward) {
            for(Iterator<User> itr = secondNobilityPathUserToReward.iterator(); itr.hasNext();) {
                User userUsed = itr.next();
                if (userMaxHelper.get(0).getHelpers().size() + userMaxHelper.get(0).getPoliticCardSize() > userUsed.getHelpers().size() + userUsed.getPoliticCardSize()){
                    itr.remove();
                }
            }


        firstNobilityPathUserToReward.forEach(user -> user.setVictoryPathPosition(user.getVictoryPathPosition()+5));
        secondNobilityPathUserToReward.forEach(user -> user.setVictoryPathPosition(user.getVictoryPathPosition() + 2));
        userToRewardMaxPermitCard.setVictoryPathPosition(userToRewardMaxPermitCard.getVictoryPathPosition()+3);

        ArrayList<User> userWhoWin = new ArrayList<>(users);
        userWhoWin = checkFirst(userWhoWin);

        if (userWhoWin.size()>1){
            for (User user : users) {
                if (user.equals(userWithMaxHelperAndPoliticCard)){
                    user.setVictoryPathPosition(user.getVictoryPathPosition()+3);
                }
            }
            userWhoWin = checkFirst(userWhoWin);
        }

        sortingOnWin(users);

        ArrayList<BaseUser> finalSnapshot = new ArrayList<>();
        for (User userInArray : users) {
            finalSnapshot.add(new BaseUser(userInArray));
        }

        for (User user : users) {
            user.getBaseCommunication().sendMatchFinishedWithWin(finalSnapshot);
        }
    }

    private void sortingOnWin(ArrayList<User> arrayList) {
        Collections.sort(arrayList, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                if (o1.getVictoryPathPosition() > o1.getVictoryPathPosition())
                    return -1;
                if (o1.getVictoryPathPosition() > o1.getVictoryPathPosition())
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
            for(Iterator<User> itr = arrayList.iterator(); itr.hasNext();) {
                User userUsed = itr.next();
                if (arrayList.get(0).getVictoryPathPosition() > userUsed.getVictoryPathPosition()){
                    itr.remove();
                }
            }
        }
        return arrayList;
    }


    public void onUserPass(User user) {

        if(users.indexOf(user) == (nextUser%users.size()) && !buyPhase && !sellPhase) {
            user.setMainActionCounter(0);
            user.setFastActionCounter(0);
            user.decrementOptionalActionCounter();
        }
    }

    public void onUserDisconnected(User user) {
        if(buyPhase){
            onFinishBuyPhase(user);
        }
        else{
            if(sellPhase)
                onFinishSellPhase(user);
        }
    }

    public void onSelectOldPermitCard(User user, PermitCard permitCard) {
        try {
            System.out.println("on select old permit card");
            permitCard.getBonus().getBonus(user,game);
            user.decrementOptionalActionCounter();

        } catch (ActionNotPossibleException e) {
            e.printStackTrace();
        }
        sendSnapshotToAll();

    }

    public boolean userConnectedRoutine(){
        for (User user : users) {
            if (user.isConnected()){
                return true;
            }
        }
        return false;
    }

    public void cleanGame(){
        if (!userConnectedRoutine()){
            roundTimer.cancel();
            users.clear();
            GamesManager.getInstance().cancelThisGame(game, this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameController that = (GameController) o;

        return fakeUser != null ? fakeUser.equals(that.fakeUser) : that.fakeUser == null;

    }

    @Override
    public int hashCode() {
        return fakeUser != null ? fakeUser.hashCode() : 0;
    }
}
