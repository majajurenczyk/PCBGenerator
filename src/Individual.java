import com.sun.org.apache.xml.internal.security.algorithms.JCEMapper;

import java.util.ArrayList;

public class Individual {
    private Path [] pathsOnBoard; //individual - solution is array of paths between connections on board
    private Population individualsPopulation;

    private double individualFitInPopulation = 0;

    Individual(Population population){
        pathsOnBoard = new Path [population.getProblem().getBoardDefinedConnections().size()]; //ARRAY OF PATHS (LIKE ONE SOLVED PCB)
        individualsPopulation = population;
    }

    void randomInitIndividual(){
        for(int i = 0; i < pathsOnBoard.length; i++){
            pathsOnBoard[i] = new Path(individualsPopulation.getProblem().getBoardDefinedConnections().get(i), this);
            pathsOnBoard[i].randomInitPath();
        }
    }

    int individualPunishment(){ //sum of punishments with their weighs
        return AlgorithmConfiguration.punishmentForPathsLength*getAllPathsLength() +
                AlgorithmConfiguration.punishmentForNumberOfSegments*getAllPathsNumberOfSegments() +
                AlgorithmConfiguration.punishmentForNumberOfPathsOutOfBoard*getNumberOfPathsOutOfBoard() +
                AlgorithmConfiguration.punishmentForPathsLengthOutOfBoard*getAllPathsOutOfBoardLength() +
                AlgorithmConfiguration.punishmentForIntersects*getAllIntersects();
    }

    //////////////////////

    double countIndividualFitInPopulation(int minPunishInPopulation){ //counts fitness for individual based on smallest individual punishment
        return (double)minPunishInPopulation/(double)individualPunishment();
    }

    void setIndividualFitInPopulation(double individualFitInPopulation){ //it sets individuals fitness
        this.individualFitInPopulation = individualFitInPopulation;
    }

    double getIndividualFit(){ //gets individual fitness
        return individualFitInPopulation;
    }
    //////////////////////

    private int getAllPathsLength(){
        int sum = 0;
        for (Path p: pathsOnBoard) {
            sum += p.getPathLength();
        }
        return sum;
    }

    private int getNumberOfPathsOutOfBoard(){
        int sum = 0;
        for (Path p: pathsOnBoard) {
            if(p.isOutOfBoard()){
                sum++;
            }
        }
        return sum;
    }

    private int getAllPathsOutOfBoardLength(){
        int sum = 0;
        for (Path p: pathsOnBoard) {
            sum += p.getPathLengthOutOfBoard();
        }
        return sum;
    }


    private int getAllPathsNumberOfSegments(){
        int sum = 0;
        for (Path p: pathsOnBoard) {
            sum += p.getNumberOfSegments();
        }
        return sum;
    }

    private int getAllIntersects(){
       ArrayList<Point> allPointsFromPaths = new ArrayList<>();
       ArrayList<Point> pointsWithoutDuplicates = new ArrayList<>();
        for (Path p: pathsOnBoard) {
            allPointsFromPaths.addAll(p.getAllPointsOnPath()); //SUM EVERY POINT ON PCB
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
        return allPointsFromPaths.size() - pointsWithoutDuplicates.size(); //NUMBER OF INTERSECTS IS NUMBER OF DUPLICATES IN ALL POINTS
    }

    //GETTERS AND SETTERS
    public Path[] getPathsOnBoard() {
        return pathsOnBoard;
    }

    public void setPathsOnBoard(Path[] pathsOnBoard) {
        this.pathsOnBoard = pathsOnBoard;
    }

    Population getIndividualsPopulation() {
        return individualsPopulation;
    }

    //OVERRIDE FROM OBJECT
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("\nINDIVIDUAL: ");
        for (Path p: pathsOnBoard) {
            result.append("\t").append(p.toString()).append("\n");
        }
        return result.toString();
    }
}
