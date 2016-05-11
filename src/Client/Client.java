package Client;

import Client.Service.ClientService;
import Client.Service.FactoryService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;

/**
 * Created by Emanuele on 09/05/2016.
 */
public class Client {

    public Client(){

    }

    public void Start(){
        try {
            String method = "";
            BufferedReader inKeyboard = new BufferedReader(new InputStreamReader(System.in));
            method = getChoiceConnection(inKeyboard);
            String serverIP = getServerIP(inKeyboard);
            ClientService clientService = FactoryService.getService(method,serverIP);
            if(clientService.Connect()) {
                System.out.println("connected");
                clientService.SendMessage("Hello server");


                while (true) {
                    System.out.println("Cosa vuoi mandare?");
                    String line = inKeyboard.readLine();
                    clientService.SendMessage(line);
                }
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
}
