package CommonModel.GameModel.Action;

import ClientPackage.Service.FactoryService;
import CommonModel.GameModel.ActionNotPossibleException;
import CommonModel.GameModel.Card.PermitCard;
import CommonModel.GameModel.City.*;
import Server.Model.Game;
import Server.UserClasses.User;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.ArrayList;

/**
 * Created by Giulio on 16/05/2016.
 */
public class BuildWithPermitCard implements Action{

    private PermitCard permitCard;
    private City city;
    private final String type = "MAIN_ACTION";

    public BuildWithPermitCard(City city, PermitCard permitCard) {
        this.city = city;
        this.permitCard = permitCard;
    }

    @Override
    public void doAction(Game game, User user) throws ActionNotPossibleException {
        City gameCity = game.getCity(city);
        int helperToSpend = 0;
        for (User userToFind: game.getUsers()) {
            if (userToFind.getUsersEmporium().contains(gameCity)){
                helperToSpend++;
            }
        }
        if (user.getHelpers()>=helperToSpend){
            user.setHelpers(user.getHelpers()-helperToSpend);
            user.addEmporium(gameCity);
            gameCity.getBonus().getBonus(user, game);
            CityVisitor cityVisitor = new CityVisitor(game.getGraph(), user.getUsersEmporium());
            for (City cityToVisit : cityVisitor.visit(gameCity)) {
                cityToVisit.getBonus().getBonus(user, game);
            }
            if (gameCity.getRegion().checkRegion(user.getUsersEmporium())){
                //TODO premio bonus card regione + king
            }
            if (gameCity.getColor().checkColor(user.getUsersEmporium())){
                //TODO premio bonus card colore + king
            }
        } else {
            throw new ActionNotPossibleException();
        }


    }

    @Override
    public String getType() {
        return type;
    }








    //TODO TEST!!!!
    public static void main(String[] args){
        City framek = new City(Color.BLUE, CityName.FRAMEK, Region.COAST);
        City indur = new City(Color.GREY,CityName.INDUR,Region.COAST);
        City juvelar = new City(Color.BLUE,CityName.JUVELAR,Region.COAST);
        City graden = new City(Color.GREY,CityName.GRADEN,Region.COAST);
        City hellar = new City(Color.BLUE,CityName.HELLAR,Region.COAST);
        City castrum = new City(Color.GREY,CityName.CASTRUM,Region.COAST);
        City arkon = new City(Color.GREY,CityName.ARKON,Region.COAST);
        City burgen = new City(Color.BLUE,CityName.BURGEN,Region.COAST);
        City dorful = new City(Color.GREY,CityName.DORFUL,Region.COAST);
        City esti = new City(Color.GREY,CityName.GRADEN,Region.COAST);

        UndirectedGraph<City,DefaultEdge> graph = new SimpleGraph<City, DefaultEdge>(DefaultEdge.class);
        graph.addVertex(framek);
        graph.addVertex(indur);
        graph.addVertex(juvelar);
        graph.addVertex(graden);
        graph.addVertex(hellar);
        graph.addVertex(castrum);
        graph.addVertex(arkon);
        graph.addVertex(burgen);
        graph.addVertex(dorful);
        graph.addVertex(esti);

        graph.addEdge(framek,indur);
        graph.addEdge(graden,juvelar);
        graph.addEdge(juvelar,indur);
        graph.addEdge(juvelar,hellar);
        graph.addEdge(castrum,framek);
        graph.addEdge(arkon,castrum);
        graph.addEdge(arkon,burgen);
        graph.addEdge(burgen,dorful);
        graph.addEdge(burgen,esti);
        graph.addEdge(dorful,graden);
        graph.addEdge(esti,hellar);
        ArrayList<City> usersEmporium = new ArrayList<>();
        usersEmporium.add(framek);
        usersEmporium.add(indur);
        usersEmporium.add(juvelar);
        usersEmporium.add(graden);
        usersEmporium.add(esti);

        Game game = new Game();

        game.setGraph(graph);
        BuildWithPermitCard buildWithPermitCard = new BuildWithPermitCard(arkon);
        buildWithPermitCard.doAction(game, new User(FactoryService.getService));
    }

}
