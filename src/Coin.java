import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;


public class Coin {
    private int x, y, width, height;
    private BufferedImage coinImage;

    Coin(int X, int Y) {
        this.x = X;
        this.y = Y;
        width=50;
        height=50;
        File coinFile=new File("textures\\\\coin.png");
        try{
            coinImage=ImageIO.read(coinFile);
        }catch (Exception ex){
            System.out.println("Error with opening image at class coin");
        }
    }

    public BufferedImage getImage(){
        return this.coinImage;
    }

    public void moveCoin(){this.x -=GamePanel.backgroundSpeed;}



    public boolean coinCollide(int ball_x, int ball_y, int ball_width, int ball_height){
        return (ball_x+ball_width >= this.x && ball_x <= this.x+this.width && ball_y+ball_height >= this.y && ball_y <= this.y+this.height);
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
