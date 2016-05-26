package ClientPackage.View.GUIResources.customComponent;

import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import org.controlsfx.control.HiddenSidesPane;

/**
 * Created by Emanuele on 26/05/2016.
 */
public class SideNode extends VBox {

    public SideNode(double spacing, Side side, HiddenSidesPane pane, Node... children) {
        super(spacing, children);
        setAlignment(Pos.CENTER);
        setPrefSize(100, 100);
    }
}
