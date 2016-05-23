package ClientPackage.NetworkInterface;

import ClientPackage.Controller.ClientController;
import CommonModel.GameModel.Action.Action;
import CommonModel.GameModel.Bonus.Generic.Bonus;
import CommonModel.Snapshot.SnapshotToSend;
import Server.Model.Map;
import Utilities.Class.CommunicationInfo;
import Utilities.Class.Constants;
import Utilities.Class.InterfaceAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.LocatorEx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Modifier;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Emanuele on 09/05/2016.
 */
public class ClientSocketService extends ClientService implements Runnable {

    private String hostname = Constants.SOCKET_IP;
    private int port = Constants.SOCKET_PORT;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private ClientController clientController;
    private ExecutorService executorService;

    ClientSocketService(String serverIP, ClientController clientController){
        hostname = serverIP;
        this.clientController = clientController;
        executorService = Executors.newCachedThreadPool();
    }

    @Override
    public boolean Connect() {
        try {
            socket = new Socket(hostname,port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Thread thread = new Thread(this);
            thread.start();
            System.out.println("Thread started");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void sendName(String name) {
        CommunicationInfo.SendCommunicationInfo(out, Constants.CODE_NAME,name);
    }

    //TODO test?
    @Override
    public void onTestAction(Action electCouncilor) {
        CommunicationInfo.SendCommunicationInfo(out, Constants.CODE_ACTION, electCouncilor);
    }

    @Override
    public void sendMap(Map map) {
        CommunicationInfo.SendCommunicationInfo(out, Constants.CODE_MAP, map);
    }

    @Override
    public void run() {
        System.out.println("ClientSocketService Started");
        String line;
        try {
            while ((line = in.readLine())!=null){
                // create a new runnable and use a executor service in order to execute this task
                class DecoderTask implements Runnable{
                    String lineToDecode;
                    public DecoderTask(String line) {
                        this.lineToDecode = line;
                    }
                    @Override
                    public void run() {
                        decodeInfo(lineToDecode);
                    }
                }
                DecoderTask decoderTask = new DecoderTask(line);
                executorService.execute(decoderTask);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void decodeInfo(String line){
        Gson gson = new GsonBuilder().registerTypeAdapter(Action.class, new InterfaceAdapter<Action>())
                .registerTypeAdapter(Bonus.class,new InterfaceAdapter<Bonus>())
                .excludeFieldsWithModifiers(Modifier.TRANSIENT)
                .create();
        CommunicationInfo communicationInfo = CommunicationInfo.decodeCommunicationInfo(line);
        switch (communicationInfo.getCode()){
            case Constants.CODE_NAME:{
                boolean result =  gson.fromJson(communicationInfo.getInfo(),boolean.class);
                clientController.onNameReceived(result);
                break;
            }
            case Constants.CODE_CHAT:{
                String message = gson.fromJson(communicationInfo.getInfo(),String.class);
                System.out.println(message);
                break;
            }
            case Constants.CODE_SNAPSHOT:{
                SnapshotToSend snapshotToSend = CommunicationInfo.getSnapshot(communicationInfo.getInfo());
                //SnapshotToSend snapshotToSend = gson.fromJson(communicationInfo.getInfo(),SnapshotToSend.class);
                clientController.setSnapshot(snapshotToSend);
                break;
            }
            case Constants.CODE_JSON_TEST:{
                ArrayList<Map> mapArrayList = gson.fromJson(communicationInfo.getInfo(), new TypeToken<ArrayList<Map>>(){}.getType());
                System.out.println("EILA");
                clientController.showMap(mapArrayList);
                break;
            }
            case Constants.CODE_INITIALIZE_GAME:{
                SnapshotToSend snapshotToSend = CommunicationInfo.getSnapshot(communicationInfo.getInfo());
                clientController.gameInitialization(snapshotToSend);
                break;
            }
        }
    }
}
