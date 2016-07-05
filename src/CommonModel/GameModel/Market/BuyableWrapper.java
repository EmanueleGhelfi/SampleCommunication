package CommonModel.GameModel.Market;

import java.io.Serializable;

/**
 * Created by Emanuele on 30/05/2016.
 */
public class BuyableWrapper implements Serializable {
    private BuyableObject buyableObject;
    private int cost;
    private String username;
    private boolean onSale;


    public BuyableWrapper() {
    }

    public BuyableWrapper(BuyableObject buyableObject, int cost, String username) {
        this.buyableObject = buyableObject;
        this.cost = cost;
        this.username = username;
    }

    public BuyableWrapper(BuyableObject buyableObject, String username) {
        this.buyableObject = buyableObject;
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        BuyableWrapper that = (BuyableWrapper) o;

        if (this.buyableObject != null ? !this.buyableObject.equals(that.buyableObject) : that.buyableObject != null)
            return false;
        return this.username != null ? this.username.equals(that.username) : that.username == null;
    }

    public BuyableObject getBuyableObject() {
        return this.buyableObject.getCopy();
    }

    public String getUsername() {
        return this.username;
    }

    public int getCost() {
        return this.cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public boolean isOnSale() {
        return this.onSale;
    }

    public void setOnSale(boolean onSale) {
        this.onSale = onSale;
    }

    @Override
    public String toString() {
        return "BuyableWrapper{" +
                "buyableObject=" + this.buyableObject +
                ", cost=" + this.cost +
                ", username='" + this.username + '\'' +
                ", onSale=" + this.onSale +
                '}';
    }
}



