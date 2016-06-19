package ClientPackage.View.CLIResources;

import Server.Model.Map;

/**
 * Created by Emanuele on 19/06/2016.
 */
public interface CLIPrinterInterface {

    void printHelp();
    void printUsage();

    String toStringFormatted(Map map);
}
