package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Card;
import commons.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
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

    private List data;

    @FXML
    private ScrollPane pane;

    private ListCell<Card> draggedCard;

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

//        data = server.getLists();
        refresh();
//        colFirstName.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().person.firstName));
//        colLastName.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().person.lastName));
//        colQuote.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().quote));

    }

    public void logOut(){
        mainCtrl.switchRegistration();
    }


    public void refresh() {
        //changed so I can implement drag and drop
        addLists(server.getLists());
//        addPanels(server.getPanels());
        Button addListButton = new Button("Create List");
        VBox vboxEnd = new VBox();
        vboxEnd.getChildren().add(addListButton);
        hboxList.getChildren().add(vboxEnd);
        addListButton.setOnAction(e -> {
            createList(vboxEnd);
        });
        hboxList.setPadding(new Insets(30, 30, 30, 30));
        hboxList.setSpacing(30);
    }

    private void addLists(java.util.List<List>list){
        for(List listCurr : list){
            VBox vBox = new VBox();
            Label label = new Label(listCurr.name);
            HBox hboxButtons = new HBox();
            Button delete = new Button("Delete List");

            // Here is code for List Name edition
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
            // Add Card Button
            label.setFont(Font.font(20));
            Button addTaskButton = new Button("Add Task");
            addTaskButton.setOnAction(e -> {
                mainCtrl.switchTaskCreation();
            });
            ListView<Card>listView = new ListView<>();
            // Call the method that sets the cell factory review.
            setFactory(listView);

            // Delete List Button
            delete.setOnAction(e -> {
                vBox.getChildren().clear();
                // add something to delete the list form tables
            });

            //Create a list
            vBox.getChildren().add(label);
            vBox.getChildren().add(listView);
            vBox.getChildren().add(hboxButtons);
            hboxButtons.getChildren().add(addTaskButton);
            hboxButtons.getChildren().add(delete);

            addCards(listCurr, vBox, listView);
        }
    }

    private void setFactory(ListView list){
        list.setCellFactory(q -> new ListCell<Card>() {
            ListCell thisCell = this;
            @Override
            protected void updateItem(Card q, boolean empty) {
                super.updateItem(q, empty);
                if (empty) {
                    setText("");
                } else {
                    setText(q.description);
                    setOnMouseClicked(event -> {
                        mainCtrl.switchTaskView(q);
                    });
                }
                setOnDragDetected(event -> {
                    System.out.println("It only reaches here on start");
                    if (getItem() == null) {
                        return;
                    }
//
                    draggedCard = this;

                    Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent content = new ClipboardContent();

                    content.putString(getItem().name);
                    dragboard.setContent(content);

                    event.consume();
                });

                setOnDragOver(event -> {
                    System.out.println("testing dummer");
                    if (event.getGestureSource() != thisCell
//                            && event.getDragboard().hasString()
                    ) {
                        event.acceptTransferModes(TransferMode.MOVE);
                    }

                    event.consume();
                });

                setOnDragEntered(event -> {
                    System.out.println("testing dummy");
                    if (event.getGestureSource() != thisCell) {
                        setOpacity(0.3);
                    }
                });

                setOnDragExited(event -> {
                    if (event.getGestureSource() != thisCell) {
                        setOpacity(1);
                    }
                });

                setOnDragDropped(event -> {
//                    if (getItem() == null) {
//                        return;
//                    }

//                    Dragboard db = event.getDragboard();

                    if (draggedCard != null) {
                        var sourceListView = draggedCard.getListView();
                        var sourceItems = sourceListView.getItems();
                        int sourceIndex = draggedCard.getIndex();
                        int dropIndex = this.getIndex();

                        if (dropIndex >= 0 && dropIndex != sourceIndex) {
                            sourceItems.remove(sourceIndex);
                            this.getListView().getItems().add(dropIndex, draggedCard.getItem());
                            System.out.println("fucking works");

                        }
//                            this.getListView().getItems().add(dropIndex, draggedCard.getItem());
//                        draggedCard = null;
                    }
                    event.setDropCompleted(true);

                    event.consume();
                });

//                setOnDragDone(DragEvent::consume);

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

//private void updateList
//    private class CardCell extends ListCell<Card> {
//        private Card card;
//        public CardCell (Card card){
//            card =
//        }
//    }

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
            if (!newValue) {
                String newText = textField.getText();
                //send the text to the database
                vboxEnd.getChildren().remove(textField);
                vboxEnd.getChildren().remove(spacer);
            }
        });

        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String newText = textField.getText();
                //send the text to the database
                vboxEnd.getChildren().remove(textField);
                vboxEnd.getChildren().remove(spacer);
            }
        });
    }


    public void addCards(List list, VBox vBox, ListView listView){// Set the card in our lists
        java.util.List<Card> cardlist = list.cards;
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

