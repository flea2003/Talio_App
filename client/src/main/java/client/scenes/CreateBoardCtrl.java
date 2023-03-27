package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class CreateBoardCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField create; // text field for create a board

    @FXML
    private javafx.scene.control.Button buttonCreate; // create button

    @FXML
    private Text error; // for the error if there is no given name


    /**
     * constructor for a "create board" scene
     * @param server
     * @param mainCtrl
     */
    @Inject
    public CreateBoardCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * sets the name text field to an empty string
     * @param event the create text field
     */
    @FXML
    public void emptyTextField(javafx.scene.input.KeyEvent event){
        if(event.getSource() == create){
            create.setText("");
        }
    }

    /**
     * the user should not create a nameless board
     * @param event if there is no name assigned and the button "create" is clicked,
     *              a warning is shown to the user
     */
    @FXML
    public void processClick(javafx.event.ActionEvent event){
        String value = new String("");
        setError("");
        if (event.getSource() == buttonCreate) {
            value = extractValue(create);
            if (value.equals("")){
                setError("Board Name cannot be empty. Please try again!");
            }
            else {
                // here we send the value to the database
                mainCtrl.switchDashboard(value);
            }
        }
        create.setText("");
    }

    /**
     * the user should not create a nameless board
     * @param e if there is no name assigned and enter is pressed,
     *          a warning is shown to the user
     */
    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER:
                String createText = extractValue(create);
                if (createText.length() >= 1){
                    buttonCreate.fire();
                } else{
                    setError("Board Name cannot be empty. Please try again!");
                }
                break;
            default:
                break;
        }
    }

    /**
     * Cancel button
     * If the user decides to not create a board, they can go back to the dashboard scene.
     */
    public void goBack() {
        String user = new String("");
        mainCtrl.switchDashboard(user);
    }

    /**
     * gets the text from a field
     * @param curr
     * @return
     */
    private String extractValue(TextField curr){
        return curr.getText();
    }

    /**
     * sets the error if there is no name for the board
     * @param err can be found in keyPressed
     */
    private void setError(String err){
        error.setText(err);
    }

    /**
     * a getter for create
     * @return the textfield create
     */
    public TextField getCreate() {
        return create;
    }

    public Button getButtonCreate() {
        return buttonCreate;
    }

    public Text getError() {
        return error;
    }
}
