package Utilities.Class;

import CommonModel.GameModel.Action.Action;
import CommonModel.GameModel.Bonus.Generic.Bonus;
import CommonModel.Snapshot.SnapshotToSend;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.PrintWriter;

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
    public static void SendCommunicationInfo(PrintWriter out, String code, Object toSend ) {

        String toSendString="";
        CommunicationInfo communicationInfo;
        String communicationToSend;
        Gson gson = null;

        gson = new GsonBuilder().registerTypeAdapter(Action.class, new InterfaceAdapter<Action>())
                .registerTypeAdapter(Bonus.class,new InterfaceAdapter<Bonus>())
                .create();

        if(toSend!=null) {
            toSendString = gson.toJson(toSend, toSend.getClass());
        }
        else{
            toSendString="";
        }
        /*switch (code){
            case Constants.CODE_ACTION:
                toSendString = gson.toJson(toSend,Action.class);
                break;
            case Constants.CODE_SNAPSHOT:

                break;
            default:
                toSendString= gson.toJson(toSend);
                break;
        }
        */
        System.out.println(toSendString);
        communicationInfo = new CommunicationInfo(code, toSendString);
        communicationToSend = gson.toJson(communicationInfo);
        out.println(communicationToSend);
    }

    public static CommunicationInfo decodeCommunicationInfo(String communicationInfo){
        System.out.println(communicationInfo);
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
                .create();
         return gson.fromJson(action,Action.class);
    }

    public static SnapshotToSend getSnapshot(String snapshot){
        Gson gson = new GsonBuilder().registerTypeAdapter(Action.class, new InterfaceAdapter<Action>())
                .registerTypeAdapter(Bonus.class,new InterfaceAdapter<Bonus>())
                .create();
        return gson.fromJson(snapshot,SnapshotToSend.class);
    }

}
