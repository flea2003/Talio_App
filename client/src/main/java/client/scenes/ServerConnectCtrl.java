package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.*;

import javax.inject.Inject;


public class ServerConnectCtrl {

    private final ServerUtils server;

    private final MainCtrl mainCtrl;

    @FXML
    private javafx.scene.control.TextField serverAddress;

    @FXML
    private javafx.scene.control.Button connectButton;

    @FXML
    private Text message;

    @Inject
    public ServerConnectCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void connectToTheServer(javafx.event.ActionEvent event){
        if(event.getSource() == connectButton) {
            String server=serverAddress.getText();
            message.setText("Searching for Server...");
            if(serverExists(server)){
                message.setText("Connecting to the Server...");
                ServerUtils.setSERVER(serverAddress.getText());
                mainCtrl.switchDashboard("");
            }else{
                message.setText("Server not found");
            }
        }
    }

    public boolean serverExists(String server){
        try{
            HttpURLConnection con = (HttpURLConnection) new URL(server).openConnection();
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return true;
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }

}
