package client.services;

import commons.Board;
import commons.Card;
import commons.List;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.w3c.dom.Text;

import java.util.ArrayList;

public abstract class ButtonTalio extends Button {
    public TextField textField;
    public ButtonTalio(String textLabel) {
        super();
        Pane node = addButton();
        setOnAction(e -> {
           generateTextField(textLabel, node);
        });
    }

    public ButtonTalio(String textButton, String textLabel) {
        super(textButton);
        Pane node = addButton();
        setOnAction(e -> {
            generateTextField(textLabel, node);
        });
    }

    public void generateTextField(String textLabel, Pane node){
        textField = new TextField();
        textField.setPromptText(textLabel);
        addLabel(node);
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                textField.setText("");
            } else {
                if (textField.getText().strip().length() != 0) {
                    String data = textField.getText();
                    System.out.println(data);
                    processData(data);
                    deleteLabel(node);
                }
            }
        });

        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                deleteLabel(node);
            }
        });
    }

    public abstract void processData(String data);
    public abstract void addLabel(Pane node);
    public abstract void deleteLabel(Pane node);
    public abstract Pane addButton();



}
