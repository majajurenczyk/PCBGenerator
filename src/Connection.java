public class Connection {
    private Point firstPoint;
    private Point secondPoint;

    Connection(Point firstPoint, Point secondPoint){
        this.firstPoint = firstPoint;
        this.secondPoint = secondPoint;
    }

    Point getSecondPoint() {
        return secondPoint;
    }

    public void setSecondPoint(Point secondPoint) {
        this.secondPoint = secondPoint;
    }

    Point getFirstPoint() {
        return firstPoint;
    }

    public void setFirstPoint(Point firstPoint) {
        this.firstPoint = firstPoint;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Connection)
            return (this.firstPoint.equals(((Connection)obj).firstPoint) && this.secondPoint.equals(((Connection)obj).secondPoint)) ||
                    (this.firstPoint.equals(((Connection)obj).secondPoint) && this.secondPoint.equals(((Connection)obj).firstPoint));
        return false;
    }

    @Override
    public String toString() {
        return "( "+firstPoint.toString()+ " , " + secondPoint.toString()+ " )";
    }
}
