package CommonModel.GameModel.Council;

import CommonModel.GameModel.Market.BuyableObject;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Emanuele on 03/06/2016.
 */
public class Helper implements BuyableObject {

    // for equals, id of the object
    static final AtomicLong NEXT_ID = new AtomicLong(0);
    final long id = Helper.NEXT_ID.getAndIncrement();
    private final String type = "Helper";

    public Helper() {
    }

    @Override
    public String getInfo() {
        return this.toString();
    }

    @Override
    public BuyableObject getCopy() {
        try {
            return (Helper) clone();
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
        if (o == null || this.getClass() != o.getClass()) return false;

        Helper helper = (Helper) o;

        if (this.id != helper.id) return false;
        return this.type != null ? this.type.equals(helper.type) : helper.type == null;

    }

    @Override
    public int hashCode() {
        int result = this.type != null ? this.type.hashCode() : 0;
        result = 31 * result + (int) (this.id ^ this.id >>> 32);
        return result;
    }

    @Override
    public String toString() {
        return "Aiutante";
    }
}
