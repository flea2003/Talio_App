package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.awt.*;

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
            logIn.setText("");
        }
        else{
            signUp.setText("");
        }
    }

    @FXML
    public void processClick(javafx.event.ActionEvent event){
        System.out.println(event);
        String value = new String("");
        setError("");
        System.out.println(buttonLogIn);
        System.out.println(event.getSource());
        if(event.getSource() == buttonLogIn) {
            value = extractValue(logIn);
            if(value.equals("")){
                setError("User Name cannot be empty. Please try again1!");
            }
            else {
                // here we send the value to the database
                mainCtrl.switchDashboard(value);
            }
        }else if(event.getSource() == buttonSignUp){
            value = extractValue(signUp);
            if(value.equals("")){
                setError("User Name cannot be empty. Please try again2!");
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
                String logInText = extractValue(logIn);
                String signUpText = extractValue(signUp);
                if(logInText.length() >= 1){
                    buttonLogIn.fire();
                }else if(signUpText.length() >= 1) {
                    buttonSignUp.fire();
                }else{
                    setError("User Name cannot be empty. Please try again2!");
                }
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
