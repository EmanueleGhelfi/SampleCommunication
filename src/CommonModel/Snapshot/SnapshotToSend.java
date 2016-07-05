package CommonModel.Snapshot;

import CommonModel.GameModel.Bonus.Reward.ColorBonusCard;
import CommonModel.GameModel.Bonus.Reward.KingBonusCard;
import CommonModel.GameModel.Bonus.Reward.RegionBonusCard;
import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.City.Region;
import CommonModel.GameModel.City.RegionName;
import CommonModel.GameModel.Council.Bank;
import CommonModel.GameModel.Council.Councilor;
import CommonModel.GameModel.Council.King;
import CommonModel.GameModel.Market.BuyableWrapper;
import CommonModel.GameModel.Path.Position;
import Server.Model.Game;
import Server.Model.Map;
import Server.Model.User;
import Utilities.Exception.CouncilNotFoundException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

/**
 * Created by Emanuele on 18/05/2016.
 */
public class SnapshotToSend implements Serializable {

    private final HashMap<String, BaseUser> usersInGame = new HashMap<>();
    private HashMap<RegionName, Region> regions = new HashMap<>();
    private King king;
    private final HashMap<RegionName, ArrayList<PermitCard>> visiblePermitCards = new HashMap<>();
    private HashMap<RegionName, RegionBonusCard> regionBonusCards = new HashMap<>();
    private HashMap<String, ColorBonusCard> colorBonusCards = new HashMap<>();
    private Stack<KingBonusCard> kingBonusCards = new Stack<>();
    //Nobility path position
    private Position[] nobilityPathPosition;
    private CurrentUser currentUser;
    private Map map;
    private Bank bank;

    //list of buyable wrapper, all object that user can buy
    private ArrayList<BuyableWrapper> marketList = new ArrayList<>();

    public SnapshotToSend() {
    }

    public SnapshotToSend(Game game, User user) {
        this.addUserToSnapshot(game);
        this.addRegions(game);
        //add king
        king = game.getKing();
        regionBonusCards = game.getRegionBonusCard();
        colorBonusCards = game.getColorBonusCard();
        kingBonusCards = game.getKingBonusCards();
        nobilityPathPosition = game.getNobilityPath().getPosition();
        currentUser = new CurrentUser(user);
        map = game.getMap();
        marketList = game.getMarketList();
        bank = game.getBank();
        for (RegionName region : RegionName.values()) {
            this.visiblePermitCards.put(region, game.getPermitDeck(region).getVisibleArray());
        }
    }

    private void addRegions(Game game) {
        regions = game.getRegions();
    }

    private void addUserToSnapshot(Game game) {
        for (User user : game.getUsersInGame().values()) {
            this.usersInGame.put(user.getUsername(), new BaseUser(user));
        }
    }

    @Override
    public String toString() {
        return "SnapshotToSend{" +
                "visiblePermitCards=" + this.visiblePermitCards +
                ", regions=" + this.regions +
                ", regionBonusCards=" + this.regionBonusCards +
                ", nobilityPathPosition=" + Arrays.toString(this.nobilityPathPosition) +
                ", map=" + this.map +
                ", kingBonusCards=" + this.kingBonusCards +
                ", currentUser=" + this.currentUser +
                ", king=" + this.king +
                ", colorBonusCards=" + this.colorBonusCards +
                '}';
    }

    public King getKing() {
        return this.king;
    }

    public Map getMap() {
        return this.map;
    }

    public HashMap<RegionName, ArrayList<PermitCard>> getVisiblePermitCards() {
        return this.visiblePermitCards;
    }

    public ArrayList<PermitCard> getVisibleRegionPermitCard(RegionName regionName) {

        return this.visiblePermitCards.get(regionName);
    }

    public CurrentUser getCurrentUser() {
        return this.currentUser;
    }

    public HashMap<RegionName, Region> getRegions() {
        return this.regions;
    }

    public ArrayList<Councilor> getCouncil(RegionName region) throws CouncilNotFoundException {
        if (this.regions.get(region) != null) {
            return new ArrayList<Councilor>(this.regions.get(region).getCouncil().getCouncil());
        } else throw new CouncilNotFoundException();
    }

    public HashMap<String, BaseUser> getUsersInGame() {
        return this.usersInGame;
    }

    public Position[] getNobilityPathPosition() {
        return this.nobilityPathPosition;
    }

    public ArrayList<BuyableWrapper> getMarketList() {
        return (ArrayList<BuyableWrapper>) this.marketList.clone();
    }

    public Stack<KingBonusCard> getKingBonusCards() {
        return this.kingBonusCards;
    }

    public Bank getBank() {
        try {
            return (Bank) this.bank.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
