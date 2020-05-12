package com.onur.todolist;

import com.onur.todolist.datamodel.TodoData;
import com.onur.todolist.datamodel.TodoItem;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class Controller {

    private List<TodoItem> todoItems;

    @FXML
    private ListView<TodoItem> todoListView;

    @FXML
    private TextArea itemDetailsTextArea;

    @FXML
    private Label deadlineLabel;

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private ContextMenu listContextMenu;

    @FXML
    private ToggleButton filteredToggleButton;

    private FilteredList<TodoItem> filteredList;

    private Predicate<TodoItem> wantAllItems;

    private Predicate<TodoItem> wantTodaysItem;

    public void initialize() {

        listContextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                TodoItem item = todoListView.getSelectionModel().getSelectedItem();
                deleteItem(item);
            }
        });

        wantTodaysItem = new Predicate<TodoItem>() {
            @Override
            public boolean test(TodoItem item) {
                return item.getDeadline().isEqual(LocalDate.now());
            }
        };
        wantAllItems = new Predicate<TodoItem>() {
            @Override
            public boolean test(TodoItem item) {
                return true;
            }
        };
        listContextMenu.getItems().add(deleteMenuItem);
        todoListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TodoItem>() {
            @Override
            public void changed(ObservableValue<? extends TodoItem> observable, TodoItem oldValue, TodoItem newValue) {
                if(newValue != null) {
                    TodoItem item = todoListView.getSelectionModel().getSelectedItem();
                    itemDetailsTextArea.setText(item.getDetails());
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("MMMM d, yyyy"); // "d M yy");
                    deadlineLabel.setText(df.format(item.getDeadline()));
                }
            }
        });

        filteredList = new FilteredList<>(TodoData.getInstance().getTodoItems(), new Predicate<TodoItem>() {
            @Override
            public boolean test(TodoItem item) {
                return true;
            }
        });



        SortedList<TodoItem> sortedList = new SortedList<>( filteredList, new Comparator<>() {
            @Override
            public int compare(TodoItem o1, TodoItem o2) {
                return o1.getDeadline().compareTo(o2.getDeadline());
            }
        });

        //todoListView.setItems(TodoData.getInstance().getTodoItems());
        todoListView.setItems(sortedList);
        todoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        todoListView.getSelectionModel().selectFirst();

        todoListView.setCellFactory(new Callback<ListView<TodoItem>, ListCell<TodoItem>>() {
            @Override
            public ListCell<TodoItem> call(ListView<TodoItem> todoItemListView) {
                ListCell<TodoItem> cell = new ListCell<TodoItem>(){
                    @Override
                    protected void updateItem(TodoItem todoItem, boolean empty) {
                        super.updateItem(todoItem, empty);
                        if (empty){
                            setText(null);
                        }else{
                            setText(todoItem.getShortDescription());
                            if (todoItem.getDeadline().isBefore(LocalDate.now())){
                                setTextFill(Color.RED);
                            }
                        }
                    }
                };
                cell.emptyProperty().addListener(
                        (obs, wasEmpty, isNowEmpty) -> {
                            if (isNowEmpty){
                                cell.setContextMenu(null);
                            }else {
                                cell.setContextMenu(listContextMenu);
                            }
                        }
                );
                return cell;
            }
        });

    }

    @FXML
    public void showNewItemDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Add New Todo Item");
        dialog.setHeaderText("Use this dialog to create a new todo item");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("todoItemDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());

        } catch(IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            DialogController controller = fxmlLoader.getController();
            TodoItem newItem = controller.processResults();
            todoListView.setItems(TodoData.getInstance().getTodoItems());
            System.out.println("OK pressed");
            initialize();

        } else {
            System.out.println("Cancel pressed");
        }


    }

    public void deleteItem(TodoItem item){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning");
        alert.setHeaderText("Delete Item : " + item.getShortDescription());
        alert.setContentText("Are you sure? Press Ok to confirm, or cancel to back out.");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && (result.get() == ButtonType.OK)){
            TodoData.getInstance().removeItem(item);
        }
    }

    @FXML
    public void handleFilterButton(){
        TodoItem selectedItem = todoListView.getSelectionModel().getSelectedItem();
        if (filteredToggleButton.isSelected()){
            filteredList.setPredicate(wantTodaysItem);
            todoListView.setItems(filteredList);

            if (filteredList.isEmpty()){
                itemDetailsTextArea.clear();
                deadlineLabel.setText("");
            } else if (filteredList.contains(selectedItem)){
                todoListView.getSelectionModel().select(selectedItem);
            } else {
                todoListView.getSelectionModel().selectFirst();
            }

        }else{
            filteredList.setPredicate(wantAllItems);
            itemDetailsTextArea.clear();
        }
    }


    @FXML
    public void handleClickListView() {
        TodoItem item = todoListView.getSelectionModel().getSelectedItem();
        itemDetailsTextArea.setText(item.getDetails());
        deadlineLabel.setText(item.getDeadline().toString());
    }

    @FXML
    public void handleExits(){
        Platform.exit();
    }
}
