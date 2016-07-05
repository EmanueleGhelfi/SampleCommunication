package ClientPackage.View.CLIResources;

import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import CommonModel.GameModel.City.City;
import CommonModel.GameModel.Council.Councilor;
import CommonModel.GameModel.Council.Helper;
import CommonModel.GameModel.Market.BuyableWrapper;
import CommonModel.Snapshot.BaseUser;
import CommonModel.Snapshot.SnapshotToSend;
import Server.Model.Link;
import Server.Model.Map;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import static ClientPackage.View.CLIResources.CLIColor.*;

/**
 * Created by Emanuele on 19/06/2016.
 */
public class CLIPrinter implements CLIPrinterInterface {

    public static CLIPrinter cliPrinter;

    private static CLIPrinter getInstance() {
        if (CLIPrinter.cliPrinter == null) {
            CLIPrinter.cliPrinter = new CLIPrinter();
        }
        return CLIPrinter.cliPrinter;
    }


    /**
     * Print usage information to provided OutputStream.
     *
     * @param applicationName Name of application to list in usage.
     * @param options         Command-line options to be part of usage.
     * @param out             OutputStream to which to write the usage information.
     */
    public static void printUsage(
            String applicationName,
            Options options,
            OutputStream out) {
        PrintWriter writer = new PrintWriter(out, true);
        HelpFormatter usageFormatter = new HelpFormatter();
        usageFormatter.printUsage(writer, 80, applicationName, options);
    }

    /**
     * Write "help" to the provided OutputStream.
     */
    public static void printHelp(
            Options options,
            int printedRowWidth,
            String header,
            String footer,
            int spacesBeforeOption,
            int spacesBeforeOptionDescription,
            boolean displayUsage,
            OutputStream out) {
        String commandLineSyntax = "Welcome to Council of four!";
        PrintWriter writer = new PrintWriter(out, true);
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp(
                writer,
                printedRowWidth,
                commandLineSyntax,
                header,
                options,
                spacesBeforeOption,
                spacesBeforeOptionDescription,
                footer,
                displayUsage);
    }


    @Override
    public void printHelp(Options options) {
        CLIPrinter.printHelp(options, 80, "COFfee", "Select your Option", 0, 0, true, System.out);
    }

    @Override
    public void printUsage() {

    }

    @Override
    public String toStringFormatted(Map map) {
        String toReturn = "Map " + map.getMapName();
        toReturn += ANSI_BLUE + "Cities: \n" + ANSI_RESET;
        for (City city : map.getCity()) {
            toReturn += "City: " + ANSI_RED + city.getCityName() + ANSI_RESET + "  Color: " + city.getColor().getColor() + "\n";
        }
        toReturn += ANSI_BLUE + "Links: \n" + ANSI_RESET;
        for (Link link : map.getLinks()) {
            toReturn += "" + link + "\n";
        }
        toReturn += "END MAP";
        return toReturn;
    }

    @Override
    public String toStringFormatted(SnapshotToSend snapshotToSend) {
        String toReturn = "";
        toReturn += "Users: \n";
        for (BaseUser baseUser : snapshotToSend.getUsersInGame().values()) {
            toReturn += baseUser.toString();
        }
        return toReturn;
    }

    @Override
    public String toStringFormatted(PoliticCard politicCard) {
        if (politicCard.isMultiColor()) {
            return "Multicolor";
        } else {
            return politicCard.getPoliticColor().getColor();
        }

    }

    @Override
    public String toStringFormatted(PermitCard permitCard) {
        return permitCard.toString();

    }

    @Override
    public String toStringFormatted(City city) {

        return String.format("%-10s %-10s %-40s", city.getCityName(), city.getRegion(), city.getColor());
    }

    @Override
    public void printBlue(String toPrint) {
        System.out.println(ANSI_BLUE + toPrint + ANSI_RESET);
    }

    @Override
    public void printError(String toPrint) {
        System.out.println("------------------------------------------------------------------");
        System.out.println(ANSI_RED + toPrint + ANSI_RESET);
        System.out.println("------------------------------------------------------------------");
    }

    @Override
    public void printCouncil(ArrayList<Councilor> council) {
        for (Councilor councilor : council) {
            System.out.println(" Councilor: " + councilor.getColor());
        }
    }

    @Override
    public String toStringFormatted(BuyableWrapper buyableWrapper) {
        if (buyableWrapper.getBuyableObject() instanceof PermitCard) {
            return " Permit Card " + ((PermitCard) buyableWrapper.getBuyableObject()).getCityAcronimous() + " "
                    + " from user: " + buyableWrapper.getUsername() + " cost: " + buyableWrapper.getCost() + " onSale: " + buyableWrapper.isOnSale();
        }
        if (buyableWrapper.getBuyableObject() instanceof Helper) {
            return " Helper " + "from user: " + buyableWrapper.getUsername() + " cost: " + buyableWrapper.getCost() + " onSale: " + buyableWrapper.isOnSale();
        }

        if (buyableWrapper.getBuyableObject() instanceof PoliticCard) {
            return " Politic Card " + this.toStringFormatted((PoliticCard) buyableWrapper.getBuyableObject()) +
                    " from user: " + buyableWrapper.getUsername() + " cost: " + buyableWrapper.getCost() + " onSale: " + buyableWrapper.isOnSale();
        }
        return "";
    }

    @Override
    public void printGreen(String s) {
        System.out.println("------------------------------------------------------------------");
        System.out.println(ANSI_YELLOW + s + ANSI_RESET);
        System.out.println("------------------------------------------------------------------");
    }


}
