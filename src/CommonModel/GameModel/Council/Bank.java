package CommonModel.GameModel.Council;

import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticColor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.Collectors;

/**
 * Created by Giulio on 16/06/2016.
 */
public class Bank implements Serializable, Cloneable {

    private HashMap<PoliticColor, ArrayBlockingQueue<Councilor>> hashMapArrayList = new HashMap<>();

    public Bank() {
        for (PoliticColor politicColor : PoliticColor.values()) {
            ArrayBlockingQueue<Councilor> politicColorArrayList = new ArrayBlockingQueue<>(4);
            for (int i = 0; i < 4; i++) {
                politicColorArrayList.add(new Councilor(politicColor));
            }
            hashMapArrayList.put(politicColor, politicColorArrayList);
        }
    }

    public synchronized boolean checkCouncilor(PoliticColor politicColor) {
        int size = hashMapArrayList.get(politicColor).size();
        if (size > 0 && size <= 4) {
            return true;
        }
        return false;
    }

    public synchronized Councilor getCouncilor(PoliticColor politicColor){
        if(checkCouncilor(politicColor)) {
            return hashMapArrayList.get(politicColor).remove();
        }
        return null;
    }

    public synchronized void addCouncilor(Councilor councilor){
        hashMapArrayList.get(councilor.getColor()).add(councilor);
    }

    public synchronized ArrayList<PoliticColor> showCouncilor(){
        ArrayList<PoliticColor> colorToShow = hashMapArrayList.entrySet()
                .stream()
                .filter(entry -> entry.getValue().size() > 0)
                .map(Map.Entry::getKey)
                .collect(Collectors.toCollection(ArrayList::new));
        System.out.println(colorToShow);
        return colorToShow;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "Bank{" +
                "hashMapArrayList=" + hashMapArrayList +
                '}';
    }
}
