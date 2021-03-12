import com.sun.org.apache.xml.internal.security.algorithms.JCEMapper;

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
            pathsOnBoard[i].randomInitPath();
        }
    }

    public int individualFitness(){
        return AlgorithmConfiguration.punishmentForPathsLength*getAllPathsLength() +
                AlgorithmConfiguration.punishmentForNumberOfSegments*getAllPathsNumberOfSegments() +
                AlgorithmConfiguration.punishmentForNumberOfPathsOutOfBoard*getNumberOfPathsOutOfBoard() +
                AlgorithmConfiguration.punishmentForPathsLengthOutOfBoard*getAllPathsOutOfBoardLength() +
                AlgorithmConfiguration.punishmentForIntersects*getAllIntersects();
    }

    public int getAllPathsLength(){
        int sum = 0;
        for (Path p: pathsOnBoard) {
            sum += p.getPathLength();
        }
        return sum;
    }

    public int getNumberOfPathsOutOfBoard(){
        int sum = 0;
        for (Path p: pathsOnBoard) {
            if(p.isOutOfBoard()){
                sum++;
            }
        }
        return sum;
    }


    public int getAllPathsOutOfBoardLength(){
        int sum = 0;
        for (Path p: pathsOnBoard) {
            sum += p.getPathLengthOutOfBoard();
        }
        return sum;
    }


    public int getAllPathsNumberOfSegments(){
        int sum = 0;
        for (Path p: pathsOnBoard) {
            sum += p.getNumberOfSegments();
        }
        return sum;
    }

    public int getAllIntersects(){
       ArrayList<Point> allPointsFromPaths = new ArrayList<>();
       ArrayList<Point> pointsWithoutDuplicates = new ArrayList<>();
        for (Path p: pathsOnBoard) {
            allPointsFromPaths.addAll(p.getAllPointsOnPath());
        }

        for(Point pFromAll : allPointsFromPaths){
            boolean isInListWithoutDuplicates = false;
            for(Point pFromWithoutDuplicates : pointsWithoutDuplicates){
                if (pFromAll.equals(pFromWithoutDuplicates)) {
                    isInListWithoutDuplicates = true;
                    break;
                }
            }
            if(!isInListWithoutDuplicates){
                pointsWithoutDuplicates.add(pFromAll);
            }
        }
        return allPointsFromPaths.size() - pointsWithoutDuplicates.size();
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

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("INDIVIDUAL: ");
        for (Path p: pathsOnBoard) {
            result.append("\t").append(p.toString()).append("\n");
        }
        return result.toString();
    }
}
