package com.company;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import static java.lang.Math.*;

public class Main extends Application {
    @Override
    public void start(Stage primary_stage)
    {
        primary_stage.setTitle("slidy practice");
        Group root = new Group();
        Canvas canvas = new Canvas(490, 700);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        primary_stage.setScene(scene);
        primary_stage.show();

        final Timeline timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.setAutoReverse(true);

        Game game = new Game();

        timeline.getKeyFrames().add(new KeyFrame(Duration.millis((int) (1000.0 / 144)), event -> {
            game.draw(gc);
            game.stats.update();
        }));

        scene.setOnKeyPressed(event -> {
            game.key_pressed(event.getCode());
        });

        timeline.play();
    }


    public static void main(String[] args)
    {
        launch(args);
    }
}

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
        long time_nano = stats.end_time - stats.start_time;
        double time = ((double) time_nano) / 1000000000;
        gc.fillText(String.format("Time: %.3f", time) + "s", 30, 525);
        gc.fillText("Moves: " + stats.moves, 190, 525);
        double kps = (abs(time) < 0.00001) ? 0 : ((double) stats.moves) / time;
        gc.fillText(String.format("KPS: %.3f", kps), 330, 525);
    }
}

class Stats
{
    boolean running = false;
    long start_time, end_time;
    int moves = 0;

    void move() {
        start();
        moves++;
    }

    void update() {
        if (running)
            end_time = System.nanoTime();
    }

    void start() {
        if (running)
            return;
        running = true;
        start_time = System.nanoTime();
        end_time = start_time;
    }

    void stop() {
        running = false;
    }
}

class Grid {

    int tile_size = 110, tile_pad = 10;
    int[][] grid = new int[4][4];
    int x = 3, y = 3;
    boolean shuffled = false;

    Grid() {
        int val = 1;
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                grid[j][i] = val++;
    }

    boolean solved() {
        int val = 1;
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                if (grid[j][i] != val++)
                    return false;
        return true;
    }

    boolean move(int ox, int oy) {
        if (ox < 0 || oy < 0 || ox > 3 || oy > 3)
            return false;
        grid[x][y] = grid[ox][oy];
        grid[ox][oy] = 16;
        x = ox;
        y = oy;
        return true;
    }

    void shuffle() {
        for (int moves = 0; moves < 100000; moves++) {
            int ox = x, oy = y;
            if (random() < 0.5)
                ox += (random() < 0.5) ? 1 : -1;
            else
                oy += (random() < 0.5) ? 1 : -1;
            move(ox, oy);
        }
        shuffled = true;
    }

    void draw(GraphicsContext gc) {
        gc.setFont(new Font("Arial", 50));

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {
                int x = (i + 1) * tile_pad + i * tile_size;
                int y = (j + 1) * tile_pad + j * tile_size;

                gc.setFill(Color.BLACK);
                gc.fillRect(x - 2, y - 2, tile_size + 4, tile_size + 4);

                if (grid[i][j] >= 1 && grid[i][j] <= 4)
                    gc.setFill(Color.RED);
                else if (grid[i][j] >= 6 && grid[i][j] <= 8)
                    gc.setFill(Color.LIMEGREEN);
                else if (grid[i][j] >= 11 && grid[i][j] <= 12)
                    gc.setFill(Color.BLUE);
                else if (grid[i][j] == 10 || grid[i][j] == 14)
                    gc.setFill(Color.LIGHTBLUE);
                else if (grid[i][j] == 15)
                    gc.setFill(Color.PURPLE);
                else
                    gc.setFill(Color.YELLOW);

                gc.fillRect(x, y, tile_size, tile_size);
                gc.setFill(Color.BLACK);
                if (("" + grid[i][j]).length() == 2)
                    gc.fillText("" + grid[i][j], x + tile_size / 2 - 30, y + tile_size / 2 + 17);
                else
                    gc.fillText("" + grid[i][j], x + tile_size / 2 - 14, y + tile_size / 2 + 17);

                if (grid[i][j] == 16)
                    gc.clearRect(x, y, tile_size, tile_size);
            }

    }
}
