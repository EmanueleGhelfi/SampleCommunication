package CommonModel.GameModel.Path;

import CommonModel.GameModel.Bonus.Generic.Bonus;
import java.io.Serializable;

/**
 * Created by Giulio on 14/05/2016.
 */

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

    @Override
    public String toString() {

        return ""+position +"\t ---> \t\t Bonus:\t\t "+bonus.toString();
    }
}
