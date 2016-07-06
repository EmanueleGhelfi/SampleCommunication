package ClientPackage.View.GUIResources.CustomComponent;

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

    private final Gauge gauge;
    private final MatchController matchController;
    private final ClientController clientController;
    private double time = Constants.ROUND_DURATION - 5000;

    public TimerProgress(Gauge gauge, MatchController matchController, ClientController clientController) {
        this.gauge = gauge;
        gauge.setMaxValue(this.time / 1000);
        this.matchController = matchController;
        this.clientController = clientController;
    }

    @Override
    public void run() {
        this.time -= 1000;
        if (this.time >= 0) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    TimerProgress.this.gauge.setValue(TimerProgress.this.time / 1000);
                }
            });
        }
        if (this.time == 0) {
            this.clientController.onFinishTurn();
        }
    }
}
