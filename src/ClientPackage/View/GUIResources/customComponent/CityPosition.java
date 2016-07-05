package ClientPackage.View.GUIResources.CustomComponent;

import CommonModel.GameModel.City.City;

/**
 * Created by Emanuele on 09/06/2016.
 */
public class CityPosition {

    public static double getX(City city) {
        switch (city.getCityName()){
            case ARKON:{
                //return 0.15;
                return 0.12;
            }
            case BURGEN:{
                return 0.11;
            }
            case CASTRUM:{
                //return 0.27;
                return 0.22;
            }
            case DORFUL:{
                return 0.27;
            }
            case ESTI:{
                return 0.17;
            }
            case FRAMEK:{
                return 0.41;
            }
            case GRADEN:{
                return 0.41;
            }
            case HELLAR:{
                return 0.43;
            }
            case INDUR:{
                return 0.55;
            }
            case JUVELAR:{
                return 0.54;
            }
            case KULTOS:{
                return 0.7;
            }
            case LYRAM:{
                return 0.66;
            }
            case MERKATIM:{
                return 0.64;
            }
            case NARIS:{
                return 0.80;
            }
            case OSIUM:{
                return 0.78;
            }
            default:{
                return 0.00;
            }
        }
    }

    public static double getY(City city) {
        switch (city.getCityName()) {
            case ARKON: {
                return 0.13;
            }
            case BURGEN: {
                return 0.43;
            }
            case CASTRUM: {
                //return 0.25;
                return 0.16;
            }
            case DORFUL: {
                return 0.49;
            }
            case ESTI:{
                //return 0.69;
                return 0.65;
            }
            case FRAMEK: {
                return 0.19;
            }
            case GRADEN: {
                return 0.40;
            }
            case HELLAR: {
                return 0.65;
            }
            case INDUR: {
                //return 0.2;
                return 0.17;
            }
            case JUVELAR: {
                //return 0.48;
                return 0.44;
            }
            case KULTOS: {
                return 0.17;
            }
            case LYRAM: {
                return 0.44;
            }
            case MERKATIM: {
                return 0.69;
            }
            case NARIS: {
                return 0.32;
            }
            case OSIUM: {
                return 0.59;
            }
            default: {
                return 0.00;
            }
        }
    }
}
