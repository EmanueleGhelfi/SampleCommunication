package ClientPackage.NetworkInterface;

import ClientPackage.Controller.ClientController;
import Utilities.Class.CommunicationInfo;
import Utilities.Class.Constants;
import CommonModel.GameModel.Action.MainActionElectCouncilor;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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

    @Override
    public void onTestAction(MainActionElectCouncilor electCouncilor) {
        CommunicationInfo.SendCommunicationInfo(out, "PROVA", electCouncilor);
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
        Gson gson = new Gson();
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
        }
    }
}
