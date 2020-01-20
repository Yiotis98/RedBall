import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

public class Platform {
    private int x, y, width, height;
    private BufferedImage platformImage;

    public Platform(int X, int Y) {
        this.x = X;
        this.y = Y;
        width=300;
        height=50;
        File platformFile=new File("textures\\\\platform.png");
        try{
            platformImage= ImageIO.read(platformFile);
        }catch (Exception ex){
            System.out.println("Error with opening image at class platform");
        }
    }
    public BufferedImage getImage(){
        return this.platformImage;
    }

    public void movePlatform(){this.x -= GamePanel.backgroundSpeed;}

    public boolean platformCollide(int ball_x, int ball_y, int ball_width, int ball_height){
        return (ball_y < this.y + 5 && ball_y + ball_height >= this.y - 10 && ball_x <= this.x+this.width && ball_x + ball_width >= this.x);
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

    public int getHeight(){
        return this.height;
    }
}
