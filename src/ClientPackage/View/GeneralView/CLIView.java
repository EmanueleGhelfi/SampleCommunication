package ClientPackage.View.GeneralView;

import ClientPackage.Controller.ClientController;
import ClientPackage.View.GeneralView.BaseView;

/**
 * Created by Emanuele on 13/05/2016.
 */
public class CLIView implements BaseView {


    public CLIView(ClientController clientController) {
        // to implement
    }

    @Override
    public void initView() {
        System.out.println("CLI Started correctly");

    }

    @Override
    public void showLoginError() {

    }

    @Override
    public void showWaitingForStart() {

    }
}
