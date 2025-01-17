import javafx.stage.Stage;

import java.io.*;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.*;

public class PCB { //PCB is problem instance < - > we are trying to find best solution for defined board
    private int boardWidth; //PCB width
    private int boardHeight; //PCB height
    private ArrayList<Point> boardDefinedPoints; //Available points
    private ArrayList<Connection> boardDefinedConnections; //Connections between points

    private ArrayList<Population> populations;


    public PCB(int width, int height, ArrayList<Point> points, ArrayList<Connection> connections) {
        this.boardWidth = width;
        this.boardHeight = height;
        this.boardDefinedPoints = points;
        this.boardDefinedConnections = connections;

        populations = new ArrayList<>();
    }

    public PCB() {
        boardDefinedPoints = new ArrayList<>();
        boardDefinedConnections = new ArrayList<>();
        populations = new ArrayList<>();
    }

    private Population initPopulation() {
        Population boardSolutionsPopulation = new Population(AlgorithmConfiguration.populationSize);
        boardSolutionsPopulation.randomInitPopulation(this);
        return boardSolutionsPopulation;
    }

    public Individual findSolution() {
        populations.clear();
        Population initial = initPopulation();
        populations.add(initial);

        Population actPop = initial;
        Arrays.sort(actPop.getIndividualsInPopulation(), Individual::compareTo);
        System.out.println("punish. before: ");
        initial.showIndividualsPunishments();

        for (int i = 0; i < AlgorithmConfiguration.numberOfPopulations; i++) {
            Population nextPopulation = new Population(AlgorithmConfiguration.populationSize);
            int counter = 0;
            while (counter != AlgorithmConfiguration.populationSize) {
                Individual firstParent = GeneticOperators.selectionOperatorTournament(actPop);
                Individual secondParent = GeneticOperators.selectionOperatorTournament(actPop);
                Individual child = GeneticOperators.crossing(firstParent, secondParent);
                child = GeneticOperators.mutationRand(child, firstParent);
                child.setIndividualFitness(child.countIndividualFitness());
                nextPopulation.getIndividualsInPopulation()[counter] = child;

                counter++;
            }
            nextPopulation.setFitnessForAllSolutions();
            actPop = nextPopulation;
        }

        Arrays.sort(actPop.getIndividualsInPopulation(), Individual::compareTo);
        System.out.println("\nPunish. after: ");
        actPop.showIndividualsPunishments();
        System.out.println((actPop.getIndividualsInPopulation())[actPop.getIndividualsInPopulation().length - 1]);
        System.out.println((actPop.getIndividualsInPopulation())[actPop.getIndividualsInPopulation().length - 1].getIndividualFitness());
        return (actPop.getIndividualsInPopulation())[actPop.getIndividualsInPopulation().length - 1];
    }

    //METHODS
    boolean readAndSetPCBParamsFromFile(String filePath) {
        if (!isPathValid(filePath))
            return false;
        try {
            File problemInitFile = new File(filePath);
            BufferedReader initFileReader = new BufferedReader(new FileReader(problemInitFile));

            return readAndSetBoardSizeFromFile(initFileReader) &&
                    readAndSetConnectionsAndPointsFromFile(initFileReader);
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isPathValid(String path) {
        try {
            Paths.get(path);
            return true;
        } catch (InvalidPathException ex) {
            return false;
        }
    }

    private boolean readAndSetBoardSizeFromFile(BufferedReader fileReader) {
        try {
            String[] boardSizeParams = fileReader.readLine().split(";");
            setBoardWidth(Integer.parseInt(boardSizeParams[0]) - 1);
            setBoardHeight(Integer.parseInt(boardSizeParams[1]) - 1);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean readAndSetConnectionsAndPointsFromFile(BufferedReader fileReader) {
        String connectionLine;
        String[] actualConnectionParams;
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
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    //OVERRIDE FROM OBJECT

    @Override
    public String toString() {
        return "width: " + this.boardWidth + "\n" +
                "height: " + this.boardHeight + "\n" +
                "points: " + this.boardDefinedPoints + "\n" +
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

    private boolean validatePointsOnBoard() {
        for (Point p : this.boardDefinedPoints) {
            if (!checkIfPointIsOnBoard(p))
                return false;
        }
        return true;
    }

    private boolean validateConnectionsOnBoard() {
        for (int i = 0; i < this.boardDefinedConnections.size(); i++) {
            for (int j = i + 1; j < this.boardDefinedConnections.size(); j++) {
                if (this.boardDefinedConnections.get(i).equals(this.boardDefinedConnections.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkIfPointIsOnBoard(Point p) {
        return p.getX() <= this.boardWidth && p.getY() <= this.boardHeight && p.getX() >= 0 && p.getY() >= 0;
    }

    //===================================================================================================================='


    public static void main(String[] args) throws Exception {
        PCB pcb = new PCB();
        boolean res = pcb.readAndSetPCBParamsFromFile("C:\\Users\\User\\Desktop\\3rok\\6sem\\SI\\L\\lab1\\PCBGenerator\\src\\zad1.txt");
        System.out.println(pcb);

        System.out.println("========================================");

        Individual sol = pcb.findSolution();

        System.out.println(sol.toString());
        //Population pop = pcb.initPopulation();
        //Population pop1 = new Population(AlgorithmConfiguration.populationSize, pcb);

        //System.out.println(pop.toString());
        //System.out.println(pop.getFitnessForAllSolutions());

        /*Individual ind1 = GeneticOperators.selectionOperatorTournament(pop);
        Individual ind2 = GeneticOperators.selectionOperatorTournament(pop);

        System.out.println(ind1.toString());
        System.out.println(ind2.toString());

        if(ind1 != ind2){
            Individual result = GeneticOperators.crossing(ind1, ind2);
            System.out.println(result.toString());
        }*/
    }
}
