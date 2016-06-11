package Utilities.Class;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * Created by Giulio on 08/06/2016.
 */
public class Graphics {

    public void playSomeSound(String path){
        try{
            Media buttonSound = new Media(getClass().getResource(path).toString());
            MediaPlayer mediaPlayer = new MediaPlayer(buttonSound);
            mediaPlayer.play();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void fadeTransitionEffect(Node nodeToEffect, float fromValue, float toValue, int duration){
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(duration), nodeToEffect);
        fadeTransition.setFromValue(fromValue);
        fadeTransition.setToValue(toValue);
        fadeTransition.setAutoReverse(true);
        fadeTransition.play();
    }

    public static void scaleTransition(Node node, float toValueX, float toValueY, int duration) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(duration), node);
        scaleTransition.setCycleCount(1);
        scaleTransition.setInterpolator(Interpolator.EASE_BOTH);
        scaleTransition.setFromX(node.getScaleX());
        scaleTransition.setFromY(node.getScaleY());
        scaleTransition.setToX(toValueX);
        scaleTransition.setToY(toValueY);
        scaleTransition.playFromStart();
    }

    public static Animation scaleTransitionEffectCycle (Node node, float toValueX, float toValueY, BooleanProperty stopTransition){
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(500), node);
        scaleTransition.setCycleCount(Animation.INDEFINITE);
        scaleTransition.setInterpolator(Interpolator.EASE_BOTH);
        scaleTransition.setFromX(node.getScaleX());
        scaleTransition.setFromY(node.getScaleY());
        scaleTransition.setToX(toValueX);
        scaleTransition.setToY(toValueY);
        scaleTransition.playFromStart();
        stopTransition.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    scaleTransition.stop();
                    node.setScaleX(1);
                    node.setScaleY(1);
                }
            }
        });
        return scaleTransition;
    }

}
