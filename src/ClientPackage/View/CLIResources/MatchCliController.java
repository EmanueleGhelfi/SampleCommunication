package ClientPackage.View.CLIResources;

/**
 * Created by Emanuele on 13/05/2016.
 */

import ClientPackage.Controller.ClientController;
import ClientPackage.View.GeneralView.CLIView;
import CommonModel.GameModel.Action.*;
import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticColor;
import CommonModel.GameModel.City.City;
import CommonModel.GameModel.City.Region;
import CommonModel.GameModel.City.RegionName;
import CommonModel.GameModel.Council.Council;
import CommonModel.GameModel.Council.Councilor;
import CommonModel.Snapshot.CurrentUser;
import CommonModel.Snapshot.SnapshotToSend;
import Server.Model.Link;
import Utilities.Exception.CouncilNotFoundException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import sun.util.resources.cldr.ebu.CurrencyNames_ebu;

import java.util.*;
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

            if(commandLine.hasOption("buyHelper")){
                Action action = new FastActionMoneyForHelper();
                clientController.doAction(action);
            }

            if(commandLine.hasOption("buildEmporium")){
                buildEmporium(commandLine.getOptionValues("buildEmporium"));
            }

            if(commandLine.hasOption("buyPermit")){
                buyPermit(commandLine.getOptionValues("buyPermit"));
            }

            if(commandLine.hasOption("buildKing")){
                buildWithKing();
            }


            getInput();
        } catch (ParseException e) {
            System.out.println("Syntax error!");
            getInput();
        }
    }

    private void buildWithKing() {

        cliPrinter.printBlue("Build with King help");

        cliPrinter.printBlue("King's council: ");
        cliPrinter.printCouncil(new ArrayList<Councilor>(clientController.getSnapshot().getKing().getCouncil().getCouncil()));

        ArrayList<PoliticCard> politicCardArrayList = selectPoliticCard(clientController.getSnapshot().getCurrentUser().getPoliticCards());

        ArrayList<City> cities = selectKingPath();

        Action action = new MainActionBuildWithKingHelp(cities,politicCardArrayList);
        clientController.doAction(action);
    }

    private ArrayList<City> selectKingPath() {

        ArrayList<City> cities= new ArrayList<>();
        cliPrinter.printBlue("Select city king: ");

        System.out.println("Città corrente del re: "+ cliPrinter.toStringFormatted(clientController.getSnapshot().getKing().getCurrentCity()));

        cliPrinter.printBlue("Links");

        for(Link link: clientController.getSnapshot().getMap().getLinks()){
            System.out.println(link);
        }

        cliPrinter.printBlue("Cities");
        for(City city: clientController.getSnapshot().getMap().getCity()){
            cliPrinter.printBlue(cliPrinter.toStringFormatted(city));
        }

        boolean correct = false;
        String[] selectedArray= {};
        while (!correct) {
            cliPrinter.printBlue("Inserisci i numeri delle città seguite da uno spazio: ");
            for (int i = 0; i < clientController.getSnapshot().getMap().getCity().size(); i++) {
                System.out.println(" " + i + ". " + cliPrinter.
                        toStringFormatted(clientController.getSnapshot().getMap().getCity().get(i)));
            }

            String selected = scanner.nextLine();

            selectedArray = selected.split(" ");
            correct = checkInteger(selectedArray,clientController.getSnapshot().getMap().getCity());
        }

        for(int i = 0; i<selectedArray.length;i++){
            cities.add(clientController.getSnapshot().getMap().getCity().get(i));
        }

        if(!cities.get(0).equals(clientController.getSnapshot().getKing().getCurrentCity())){
            cities.add(0,clientController.getSnapshot().getKing().getCurrentCity());
        }
        return cities;


    }


    private void buyPermit(String[] buyPermits) {
        if(buyPermits.length>1 && Validator.isValidRegion(buyPermits[0])){
            cliPrinter.printBlue("Select a permit card: ");
            RegionName regionName = RegionName.valueOf(buyPermits[0]);
            for(PermitCard permitCard: clientController.getSnapshot().getVisibleRegionPermitCard(regionName)){
                System.out.println(" "+clientController.getSnapshot().getVisibleRegionPermitCard(regionName)
                        .indexOf(permitCard)+". "+cliPrinter.toStringFormatted(permitCard));
            }

            CurrentUser currentUser = clientController.getSnapshot().getCurrentUser();
            PermitCard permitCardSelected = selectPermitCard(clientController.getSnapshot().getVisibleRegionPermitCard(regionName));

            cliPrinter.printBlue("Select politic card for: "+regionName);

            try {
                cliPrinter.printCouncil(clientController.getSnapshot().getCouncil(regionName));
                ArrayList<PoliticCard> politicCardArrayList = selectPoliticCard(currentUser.getPoliticCards());
                Action action = new MainActionBuyPermitCard(politicCardArrayList,regionName,permitCardSelected);
                clientController.doAction(action);
            } catch (CouncilNotFoundException e) {
                e.printStackTrace();
            }


        }
    }

    private ArrayList<PoliticCard> selectPoliticCard(ArrayList<PoliticCard> politicCards) {
        boolean done = false;
        boolean correct = false;
        ArrayList<PoliticCard> politicCardArrayList = new ArrayList<>();
        String[] selectedArray= {};
        while (!done && !correct) {
            cliPrinter.printBlue("Inserisci i numeri delle carte separati da uno spazio: ");
            for (int i = 0; i < politicCards.size(); i++) {
                System.out.println(" " + i + ". " + cliPrinter.toStringFormatted(politicCards.get(i)));
            }

            String selected = scanner.nextLine();

            selectedArray = selected.split(" ");
            done= checkDuplicate(selectedArray);
            correct = checkInteger(selectedArray,politicCards);
        }

        for(int i = 0; i<selectedArray.length;i++){
            politicCardArrayList.add(politicCards.get(i));
        }
        return politicCardArrayList;

    }

    private boolean checkInteger(String[] selectedArray, ArrayList arrayList) {
        for(String string: selectedArray){
            try {
                int integer = Integer.parseInt(string);
                if(integer<0 || integer>=arrayList.size()){
                    return false;
                }
            }
            catch (Exception e){
                return false;
            }
        }
        return true;
    }

    private boolean checkDuplicate(String[] selectedArray) {
        //set does not allow duplicate
        Set<String> set = new HashSet<>();
        for (String s: selectedArray){

            if(!set.add(s)){
                return false;
            }
        }
        return true;
    }

    private PermitCard selectPermitCard(ArrayList<PermitCard> permitCards) {
        int scelta = -2;
        while ((scelta<0 || scelta>permitCards.size()) && (scelta!=-1)){
            try{
                scelta=scanner.nextInt();
                if(scelta>0 && scelta< permitCards.size()){

                    return permitCards.get(scelta);
                }
            }
            catch (Exception e){
                System.out.println("Invalid input!");
            }

        }
        return null;


    }

    private void buildEmporium(String[] buildEmporia) {

        if(buildEmporia.length>0 && Validator.isValidCity(buildEmporia[0])) {
            System.out.println(ANSI_BLUE + " Select a permit card for build in " + buildEmporia[0] + ": " + ANSI_RESET);
            System.out.println("(-1 for cancel)");
            CurrentUser currentUser = clientController.getSnapshot().getCurrentUser();
            for(int i=0; i< currentUser.getPermitCards().size();i++){
                System.out.println(""+i+". "+cliPrinter.toStringFormatted(currentUser.getPermitCards().get(i)));
            }
            PermitCard permitCard = selectPermitCard(currentUser.getPermitCards());
            City selectedCity = Validator.getCity(buildEmporia[0],clientController.getSnapshot().getMap().getCity());
            Action action = new MainActionBuildWithPermitCard(selectedCity,permitCard);
            clientController.doAction(action);
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
                        Action action = new MainActionElectCouncilor(new Councilor(PoliticColor.valueOf(args.get(1).toUpperCase())),null, RegionName.valueOf(args.get(2).toUpperCase()));
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
            RegionName.valueOf(s.toUpperCase());
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
