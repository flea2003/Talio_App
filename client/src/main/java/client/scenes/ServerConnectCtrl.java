package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Provides;
import javafx.fxml.FXML;
import javafx.fxml.LoadException;
import javafx.scene.text.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

import javax.inject.Inject;
import javax.inject.Singleton;


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

    /**
     * Connects the client to the provided server if it exists
     * when the "Connect" button is clicked
     * @param event the event that triggers the method
     * @return if it managed to connect the client to the server
     */
    public boolean connectToTheServer(javafx.event.ActionEvent event) {
        if(event.getSource() == connectButton) {
            String IP=serverAddress.getText();
            String server="http://"+IP+":8080";
            message.setText("Searching for the server...");
            if(serverExists(server)) {
                message.setText("Connecting to the Server...");
                this.server.setSERVER(server);                      //set the server
                this.server.initialiseSession(IP);                  //set the websocket session
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the provided server exists and is a Talio application
     * by sending a get request and checking the response from the input stream
     * @param server the server the client is trying to connect to
     * @return if this server exists and is a Talio application
     */
    public boolean serverExists(String server){
        try{
            HttpURLConnection con = (HttpURLConnection) new URL(server).openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {

                BufferedReader reader=new BufferedReader(new InputStreamReader(con.getInputStream()));
                String response="";
                String nextline=reader.readLine();

                while(nextline!=null){
                    response+=nextline;
                    nextline=reader.readLine();
                }

                reader.close();

                if(response.contains("Talio app")){
                    return true;
                }else{
                    message.setText("This server does not belong to a Talio app");
                }
            }else{
                message.setText("Server not found");
            }
        } catch (IOException e) {
            message.setText("Server not found");
        }
        return false;
    }

}
