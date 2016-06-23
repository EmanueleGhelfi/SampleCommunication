package ClientPackage.View.CLIResources;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.GeneralView.CLIView;
import asg.cliche.Command;
import asg.cliche.Param;
import org.apache.commons.cli.Options;

import java.util.Scanner;

/**
 * Created by Emanuele on 21/06/2016.
 */
public class LoginCliController implements CliController {

    CLIView cliView;
    ClientController clientController;
    Options options = OptionsClass.getMarketOptions();
    Scanner scanner = new Scanner(System.in);
    CLIParser parser = new CLIParser(this.getClass());
    CLIPrinter cliPrinter = new CLIPrinter();

    public LoginCliController(CLIView cliView, ClientController clientController) {
        this.cliView = cliView;
        this.clientController = clientController;
    }

    @Override
    public void parseLine(String line) {
        parser.parseInput(line,this,cliPrinter);
    }

    @Override
    public void changeController() {

    }

    @Override
    public void printHelp() {
        parser.printHelp();
    }

    @Command (description = "Do login",name = "login", abbrev = "l")
    public void onLogin(@Param(name = "name", description = "your name in game") String name) {
        System.out.println("sending login");
        clientController.onSendLogin(name);
    }
}
