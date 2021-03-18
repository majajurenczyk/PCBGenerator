public class Point {
    private int x;
    private  int y;

    Point(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Point deepCopy(){
        return new Point(this.x, this.y);
    }

    double countDistanceToAnotherPoint(Point anotherPoint){
        return Math.sqrt((anotherPoint.y - this.y)*(anotherPoint.y - this.y) + (anotherPoint.x - this.x)*(anotherPoint.x - this.x));
    }


    int getY() {
        return y;
    }

    void setY(int y) {
        this.y = y;
    }

    int getX() {
        return x;
    }

    void setX(int x) {
        this.x = x;
    }


    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Point)
            return this.x==((Point)obj).x && this.y==((Point)obj).getY();
        return false;
    }

    @Override
    public String toString() {
        return "("+this.x+" , "+this.y+")";
    }
}
