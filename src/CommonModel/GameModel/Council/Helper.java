package CommonModel.GameModel.Council;

import CommonModel.GameModel.Market.BuyableObject;

/**
 * Created by Emanuele on 03/06/2016.
 */
public class Helper implements BuyableObject{

    private String type = "Helper";

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Helper helper = (Helper) o;

        return type != null ? type.equals(helper.type) : helper.type == null;

    }



    @Override
    public String toString() {
        return "Aiutante";
    }
}
