package CommonModel.GameModel.Bonus.Reward;

import CommonModel.GameModel.Bonus.Generic.Bonus;
import CommonModel.GameModel.Bonus.SingleBonus.VictoryPointBonus;
import Server.Model.Game;
import Server.Model.User;
import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Giulio on 13/05/2016.
 */
public class KingBonusCard implements Bonus, Serializable {

    private VictoryPointBonus victoryPointBonus;
    private int order; //from 1 to 5

    public KingBonusCard() {
    }

    public KingBonusCard(int order) {
        this.order = order;
        switch (order) {
            case 1:
                victoryPointBonus = new VictoryPointBonus(Constants.FIRST_ARRIVED_KING_BONUS);
                break;
            case 2:
                victoryPointBonus = new VictoryPointBonus(Constants.SECOND_ARRIVED_KING_BONUS);
                break;
            case 3:
                victoryPointBonus = new VictoryPointBonus(Constants.THIRD_ARRIVED_KING_BONUS);
                break;
            case 4:
                victoryPointBonus = new VictoryPointBonus(Constants.FOURTH_ARRIVED_KING_BONUS);
                break;
            case 5:
                victoryPointBonus = new VictoryPointBonus(Constants.FIFTH_ARRIVED_KING_BONUS);
                break;
        }
    }

    @Override
    public void getBonus(User user, Game game) throws ActionNotPossibleException {
        victoryPointBonus.getBonus(user, game);
        game.popKingBonusCard();
    }

    @Override
    public String getBonusName() {
        return "KingBonusCard";
    }

    @Override
    public ArrayList<Bonus> getBonusArrayList() {
        return null;
    }

    @Override
    public ArrayList<String> getBonusURL() {
        return null;
    }

    @Override
    public ArrayList<String> getBonusInfo() {
        ArrayList<String> toReturn = new ArrayList<String>();
        toReturn.addAll(victoryPointBonus.getBonusInfo());
        return toReturn;
    }

    public int getOrder() {
        return order;
    }

    @Override
    public String toString() {
        return "KingBonusCard{" +
                "order=" + order +
                ", victoryPointBonus=" + victoryPointBonus +
                '}';
    }
}
