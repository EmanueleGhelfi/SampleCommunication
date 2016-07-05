package CommonModel.GameModel.Council;

import Utilities.Class.Constants;

import java.io.Serializable;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Giulio on 14/05/2016.
 */
public class Council implements Serializable {

    private final Queue<Councilor> councilorQueue;
    private final Bank bank;

    public Council(Bank bank) {
        this.bank = bank;
        councilorQueue = new ArrayBlockingQueue<>(Constants.COUNCILOR_DIMENSION);
    }

    public void add(Councilor councilorToAdd) {
        try {
            this.councilorQueue.add(councilorToAdd);
        } catch (IllegalStateException e) {
            this.bank.addCouncilor(this.councilorQueue.remove());
            this.councilorQueue.add(councilorToAdd);
        }
    }

    @Override
    public String toString() {
        return "Council{" +
                "councilorQueue=" + this.councilorQueue +
                '}';
    }

    public Queue<Councilor> getCouncil() {
        Queue q2 = new ArrayBlockingQueue<Councilor>(4);
        for (Councilor councilor : this.councilorQueue) {
            q2.add(councilor);
        }
        return q2;
    }

}
