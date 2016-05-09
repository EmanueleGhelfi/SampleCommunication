package Interface;

import java.rmi.Remote;


/**
 * Created by Emanuele on 09/05/2016.
 */
public interface RMIClientInterface extends Remote {

    void OnMessage(String message);


}
