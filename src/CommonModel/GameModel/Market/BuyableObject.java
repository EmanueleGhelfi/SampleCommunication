package CommonModel.GameModel.Market;

import java.io.Serializable;

/**
 * Created by Emanuele on 30/05/2016.
 */
public interface BuyableObject extends Serializable, Cloneable {

    /** Getter of the info of the buyable object
     * @return the string with info
     */
    String getInfo();

    /** Getter of the copy
     * @return the copy of the buyable object
     */
    BuyableObject getCopy();

    /** Getter of the URL
     * @return the url to upload the image
     */
    String getUrl();

}
