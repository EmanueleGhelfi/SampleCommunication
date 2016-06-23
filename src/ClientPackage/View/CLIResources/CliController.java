package ClientPackage.View.CLIResources;

/**
 * Created by Emanuele on 21/06/2016.
 */
public interface CliController {

    void parseLine(String line);
    void changeController();

    void printHelp();
}
