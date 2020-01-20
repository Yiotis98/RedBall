import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Inet4Address;
import java.nio.Buffer;
import java.util.Scanner;
import java.util.Vector;

public class Menu implements MouseListener {
    private Vector<Integer>positionY;
    private Vector<String>scorePosition;
    private Vector<String>scoreName;
    private Vector<String>scoreScore;
    private int menuX, firstMenuY;
    public boolean firstDraw,showScore,showHelp,backButtonEnable;
    private BufferedImage help,startBackground,back,contact;
    private static final int  menuWidth=250, menuHeight=72,contactX=Main.WIDTH-145,contactY=Main.HEIGHT-100,contactWidth=125,contactHeight=50;

    private String userName;

    Menu(String userName){
        try {
            File helpFile = new File("textures\\\\help.png");
            File startBackgroundFile=new File ("textures\\\\startbackground.png");
            File backFile=new File("textures\\\\back.png");
            File contactFile=new File("textures\\\\contact.png");
            startBackground=ImageIO.read(startBackgroundFile);
            help= ImageIO.read(helpFile);
            back=ImageIO.read(backFile);
            contact=ImageIO.read(contactFile);
        }catch (Exception e){
            System.out.println("Error opening image in class menu");
        }
        this.userName=userName;
        positionY=new Vector<Integer>(1);
        scorePosition=new Vector<String>(1);
        scoreName=new Vector<String>(1);
        scoreScore=new Vector<String>(1);
        readFile();
        reset();
    }

    public void reset(){
        backButtonEnable=false;
        firstDraw=true;
        showHelp=false;
        showScore=false;
        menuX=Main.WIDTH/2-125;
        firstMenuY = (Main.HEIGHT / 2)-(menuHeight)-165;
    }

    private void updateFile(){
        try {
            File file = new File("score.txt");
            FileOutputStream is = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(is);
            Writer w = new BufferedWriter(osw);
            for (int i=0;i<4;i++){
                w.write(scorePosition.get(i)+"\t"+scoreName.get(i)+"\t"+scoreScore.get(i)+"\n");
            }
            w.close();
        }catch (Exception ex){
            System.out.println("Error with opening score.txt for writing");
        }
    }

    public void updateLeaderboard(Integer score){
        int index=-1;
        Integer temp;
        String tempstr;
        for (int i=1;i<scorePosition.size();i++){
            temp=Integer.parseInt(scoreScore.get(i));
            if (score>=temp){
                index=i;
                break;
           }
        }
        if (index!=-1){
            tempstr=score.toString();
            scoreScore.add(index,tempstr);
            scoreName.add(index,userName);
            scoreScore.remove(4);
            scoreName.remove(4);
        }
        updateFile();
    }

    private void readFile(){
        try {
            File file = new File("score.txt");
            Scanner scanFile = new Scanner(file);
            while (scanFile.hasNextLine()){
                scorePosition.add(scanFile.next());
                scoreName.add(scanFile.next());
                scoreScore.add(scanFile.next());
            }
        }catch (Exception ex){
        }
    }

    public void draw(Graphics g) {
        g.drawImage(startBackground, 0, 0, Main.WIDTH, Main.HEIGHT-35, null);
        g.setColor(Color.red);
        int menuY = firstMenuY;
        for (int i = 0; i < 4; i++) {
            g.drawRect(menuX, menuY, menuWidth, menuHeight);
            positionY.add(menuY);
            menuY += menuHeight;
        }
        firstDraw = false;
        g.setFont(new Font("Arial", Font.BOLD, 45));
        g.drawString("Play", menuX + 5, positionY.get(0) + 50);
        g.drawString("High Score", menuX + 5, positionY.get(1) + 50);
        g.drawString("Help", menuX + 5, positionY.get(2) + 50);
        g.drawString("Exit", menuX + 5, positionY.get(3) + 50);
        g.drawImage(contact,contactX,contactY,contactWidth,contactHeight,null);
    }

    public void drawScore(Graphics g){
        firstDraw=false;
        showScore=false;
        g.drawImage(startBackground, 0, 0, Main.WIDTH, Main.HEIGHT-35, null);
        g.drawImage(back,0,0,50,50,null);
        int posX=menuX-75;
        int posY=200;
        g.setFont(new Font("Arial", Font.BOLD, 25));
        g.setColor(Color.red);
        for (int i=0;i<scorePosition.size();i++){
            g.drawString(scorePosition.get(i),posX,posY);
            posX+=220;
            g.drawString(scoreName.get(i),posX,posY);
            posX+=220;
            g.drawString(scoreScore.get(i),posX,posY);
            posY+=45;
            posX-=440;
        }
    }

    public void drawHelp(Graphics g){
        firstDraw=false;
        showHelp=false;
        g.drawImage(startBackground, 0, 0, Main.WIDTH, Main.HEIGHT-35, null);
        g.drawImage(help,Main.WIDTH/2-250,100,500,380,null);
        g.drawImage(back,0,0,50,50,null);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        int mouseX = mouseEvent.getX();
        int mouseY = mouseEvent.getY();
        if (!firstDraw&&!backButtonEnable) {
            int menuIndex=-1;
            for (int i = 0; i < positionY.size(); i++) {
                if ((mouseX > menuX) && (mouseX < (menuX + menuWidth)) && (mouseY > positionY.get(i)) && (mouseY < (positionY.get(i) + menuHeight))) {
                    GamePanel.playMusic("sounds\\\\click.wav");
                    menuIndex=i;
                    break;
                }
            }
            switch (menuIndex){
                case 0:
                    GamePanel.mainMenu=0;
                    backButtonEnable=false;
                    firstDraw=true;
                    break;
                case 1:
                    firstDraw=true;
                    showScore=true;
                    backButtonEnable=true;
                    break;
                case 2:
                    showHelp=true;
                    firstDraw=true;
                    backButtonEnable=true;
                    break;
                case 3:
                    System.out.println("Exit");
                    System.exit(0);
                    break;
                default:
            }
            if ((mouseX>contactX)&&(mouseX<(contactX+contactWidth))&&(mouseY>contactY)&&(mouseY<(contactY+contactHeight))){
                GamePanel.playMusic("sounds\\\\click.wav");
                String msg=JOptionPane.showInputDialog("Insert message:");
                try {
                    if (!SendEmail.createAndSendMail(this.userName,msg)) {
                        System.out.println("Error sending email!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }else if(backButtonEnable){
            if(mouseX>0&&mouseX<50&&mouseY>0&&mouseY<50){
                GamePanel.playMusic("sounds\\\\click.wav");
                firstDraw=true;
                backButtonEnable=false;
            }
        }
    }



    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }
}
