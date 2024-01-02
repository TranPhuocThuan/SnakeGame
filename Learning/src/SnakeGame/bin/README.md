# Snake Game in Java

## Overview

This Java project implements a classic Snake Game using the Swing library. The game features a snake that navigates a grid, consumes food, and avoids obstacles. The code provides a customizable and interactive gaming experience.

## Features

- **Snake Movement:** Control the snake's movement using arrow keys (UP, DOWN, LEFT, RIGHT).
  
- **Food:** Randomly placed food increases the snake's length and scores points.

- **Obstacles:** Gray-colored obstacles challenge the player, requiring strategic navigation.

- **Game Logic:** Timed intervals control the game progression. Obstacles move randomly, and the snake head wraps around the grid.

- **User Interaction:** Pause and resume the game, view score, and high score. A dialog prompts for a replay or game exit.

- **Scoring System:** The player's score is based on the snake's length multiplied by a score multiplier.

## Customization

Adjustable parameters for customization include:
- Board size
- Tile size
- Initial snake speed
- Number of obstacles

## Implementation Details

The code uses Java's Swing library for GUI components, including grid, snake, food, and obstacles. Key elements such as snake body representation with a linked list and collision detection algorithms are implemented.

## Usage

1. Clone the repository.
2. Compile and run the `SnakeGame.java` file.
3. Use arrow keys to control the snake.
4. Press 'P' to pause and resume the game.
5. Enjoy the game and try to achieve a high score!

## Contributing

Contributions are welcome! Feel free to submit issues or pull requests.

