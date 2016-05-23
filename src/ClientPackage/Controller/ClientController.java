package ClientPackage.Controller;

import ClientPackage.NetworkInterface.ClientService;
import ClientPackage.NetworkInterface.ClientFactoryService;
import ClientPackage.View.GeneralView.BaseView;
import ClientPackage.View.GeneralView.FactoryView;
import CommonModel.GameModel.Action.Action;
import CommonModel.GameModel.Action.FastActionChangePermitCardWithHelper;
import CommonModel.Snapshot.SnapshotToSend;
import Server.Model.Map;
import Utilities.Class.Constants;
import Utilities.Exception.ViewException;
import CommonModel.GameModel.Action.MainActionElectCouncilor;
import Utilities.Exception.ActionNotPossibleException;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticColor;
import CommonModel.GameModel.City.Region;
import CommonModel.GameModel.Council.Councilor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by Emanuele on 09/05/2016.
 */

/**
 * This class manage event and transaction with server
 */
public class ClientController {

    private ClientService clientService;
    private BufferedReader inKeyboard;
    private BaseView baseView;
    private static ClientController clientController;
    private SnapshotToSend snapshot;

    private ClientController(){
    }

    public static ClientController getInstance(){
        if (clientController == null)
            clientController = new ClientController();
        return clientController;
    }

    public void init(){
        try {
            String networkMethod;
            String uiMethod;
            inKeyboard = new BufferedReader(new InputStreamReader(System.in));
            networkMethod = getChoiceConnection(inKeyboard);
            String serverIP = getServerIP(inKeyboard);
            clientService = ClientFactoryService.getService(networkMethod,serverIP,this);
            if(clientService.Connect()) {
                uiMethod = getUIMethod(inKeyboard);
                baseView = FactoryView.getBaseView(uiMethod,this);
                baseView.initView();
            }
            else{
                System.out.println("Not connected, sorry");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (ViewException e) {
            e.printStackTrace();
        }
    }

    private String getUIMethod(BufferedReader inKeyboard) throws IOException {
        String method = "";
        System.out.println("Quale UI vuoi utilizzare? \n 1. GUI \n 2. CLI");
        int choice = Integer.parseInt(inKeyboard.readLine());
        while(method.equals("")) {
            switch (choice) {
                case 1:
                    method = Constants.GUI;
                    break;
                case 2:
                    method = Constants.CLI;
                    break;
                default:
                    System.out.println("Scelta non valida");
            }
        }
        return method;
    }

    private void ReadName() throws IOException {
        baseView.showLoginError();
    }

    public String getChoiceConnection (BufferedReader inKeyboard) throws IOException {
        String method;
        System.out.println("Inserisci metodo comunicazione\n 1. RMI \n 2. Socket \n (So che non sai cosa sono ma metti una cosa a caso)");
        String scelta = inKeyboard.readLine();
        if (scelta.equals("1")) {
            method = Constants.RMI;
        } else {
            method = Constants.SOCKET;
        }
        return method;
    }

    public String getServerIP (BufferedReader inKeyboard) throws IOException {
        System.out.println("Inserisci IP GamesManager");
        String scelta = inKeyboard.readLine();
        return scelta;
    }

    /**
     * Called when the name is accepted
     * @param result
     */
    public void onNameReceived(boolean result) {
       try {
           if (!result) {
               ReadName();
           } else {
               baseView.showWaitingForStart();
           }
       }catch (Exception e ){
           e.printStackTrace();
       }
    }

    public void onSendLogin(String userName) {
        clientService.sendName(userName);
    }

    public void setSnapshot(SnapshotToSend snapshot) {
        this.snapshot = snapshot;
        System.out.println("CLIENTCONTROLLER <- "+snapshot);
    }

    /**
     * debug
     */
    public void createAction() {
        Action action = new MainActionElectCouncilor(new Councilor(PoliticColor.BLACK), null, Region.COAST);
        try {
            clientService.onTestAction(action);
        } catch (ActionNotPossibleException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * debug
     */
    public void createSecondAction() {
        Action action = new FastActionChangePermitCardWithHelper(Region.HILL);
        try {
            clientService.onTestAction(action);
        } catch (ActionNotPossibleException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void showMap(ArrayList<Map> mapArrayList) {
        baseView.showMap(mapArrayList);
    }

    public void sendMap(Map map) {
        clientService.sendMap(map);
    }

    public void gameInitialization(SnapshotToSend snapshotToSend) {
        snapshot = snapshotToSend;
        System.out.println(snapshot + " <- SNAP");
        baseView.gameInitialization(snapshotToSend);
    }
}
