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

import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.Stack;

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

    }

    //try try
    private void addRegions(Game game) {
        for (Region region: game.getRegions().values()) {

        }
    }

    private void addUserToSnapshot(Game game) {
        for (User user : game.getUsers()) {
            this.usersInGame.put(user.getUsername(), new BaseUser(user));
        }
    }

}
