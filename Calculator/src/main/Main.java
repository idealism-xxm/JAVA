package main;

import gui.Calculator;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
        new Calculator().start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
