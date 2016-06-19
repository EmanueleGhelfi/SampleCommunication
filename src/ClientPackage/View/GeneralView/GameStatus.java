package ClientPackage.View.GeneralView;

/**
 * Created by Emanuele on 19/06/2016.
 */
public class GameStatus {

    private static GameStatus instance;
    private StatusValue statusValue;

    public GameStatus() {
    }

    public static GameStatus getInstance() {
        if(instance==null){
            instance= new GameStatus();
        }
        return instance;
    }

    public void changeStatus(StatusValue statusValue){
        this.statusValue = statusValue;
    }
}
