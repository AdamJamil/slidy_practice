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