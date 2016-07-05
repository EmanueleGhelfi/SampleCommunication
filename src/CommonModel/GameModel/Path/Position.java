package CommonModel.GameModel.Path;

import CommonModel.GameModel.Bonus.Generic.Bonus;

import java.io.Serializable;

/**
 * Created by Giulio on 14/05/2016.
 */

/**
 * Position in nobility path
 */
public class Position implements Serializable {

    private int position;
    private Bonus bonus;

    public Position() {
    }

    public Position(int position, Bonus bonus) {
        this.position = position;
        this.bonus = bonus;
    }

    public int getPosition() {
        return this.position;
    }

    public Bonus getBonus() {
        return this.bonus;
    }

    @Override
    public String toString() {

        return "" + this.position + "\t ---> \t\t Bonus:\t\t " + this.bonus;
    }
}
