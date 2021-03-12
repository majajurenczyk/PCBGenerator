import java.util.ArrayList;

public class Segment { //FOR THIS MOMENT SEGMENTS CAN BE OUT OF BOARD ETC
    private Point segmentStartPoint;
    private Point segmentEndPoint;

    private int segmentOrientationData = Direction.NO_DIRECTION; //ORIENTATION, DIRECTION

    private Path segmentsPath;

    Segment(Point start, Path segmentsPath){ //SEGMENT IS A POINT AT THE BEGINNING, INITIAL MOVE DEFINES ORIENTATION DATA
        segmentStartPoint = new Point(start.getX(), start.getY());
        segmentEndPoint = new Point(start.getX(), start.getY());

        this.segmentsPath = segmentsPath;
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
            segmentEndPoint = new Point(point.getX(), point.getY());
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
        if(segmentOrientationData == Direction.NO_DIRECTION){
            return 0;
        }
        return (int)segmentStartPoint.countDistanceToAnotherPoint(segmentEndPoint);
    }

    int getOutOfBoardSegmentLength(){ //LENGTH OF SEGMENT OUT OF BOARD
        int width = segmentsPath.getPathsIndividual().getIndividualsPopulation().getProblem().getBoardWidth();
        int height = segmentsPath.getPathsIndividual().getIndividualsPopulation().getProblem().getBoardHeight();

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

    //GETTERS AND SETTERS

    public Path getSegmentsPath() {
        return segmentsPath;
    }

    Point getSegmentEndPoint() {
        return segmentEndPoint;
    }

    Point getSegmentStartPoint(){
        return segmentStartPoint;
    }

    public int getSegmentOrientationData(){
        return segmentOrientationData;
    }

    //FROM OBJECT


    @Override
    public String toString() {
        return "[" + segmentStartPoint.toString() + " , " + segmentEndPoint.toString() + "]";
    }
}
