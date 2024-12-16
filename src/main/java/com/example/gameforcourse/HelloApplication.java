package com.example.game;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/game/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1500, 800);

        stage.setScene(scene);
        stage.setTitle("Game");
        stage.show();

        // Получение контроллера и установка фокуса
        com.example.game.HelloController controller = fxmlLoader.getController();
        controller.gamePane.requestFocus();
    }

    public static void main(String[] args) {
        launch();
    }
}
