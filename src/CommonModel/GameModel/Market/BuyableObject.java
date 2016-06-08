package CommonModel.GameModel.Market;

import java.io.Serializable;

/**
 * Created by Emanuele on 30/05/2016.
 */
public interface BuyableObject extends Serializable, Cloneable {

    //String getType();

    String getInfo();

    BuyableObject getCopy();

    String getUrl();

}
