package ClientPackage.View.GeneralView;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.CLIResources.*;
import ClientPackage.View.GUIResources.Class.MatchController;
import CommonModel.GameModel.City.City;
import CommonModel.Snapshot.SnapshotToSend;
import Server.Model.Map;
import Utilities.Exception.ActionNotPossibleException;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Emanuele on 13/05/2016.
 */
//TODO
public class CLIView implements BaseView {

    private Scanner reader = new Scanner(System.in);
    private CLIPrinterInterface cliPrinterInterface = new CLIPrinter();
    private CLIParser cliParser = new CLIParser(this.getClass());
    private ClientController clientController;
    // for showing help
    private boolean first = true;
    private MatchCliController matchCliController;
    private ShopCliController shopCliController;
    private LoginCliController loginCliController;
    private SnapshotToSend currentSnapshot;
    private CliController currentController;
    private AtomicBoolean needToRead = new AtomicBoolean(true);
    private BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));


    public CLIView(ClientController clientController) {
        // to implement
        this.clientController = clientController;
        this.clientController.setBaseView(this);
        matchCliController= new MatchCliController(this,clientController);
        shopCliController = new ShopCliController(this,clientController);
        loginCliController = new LoginCliController(this,clientController);
    }

    @Override
    public void initView() {
        currentController=loginCliController;

        getInput();


    }

    private void getInput() {
        String input="";
        if(first) {
            currentController.printHelp();
            first=false;
        }

        while (true) {
            try {
                while (!bufferedReader.ready() || !needToRead.get()) {
                    Thread.sleep(200);
                }
                input = bufferedReader.readLine();


                currentController.parseLine(input);
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void showLoginError() {
        System.out.println("Name is already used! Insert another name!");
    }

    @Override
    public void showWaitingForStart() {
        System.out.println("Waiting for other player...");
    }

    @Override
    public void showMap(ArrayList<Map> mapArrayList) {
        needToRead.set(false);
        System.out.println("Select map: \n");
        for(Map map : mapArrayList){
            System.out.println(cliPrinterInterface.toStringFormatted(map)+"\n");
        }
        int scelta;
        do{
            scelta=reader.nextInt();
            if(scelta>=0 && scelta<mapArrayList.size()){
                clientController.sendMap(mapArrayList.get(scelta));
            }
        }while (scelta<0 || scelta>=mapArrayList.size());

    }

    @Override
    public void gameInitialization(SnapshotToSend snapshotToSend) {
        System.out.println("Game Started Correctly\n");
        this.currentSnapshot = snapshotToSend;
        currentController = matchCliController;
        matchCliController.onGameStart();
        needToRead.set(true);
        getInput();
    }

    @Override
    public void turnFinished() {
        System.out.println(" "+CLIColor.ANSI_RED+" Turno finito "+CLIColor.ANSI_RESET);

    }

    @Override
    public void isMyTurn(SnapshotToSend snapshot) {
        System.out.println("Is your turn, make you choice: ");
        matchCliController.onYourTurn();
    }

    @Override
    public void updateSnapshot() {
        //cliPrinterInterface.toStringFormatted(clientController.getSnapshot());
    }

    @Override
    public void onStartMarket() {
        currentController = shopCliController;
        System.out.println(" MARKET STARTED ");
        currentController.printHelp();
    }

    @Override
    public void onStartBuyPhase() {
        currentController = shopCliController;
        shopCliController.onStartBuyPhase();
    }

    @Override
    public void onFinishMarket() {
        currentController=matchCliController;
        shopCliController.onFinishBuyPhase();
    }

    @Override
    public void selectPermitCard() {

    }

    @Override
    public void selectCityRewardBonus() {

    }

    @Override
    public void onMoveKing(ArrayList<City> kingPath) {
            cliPrinterInterface.printBlue(" King moved to "+kingPath.get(kingPath.size()-1));
    }

    @Override
    public void onActionNotPossibleException(ActionNotPossibleException e) {
        cliPrinterInterface.printError("AZIONE NON POSSIBILE!");
        System.out.println(CLIColor.ANSI_RED+" "+e.getMessage()+" "+CLIColor.ANSI_RESET);
    }

    @Override
    public void sendMatchFinishedWithWin(boolean win) {

    }

    @Override
    public void selectOldPermitCardBonus() {

    }




    public void printHelp() {
        cliPrinterInterface.printHelp(OptionsClass.constructOptions());
    }

    public SnapshotToSend getSnapshot() {
        return currentSnapshot;
    }
}
