import java.util.ArrayList;

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
        Point actStartPoint = pathStartPoint;
        Segment initSegment = new Segment(actStartPoint, this);
        return true;
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
