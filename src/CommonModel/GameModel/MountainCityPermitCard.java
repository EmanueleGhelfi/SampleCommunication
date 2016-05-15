package CommonModel.GameModel;

import java.util.ArrayList;

/**
 * Created by Emanuele on 15/05/2016.
 */
public enum MountainCityPermitCard {

    N('N',null,null),KNO('K','N','O'),L('L',null,null),KL('K','L',null),OK('O','K',null),ML('M','L',null),LMN('L','M','N'),
    KLM('K','L','M'),KLO('K','L','O'),O('O',null,null),K('K',null,null),M('M',null,null),MN('M','N',null),MNO('M','N','O'),
    NO('N','O',null);

    ArrayList<Character> cities;

    MountainCityPermitCard(Character city1,Character city2, Character city3) {
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
}
