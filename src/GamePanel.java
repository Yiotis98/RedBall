import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;
import java.util.Vector;


public class GamePanel extends JPanel implements KeyListener, MouseListener {
    private int key, backgroundX1, backgroundX2, currentBackground, ticksUntilNextSpike,ticksUntilNextCoin,ticksUntilNextPlatform,bulletCounter,longPressed,scoreCounter,drawNextGolem;
    static final int backgroundSpeed = 2;
    private static final int pausePositionX = Main.WIDTH - 100;
    private static final int pausePositionY = 25;
    private static final int playPositionX = Main.WIDTH / 2 - 100;
    private static final int playPositionY = Main.HEIGHT / 2 - 100;
    private static final int restartPositionX = playPositionX + 100;
    private static final int restartPositionY = playPositionY;
    private static final int menuPositionX = restartPositionX + 100;
    private static final int menuPositionY = restartPositionY;
    private Ball ball;
    boolean ready;
    private boolean rightPressed, leftPressed, leftLongPressed, jumpPressed, rightLongPressed, doubleJump,pausePressed,firstTickPause,firstTickOver;
    private BufferedImage background, pause, play, restart,mMenu;
    private double delayDoubleJump = 0.0D;
    private Vector<Bullet> bullets;
    private Vector<Obstacle> spikes;
    private Vector<Coin> coins;
    private Vector<Integer> toDelete;
    private Vector<Bullet> toDeleteBullets;
    private Random rand = new Random();
    private Golem golem;
    private Vector<Platform> platforms;
    static int mainMenu=1;
    private Menu menu;

    GamePanel(String userName) {
        try {
            File backgroundFile = new File("textures\\\\background.png");
            background = ImageIO.read(backgroundFile);
            File playFile = new File("textures\\\\play.png");
            play = ImageIO.read(playFile);
            File pauseFile = new File("textures\\\\pause.png");
            pause = ImageIO.read(pauseFile);
            File restartFile = new File("textures\\\\restart.png");
            restart = ImageIO.read(restartFile);
            File menuFile=new File("textures\\\\menu.png");
            mMenu=ImageIO.read(menuFile);

        } catch (Exception e) {
            System.out.println("Cant open Image");
            return;
        }
        this.restart();
        this.addKeyListener(this);
        this.addMouseListener(this);
        menu=new Menu(userName);
        playBackgroundMusic();
    }

    public void addNotify() {
        super.addNotify();
        this.requestFocus();
        this.ready = true;
    }

    private void restart() {
        ball = new Ball(50, 600);
        ball.minimumHeight=Main.HEIGHT - (2 * ball.ballHeight);
        bullets = new Vector<Bullet>(1);
        spikes = new Vector<Obstacle>(1);
        coins = new Vector<Coin>(1);
        platforms=new Vector<Platform>(1);
        golem = new Golem();
        drawNextGolem = rand.nextInt(500) + 100;
        backgroundX1 = 0;
        backgroundX2 = 0;
        currentBackground = 1;
        rightPressed = false;
        leftPressed = false;
        jumpPressed = false;
        rightLongPressed = false;
        longPressed = 0;
        doubleJump = false;
        delayDoubleJump = 0.0D;
        pausePressed = false;
        firstTickPause = false;
        firstTickOver = false;
        ticksUntilNextSpike = 1;
        ticksUntilNextCoin = 15;
        ticksUntilNextPlatform=40;
        scoreCounter = 0;
        bulletCounter = 0;
    }

