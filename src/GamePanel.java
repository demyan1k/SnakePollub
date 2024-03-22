import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int WIDTH = 600;
    static final int HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (WIDTH*HEIGHT)/UNIT_SIZE;
    static final int DELAY = 75;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 1;
    int applesEaten;
    int appleX;
    int appleY;
    char direcrion = 'R';
    boolean running = false;
    Timer timer;
    Random random = new Random();
    JButton playAgainButton;


    GamePanel(){
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdaoter());
        startGame();

        playAgainButton = new JButton("PLAY AGAIN");
        playAgainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGame();
            }
        });
        this.add(playAgainButton);
        playAgainButton.setVisible(false);
    }

    public void startGame(){
    newApple();
    running = true;
    timer = new Timer(DELAY, this);
    timer.start();
    }



    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        if (running) {

//            for (int i = 0; i < HEIGHT / UNIT_SIZE; i++) {
//                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, HEIGHT);
//                g.drawLine(0, i * UNIT_SIZE, WIDTH, i * UNIT_SIZE);
//            }
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);

                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.white);
            g.setFont(new Font("Minecraft", Font.BOLD, 25));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("SCORE: " +applesEaten, (WIDTH- metrics.stringWidth("SCORE: "+applesEaten))/2, g.getFont().getSize());

        }
        else {
            gameOver(g);
        }
    }
    public void newApple(){
        appleX = random.nextInt((int)(WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }
    public void move(){
        for (int i = bodyParts; i>0;i--){
            x[i] = x[i-1];
            y[i] = y[i-1];

        }
        switch (direcrion){
            case 'U':
                y[0] = y[0] -UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] +UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] -UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] +UNIT_SIZE;
                break;

        }
    }

    public void checkApple(){
        if((x[0] == appleX)&&(y[0] == appleY)){
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollision(){

        for(int i = bodyParts;i>0;i--){
            if((x[0]==x[i])&&(y[0]==y[i])){
                running = false;
            }
        }

        if(x[0]<0){
            running = false ;
        }
        if(x[0]>WIDTH){
            running = false ;
        }
        if(y[0]>HEIGHT){
            running = false ;
        }
        if(y[0]<0){
            running = false ;
        }
        if(!running){
            timer.stop();
        }
    }
    public void gameOver(Graphics g){

        g.setColor(Color.white);
        g.setFont(new Font("Minecraft", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("GAME OVER", (WIDTH- metrics.stringWidth("GAME OVER"))/2, WIDTH/2);

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(running) {
            move();
            checkApple();
            checkCollision();
        } else {
            // Show the "PLAY AGAIN" button when game over
            playAgainButton.setVisible(true);
        }
        repaint();
    }

    public class MyKeyAdaoter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){

            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direcrion != 'R'){
                        direcrion = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direcrion != 'L'){
                        direcrion = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direcrion != 'D'){
                        direcrion = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direcrion != 'U'){
                        direcrion = 'D';
                    }
                    break;
            }

        }
    }
    private void resetGame() {
        // Reset all necessary game variables
        bodyParts = 1;
        applesEaten = 0;
        direcrion = 'R';
        running = true;
        newApple();
        for (int i = 0; i < bodyParts; i++) {
            x[i] = 0;
            y[i] = 0;
        }
        timer.restart();
        playAgainButton.setVisible(false);
    }
}
