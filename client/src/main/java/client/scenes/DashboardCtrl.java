package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Card;
import commons.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
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
            System.out.println(listCurr);
            VBox vBox = new VBox();
            Label label = new Label(listCurr.name);
            label.setOnMouseClicked(e ->{
                if (e.getClickCount() == 2) {

                    System.out.println("Label was double-clicked!");
                    TextField textField = new TextField(label.getText());

                    int labelIndex = vBox.getChildren().indexOf(label);
                    vBox.getChildren().remove(labelIndex);
                    vBox.getChildren().add(labelIndex, textField);

                    textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                        if (!newValue) {
                            // Update the label with the text from the TextField when it loses focus
                            String newText = textField.getText();
                            listCurr.name= newText;
                            label.setText(listCurr.name);
                            vBox.getChildren().remove(textField);
                            vBox.getChildren().add(labelIndex, label);
                        }
                    });
                }
            });



            label.setFont(Font.font(20));
            Button addTaskButton = new Button("+");
            addTaskButton.setOnAction(e -> {
                mainCtrl.switchTaskCreation();
            });
            ListView<Card>listView = new ListView<>();

            // Call the method that sets the cell factory review.
            setFactory(listView);

            //Create a card
            vBox.getChildren().add(label);
            vBox.getChildren().add(listView);
            vBox.getChildren().add(addTaskButton);


            // Set the card in our lists
            java.util.List<Card> cardlist = listCurr.cards;
            listView.setItems(FXCollections.observableList(cardlist));
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
        list.setCellFactory(q -> new ListCell<Card>() {
            @Override
            protected void updateItem(Card q, boolean bool) {
                super.updateItem(q, bool);
                if(bool) {
                    setText("");
                }
                else{
                    setText(q.description);
                    setOnMouseClicked(event -> {
                        mainCtrl.switchTaskView(q);
                    });
                }
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

    public void createBoard(ActionEvent actionEvent) {
        mainCtrl.switchCreateBoard();
        System.out.println("new Board");
    }


}
