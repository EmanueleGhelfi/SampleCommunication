package CommonModel.GameModel;

/**
 * Created by Giulio on 14/05/2016.
 */
public class Position {

    private int position;
    private Bonus bonus;

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
