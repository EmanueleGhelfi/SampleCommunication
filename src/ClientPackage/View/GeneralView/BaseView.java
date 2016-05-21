package ClientPackage.View.GeneralView;

import Server.Model.Map;

import java.util.ArrayList;

/**
 * Created by Emanuele on 13/05/2016.
 */
public interface BaseView {

    void initView();

    void showLoginError();

    void showWaitingForStart();

    void showMap(ArrayList<Map> mapArrayList);
}
