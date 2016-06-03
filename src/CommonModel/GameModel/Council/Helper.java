package CommonModel.GameModel.Council;

import CommonModel.GameModel.Market.BuyableObject;

/**
 * Created by Emanuele on 03/06/2016.
 */
public class Helper implements BuyableObject{

    public Helper() {
    }

    @Override
    public String getInfo() {
        return toString();
    }

    @Override
    public BuyableObject getCopy() {
        try {
            return (Helper)this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return "Aiutante";
    }
}
