package ClientPackage.Controller;

import ClientPackage.Service.ClientService;
import ClientPackage.Service.FactoryService;
import ClientPackage.View.BaseView;
import ClientPackage.View.FactoryView;
import ClientPackage.View.ViewException;
import CommonModel.GameModel.Action.BuildWithPermitCard;
import CommonModel.GameModel.Action.ElectCouncillor;
import CommonModel.GameModel.ActionNotPossibleException;
import CommonModel.GameModel.Card.PermitCard;
import CommonModel.GameModel.Card.PoliticColor;
import CommonModel.GameModel.City.City;
import CommonModel.GameModel.City.CityName;
import CommonModel.GameModel.City.Color;
import CommonModel.GameModel.City.Region;
import CommonModel.GameModel.Councilor;
import com.sun.deploy.util.SessionState;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

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

    private ClientController(){

    }

    public static ClientController getInstance(){
        if (clientController == null)
            clientController = new ClientController();
        return clientController;
    }

    public void init(){
        try {
            String networkMethod = "";
            String uiMethod = "";

             inKeyboard = new BufferedReader(new InputStreamReader(System.in));
            networkMethod = getChoiceConnection(inKeyboard);
            String serverIP = getServerIP(inKeyboard);
            clientService = FactoryService.getService(networkMethod,serverIP,this);
            if(clientService.Connect()) {
                uiMethod = getUIMethod(inKeyboard);
                baseView = FactoryView.getBaseView(uiMethod,this);
                baseView.initView();
            }
            else{
                System.out.println("not connected, sorry");
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
        String choice = inKeyboard.readLine();
        while(method.equals("")) {
            switch (choice) {
                case "1":
                    method = "GUI";
                    break;
                case "2":
                    method = "CLI";
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
            method = "RMI";
        } else {
            method = "SOCKET";
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

    public void onTestAction() {
        ElectCouncillor electCouncillor = new ElectCouncillor(new Councilor(PoliticColor.BLACK), null, Region.COAST);
        try {
            try {
                clientService.onTestAction(electCouncillor);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } catch (ActionNotPossibleException e) {
            e.printStackTrace();
        }
    }
}
