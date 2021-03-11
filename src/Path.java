import java.util.ArrayList;

public class Path {
    private ArrayList<Segment> segmentsInPath;

    private Point pathStartPoint;
    private Point pathEndPoint;

    public Path(Connection connection){
        segmentsInPath = new ArrayList<>();

        pathStartPoint = connection.getFirstPoint();
        pathEndPoint = connection.getSecondPoint();
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
}
