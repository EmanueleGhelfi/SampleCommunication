package CommonModel.Snapshot;

import CommonModel.GameModel.Bonus.Reward.ColorBonusCard;
import CommonModel.GameModel.Bonus.Reward.KingBonusCard;
import CommonModel.GameModel.Bonus.Reward.RegionBonusCard;
import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.City.Region;
import CommonModel.GameModel.Council.King;
import CommonModel.GameModel.Path.Position;
import Server.Model.*;
import Server.Model.Map;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Emanuele on 18/05/2016.
 */
public class SnapshotToSend implements Serializable{

    private HashMap<String,BaseUser> usersInGame = new HashMap<>();
    private HashMap<String,Region> regions = new HashMap<>();
    private King king;
    private HashMap<String,ArrayList<PermitCard>> visiblePermitCards = new HashMap<>();
    private HashMap<String,RegionBonusCard> regionBonusCards = new HashMap<>();
    private HashMap<String,ColorBonusCard> colorBonusCards = new HashMap<>();
    private Stack<KingBonusCard> kingBonusCards = new Stack<>();
    //Nobility path position
    private Position[] nobilityPathPosition;
    private CurrentUser currentUser;
    private Map map;

    public SnapshotToSend() {
    }

    public SnapshotToSend(Game game, User user) {
        addUserToSnapshot(game);
        addRegions(game);
        //add king
        this.king = game.getKing();
        this.regionBonusCards = game.getRegionBonusCard();
        this.colorBonusCards = game.getColorBonusCard();
        this.kingBonusCards = game.getKingBonusCards();
        this.nobilityPathPosition = game.getNobilityPath().getPosition();
        this.currentUser = new CurrentUser(user);
        this.map = game.getMap();
    }

    private void addRegions(Game game) {
        this.regions = game.getRegions();
    }

    private void addUserToSnapshot(Game game) {
        for (User user: game.getUsersInGame().values()) {
            usersInGame.put(user.getUsername(),new BaseUser(user));
        }
    }

    @Override
    public String toString() {
        return "SnapshotToSend{" +
                "visiblePermitCards=" + visiblePermitCards +
                ", regions=" + regions +
                ", regionBonusCards=" + regionBonusCards +
                ", nobilityPathPosition=" + Arrays.toString(nobilityPathPosition) +
                ", map=" + map +
                ", kingBonusCards=" + kingBonusCards +
                ", currentUser=" + currentUser +
                ", king=" + king +
                ", colorBonusCards=" + colorBonusCards +
                '}';
    }

    public King getKing() {
        return king;
    }

    public Map getMap() {
        return map;
    }

    public HashMap<String, ArrayList<PermitCard>> getVisiblePermitCards() {
        return visiblePermitCards;
    }
}
