package CommonModel.GameModel;

import java.util.ArrayList;

/**
 * Created by Emanuele on 15/05/2016.
 */
public enum CoastCityPermitCard implements CityEnum{

    ABE('A','B','E'),A('A',null,null),CD('C','D',null),B('B',null,null),D('D',null,null),AB('A','B',null),
    CDE('C','D','E'),AE('A','E',null),BCD('B','C','D'),C('C',null,null),DE('D','E',null),BC('B','C',null),ADE('A','D','E'),
    E('E',null,null),ABC('A','B','C');

    ArrayList<Character> cities;



    CoastCityPermitCard(Character city1,Character city2, Character city3) {
        if(city1!=null){
            cities.add(city1);
        }
        if(city2!=null){
            cities.add(city2);
        }
        if(city3!=null){
            cities.add(city3);
        }
    }

    public static CityEnum[] getCities() {
        return CoastCityPermitCard.values();
    }
}
