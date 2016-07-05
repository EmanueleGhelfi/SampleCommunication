package CommonModel.GameModel.Council;

import CommonModel.GameModel.Card.SingleCard.PoliticCard.PoliticColor;

import java.io.Serializable;

/**
 * Created by Giulio on 14/05/2016.
 */
public class Councilor implements Serializable, Cloneable {

    private PoliticColor color;

    /**
     * Default constructor for deserialization
     */
    public Councilor() {
    }

    public Councilor(PoliticColor color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Councilor{" +
                "color=" + this.color +
                '}';
    }

    public PoliticColor getColor() {
        return this.color;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        Councilor councilor = (Councilor) o;

        return this.color == councilor.color;

    }

    @Override
    public int hashCode() {
        return this.color != null ? this.color.hashCode() : 0;
    }
}
