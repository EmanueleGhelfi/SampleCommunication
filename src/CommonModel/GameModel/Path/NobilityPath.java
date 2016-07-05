package CommonModel.GameModel.Path;

import CommonModel.GameModel.Bonus.Generic.Bonus;
import CommonModel.GameModel.Bonus.Generic.MainBonus;
import Server.Model.User;
import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;

import java.io.Serializable;

/**
 * Created by Giulio on 14/05/2016.
 */
public class NobilityPath implements Path, Serializable {

    private final int length = Constants.NOBILITY_PATH_ELEMENT;
    private final Position[] position;

    public NobilityPath() {
        this.position = new Position[this.length + 1];
        for (int i = 0; i <= this.length; i++) {
            Bonus bonus = new MainBonus(1, 3, 10, true);
            if (i == 0)
                bonus.getBonusArrayList().clear();
            this.position[i] = new Position(i, bonus);
        }
    }

    @Override
    public void goAhead(User user, int value) throws ActionNotPossibleException {
        if (user.getNobilityPathPosition().getPosition() + value > this.length) {
            user.setNobilityPathPosition(this.position[this.length]);
            if (this.position[this.length].getBonus() != null) {
                this.position[this.length].getBonus().getBonus(user, user.getGame());
            }
        } else {
            if (user.getNobilityPathPosition().getPosition() + value < 0) {
                throw new ActionNotPossibleException(Constants.NOBILITY_PATH_EXCEPTION);
            } else {
                user.setNobilityPathPosition(this.position[user.getNobilityPathPosition().getPosition() + value]);
                this.position[user.getNobilityPathPosition().getPosition()].getBonus().getBonus(user, user.getGame());
            }
        }
    }

    public Position[] getPosition() {
        return this.position;
    }
}
