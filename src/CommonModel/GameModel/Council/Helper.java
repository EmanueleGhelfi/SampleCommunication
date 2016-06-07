package CommonModel.GameModel.Council;

import CommonModel.GameModel.Market.BuyableObject;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Emanuele on 03/06/2016.
 */
public class Helper implements BuyableObject{

    private String type = "Helper";

    public Helper() {
    }

    // for equals, id of the object
    static final AtomicLong NEXT_ID = new AtomicLong(0);
    final long id = NEXT_ID.getAndIncrement();

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
    public String getUrl() {
        return "Helper";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Helper helper = (Helper) o;

        if (id != helper.id) return false;
        return type != null ? type.equals(helper.type) : helper.type == null;

    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Aiutante";
    }
}
