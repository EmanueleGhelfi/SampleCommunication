package CommonModel.GameModel;

/**
 * Created by Giulio on 13/05/2016.
 */
public enum Color {

    BLUE("blue", 2), GREY ("grey", 4), ORANGE ("orange", 3), YELLOW ("yellow", 5), PURPLE ("purple", 1);

    private String color;
    private int cityNumber;

    private Color (String color, int cityNumber){
        this.color = color;
        this.cityNumber = cityNumber;
    }

    public String getColor(){
        return color;
    }

    public int getCityNumber(){
        return cityNumber;
    }
}
