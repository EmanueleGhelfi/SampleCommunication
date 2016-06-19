package ClientPackage.View.CLIResources;

import ClientPackage.View.GeneralView.CLIView;
import org.apache.commons.cli.*;

/**
 * Created by Emanuele on 19/06/2016.
 */
public class CLIParser {

    private static CLIParser cliParserInstance;
    private Options currentOption;

    public CLIParser(Options options){
        currentOption = options;
    }

    public void parseLine(String line, CLIView cliView){
        String[] strings = line.split(" ");
        System.out.println("parsed" + strings.length);
        CommandLineParser commandLineParser = new DefaultParser();
        try {
            CommandLine commandLine = commandLineParser.parse(currentOption,strings);
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
            System.err.println( "Parsing failed." );
            cliView.initView();
        }


    }

    public CommandLine retrieveCommandLine(String line) throws ParseException {
        String[] strings = line.split(" ");
        System.out.println("parsed" + strings.length);
        CommandLineParser commandLineParser = new DefaultParser();
            CommandLine commandLine = commandLineParser.parse(currentOption, strings);
            return commandLine;
    }

}
