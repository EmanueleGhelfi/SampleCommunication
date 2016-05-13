package ClientPackage.Service;

import ClientPackage.Controller.ClientController;
import Interface.RMIClientHandler;
import Interface.RMIClientInterface;
import Interface.RMIListenerInterface;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

/**
 * Created by Emanuele on 09/05/2016.
 */
public class ClientRMIService extends ClientService implements RMIClientInterface {

    private String serverName;
    RMIListenerInterface rmiListenerInterface;
    Registry registry;
    private String rmiHandlerName;
    RMIClientHandler rmiClientHandler;
    private ClientController clientController;

    ClientRMIService(String serverName, String serverIP, ClientController clientController) throws RemoteException, NotBoundException {
        this.serverName = serverName;
        this.clientController = clientController;
        registry = LocateRegistry.getRegistry(serverIP, 1099);
        rmiListenerInterface = (RMIListenerInterface) registry.lookup(serverName);
        UnicastRemoteObject.exportObject(this,0);
    }

    @Override
    public void SendMessage(String message) {
        try {
            rmiClientHandler.OnMessage(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean Connect() {
        try {
        rmiHandlerName = rmiListenerInterface.Connect();

            rmiClientHandler = (RMIClientHandler) registry.lookup(rmiHandlerName);
            System.out.println("Connected to server");
            // get ip address and sends it to server with the name of remote object
            String ip = getIP();

            String name = generateName();
            registry.rebind(name,this);
            rmiClientHandler.sendIP(ip,name);
            return true;
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
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

    @Override
    public void OnMessage(String message) {
        System.out.println(message);

    }

    public String getIP() throws UnknownHostException {
        InetAddress IP=InetAddress.getLocalHost();
        System.out.println("IP of my system is := "+IP.getHostAddress());
        return IP.getHostAddress();
    }
}
