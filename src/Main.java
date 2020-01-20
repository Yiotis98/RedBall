import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame implements ActionListener {


    private GamePanel game;
    private Timer gameTimer;

    // Game setup constants
    static final int WIDTH  = 1500;
    static final int HEIGHT = 800;
    private static final int DELAY = 12;

    private Main() {
        super("Red Ball");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);

        String userName=JOptionPane.showInputDialog("Insert your name: ");

        // Game timer
        gameTimer = new Timer(DELAY, this);
        gameTimer.start();

        // Add Panel to Frame
        game = new GamePanel(userName);
        add(game);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    public void actionPerformed (ActionEvent e) {
        if (game != null && game.ready) {
            game.repaint();
        }
    }

    public static void main(String[] args){
        Main game = new Main();
    }

}