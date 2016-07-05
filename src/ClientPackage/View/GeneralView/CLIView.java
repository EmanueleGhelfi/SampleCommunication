package ClientPackage.View.GeneralView;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.CLIResources.*;
import CommonModel.GameModel.Action.Action;
import CommonModel.GameModel.City.City;
import CommonModel.Snapshot.SnapshotToSend;
import Server.Model.Map;
import Utilities.Exception.ActionNotPossibleException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Emanuele on 13/05/2016.
 */
public class CLIView implements BaseView {

    private Scanner reader = new Scanner(System.in);
    private CLIPrinterInterface cliPrinterInterface = new CLIPrinter();
    private CLIParser cliParser = new CLIParser(this.getClass());
    private ClientController clientController;
    private boolean first = true;
    private MatchCliController matchCliController;
    private ShopCliController shopCliController;
    private LoginCliController loginCliController;
    private SnapshotToSend currentSnapshot;
    private CliController currentController;
    private AtomicBoolean needToRead = new AtomicBoolean(true);
    private BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private Future futureTask;

    public CLIView(ClientController clientController) {
        this.clientController = clientController;
        this.clientController.setBaseView(this);
        matchCliController = new MatchCliController(this, clientController);
        shopCliController = new ShopCliController(this, clientController);
        loginCliController = new LoginCliController(this, clientController);
    }

    @Override
    public void initView() {
        currentController = loginCliController;
        printWelcome();
        getInput();
    }

    private void getInput() {
        Runnable runnable = () -> {
            String input = "";
            if (first) {
                currentController.printHelp();
                first = false;
            }

            while (true) {
                try {
                    while (!bufferedReader.ready() || !needToRead.get()) {
                        Thread.sleep(200);
                    }
                    input = bufferedReader.readLine();
                    String finalInput = input;
                    currentController.parseLine(finalInput);
                } catch (IOException e1) {

                } catch (InterruptedException e) {
                }
            }
        };
        futureTask = executorService.submit(runnable);
    }

    @Override
    public void showLoginError() {
        cliPrinterInterface.printError("Name is already used! Insert another name!");
        loginCliController.setLoginDone(false);
    }

    @Override
    public void showWaitingForStart() {
        cliPrinterInterface.printGreen("Waiting for other player...");
    }

    @Override
    public void showMap(ArrayList<Map> mapArrayList) {
        needToRead.set(false);
        System.out.println("Select map: \n");
        for (Map map : mapArrayList) {
            System.out.println(cliPrinterInterface.toStringFormatted(map) + "\n");
        }
        int scelta = -1;
        do {
            try {
                scelta = reader.nextInt();
                if (scelta >= 0 && scelta < mapArrayList.size()) {
                    clientController.sendMap(mapArrayList.get(scelta));
                }
            } catch (Exception e) {
                cliPrinterInterface.printError("ERROR IN MAP SELECTION, RETRY!");
                reader.nextLine();
            }

        } while (scelta < 0 || scelta >= mapArrayList.size());

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
        System.out.println(" " + CLIColor.ANSI_RED + " Turno finito " + CLIColor.ANSI_RESET);
        futureTask.cancel(true);
        matchCliController.onFinisTurn();
        getInput();
    }

    @Override
    public void isMyTurn(SnapshotToSend snapshot) {
        System.out.println("Is your turn, make you choice: ");
        matchCliController.onYourTurn();
    }

    @Override
    public void updateSnapshot() {
    }

    @Override
    public void onStartMarket() {
        currentController = shopCliController;
        shopCliController.onStartMarket();
        currentController.printHelp();
    }

    @Override
    public void onStartBuyPhase() {
        currentController = shopCliController;
        shopCliController.onStartBuyPhase();
    }

    @Override
    public void onFinishMarket() {
        currentController = matchCliController;
        shopCliController.onFinishBuyPhase();
    }

    @Override
    public void selectPermitCard() {
        futureTask.cancel(true);
        matchCliController.selectPermitCard();
        getInput();
    }

    @Override
    public void selectCityRewardBonus() {
        futureTask.cancel(true);
        matchCliController.selectCityRewardBonus();
        getInput();
    }

