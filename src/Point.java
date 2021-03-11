public class Point {
    private int x;
    private  int y;

    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
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
