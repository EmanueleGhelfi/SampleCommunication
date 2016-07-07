package ClientPackage.View.GeneralView;

import ClientPackage.Controller.ClientController;
import Utilities.Class.Constants;
import Utilities.Exception.ViewException;

/** This class is used for the creation according to the choice of a view.
 * Created by Emanuele on 13/05/2016.
 */
public class FactoryView {

    public static BaseView getBaseView(String viewType, ClientController clientController) throws ViewException {
        switch (viewType) {
            case Constants.GUI:
                return new GUIView(clientController);
            case Constants.CLI:
                return new CLIView(clientController);
        }
        throw new ViewException("ViewType Not Supported");
    }

}
