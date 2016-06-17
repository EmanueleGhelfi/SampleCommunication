package ClientPackage.View.GUIResources.Class;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * Created by Emanuele on 17/06/2016.
 */
public class BringUpHandler implements EventHandler<MouseEvent> {

    private Node node;

    public BringUpHandler(Node node) {
        this.node=node;

    }

    @Override
    public void handle(MouseEvent event) {
        node.setTranslateY(-3);
    }

    public void setNormalPosition(){
        node.setTranslateY(3);
    }
}
