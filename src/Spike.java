public class Spike extends Obstacle{

    Spike(String name) {
        super(name);
    }


    protected double area(int x1, int y1, int x2, int y2,int x3, int y3) {
        return Math.abs((x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2)) / 2.0);
    }

    @Override
    public boolean collide(int nX,int nY,int nW,int nH) {
        if (nX > this.x && nY < 0) {
            return false;
        } else {
            double A  = area(this.x, this.y + this.height, this.x + this.width / 2, this.y, this.x + this.width, this.y + this.height);
            double A1 = area(nX + nW, nY + nH, this.x + this.width / 2, this.y, this.x + this.width, this.y + this.height);
            double A2 = area(nX + nW, nY + nH, this.x, this.y + this.height, this.x + this.width, this.y + this.height);
            double A3 = area(nX + nW, nY + nH, this.x, this.y + this.height, this.x + this.width / 2, this.y);
            double B1 =area(nX, nY + nH, this.x + this.width / 2, this.y, this.x + this.width, this.y + this.height);
            double B2 =area(nX , nY + nH, this.x, this.y + this.height, this.x + this.width, this.y + this.height);
            double B3 = area(nX, nY + nH, this.x, this.y + this.height, this.x + this.width / 2, this.y);
            return ((A == A1 + A2 + A3)||(A==B1+B2+B3));
        }
    }
}
