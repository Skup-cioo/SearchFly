package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;


public class Script extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/MainScreen.fxml")));
        Scene scene = new Scene(root, 608, 577);
        scene.getStylesheets().add("/Theme.css");
        primaryStage.setTitle("SearchFly");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}