package ClientPackage.View.GUIResources.CustomComponent;

import javafx.scene.image.Image;
import javafx.stage.Screen;
import java.util.HashMap;

/**
 * Created by Emanuele on 22/06/2016.
 */
public class ImageLoader {

    private static ImageLoader imageLoader = new ImageLoader();

    private HashMap<String,Image> imageHashMap = new HashMap<>();

    private double width = Screen.getPrimary().getBounds().getWidth();

    private double height = Screen.getPrimary().getBounds().getWidth();

    private ImageLoader(){
        imageHashMap = new HashMap<>();
    }

    public static ImageLoader getInstance() {
        return imageLoader;
    }

    public Image getImage(String path){
        if(!imageHashMap.containsKey(path)){
            Image image=null;
            if(!path.contains("Map")) {
                 image = new Image(path,true);
            }
            else{
                image=new Image(path,true);
            }
            imageHashMap.put(path,image);
        }
        return imageHashMap.get(path);
    }

    public Image getImage(String path, double width, double height) {
        if(!imageHashMap.containsKey(path)){
            Image image=null;
            if(!path.contains("Map")) {
                image = new Image(path,width,height,true, true,true);
            }
            else{
                image=new Image(path,width,height,true,true,true);
            }
            imageHashMap.put(path,image);
        }
        return imageHashMap.get(path);
    }
}
