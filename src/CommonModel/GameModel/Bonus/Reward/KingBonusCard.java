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
            case Constants.ONE:
                victoryPointBonus = new VictoryPointBonus(Constants.TWENTYFIVE);
                break;
            case Constants.TWO:
                victoryPointBonus = new VictoryPointBonus(Constants.EIGHTEEN);
                break;
            case Constants.THREE:
                victoryPointBonus = new VictoryPointBonus(Constants.TWELVE);
                break;
            case Constants.FOUR:
                victoryPointBonus = new VictoryPointBonus(Constants.SEVEN);
                break;
            case Constants.FIVE:
                victoryPointBonus = new VictoryPointBonus(Constants.THREE);
                break;
        }
    }

    @Override
    public void getBonus(User user, Game game) throws ActionNotPossibleException {
        victoryPointBonus.getBonus(user, game);
        game.popKingBonusCard();
    }
}
