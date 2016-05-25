package ClientPackage.NetworkInterface;

import ClientPackage.Controller.ClientController;
import CommonModel.GameModel.Action.Action;
import CommonModel.Snapshot.SnapshotToSend;
import Server.Model.Map;
import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;
import RMIInterface.RMIClientHandler;
import RMIInterface.RMIClientInterface;
import RMIInterface.RMIListenerInterface;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Emanuele on 09/05/2016.
 */
public class ClientRMIService extends ClientService implements RMIClientInterface {

    private String serverName;
    private RMIListenerInterface rmiListenerInterface;
    private Registry registry;
    private String rmiHandlerName;
    private RMIClientHandler rmiClientHandler;
    private ClientController clientController;

    ClientRMIService(String serverName, String serverIP, ClientController clientController) throws RemoteException, NotBoundException {
        this.serverName = serverName;
        this.clientController = clientController;
        registry = LocateRegistry.getRegistry(serverIP, Constants.RMI_PORT);
        rmiListenerInterface = (RMIListenerInterface) registry.lookup(serverName);
        UnicastRemoteObject.exportObject(this,0);
    }

    @Override
    public boolean Connect() {
        try {
            rmiHandlerName = rmiListenerInterface.Connect();
            rmiClientHandler = (RMIClientHandler) registry.lookup(rmiHandlerName);
            System.out.println("Connected to server");
            // get ip address and sends it to server with the name of remote object
            //String ip = getIP();
            //String name = generateName();
            //registry.rebind(name,this);
            rmiClientHandler.sendRemoteClientObject(this);
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
        try {
            boolean result = rmiClientHandler.tryToSetName(name);
            clientController.onNameReceived(result);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAction(Action action) throws ActionNotPossibleException, RemoteException {
        rmiClientHandler.test(action);
    }

    @Override
    public void sendMap(Map map) {
        try {
            rmiClientHandler.sendMap(map);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private String generateName() {
        String randomSequence="";
        Random randomGenerator = new Random();
        int sequenceLength = 5;
        for (int idx = 1; idx <= sequenceLength; ++idx) {
            int randomInt = randomGenerator.nextInt(10);
            System.out.println("Generated : " + randomInt);
            randomSequence = randomSequence + randomInt;
        }
        return randomSequence;
    }

    public String getIP() throws UnknownHostException {
        InetAddress IP=InetAddress.getLocalHost();
        System.out.println("IP of my system is := "+IP.getHostAddress());
        return IP.getHostAddress();
    }

    @Override
    public void sendSnapshot(SnapshotToSend snapshotToSend) throws RemoteException {
        System.out.println("CLIENTRMISERVICE sendSnapshot");
        clientController.setSnapshot(snapshotToSend);
    }

    @Override
    public void sendMap(ArrayList<Map> mapArrayList) {
        clientController.showMap(mapArrayList);
    }

    @Override
    public void gameInitialization(SnapshotToSend snapshotToSend) throws RemoteException {
        clientController.gameInitialization(snapshotToSend);
    }

    @Override
    public void isYourTurn() throws RemoteException {
        clientController.isMyTurn();
    }

    @Override
    public void finishTurn() throws RemoteException {
        clientController.turnFinished();
    }
}