    public void paintComponent(Graphics g) {
        if (ball.isAlive() && !firstTickPause && mainMenu==0) {
            super.paintComponent(g);
            backgroundHandler(g);
            obstacleHandler(g);
            coinHandler(g);
            scoreHandler(g);
            movementHandler();
            platformHandler(g);
            golemHandler(g);
            bulletHandler(g);
            ball.inGame(g);
            if (!pausePressed) {
                g.drawImage(pause, pausePositionX, pausePositionY, 50, 50, null);
            } else{
                if (!firstTickPause) {
                    firstTickPause = true;
                    g.drawImage(play, playPositionX, playPositionY, 50, 50, null);
                    g.drawImage(restart, restartPositionX, restartPositionY, 50, 50, null);
                    g.drawImage(mMenu, menuPositionX, menuPositionY, 50, 50, null);
                }
            }
        } else if (!ball.isAlive() && mainMenu == 0) {
            if (!firstTickOver) {
                firstTickOver = true;
                super.paintComponent(g);
                backgroundHandler(g);
                obstacleHandler(g);
                coinHandler(g);
                scoreHandler(g);
                movementHandler();
                platformHandler(g);
                golemHandler(g);
                bulletHandler(g);
                ball.inGame(g);
                g.setColor(Color.red);
                g.setFont(new Font("Arial", Font.BOLD, 100));
                g.drawString("Game Over", Main.WIDTH / 2 - 250, Main.HEIGHT / 2 - 220);
                g.drawImage(restart, restartPositionX, restartPositionY, 50, 50, null);
                g.drawImage(mMenu, menuPositionX, menuPositionY, 50, 50, null);
                playMusic("sounds\\\\gameoversound.wav");
                playMusic("sounds\\\\gameovervoice.wav");
            }
        } else if (mainMenu == 1) {
            if (menu.firstDraw && !menu.showScore && !menu.showHelp) {
                super.paintComponent(g);
                menu.draw(g);
            } else if (menu.firstDraw && menu.showScore && !menu.showHelp) {
                super.paintComponent(g);
                menu.drawScore(g);
            } else if (menu.firstDraw && !menu.showScore && menu.showHelp) {
                super.paintComponent(g);
                menu.drawHelp(g);
            }
        }
    }

    public static void playMusic(String path){
        try{
            File musicPath=new File(path);
            AudioInputStream audioInput= AudioSystem.getAudioInputStream(musicPath);
            Clip clip=AudioSystem.getClip();
            clip.open(audioInput);
            clip.start();
        }catch(Exception e){
            System.out.println("Error opening music file");
        }
    }

    private static void playBackgroundMusic(){
        try{
            File musicPath=new File("sounds\\\\background.wav");
            AudioInputStream audioInput= AudioSystem.getAudioInputStream(musicPath);
            Clip clip=AudioSystem.getClip();
            clip.open(audioInput);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        }catch(Exception e){
            System.out.println("Error opening music file");
        }
    }

    private void golemHandler(Graphics g) {
        drawNextGolem--;
        if (drawNextGolem <= 0) {
            if (drawNextGolem == 0) {
                GamePanel.playMusic("sounds\\\\golem.wav");
                GamePanel.playMusic("sounds\\\\jeff.wav");
            }
            golem.move();
            g.drawImage(golem.getImage(), golem.getX(), golem.getY(), golem.getWidth(), golem.getHeight(), null);
            if (golem.collide(ball.getX(), ball.getY(), ball.getWidth(), ball.getHeight())) {
                ball.kill();
            } else if (golem.getX() < (-golem.getWidth())) {
                drawNextGolem = rand.nextInt(500) + 100;
                golem.reset();
            }
        }
    }

    private void bulletHandler(Graphics g) {
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.setColor(Color.red);
        g.drawString("BULLETS LEFT: " + this.bulletCounter, 20, 80);
        toDeleteBullets = new Vector<Bullet>(1);
        for (Bullet temp : bullets) {
            temp.move();
            if (temp.checkOutOfBounds()) {
                toDeleteBullets.add(temp);
            }
            if (golem.collide(temp.x,temp.y,temp.width,temp.height)) {
                GamePanel.playMusic("sounds\\\\dying.wav");
                toDeleteBullets.add(temp);
                golem.reset();
                drawNextGolem = rand.nextInt(500) + 100;
                scoreCounter+=20;
            }
            g.drawImage(temp.getImage(), temp.x, temp.y, 75, 75, null);
        }
        if (toDeleteBullets.size() > 0)
            for (Bullet temp : toDeleteBullets) {
                bullets.remove(temp);
            }
    }

