package ClientPackage.NetworkInterface;

import ClientPackage.Controller.ClientController;
import CommonModel.GameModel.Action.Action;
import CommonModel.GameModel.Card.SingleCard.PermitCard.PermitCard;
import CommonModel.GameModel.City.City;
import CommonModel.GameModel.Market.BuyableWrapper;
import CommonModel.Snapshot.BaseUser;
import CommonModel.Snapshot.SnapshotToSend;
import RMIInterface.RMIClientHandler;
import RMIInterface.RMIClientInterface;
import RMIInterface.RMIListenerInterface;
import Server.Model.Map;
import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Emanuele on 09/05/2016.
 */
public class ClientRMIService extends ClientService implements RMIClientInterface {

    private final String serverName;
    private final RMIListenerInterface rmiListenerInterface;
    private final Registry registry;
    private String rmiHandlerName;
    private RMIClientHandler rmiClientHandler;
    private final ClientController clientController;
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    ClientRMIService(String serverName, String serverIP, ClientController clientController) throws RemoteException, NotBoundException {
        this.serverName = serverName;
        this.clientController = clientController;
        this.registry = LocateRegistry.getRegistry(serverIP, Constants.RMI_PORT);
        this.rmiListenerInterface = (RMIListenerInterface) this.registry.lookup(serverName);
        UnicastRemoteObject.exportObject(this, 0);
    }

    @Override
    public boolean Connect() {
        try {
            this.rmiHandlerName = this.rmiListenerInterface.Connect();
            this.rmiClientHandler = (RMIClientHandler) this.registry.lookup(this.rmiHandlerName);
            System.out.println("Connected to server");
            this.rmiClientHandler.sendRemoteClientObject(this);
            return true;
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void sendName(String name) {
        Runnable runnable = () -> {
            try {
                boolean result = this.rmiClientHandler.tryToSetName(name);
                this.clientController.onNameReceived(result);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        };
        this.executorService.execute(runnable);
    }

    @Override
    public void onAction(Action action) throws ActionNotPossibleException, RemoteException {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    ClientRMIService.this.rmiClientHandler.test(action);
                } catch (ActionNotPossibleException e) {
                    ClientRMIService.this.clientController.onActionNotPossible(e);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        this.executorService.execute(runnable);
    }

    @Override
    public void sendMap(Map map) {
        Runnable runnable = () -> {
            try {
                this.rmiClientHandler.sendMap(map);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        };
        this.executorService.execute(runnable);
    }

    @Override
    public void sendSaleItem(ArrayList<BuyableWrapper> realSaleList) {
        Runnable runnable = () -> {
            try {
                if (this.rmiClientHandler.sendBuyableObject(realSaleList)) {
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        };
        this.executorService.execute(runnable);
    }

    @Override
    public void onBuy(ArrayList<BuyableWrapper> buyList) {
        Runnable runnable = () -> {
            try {
                this.rmiClientHandler.buyObject(buyList);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        };
        this.executorService.execute(runnable);
    }

    @Override
    public void onRemoveItemFromMarket(BuyableWrapper item) {
        Runnable runnable = () -> {
            try {
                this.rmiClientHandler.onRemoveItem(item);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        };
        this.executorService.execute(runnable);
    }

    @Override
    public void onFinishSellPhase() {
        Runnable runnable = () -> {
            try {
                this.rmiClientHandler.onFinishSellPhase();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        };
        this.executorService.execute(runnable);
    }

    @Override
    public void sendFinishedBuyPhase() {
        Runnable runnable = () -> {
            try {
                this.rmiClientHandler.onFinishBuyPhase();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        };
        this.executorService.execute(runnable);
    }

    @Override
    public void getCityRewardBonus(City city1) {
        Runnable runnable = () -> {
            try {
                this.rmiClientHandler.getCityRewardBonus(city1);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (ActionNotPossibleException e) {
                this.clientController.onActionNotPossible(e);
            }
        };
        this.executorService.execute(runnable);
    }

    @Override
    public void onSelectPermitCard(PermitCard permitCard) {
        Runnable runnable = () -> {
            try {
                this.rmiClientHandler.onSelectPermitCard(permitCard);
            } catch (RemoteException e) {

            }
        };
        this.executorService.execute(runnable);
    }

    @Override
    public void onFinishTurn() {
        Runnable runnable = () -> {
            try {
                this.rmiClientHandler.finishRound();
            } catch (RemoteException e) {

            }
        };
        this.executorService.execute(runnable);
    }

    @Override
    public void onSelectOldPermitCard(PermitCard permitCard) {
        Runnable runnable = () -> {
            try {
                this.rmiClientHandler.onSelectOldPermitCard(permitCard);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        };
        this.executorService.execute(runnable);
    }

    /***
     * REGION OF METHODS CALLED BY SERVER
     **/

    @Override
    public void sendSnapshot(SnapshotToSend snapshotToSend) throws RemoteException {
        Runnable runnable = () -> {
            this.clientController.setSnapshot(snapshotToSend);
        };
        this.executorService.execute(runnable);
    }

    @Override
    public void sendMap(ArrayList<Map> mapArrayList) {
        Runnable runnable = () -> {
            this.clientController.showMap(mapArrayList);
        };
        this.executorService.execute(runnable);
    }

    @Override
    public void gameInitialization(SnapshotToSend snapshotToSend) throws RemoteException {
        this.executorService.execute(() -> {
            this.clientController.gameInitialization(snapshotToSend);
        });

    }

    @Override
    public void isYourTurn() throws RemoteException {
        this.executorService.execute(() -> {
            this.clientController.isMyTurn();
        });

    }

    @Override
    public void finishTurn() throws RemoteException {
        this.executorService.execute(() -> {
            this.clientController.turnFinished();
        });

    }

    @Override
    public void onStartMarket() throws RemoteException {
        this.executorService.execute(() -> {
            this.clientController.onStartMarket();
        });

    }

    @Override
    public void onStartBuyPhase() throws RemoteException {
        this.executorService.execute(() -> {
            this.clientController.onStartBuyPhase();
        });

    }

    @Override
    public void disableMarketPhase() throws RemoteException {

        this.executorService.execute(() -> {
            this.clientController.onFinishBuyPhase();
        });

    }

    @Override
    public void selectPermitCard() throws RemoteException {
        this.executorService.execute(() -> {
            this.clientController.selectPermitCard();
        });

    }

    @Override
    public void selectCityRewardBonus(SnapshotToSend snapshotToSend) throws RemoteException {
        this.executorService.execute(() -> {
            this.clientController.selectCityRewardBonus(snapshotToSend);
        });

    }

    @Override
    public void moveKing(ArrayList<City> kingPath) throws RemoteException {
        this.executorService.execute(() -> {
            this.clientController.onMoveKing(kingPath);
        });

    }

    @Override
    public void sendMatchFinishedWithWin(ArrayList<BaseUser> finalSnapshot) throws RemoteException {
        this.executorService.execute(() -> {
            this.clientController.sendMatchFinishedWithWin(finalSnapshot);
        });

    }

    @Override
    public void ping() throws RemoteException {

    }

    @Override
    public void selectOldPermiCard() throws RemoteException {
        this.executorService.execute(() -> {
            this.clientController.selectOldPermitCardBonus();
        });

    }

    @Override
    public void onUserDisconnect(String username) throws RemoteException {
        this.executorService.execute(() -> {
            this.clientController.onUserDisconnect(username);
        });
    }
}
