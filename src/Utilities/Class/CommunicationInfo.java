package Utilities.Class;

import com.google.gson.Gson;
import java.io.PrintWriter;

/**
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

    public static void SendCommunicationInfo(PrintWriter out, String code, Object toSend ) {
        Gson gson = new Gson();
        String toSendString = gson.toJson(toSend);
        CommunicationInfo communicationInfo = new CommunicationInfo(code,toSendString);
        String communicationToSend = gson.toJson(communicationInfo);
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

}
