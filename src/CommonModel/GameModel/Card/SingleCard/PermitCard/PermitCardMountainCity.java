package CommonModel.GameModel.Card.SingleCard.PermitCard;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Giulio on 15/05/2016.
 */
public enum PermitCardMountainCity implements Serializable {

    KLM('K', 'L', 'M'), LMN('L', 'M', 'N'), OK('O', 'K', null), ML('M', 'L', null), L('L', null, null), M('M', null, null), NO('N', 'O', null),
    KL('K', 'L', null), KLO('K', 'L', 'O'), K('K', null, null), KNO('K', 'N', 'O'), MNO('M', 'N', 'O'), O('O', null, null), N('N', null, null),
    MN('M', 'N', null);

    ArrayList<Character> cities = new ArrayList<>();

    PermitCardMountainCity() {
    }

    PermitCardMountainCity(Character city1, Character city2, Character city3) {
        if (city1 != null) {
            cities.add(city1);
        }
        if (city2 != null) {
            cities.add(city2);
        }
        if (city3 != null) {
            cities.add(city3);
        }
    }

    public static ArrayList<ArrayList<Character>> getCities() {
        ArrayList<ArrayList<Character>> cities = new ArrayList<>();
        for (PermitCardMountainCity permitCardMountainCity : PermitCardMountainCity.values()) {
            cities.add(permitCardMountainCity.cities);
        }
        return cities;
    }
}
