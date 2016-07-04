package Utilities.Class;

/**
 * Created by Emanuele on 11/05/2016.
 */
public class Constants {

    //CODES
    public static final String CODE_NAME ="CODE_NAME";
    public static final String CODE_CHAT ="CODE_CHAT";
    public static final String CODE_YOUR_TURN = "CODE_YOUR_TURN";
    public static final String CODE_ACTION= "CODE_ACTION";
    public static final String CODE_SNAPSHOT= "CODE_SNAPSHOT";
    public static final String CODE_JSON_TEST = "CODE_JSON_TEST";
    public static final String CODE_MAP = "CODE_MAP";
    public static final String CODE_INITIALIZE_GAME = "CODE_INITIALIZE_GAME";
    public static final String CODE_TURN_FINISHED = "CODE_TURN_FINISHED";
    public static final String CODE_MARKET_SELL = "CODE_MARKET_SELL";
    public static final String CODE_MARKET_BUY = "CODE_MARKET_BUY";
    public static final String CODE_MARKET_REMOVE = "CODE_MARKET_REMOVE";
    public static final String CODE_START_BUY_PHASE = "CODE_START_BUY_PHASE";
    public static final String CODE_START_MARKET = "CODE_START_MARKET";
    public static final String CODE_FINISH_SELL_PHASE = "CODE_FINISH_SELL_PHASE";
    public static final String CODE_FINISH_BUY_PHASE = "CODE_FINISH_BUY_PHASE";
    public static final String CODE_FINISH_MARKET_PHASE = "CODE_FINISH_MARKET_PHASE";
    public static final String SELECT_CITY_REWARD_BONUS = "SELECT_CITY_REWARD_BONUS";
    public static final String CODE_CITY_REWARD_BONUS = "CODE_CITY_REWARD_BONUS";
    public static final String SELECT_PERMITCARD_BONUS = "SELECT_PERMITCARD_BONUS";
    public static final String CODE_SELECT_PERMIT_CARD = "CODE_SELECT_PERMIT_CARD";
    public static final String MOVE_KING = "MOVE_KING";
    public static final String CODE_EXCEPTION = "CODE_EXCEPTION";
    public static final String CODE_FINISH_TURN = "CODE_FINISH_TURN";
    public static final String CODE_FINISH = "CODE_FINISH";
    public static final String CODE_OLD_PERMIT_CARD_BONUS = "CODE_OLD_PERMIT_CARD_BONUS";
    public static final String CODE_USER_DISCONNECT = "CODE_USER_DISCONNECT";

    //GENERAL STRING
    public static final String GUI = "GUI";
    public static final String CLI = "CLI";
    public static final String RMI = "RMI";
    public static final String SOCKET = "SOCKET";
    public static final String SERVER = "SERVER";
    public static final String MAIN_ACTION = "MAIN_ACTION";
    public static final String FAST_ACTION = "FAST_ACTION";
    public static final String REGION_COUNCIL = "REGION_COUNCIL" ;
    public static final String KING_COUNCIL = "KING_COUNCIL";

    //VIEW
    public static final String LOGIN_FXML = "/ClientPackage/View/GUIResources/FXML/LoginFXML.fxml";
    public static final String WAITING_FXML = "/ClientPackage/View/GUIResources/FXML/WaitingFXML.fxml";
    public static final String MATCH_FXML = "/ClientPackage/View/GUIResources/FXML/MatchFXML.fxml";
    public static final String MAP_SELECTION_FXML = "/ClientPackage/View/GUIResources/FXML/MapSelectionFXML.fxml";
    public static final String NOTIFICATION_ICON = "Icon.png";
    public static final String NOTIFICATION_TEXT = "COFfee";
    public static final String CITY_REWARD_BONUS_INCORRECT = "Bonus not correct because of nobility bonus";



    //NETWORK
    public static int RMI_PORT = 1099;
    public static int SOCKET_PORT = 4333;
    public static String SOCKET_IP = "localhost";

    //BONUS
    public static final int REGION_BONUS = 5;
    public static final int YELLOW_BONUS = 20;
    public static final int BLUE_BONUS = 5;
    public static final int GREY_BONUS = 12;
    public static final int ORANGE_BONUS = 8;
    public static final int FIFTH_ARRIVED_KING_BONUS = 3;
    public static final int FOURTH_ARRIVED_KING_BONUS = 7;
    public static final int THIRD_ARRIVED_KING_BONUS = 12;
    public static final int SECOND_ARRIVED_KING_BONUS = 18;
    public static final int FIRST_ARRIVED_KING_BONUS = 25;

    //RANDOM
    public static final int RANDOM_COIN_FIRST_PARAMETER = 6;
    public static final int RANDOM_COIN_SECOND_PARAMETER = 1;
    public static final int RANDOM_HELPER_FIRST_PARAMETER = 5;
    public static final int RANDOM_HELPER_SECOND_PARAMETER = 1;
    public static final int RANDOM_MAINACTION_FIRST_PARAMETER = 1;
    public static final int RANDOM_NOBILITY_FIRST_PARAMETER = 5;
    public static final int RANDOM_NOBILITY_SECOND_PARAMETER = 1;
    public static final int RANDOM_VICTORY_FIRST_PARAMETER = 4;
    public static final int RANDOM_VICTORY_SECOND_PARAMETER = 1;

    //COUNCIL
    public static final int COUNCILOR_DIMENSION = 4;

