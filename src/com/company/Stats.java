package com.company;

import static java.lang.Math.*;

class Stats
{
    boolean running = false;
    long start_time, end_time;
    double time, kps;
    int moves = 0;

    void move() {
        start();
        moves++;
    }

    void update() {
        if (running) {
            end_time = System.nanoTime();
            time = ((double) (end_time - start_time)) / 1000000000;
            kps = (abs(time) < 0.001) ? 0 : moves / time;
        }
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