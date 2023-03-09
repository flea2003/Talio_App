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
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class DashboardCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private Button logOut;

    private String currentBoard;

    @FXML
    private HBox hboxList;

    private ObservableList<List> data;

    @FXML
    private ScrollPane pane;

    @Inject
    public DashboardCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        new Timer().scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                refresh();
//            }
//        }, 0, 1000);
        refresh();
//        colFirstName.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().person.firstName));
//        colLastName.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().person.lastName));
//        colQuote.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().quote));

    }

    public void logOut(){
        mainCtrl.switchRegistration();
    }

    public void refresh() {
        addLists(server.getLists());
//        addPanels(server.getPanels());
        hboxList.getChildren().add(new Button("Create List"));
        hboxList.setPadding(new Insets(30, 30, 30, 30));
        hboxList.setSpacing(30);
    }

    private void addLists(java.util.List<List>list){
        for(List listCurr : list){
            VBox vBox = new VBox();
            Label label = new Label(listCurr.name);
            label.setFont(Font.font(20));
            Button addTaskButton = new Button("+");

            ListView<String>listView = new ListView<>();
            
            // Call the method that sets the cell factory review.
            setFactory(listView);
            
            //Create a card
            vBox.getChildren().add(label);
            vBox.getChildren().add(listView);
            vBox.getChildren().add(addTaskButton);
            
            
            // Set the card in our lists
            java.util.List<Card> cardlist = listCurr.cards;
            var descriptions = cardlist.stream().map(x -> x.description).collect(Collectors.toList());
            listView.setItems(FXCollections.observableList(descriptions));
            hboxList.getChildren().add(vBox);
            
            // Make the card have a specified height and width
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();
            double screenHeight = bounds.getHeight();
            double screenWidth= bounds.getWidth();
            VBox.setMargin(vBox, new Insets(10, 10, 10, 10));
            vBox.setMaxWidth(250);
            listView.setPrefHeight(Math.min(screenHeight - screenHeight/4, listView.getItems().size() * 100)); // Set a default height based on the number of items (assuming each item is 24 pixels high)
        
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
