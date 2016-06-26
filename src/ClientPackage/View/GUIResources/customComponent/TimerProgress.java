package ClientPackage.View.GUIResources.customComponent;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.GUIResources.Class.MatchController;
import Utilities.Class.Constants;
import eu.hansolo.medusa.Gauge;
import javafx.application.Platform;

import java.util.TimerTask;

/**
 * Created by Emanuele on 25/06/2016.
 */
public class TimerProgress extends TimerTask {

    private Gauge gauge;
    private double time= Constants.ROUND_DURATION-5000;
    private MatchController matchController;
    private ClientController clientController;

    public TimerProgress(Gauge gauge, MatchController matchController, ClientController clientController) {
        this.gauge=gauge;
        gauge.setMaxValue(time/1000);
        this.matchController=matchController;
        this.clientController=clientController;
    }

    @Override
    public void run() {

        time-=1000;

        if(time>=0) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    gauge.setValue(time / 1000);
                }
            });
        }

        if(time==0){
            clientController.onFinishTurn();
        }
    }
}
