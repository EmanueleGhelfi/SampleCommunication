package ClientPackage.View.CLIResources;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.GeneralView.CLIView;
import org.apache.commons.cli.Options;

import java.util.Scanner;

/**
 * Created by Emanuele on 19/06/2016.
 */
public class ShopCliController {

    CLIView cliView;
    ClientController clientController;
    Options options = OptionsClass.getMarketOptions();
    Scanner scanner = new Scanner(System.in);

    public ShopCliController(CLIView cliView, ClientController clientController) {
        this.cliView = cliView;
        this.clientController = clientController;
    }

    public void onStartMarket() {

        System.out.println("START MARKET");
        String read = scanner.nextLine();
        System.out.println("In shop: "+read);
    }
}
