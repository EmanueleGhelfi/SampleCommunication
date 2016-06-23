package CommonModel.GameModel.Path;

import Utilities.Class.Constants;
import Utilities.Exception.ActionNotPossibleException;
import CommonModel.GameModel.Bonus.Generic.Bonus;
import CommonModel.GameModel.Bonus.Generic.MainBonus;
import Server.Model.User;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Giulio on 14/05/2016.
 */
public class NobilityPath implements Path, Serializable {

    private Position[] position;
    private final int length = Constants.NOBILITY_PATH_ELEMENT;

    public NobilityPath() {
        position = new Position[length+1];
        for (int i = 0 ; i<length; i++){
            Bonus bonus = new MainBonus(1,3,10,true);
            if (i == 0)
                bonus.getBonusArrayList().clear();
            position[i] = new Position(i,bonus);
        }
    }

    @Override
    public void goAhead(User user, int value) throws ActionNotPossibleException {
        if(user.getNobilityPathPosition().getPosition()+value>length){
            user.setNobilityPathPosition(position[length]);
            position[length].getBonus().getBonus(user,user.getGame());
        }
        else{
            if(user.getNobilityPathPosition().getPosition()+value<0){
                throw new ActionNotPossibleException(Constants.NOBILITY_PATH_EXCEPTION);
            }
            else {
                user.setNobilityPathPosition(position[user.getNobilityPathPosition().getPosition()+value]);
                position[user.getNobilityPathPosition().getPosition()].getBonus().getBonus(user,user.getGame());
            }
        }
    }

    public Position[] getPosition() {
        return position;
    }
}
