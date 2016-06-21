package ClientPackage.View.CLIResources;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.GeneralView.CLIView;
import CommonModel.GameModel.Market.BuyableWrapper;
import Utilities.Class.ArrayUtils;
import Utilities.Exception.CancelException;
import asg.cliche.Command;
import org.apache.commons.cli.Options;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Emanuele on 19/06/2016.
 */
public class ShopCliController implements CliController{

    CLIView cliView;
    ClientController clientController;
    Options options = OptionsClass.getMarketOptions();
    Scanner scanner = new Scanner(System.in);
    CLIParser parser = new CLIParser(this.getClass());
    CLIPrinter cliPrinter = new CLIPrinter();
    private boolean onMarketPhase;
    private boolean onBuyPhase;
    private boolean onSellPhase;

    public ShopCliController(CLIView cliView, ClientController clientController) {
        this.cliView = cliView;
        this.clientController = clientController;
    }

    public void onStartMarket() {
        cliPrinter.printBlue("START MARKET!");
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

    public void onStartBuyPhase() {
        cliPrinter.printBlue("Start buy phase!");
    }

    public void onFinishBuyPhase() {
        cliPrinter.printBlue("Finish Buy Phase");
    }

    @Command(name = "buy", abbrev = "b", description = "buy Something in the market")
    public void buy(){

        cliPrinter.printBlue("MARKET LIST: ");
        ArrayList<BuyableWrapper> buyableWrappers = new ArrayList<>();
        clientController.getSnapshot().getMarketList().stream().filter(buyableWrapper ->
                !buyableWrapper.getUsername().equalsIgnoreCase(clientController.getSnapshot().getCurrentUser().getUsername()))
                .forEach(buyableWrappers::add);

        for (BuyableWrapper buyableWrapper : buyableWrappers) {
            System.out.println("" + buyableWrappers.indexOf(buyableWrapper) + ". "
                    + cliPrinter.toStringFormatted(buyableWrapper));
        }
        try {
            ArrayList<BuyableWrapper> selected = selectBuyableWrapper(buyableWrappers);
            clientController.onBuy(selected);
        } catch (CancelException e) {
            e.printStackTrace();
        }

    }

    private ArrayList<BuyableWrapper> selectBuyableWrapper(ArrayList<BuyableWrapper> buyableWrappers) throws CancelException {
        cliPrinter.printBlue("Select objects that you want to buy separated by blank spaces");
        System.out.println("-1 for cancel");
        ArrayList<BuyableWrapper> toReturn = new ArrayList<>();

        String selected = scanner.nextLine();

        String[] selectedParsed = selected.split(" ");


        if(ArrayUtils.checkInteger(selectedParsed,buyableWrappers) &&  ArrayUtils.checkDuplicate(selectedParsed)){
            //TODO: continue
            if(Integer.parseInt(selectedParsed[0])!=-1) {
                for (int i = 0; i < selectedParsed.length; i++) {
                    toReturn.add(buyableWrappers.get(Integer.parseInt(selectedParsed[i])));
                }
            }
            else throw new CancelException();
        }

        return selectBuyableWrapper(buyableWrappers);

    }

    @Command(name = "sell", abbrev = "s", description = "Sell something in the market")
    public void sell() {

    }
}
