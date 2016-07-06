package ClientPackage.View.GUIResources.CustomComponent;

import javafx.scene.image.Image;
import javafx.stage.Screen;

import java.util.HashMap;

/**
 * Created by Emanuele on 22/06/2016.
 */
public class ImageLoader {

    private static final ImageLoader imageLoader = new ImageLoader();
    private final double width = Screen.getPrimary().getBounds().getWidth();
    private final double height = Screen.getPrimary().getBounds().getWidth();
    private HashMap<String, Image> imageHashMap = new HashMap<>();

    private ImageLoader() {
        this.imageHashMap = new HashMap<>();
    }

    public static ImageLoader getInstance() {
        return ImageLoader.imageLoader;
    }

    public Image getImage(String path) {
        if (!this.imageHashMap.containsKey(path)) {
            Image image = null;
            if (!path.contains("Map")) {
                image = new Image(path, true);
            } else {
                image = new Image(path, true);
            }
            this.imageHashMap.put(path, image);
        }
        return this.imageHashMap.get(path);
    }

    public Image getImage(String path, double width, double height) {
        if (!this.imageHashMap.containsKey(path)) {
            Image image = null;
            if (!path.contains("Map")) {
                image = new Image(path, width, height, true, true, true);
            } else {
                image = new Image(path, width, height, true, true, true);
            }
            this.imageHashMap.put(path, image);
        }
        return this.imageHashMap.get(path);
    }
}
