package CommonModel.GameModel.Path;

import Utilities.Exception.ActionNotPossibleException;
import CommonModel.GameModel.Bonus.Bonus;
import CommonModel.GameModel.Bonus.MainBonus;
import CommonModel.GameModel.Position;
import Server.Model.User;

/**
 * Created by Giulio on 14/05/2016.
 */
public class NobilityPath implements Path {

    private Position[] position;
    private final int length = 21;

    public NobilityPath() {
        position = new Position[length+1];
        for (int i = 0 ; i<length; i++){
            Bonus bonus = new MainBonus(1,2,9,true);
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
                throw new ActionNotPossibleException();
            }
            else {
                user.setNobilityPathPosition(position[user.getNobilityPathPosition().getPosition()+value]);
                position[user.getNobilityPathPosition().getPosition()+value].getBonus().getBonus(user,user.getGame());
            }
        }

    }
}
