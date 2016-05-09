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
            System.out.println("Inserisci metodo comunicazione\n 1. RMI \n 2. Socket \n (So che non sai cosa sono ma metti una cosa a caso)");
            BufferedReader inKeyboard = new BufferedReader(new InputStreamReader(System.in));
            String scelta = inKeyboard.readLine();
            if(scelta.equals("1")){
                method="RMI";
            }
            else {
                method="SOCKET";
            }
            ClientService clientService = FactoryService.getService(method);
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
}
