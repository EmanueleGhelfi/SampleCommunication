package ClientPackage.Service;

import CommonModel.GameModel.Action.ElectCouncillor;
import CommonModel.GameModel.ActionNotPossibleException;

import java.rmi.RemoteException;

/**
 * Created by Emanuele on 09/05/2016.
 */
 public abstract class ClientService {



    public abstract boolean Connect();

    public abstract void sendName(String name);

    public abstract void onTestAction(ElectCouncillor electCouncillor) throws ActionNotPossibleException, RemoteException;
}
