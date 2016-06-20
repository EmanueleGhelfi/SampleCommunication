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

/**
 * Created by Emanuele on 13/05/2016.
 */
//TODO
public class CLIView implements BaseView {

    private Scanner reader = new Scanner(System.in);
    private CLIPrinterInterface cliPrinterInterface = new CLIPrinter();
    private CLIParser cliParser = new CLIParser(OptionsClass.constructOptions());
    private ClientController clientController;
    // for showing help
    private boolean first = true;
    private MatchCliController matchCliController;
    private ShopCliController shopCliController;
    private SnapshotToSend currentSnapshot;


    public CLIView(ClientController clientController) {
        // to implement
        this.clientController = clientController;
        this.clientController.setBaseView(this);
        matchCliController= new MatchCliController(this,clientController);
        shopCliController = new ShopCliController(this,clientController);
    }

    @Override
    public void initView() {
        String input="";
        if(first) {
            cliPrinterInterface.printHelp(OptionsClass.constructOptions());
            first=false;
        }
        input=reader.nextLine();
        cliParser.parseLine(input,this);
    }

    @Override
    public void showLoginError() {
        System.out.println("Name is already used! Insert another name!");
        initView();
    }

    @Override
    public void showWaitingForStart() {
        System.out.println("Waiting for other player...");
    }

    @Override
    public void showMap(ArrayList<Map> mapArrayList) {
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
        matchCliController.onGameStart();

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

        System.out.println("On update snapshot");
        cliPrinterInterface.toStringFormatted(clientController.getSnapshot());
    }

    @Override
    public void onStartMarket() {
        shopCliController.onStartMarket();

    }

    @Override
    public void onStartBuyPhase() {

    }

    @Override
    public void onFinishMarket() {

    }

    @Override
    public void selectPermitCard() {

    }

    @Override
    public void selectCityRewardBonus() {

    }

    @Override
    public void onMoveKing(ArrayList<City> kingPath) {

    }

    @Override
    public void onActionNotPossibleException(ActionNotPossibleException e) {

    }

    @Override
    public void sendMatchFinishedWithWin(boolean win) {

    }

    @Override
    public void selectOldPermitCardBonus() {

    }

    public void showStatus() {
        if(clientController.getSnapshot()!=null) {
            System.out.println(clientController.getSnapshot().toString());
        }
    }

    public void onLogin(String name) {
        System.out.println("sending login");
        clientController.onSendLogin(name);
    }

    public void printHelp() {
        cliPrinterInterface.printHelp(OptionsClass.constructOptions());
    }

    public SnapshotToSend getSnapshot() {
        return currentSnapshot;
    }
}
