package CommonModel.GameModel.City;

import CommonModel.GameModel.Council.Bank;
import CommonModel.Snapshot.BaseUser;
import Server.Model.User;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Emanuele on 06/07/2016.
 */
public class RegionTest {

    private Region region;

    @Before
    public void setUp() throws Exception {
        region = new Region(RegionName.COAST,5,new Bank());

    }

    @Test
    public void testCheckRegion() {
        ArrayList<City> usersEmporium = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            usersEmporium.add(new City(Color.BLUE, CityName.ARKON, RegionName.COAST));
        }

        assertTrue(region.checkRegion(usersEmporium));

        usersEmporium.remove(0);

        assertFalse(region.checkRegion(usersEmporium));


        usersEmporium.clear();

        for (int i = 0; i < 5; i++){
            usersEmporium.add(new City(Color.BLUE, CityName.ARKON, RegionName.HILL));
        }

        assertFalse(region.checkRegion(usersEmporium));

    }

}