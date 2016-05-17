package ClientPackage.Main;

import ClientPackage.Controller.ClientController;

/**
 * Created by Emanuele on 09/05/2016.
 */
public class ClientMain {

    public static void main(String[] args){
        ClientController clientController = ClientController.getInstance();
        clientController.init();
    }
}
