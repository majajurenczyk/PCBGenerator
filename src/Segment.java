import java.util.ArrayList;

public class Segment { //FOR THIS MOMENT SEGMENTS CAN BE OUT OF BOARD ETC
    private Point segmentStartPoint;
    private Point segmentEndPoint;

    private int segmentOrientationData = Direction.NO_DIRECTION; //ORIENTATION, DIRECTION

    public Segment(Point start){ //SEGMENT IS A POINT AT THE BEGINNING, INITIAL MOVE DEFINES ORIENTATION DATA
        segmentStartPoint = start.deepCopy();
        segmentEndPoint = start.deepCopy();
    }

    public Segment(Point start, Point end, int orientation){ //SEGMENT IS A POINT AT THE BEGINNING, INITIAL MOVE DEFINES ORIENTATION DATA
        segmentStartPoint = start.deepCopy();
        segmentEndPoint = end.deepCopy();
        segmentOrientationData = orientation;
    }

    public Segment deepCopySegment() {
        return new Segment(segmentStartPoint.deepCopy(), segmentEndPoint.deepCopy(), segmentOrientationData);
    }

    void initSegmentEndPoint(Point point){ //CHANGING ENDPOINT, DEFINES DIRECTIONS
        if(!point.equals(segmentStartPoint) && segmentOrientationData == Direction.NO_DIRECTION && (point.getX() == segmentEndPoint.getX() || point.getY() == segmentEndPoint.getY())){
            if(point.getX() == segmentStartPoint.getX()){
                if (point.getY() > segmentStartPoint.getY())
                    segmentOrientationData = Direction.VERTICAL_UP;
                else
                    segmentOrientationData = Direction.VERTICAL_DOWN;
            }
            else if(point.getY() == segmentStartPoint.getY()){
                if(point.getX() > segmentStartPoint.getX())
                    segmentOrientationData = Direction.HORIZONTAL_RIGHT;
                else
                    segmentOrientationData = Direction.HORIZONTAL_LEFT;
            }
            segmentEndPoint = point.deepCopy();
        }
    }

    boolean ifPointCanBeNewPartOfSegment(Point point){ //CHECKING IF NEW POINT CAN BE AN EXTENSION TO SEGMENT
        if(segmentOrientationData == Direction.NO_DIRECTION && (point.getX() == segmentEndPoint.getX() ^ point.getY() == segmentEndPoint.getY())){ //XOR
            return true;
        }
        else if(segmentOrientationData == Direction.VERTICAL_UP){
            return point.getX() == segmentEndPoint.getX() && point.getY() > segmentEndPoint.getY();
        }
        else if(segmentOrientationData == Direction.VERTICAL_DOWN){
            return point.getX() == segmentEndPoint.getX() && point.getY() < segmentEndPoint.getY();
        }
        else if(segmentOrientationData == Direction.HORIZONTAL_LEFT){
            return point.getX() < segmentEndPoint.getX() && point.getY() == segmentEndPoint.getY();
        }
        else if(segmentOrientationData == Direction.HORIZONTAL_RIGHT){
            return point.getX() > segmentEndPoint.getX() && point.getY() == segmentEndPoint.getY();
        }
        else{
            return false;
        }
    }

    int getSegmentLength(){ //LENGTH BETWEEN END POINT AND START POINT
        return (int)segmentStartPoint.countDistanceToAnotherPoint(segmentEndPoint);
    }

    int getOutOfBoardSegmentLength(int width, int height){ //LENGTH OF SEGMENT OUT OF BOARD
        int numberOfPointsOnBoard = 0;

        if(segmentOrientationData == Direction.VERTICAL_UP){
            for(int i = segmentStartPoint.getY(); i <= segmentEndPoint.getY(); i++){
                if(isPointOnBoard(new Point(segmentStartPoint.getX(), i), height, width)){
                    numberOfPointsOnBoard++;
                }
            }
        }
        else if(segmentOrientationData == Direction.VERTICAL_DOWN){
            for(int i = segmentStartPoint.getY(); i >= segmentEndPoint.getY(); i--){
                if(isPointOnBoard(new Point(segmentStartPoint.getX(), i), height, width)){
                    numberOfPointsOnBoard++;
                }
            }
        }
        else if(segmentOrientationData == Direction.HORIZONTAL_RIGHT){
            for(int i = segmentStartPoint.getX(); i <= segmentEndPoint.getX(); i++){
                if(isPointOnBoard(new Point(i, segmentStartPoint.getY()), height, width)){
                    numberOfPointsOnBoard++;
                }
            }
        }
        else if(segmentOrientationData == Direction.HORIZONTAL_LEFT){
            for(int i = segmentStartPoint.getX(); i >= segmentEndPoint.getX(); i--){
                if(isPointOnBoard(new Point(i, segmentStartPoint.getY()), height, width)){
                    numberOfPointsOnBoard++;
                }
            }
        }

        if(numberOfPointsOnBoard == 0 || numberOfPointsOnBoard == 1)
            return getSegmentLength();
        else
            return getSegmentLength() - (numberOfPointsOnBoard-1);
    }


