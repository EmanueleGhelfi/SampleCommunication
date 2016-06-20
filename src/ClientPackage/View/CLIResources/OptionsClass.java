package ClientPackage.View.CLIResources;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PatternOptionBuilder;

/**
 * Created by Emanuele on 19/06/2016.
 */
public class OptionsClass {

    public static Options constructOptions(){
        final Options standardOptions = new Options();
        standardOptions.addOption("login",true,"Do login, param is login name");
        standardOptions.addOption("help",false,"Show help");
        return standardOptions;
    }

    public static Options getGameOption() {
        final Options standardOptions = new Options();
        standardOptions.addOption("login",true,"Do login, param is login name");
        standardOptions.addOption("help",false,"Show help");
        return standardOptions;

    }

    public static Options createMatchOptions() {
        final Options standardOptions = new Options();
        standardOptions.addOption("status",false,"Show user status");
        standardOptions.addOption("help",false,"Show help");
        standardOptions.addOption("permit",false,"Show Visible Permit Card");
        Option councilorOption = new Option("elect",true,"Elect councilor. First param is money or helper, second" +
                "is politicColor, third is king or region");
        councilorOption.setArgs(3);
        councilorOption.setArgName("Type");
        standardOptions.addOption(councilorOption);
        Option option = new Option("changePermit",true,"Change permit card with helpers, param is permitType");
        option.setArgName("permitType");
        standardOptions.addOption(option);
        standardOptions.addOption("finish",false,"Change Turn");
        standardOptions.addOption("politicColor",false,"Show available politic color");
        standardOptions.addOption("buyAction",false,"Buy main Action for three helper");
        standardOptions.addOption("buyHelper",false,"Buy Helper for three money");
        Option buyEmporiumOption = Option.builder()
                .longOpt("buildEmporium")
                .desc("Build emporium in city. First argument is city name")
                .numberOfArgs(1)
                .argName("City")
                .hasArgs()
                .build();
        standardOptions.addOption(buyEmporiumOption);

        Option buyPermitCard = Option.builder()
                .longOpt("buyPermit")
                .numberOfArgs(1)
                .hasArgs()
                .desc("Buy Permit a permit Card specified by regionName")
                .argName("regionName")
                .build();

        standardOptions.addOption(buyPermitCard);

        return standardOptions;

    }

    public static Options getMarketOptions() {
        final Options standardOptions = new Options();
        standardOptions.addOption("showList",false,"Show Your sell List");
        standardOptions.addOption("help",false,"Show help");
        return standardOptions;


    }
}
