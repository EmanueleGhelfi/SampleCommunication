package CommonModel.GameModel.Path;

/**
 * Created by Giulio on 14/05/2016.
 */

import CommonModel.GameModel.Bonus.Generic.Bonus;

import java.io.Serializable;

/**
 * Position in nobility path
 */
public class Position implements Serializable{

    private int position;
    private Bonus bonus;

    public Position() {
    }

    public Position(int position, Bonus bonus) {
        this.position = position;
        this.bonus = bonus;
    }

    public int getPosition() {
        return position;
    }

    public Bonus getBonus() {
        return bonus;
    }

}
