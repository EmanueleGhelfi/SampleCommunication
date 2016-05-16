package CommonModel.GameModel.Bonus;

import CommonModel.GameModel.ActionNotPossibleException;
import CommonModel.GameModel.Bonus.Bonus;
import CommonModel.GameModel.Bonus.VictoryPointBonus;
import Server.Model.Game;
import Server.UserClasses.User;

/**
 * Created by Giulio on 13/05/2016.
 */
public class KingBonusCard implements Bonus {

    private VictoryPointBonus victoryPointBonus;
    private int order; //from 1 to 5

    public KingBonusCard(int order) {
        this.order = order;
        switch(order){
            case 1:
                victoryPointBonus = new VictoryPointBonus(25);
                break;
            case 2:
                victoryPointBonus = new VictoryPointBonus(18);
                break;
            case 3:
                victoryPointBonus = new VictoryPointBonus(12);
                break;
            case 4:
                victoryPointBonus = new VictoryPointBonus(7);
                break;
            case 5:
                victoryPointBonus = new VictoryPointBonus(3);
                break;
        }
    }

    @Override
    public void getBonus(User user, Game game) throws ActionNotPossibleException {
        victoryPointBonus.getBonus(user, game);
        game.popKingBonusCard();
    }
}
