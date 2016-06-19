package ClientPackage.View.CLIResources;

import ClientPackage.View.GeneralView.CLIView;
import javafx.util.Callback;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import sun.plugin2.jvm.RemoteJVMLauncher;

/**
 * Created by Emanuele on 19/06/2016.
 */
public class CLIParser {

    private static CLIParser cliParserInstance;

    private CLIParser(){

    }

    public static CLIParser getInstance() {
        if(cliParserInstance==null){
            cliParserInstance= new CLIParser();
        }
        return cliParserInstance;
    }

    public void parseLine(String line, CLIView cliView){
        String[] strings = line.split(" ");
        System.out.println("parsed" + strings.length);
        CommandLineParser commandLineParser = new DefaultParser();
        try {
            CommandLine commandLine = commandLineParser.parse(OptionsClass.constructOptions(),strings);
            if(commandLine.hasOption("status")) {
                cliView.showStatus();
            }
            if(commandLine.hasOption("login")){
                System.out.println("On login");
                cliView.onLogin(commandLine.getOptionValue("login"));
            }
            if(commandLine.hasOption("help")){
                cliView.printHelp();
            }
        } catch (ParseException e) {
            // oops, something went wrong
            System.err.println( "Parsing failed.  Reason: " + e.getMessage() );
        }


    }
}
