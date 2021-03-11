public class Segment { //FOR THIS MOMENT SEGMENTS CAN BE OUT OF BOARD ETC
    private Point segmentStartPoint;
    private Point segmentEndPoint;

    private int segmentOrientationData = Direction.NO_DIRECTION; //ORIENTATION, DIRECTION

    private Path segmentsPath;

    public Segment(Point start, Path segmentsPath){ //SEGMENT IS A POINT AT THE BEGINNING, INITIAL MOVE DEFINES ORIENTATION DATA
        segmentStartPoint = start;
        segmentEndPoint = start;

        this.segmentsPath = segmentsPath;
    }

    public boolean enlargeSegment(){
        if(segmentOrientationData == Direction.VERTICAL_UP){
            segmentEndPoint = new Point(segmentEndPoint.getX(), segmentEndPoint.getY()+1);
            return true;
        }
        else if(segmentOrientationData == Direction.VERTICAL_DOWN){
            segmentEndPoint = new Point(segmentEndPoint.getX(), segmentEndPoint.getY()-1);
            return true;
        }
        else if(segmentOrientationData == Direction.HORIZONTAL_RIGHT){
            segmentEndPoint = new Point(segmentEndPoint.getX()+1, segmentEndPoint.getY());
            return true;
        }
        else if(segmentOrientationData == Direction.HORIZONTAL_LEFT){
            segmentEndPoint = new Point(segmentEndPoint.getX()-1, segmentEndPoint.getY());
            return true;
        }
        else {
            return false;
        }
    }

    public boolean initSegmentEndPoint(Point point){ //FIRST INIT MOVE, DEFINES DIRECTIONS
        if(point != segmentStartPoint &&(segmentOrientationData == Direction.NO_DIRECTION && (point.getX() == segmentEndPoint.getX() || point.getY() == segmentEndPoint.getY()))){
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
                return true;
        }
        else{
            return false;
        }
    }

    public boolean ifPointCanBeNewPartOfSegment(Point point){
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

    //GETTERS AND SETTERS

    public Path getSegmentsPath() {
        return segmentsPath;
    }

    public Point getSegmentEndPoint() {
        return segmentEndPoint;
    }

    public Point getSegmentStartPoint(){
        return segmentStartPoint;
    }

    public int getSegmentOrientationData(){
        return segmentOrientationData;
    }
}
