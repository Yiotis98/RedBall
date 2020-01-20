public class DroppingSpike extends Spike {

    DroppingSpike(String name) {
        super(name);
        width = 50;
        height = 50;
        this.x = Main.WIDTH - rand.nextInt(1200);
        this.y = 0;
    }

    @Override
    public void move() {
        this.x -= GamePanel.backgroundSpeed;
        this.y = this.y + 10;
    }


    @Override
    public boolean collide(int nX, int nY, int nW, int nH) {
        if (nX > this.x && nY < 0) {
            return false;
        } else {
            double A = area(this.x, this.y, this.x + this.width, this.y, this.x + this.width / 2, this.y + this.height);
            double A1 = area(nX, nY, this.x + this.width, this.y, this.x + this.width / 2, this.y + this.height);
            double A2 = area(nX, nY, this.x, this.y, this.x + this.width / 2, this.y + this.height);
            double A3 = area(nX, nY, this.x, this.y, this.x + this.width, this.y);
            double B1 = area(nX + nW, nY, this.x + this.width, this.y, this.x + this.width / 2, this.y + this.height);
            double B2 = area(nX + nW, nY, this.x, this.y, this.x + this.width / 2, this.y + this.height);
            double B3 = area(nX + nW, nY, this.x, this.y, this.x + this.width, this.y);
            return ((A == A1 + A2 + A3) || (A == B1 + B2 + B3));
        }
    }
}
