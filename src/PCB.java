import java.io.*;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class PCB { //PCB is problem instance < - > we are trying to find best solution for defined board
    private int boardWidth; //PCB width
    private int boardHeight; //PCB height
    private ArrayList<Point> boardDefinedPoints; //Available points
    private ArrayList<Connection> boardDefinedConnections; //Connections between points


    public PCB(int width, int height, ArrayList<Point> points, ArrayList<Connection> connections){
        this.boardWidth = width;
        this.boardHeight = height;
        this.boardDefinedPoints = points;
        this.boardDefinedConnections = connections;
    }

    public PCB(){
        boardDefinedPoints = new ArrayList<>();
        boardDefinedConnections = new ArrayList<>();
    }

    //OPERATORS
    private Population initPopulation(){
        Population boardSolutionsPopulation = new Population(AlgorithmConfiguration.populationSize, this);
        boardSolutionsPopulation.randomInitPopulation();
        return boardSolutionsPopulation;
    }


    //METHODS
    private boolean readAndSetPCBParamsFromFile(String filePath) {
        if (!isPathValid(filePath))
                return false;
        try {
            File problemInitFile = new File(filePath);
            BufferedReader initFileReader = new BufferedReader(new FileReader(problemInitFile));

            return readAndSetBoardSizeFromFile(initFileReader) &&
                    readAndSetConnectionsAndPointsFromFile(initFileReader);
        }
        catch (IOException | NumberFormatException e){
            e.printStackTrace();
            return false;
        }
    }

    private boolean isPathValid(String path){
        try {
            Paths.get(path);
            return true;
        }
        catch (InvalidPathException ex) {
            return false;
        }
    }

    private boolean readAndSetBoardSizeFromFile(BufferedReader fileReader){
        try {
            String[] boardSizeParams = fileReader.readLine().split(";");
            setBoardWidth(Integer.parseInt(boardSizeParams[0]));
            setBoardHeight(Integer.parseInt(boardSizeParams[1]));
            return true;
        }
        catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    private boolean readAndSetConnectionsAndPointsFromFile(BufferedReader fileReader){
        String connectionLine;
        String [] actualConnectionParams;
        try {
            while ((connectionLine = fileReader.readLine()) != null) {
                actualConnectionParams = connectionLine.split(";");
                Point firstPoint = new Point(Integer.parseInt(actualConnectionParams[0]), Integer.parseInt(actualConnectionParams[1]));
                Point secondPoint = new Point(Integer.parseInt(actualConnectionParams[2]), Integer.parseInt(actualConnectionParams[3]));
                Connection connection = new Connection(firstPoint, secondPoint);
                this.boardDefinedPoints.add(firstPoint);
                this.boardDefinedPoints.add(secondPoint);
                this.boardDefinedConnections.add(connection);
            }
            return true;
        }
        catch (IOException | NumberFormatException e){
            e.printStackTrace();
            return false;
        }
    }

    //OVERRIDE FROM OBJECT

    @Override
    public String toString() {
        return "width: " + this.boardWidth + "\n"+
                "height: " + this.boardHeight + "\n"+
                "points: " + this.boardDefinedPoints + "\n"+
                "connections: " + this.boardDefinedConnections;
    }

    //GETTER AND SETTERS

    int getBoardWidth() {
        return boardWidth;
    }

    private void setBoardWidth(int boardWidth) {
        this.boardWidth = boardWidth;
    }

    int getBoardHeight() {
        return boardHeight;
    }

    private void setBoardHeight(int boardHeight) {
        this.boardHeight = boardHeight;
    }

    public ArrayList<Point> getBoardDefinedPoints() {
        return boardDefinedPoints;
    }

    public void setBoardDefinedPoints(ArrayList<Point> boardDefinedPoints) {
        this.boardDefinedPoints = boardDefinedPoints;
    }

    ArrayList<Connection> getBoardDefinedConnections() {
        return boardDefinedConnections;
    }

    public void setBoardDefinedConnections(ArrayList<Connection> boardDefinedConnections) {
        this.boardDefinedConnections = boardDefinedConnections;
    }

    //ADDITIONALLY

    private boolean validatePointsOnBoard(){
        for (Point p : this.boardDefinedPoints) {
            if(!checkIfPointIsOnBoard(p))
                return false;
        }
        return true;
    }

    private boolean validateConnectionsOnBoard(){
        for(int i = 0; i < this.boardDefinedConnections.size(); i++){
            for(int j = i+1; j < this.boardDefinedConnections.size(); j++){
                if(this.boardDefinedConnections.get(i).equals(this.boardDefinedConnections.get(j))){
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkIfPointIsOnBoard(Point p){
        return p.getX() <= this.boardWidth && p.getY() <= this.boardHeight && p.getX() >= 0 && p.getY() >= 0;
    }

    //===================================================================================================================='


    public static void main(String[] args) {
        PCB pcb = new PCB();
        boolean res = pcb.readAndSetPCBParamsFromFile("C:\\Users\\User\\Desktop\\3rok\\6sem\\SI\\L\\lab1\\PCBGenerator\\src\\zad0.txt");
        System.out.println(pcb);

        System.out.println("========================================");

        Population pop = pcb.initPopulation();
        //Population pop1 = new Population(AlgorithmConfiguration.populationSize, pcb);

        //System.out.println(pop.toString());
        //System.out.println(pop.getFitnessForAllSolutions());

        Individual ind1 = GeneticOperators.selectionOperatorTournament(pop);
        Individual ind2 = GeneticOperators.selectionOperatorTournament(pop);

        System.out.println(ind1.toString());
        System.out.println(ind2.toString());

        if(ind1 != ind2){
            Individual result = GeneticOperators.crossing(ind1, ind2);
            System.out.println(result.toString());
        }
    }
}
