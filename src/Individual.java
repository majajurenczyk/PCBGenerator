import java.lang.reflect.Array;
import java.util.ArrayList;

public class Individual {
    private Path [] pathsOnBoard; //individual - solution is array of paths between connections on board
    private Population individualsPopulation;

    public Individual(Population population){
        pathsOnBoard = new Path [population.getProblem().getBoardDefinedConnections().size()];
        individualsPopulation = population;
    }

    public void randomInitIndividual(){
        for(int i = 0; i < pathsOnBoard.length; i++){
            pathsOnBoard[i] = new Path(individualsPopulation.getProblem().getBoardDefinedConnections().get(i), this);
        }
    }


    //GETTERS AND SETTERS
    public Path[] getPathsOnBoard() {
        return pathsOnBoard;
    }

    public void setPathsOnBoard(Path[] pathsOnBoard) {
        this.pathsOnBoard = pathsOnBoard;
    }

    public Population getIndividualsPopulation() {
        return individualsPopulation;
    }
}
