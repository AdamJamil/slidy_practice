package com.company;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import static java.lang.Math.*;

class Game {
    Grid grid = new Grid();
    Stats stats = new Stats();

    void key_pressed(KeyCode key) {
        if (key == KeyCode.UP)
            if (grid.move(grid.x, grid.y - 1) && grid.shuffled)
                stats.move();
        if (key == KeyCode.DOWN)
            if (grid.move(grid.x, grid.y + 1) && grid.shuffled)
                stats.move();
        if (key == KeyCode.LEFT)
            if (grid.move(grid.x - 1, grid.y) && grid.shuffled)
                stats.move();
        if (key == KeyCode.RIGHT)
            if (grid.move(grid.x + 1, grid.y) && grid.shuffled)
                stats.move();
        if (key == KeyCode.SPACE) {
            grid.shuffle();
            stats = new Stats();
        }

        if (grid.solved()) {
            stats.stop();
            grid.shuffled = false;
        }
    }

    void draw(GraphicsContext gc) {
        grid.draw(gc);

        gc.setFont(new Font("Arial", 20));

        gc.setFill(Color.LIGHTGREY);
        gc.fillRoundRect(20, 500, 150, 35, 10, 10);
        gc.fillRoundRect(180, 500, 130, 35, 10, 10);
        gc.fillRoundRect(320, 500, 150, 35, 10, 10);

        gc.setFill(Color.BLACK);
        gc.fillText(String.format("Time: %.3f", stats.time) + "s", 30, 525);
        gc.fillText("Moves: " + stats.moves, 190, 525);
        gc.fillText(String.format("KPS: %.3f", stats.kps), 330, 525);
    }
}