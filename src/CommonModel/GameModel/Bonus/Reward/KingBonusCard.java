package CommonModel.GameModel.Bonus.Reward;

import CommonModel.GameModel.Bonus.Generic.Bonus;
import CommonModel.GameModel.Bonus.SingleBonus.VictoryPointBonus;
import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;
import Server.Model.Game;
import Server.Model.User;
import java.io.Serializable;

/**
 * Created by Giulio on 13/05/2016.
 */
public class KingBonusCard implements Bonus,Serializable {

    private VictoryPointBonus victoryPointBonus;
    private int order; //from 1 to 5

    public KingBonusCard() {
    }

    public KingBonusCard(int order) {
        this.order = order;
        switch(order){
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
}
