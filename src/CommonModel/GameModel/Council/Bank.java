package CommonModel.GameModel.Council;

import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticColor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Giulio on 16/06/2016.
 */
public class Bank implements Serializable, Cloneable {

    private final HashMap<PoliticColor, ArrayList<Councilor>> hashMapArrayList = new HashMap<>();

    public Bank() {
        for (PoliticColor politicColor : PoliticColor.values()) {
            ArrayList<Councilor> politicColorArrayList = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                politicColorArrayList.add(new Councilor(politicColor));
            }
            this.hashMapArrayList.put(politicColor, politicColorArrayList);
        }
    }

    public synchronized boolean checkCouncilor(PoliticColor politicColor) {
        int size = this.hashMapArrayList.get(politicColor).size();
        return size > 0 && size <= 4;
    }

    public synchronized Councilor getCouncilor(PoliticColor politicColor) {
        if (this.checkCouncilor(politicColor)) {
            return this.hashMapArrayList.get(politicColor).remove(this.hashMapArrayList.get(politicColor).size() - 1);
        }
        return null;
    }

    public synchronized void addCouncilor(Councilor councilor) {
        this.hashMapArrayList.get(councilor.getColor()).add(councilor);
    }

    public synchronized ArrayList<PoliticColor> showCouncilor() {
        ArrayList<PoliticColor> colorToShow = new ArrayList<>();
        for (PoliticColor politicColor : PoliticColor.values()) {
            if (this.hashMapArrayList.get(politicColor).size() > 0) {
                colorToShow.add(politicColor);
            }
        }
        return colorToShow;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "Bank{" +
                "hashMapArrayList=" + this.hashMapArrayList +
                '}';
    }
}
