import java.lang.reflect.Array;
import java.util.ArrayList;

public class Individual {
    private Path [] pathsOnBoard; //individual - solution is array of paths between connections on board

    public Individual(int numberOfPaths){
        pathsOnBoard = new Path [numberOfPaths];
    }


    //GETTERS AND SETTERS
    public Path[] getPathsOnBoard() {
        return pathsOnBoard;
    }

    public void setPathsOnBoard(Path[] pathsOnBoard) {
        this.pathsOnBoard = pathsOnBoard;
    }
}
