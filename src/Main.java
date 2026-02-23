import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.Timer;

public class Main extends JPanel implements KeyListener {

    public static final int CELL_SIZE = 20;
    public static int width = 400;
    public static int height = 400;
    public static int row = height / CELL_SIZE;
    public static int column = width / CELL_SIZE;

    private Snake snake;
    private Fruit fruit;
    private Timer t;
    private int speed = 100;
    private static String direction;
    private boolean allowKeyPress;
    private int score;
    private int highestScore;
    private boolean isGameOver;

    public Main() {
        this.setFocusable(true);
        readHighestScore();
        reset();
        addKeyListener(this);
    }

    private void setTimer() {
        t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!isGameOver) {
                    updateLogic();
                }
                repaint();
            }
        }, 0, speed);
    }

    private void reset() {
        score = 0;
        isGameOver = false;
        if (snake != null) {
            snake.getSnakeBody().clear();
        }
        allowKeyPress = true;
        direction = "Right";
        snake = new Snake();
        fruit = new Fruit();
        setTimer();
        requestFocusInWindow();
    }

    private void updateLogic() {
        ArrayList<Node> snakeBody = snake.getSnakeBody();
        int snakeX = snakeBody.get(0).x;
        int snakeY = snakeBody.get(0).y;

        if (direction.equals("Left")) snakeX -= CELL_SIZE;
        else if (direction.equals("Up")) snakeY -= CELL_SIZE;
        else if (direction.equals("Right")) snakeX += CELL_SIZE;
        else if (direction.equals("Down")) snakeY += CELL_SIZE;

        if (snakeX < 0) {
            snakeX = width - CELL_SIZE;
        } else if (snakeX >= width) {
            snakeX = 0;
        }

        if (snakeY < 0) {
            snakeY = height - CELL_SIZE;
        } else if (snakeY >= height) {
            snakeY = 0;
        }

        Node newHead = new Node(snakeX, snakeY);

        for (int i = 1; i < snakeBody.size(); i++) {
            if (snakeBody.get(i).x == newHead.x && snakeBody.get(i).y == newHead.y) {
                handleGameOver();
                return;
            }
        }

        if (newHead.x == fruit.getX() && newHead.y == fruit.getY()) {
            fruit.setNewLocation(snake);
            score++;
        } else {
            snakeBody.remove(snakeBody.size() - 1);
        }

        snakeBody.add(0, newHead);
        allowKeyPress = true;
    }

    private void handleGameOver() {
        isGameOver = true;
        t.cancel();
        t.purge();

        saveHighestScore(score);

        int response = JOptionPane.showOptionDialog(this,
                "Game Over!! Your score is " + score + ".\nThe highest score was " + highestScore + ".\nWould you like to start over?",
                "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, JOptionPane.YES_OPTION);

        if (response == JOptionPane.YES_OPTION) {
            reset();
        } else {
            System.exit(0);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 畫背景
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);

        // 畫水果與蛇
        if (fruit != null) fruit.drawFruit(g);
        if (snake != null) snake.drawSnake(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    private File getScoreFile() {
        String userHome = System.getProperty("user.home");
        File gameDir = new File(userHome, ".snakegame");

        if (!gameDir.exists()) {
            gameDir.mkdir();
        }
        return new File(gameDir, "scores.txt");
    }

    public void readHighestScore() {
        File scoreFile = getScoreFile();
        try (Scanner myReader = new Scanner(scoreFile)) {
            highestScore = myReader.nextInt();
        } catch (FileNotFoundException e) {
            highestScore = 0;
            try (FileWriter myWriter = new FileWriter(scoreFile)) {
                myWriter.write("0");
            } catch (IOException err) {
                err.printStackTrace();
            }
        }
    }

    public void saveHighestScore(int score) {
        if (score <= highestScore) return;

        File scoreFile = getScoreFile();
        try (FileWriter myWriter = new FileWriter(scoreFile)) {
            myWriter.write(String.valueOf(score));
            highestScore = score;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JFrame window = new JFrame("Snake Game");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setContentPane(new Main());
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        window.setResizable(false);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (allowKeyPress) {
            if (e.getKeyCode() == 37 && !direction.equals("Right")) direction = "Left";
            else if (e.getKeyCode() == 38 && !direction.equals("Down")) direction = "Up";
            else if (e.getKeyCode() == 39 && !direction.equals("Left")) direction = "Right";
            else if (e.getKeyCode() == 40 && !direction.equals("Up")) direction = "Down";
            allowKeyPress = false;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}