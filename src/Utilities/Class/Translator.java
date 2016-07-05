package Utilities.Class;

/**
 * Created by Giulio on 01/07/2016.
 */
public class Translator {

    public Translator() {
    }

    public static String translatingToIta(String string) {
        switch (string) {
            case Constants.BLUE:
                return "Blu";
            case Constants.YELLOW:
                return "Giallo";
            case Constants.WHITE:
                return "Bianco";
            case Constants.BLACK:
                return "Nero";
            case Constants.PURPLE:
                return "Fucsia";
            case Constants.VIOLET:
                return "Viola";
            case Constants.ORANGE:
                return "Arancione";
            case Constants.MOUNTAIN:
                return "Montagna";
            case Constants.COAST:
                return "Costa";
            case Constants.HILL:
                return "Collina";
            default:
                return string;
        }
    }

    public static String translatingToEng(String string) {
        switch (string) {
            case "Blu":
                return Constants.BLUE;
            case "Giallo":
                return Constants.YELLOW;
            case "Bianco":
                return Constants.WHITE;
            case "Nero":
                return Constants.BLACK;
            case "Fucsia":
                return Constants.PURPLE;
            case "Viola":
                return Constants.VIOLET;
            case "Arancione":
                return Constants.ORANGE;
            default:
                return string;
        }
    }

}
