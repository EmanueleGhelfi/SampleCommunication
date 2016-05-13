package ClientPackage.View;

/**
 * Created by Emanuele on 13/05/2016.
 */
public class FactoryView {

    public static BaseView getBaseView(String viewType) throws ViewException {
        switch (viewType){
            case "GUI":
                return new GUIView();
            case "CLI":
                return new CLIView();
        }

        throw new ViewException("ViewType Not Supported");
    }
}
