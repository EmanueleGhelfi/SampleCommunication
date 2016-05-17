package ClientPackage.View.GeneralView;

import ClientPackage.Controller.ClientController;
import Utilities.Exception.ViewException;

/**
 * Created by Emanuele on 13/05/2016.
 */
public class FactoryView {

    public static BaseView getBaseView(String viewType, ClientController clientController) throws ViewException {
        switch (viewType){
            case "GUI":
                return new GUIView(clientController);
            case "CLI":
                return new CLIView(clientController);
        }

        throw new ViewException("ViewType Not Supported");
    }
}
