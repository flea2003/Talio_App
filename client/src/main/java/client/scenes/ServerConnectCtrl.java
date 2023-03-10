package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;

import javax.inject.Inject;


public class ServerConnectCtrl {

    private final ServerUtils server;

    private final MainCtrl mainCtrl;

    @FXML
    private javafx.scene.control.TextField serverAddress;

    @FXML
    private javafx.scene.control.Button connectButton;

    @Inject
    public ServerConnectCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void connectToTheServer(javafx.event.ActionEvent event){
        if(event.getSource() == connectButton) {
            ServerUtils.setSERVER(serverAddress.getText());
            mainCtrl.switchDashboard("");
        }

    }

}
