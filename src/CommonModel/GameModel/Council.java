package CommonModel.GameModel;

import CommonModel.GameModel.Card.PoliticColor;
import CommonModel.GameModel.City.Color;
import CommonModel.GameModel.City.Region;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Giulio on 14/05/2016.
 */
public class Council {

    private Queue<Councilor> councilorQueue;

    public Council() {
        this.councilorQueue = new ArrayBlockingQueue<Councilor>(4);
    }

    public void add(Councilor councilorToAdd) {
        try {
            councilorQueue.add(councilorToAdd);
        }
        catch (IllegalStateException e){
            councilorQueue.remove();
            councilorQueue.add(councilorToAdd);
        }

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
