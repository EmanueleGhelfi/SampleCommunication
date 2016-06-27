package ClientPackage.View.CLIResources;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.GeneralView.CLIView;
import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import CommonModel.GameModel.City.City;
import CommonModel.GameModel.Council.Helper;
import CommonModel.GameModel.Market.BuyableWrapper;
import CommonModel.Snapshot.CurrentUser;
import CommonModel.Snapshot.SnapshotToSend;
import Utilities.Class.ArrayUtils;
import Utilities.Exception.CancelException;
import asg.cliche.Command;
import org.apache.commons.cli.Options;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import static ClientPackage.View.CLIResources.CLIColor.ANSI_RED;
import static ClientPackage.View.CLIResources.CLIColor.ANSI_RESET;

/**
 * Created by Emanuele on 19/06/2016.
 */
public class ShopCliController implements CliController{

    CLIView cliView;
    ClientController clientController;
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
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
        System.out.println("---------------------------------------------------------------------------");
        cliPrinter.printBlue("START MARKET!");
        System.out.println("----------------------------------------------------------------------------");
        onMarketPhase = true;
        onSellPhase=true;
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
        onBuyPhase = true;
        onSellPhase=false;
    }

    public void onFinishBuyPhase() {
        cliPrinter.printBlue("----------------------------------------------------------");
        cliPrinter.printBlue("Finish Buy Phase");
        cliPrinter.printBlue("----------------------------------------------------------");
        onBuyPhase=false;
        onMarketPhase=false;
    }

    @Command(name = "buy", abbrev = "b", description = "buy Something in the market")
    public void buy(){

        if(onBuyPhase) {
            cliPrinter.printBlue("MARKET LIST: ");
            ArrayList<BuyableWrapper> buyableWrappers = new ArrayList<>();
            clientController.getSnapshot().getMarketList().stream().filter(buyableWrapper ->
                    !buyableWrapper.getUsername().equalsIgnoreCase(clientController.getSnapshot().getCurrentUser().getUsername()))
                    .forEach(buyableWrappers::add);
            printMarketList(buyableWrappers);
            try {
                ArrayList<BuyableWrapper> selected = selectBuyableWrapper(buyableWrappers);
                if (selected != null) {
                    clientController.onBuy(selected);
                    clientController.sendFinishedBuyPhase();
                    onBuyPhase = false;
                    cliPrinter.printBlue("SENDING OBJECT TO SERVER....");
                }
            } catch (CancelException e) {
                System.out.println("Cancelled correctly");
            }
        }
        else{
            cliPrinter.printError("Sorry, you aren't in buy phase!");
        }

    }

    private ArrayList<BuyableWrapper> selectBuyableWrapper(ArrayList<BuyableWrapper> buyableWrappers) throws CancelException {
        cliPrinter.printBlue("Select objects that you want to buy separated by blank spaces");
        System.out.println("-1 for cancel");
        ArrayList<BuyableWrapper> toReturn = new ArrayList<>();

        try {
            while (!reader.ready() && onMarketPhase){
                Thread.sleep(200);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(onMarketPhase) {
            String selected = null;
            try {
                selected = reader.readLine();
                String[] selectedParsed = selected.split(" ");


                if (ArrayUtils.checkInteger(selectedParsed, buyableWrappers) && ArrayUtils.checkDuplicate(selectedParsed)) {
                    if (!selectedParsed[0].equals("-1")) {
                        for (int i = 0; i < selectedParsed.length; i++) {
                            toReturn.add(buyableWrappers.get(Integer.parseInt(selectedParsed[i])));
                        }
                        return toReturn;
                    } else throw new CancelException();
                }
            } catch (IOException e) {

            }
        }
        else{
            cliPrinter.printError("Not on market phase");
        }
        return null;

    }

    @Command(name = "sell", abbrev = "s", description = "Sell something in the market")
    public void sell() {

        if(onSellPhase) {
            cliPrinter.printBlue("YOUR OBJECTS: ");
            ArrayList<BuyableWrapper> buyableWrappers = updateList();

            for (BuyableWrapper buyableWrapper : buyableWrappers) {
                System.out.println("" + buyableWrappers.indexOf(buyableWrapper) + ". "
                        + cliPrinter.toStringFormatted(buyableWrapper));
            }

            try {
                ArrayList<BuyableWrapper> toSell = selectBuyableWrapper(buyableWrappers);
                if (toSell != null) {
                    ArrayList<BuyableWrapper> itemCost = getCost(toSell);
                    clientController.sendSaleItem(itemCost);
                    clientController.sendFinishSellPhase();
                    onSellPhase = false;
                    System.out.println("SENDING ITEMS TO SERVER...");
                } else {
                }

            } catch (CancelException e) {
                System.out.println("Cancelled!");
            }
        }
        else{
            cliPrinter.printError("Sorry, you aren't in sell phase, sorry!");
        }
    }

    private ArrayList<BuyableWrapper> getCost(ArrayList<BuyableWrapper> toSell) throws CancelException {
        cliPrinter.printBlue("Insert cost (-1 for cancel)");
        for(int i = 0; i<toSell.size();i++){
            System.out.println("Insert price for: "+cliPrinter.toStringFormatted(toSell.get(i)));

            try {
                while (!reader.ready() && onMarketPhase){
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if(!onMarketPhase)
                    break;

                try {
                    String line = reader.readLine();
                    if(line.equals("-1"))
                        throw new CancelException();

                    int cost = Integer.parseInt(line);
                    toSell.get(i).setCost(cost);
                }
                catch (NumberFormatException e ){
                    cliPrinter.printError("ERROR IN COST!");
                    i--;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return toSell;
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


    @Command(name = "showMarket", abbrev = "sm", description = "Show elements in market")
    public void showMarket(){

        if(onBuyPhase){
            ArrayList<BuyableWrapper> buyableWrappers = new ArrayList<>();
            clientController.getSnapshot().getMarketList().stream().filter(buyableWrapper ->
                    !buyableWrapper.getUsername().equalsIgnoreCase(clientController.getSnapshot().getCurrentUser().getUsername()))
                    .forEach(buyableWrappers::add);
            printMarketList(buyableWrappers);
        }
        else{
            cliPrinter.printError("Sorry, you aren't in Buy Phase");
        }
    }


    private void printMarketList(ArrayList<BuyableWrapper> buyableWrappers){
        for (BuyableWrapper buyableWrapper : buyableWrappers) {
            System.out.println("" + buyableWrappers.indexOf(buyableWrapper) + ". "
                    + cliPrinter.toStringFormatted(buyableWrapper));
        }
    }


    @Command(name = "showSell", abbrev = "ss", description = "Show your items on market list")
    public void showSellList(){
        ArrayList<BuyableWrapper> buyableWrappers = new ArrayList<>();
        clientController.getSnapshot().getMarketList().stream().filter(buyableWrapper ->
                buyableWrapper.getUsername().equalsIgnoreCase(clientController.getSnapshot().getCurrentUser().getUsername()))
                .forEach(buyableWrappers::add);
        printMarketList(buyableWrappers);
    }


    @Command (description = "Show your status", name = "status", abbrev = "st")
    public void showStatus() {
        System.out.println("-----------------------------------------------------------------");
        CurrentUser currentUser = clientController.getSnapshot().getCurrentUser();
        System.out.println(ANSI_RED+"STATUS: \n"+ANSI_RESET);
        cliPrinter.printBlue("Politic Card: ");
        for (PoliticCard politicCard: currentUser.getPoliticCards()){
            System.out.println("\t"+cliPrinter.toStringFormatted(politicCard)+"");
        }

        cliPrinter.printBlue("Permit Card:\n");
        for (PermitCard permitCard: currentUser.getPermitCards()){
            System.out.println("\t"+cliPrinter.toStringFormatted(permitCard));
        }

        cliPrinter.printBlue("Posizioni:");
        System.out.println("\t Nobility Path: "+currentUser.getNobilityPathPosition().getPosition());
        System.out.println("\t Money Path: "+currentUser.getCoinPathPosition());
        System.out.println("\t Nobility Path: "+currentUser.getNobilityPathPosition().getPosition());
        System.out.println("Aiutanti : "+currentUser.getHelpers().size());

        System.out.println("Le tue Città:");

        for (City city: currentUser.getUsersEmporium()){
            System.out.println("\t " + cliPrinter.toStringFormatted(city));
        }

        cliPrinter.printBlue("Le tue azioni: ");
        System.out.println("Azioni Principali:  "+currentUser.getMainActionCounter());
        System.out.println("Azioni veloci :"+currentUser.getFastActionCounter());

        System.out.println("Fine status\n ");
        System.out.println("-----------------------------------------------------------------");
    }

    @Command(description = "Finish current phase", name = "finish",abbrev = "f")
    public void finish(){
        if(onMarketPhase) {
            if (onSellPhase) {
                clientController.sendFinishSellPhase();
            } else {
                if (onBuyPhase)
                    clientController.sendFinishedBuyPhase();
            }
        }
        else{
            cliPrinter.printError("Sorry, you aren't in market phase!");
        }

    }
}
