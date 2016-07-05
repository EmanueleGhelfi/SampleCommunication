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
import CommonModel.GameModel.Path.Position;
import CommonModel.Snapshot.BaseUser;
import CommonModel.Snapshot.CurrentUser;
import CommonModel.Snapshot.SnapshotToSend;
import Server.Model.FakeUser;
import Server.Model.Link;
import Utilities.Class.ArrayUtils;
import Utilities.Class.Constants;
import Utilities.Class.TableBuilder;
import Utilities.Exception.CouncilNotFoundException;
import asg.cliche.Command;
import asg.cliche.Param;
import org.apache.commons.cli.Options;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static ClientPackage.View.CLIResources.CLIColor.*;

/**
 * Manage user input on CommandLine
 */
public class MatchCliController implements CliController {

    private boolean isMyTurn;
    private final Options matchOptions;
    private final CLIView cliView;
    private ClientController clientController;
    private final CLIParser cliParser;
    private final CLIPrinter cliPrinter = new CLIPrinter();
    private final BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in));
    private SnapshotToSend snapshotToSend;

    public MatchCliController(CLIView cliView, ClientController clientController) {

        this.cliView = cliView;
        this.clientController = clientController;
        matchOptions = OptionsClass.createMatchOptions();
        this.clientController = clientController;
        this.cliParser = new CLIParser(getClass());
    }

    public void onGameStart() {
        this.printHelp();
    }

    public void onYourTurn() {
        this.snapshotToSend = this.cliView.getSnapshot();
        this.isMyTurn = true;
        System.out.println(ANSI_RED + "Turno iniziato" + ANSI_RESET);
        //cliPrinter.printHelp(matchOptions);
    }


    private ArrayList<City> selectKingPath() throws IOException {

        ArrayList<City> cities = new ArrayList<>();
        this.cliPrinter.printBlue("Select city king: ");

        System.out.println("Città corrente del re: " + this.cliPrinter.toStringFormatted(this.clientController.getSnapshot().getKing().getCurrentCity()));

        this.cliPrinter.printBlue("Links");

        for (Link link : this.clientController.getSnapshot().getMap().getLinks()) {
            System.out.println(link);
        }

        this.cliPrinter.printBlue("Cities \t\t\t\t\t\t Region \t\t\t\t\t\t Color");
        for (City city : this.clientController.getSnapshot().getMap().getCity()) {
            System.out.println(this.cliPrinter.toStringFormatted(city));
        }

        boolean correct = false;
        String[] selectedArray = {};
        //Object[][] citiesToPrint = new Object[clientController.getSnapshot().getMap().getCity().size()][10];
        while (!correct && this.isMyTurn) {
            this.cliPrinter.printBlue("Inserisci i numeri delle città seguite da uno spazio: ");
            TableBuilder tableBuilder = new TableBuilder();
            tableBuilder.addRow("Number ", "CITY", "COLOR", "BONUS");
            tableBuilder.addRow("-------", "--------", "-----------", "----------------");
            for (int i = 0; i < this.clientController.getSnapshot().getMap().getCity().size(); i++) {
                System.out.println(" " + i + ". \t\t\t\t" + this.cliPrinter.
                        toStringFormatted(this.clientController.getSnapshot().getMap().getCity().get(i)));

            }

            while (!this.scanner.ready() && this.isMyTurn) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                }
            }
            if (this.isMyTurn) {
                String selected = this.scanner.readLine();

                selectedArray = selected.split(" ");
                correct = ArrayUtils.checkInteger(selectedArray, this.clientController.getSnapshot().getMap().getCity());
            }
        }

        if (this.isMyTurn) {
            for (int i = 0; i < selectedArray.length; i++) {
                cities.add(this.clientController.getSnapshot().getMap().getCity().get(Integer.parseInt(selectedArray[i])));
            }

            if (!cities.get(0).getCityName().equals(this.clientController.getSnapshot().getKing().getCurrentCity().getCityName())) {
                cities.add(0, this.clientController.getSnapshot().getKing().getCurrentCity());
            }

            return cities;
        }

        return null;

    }


    private void addCity(Object[][] citiesToPrint, int i, City city) {
        citiesToPrint[i] = new String[]{city.getCityName().getCityName(), city.getColor().getColor(), city.getBonus().toString()};
    }


    private ArrayList<PoliticCard> selectPoliticCard(ArrayList<PoliticCard> politicCards) throws IOException {
        boolean done = false;
        boolean correct = false;
        ArrayList<PoliticCard> politicCardArrayList = new ArrayList<>();
        String[] selectedArray = {};
        while (!done && !correct && this.isMyTurn) {
            this.cliPrinter.printBlue("Inserisci i numeri delle carte separati da uno spazio: ");
            for (int i = 0; i < politicCards.size(); i++) {
                System.out.println(" " + i + ". " + this.cliPrinter.toStringFormatted(politicCards.get(i)));
            }

            while (!this.scanner.ready() && this.isMyTurn) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                }
            }
            if (this.isMyTurn) {
                String selected = this.scanner.readLine();

                selectedArray = selected.split(" ");
                done = ArrayUtils.checkDuplicate(selectedArray);
                correct = ArrayUtils.checkInteger(selectedArray, politicCards);
            }
        }

        if (this.isMyTurn) {
            for (int i = 0; i < selectedArray.length; i++) {
                System.out.println("You have selected " + this.cliPrinter.toStringFormatted(politicCards.get(Integer.parseInt(selectedArray[i]))));
                politicCardArrayList.add(politicCards.get(Integer.parseInt(selectedArray[i])));
            }
            return politicCardArrayList;
        }
        return null;

    }

    private boolean checkValidRegion(String s) {

        try {
            RegionName.valueOf(s.toUpperCase());
        } catch (Exception e) {
            return false;
        }
        return true;

    }


    private PermitCard selectPermitCard(ArrayList<PermitCard> permitCards) {
        int scelta = -2;
        while ((scelta < 0 || scelta > permitCards.size()) && scelta != -1 && this.isMyTurn) {
            try {
                while (!this.scanner.ready() && this.isMyTurn) {
                    Thread.sleep(200);
                }
                if (this.isMyTurn) {
                    scelta = Integer.parseInt(this.scanner.readLine());

                    if (scelta >= 0 && scelta < permitCards.size()) {

                        return permitCards.get(scelta);
                    }
                } else {
                    System.out.println("turno finito");
                }
            } catch (Exception e) {
                System.out.println("Invalid input!");
            }

        }
        return null;


    }


    @Command(description = "Show permit card", name = "showPermit", abbrev = "sp")
    public void showPermitCard() {
        for (RegionName regionName : RegionName.values()) {
            System.out.println(ANSI_RED + " " + regionName + " " + ANSI_RESET);
            for (PermitCard permitCard : this.clientController.getSnapshot().getVisibleRegionPermitCard(regionName)) {
                System.out.println(this.cliPrinter.toStringFormatted(permitCard));
            }
        }
    }


    @Command(description = "Show your status", name = "showStatus", abbrev = "st")
    public void showStatus() {
        System.out.println("-----------------------------------------------------------------");
        CurrentUser currentUser = this.clientController.getSnapshot().getCurrentUser();
        System.out.println(ANSI_RED + "STATUS: \n" + ANSI_RESET);
        this.cliPrinter.printBlue("Politic Card: ");
        for (PoliticCard politicCard : currentUser.getPoliticCards()) {
            System.out.println("\t" + this.cliPrinter.toStringFormatted(politicCard) + "");
        }

        this.cliPrinter.printBlue("Permit Card:\n");
        for (PermitCard permitCard : currentUser.getPermitCards()) {
            System.out.println("\t" + this.cliPrinter.toStringFormatted(permitCard));
        }

        this.cliPrinter.printBlue("Posizioni:");
        System.out.println("\t Nobility Path: " + currentUser.getNobilityPathPosition().getPosition());
        System.out.println("\t Money Path: " + currentUser.getCoinPathPosition());
        System.out.println("\t Nobility Path: " + currentUser.getNobilityPathPosition().getPosition());
        System.out.println("Aiutanti : " + currentUser.getHelpers().size());

        System.out.println("Le tue Città:");

        for (City city : currentUser.getUsersEmporium()) {
            System.out.println("\t " + this.cliPrinter.toStringFormatted(city));
        }

        this.cliPrinter.printBlue("Le tue azioni: ");
        System.out.println("Azioni Principali:  " + currentUser.getMainActionCounter());
        System.out.println("Azioni veloci :" + currentUser.getFastActionCounter());

        System.out.println("Fine status\n ");
        System.out.println("-----------------------------------------------------------------");
    }

    @Command(name = "finish", description = "Finish your turn", abbrev = "f")
    public void finishTurn() {
        this.isMyTurn = false;
        this.clientController.onFinishTurn();
    }

    @Override
    public void parseLine(String line) {
        this.cliParser.parseInput(line, this, this.cliPrinter);
    }

    @Override
    public void changeController() {

    }

    @Override
    public void printHelp() {
        this.cliParser.printHelp();
    }


    @Command(abbrev = "pc", description = "Show available Politic Color", name = "politicColor")
    public void showPoliticColor() {
        this.cliPrinter.printBlue("Available Politic Color");
        for (PoliticColor politicColor : this.clientController.getSnapshot().getBank().showCouncilor()) {
            System.out.println(politicColor);
        }
    }

    @Command(abbrev = "svp", name = "visiblePermit", description = "Show visible Permit Card")
    public void permit() {
        this.cliPrinter.printBlue("Here are Visible Permit Card");
        for (RegionName regionName : RegionName.values()) {
            this.cliPrinter.printBlue("\t " + regionName);
            for (PermitCard permitCard : this.clientController.getSnapshot().getVisiblePermitCards().get(regionName)) {
                System.out.println(this.cliPrinter.toStringFormatted(permitCard));
            }
        }
    }


    @Command(description = "" + ANSI_RED + "|MAIN ACTION|" + ANSI_RESET + " Build an empory with king help",
            abbrev = "bk", name = "buildKing")
    public void buildWithKing() {

        this.cliPrinter.printBlue("Build with King help");

        this.cliPrinter.printBlue("King's council: ");
        this.cliPrinter.printCouncil(new ArrayList<Councilor>(this.clientController.getSnapshot().getKing().getCouncil().getCouncil()));

        ArrayList<PoliticCard> politicCardArrayList = null;
        try {
            politicCardArrayList = this.selectPoliticCard(this.clientController.getSnapshot().getCurrentUser().getPoliticCards());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (this.isMyTurn) {
            ArrayList<City> cities = null;
            try {
                cities = this.selectKingPath();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (cities != null && this.isMyTurn) {
                System.out.println(politicCardArrayList + " " + cities);
                Action action = new MainActionBuildWithKingHelp(cities, politicCardArrayList);
                this.clientController.doAction(action);
            }
        }
    }

    @Command(description = "" + ANSI_RED + "|MAIN ACTION|" + ANSI_RESET + "Build an empory with permitCard", name = "buildEmpory", abbrev = "be")
    public void buildEmporium(@Param(name = "city", description = "The city when you want to build") String city) {

        if (Validator.isValidCity(city)) {
            if (this.clientController.getSnapshot().getCurrentUser().getPermitCards().size() > 0) {
                System.out.println(ANSI_BLUE + " Select a permit card for build in " + city + ": " + ANSI_RESET);
                System.out.println("(-1 for cancel)");
                CurrentUser currentUser = this.clientController.getSnapshot().getCurrentUser();
                for (int i = 0; i < currentUser.getPermitCards().size(); i++) {
                    System.out.println("" + i + ". " + this.cliPrinter.toStringFormatted(currentUser.getPermitCards().get(i)));
                }
                PermitCard permitCard = this.selectPermitCard(currentUser.getPermitCards());
                if (this.isMyTurn && permitCard != null) {
                    City selectedCity = Validator.getCity(city, this.clientController.getSnapshot().getMap().getCity());
                    Action action = new MainActionBuildWithPermitCard(selectedCity, permitCard);
                    this.clientController.doAction(action);
                }
            } else {
                this.cliPrinter.printError("Sorry, you have 0 permit card, so you can't build an empory!");
            }
        } else {
            this.cliPrinter.printError("City not valid!");
        }

    }

    @Command(description = "" + ANSI_RED + "|MAIN ACTION|" + ANSI_RESET + " Buy a permit card in selected region", name = "buyPermit", abbrev = "bp")
    public void buyPermit(@Param(name = "Region", description = "region where you want to buy") String region) {
        if (Validator.isValidRegion(region)) {
            this.cliPrinter.printBlue("Select a permit card: ");
            RegionName regionName = Validator.getRegion(region);
            for (PermitCard permitCard : this.clientController.getSnapshot().getVisibleRegionPermitCard(regionName)) {
                System.out.println(" " + this.clientController.getSnapshot().getVisibleRegionPermitCard(regionName)
                        .indexOf(permitCard) + ". " + this.cliPrinter.toStringFormatted(permitCard));
            }

            CurrentUser currentUser = this.clientController.getSnapshot().getCurrentUser();
            PermitCard permitCardSelected = this.selectPermitCard(this.clientController.getSnapshot().getVisibleRegionPermitCard(regionName));

            if (permitCardSelected != null) {
                this.cliPrinter.printBlue("Select politic card for: " + regionName);

                try {
                    this.cliPrinter.printCouncil(this.clientController.getSnapshot().getCouncil(regionName));
                    ArrayList<PoliticCard> politicCardArrayList = null;
                    try {
                        politicCardArrayList = this.selectPoliticCard(currentUser.getPoliticCards());
                    } catch (IOException e) {
                    }
                    Action action = new MainActionBuyPermitCard(politicCardArrayList, regionName, permitCardSelected);
                    this.clientController.doAction(action);
                } catch (CouncilNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                if (this.isMyTurn) {
                    this.cliPrinter.printError("Sorry, invalid selection!");
                    this.buyPermit(region);
                } else {

                    System.out.println("finished turn");
                }

            }


        }
    }


    @Command(description = "" + ANSI_RED + "|FAST OR MAIN ACTION|" + ANSI_RESET + "Elect councilor", name = "electCouncilor", abbrev = "ec")
    public void getElectCouncilorArgs(@Param(name = "type", description = "helper or money") String type,
                                      @Param(name = "region", description = "King or region name") String region,
                                      @Param(name = "Politic Color", description = "Color of councilor that you want to elect") String color) {


        PoliticColor selectedPoliticColor = null;
        RegionName regionName = null;
        King king = null;


        for (PoliticColor politicColor : PoliticColor.values()) {
            if (politicColor.getColor().equalsIgnoreCase(color)) {
                selectedPoliticColor = politicColor;
            }
        }

        if (this.clientController.getSnapshot().getBank().showCouncilor().contains(selectedPoliticColor)) {

            if (!this.checkValidRegion(region)) {
                if (region.equalsIgnoreCase("king")) {
                    king = this.clientController.getSnapshot().getKing();
                }
            } else {
                for (RegionName regionName1 : RegionName.values()) {
                    if (regionName1.getRegion().equalsIgnoreCase(region.toLowerCase())) {
                        regionName = regionName1;
                    }
                }
            }

            if ((king != null || regionName != null) && selectedPoliticColor != null) {
                Action action = null;
                switch (type.toLowerCase()) {
                    case "helper":
                        action = new FastActionElectCouncilorWithHelper(regionName, king, new Councilor(selectedPoliticColor), "");
                        break;
                    case "money":
                        action = new MainActionElectCouncilor(new Councilor(selectedPoliticColor), king, regionName);
                }
                if (action != null)
                    this.clientController.doAction(action);
                else
                    this.cliPrinter.printError("ERROR");

            } else
                this.cliPrinter.printError("SYNTAX ERROR");


        } else {
            System.out.println("Councilor not present in back, sorry!");
        }
    }


    @Command(abbrev = "bh", description = "" + ANSI_RED + "|FAST ACTION|" + ANSI_RESET + "Buy helper for money", name = "buyHelper")
    public void buyHelper() {
        Action action = new FastActionMoneyForHelper();
        this.clientController.doAction(action);
    }


    @Command(abbrev = "ba", description = "" + ANSI_RED + "|FAST ACTION|" + ANSI_RESET + "Buy a Main action for" + Constants.HELPER_LIMITATION_NEW_MAIN_ACTION + " Helper", name = "buyAction")
    public void buyMainAction() {
        Action action = new FastActionNewMainAction();
        this.clientController.doAction(action);
    }


    @Command(description = "" + ANSI_RED + "|FAST ACTION|" + ANSI_RESET + "Change permit card", name = "changePermit", abbrev = "cp")
    public void changePermitAction(@Param(name = "region", description = "Region of the permit card that you want to change") String arg) {
        try {
            RegionName regionName = null;

            for (RegionName tmpRegion : RegionName.values()) {
                if (tmpRegion.getRegion().equalsIgnoreCase(arg)) {
                    regionName = tmpRegion;
                }
            }
            if (regionName != null) {
                Action action = new FastActionChangePermitCardWithHelper(regionName);
                this.clientController.doAction(action);
            } else {
                this.cliPrinter.printError("ERROR IN REGION NAME");
            }
        } catch (Exception e) {
            System.out.println("Error in region Name");
        }

    }


    public void onFinisTurn() {
        this.isMyTurn = false;
    }


    @Command(description = "Show nobility Path", name = "showNobility", abbrev = "sn")
    public void showNobility() {
        for (Position position : this.clientController.getSnapshot().getNobilityPathPosition()) {
            System.out.println(position);
        }
    }

    @Command(description = "Show City Bonus", name = "showCity", abbrev = "sc")
    public void showCity() {
        this.cliPrinter.printBlue(String.format("%-30s %-50s", "CITY", "BONUS"));
        for (City city : this.clientController.getSnapshot().getMap().getCity()) {
            String bonusToPrint = "";
            if (city.getBonus() != null) {
                bonusToPrint = String.format("%-50s", city.getBonus().toString());
            }
            System.out.println(String.format("%-30s", city.getCityName()) + bonusToPrint);
        }
    }

    public void selectPermitCard() {

        for (RegionName regionName : RegionName.values()) {
            this.cliPrinter.printBlue("REGIONE" + regionName);
            for (PermitCard permitCard : this.clientController.getSnapshot().getVisibleRegionPermitCard(regionName)) {
                System.out.println(this.clientController.getSnapshot().getVisibleRegionPermitCard(regionName).indexOf(permitCard) +
                        ". " + this.cliPrinter.toStringFormatted(permitCard));
            }
        }

        this.cliPrinter.printBlue("Select a region and a permit card separated by a blank space (like this: mountain 1)");

        try {
            while (!this.scanner.ready() && this.isMyTurn) {
                Thread.sleep(200);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (this.isMyTurn) {
            try {
                String line = this.scanner.readLine();
                String[] parsed = line.split(" ");

                if (parsed.length > 1 && Validator.isValidRegion(parsed[0])) {
                    try {
                        if (Integer.parseInt(parsed[1]) >= 0 && Integer.parseInt(parsed[1]) <= 1) {
                            RegionName regionName = Validator.getRegion(parsed[0]);
                            PermitCard permitCard = this.clientController.getSnapshot().getVisibleRegionPermitCard(regionName)
                                    .get(Integer.parseInt(parsed[1]));
                            this.clientController.onSelectPermitCard(permitCard);
                        } else {
                            this.selectPermitCard();
                        }
                    } catch (Exception e) {
                        this.selectPermitCard();
                    }

                } else {
                    this.selectPermitCard();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public void selectCityRewardBonus() {
        this.cliPrinter.printBlue("You can choose a bonus of you city! Note that you can't choose a victory bonus!");
        if (this.clientController.getSnapshot().getCurrentUser().getUsersEmporium().size() > 0) {
            this.cliPrinter.printBlue("\t\t\tCITY\t\t\t BONUS");
            for (City city : this.clientController.getSnapshot().getCurrentUser().getUsersEmporium()) {
                System.out.println("" + this.clientController.getSnapshot().getCurrentUser().getUsersEmporium().indexOf(city) + ".\t\t\t"
                        + city.getCityName() + "\t\t\t " + city.getBonus());
            }

            System.out.println("Select a city!");

            try {
                while (!this.scanner.ready() && this.isMyTurn) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (this.isMyTurn) {
                    String selected = this.scanner.readLine();
                    try {
                        int index = Integer.parseInt(selected);
                        if (index >= 0 && index < this.clientController.getSnapshot().getCurrentUser().getUsersEmporium().size()) {
                            this.clientController.getCityRewardBonus(this.clientController.getSnapshot().getCurrentUser().getUsersEmporium().get(index));
                        } else {
                            this.selectCityRewardBonus();
                        }
                    } catch (Exception e) {
                        this.selectCityRewardBonus();
                    }


                }
            } catch (IOException e) {

            }
        } else {
            this.cliPrinter.printError("You can't choose a city because you have 0 empory, sorry!");
        }
    }

    public void selectOldPermitCardBonus() {

        this.cliPrinter.printBlue("Select an old permit card bonus!");

        if (this.clientController.getSnapshot().getCurrentUser().getOldPermitCards().size() > 0 ||
                this.clientController.getSnapshot().getCurrentUser().getPermitCards().size() > 0) {

            if (this.clientController.getSnapshot().getCurrentUser().getPermitCards().size() > 0) {
                System.out.println("CURRENT PERMIT CARD:");

                for (PermitCard permitCard : this.clientController.getSnapshot().getCurrentUser().getPermitCards()) {
                    System.out.println(" " + this.clientController.getSnapshot().getCurrentUser().getPermitCards().indexOf(permitCard)
                            + " " + this.cliPrinter.toStringFormatted(permitCard));
                }
            }

            if (this.clientController.getSnapshot().getCurrentUser().getOldPermitCards().size() > 0) {
                System.out.println("OLD PERMIT CARD");
                for (PermitCard permitCard : this.clientController.getSnapshot().getCurrentUser().getOldPermitCards()) {
                    System.out.println(" " + this.clientController.getSnapshot().getCurrentUser().getOldPermitCards().indexOf(permitCard)
                            + " " + this.cliPrinter.toStringFormatted(permitCard));
                }
            }

            System.out.println("Select OLD or CURRENT and permit card's index (like this:" +
                    " old 2 -> means permit card number 2 in old permit card)");

            try {
                while (!this.scanner.ready() && this.isMyTurn) {
                    Thread.sleep(200);
                }
                if (this.isMyTurn) {
                    String selected = this.scanner.readLine();
                    String[] parsed = selected.split(" ");
                    if (parsed.length != 2)
                        this.selectOldPermitCardBonus();
                    else {
                        try {
                            int index = Integer.parseInt(parsed[1]);
                            if (parsed[0].equalsIgnoreCase("old")) {
                                if (index < 0 || index >= this.clientController.getSnapshot().getCurrentUser().getOldPermitCards().size()) {
                                    this.selectOldPermitCardBonus();
                                } else {
                                    this.clientController.onSelectOldPermitCard(this.clientController.getSnapshot().getCurrentUser().getOldPermitCards().get(index));
                                }
                            } else {
                                if (parsed[0].equalsIgnoreCase("current")) {
                                    if (index < 0 || index >= this.clientController.getSnapshot().getCurrentUser().getPermitCards().size()) {
                                        this.selectOldPermitCardBonus();
                                    } else {
                                        this.clientController.onSelectOldPermitCard(this.clientController.getSnapshot().getCurrentUser().getPermitCards().get(index));
                                    }
                                } else {
                                    this.selectOldPermitCardBonus();
                                }
                            }
                        } catch (Exception e) {
                            this.selectOldPermitCardBonus();
                        }
                    }
                }
            } catch (IOException | InterruptedException e) {
            }

        }

    }

    @Command(name = "showEnemy", abbrev = "se", description = "Show other user status")
    public void showUsers() {

        this.cliPrinter.printGreen("YOUR ENEMY:");
        for (BaseUser baseUser : this.clientController.getSnapshot().getUsersInGame().values()) {
            if (!(baseUser instanceof FakeUser)) {
                this.printUser(baseUser);
            }
        }
    }


    public void printUser(BaseUser baseUser) {
        System.out.println("-----------------------------------------------------------------");
        System.out.println(ANSI_RED + "STATUS: " + baseUser.getUsername() + ANSI_RESET);
        this.cliPrinter.printBlue("Politic Card: ");

        this.cliPrinter.printBlue("Permit Card:\n");
        for (PermitCard permitCard : baseUser.getPermitCards()) {
            System.out.println("\t" + this.cliPrinter.toStringFormatted(permitCard));
        }

        this.cliPrinter.printBlue("Posizioni:");
        System.out.println("\t Nobility Path: " + baseUser.getNobilityPathPosition().getPosition());
        System.out.println("\t Money Path: " + baseUser.getCoinPathPosition());
        System.out.println("\t Nobility Path: " + baseUser.getNobilityPathPosition().getPosition());
        System.out.println("Aiutanti : " + baseUser.getHelpers().size());

        System.out.println("Le tue Città:");

        for (City city : baseUser.getUsersEmporium()) {
            System.out.println("\t " + this.cliPrinter.toStringFormatted(city));
        }

        System.out.println("Fine status\n ");
        System.out.println("-----------------------------------------------------------------");
    }
}
