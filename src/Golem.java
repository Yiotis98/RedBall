import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;
import java.util.Vector;

public class Golem {
    private int x, y, width, height, frames, delay;
    private String path="Golem_Running\\0_Golem_Running_0";
    private Vector<BufferedImage>movement;
    private BufferedImage tempImage;
    private Random rand = new Random();


    Golem(){
        frames=0;
        delay=0;
        movement=new Vector<BufferedImage>(1);
        width=rand.nextInt(150)+75;
        height=width*109/79;
        x=Main.WIDTH+200;
        y=Main.HEIGHT-height-50;
        for (int i=0;i<12;i++){
            String pathNew = path;
            if(i<10){
                pathNew=pathNew+"0"+i+".png";
            }else{
                pathNew=pathNew+i+".png";
            }
            File golemFile=new File(pathNew);
            try{
                tempImage=ImageIO.read(golemFile);
            }catch (Exception ex){
                System.out.println("Something went wrong reading golem images");
            }
            movement.add(tempImage);
        }
    }

    public void move(){
        delay++;
        this.x -=(GamePanel.backgroundSpeed+5);
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

    public BufferedImage getImage() {
        if (delay>2){
            delay=0;
            frames++;
            if (frames>11){
                frames=0;
            }
        }
        return this.movement.get(this.frames);
    }

    public boolean collide(int nX,int nY,int nW,int nH){
        if(nX>this.x&&nY<0){
            return false;
        }else{
            return nX < this.x + this.width && nX + nW > this.x && nY < this.y + this.height && nY + nH > this.y;
        }
    }

    public void reset(){
        width=rand.nextInt(150)+75;
        height=width*109/79;
        x=Main.WIDTH+200;
        y=Main.HEIGHT-height-50;
    }


}
