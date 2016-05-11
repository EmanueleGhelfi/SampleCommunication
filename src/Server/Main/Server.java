package Server.Main;

import Interface.RMIClientHandler;
import Interface.RMIListenerInterface;
import Server.Listeners.RMIHandler;
import Server.Listeners.RMIListener;
import Server.Listeners.SocketListener;
import Server.UserClasses.User;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Created by Emanuele on 09/05/2016.
 */
public class Server {

    private ArrayList<User> users = new ArrayList<>();

    public Server(){

    }

    public void start(){
        try {
            RMIListenerInterface rmiListener = new RMIListener(this);
            Registry registry=null;
        try{
             registry = LocateRegistry.createRegistry(1099);
        }
        catch (ExportException e){
            e.printStackTrace();
            registry = LocateRegistry.getRegistry();

        }
        registry.rebind("server",rmiListener);

            SocketListener socketListener = SocketListener.getInstance(this);
            Thread thread = new Thread(socketListener);
            thread.start();


        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void AddToUsers(User userToAdd){
        users.add(userToAdd);
    }
}
