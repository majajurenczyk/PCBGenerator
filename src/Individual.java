import com.sun.org.apache.xml.internal.security.algorithms.JCEMapper;

import java.util.ArrayList;

public class Individual implements Comparable {
    private Path [] pathsOnBoard; //individual - solution is array of paths between connections on board
    private PCB problem;

    private int individualFitness;

    public Individual(PCB pcb){
        pathsOnBoard = new Path [pcb.getBoardDefinedConnections().size()]; //ARRAY OF PATHS (LIKE ONE SOLVED PCB)
        problem = pcb;
    }

    public Individual(Path [] pathsOnBoard, PCB pcb){
        this.pathsOnBoard = pathsOnBoard;
        this.problem = pcb;
    }

    public void randomInitIndividual(){
        for(int i = 0; i < pathsOnBoard.length; i++){
            pathsOnBoard[i] = new Path(problem.getBoardDefinedConnections().get(i)/*, this*/);
            pathsOnBoard[i].randomInitPath();
        }

        setIndividualFitness(countIndividualFitness());
    }

    public Individual deepCopyIndividual(){
        Path [] newPaths = new Path[pathsOnBoard.length];
        Individual result = new Individual(newPaths, this.problem);
        for (int i = 0; i < pathsOnBoard.length; i++){
            result.pathsOnBoard[i] = pathsOnBoard[i].deepCopyPath();
        }
        result.setIndividualFitness(this.individualFitness);
        return result;
    }

    public int countIndividualFitness(){ //sum of punishments with their weighs
        return AlgorithmConfiguration.punishmentForPathsLength*getAllPathsLength() +
                AlgorithmConfiguration.punishmentForNumberOfSegments*getAllPathsNumberOfSegments() +
                AlgorithmConfiguration.punishmentForNumberOfPathsOutOfBoard*getNumberOfPathsOutOfBoard(problem) +
                AlgorithmConfiguration.punishmentForPathsLengthOutOfBoard*getAllPathsOutOfBoardLength(problem) +
                AlgorithmConfiguration.punishmentForIntersects*getAllIntersects();
    }

    public void setIndividualFitness(int individualFitness){
        this.individualFitness = individualFitness;
    }

    public int getIndividualFitness(){
        return individualFitness;
    }

    private int getAllPathsLength(){
        int sum = 0;
        for (Path p: pathsOnBoard) {
            sum += p.getPathLength();
        }
        return sum;
    }

    private int getNumberOfPathsOutOfBoard(PCB problem){
        int sum = 0;
        for (Path p: pathsOnBoard) {
            if(p.isOutOfBoard(problem.getBoardWidth(), problem.getBoardHeight())){
                sum++;
            }
        }
        return sum;
    }

    private int getAllPathsOutOfBoardLength(PCB problem){
        int sum = 0;
        for (Path p: pathsOnBoard) {
            sum += p.getPathLengthOutOfBoard(problem.getBoardWidth(), problem.getBoardHeight());
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
        return allPointsFromPaths.size() - pointsWithoutDuplicates.size() + 1; //NUMBER OF INTERSECTS IS NUMBER OF DUPLICATES IN ALL POINTS
    }

    //GETTERS AND SETTERS
    public Path[] getPathsOnBoard() {
        return pathsOnBoard;
    }

    //OVERRIDE FROM OBJECT
    //@Override
    /*public String toString() {
        StringBuilder result = new StringBuilder("\nINDIVIDUAL: ");
        for (Path p: pathsOnBoard) {
            result.append("\t").append(p.toString()).append("\n");
        }
        return result.toString();
    }*/


    @Override
    public int compareTo(Object o) { //INDIVIDUALS WITH LOWER FITNESS ARE BETTER
        return -1 * Double.compare(this.individualFitness, ((Individual) o).individualFitness);
    }
}
