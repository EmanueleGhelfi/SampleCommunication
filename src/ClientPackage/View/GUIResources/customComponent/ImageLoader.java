package ClientPackage.View.GUIResources.customComponent;

import javafx.scene.image.Image;

import java.util.HashMap;

/**
 * Created by Emanuele on 22/06/2016.
 */
public class ImageLoader {

    private static ImageLoader imageLoader = new ImageLoader();

    private HashMap<String,Image> imageHashMap = new HashMap<>();

    private ImageLoader(){
        imageHashMap = new HashMap<>();
    }

    public static ImageLoader getInstance() {
        return imageLoader;
    }




    public Image getImage(String path){
        if(!imageHashMap.containsKey(path)){
            Image image = new Image(path,true);
            imageHashMap.put(path,image);
        }
        return imageHashMap.get(path);
    }
}