    private boolean isPointOnBoard(Point p, int height, int width){
        return p.getX() <= width && p.getY() <= height && p.getX() >= 0 && p.getY() >= 0;
    }

    ArrayList<Point> getListOfPointsOnSegment(){
        ArrayList<Point> listOfPoints = new ArrayList<>();

        if(segmentOrientationData == Direction.VERTICAL_UP){
            for(int i = segmentStartPoint.getY(); i <= segmentEndPoint.getY(); i++){
                listOfPoints.add(new Point(segmentStartPoint.getX(), i));
            }
        }
        else if(segmentOrientationData == Direction.VERTICAL_DOWN){
            for(int i = segmentStartPoint.getY(); i >= segmentEndPoint.getY(); i--){
                listOfPoints.add(new Point(segmentStartPoint.getX(), i));
            }
        }
        else if(segmentOrientationData == Direction.HORIZONTAL_RIGHT){
            for(int i = segmentStartPoint.getX(); i <= segmentEndPoint.getX(); i++){
                listOfPoints.add(new Point(i, segmentStartPoint.getY()));
            }
        }
        else if(segmentOrientationData == Direction.HORIZONTAL_LEFT){
            for(int i = segmentStartPoint.getX(); i >= segmentEndPoint.getX(); i--){
                listOfPoints.add(new Point(i, segmentStartPoint.getY()));
            }
        }
        return listOfPoints;
    }

    Point getSecondLastEndPoint(){
        Point result = new Point(segmentStartPoint.getX(), segmentStartPoint.getY());
        if(segmentOrientationData == Direction.VERTICAL_UP){
            result.setX(segmentEndPoint.getX());
            result.setY(segmentEndPoint.getY() - 1);
        }
        else if(segmentOrientationData == Direction.VERTICAL_DOWN){
            result.setX(segmentEndPoint.getX());
            result.setY(segmentEndPoint.getY() + 1);
        }
        else if(segmentOrientationData == Direction.HORIZONTAL_RIGHT){
            result.setX(segmentEndPoint.getX() - 1);
            result.setY(segmentEndPoint.getY());
        }
        else if(segmentOrientationData == Direction.HORIZONTAL_LEFT){
            result.setX(segmentEndPoint.getX() + 1);
            result.setY(segmentEndPoint.getY());
        }
        return result;
    }

    public boolean ifIntersects(Segment s){
        for(int i = 0; i < this.getListOfPointsOnSegment().size()-1; i++){
            for(int j = i; j < s.getListOfPointsOnSegment().size(); j++){
                if(this.getListOfPointsOnSegment().get(i).equals(s.getListOfPointsOnSegment().get(j))){
                    return true;
                }
            }
        }
        return false;
    }

    public ArrayList<Point> intersectsPoint(Segment s){
        ArrayList<Point> intersectingPoints = new ArrayList<>();

        for(int i = 0; i <this.getListOfPointsOnSegment().size() - 1; i++){
            for(int j = i; j < s.getListOfPointsOnSegment().size(); j++){
                if(this.getListOfPointsOnSegment().get(i).equals(s.getListOfPointsOnSegment().get(j))){
                    intersectingPoints.add(this.getListOfPointsOnSegment().get(i).deepCopy());
                }
            }
        }
        return intersectingPoints;
    }

    //GETTERS AND SETTERS

    Point getSegmentEndPoint() {
        return segmentEndPoint;
    }

    Point getSegmentStartPoint(){
        return segmentStartPoint;
    }

    int getSegmentOrientationData(){
        return segmentOrientationData;
    }

    void setSegmentEndPoint(Point p){
        segmentEndPoint = p;
    }
    void setSegmentStartPoint(Point p){
        segmentStartPoint = p;
    }

    //FROM OBJECT

    @Override
    public String toString() {
        return "[" + segmentStartPoint.toString() + " , " + segmentEndPoint.toString() + "]";
    }
}
