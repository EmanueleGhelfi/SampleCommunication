package ClientPackage.Main;

import ClientPackage.Controller.ClientController;

/** This class is used to initialize and create the clientController.
 * Created by Emanuele on 09/05/2016.
 */
public class ClientMain {

    public static void main(String[] args) {
        ClientController clientController = ClientController.getInstance();
        clientController.init();
    }

}