    @Override
    public void onMoveKing(ArrayList<City> kingPath) {
        cliPrinterInterface.printBlue(" King moved to " + kingPath.get(kingPath.size() - 1).getCityName() + " Bonus: " + kingPath.get(kingPath.size() - 1).getBonus());
    }

    @Override
    public void onActionNotPossibleException(ActionNotPossibleException e) {
        cliPrinterInterface.printError("AZIONE NON POSSIBILE!");
        System.out.println(CLIColor.ANSI_RED + " " + e.getMessage() + " " + CLIColor.ANSI_RESET);
    }

    @Override
    public void sendMatchFinishedWithWin() {

    }

    @Override
    public void selectOldPermitCardBonus() {
        futureTask.cancel(true);
        matchCliController.selectOldPermitCardBonus();
        getInput();
    }

    @Override
    public void onActionDone(Action action) {
        cliPrinterInterface.printBlue("Sending Action to Server: ");
        System.out.println(action.toString());
    }

    @Override
    public void onUserDisconnect(String username) {
        cliPrinterInterface.printError("User: " + username + " is offline!");
    }


    public void printHelp() {
        cliPrinterInterface.printHelp(OptionsClass.constructOptions());
    }

    public SnapshotToSend getSnapshot() {
        return currentSnapshot;
    }


    public void printWelcome() {
        System.out.println("                                                                                                                                                          \n" +
                "                                                                                     ,----..                                                              \n" +
                "  ,----..                                                         ,--,              /   /   \\                      ,---,.                                 \n" +
                " /   /   \\                                               ,--,   ,--.'|             /   .     :   .--.,           ,'  .' |                                 \n" +
                "|   :     :  ,---.           ,--,      ,---,           ,--.'|   |  | :            .   /   ;.  \\,--.'  \\        ,---.'   |   ,---.           ,--,  __  ,-. \n" +
                ".   |  ;. / '   ,'\\        ,'_ /|  ,-+-. /  |          |  |,    :  : '           .   ;   /  ` ;|  | /\\/        |   |   .'  '   ,'\\        ,'_ /|,' ,'/ /| \n" +
                ".   ; /--` /   /   |  .--. |  | : ,--.'|'   |   ,---.  `--'_    |  ' |           ;   |  ; \\ ; |:  : :          :   :  :   /   /   |  .--. |  | :'  | |' | \n" +
                ";   | ;   .   ; ,. :,'_ /| :  . ||   |  ,\"' |  /     \\ ,' ,'|   '  | |           |   :  | ; | ':  | |-,        :   |  |-,.   ; ,. :,'_ /| :  . ||  |   ,' \n" +
                "|   : |   '   | |: :|  ' | |  . .|   | /  | | /    / ' '  | |   |  | :           .   |  ' ' ' :|  : :/|        |   :  ;/|'   | |: :|  ' | |  . .'  :  /   \n" +
                ".   | '___'   | .; :|  | ' |  | ||   | |  | |.    ' /  |  | :   '  : |__         '   ;  \\; /  ||  |  .'        |   |   .''   | .; :|  | ' |  | ||  | '    \n" +
                "'   ; : .'|   :    |:  | : ;  ; ||   | |  |/ '   ; :__ '  : |__ |  | '.'|         \\   \\  ',  / '  : '          '   :  '  |   :    |:  | : ;  ; |;  : |    \n" +
                "'   | '/  :\\   \\  / '  :  `--'   \\   | |--'  '   | '.'||  | '.'|;  :    ;          ;   :    /  |  | |          |   |  |   \\   \\  / '  :  `--'   \\  , ;    \n" +
                "|   :    /  `----'  :  ,      .-./   |/      |   :    :;  :    ;|  ,   /            \\   \\ .'   |  : \\          |   :  \\    `----'  :  ,      .-./---'     \n" +
                " \\   \\ .'            `--`----'   '---'        \\   \\  / |  ,   /  ---`-'              `---`     |  |,'          |   | ,'             `--`----'             \n" +
                "  `---`                                        `----'   ---`-'                                 `--'            `----'                                     \n" +
                "                                                                                                                                                          ");
    }
}
