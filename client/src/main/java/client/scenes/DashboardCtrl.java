package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Card;
import commons.List;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class DashboardCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private Button logOut;

    private String currentBoard;

    @FXML
    private HBox hboxList;

    private ObservableList<List> data;

    @Inject
    public DashboardCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refresh();
//        colFirstName.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().person.firstName));
//        colLastName.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().person.lastName));
//        colQuote.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().quote));
    }

    public void logOut(){
        mainCtrl.switchRegistration();
    }

    public void refresh() {
        var list = server.getLists();
        for(List listCurr : list){
            VBox vBox = new VBox();
            Label label = new Label(listCurr.name);
            Button addTaskButton = new Button("+");
            ListView<String>listView = new ListView<>();
            vBox.getChildren().add(label);
            vBox.getChildren().add(listView);
            vBox.getChildren().add(addTaskButton);
            Map<Long, Card> map = listCurr.cards;
            for(Card card : map.values()){
                listView.getItems().add(card.description);
            }
            hboxList.getChildren().add(vBox);
//            hboxList.getChildren().add(new Button("+"));
        }
    }

}
