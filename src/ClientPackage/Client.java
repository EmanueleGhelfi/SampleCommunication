package ClientPackage;

import ClientPackage.Service.ClientService;
import ClientPackage.Service.FactoryService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;

/**
 * Created by Emanuele on 09/05/2016.
 */
public class Client {

    private ClientService clientService;
    private BufferedReader inKeyboard;

    public Client(){

    }

    public void Start(){
        try {
            String method = "";

             inKeyboard = new BufferedReader(new InputStreamReader(System.in));
            method = getChoiceConnection(inKeyboard);
            String serverIP = getServerIP(inKeyboard);
            clientService = FactoryService.getService(method,serverIP,this);
            if(clientService.Connect()) {
                System.out.println("connected");
                ReadName();
            }
            else{
                System.out.println("not connected, sorry");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }


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
        System.out.println("Inserisci IP GameManager");
        String scelta = inKeyboard.readLine();
        return scelta;
    }

    public void onNameReceived(boolean result) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(!result){
                        ReadName();
                    }
                    else{
                        goToChat();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }

    private void goToChat() throws IOException {
        while (true) {
            System.out.println("Cosa vuoi mandare?");
            String line = inKeyboard.readLine();
            clientService.SendMessage(line);
        }
    }
}
