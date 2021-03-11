public class Segment {
    private Point segmentStartPoint;
    private Point segmentEndPoint;

    private int segmentOrientationData = Direction.NO_DIRECTION; //ORIENTATION, DIRECTION

    public Segment(Point start){ //PRZY INICJALIZACJI NAJPIERW SEGMENT MA POCZATEK I KONIEC W SOBIE, POZNIEJ W MIARE ZWIEKSZANIA SPRAWDZAM CZY JEST OKI
        segmentStartPoint = start;
        segmentEndPoint = start;
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

    public boolean initSegmentEndPoint(Point point){ //INICJUJE KONIEC ENDPOINTU - ZAWSZE PIERWSZY
        if(point.isPointValid() && point != segmentStartPoint){
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
}
