package ClientPackage;

import ClientPackage.Controller.ClientController;

/**
 * Created by Emanuele on 09/05/2016.
 */
public class ClientMain {

    public static void main(String[] args){
        ClientController clientController = new ClientController();
        clientController.init();
    }
}
