package CommonModel.GameModel.Council;

import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticColor;

import java.io.Serializable;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Giulio on 14/05/2016.
 */
public class Council implements Serializable {

    private Queue<Councilor> councilorQueue;
    public Council() {
        this.councilorQueue = new ArrayBlockingQueue<>(4);
    }

    public void add(Councilor councilorToAdd) {
        try {
            councilorQueue.add(councilorToAdd);
        }
        catch (IllegalStateException e){
            councilorQueue.remove();
            councilorQueue.add(councilorToAdd);
            System.out.println(councilorQueue);
        }
    }

    public Queue<Councilor> getCouncil(){
        Queue q2 = new ArrayBlockingQueue<Councilor>(4);
        for (Councilor councilor: councilorQueue) {
            q2.add(councilor);
        }
        return q2;
    }

    @Override
    public String toString() {
        return "Council{" +
                "councilorQueue=" + councilorQueue +
                '}';
    }

    public static void main(String[] args){
        Councilor councilor = new Councilor(PoliticColor.WHITE);
        Councilor councilor2 = new Councilor(PoliticColor.BLACK);
        Councilor councilor3 = new Councilor(PoliticColor.BLACK);
        Councilor councilor4 = new Councilor(PoliticColor.BLACK);
        Councilor councilor5 = new Councilor(PoliticColor.BLUE);
        Councilor councilor6 = new Councilor(PoliticColor.ORANGE);
        Council council = new Council();
        council.add(councilor);
        council.add(councilor2);
        council.add(councilor3);
        council.add(councilor4);
        System.out.println(council);
        council.add(councilor5);
        System.out.println(council);
        council.add(councilor6);
        System.out.println(council);
    }
}
