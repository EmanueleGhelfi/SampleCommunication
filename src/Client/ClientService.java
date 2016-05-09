package Client;

/**
 * Created by Emanuele on 09/05/2016.
 */
public abstract class ClientService {

    abstract void SendMessage(String message);

    abstract void Connect();
}
