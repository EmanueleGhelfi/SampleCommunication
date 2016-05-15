package CommonModel.GameModel;

import java.util.ArrayList;

/**
 * Created by Giulio on 15/05/2016.
 */
public enum HillCityPermitCard implements CityEnum {

    H('H', null, null), GHI('G', 'H', 'I'), F('F', null, null), HI('H', 'I', null), I('I', null, null), FG('F', 'G', null), J('J', null, null),
    HG('H', 'G', null), FGH('F', 'G', 'H'), G('G', null, null), FJ('F', 'J', null), IJ('I', 'J', null), FGJ('F', 'G', 'J'), FIJ('F', 'I', 'J'),
    IJH('I', 'J', 'H');

    @Override
    public CityEnum[] getCities() {
        return HillCityPermitCard.values();
    }

    ArrayList<Character> cities;


    private HillCityPermitCard(Character city1, Character city2, Character city3) {
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
}
