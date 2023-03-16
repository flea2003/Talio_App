package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Card;
import commons.List;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class DashboardCtrl implements Initializable {

    private Main main;
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

    @FXML
    private Button refreshButton;
    @FXML
    private Button disconnectButton;
    @Inject
    public DashboardCtrl(Main main,ServerUtils server, MainCtrl mainCtrl) {
        this.main = main;
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

//        new Timer().scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                Platform.runLater(() -> {
//                    try{
//                        fetchUpdatesDashboard();
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//                });
//            }
//        }, 0L, 500L);
        server.refreshLists("/topic/updates", Boolean.class, l -> {
            Platform.runLater(() -> {
                try{
                    fetchUpdatesDashboard();
                }catch (Exception e){
                    e.printStackTrace();
                }
            });
//            refreshDashboard();
        });
    }

    public void logOut(){
        mainCtrl.switchRegistration();
    }

    public void refresh() {
        if(hboxList.getChildren().size() >= 1) {
            for (int i = hboxList.getChildren().size() - 2; i >= 0; i--) {
                hboxList.getChildren().remove(i);

            }
            addLists(server.getLists());
        }
        else{
            Button addListButton = new Button("Create List");
            VBox vboxEnd = new VBox();
            vboxEnd.getChildren().add(addListButton);
            hboxList.getChildren().add(vboxEnd);
            addListButton.setOnAction(e -> {
                createList(vboxEnd);
            });
        }

        hboxList.setPadding(new Insets(30, 30, 30, 30));
        hboxList.setSpacing(30);
    }

    private void addLists(java.util.List<List>list){
        for(List listCurr : list){
            VBox vBox = new VBox();
            Label label = new Label(listCurr.name);

            label.setUserData(listCurr.getID()); // set the list id as the label's UserData

            HBox hboxButtons = new HBox();
            Button delete = new Button("Delete List");
            Button edit=new Button("Edit List");

            //edit list using double-click
            label.setOnMouseClicked(e ->{
                if (e.getClickCount() == 2) {
                    System.out.println("Label was double-clicked!");
                    editList(vBox,label);
                }
            });

            //edit list using edit button
            edit.setOnAction(e->{
                editList(vBox,label);
            });

            // Add Card Button
            label.setFont(Font.font(20));
            Button addTaskButton = new Button("Add Task");
            addTaskButton.setOnAction(e -> {
                mainCtrl.switchTaskCreation(listCurr);
            });
            ListView<Card>listView = new ListView<>();
            // Call the method that sets the cell factory review.
            setFactory(listView);

            // Delete List Button
            delete.setOnAction(e -> {
                server.deleteList((Long) label.getUserData());
            });

            //Create a list
            vBox.getChildren().add(label);
            vBox.getChildren().add(listView);
            vBox.getChildren().add(hboxButtons);
            hboxButtons.getChildren().add(addTaskButton);
            hboxButtons.getChildren().add(delete);
            hboxButtons.getChildren().add(edit);

            addCards(listCurr, vBox, listView);
        }
    }

    private void editList(VBox vBox, Label label) {
        TextField textField = new TextField(label.getText());

        int labelIndex = vBox.getChildren().indexOf(label);
        vBox.getChildren().remove(labelIndex);
        vBox.getChildren().add(labelIndex, textField);

        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                String txt=textField.getText();
                //update the database with the changes
                server.updateListName(server.getListById((Long) label.getUserData()),txt);
            }
        });
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
                    setText(q.name);
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

    public void createList(VBox vboxEnd){
        if(vboxEnd.getChildren().size()>1){
            ObservableList<Node> children = vboxEnd.getChildren();
            int numChildren = children.size();
            children.remove(numChildren - 1);
            children.remove(numChildren - 2);
        }
        TextField textField = new TextField("Enter List Name");
        Region spacer = new Region();
        spacer.setPrefHeight(10);
        vboxEnd.getChildren().add(spacer);
        vboxEnd.getChildren().add(textField);

        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                textField.setText("");
            }else{
                if(textField.getText().strip().length()!=0) {
                    String newText = textField.getText();
//
                    server.addList(new List(newText));//send the text to the database
//
//                    server.send("/app/addlist", new List(newText));

                    vboxEnd.getChildren().remove(textField);
                    vboxEnd.getChildren().remove(spacer);
                }
            }
        });

        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                    String newText = textField.getText();
                    vboxEnd.getChildren().remove(textField);
                    vboxEnd.getChildren().remove(spacer);
            }
        });

//        String text = textField.getText();
//        server.send("/app/lists",new List(text));
    }


    public void addCards(List list, VBox vBox, ListView listView){// Set the card in our lists
        java.util.List<Card> cardlist = list.cards;
        listView.setItems(FXCollections.observableList(cardlist));
        int index = hboxList.getChildren().size() - 1;
        hboxList.getChildren().add(index, vBox);

        // Make the card have a specified height and width
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        double screenHeight = bounds.getHeight();
        double screenWidth= bounds.getWidth();
        VBox.setMargin(vBox, new Insets(10, 10, 10, 10));
        vBox.setMaxWidth(250);
        listView.setPrefHeight(Math.min(screenHeight - screenHeight/4, listView.getItems().size() * 100)); // Set a default height based on the number of items (assuming each item is 24 pixels high)
    }

    @FXML
    public void serverDisconnect(){
        mainCtrl.getPrimaryStage().close();
        main.start(new Stage());
    }

    @FXML
    public void refreshDashboard(){
        System.out.println("VREAU REFRESH");
        mainCtrl.switchDashboard("");
    }

    @FXML
    public void fetchUpdatesDashboard(){
        mainCtrl.fetchUpdatesDashboard("");
    }

}

