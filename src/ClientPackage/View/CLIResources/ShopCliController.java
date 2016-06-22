package ClientPackage.View.CLIResources;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.GeneralView.CLIView;
import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import CommonModel.GameModel.Council.Helper;
import CommonModel.GameModel.Market.BuyableWrapper;
import CommonModel.Snapshot.SnapshotToSend;
import Utilities.Class.ArrayUtils;
import Utilities.Exception.CancelException;
import asg.cliche.Command;
import org.apache.commons.cli.Options;

import java.util.ArrayList;
import java.util.Iterator;
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

        cliPrinter.printBlue("YOUR OBJECT: ");
        ArrayList<BuyableWrapper> buyableWrappers = updateList();

        for (BuyableWrapper buyableWrapper : buyableWrappers) {
            System.out.println("" + buyableWrappers.indexOf(buyableWrapper) + ". "
                    + cliPrinter.toStringFormatted(buyableWrapper));
        }




    }

    private ArrayList<BuyableWrapper> updateList() {
        ArrayList<BuyableWrapper> sellList = new ArrayList<>();
        ArrayList<BuyableWrapper> mBuyList = clientController.getSnapshot().getMarketList();
        SnapshotToSend snapshotTosend = clientController.getSnapshot();
        for (PoliticCard politicCard: snapshotTosend.getCurrentUser().getPoliticCards()) {
            BuyableWrapper buyableWrapperTmp = new BuyableWrapper(politicCard,snapshotTosend.getCurrentUser().getUsername());
            sellList.add(buyableWrapperTmp);
        }

        for(PermitCard permitCard: snapshotTosend.getCurrentUser().getPermitCards()){
            BuyableWrapper buyableWrapperTmp = new BuyableWrapper(permitCard,snapshotTosend.getCurrentUser().getUsername());
            sellList.add(buyableWrapperTmp);
        }

        for(Helper helper: snapshotTosend.getCurrentUser().getHelpers()){
            BuyableWrapper buyableWrapper = new BuyableWrapper(helper,snapshotTosend.getCurrentUser().getUsername());
            sellList.add(buyableWrapper);
        }

        for(Iterator<BuyableWrapper> itr = mBuyList.iterator(); itr.hasNext();){
            BuyableWrapper buyableWrapper = itr.next();
            if(buyableWrapper.getUsername().equals(snapshotTosend.getCurrentUser().getUsername())){
                sellList.remove(buyableWrapper);
                sellList.add(buyableWrapper);
                itr.remove();
            }
        }

        return sellList;
    }
}