    //ACTION POSSIBLE
    public static final int MAIN_ACTION_POSSIBLE = 1;
    public static final int FAST_ACTION_POSSIBLE = 1;

    //COLOR
    public static final String BLUE = "blue";
    public static final String ORANGE = "orange";
    public static final String YELLOW = "yellow";
    public static final String GREY = "grey";
    public static final String VIOLET = "violet";
    public static final String BLACK = "black";
    public static final String WHITE = "white";
    public static final String PURPLE = "purple";
    public static final int BLUE_COUNTER = 2;
    public static final int ORANGE_COUNTER = 3;
    public static final int PURPLE_COUNTER = 1;
    public static final int GREY_COUNTER = 4;
    public static final int YELLOW_COUNTER = 5;

    //CITTA'
    public static final String ARKON = "Arkon";
    public static final String BURGEN = "Burgen";
    public static final String CASTRUM = "Castrum";
    public static final String DORFUL = "Dorful";
    public static final String ESTI = "Esti";
    public static final String FRAMEK = "Framek";
    public static final String GRADEN = "Graden";
    public static final String HELLAR = "Hellar";
    public static final String INDUR = "Indur";
    public static final String JUVELAR = "Juvelar";
    public static final String KULTOS = "Kultos";
    public static final String LYRAM = "Lyram";
    public static final String MERKATIM = "Merkatim";
    public static final String NARIS = "Naris";
    public static final String OSIUM = "Osium";

    //REGIONI
    public static final String MOUNTAIN = "Mountain";
    public static final String HILL = "Hill";
    public static final String COAST = "Coast";

    //PATH LENGHT
    public static final int MONEY_PATH_LENGTH = 20;
    public static final int NOBILITY_PATH_ELEMENT = 20;
    public static final int VICTORY_PATH_LENGTH = 100;

    //TIMEOUT TO CREATE GAME
    public static final int GAME_TIMEOUT = 5000;

    //DECK SIZE
    public static final int SINGLECOLOR_POLITIC_DECK_SIZE = 13;
    public static final int MULTICOLOR_POLITIC_DECK_SIZE = 12;
    public static final int REGION_DECK_SIZE = 15;

    //GAME RULES
    public static final int HELPER_LIMITATION_CHANGE_PERMIT_CARD = 1;
    public static final int HELPER_LIMITATION_ELECT_COUNCILOR = 1;
    public static final int MONEY_LIMITATION_MONEY_FOR_HELPER = 3;
    public static final int HELPER_ADDED_MONEY_FOR_HELPER = 1;
    public static final int HELPER_LIMITATION_NEW_MAIN_ACTION = 3;
    public static final int MAIN_ACTION_ADDED = 1;
    public static final int TEN_PARAMETER_BUY_PERMIT_CARD = 10;
    public static final int FOUR_PARAMETER_BUY_PERMIT_CARD = 4;
    public static final int ONE_PARAMETER_BUY_PERMIT_CARD = 1;
    public static final int MONEY_EARNED_ELECT_COUNCILOR = 4;
    public static final int KING_PRICE = 2;
    public static final int EMPORIUMS_BUILDABLE = 10;
    public static final int KING_CARDS = 6;
    public static final int INITIAL_POSITION_ON_NOBILITY_PATH = 0;
    public static final int INITIAL_POSITION_ON_VICTORY_PATH = 0;
    public static final int FIRST_INITIAL_POSITION_ON_MONEY_PATH = 10;
    public static final int DEFAULT_MAIN_ACTION_COUNTER = 0;
    public static final int DEFAULT_FAST_ACTION_COUNTER = 0;
    public static final int DEFAULT_HELPER_COUNTER = 1;
    public static final int DEFAULT_POLITIC_CARD_HAND = 6;
    public static final int MAX_CLIENT_NUMBER = 10;

    //MARKET NAME
    public static final String PERMIT_CARD = "PERMIT_CARD";
    public static final String POLITIC_CARD = "POLITIC_CARD";

    //ROUND
    public static final long ROUND_DURATION = 2000000;

    // Folder
    public  static String IMAGE_PATH= "/ClientPackage/View/GUIResources/Image/";

    //ERROR MESSAGE
    public static final String MONEY_EXCEPTION= "You don\'t have enough money!";
    public static final String HELPER_EXCEPTION= "You don\'t have enough helper!";
    public static final String MAIN_ACTION_EXCEPTION= "You don\'t have enough main action!";
    public static final String FAST_ACTION_EXCEPTION= "You don\'t have enough fast action!";
    public static final String TURN_EXCEPTION= "This is\'t your turn!";
    public static final String EMPORIUM_PRESENT_EXCEPTION= "You have this emporium!";
    public static final String INCORRECT_PATH_EXCEPTION= "The path is incorrect, check it!";
    public static final String COUNCIL_NOT_PRESENT_EXCEPTION= "Council not present!";
    public static final String NOBILITY_PATH_EXCEPTION = "You can\'t go back in nobility path!";
    public static final String VICTORY_PATH_EXCEPTION = "You can\'t go back in victory path!";
    public static final String MONEY_PATH_EXCEPTION = "You can\'t go back in money path!";
    public static final String PERMIT_CARD_NOT_PRESENT_EXCEPTION = "This permit card is not present!";
    public static final String CITY_NOT_CORRECT_EXCEPTION = "City not correct exception";
    public static final String POLITIC_CARD_EXCEPTION = "Politic cards not correct, sorry bro!";
}
