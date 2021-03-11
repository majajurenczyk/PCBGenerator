import java.io.*;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.ArrayList;

public class PCB { //PCB is problem instance < - > we are trying to find best solution for defined board
    private int boardWidth; //PCB width
    private int boardHeight; //PCB height
    private ArrayList<Point> boardDefinedPoints; //Available points
    private ArrayList<Connection> boardDefinedConnections; //Connections between points

    private Population boardSolutionsPopulation = null; //every solution is individual

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

    //METHODS

    public void printPCB(){
        initPopulation(); //?????????????????????????????XD
    }

    public void initPopulation(){
        if(boardSolutionsPopulation == null){
            boardSolutionsPopulation = new Population(AlgorithmConfiguration.populationSize, this);
            boardSolutionsPopulation.randomInitPopulation();
        }
        else{
            boardSolutionsPopulation.randomInitPopulation();
        }
    }

    public boolean readAndSetPCBParamsFromFile(String filePath) {
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

    public boolean validatePCB(){
        return validatePointsOnBoard() && validateConnectionsOnBoard();
    }

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

    //OVERRIDE FROM OBJECT


    @Override
    public String toString() {
        return "width: " + this.boardWidth + "\n"+
                "height: " + this.boardHeight + "\n"+
                "points: " + this.boardDefinedPoints + "\n"+
                "connections: " + this.boardDefinedConnections;
    }

    //GETTER AND SETTERS
    public int getBoardWidth() {
        return boardWidth;
    }

    public void setBoardWidth(int boardWidth) {
        this.boardWidth = boardWidth;
    }

    public int getBoardHeight() {
        return boardHeight;
    }

    public void setBoardHeight(int boardHeight) {
        this.boardHeight = boardHeight;
    }

    public ArrayList<Point> getBoardDefinedPoints() {
        return boardDefinedPoints;
    }

    public void setBoardDefinedPoints(ArrayList<Point> boardDefinedPoints) {
        this.boardDefinedPoints = boardDefinedPoints;
    }

    public ArrayList<Connection> getBoardDefinedConnections() {
        return boardDefinedConnections;
    }

    public void setBoardDefinedConnections(ArrayList<Connection> boardDefinedConnections) {
        this.boardDefinedConnections = boardDefinedConnections;
    }

    public static void main(String[] args) {
        PCB pcb = new PCB();
        boolean res = pcb.readAndSetPCBParamsFromFile("C:\\Users\\User\\Desktop\\3rok\\6sem\\SI\\L\\lab1\\PCBGenerator\\src\\zad0.txt");
        System.out.println(pcb);
    }
}
