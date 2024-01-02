/* Name: Trần Phước Thuận - ITITIU21324
 Member : 1
 Purpose: Game for Lab10- Snake Game 
*/
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {

    // Enum to represent the possible directions the snake can move
    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    // Inner class representing a tile in the game (snake, food, obstacles)
    private class Tile {
        int x;
        int y;
        Color color;

        Tile(int x, int y, Color color) {
            this.x = x;
            this.y = y;
            this.color = color;
        }
    }

    // Constants for the game board and initial settings
    private static final int BOARD_WIDTH = 500;
    private static final int BOARD_HEIGHT = 500;
    private static final int TILE_SIZE = 25;
    private static final int INITIAL_SPEED = 100;
    private static final int NUM_OBSTACLES = 10;

    // Game state variables
    private int speed;
    private int highScore;
    private Tile snakeHead;
    private LinkedList<Tile> snakeBody;
    private Tile food;
    private Random random;
    private LinkedList<Tile> obstacles;
    private Direction currentDirection;
    private Timer gameLoop;
    private boolean gameOver;
    private boolean paused;
    private int scoreMultiplier;
    private int obstacleSpeed;
    private Timer obstacleMoveTimer;

    // Constructor for the SnakeGame class
    SnakeGame() {
        // Set up the panel
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        // Initialize game elements
        snakeHead = new Tile(5, 5, Color.green);
        snakeBody = new LinkedList<>();
        food = new Tile(10, 10, Color.red);
        random = new Random();
        obstacles = new LinkedList<>();
        placeObstacles();
        placeFood();

        // Set initial game settings
        speed = INITIAL_SPEED;
        currentDirection = Direction.RIGHT;
        gameLoop = new Timer(speed, this);
        gameLoop.start();
        highScore = 0;
        scoreMultiplier = 1;
        obstacleSpeed = 500;

        // Timer for moving obstacles
        obstacleMoveTimer = new Timer(obstacleSpeed, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveObstacles();
                repaint();
            }
        });
        obstacleMoveTimer.start();
    }

    // Method to paint the game components
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    // Method to draw the game components on the panel
    public void draw(Graphics g) {
        // Draw grid lines
        for (int i = 0; i < BOARD_WIDTH / TILE_SIZE; i++) {
            g.drawLine(i * TILE_SIZE, 0, i * TILE_SIZE, BOARD_HEIGHT);
            g.drawLine(0, i * TILE_SIZE, BOARD_WIDTH, i * TILE_SIZE);
        }

        // Draw obstacles
        g.setColor(Color.gray);
        for (Tile obstacle : obstacles) {
            g.fill3DRect(obstacle.x * TILE_SIZE, obstacle.y * TILE_SIZE, TILE_SIZE, TILE_SIZE, true);
        }

        // Draw food
        g.setColor(food.color);
        g.fill3DRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE, true);

        // Draw snake head
        g.setColor(snakeHead.color);
        g.fill3DRect(snakeHead.x * TILE_SIZE, snakeHead.y * TILE_SIZE, TILE_SIZE, TILE_SIZE, true);

        // Draw snake body
        for (Tile snakePart : snakeBody) {
            g.setColor(snakePart.color);
            g.fill3DRect(snakePart.x * TILE_SIZE, snakePart.y * TILE_SIZE, TILE_SIZE, TILE_SIZE, true);
        }

        // Draw score
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if (gameOver) {
            g.setColor(Color.red);
            g.drawString("Game Over: " + snakeBody.size() + " | High Score: " + highScore, TILE_SIZE - 16, TILE_SIZE);
        } else {
            g.drawString("Score: " + snakeBody.size() + " | High Score: " + highScore, TILE_SIZE - 16, TILE_SIZE);
        }

        // Draw pause message
        if (paused) {
            g.setColor(Color.white);
            g.drawString("Paused - Press P to resume", BOARD_WIDTH / 2 - 80, BOARD_HEIGHT / 2);
        }
    }

    // Method to place food in a valid position on the board
    public void placeFood() {
        boolean validPosition = false;

        while (!validPosition) {
            food.x = random.nextInt(BOARD_WIDTH / TILE_SIZE);
            food.y = random.nextInt(BOARD_HEIGHT / TILE_SIZE);

            // Check if the food position collides with any obstacles
            boolean collisionWithObstacles = false;
            for (Tile obstacle : obstacles) {
                if (collision(food, obstacle)) {
                    collisionWithObstacles = true;
                    break;
                }
            }

            // Check if the food position collides with the snake body
            boolean collisionWithSnake = false;
            for (Tile snakePart : snakeBody) {
                if (collision(food, snakePart)) {
                    collisionWithSnake = true;
                    break;
                }
            }

            // If there is no collision, the position is valid
            if (!collisionWithObstacles && !collisionWithSnake) {
                validPosition = true;
            }
        }
    }

    // Method to place initial obstacles on the board
    public void placeObstacles() {
        for (int i = 0; i < NUM_OBSTACLES; i++) {
            int obstacleX = random.nextInt(BOARD_WIDTH / TILE_SIZE);
            int obstacleY = random.nextInt(BOARD_HEIGHT / TILE_SIZE);
            obstacles.add(new Tile(obstacleX, obstacleY, Color.gray));
        }
    }

    // Method to move obstacles randomly
    private void moveObstacles() {
        for (Tile obstacle : obstacles) {
            int direction = random.nextInt(4);
            switch (direction) {
                case 0: // Move up
                    obstacle.y = (obstacle.y - 1 + BOARD_HEIGHT / TILE_SIZE) % (BOARD_HEIGHT / TILE_SIZE);
                    break;
                case 1: // Move down
                    obstacle.y = (obstacle.y + 1) % (BOARD_HEIGHT / TILE_SIZE);
                    break;
                case 2: // Move left
                    obstacle.x = (obstacle.x - 1 + BOARD_WIDTH / TILE_SIZE) % (BOARD_WIDTH / TILE_SIZE);
                    break;
                case 3: // Move right
                    obstacle.x = (obstacle.x + 1) % (BOARD_WIDTH / TILE_SIZE);
                    break;
            }
        }
    }

    // Method to reset the game to its initial state
    private void resetGame() {
        snakeHead = new Tile(5, 5, Color.green);
        snakeBody.clear();
        placeFood();
        currentDirection = Direction.RIGHT;
        gameOver = false;
        gameLoop.start();
        speed = INITIAL_SPEED;
        paused = false;
        scoreMultiplier = 1;
    }

    // Method to handle the movement of the snake and game logic
    public void move() {
        if (!paused) {
            // Eat food
            if (collision(snakeHead, food)) {
                snakeBody.addLast(new Tile(food.x, food.y, Color.yellow));
                placeFood();

                // Increase score
                int currentScore = snakeBody.size() * scoreMultiplier;

                // Update high score
                if (currentScore > highScore) {
                    highScore = currentScore;
                }
            }

            // Move snake body
            if (!snakeBody.isEmpty()) {
                LinkedList<Tile> newBody = new LinkedList<>();
                newBody.add(new Tile(snakeHead.x, snakeHead.y, Color.blue));

                // Copy the remaining body, but skip the last element
                int length = snakeBody.size();
                for (int i = 0; i < length - 1; i++) {
                    newBody.add(snakeBody.get(i));
                }

                snakeBody = newBody;
            }

            // Move snake head with wrap-around
            switch (currentDirection) {
                case UP:
                    snakeHead.y = (snakeHead.y - 1 + BOARD_HEIGHT / TILE_SIZE) % (BOARD_HEIGHT / TILE_SIZE);
                    break;
                case DOWN:
                    snakeHead.y = (snakeHead.y + 1) % (BOARD_HEIGHT / TILE_SIZE);
                    break;
                case LEFT:
                    snakeHead.x = (snakeHead.x - 1 + BOARD_WIDTH / TILE_SIZE) % (BOARD_WIDTH / TILE_SIZE);
                    break;
                case RIGHT:
                    snakeHead.x = (snakeHead.x + 1) % (BOARD_WIDTH / TILE_SIZE);
                    break;
            }

            // Game over conditions
            for (Tile obstacle : obstacles) {
                if (collision(snakeHead, obstacle)) {
                    gameOver = true;
                }
            }

            for (Tile snakePart : snakeBody) {
                if (collision(snakeHead, snakePart)) {
                    gameOver = true;
                }
            }

            if (snakeHead.x < 0 || snakeHead.x >= BOARD_WIDTH / TILE_SIZE ||
                    snakeHead.y < 0 || snakeHead.y >= BOARD_HEIGHT / TILE_SIZE) {
                gameOver = true;
            }
        }
    }

    // Method to check collision between two tiles
    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    // ActionListener method for handling game updates
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            gameLoop.stop();
            JOptionPane.showMessageDialog(this, "Game Over: " + snakeBody.size() * scoreMultiplier +
                    " | High Score: " + highScore, "Game Over", JOptionPane.INFORMATION_MESSAGE);
            int option = JOptionPane.showConfirmDialog(this, "Do you want to play again?", "Play Again",
                    JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                resetGame();
            } else {
                System.exit(0);
            }
        }
    }

    // KeyListener method for handling key events
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && currentDirection != Direction.DOWN) {
            currentDirection = Direction.UP;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && currentDirection != Direction.UP) {
            currentDirection = Direction.DOWN;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && currentDirection != Direction.RIGHT) {
            currentDirection = Direction.LEFT;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && currentDirection != Direction.LEFT) {
            currentDirection = Direction.RIGHT;
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE && gameOver) {
            resetGame();
        } else if (e.getKeyCode() == KeyEvent.VK_P) {
            paused = !paused;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Snake Game");
            SnakeGame snakeGame = new SnakeGame();
            frame.add(snakeGame);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            snakeGame.requestFocus();
        });
    }
}
