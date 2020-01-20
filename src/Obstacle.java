import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

public abstract class Obstacle {
    protected int x,y, width, height;
    private BufferedImage obstacle;
    private boolean firstTimePassed;
    Random rand = new Random();


    Obstacle(String name) {
        width = 25 + rand.nextInt(60);
        this.x = Main.WIDTH;
        height = width * 2;
        this.y = Main.HEIGHT - height - 50;
        File obstacleFile = new File(name);
        try {
            this.obstacle = ImageIO.read(obstacleFile);
        } catch (Exception ex) {
            System.out.println("Error with opening image at class obstacle");
        }
        firstTimePassed=false;
    }

    public BufferedImage getImage() {
        return this.obstacle;
    }

    public  void move(){
        this.x -=GamePanel.backgroundSpeed;
    }

    public boolean passedObstacle(int nX){
        if (nX>this.x+this.width && !firstTimePassed){
            firstTimePassed=true;
            return true;
        }else{
            return false;
        }
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getWidth(){
        return this.width;
    }

    public abstract boolean collide(int nX, int nY, int nW, int nH);
}

