package ClientPackage.View.GeneralView;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.CLIResources.CLIParser;
import ClientPackage.View.CLIResources.CLIPrinter;
import ClientPackage.View.CLIResources.CLIPrinterInterface;
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
    private CLIParser cliParser = CLIParser.getInstance();
    private ClientController clientController;
    private GameStatus gameStatus = GameStatus.getInstance();

    public CLIView(ClientController clientController) {
        // to implement
        this.clientController = clientController;
        this.clientController.setBaseView(this);
    }

    @Override
    public void initView() {
        System.out.println("CLI Started correctly");
        String input="";
        boolean first = true;
        do{
            if(first) {
                cliPrinterInterface.printHelp();
                first=false;
            }
            input=reader.nextLine();
            cliParser.parseLine(input,this);


        }while (!input.equals("exit"));

    }

    @Override
    public void showLoginError() {
        System.out.println("Name is already used!");
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


    }

    @Override
    public void gameInitialization(SnapshotToSend snapshotToSend) {

    }

    @Override
    public void turnFinished() {

    }

    @Override
    public void isMyTurn(SnapshotToSend snapshot) {

    }

    @Override
    public void updateSnapshot() {

    }

    @Override
    public void onStartMarket() {

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
        cliPrinterInterface.printHelp();
    }
}
