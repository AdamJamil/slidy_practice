package com.company;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import static java.lang.Math.*;

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
                    gc.fillText("" + grid[i][j], x + tile_size / 2. - 30, y + tile_size / 2. + 17);
                else
                    gc.fillText("" + grid[i][j], x + tile_size / 2. - 14, y + tile_size / 2. + 17);

                if (grid[i][j] == 16)
                    gc.clearRect(x, y, tile_size, tile_size);
            }

    }
}
