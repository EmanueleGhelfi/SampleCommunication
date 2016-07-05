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

    private final Scanner reader = new Scanner(System.in);
    private final CLIPrinterInterface cliPrinterInterface = new CLIPrinter();
    private final CLIParser cliParser = new CLIParser(getClass());
    private final ClientController clientController;
    private boolean first = true;
    private final MatchCliController matchCliController;
    private final ShopCliController shopCliController;
    private final LoginCliController loginCliController;
    private SnapshotToSend currentSnapshot;
    private CliController currentController;
    private final AtomicBoolean needToRead = new AtomicBoolean(true);
    private final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private Future futureTask;

    public CLIView(ClientController clientController) {
        this.clientController = clientController;
        this.clientController.setBaseView(this);
        this.matchCliController = new MatchCliController(this, clientController);
        this.shopCliController = new ShopCliController(this, clientController);
        this.loginCliController = new LoginCliController(this, clientController);
    }

    @Override
    public void initView() {
        this.currentController = this.loginCliController;
        this.printWelcome();
        this.getInput();
    }

    private void getInput() {
        Runnable runnable = () -> {
            String input = "";
            if (this.first) {
                this.currentController.printHelp();
                this.first = false;
            }

            while (true) {
                try {
                    while (!this.bufferedReader.ready() || !this.needToRead.get()) {
                        Thread.sleep(200);
                    }
                    input = this.bufferedReader.readLine();
                    String finalInput = input;
                    this.currentController.parseLine(finalInput);
                } catch (IOException e1) {

                } catch (InterruptedException e) {
                }
            }
        };
        this.futureTask = this.executorService.submit(runnable);
    }

    @Override
    public void showLoginError() {
        this.cliPrinterInterface.printError("Name is already used! Insert another name!");
        this.loginCliController.setLoginDone(false);
    }

    @Override
    public void showWaitingForStart() {
        this.cliPrinterInterface.printGreen("Waiting for other player...");
    }

    @Override
    public void showMap(ArrayList<Map> mapArrayList) {
        this.needToRead.set(false);
        System.out.println("Select map: \n");
        for (Map map : mapArrayList) {
            System.out.println(this.cliPrinterInterface.toStringFormatted(map) + "\n");
        }
        int scelta = -1;
        do {
            try {
                scelta = this.reader.nextInt();
                if (scelta >= 0 && scelta < mapArrayList.size()) {
                    this.clientController.sendMap(mapArrayList.get(scelta));
                }
            } catch (Exception e) {
                this.cliPrinterInterface.printError("ERROR IN MAP SELECTION, RETRY!");
                this.reader.nextLine();
            }

        } while (scelta < 0 || scelta >= mapArrayList.size());

    }

    @Override
    public void gameInitialization(SnapshotToSend snapshotToSend) {
        System.out.println("Game Started Correctly\n");
        currentSnapshot = snapshotToSend;
        this.currentController = this.matchCliController;
        this.matchCliController.onGameStart();
        this.needToRead.set(true);
        this.getInput();
    }

    @Override
    public void turnFinished() {
        System.out.println(" " + CLIColor.ANSI_RED + " Turno finito " + CLIColor.ANSI_RESET);
        this.futureTask.cancel(true);
        this.matchCliController.onFinisTurn();
        this.getInput();
    }

    @Override
    public void isMyTurn(SnapshotToSend snapshot) {
        System.out.println("Is your turn, make you choice: ");
        this.matchCliController.onYourTurn();
    }

    @Override
    public void updateSnapshot() {
    }

    @Override
    public void onStartMarket() {
        this.currentController = this.shopCliController;
        this.shopCliController.onStartMarket();
        this.currentController.printHelp();
    }

    @Override
    public void onStartBuyPhase() {
        this.currentController = this.shopCliController;
        this.shopCliController.onStartBuyPhase();
    }

    @Override
    public void onFinishMarket() {
        this.currentController = this.matchCliController;
        this.shopCliController.onFinishBuyPhase();
    }

    @Override
    public void selectPermitCard() {
        this.futureTask.cancel(true);
        this.matchCliController.selectPermitCard();
        this.getInput();
    }

    @Override
    public void selectCityRewardBonus() {
        this.futureTask.cancel(true);
        this.matchCliController.selectCityRewardBonus();
        this.getInput();
    }

    @Override
    public void onMoveKing(ArrayList<City> kingPath) {
        this.cliPrinterInterface.printBlue(" King moved to " + kingPath.get(kingPath.size() - 1).getCityName() + " Bonus: " + kingPath.get(kingPath.size() - 1).getBonus());
    }

    @Override
    public void onActionNotPossibleException(ActionNotPossibleException e) {
        this.cliPrinterInterface.printError("AZIONE NON POSSIBILE!");
        System.out.println(CLIColor.ANSI_RED + " " + e.getMessage() + " " + CLIColor.ANSI_RESET);
    }

    @Override
    public void sendMatchFinishedWithWin() {

    }

    @Override
    public void selectOldPermitCardBonus() {
        this.futureTask.cancel(true);
        this.matchCliController.selectOldPermitCardBonus();
        this.getInput();
    }

    @Override
    public void onActionDone(Action action) {
        this.cliPrinterInterface.printBlue("Sending Action to Server: ");
        System.out.println(action);
    }

    @Override
    public void onUserDisconnect(String username) {
        this.cliPrinterInterface.printError("User: " + username + " is offline!");
    }


    public void printHelp() {
        this.cliPrinterInterface.printHelp(OptionsClass.constructOptions());
    }

    public SnapshotToSend getSnapshot() {
        return this.currentSnapshot;
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
