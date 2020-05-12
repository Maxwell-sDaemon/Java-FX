package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

//        GridPane root = new GridPane();
//        root.setAlignment(Pos.CENTER);
//        root.setHgap(10);
//        root.setVgap(10);
//
//        Label greeting = new Label("Welcome babe my label world");
//        greeting.setTextFill(Color.GREEN);
//        greeting.setFont(Font.font("Times New Roamn", FontWeight.BOLD, FontPosture.ITALIC,70));
//        root.getChildren().add(greeting);
//        primaryStage.setTitle("Hello Babe!");
//
        primaryStage.setScene(new Scene(root, 500, 100));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
