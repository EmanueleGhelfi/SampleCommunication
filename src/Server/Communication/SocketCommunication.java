package Server.Communication;

import CommonModel.CommunicationInfo;
import CommonModel.Constants;
import CommonModel.GameImmutable;
import Server.Controller.GameController;
import Server.Managers.GamesManager;
import Server.UserClasses.User;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Emanuele on 09/05/2016.
 */
public class SocketCommunication extends BaseCommunication implements Runnable {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private User user;
    private GamesManager gamesManager;
    private GameController gameController;
    private GameImmutable gameImmutable;

    public SocketCommunication(Socket socket) throws IOException {
        this.socket = socket;
        //Open the buffers
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        gamesManager = GamesManager.getInstance();
    }



    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void notifyGameStart() {


    }

    @Override
    public void run() {
        String line;
        Gson gson = new Gson();
        System.out.println("Socket communication started");
        try {
            while ((line = in.readLine()) != null) {
                CommunicationInfo communicationInfo = CommunicationInfo.decodeCommunicationInfo(line);
                switch (communicationInfo.getCode()) {
                    case Constants.CODE_NAME: {
                        String username = gson.fromJson(communicationInfo.getInfo(),String.class);
                        if(!gamesManager.userAlreadyPresent(username)) {
                            this.user.setUsername(username);
                            gamesManager.addToGame(user);
                            CommunicationInfo.SendCommunicationInfo(out, Constants.CODE_NAME, true);
                        }
                        else{
                            CommunicationInfo.SendCommunicationInfo(out, Constants.CODE_NAME, false);
                        }
                        break;
                    }
                    case Constants.CODE_CHAT: {
                        String message = gson.fromJson(communicationInfo.getInfo(), String.class);
                        System.out.println(message);
                       // user.OnMessage(message);
                        break;
                    }
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
            user.setConnected(false);
        }
    }


}
