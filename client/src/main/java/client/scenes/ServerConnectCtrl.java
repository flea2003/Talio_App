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

    public Text getMessage() {
        return message;
    }

    @Inject
    public ServerConnectCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public boolean connectToTheServer(javafx.event.ActionEvent event) {
        if(event.getSource() == connectButton) {
            String server=serverAddress.getText();
            message.setText("Searching for Server...");
            if(serverExists(server)) {
                message.setText("Connecting to the Server...");
                this.server.setSERVER(serverAddress.getText());
                return true;
            }
        }
        return false;
    }

    public boolean serverExists(String server){
        try{
            if(server.charAt(server.length()-1)!=58){
               server+=":";
            }
            server+="8080";

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
            return false;
        }
        return false;
    }

}
