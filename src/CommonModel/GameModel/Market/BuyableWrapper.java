package CommonModel.GameModel.Market;

import CommonModel.Snapshot.BaseUser;

/**
 * Created by Emanuele on 30/05/2016.
 */
public class BuyableWrapper {
    private BuyableObject buyableObject;
    private int cost;
    private String username;


    public BuyableWrapper(BuyableObject buyableObject, int cost, String username) {
        this.buyableObject = buyableObject;
        this.cost = cost;
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BuyableWrapper that = (BuyableWrapper) o;

        if (cost != that.cost) return false;
        if (buyableObject != null ? !buyableObject.equals(that.buyableObject) : that.buyableObject != null)
            return false;
        return username != null ? username.equals(that.username) : that.username == null;

    }

    @Override
    public int hashCode() {
        int result = buyableObject != null ? buyableObject.hashCode() : 0;
        result = 31 * result + cost;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        return result;
    }

    public BuyableObject getBuyableObject() {
        return buyableObject;
    }

    public String getUsername() {
        return username;
    }

    public int getCost() {
        return cost;
    }
}



