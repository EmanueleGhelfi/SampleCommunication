package Utilities.Class;

import ClientPackage.View.GUIResources.CustomComponent.BringUpHandler;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 * Created by Giulio on 08/06/2016.
 */
public class Graphics {

    public static void playSomeSound(String path){
        try{
            Media buttonSound = new Media(Graphics.class.getClassLoader().getResource("ClientPackage/View/GUIResources/Sound/" + path + ".wav").toString());
            MediaPlayer mediaPlayer = new MediaPlayer(buttonSound);
            mediaPlayer.play();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void bringUpImages(Node... nodes) {
        for (Node node: nodes) {
            BringUpHandler bringUpHandler = new BringUpHandler(node);
            node.setOnMouseEntered(bringUpHandler);
            node.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    bringUpHandler.setNormalPosition();
                }
            });
        }
    }

    public static void fadeTransitionEffect(Node nodeToEffect, float fromValue, float toValue, int duration){
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(duration), nodeToEffect);
        fadeTransition.setFromValue(fromValue);
        fadeTransition.setToValue(toValue);
        fadeTransition.setAutoReverse(true);
        fadeTransition.play();
    }

    public static void addShadow(Node node){
        int depth = 70; //Setting the uniform variable for the glow width and height
        DropShadow borderGlow= new DropShadow();
        borderGlow.setOffsetY(0f);
        borderGlow.setOffsetX(0f);
        borderGlow.setColor(Color.BLACK);
        borderGlow.setWidth(depth);
        borderGlow.setHeight(depth);
        node.setEffect(borderGlow);
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

    /** Metodo di notifica, ossia quando viene chiamato in basso a destra spunta una notifica che dopo 3 secondi sparisce.
     * @param messageOfTheMoment è il testo che sarà stampato a video nella notifica.
     */
    public static void notification(String messageOfTheMoment){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Image icona = new Image(Constants.IMAGE_PATH+Constants.NOTIFICATION_ICON);
                TrayNotification tray = new TrayNotification(Constants.NOTIFICATION_TEXT, messageOfTheMoment, NotificationType.SUCCESS);
                tray.setImage(icona);
                tray.showAndWait();
                tray.showAndDismiss(Duration.seconds(3));
            }
        });
    }

    public static void addBorder(Node node) {
        node.getStyleClass().add("bordered");
        DropShadow ds = new DropShadow( 10, Color.AQUA );
        node.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                node.setEffect(ds);
            }
        });
        node.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                node.setEffect(null);
            }
        });
    }


}
