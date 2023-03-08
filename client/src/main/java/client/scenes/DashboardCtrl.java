package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Card;
import commons.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

import java.awt.*;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
            setFactory(listView);
            java.util.List<Card> cardlist = listCurr.cards;
            var descriptions = cardlist.stream().map(x -> x.description).collect(Collectors.toList());
            listView.setItems(FXCollections.observableList(descriptions));
            hboxList.getChildren().add(vBox);
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();
            double screenHeight = bounds.getHeight();
            listView.setPrefHeight(Math.min(screenHeight - 200, listView.getItems().size() * 100)); // Set a default height based on the number of items (assuming each item is 24 pixels high)
//            hboxList.getChildren().add(new Button("+"));
        }
    }

    private void setFactory(ListView list){
        list.setCellFactory(q -> new ListCell<String>() {
            @Override
            protected void updateItem(String q, boolean bool) {
                super.updateItem(q, bool);

                setText(q);
                double size = 100; // Adjust this value to change the size of the cells
                setMinHeight(size);
                setMaxHeight(size);
                setPrefHeight(size);
                setMinWidth(size);
                setMaxWidth(size);
                setPrefWidth(size);
            }
        });
    }

}
