package ClientPackage.Controller;

import ClientPackage.Service.ClientService;
import ClientPackage.Service.FactoryService;
import ClientPackage.View.BaseView;
import ClientPackage.View.FactoryView;
import ClientPackage.View.ViewException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;

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

    public ClientController(){

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
                baseView = FactoryView.getBaseView(uiMethod);
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

        System.out.println("Inserisci il tuo nome:");
        String name = inKeyboard.readLine();
        System.out.println("Attendi la verifica...");
        clientService.sendName(name);


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
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(!result){
                        ReadName();
                    }
                    else{
                        // Start All
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }


}
