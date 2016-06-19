package ClientPackage.View.CLIResources;

import org.apache.commons.cli.Options;

/**
 * Created by Emanuele on 19/06/2016.
 */
public class OptionsClass {

    public static Options constructOptions(){
        final Options standardOptions = new Options();
        standardOptions.addOption("status",false,"Display game status");
        standardOptions.addOption("login",true,"Do login, param is login name");
        standardOptions.addOption("help",false,"Show help");
        return standardOptions;
    }
}