    private void movementHandler() {
        if (this.delayDoubleJump > 0.0D) {
            --this.delayDoubleJump;
        }
        if (rightLongPressed || rightPressed)
            ball.moveRight(rightLongPressed);
        if (leftPressed || leftLongPressed)
            ball.moveLeft(leftLongPressed);
        if (jumpPressed || doubleJump) {
            ball.jump(doubleJump);
            doubleJump = false;
            jumpPressed = false;
        }
    }

    private void obstacleHandler(Graphics g) {
        ticksUntilNextSpike--;
        if (ticksUntilNextSpike == 0) {
            ticksUntilNextSpike = 50 + rand.nextInt(500);
            if (ticksUntilNextSpike > 200) {
                spikes.add(new Spike("textures\\\\spike.png"));
            } else {
                GamePanel.playMusic("sounds\\\\droppingspike.wav");
                spikes.add(new DroppingSpike("textures\\\\spike_upsidedown.png"));
            }
        }
        toDelete = new Vector<Integer>(1);
        for (Obstacle temp : spikes) {
            temp.move();
            if (temp.collide(ball.getX(), ball.getY(), ball.getWidth(), ball.getHeight())) {
                ball.kill();
            }
            if (temp.passedObstacle(ball.getX())){
                scoreCounter++;
            }
            if (temp.getX() > -temp.width && temp.getY() < Main.WIDTH + 100) {
                g.drawImage(temp.getImage(), temp.getX(), temp.getY(), temp.width, temp.height, null);
            } else {
                toDelete.add(spikes.indexOf(temp));
            }
        }
        if (toDelete.size() > 0)
            for (Integer temp : toDelete) {
                spikes.remove(spikes.get(temp));
            }
    }

    private void platformHandler(Graphics g){
        ticksUntilNextPlatform--;
        if (ticksUntilNextPlatform == 0) {
            ticksUntilNextPlatform = 200 + rand.nextInt(500);
            platforms.add(new Platform(Main.WIDTH,Main.HEIGHT-200-rand.nextInt(200)));
        }
        toDelete=new Vector<Integer>(1);
        boolean found=false;
        for (Platform temp:platforms) {
            temp.movePlatform();
            g.drawImage(temp.getImage(), temp.getX(), temp.getY(), temp.getWidth(), temp.getHeight(), null);
            if (!found) {
                if (temp.platformCollide(ball.getX(), ball.getY(), ball.getWidth(), ball.getHeight())) {
                    ball.minimumHeight = Main.HEIGHT - (Main.HEIGHT - temp.getY()) - 30;
                    found=true;
                } else
                    ball.minimumHeight = Main.HEIGHT - (2 * ball.ballHeight);
            }
            if (temp.getX()<-temp.getWidth())
                toDelete.add(platforms.indexOf(temp));
        }
        if (toDelete.size() > 0)
            for (Integer temp : toDelete) {
                platforms.remove(platforms.get(temp));
            }
    }

    private void coinHandler(Graphics g) {
        ticksUntilNextCoin--;
        if (ticksUntilNextCoin == 0) {
            ticksUntilNextCoin = 100 + rand.nextInt(500);
            coins.add(new Coin(Main.WIDTH, 200 + rand.nextInt(Main.HEIGHT - 300)));
        }

        toDelete = new Vector<Integer>(1);
        for (Coin coin : coins) {
            coin.moveCoin();
            g.drawImage(coin.getImage(), coin.getX(), coin.getY(), coin.getWidth(), coin.getHeight(), null);
            if (coin.coinCollide(ball.getX(), ball.getY(), ball.getWidth(), ball.getHeight())) {
                toDelete.add(coins.indexOf(coin));
                scoreCounter+=2;
                bulletCounter+=2;
            } else if (coin.getX() < (0 - coin.getWidth())) { //out of border
                toDelete.add(coins.indexOf(coin));
            }
        }
        if (toDelete.size() > 0)
            for (Integer temp : toDelete) {
                coins.remove(coins.get(temp));
            }
    }

