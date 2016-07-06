package Utilities.Test;

import CommonModel.GameModel.Action.Action;
import CommonModel.GameModel.Action.MainActionElectCouncilor;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticColor;
import CommonModel.GameModel.City.RegionName;
import CommonModel.GameModel.Council.Councilor;
import Utilities.Class.InterfaceAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Emanuele on 19/05/2016.
 */
public class Serialization {

    public static void main(String[] args) {
        Action action = new MainActionElectCouncilor(new Councilor(PoliticColor.BLACK), null, RegionName.HILL);
        Gson gson = new GsonBuilder().registerTypeAdapter(Action.class, new InterfaceAdapter<Action>())
                .create();
        String json = gson.toJson(action, Action.class);
        System.out.println(json + "STRING !");

        Gson gson1 = new GsonBuilder().registerTypeAdapter(Action.class, new InterfaceAdapter<Action>())
                .create();
        Action action2 = gson1.fromJson(json, Action.class);
        System.out.println("ACTION 2" + action2);
    }
}
