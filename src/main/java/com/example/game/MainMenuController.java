package com.example.game;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainMenu extends Application {

    @Override
    public void start(Stage stage) {
        // Фон
        Image backgroundImage = new Image(getClass().getResourceAsStream("/com/example/game/background.jpg"));
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(1500);
        backgroundView.setFitHeight(800);
        backgroundView.setPreserveRatio(true);

        // Кнопка для начала игры
        Button startButton = new Button("Start Game");
        startButton.setStyle("-fx-font-size: 24px; -fx-padding: 10 20;");

        // Событие нажатия кнопки
        startButton.setOnAction(e -> {
            try {
                // Переход к основному экрану игры
                HelloController gameApp = new HelloController();
                gameApp.start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Создаем панель и размещаем элементы
        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundView, startButton);
        StackPane.setAlignment(startButton, javafx.geometry.Pos.CENTER);

        Scene menuScene = new Scene(root, 1500, 800);
        stage.setScene(menuScene);
        stage.setTitle("KAREL 2D Game - Main Menu");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
