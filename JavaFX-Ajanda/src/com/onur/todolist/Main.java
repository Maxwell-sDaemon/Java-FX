package com.onur.todolist;

import com.onur.todolist.datamodel.TodoData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainwindow.fxml"));
        primaryStage.setTitle("TodoList");
        primaryStage.setScene(new Scene(root, 900, 500));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() {
        try {
            TodoData.getInstance().storeTodoItems();

        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void init() {
        try {
            TodoData.getInstance().loadTodoItems();

        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
