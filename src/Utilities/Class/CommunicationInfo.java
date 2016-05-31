package Utilities.Class;

import CommonModel.GameModel.Action.Action;
import CommonModel.GameModel.Bonus.Generic.Bonus;
import CommonModel.GameModel.Market.BuyableObject;
import CommonModel.GameModel.Market.BuyableWrapper;
import CommonModel.Snapshot.SnapshotToSend;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.PrintWriter;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Sends and decode communicationInfo
 * Created by Emanuele on 11/05/2016.
 */

public class CommunicationInfo {

    //The code of the Request
    private String code;
    //Information associated to the request
    private String info;

    public CommunicationInfo(String code, String info) {
        this.code = code;
        this.info = info;
    }

    /**
     * Separate implementation between action and other object
     * @param out printwriter in wich sends object
     * @param code code of the action
     * @param toSend object to send
     */
    public static void SendCommunicationInfo(PrintWriter out, String code, Object toSend) {

        String toSendString="";
        CommunicationInfo communicationInfo;
        String communicationToSend;
        Gson gson = null;

        gson = new GsonBuilder().registerTypeAdapter(Action.class, new InterfaceAdapter<Action>())
                .registerTypeAdapter(Bonus.class,new InterfaceAdapter<Bonus>())
                .registerTypeAdapter(BuyableObject.class,new InterfaceAdapter<BuyableObject>())
                .create();

        if(toSend!=null) {
            if(code.equals(Constants.CODE_ACTION)){
                toSendString = gson.toJson(toSend, Action.class);
            }
            else {
                toSendString = gson.toJson(toSend, toSend.getClass());
            }
        }
        else{
            toSendString="";
        }
        communicationInfo = new CommunicationInfo(code, toSendString);
        communicationToSend = gson.toJson(communicationInfo);
        out.println(communicationToSend);
    }

    public static CommunicationInfo decodeCommunicationInfo(String communicationInfo){
        Gson gson = new Gson();
        return gson.fromJson(communicationInfo,CommunicationInfo.class);
    }

    public String getCode() {
        return code;
    }
    public String getInfo() {
        return info;
    }

    /**
     * This function deserialize an action with interface adapter
     * @param action action to deserialize
     * @return action deserialized
     */
    public static Action getAction(String action){
        Gson gson = new GsonBuilder().registerTypeAdapter(Action.class, new InterfaceAdapter<Action>())
                .registerTypeAdapter(Bonus.class,new InterfaceAdapter<Bonus>())
                .create();
         return gson.fromJson(action,Action.class);
    }

    public static SnapshotToSend getSnapshot(String snapshot){
        Gson gson = new GsonBuilder().registerTypeAdapter(Action.class, new InterfaceAdapter<Action>())
                .registerTypeAdapter(Bonus.class,new InterfaceAdapter<Bonus>())
                .create();
        return gson.fromJson(snapshot,SnapshotToSend.class);
    }

    public static ArrayList<BuyableWrapper> getBuyableArray(String array){
        Gson gson = new GsonBuilder().registerTypeAdapter(Action.class, new InterfaceAdapter<Action>())
                .registerTypeAdapter(Bonus.class,new InterfaceAdapter<Bonus>())
                .registerTypeAdapter(BuyableObject.class,new InterfaceAdapter<BuyableObject>())
                .create();
        Type resultType = new TypeToken<ArrayList<BuyableWrapper>>() {
        }.getType();
        return gson.fromJson(array,resultType);

    }

}
