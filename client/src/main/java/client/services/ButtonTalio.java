package client.services;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

public abstract class ButtonTalio extends Button {
    public TextField textField;

    /**
     * Constructs a button with no text on it
     * @param textLabel name of the prompt of the label
     */
    public ButtonTalio(String textLabel) {
        super();
        Pane node = addButton();
        setOnAction(e -> {
           generateTextField(textLabel, node);
        });
    }

    /**
     * Constructor which sets the name of the button to textButton
     * and the name of the placeholder label to textLabel
     * @param textButton name of the button
     * @param textLabel name of the prompt of the label
     */
    public ButtonTalio(String textButton, String textLabel) {
        super(textButton);
        Pane node = addButton();
        setOnAction(e -> {
            generateTextField(textLabel, node);
        });
    }

    /**
     * generates a textfield which takes input from user and
     * dissapears when enter is pressed or it gets unfocused
     * @param textLabel prompt of the label
     * @param node the pane where the textlabel is located
     */
    public void generateTextField(String textLabel, Pane node){
        textField = new TextField();
        textField.setPromptText(textLabel);
        addLabel(node);
        textField.requestFocus();
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                textField.setText("");
            } else {
                if (textField.getText().strip().length() != 0) {
                    String data = textField.getText();
                    processData(data);
                    deleteLabel(node);
                }else{
//                    deleteLabel(node);
                }
            }
        });

        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                deleteLabel(node);
            }
        });
    }

    /**
     * provide an implimentation on
     * what to do with the data
     * @param data the string that should be sent to the database
     */
    public abstract void processData(String data);

    /**
     * provide an implimentation on where to add the label
     * @param node the structure where the node is added
     */
    public abstract void addLabel(Pane node);

    /**
     * provide an implimentation on how to delete the label
     * @param node pane where the label is right now
     */
    public abstract void deleteLabel(Pane node);

    /**
     * provide an implimentation on how to add the button
     * @return the pane where the label is added so we can use it further
     */
    public abstract Pane addButton();



}
