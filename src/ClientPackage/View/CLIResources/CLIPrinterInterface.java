package ClientPackage.View.CLIResources;

import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticCard;
import CommonModel.GameModel.City.City;
import CommonModel.GameModel.Council.Councilor;
import CommonModel.GameModel.Market.BuyableWrapper;
import CommonModel.Snapshot.SnapshotToSend;
import Server.Model.Map;
import org.apache.commons.cli.Options;

import java.util.ArrayList;

/**
 * Created by Emanuele on 19/06/2016.
 */
public interface CLIPrinterInterface {

    void printHelp(Options options);
    void printUsage();

    String toStringFormatted(Map map);

    String toStringFormatted(SnapshotToSend snapshotToSend);

    String toStringFormatted(PoliticCard politicCard);

    String toStringFormatted(PermitCard permitCard);

    String toStringFormatted(City city);

    void printBlue(String toPrint);

    void printError(String toPrint);

    void printCouncil(ArrayList<Councilor> council);

    String toStringFormatted(BuyableWrapper buyableWrapper);

    void printGreen(String s);
}
