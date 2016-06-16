package CommonModel.GameModel.Council;

import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Giulio on 16/06/2016.
 */
public class Bank {

    private HashMap<PoliticColor, ArrayBlockingQueue<Councilor>> hashMapArrayList;

    public Bank() {
        for (PoliticColor politicColor : PoliticColor.values()) {
            ArrayBlockingQueue<Councilor> politicColorArrayList = new ArrayBlockingQueue<>(4);
            for (int i = 0; i < 4; i++) {
                politicColorArrayList.add(new Councilor(politicColor));
            }
            hashMapArrayList.put(politicColor, politicColorArrayList);
        }
    }

    public boolean checkCouncilor(PoliticColor politicColor) {
        int size = hashMapArrayList.get(politicColor).size();
        if (size > 0 && size < 4) {
            return true;
        }
        return false;
    }

    public Councilor getCouncilor(Councilor councilor){
        hashMapArrayList.get(councilor.getColor()).remove(councilor);
        return councilor;
    }

    public void setCouncilor(Councilor councilor){
        hashMapArrayList.get(councilor.getColor()).add(councilor);
    }

    public ArrayList<PoliticColor> showCouncilor(){
        ArrayList<PoliticColor> colorToShow = new ArrayList<>();
        for (Map.Entry<PoliticColor, ArrayBlockingQueue<Councilor>> entry : hashMapArrayList.entrySet()) {
            if (entry.getValue().size()>0)
                colorToShow.add(entry.getKey());
        }
        return colorToShow;
    }
}
