package com.example.gameforcourse;

import com.example.gameforcourse.MainMenu;
import javafx.application.Application;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {
        MainMenu mainMenu = new MainMenu();
        mainMenu.start(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}
