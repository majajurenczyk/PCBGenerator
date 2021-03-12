import java.util.*;

public class Path {
    private ArrayList<Segment> segmentsInPath;

    private Point pathStartPoint;
    private Point pathEndPoint;

    private Individual pathsIndividual; //solution that path belongs to

    public Path(Connection connection, Individual individual) {
        segmentsInPath = new ArrayList<>();
        pathStartPoint = connection.getFirstPoint();
        pathEndPoint = connection.getSecondPoint();
        pathsIndividual = individual;
    }

    public int getNumberOfSegments(){
        return segmentsInPath.size();
    }

    public boolean isOutOfBoard(){
        return getPathLengthOutOfBoard() > 0;
    }

    public int getPathLength(){
        int sum = 0;
        for (Segment s: segmentsInPath) {
            sum += s.getSegmentLength();
        }
        return sum;
    }

    public ArrayList<Point> getAllPointsOnPath(){
        ArrayList<Point> result = new ArrayList<>();
        for(Segment segment : segmentsInPath){
            result.addAll(segment.getListOfPointsOnSegment());
        }
        return result;
    }

    public int getPathLengthOutOfBoard(){
        int sum = 0;
        for (Segment s: segmentsInPath) {
            sum += s.getOutOfBoardSegmentLength();
        }
        return sum;
    }

    public void firstMoveInPath() {
        Random rand = new Random();
        Segment actInitSegment = new Segment(pathStartPoint, this);
        ArrayList<Point> availableMoves = getAllPointsToMoveFromPoint(pathStartPoint, null);
        Point randomMove = availableMoves.get(rand.nextInt(availableMoves.size()));
        actInitSegment.initSegmentEndPoint(randomMove);
        segmentsInPath.add(actInitSegment);
    }

    public void randomInitPath() { //HERE WILL BE GENERATED SEGMENTS IN PATH
        firstMoveInPath();
        while (!segmentsInPath.get(segmentsInPath.size() - 1).getSegmentEndPoint().equals(pathEndPoint)) {
            Segment actSegment = segmentsInPath.get(segmentsInPath.size() - 1);
            ArrayList<Point> actAvailableMoves = getAllPointsToMoveFromPoint(actSegment.getSegmentEndPoint(),
                    actSegment.getSecondLastEndPoint());
            if (actAvailableMoves.size() == 0) {
                segmentsInPath.clear();
                randomInitPath();
            } else {
                int[] moves_probabilities = getProbabilitiesForMoves(actAvailableMoves);
                Point actRandomMove = actAvailableMoves.get(chooseMoveIndex(moves_probabilities));
                if (actSegment.ifPointCanBeNewPartOfSegment(actRandomMove)) {
                    Segment actNewSegment = new Segment(actSegment.getSegmentStartPoint(), this);
                    actNewSegment.initSegmentEndPoint(actRandomMove);

                    segmentsInPath.remove(actSegment);
                    segmentsInPath.add(actNewSegment);
                } else {
                    Segment actNewSegment = new Segment(actSegment.getSegmentEndPoint(), this);
                    actNewSegment.initSegmentEndPoint(actRandomMove);
                    segmentsInPath.add(actNewSegment);
                }
            }
        }
    }

    private ArrayList<Point> getAllPointsToMoveFromPoint(Point actStartPoint, Point beforePoint) {
        ArrayList<Point> result = new ArrayList<>();
        Point[] availableMoves = {new Point(actStartPoint.getX() + 1, actStartPoint.getY()), //FIRST MOVE FROM START POINT
                new Point(actStartPoint.getX() - 1, actStartPoint.getY()),
                new Point(actStartPoint.getX(), actStartPoint.getY() + 1),
                new Point(actStartPoint.getX(), actStartPoint.getY() - 1)};
        for (Point p : availableMoves) {
            if (p.equals(pathEndPoint)) { //IF THERE IS AN ENDPOINT IN AVAILABLE MOVES IT IS THE ONLY CHOICE
                result.clear();
                result.add(p);
                return result;
            }
            if (!p.equals(beforePoint) && !isPointInPath(p)) {
                result.add(p);
            }
        }
        return result;
    }

    private int[] getProbabilitiesForMoves(ArrayList<Point> moves) {
        ArrayList<Integer> distances = new ArrayList<>();
        ArrayList<Integer> distances_sorted = new ArrayList<>();
        int[] probabilities = new int[moves.size()];
        for (Point p : moves) {
            distances.add((int) (p.countDistanceToAnotherPoint(pathEndPoint) * 1000));
            distances_sorted.add((int) (p.countDistanceToAnotherPoint(pathEndPoint) * 1000));
        }
        distances_sorted.sort(Collections.reverseOrder());
        for (int i = 0; i < moves.size(); i++) {
            probabilities[i] = distances_sorted.lastIndexOf(distances.get(i)) + 1;
        }
        return probabilities;
    }

    private int chooseMoveIndex(int[] probabilities) {
        Random ran = new Random();
        int[] results = new int[probabilities.length];
        for (int i = 0; i < probabilities.length; i++) {
            results[i] = ran.nextInt(100 * probabilities[i]);
        }
        int max = results[0];
        int max_index = 0;
        for (int i = 0; i < results.length; i++) {
            if (results[i] > max) {
                max = results[i];
                max_index = i;
            }
        }
        return max_index;
    }


    private boolean isPointInPath(Point point) {
        for (Point p : getAllPointsOnPath()) {
            if (p.equals(point))
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
        for (Segment s : segmentsInPath) {
            result.append(s.toString()).append("\n");
        }
        return result.toString();
    }
}
