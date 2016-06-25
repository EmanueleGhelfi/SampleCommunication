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
import CommonModel.GameModel.City.RegionName;
import CommonModel.GameModel.Council.Councilor;
import CommonModel.GameModel.Council.King;
import CommonModel.Snapshot.CurrentUser;
import CommonModel.Snapshot.SnapshotToSend;
import Server.Model.Link;
import Utilities.Class.ArrayUtils;
import Utilities.Exception.CouncilNotFoundException;
import asg.cliche.Command;
import asg.cliche.Param;
import org.apache.commons.cli.Options;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import static ClientPackage.View.CLIResources.CLIColor.*;

/**
 * Manage user input on CommandLine
 */
public class MatchCliController implements CliController  {

    private boolean isMyTurn = false;
    private Options matchOptions;
    private CLIView cliView;
    private ClientController clientController;
    private CLIParser cliParser;
    private CLIPrinter cliPrinter = new CLIPrinter();
    private BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in));
    private SnapshotToSend snapshotToSend;

    public MatchCliController(CLIView cliView, ClientController clientController) {

        this.cliView=cliView;
        this.clientController=clientController;
        this.matchOptions = OptionsClass.createMatchOptions();
        this.clientController = clientController;
        cliParser = new CLIParser(this.getClass());
    }

    public void onGameStart() {
        printHelp();
    }

    public void onYourTurn() {
        snapshotToSend = cliView.getSnapshot();
        isMyTurn=true;
        System.out.println(ANSI_RED+"Turno iniziato"+ANSI_RESET);
        //cliPrinter.printHelp(matchOptions);
    }




    private ArrayList<City> selectKingPath() throws IOException {

        ArrayList<City> cities= new ArrayList<>();
        cliPrinter.printBlue("Select city king: ");

        System.out.println("Città corrente del re: "+ cliPrinter.toStringFormatted(clientController.getSnapshot().getKing().getCurrentCity()));

        cliPrinter.printBlue("Links");

        for(Link link: clientController.getSnapshot().getMap().getLinks()){
            System.out.println(link);
        }

        cliPrinter.printBlue("Cities \t Region \t Color");
        for(City city: clientController.getSnapshot().getMap().getCity()){
            System.out.println(cliPrinter.toStringFormatted(city));
        }

        boolean correct = false;
        String[] selectedArray= {};
        while (!correct && isMyTurn) {
            cliPrinter.printBlue("Inserisci i numeri delle città seguite da uno spazio: ");
            for (int i = 0; i < clientController.getSnapshot().getMap().getCity().size(); i++) {
                System.out.println(" " + i + ". " + cliPrinter.
                        toStringFormatted(clientController.getSnapshot().getMap().getCity().get(i)));
            }

            String selected = scanner.readLine();

            selectedArray = selected.split(" ");
            correct = ArrayUtils.checkInteger(selectedArray,clientController.getSnapshot().getMap().getCity());
        }

            for (int i = 0; i < selectedArray.length; i++) {
                cities.add(clientController.getSnapshot().getMap().getCity().get(i));
            }

            if (!cities.get(0).equals(clientController.getSnapshot().getKing().getCurrentCity())) {
                cities.add(0, clientController.getSnapshot().getKing().getCurrentCity());
            return cities;
        }

        return null;

    }




    private ArrayList<PoliticCard> selectPoliticCard(ArrayList<PoliticCard> politicCards) throws IOException {
        boolean done = false;
        boolean correct = false;
        ArrayList<PoliticCard> politicCardArrayList = new ArrayList<>();
        String[] selectedArray= {};
        while (!done && !correct) {
            cliPrinter.printBlue("Inserisci i numeri delle carte separati da uno spazio: ");
            for (int i = 0; i < politicCards.size(); i++) {
                System.out.println(" " + i + ". " + cliPrinter.toStringFormatted(politicCards.get(i)));
            }

            String selected = scanner.readLine();

            selectedArray = selected.split(" ");
            done= ArrayUtils.checkDuplicate(selectedArray);
            correct = ArrayUtils.checkInteger(selectedArray,politicCards);
        }

        for(int i = 0; i<selectedArray.length;i++){
            System.out.println(" You have selected "+selectedArray[i]);
            politicCardArrayList.add(politicCards.get(Integer.parseInt(selectedArray[i])));
        }

        return politicCardArrayList;

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



    private PermitCard selectPermitCard(ArrayList<PermitCard> permitCards) {
        int scelta = -2;
        while ((scelta<0 || scelta>permitCards.size()) && (scelta!=-1) && isMyTurn){
            try {
                while (!scanner.ready() && isMyTurn) {
                    Thread.sleep(200);
                }
                if (isMyTurn) {
                    scelta = Integer.parseInt(scanner.readLine());

                    if (scelta >= 0 && scelta < permitCards.size()) {

                        return permitCards.get(scelta);
                    }
                }
                else{
                    System.out.println("turno finito");
                }
            }
            catch (Exception e){
                System.out.println("Invalid input!");
            }

        }
        return null;


    }



    @Command (description = "Show permit card", name = "showPermit", abbrev = "sp")
    public void showPermitCard() {
        for(RegionName regionName: RegionName.values()) {
            System.out.println(CLIColor.ANSI_RED+" "+regionName+" "+CLIColor.ANSI_RESET);
            for (PermitCard permitCard: clientController.getSnapshot().getVisibleRegionPermitCard(regionName)){
                System.out.println(cliPrinter.toStringFormatted(permitCard));
            }
        }
    }




    @Command (description = "Show your status", name = "showStatus", abbrev = "st")
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

    @Command(name = "finish",description = "Finish your turn", abbrev = "f")
    public void finishTurn(){
        isMyTurn=false;
        System.out.println(ANSI_RED+"Turno finito"+ANSI_RESET);
        clientController.onFinishTurn();
    }

    @Override
    public void parseLine(String line) {
        cliParser.parseInput(line,this,cliPrinter);
    }

    @Override
    public void changeController() {

    }

    @Override
    public void printHelp() {
        cliParser.printHelp();
    }



    @Command(abbrev = "pc", description = "Show available Politic Color", name = "politicColor")
    public void showPoliticColor(){
        cliPrinter.printBlue("Available Politic Color");
        for(PoliticColor politicColor: clientController.getSnapshot().getBank().showCouncilor()){
            System.out.println(politicColor);
        }
    }

    @Command(abbrev = "svp", name = "visiblePermit", description = "Show visible Permit Card")
    public void permit() {
        cliPrinter.printBlue("Here are Visible Permit Card");
        for(RegionName regionName:RegionName.values()){
            cliPrinter.printBlue("\t "+ regionName);
            for(PermitCard permitCard:clientController.getSnapshot().getVisiblePermitCards().get(regionName)){
                System.out.println(cliPrinter.toStringFormatted(permitCard));
            }
        }
    }


    @Command(description = ""+CLIColor.ANSI_RED+"[MAIN ACTION]"+CLIColor.ANSI_RESET+" Build an empory with king help",
            abbrev = "bk", name = "buildKing")
    public void buildWithKing() {

        cliPrinter.printBlue("Build with King help");

        cliPrinter.printBlue("King's council: ");
        cliPrinter.printCouncil(new ArrayList<Councilor>(clientController.getSnapshot().getKing().getCouncil().getCouncil()));

        ArrayList<PoliticCard> politicCardArrayList = null;
        try {
            politicCardArrayList = selectPoliticCard(clientController.getSnapshot().getCurrentUser().getPoliticCards());
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<City> cities = null;
        try {
            cities = selectKingPath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Action action = new MainActionBuildWithKingHelp(cities,politicCardArrayList);
        clientController.doAction(action);
    }

    @Command (description = ""+CLIColor.ANSI_RED+"[MAIN ACTION]"+CLIColor.ANSI_RESET+"Build an empory with permitCard", name = "buildEmpory", abbrev = "be")
    public void buildEmporium(@Param(name="city", description="The city when you want to build")String city) {

        if(Validator.isValidCity(city)) {
            System.out.println(ANSI_BLUE + " Select a permit card for build in " + city + ": " + ANSI_RESET);
            System.out.println("(-1 for cancel)");
            CurrentUser currentUser = clientController.getSnapshot().getCurrentUser();
            for(int i=0; i< currentUser.getPermitCards().size();i++){
                System.out.println(""+i+". "+cliPrinter.toStringFormatted(currentUser.getPermitCards().get(i)));
            }
            PermitCard permitCard = selectPermitCard(currentUser.getPermitCards());
            City selectedCity = Validator.getCity(city,clientController.getSnapshot().getMap().getCity());
            Action action = new MainActionBuildWithPermitCard(selectedCity,permitCard);
            clientController.doAction(action);
        }
        else{
            cliPrinter.printError("City not valid!");
        }

    }

    @Command (description = ""+CLIColor.ANSI_RED+"[MAIN ACTION]"+CLIColor.ANSI_RESET+" Buy a permit card in selected region", name = "buyPermit", abbrev = "bp")
    public void buyPermit(@Param(name = "Region", description = "region where you want to buy") String region) {
        if(Validator.isValidRegion(region)){
            cliPrinter.printBlue("Select a permit card: ");
            RegionName regionName = Validator.getRegion(region);
            for(PermitCard permitCard: clientController.getSnapshot().getVisibleRegionPermitCard(regionName)){
                System.out.println(" "+clientController.getSnapshot().getVisibleRegionPermitCard(regionName)
                        .indexOf(permitCard)+". "+cliPrinter.toStringFormatted(permitCard));
            }

            CurrentUser currentUser = clientController.getSnapshot().getCurrentUser();
            PermitCard permitCardSelected = selectPermitCard(clientController.getSnapshot().getVisibleRegionPermitCard(regionName));

            if(permitCardSelected!=null) {
                cliPrinter.printBlue("Select politic card for: " + regionName);

                try {
                    cliPrinter.printCouncil(clientController.getSnapshot().getCouncil(regionName));
                    ArrayList<PoliticCard> politicCardArrayList = null;
                    try {
                        politicCardArrayList = selectPoliticCard(currentUser.getPoliticCards());
                    } catch (IOException e) {
                    }
                    Action action = new MainActionBuyPermitCard(politicCardArrayList, regionName, permitCardSelected);
                    clientController.doAction(action);
                } catch (CouncilNotFoundException e) {
                    e.printStackTrace();
                }
            }

            else{
                if(isMyTurn) {
                    cliPrinter.printError("Sorry, invalid selection!");
                    buyPermit(region);
                }
                else{

                    System.out.println("finished turn");
                }

            }


        }
    }


    @Command (description = ""+CLIColor.ANSI_RED+"[FAST OR MAIN ACTION]"+CLIColor.ANSI_RESET+"Elect councilor", name = "electCouncilor", abbrev = "ec")
    public void getElectCouncilorArgs(@Param(name = "type", description = "helper or money") String type,
                                      @Param(name = "region", description = "King or region name") String region,
                                      @Param(name = "Politic Color", description = "Color of councilor that you want to elect") String color) {


        PoliticColor selectedPoliticColor = null;
        RegionName regionName = null;
        King king=null;

        for(PoliticColor politicColor: PoliticColor.values()){
            if(politicColor.getColor().equalsIgnoreCase(color)){
                selectedPoliticColor=politicColor;
            }
        }

        if(!checkValidRegion(region)){
            if(region.equalsIgnoreCase("king")){
                king=clientController.getSnapshot().getKing();
            }
        }
        else{
            for(RegionName regionName1: RegionName.values()){
                if(regionName1.getRegion().equalsIgnoreCase(region.toLowerCase())){
                    regionName=regionName1;
                }
            }
        }

        if((king!=null || regionName!=null)&& selectedPoliticColor!=null) {
            Action action = null;
            switch (type.toLowerCase()) {
                case "helper":
                    action = new FastActionElectCouncilorWithHelper(regionName, king, new Councilor(selectedPoliticColor), "");
                    break;
                case "money":
                    action = new MainActionElectCouncilor(new Councilor(selectedPoliticColor), king, regionName);
            }
            if(action!=null)
                clientController.doAction(action);
            else
                cliPrinter.printError("ERROR");

        }
        else
            cliPrinter.printError("SYNTAX ERROR");


    }


    @Command(abbrev = "bh", description = ""+CLIColor.ANSI_RED+"[FAST ACTION]"+CLIColor.ANSI_RESET+"Buy helper for money", name = "buyHelper")
    public void buyHelper(){
        Action action = new FastActionMoneyForHelper();
        clientController.doAction(action);
    }


    @Command(abbrev = "ba", description = ""+CLIColor.ANSI_RED+"[FAST ACTION]"+CLIColor.ANSI_RESET+"Buy a Main action for money", name = "buyAction")
    public void buyMainAction(){
        Action action = new FastActionNewMainAction();
        clientController.doAction(action);
    }


    @Command (description = ""+CLIColor.ANSI_RED+"[FAST ACTION]"+CLIColor.ANSI_RESET+"Change permit card", name = "changePermit", abbrev = "cp")
    public void changePermitAction(@Param(name="region", description="Region of the permit card that you want to change")String arg) {
        try {
            RegionName regionName = RegionName.valueOf(arg);
            Action action = new FastActionChangePermitCardWithHelper(regionName);
            clientController.doAction(action);
        }
        catch (Exception e){
            System.out.println("Error in region Name");
        }

    }


    public void onFinisTurn() {
        isMyTurn=false;
    }
}
