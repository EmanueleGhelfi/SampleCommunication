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
    CLIParser parser = new CLIParser(getClass());
    CLIPrinter cliPrinter = new CLIPrinter();
    private boolean loginDone;

    public LoginCliController(CLIView cliView, ClientController clientController) {
        this.cliView = cliView;
        this.clientController = clientController;
    }

    @Override
    public void parseLine(String line) {
        this.parser.parseInput(line, this, this.cliPrinter);
    }

    @Override
    public void changeController() {

    }

    @Override
    public void printHelp() {
        this.parser.printHelp();
    }

    @Command(description = "Do login", name = "login", abbrev = "l")
    public void onLogin(@Param(name = "name", description = "your name in game") String name) {
        if (!this.loginDone) {
            System.out.println("sending login");
            this.clientController.onSendLogin(name);
            this.loginDone = true;
        } else {
            this.cliPrinter.printError("Login already done, sorry!");
        }
    }

    public void setLoginDone(boolean loginDone) {
        this.loginDone = loginDone;
    }
}
