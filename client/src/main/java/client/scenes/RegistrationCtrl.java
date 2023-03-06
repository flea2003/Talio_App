package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.awt.*;
import java.awt.event.ActionEvent;

public class RegistrationCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField signUp;

    @FXML
    private TextField logIn;

    @FXML
    private javafx.scene.control.Button buttonSignUp;

    @FXML
    private javafx.scene.control.Button buttonLogIn;

    @FXML
    private AnchorPane pane;

    @FXML
    private Text error;


    @Inject
    public RegistrationCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }


    @FXML
    public void emptyTextField(javafx.scene.input.KeyEvent event){
        if(event.getSource() == signUp){
            logIn.setText(" ");
        }
        else{
            signUp.setText(" ");
        }
    }

    @FXML
    public void processClick(javafx.event.ActionEvent event){
        String value = new String("");
        setError("");
        if(event.getSource() == buttonLogIn) {
            value = extractValue(logIn);
            if(value.equals("")){
                setError("User Name cannot be empty. Please try again!");
            }
            else {
                // here we send the value to the database
                mainCtrl.switchDashboard(value);
            }
        }else if(event.getSource() == buttonSignUp){
            value = extractValue(signUp);
            if(value.equals("")){
                setError("User Name cannot be empty. Please try again!");
            }
            else{
                // here we send the value to the database
                mainCtrl.switchDashboard(value);
            }
        }else{

        }
        System.out.println(value);
        signUp.setText("");
        logIn.setText("");
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER:
                System.out.println("LMAO");
                break;
            case ESCAPE:
                System.out.println("LMAO");
                break;
            default:
                break;
        }
    }

    private String extractValue(TextField curr){
        return curr.getText();
    }

    private void setError(String err){
        error.setText(err);
    }

}
