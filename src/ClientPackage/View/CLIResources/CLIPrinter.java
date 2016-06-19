package ClientPackage.View.CLIResources;

import CommonModel.GameModel.City.City;
import Server.Model.Link;
import Server.Model.Map;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import java.io.OutputStream;
import java.io.PrintWriter;
import static ClientPackage.View.CLIResources.CLIColor.*;

/**
 * Created by Emanuele on 19/06/2016.
 */
public class CLIPrinter implements CLIPrinterInterface {

    /**
     * Print usage information to provided OutputStream.
     *
     * @param applicationName Name of application to list in usage.
     * @param options Command-line options to be part of usage.
     * @param out OutputStream to which to write the usage information.
     */
    public static void printUsage(
            final String applicationName,
            final Options options,
            final OutputStream out)
    {
        final PrintWriter writer = new PrintWriter(out,true);
        final HelpFormatter usageFormatter = new HelpFormatter();
        usageFormatter.printUsage(writer, 80, applicationName, options);
    }

    /**
     * Write "help" to the provided OutputStream.
     */
    public static void printHelp(
            final Options options,
            final int printedRowWidth,
            final String header,
            final String footer,
            final int spacesBeforeOption,
            final int spacesBeforeOptionDescription,
            final boolean displayUsage,
            final OutputStream out)
    {
        final String commandLineSyntax = "Welcome to Council of four!";
        final PrintWriter writer = new PrintWriter(out,true);
        final HelpFormatter helpFormatter = new HelpFormatter();
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
    public void printHelp() {
        printHelp(OptionsClass.constructOptions(),80,"COFfee", "Select your Option" , 0, 0, true, System.out);
    }

    @Override
    public void printUsage() {

    }

    @Override
    public String toStringFormatted(Map map) {
        String toReturn ="Map "+map.getMapName();
        toReturn+=CLIColor.ANSI_BLUE+"Cities: \n"+CLIColor.ANSI_RESET;
        for(City city: map.getCity()){
            toReturn+="City: "+CLIColor.ANSI_RED+city.getCityName()+ CLIColor.ANSI_RESET+"  Color: "+city.getColor().getColor()+"\n";
        }
        toReturn+= ANSI_BLUE+"Links: \n"+ANSI_RESET;
        for (Link link: map.getLinks()){
            toReturn+=""+link.toString();
        }
        toReturn+="END MAP";
        return toReturn;
    }


}
