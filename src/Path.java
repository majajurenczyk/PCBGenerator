import java.util.*;

public class Path {
    private ArrayList<Segment> segmentsInPath;

    private Point pathStartPoint;
    private Point pathEndPoint;

    public Path(Connection connection) {
        segmentsInPath = new ArrayList<>();
        pathStartPoint = connection.getFirstPoint().deepCopy();
        pathEndPoint = connection.getSecondPoint().deepCopy();
    }

    public Path(Point start, Point end, ArrayList<Segment> segments) {
        segmentsInPath = segments;
        pathStartPoint = start.deepCopy();
        pathEndPoint = end.deepCopy();
    }

    public Path deepCopyPath(){
        ArrayList<Segment> segmentsInPath = new ArrayList<>();
        Path result = new Path(pathStartPoint.deepCopy(), this.pathEndPoint.deepCopy(), segmentsInPath);
        for (Segment s: this.segmentsInPath){
            result.segmentsInPath.add(s.deepCopySegment());
        }
        return result;
    }

    public int getNumberOfSegments(){
        return segmentsInPath.size();
    }

    boolean isOutOfBoard(int width, int height){
        return getPathLengthOutOfBoard(width, height) > 0;
    }

    int getPathLength(){
        int sum = 0;
        for (Segment s: segmentsInPath) {
            sum += s.getSegmentLength();
        }
        return sum;
    }

    int getPathLengthOutOfBoard(int width, int height){
        int sum = 0;
        for (Segment s: segmentsInPath) {
            sum += s.getOutOfBoardSegmentLength(width, height);
        }
        return sum;
    }

    private void firstMoveInPath() {
        Random rand = new Random();
        Segment actInitSegment = new Segment(pathStartPoint);
        ArrayList<Point> availableMoves = getAllPointsToMoveFromPoint(pathStartPoint, null);
        Point randomMove = availableMoves.get(rand.nextInt(availableMoves.size())); //FIRST MOVE IS FULL RANDOM
        actInitSegment.initSegmentEndPoint(randomMove);
        segmentsInPath.add(actInitSegment);
    }

    void randomInitPath() { //HERE WILL BE GENERATED SEGMENTS IN PATH
        firstMoveInPath();
        while (!segmentsInPath.get(segmentsInPath.size() - 1).getSegmentEndPoint().equals(pathEndPoint)) { //TILL SEGMENTS REACH ENDPOINT
            Segment actSegment = segmentsInPath.get(segmentsInPath.size() - 1);
            ArrayList<Point> actAvailableMoves = getAllPointsToMoveFromPoint(actSegment.getSegmentEndPoint(),
                    actSegment.getSecondLastEndPoint()); //GETS ALL AVAILABLE MOVES (1 STEP), CANNOT GO BACK, CANNOT INTERSECT WITH ITSELF
            if (actAvailableMoves.size() == 0) { //INIT NEW PATH IF THERE IS NO AVAILABLE MOVES
                segmentsInPath.clear();
                randomInitPath();
            } else {
                int[] moves_probabilities = getProbabilitiesForMoves(actAvailableMoves); //PROBABILITIES TO MAKE SPECIFIC MOVE, THE BIGGER THE CLOSER TO ENDPOINT
                Point actRandomMove = actAvailableMoves.get(chooseMoveIndex(moves_probabilities)); //GET MOVE WITH THE HIGHEST PROBABILITY
                if (actSegment.ifPointCanBeNewPartOfSegment(actRandomMove)) { //IF NEW MOVE CAN BY PART OF SEGMENT
                    Segment actNewSegment = new Segment(actSegment.getSegmentStartPoint());
                    actNewSegment.initSegmentEndPoint(actRandomMove); //OLD START POINT AND NEW ENDPOINT OF LAST SEGMENT

                    segmentsInPath.remove(actSegment);
                    segmentsInPath.add(actNewSegment);
                }
                else {
                    Segment actNewSegment = new Segment(actSegment.getSegmentEndPoint()); //NEW SEGMENT
                    actNewSegment.initSegmentEndPoint(actRandomMove);
                    segmentsInPath.add(actNewSegment);
                }
            }
        }
    }

    private ArrayList<Point> getAllPointsToMoveFromPoint(Point actStartPoint, Point beforePoint) {
        ArrayList<Point> result = new ArrayList<>();
        Point[] availableMoves = {new Point(actStartPoint.getX() + 1, actStartPoint.getY()), //ALL POINTS IN SURROUNDING
                new Point(actStartPoint.getX() - 1, actStartPoint.getY()),
                new Point(actStartPoint.getX(), actStartPoint.getY() + 1),
                new Point(actStartPoint.getX(), actStartPoint.getY() - 1)};
        for (Point p : availableMoves) {
            if (p.equals(pathEndPoint)) { //IF THERE IS AN ENDPOINT IN AVAILABLE MOVES IT IS THE ONLY CHOICE
                result.clear();
                result.add(p);
                return result;
            }
            if (/*!p.equals(beforePoint) && */!isPointInPath(p)) { //IF MOVE IS COMING BACK OR INTERSECT WITH ITSELF - NOT VALID MOVE
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
            distances.add((int) (p.countDistanceToAnotherPoint(pathEndPoint) * 500)); //THE PROBABILITY TO BE CHOSEN IS THE HIGHER THE CLOSER IS ENDPOINT
            distances_sorted.add((int) (p.countDistanceToAnotherPoint(pathEndPoint) * 500));
        }
        distances_sorted.sort(Collections.reverseOrder());
        for (int i = 0; i < moves.size(); i++) {
            probabilities[i] = distances_sorted.lastIndexOf(distances.get(i)) + 1;
        }
        return probabilities;
    }

    private int chooseMoveIndex(int[] probabilities) { //GETTING INDEX OF MOVE WITH HIGHEST PROBABILITY TO BE CHOSEN
        Random ran = new Random();
        int[] results = new int[probabilities.length];
        for (int i = 0; i < probabilities.length; i++) {
            results[i] = ran.nextInt(50 * probabilities[i]);
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

    ArrayList<Point> getAllPointsOnPath(){
        ArrayList<Point> result = new ArrayList<>(); //ARRAY LIST OF POINTS ON PATH
        for(Segment segment : segmentsInPath){
            for(int i = 0; i < segment.getListOfPointsOnSegment().size()-1; i++)
                result.add(segment.getListOfPointsOnSegment().get(i));
        }
        result.add(this.pathEndPoint.deepCopy());
        return result;
    }

    public boolean ifIntersects(Path p){
        for (int i = 0; i < this.getSegmentsInPath().size() - 1; i++) {
            for (int j = i; j < p.getSegmentsInPath().size(); j++) {
                if(this.getSegmentsInPath().get(i).equals(p.getSegmentsInPath().get(j))){
                    return true;
                }
            }
        }
        return false;
    }

    public ArrayList<Point> intersectingPoints(Path p){
        ArrayList<Point> intersections = new ArrayList<>();
        for (int i = 0; i < this.getAllPointsOnPath().size(); i++) {
            for (int j = 0; j < p.getSegmentsInPath().size(); j++) {
                if(this.getAllPointsOnPath().get(i).equals(p.getAllPointsOnPath().get(j))){
                    intersections.add(this.getAllPointsOnPath().get(0).deepCopy());
                }
            }
        }
        return intersections;
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


    //OVERRIDE FROM OBJECT

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("\nPATH = \n");
        for (Segment s : segmentsInPath) {
            result.append(s.toString()).append("\n");
        }
        return result.toString();
    }
}
