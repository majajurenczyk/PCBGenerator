import java.util.ArrayList;
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

    public boolean randomInitPath(){ //HERE WILL BE GENERATED SEGMENTS IN PATH
        Random rand = new Random();
        Point actStartPoint = pathStartPoint;
        Segment actSegment = new Segment(actStartPoint, this);

        ArrayList<Point> availableMoves = getAllPointsToMoveFromPoint(actStartPoint, null);

        Point randomMove = availableMoves.get(rand.nextInt(availableMoves.size()));
        actSegment.initSegmentEndPoint(randomMove);
        segmentsInPath.add(actSegment);

        while(actSegment.getSegmentEndPoint() != pathEndPoint){
            availableMoves = getAllPointsToMoveFromPoint(actSegment.getSegmentEndPoint(), actSegment.getSecondLastEndPoint());
            randomMove = availableMoves.get(rand.nextInt(availableMoves.size()));
            if(actSegment.ifPointCanBeNewPartOfSegment(randomMove)){
                actSegment.enlargeSegment();
            }
            else{
                actSegment = new Segment(actSegment.getSegmentEndPoint(), this);
                actSegment.initSegmentEndPoint(randomMove);
                segmentsInPath.add(actSegment);
            }
        }
        return true;
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
            if(p != beforePoint){
                result.add(p);
            }
        }
        return  result;
    }


    public boolean isPathFullyCreated(){
        return segmentsInPath.get(segmentsInPath.size() - 1).getSegmentEndPoint() == pathEndPoint;
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
}
