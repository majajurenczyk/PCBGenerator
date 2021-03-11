import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class Path {
    private ArrayList<Segment> segmentsInPath;

    private Point pathStartPoint;
    private Point pathEndPoint;

    private Individual pathsIndividual; //solution that path belongs to

    public Path(Connection connection, Individual individual){
        segmentsInPath = new ArrayList<>();
        pathStartPoint = connection.getFirstPoint();
        pathEndPoint = connection.getSecondPoint();
        pathsIndividual = individual;
    }

    public void randomInitPath(){ //HERE WILL BE GENERATED SEGMENTS IN PATH
        Random rand = new Random();

        Point actStartPoint = pathStartPoint;
        Segment actSegment = new Segment(actStartPoint, this);

        ArrayList<Point> availableMoves = getAllPointsToMoveFromPoint(actStartPoint, null);

        Point randomMove = availableMoves.get(rand.nextInt(availableMoves.size()));
        actSegment.initSegmentEndPoint(randomMove);
        segmentsInPath.add(actSegment);

        while(actSegment.getSegmentEndPoint() != pathEndPoint){
            availableMoves = getAllPointsToMoveFromPoint(actSegment.getSegmentEndPoint(), actSegment.getSecondLastEndPoint());
            if(availableMoves.size() == 0){
                segmentsInPath.clear();
                randomInitPath(); //IF THERE IS NO MOVE NEW PATH IS GENERATED
            }
            else {
                int [] moves_probabilities = getProbabilitiesForMoves(availableMoves);
                randomMove = availableMoves.get(chooseMoveIndex(moves_probabilities));
                if (actSegment.ifPointCanBeNewPartOfSegment(randomMove)) {
                    actSegment.enlargeSegment();
                } else {
                    actSegment = new Segment(actSegment.getSegmentEndPoint(), this);
                    actSegment.initSegmentEndPoint(randomMove);
                    segmentsInPath.add(actSegment);
                }
            }
        }
    }

    private ArrayList<Point> getAllPointsToMoveFromPoint(Point actStartPoint, Point beforePoint){
        ArrayList<Point> result = new ArrayList<>();
        Point [] availableMoves = {new Point(actStartPoint.getX()+1, actStartPoint.getY()), //FIRST MOVE FROM START POINT
                new Point(actStartPoint.getX()-1, actStartPoint.getY()),
                new Point(actStartPoint.getX(), actStartPoint.getY()+1),
                new Point(actStartPoint.getX(), actStartPoint.getY()-1)};
        for (Point p: availableMoves) {
            if(p == pathEndPoint){ //IF THERE IS AN ENDPOINT IN AVAILABLE MOVES IT IS THE ONLY CHOICE
                result.clear();
                result.add(p);
                return result;
            }
            if(p != beforePoint && !isPointInPath(p)){
                result.add(p);
            }
        }
        return  result;
    }

    private int [] getProbabilitiesForMoves(ArrayList<Point> moves){
        ArrayList<Double> distances = new ArrayList<>();
        ArrayList<Double> distances_sorted = new ArrayList<>();
        int [] probabilities = new int[moves.size()];
        for (Point p: moves) {
            distances.add(p.countDistanceToAnotherPoint(pathEndPoint)*100);
            distances_sorted.add(p.countDistanceToAnotherPoint(pathEndPoint)*100);
        }
        distances_sorted.sort(Double::compareTo);
        for (int i = 0; i < moves.size(); i++){
            probabilities[i] = distances_sorted.lastIndexOf(distances.get(i));
        }
        return probabilities;
    }

    private int chooseMoveIndex(int [] probabilities){
        Random ran = new Random();
        int [] results = new int [probabilities.length];
        for (int i = 0; i < probabilities.length; i++){
            results [i] = ran.nextInt(100*probabilities[i]);
        }
        int max = 0;
        for (int result : results) {
            if (result > max) {
                max = result;
            }
        }
        return max;
    }


    private boolean isPointInPath(Point p){
        for (Segment s: segmentsInPath){
            if (s.isPointOnSegment(p))
                return true;
        }
        return false;
    }


    //GETTERS AND SETTERS

    public Point getPathEndPoint() {
        return pathEndPoint;
    }

    public void setPathEndPoint(Point pathEndPoint) {
        this.pathEndPoint = pathEndPoint;
    }

    public Point getPathStartPoint() {
        return pathStartPoint;
    }

    public void setPathStartPoint(Point pathStartPoint) {
        this.pathStartPoint = pathStartPoint;
    }

    public ArrayList<Segment> getSegmentsInPath() {
        return segmentsInPath;
    }

    public void setSegmentsInPath(ArrayList<Segment> segmentsInPath) {
        this.segmentsInPath = segmentsInPath;
    }

    public Individual getPathsIndividual() {
        return pathsIndividual;
    }

    //FROM OBJECT

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("PATH = \n");
        for (Segment s: segmentsInPath) {
            result.append(s.toString()).append("\n");
        }
        return result.toString();
    }
}
