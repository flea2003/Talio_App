package client.scenes;

import client.utils.ServerUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;


public class ServerConnectCtrl implements Initializable {
    private final ServerUtils server;

    private final MainCtrl mainCtrl;
    private final DashboardCtrl dashboardCtrl;

    @FXML
    private javafx.scene.control.TextField serverAddress;
    @FXML
    private javafx.scene.control.PasswordField password;

    @FXML
    private Text passwordText;

    @FXML
    private javafx.scene.control.Button connectButton;
    @FXML
    private javafx.scene.control.Button connectButton2;

    @FXML
    private ImageView seePassword;

    @FXML
    private ImageView hidePassword;

    @FXML
    private TextField showPassword;
    @FXML
    private javafx.scene.control.Button connectAdmin;

    @FXML
    Button connectUser;
    @FXML
    private Text message;


    /**
     * constructor
     * @param server the current server
     * @param mainCtrl a reference to the MainCtrl
     */
    @Inject
    public ServerConnectCtrl(ServerUtils server, MainCtrl mainCtrl, DashboardCtrl dashboardCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.dashboardCtrl = dashboardCtrl;
        dashboardCtrl.adminAccess=false;
    }

    /**
     * gets the current server
     * @return the server
     */
    public ServerUtils getServer() {
        return server;
    }


    /**
     * Connects the client to the provided server if it exists
     * when the "Connect" button is clicked
     *
     * @param event the event that triggers the method
     * @return if it managed to connect the client to the server
     */
    public boolean connectToTheServer(javafx.event.ActionEvent event) {
        if(event.getSource() == connectButton) {
            String IP=serverAddress.getText();
            if (IP.isEmpty()) {
                message.setText("This field can not be left empty");
                return false;
            }
            String server="http://"+IP+":8080";
            message.setText("Searching for the server...");
            if (serverExists(server)) {
                System.out.print("exists");
                message.setText("Connecting to the Server...");
                this.server.setServer(server);                      //set the server
                this.server.initialiseSession(IP);                  //set the websocket session
                return true;
            } else if (event.getSource() == connectButton) {
                System.out.println("here");
            }
        }
        return false;
    }

    /**
     * Checks if the provided server exists and is a Talio application
     * by sending a get request and checking the response from the input stream
     *
     * @param server the server the client is trying to connect to
     * @return if this server exists and is a Talio application
     */
    public boolean serverExists(String server) {
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(server).openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {

                BufferedReader reader=
                        new BufferedReader(new InputStreamReader(con.getInputStream()));
                String response="";
                String nextline=reader.readLine();

                while (nextline != null) {
                    response += nextline;
                    nextline = reader.readLine();
                }

                reader.close();

                if(response.contains("Talio app-74")){
                    return true;
                } else {
                    message.setText("This server does not belong to a Talio app");
                }
            } else {
                message.setText("Server not found");
            }
        } catch (IOException e) {
            message.setText("Server not found");
        }
        return false;
    }

    public boolean checkPassword(javafx.event.ActionEvent event) {
        if (event.getSource() == connectButton2) {
            if (!password.getText().equals("admin12")) {
                message.setText("Incorrect Password");
            } else {
                dashboardCtrl.adminAccess = true;
                connectButton.fire();
                return true;
            }
        }
        return false;
    }


    public void openAdminConnect(javafx.event.ActionEvent event) {
        if (event.getSource() == connectAdmin) {
            System.out.println("Lol");
            seePassword.setVisible(true);
            password.setVisible(true);
            passwordText.setVisible(true);
            connectButton2.setVisible(true);
            connectButton.setVisible(false);
            connectUser.setVisible(true);
            connectAdmin.setVisible(false);

            seePassword.setOnMousePressed( e -> {
                seePassword.setVisible(false);
                hidePassword.setVisible(true);
                password.setVisible(false);
                showPassword.setVisible(true);
                showPassword.setText(password.getText());
            });

            seePassword.setOnMouseReleased( e -> {
                seePassword.setVisible(true);
                hidePassword.setVisible(false);
                password.setVisible(true);
                showPassword.setVisible(false);
            });
        }
    }

    public void openUserConnect(javafx.event.ActionEvent event){
        if (event.getSource() == connectUser) {
            seePassword.setVisible(false);
            password.setVisible(false);
            passwordText.setVisible(false);
            connectButton2.setVisible(false);
            connectButton.setVisible(true);
            connectUser.setVisible(false);
            connectAdmin.setVisible(true);
            dashboardCtrl.adminAccess=false;
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        serverAddress.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ENTER)
                    connectButton.fire();
            }
        });
    }
}
