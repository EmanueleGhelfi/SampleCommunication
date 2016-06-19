package ClientPackage.View.CLIResources;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

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
        standardOptions.addOption("permit",false,"Show Permit Card");
        Option councilorOption = new Option("elect",true,"Elect councilor. First param is money or helper, second" +
                "is politicColor, third is king or region");
        councilorOption.setArgs(3);
        councilorOption.setArgName("Type");
        standardOptions.addOption(councilorOption);
        Option option = new Option("changePermit",true,"Change permit card, param is permitType");
        option.setArgName("permitType");
        standardOptions.addOption(option);
        standardOptions.addOption("finish",false,"Change Turn");
        standardOptions.addOption("politicColor",false,"Show available color");

        return standardOptions;

    }
}