    private void scoreHandler(Graphics g){
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.setColor(Color.red);
        g.drawString("SCORE: " + this.scoreCounter, 20, 40);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.setColor(Color.RED);
        g.drawString("BULLETS LEFT: " + this.bulletCounter, 20, 80);
    }

    private void backgroundHandler(Graphics g){
        backgroundX1-=backgroundSpeed;
        backgroundX2-=backgroundSpeed;
        if (backgroundX1<0 && currentBackground==1) {
            backgroundX2 = Main.WIDTH-1;
            currentBackground=2;
        }
        else if (backgroundX2<0 && currentBackground==2) {
            backgroundX1 = Main.WIDTH-1;
            currentBackground=1;
        }
        g.drawImage(background,backgroundX1,0,Main.WIDTH,Main.HEIGHT,null);
        g.drawImage(background,backgroundX2,0,Main.WIDTH,Main.HEIGHT,null);
    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyReleased(KeyEvent e) {
        key=e.getKeyCode();
        switch (key){
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                longPressed=0;
                rightPressed=false;
                rightLongPressed=false;
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                leftLongPressed=false;
                longPressed=0;
                leftPressed=false;
                break;
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                jumpPressed=false;
                doubleJump=false;
        }
    }

    public void keyPressed(KeyEvent e) {
        key=e.getKeyCode();
        switch (key){
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                longPressed++;
                if (longPressed>5){
                    rightLongPressed=true;
                }
                else {
                    rightPressed = true;
                }
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                longPressed++;
                if (longPressed>5){
                    leftLongPressed=true;
                }
                else {
                    leftPressed = true;
                }
                break;
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                if (delayDoubleJump>80.0D) { //Double Jump
                    doubleJump = true;
                    delayDoubleJump=80.0D;
                }
                else if (delayDoubleJump<=0 || ball.getY()>=ball.minimumHeight) {
                    jumpPressed = true;
                    delayDoubleJump=130.0D;
                }
        }
    }

    private void mouseHandler(MouseEvent e){
        int mouseX, mouseY;
        mouseX = e.getX();
        mouseY = e.getY();
        if (mouseX >= pausePositionX && mouseX <= pausePositionX + 50 && mouseY >= pausePositionY && mouseY <= pausePositionY + 50 && !pausePressed) {
            playMusic("sounds\\\\click.wav");
            pausePressed = true;
        }else if (mouseX >= playPositionX && mouseX <= playPositionX + 50 && mouseY >= playPositionY && mouseY <= playPositionY + 50 && pausePressed){
            playMusic("sounds\\\\click.wav");
            firstTickPause=false;
            pausePressed=false;
        }else if (mouseX >= restartPositionX && mouseX <= restartPositionX + 50 && mouseY >= restartPositionY && mouseY <= restartPositionY + 50 && (pausePressed || firstTickOver)){
            playMusic("sounds\\\\click.wav");
            firstTickPause=false;
            pausePressed=false;
            menu.updateLeaderboard(scoreCounter);
            restart();
        }else if(mouseX >= menuPositionX && mouseX <= menuPositionX + 50 && mouseY >= menuPositionY && mouseY <= menuPositionY + 50 && (pausePressed || firstTickOver)){
            playMusic("sounds\\\\click.wav");
            menu.reset();
            menu.updateLeaderboard(scoreCounter);
            restart();
            mainMenu=1;
        }else{
            if (bulletCounter>0 && ball.isAlive()) {
                playMusic("sounds\\\\bullet.wav");
                bullets.add(new Bullet(ball.getX(), ball.getY(), mouseX, mouseY));
                bulletCounter--;
            }
        }
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
        mouseHandler(e);
        menu.mouseReleased(e);
    }

    public void mouseClicked(MouseEvent e) {
        //mouseHandler(e);
    }

    public void mousePressed(MouseEvent e) {
    }

}