package CommonModel.Snapshot;

import java.io.Serializable;

/**
 * Created by Giulio on 21/06/2016.
 */
public enum UserColor implements Serializable {

    PINK("PINK"), BLACK("BLACK"), BROWN("BROWN"), LILAC("LILAC"), BLUE("BLUE"), PEAGREEN("PEAGREEN"), YELLOW("YELLOW"), ORANGE("ORANGE"), LIGHTBLUE("LIGHTBLUE"), GREEN("GREEN");

    private String color;

    UserColor() {
    }

    UserColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

}
