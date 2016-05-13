package ClientPackage.Service;

import ClientPackage.Controller.ClientController;
import CommonModel.CommunicationInfo;
import CommonModel.Constants;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Emanuele on 09/05/2016.
 */
public class ClientSocketService extends ClientService implements Runnable {

    private String hostname = "localhost";
    private int port = 4333;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private ClientController clientController;

    ClientSocketService(String serverIP, ClientController clientController){
        hostname = serverIP;
        this.clientController = clientController;
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
    public void run() {
        System.out.println("ClientSocketService Started");
        String line = null;
        Gson gson = new Gson();
        try {
            while ( (line = in.readLine())!=null){
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
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
