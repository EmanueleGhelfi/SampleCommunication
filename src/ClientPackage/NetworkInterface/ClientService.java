package ClientPackage.NetworkInterface;

import CommonModel.GameModel.Action.Action;
import CommonModel.GameModel.Market.BuyableWrapper;
import Server.Model.Map;
import Utilities.Exception.ActionNotPossibleException;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by Emanuele on 09/05/2016.
 */
 public abstract class ClientService {

    public abstract boolean Connect();

    public abstract void sendName(String name);

    public abstract void onAction(Action action) throws ActionNotPossibleException, RemoteException;

    public abstract void sendMap(Map map);

    public abstract void sendSaleItem(ArrayList<BuyableWrapper> realSaleList);
}
