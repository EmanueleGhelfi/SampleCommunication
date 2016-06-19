package ClientPackage.View.CLIResources;

/**
 * Created by Emanuele on 13/05/2016.
 */

import ClientPackage.Controller.ClientController;
import ClientPackage.View.GeneralView.CLIView;
import CommonModel.GameModel.Action.Action;
import CommonModel.GameModel.Action.FastActionChangePermitCardWithHelper;
import CommonModel.GameModel.Action.FastActionNewMainAction;
import CommonModel.GameModel.Action.MainActionElectCouncilor;
import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticColor;
import CommonModel.GameModel.City.City;
import CommonModel.GameModel.City.Region;
import CommonModel.GameModel.City.RegionName;
import CommonModel.GameModel.Council.Councilor;
import CommonModel.Snapshot.CurrentUser;
import CommonModel.Snapshot.SnapshotToSend;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;

import static ClientPackage.View.CLIResources.CLIColor.*;

/**
 * Manage user input on CommandLine
 */
public class MatchCliController {

    private boolean isYourTurn = false;
    private Options matchOptions;
    private CLIView cliView;
    private ClientController clientController;
    private CLIParser cliParser;
    private CLIPrinter cliPrinter = new CLIPrinter();
    private Scanner scanner = new Scanner(System.in);
    private SnapshotToSend snapshotToSend;

    public MatchCliController(CLIView cliView, ClientController clientController) {

        this.cliView=cliView;
        this.clientController=clientController;
        this.matchOptions = OptionsClass.createMatchOptions();
        this.clientController = clientController;
        cliParser = new CLIParser(matchOptions);
    }

    public void onGameStart() {

    }

    public void onYourTurn() {
        snapshotToSend = cliView.getSnapshot();
        isYourTurn=true;
        System.out.println(ANSI_RED+"Turno iniziato"+ANSI_RESET);
        cliPrinter.printHelp(matchOptions);

        getInput();


    }

    private void getInput() {
        String inputString = scanner.nextLine();
        try {
            CommandLine commandLine = cliParser.retrieveCommandLine(inputString);
            if(commandLine.hasOption("status")){
                showStatus();
            }
            if(commandLine.hasOption("help")){
                cliPrinter.printHelp(matchOptions);
            }
            if(commandLine.hasOption("elect")){
                List<String> args = Arrays.asList(commandLine.getOptionValues("elect"));
                System.out.println(args);
                System.out.println(Arrays.toString(commandLine.getOptionValues("elect")));
                getElectCouncilorArgs(args);
            }
            if(commandLine.hasOption("politicColor")){
                System.out.println(clientController.getSnapshot().getBank().showCouncilor());
            }
            if(commandLine.hasOption("permit")){
                showPermitCard();
            }
            if(commandLine.hasOption("finish")){
                clientController.onFinishTurn();
            }
            if(commandLine.hasOption("changePermit")){
                String arg = commandLine.getOptionValue("changePermit");
                changePermitAction(arg);
            }
            if(commandLine.hasOption("buyAction")){
                Action action = new FastActionNewMainAction();
                clientController.doAction(action);
            }
            getInput();
        } catch (ParseException e) {
            System.out.println("Syntax error!");
            getInput();
        }
    }

    private void changePermitAction(String arg) {
        try {
            RegionName regionName = RegionName.valueOf(arg);
            Action action = new FastActionChangePermitCardWithHelper(regionName);
            clientController.doAction(action);
        }
        catch (Exception e){
            System.out.println("Error in region Name");
        }

    }

    private void showPermitCard() {
        for(RegionName regionName: RegionName.values()) {
            System.out.println(CLIColor.ANSI_RED+" "+regionName+" "+CLIColor.ANSI_RESET);
            for (PermitCard permitCard: clientController.getSnapshot().getVisibleRegionPermitCard(regionName)){
                System.out.println(cliPrinter.toStringFormatted(permitCard));
            }
        }
    }

    private void getElectCouncilorArgs(List<String> args) {
        switch (args.get(0).toLowerCase()){
            case "money":
                if(clientController.getSnapshot().getBank().showCouncilor().contains(PoliticColor.valueOf(args.get(1).toUpperCase()))) {
                    if(!checkValidRegion(args.get(2))){
                        if(args.get(2).equalsIgnoreCase("king")) {
                            try {
                                Councilor councilor = new Councilor(PoliticColor.valueOf(args.get(1)));
                                Action action = new MainActionElectCouncilor(councilor, clientController.getSnapshot().getKing(), null);
                                clientController.doAction(action);
                            }catch (Exception e){
                                System.out.println("Incorrect color");
                            }
                        }
                        else{
                            System.out.println("Incorrect region type");
                        }
                    }
                    else{
                        Action action = new MainActionElectCouncilor(new Councilor(PoliticColor.valueOf(args.get(1))),null, RegionName.valueOf(args.get(2)));
                        clientController.doAction(action);
                    }
                }
                else{
                    System.out.println("Color not available");
                }
        }

    }

    private boolean checkValidRegion(String s) {

        try {
            RegionName.valueOf(s);
        }
        catch (Exception e){
            return false;
        }
            return true;

    }

    private void showStatus() {
        CurrentUser currentUser = clientController.getSnapshot().getCurrentUser();
        System.out.println(ANSI_RED+"STATUS: \n"+ANSI_RESET);
        System.out.println("Politic Card: \n");
        for (PoliticCard politicCard: currentUser.getPoliticCards()){
            System.out.println("Carta politica: "+cliPrinter.toStringFormatted(politicCard)+" \n");
        }

        System.out.println("Permit Card\n");
        for (PermitCard permitCard: currentUser.getPermitCards()){
            System.out.println("Carta permesso: "+cliPrinter.toStringFormatted(permitCard));
        }

        System.out.println("Posizioni: \n");

        System.out.println("\nNobility Path: "+currentUser.getNobilityPathPosition().getPosition());
        System.out.println("\nMoney Path: "+currentUser.getCoinPathPosition());
        System.out.println("\nNobility Path: "+currentUser.getNobilityPathPosition().getPosition());

        System.out.println("\n Città: \n");

        for (City city: currentUser.getUsersEmporium()){
            System.out.println(cliPrinter.toStringFormatted(city));
        }

        System.out.println("Azioni Principali:  "+currentUser.getMainActionCounter());
        System.out.println("Azioni veloci :"+currentUser.getFastActionCounter());
        System.out.println("Aiutanti : "+currentUser.getHelpers().size());


        System.out.println("Fine status\n ");
    }

    public void finishTurn(){
        isYourTurn=false;
        System.out.println(ANSI_RED+"Turno finito"+ANSI_RESET);
    }
}
