package CommonModel.Snapshot;

import CommonModel.GameModel.Bonus.Reward.ColorBonusCard;
import CommonModel.GameModel.Bonus.Reward.KingBonusCard;
import CommonModel.GameModel.Bonus.Reward.RegionBonusCard;
import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.City.Region;
import CommonModel.GameModel.Council.King;
import CommonModel.GameModel.Path.Position;
import Server.Model.Game;
import Server.Model.User;

import java.util.*;

/**
 * Created by Emanuele on 18/05/2016.
 */
public class SnapshotToSend {

    private HashMap<String,BaseUser> usersInGame = new HashMap<>();
    //TODO: cities

    private HashMap<String,Region> regions = new HashMap<>();

    private King king;

    private HashMap<String,ArrayList<PermitCard>> visiblePermitCards = new HashMap<>();

    private HashMap<String,RegionBonusCard> regionBonusCards = new HashMap<>();

    private HashMap<String,ColorBonusCard> colorBonusCards = new HashMap<>();

    private Stack<KingBonusCard> kingBonusCards = new Stack<>();

    //Nobility path position
    private Position[] nobilityPathPosition;
    
    private CurrentUser currentUser;

    public SnapshotToSend(Game game, User user) {
        addUserToSnapshot(game);
        addRegions(game);
        //add king
        this.king = game.getKing();

        this.regionBonusCards = game.getRegionBonusCard();

        this.colorBonusCards = game.getColorBonusCard();

        this.kingBonusCards = game.getKingBonusCards();

        this.nobilityPathPosition = game.getNobilityPath().getPosition();

        this.currentUser = user;
    }

    //try try
    private void addRegions(Game game) {
        this.regions = game.getRegions();
    }

    private void addUserToSnapshot(Game game) {
        for (User user: game.getUsersInGame().values()) {
            usersInGame.put(user.getUsername(),user);
        }
    }

    @Override
    public String toString() {
        return "SnapshotToSend{" +
                "currentUser=" + currentUser +
                ", usersInGame=" + usersInGame +
                ", regions=" + regions +
                ", king=" + king +
                ", visiblePermitCards=" + visiblePermitCards +
                ", regionBonusCards=" + regionBonusCards +
                ", colorBonusCards=" + colorBonusCards +
                ", kingBonusCards=" + kingBonusCards +
                ", nobilityPathPosition=" + Arrays.toString(nobilityPathPosition) +
                '}';
    }
}
