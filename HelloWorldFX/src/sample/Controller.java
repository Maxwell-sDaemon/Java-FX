package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Controller {
    @FXML
    private TextField nameField;
    @FXML
    private Button button1;
    @FXML
    private Button button2;
    @FXML
    private CheckBox checkBox1;
    @FXML
    private Label label;

    //ActionEvent classı gelen mega kalbimdeki nesnemi caldı.
    @FXML
    public void onButtonHandler(ActionEvent e){
        System.out.println("Gotcha! " + nameField.getText());
        System.out.println("bastığın fak budur : " + e.getSource());//class toStringi iş başında
        if (e.getSource().equals(button1)){
            System.out.println("naber !");
        }else if (e.getSource().equals(button2)){
            System.out.println("iyidir sahip senden naber.");
        }
        //uff thread fenaa
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                    System.out.println("iş tamamdır ali kaptan");
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            label.setText("canım neyi düşünüyorsun kara kara");
                        }
                    });
                }catch (InterruptedException e){
                    System.out.println(e.getStackTrace());
                }
            }
        };
        new Thread(task).start();
        if (checkBox1.isSelected()){
            nameField.clear();
            button1.setDisable(true);
            button2.setDisable(true);
        }
    }

    @FXML
    public void handleKeyReleased(){
        String handledKey = nameField.getText();
        boolean disableButton = handledKey.trim().isEmpty() || handledKey.isEmpty();
        button1.setDisable(disableButton);
        button2.setDisable(disableButton);
    }
    @FXML
    public void handleChange() {
        System.out.println(checkBox1.isSelected() ? "checked" : "nahh");
    }
}
