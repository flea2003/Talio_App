package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Card;
import commons.List;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DashboardCtrl implements Initializable {

    private Main main;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private HBox hboxList;
    @FXML
    private ScrollPane pane;
    @FXML
    private Button disconnectButton;
    @FXML
    private AnchorPane boardsPane;
    @FXML
    private VBox boardsVBox;
    private ListCell<Card> draggedCard;
    private VBox draggedVbox;
    private boolean sus;
    private boolean done = false; // this variable checks if the drag ended on a listcell or tableview
    private Card cardDragged; // this sets the dragged card
    private long idOfCurrentBoard=-1;
    @Inject
    public DashboardCtrl(Main main,ServerUtils server, MainCtrl mainCtrl) {
        this.main = main;
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refreshBoards(server.getBoards());
        server.refreshLists("/topic/updates", Boolean.class, l -> {
            Platform.runLater(() -> { // this method refreshes. The platform.runLater() because of thread issues.
                try{
                    refreshBoards(server.getBoards());
                }catch (Exception e){
                    e.printStackTrace();
                }
            });
        });
    }

    public void refreshBoards(java.util.List<Board> boards){
        if(hboxList.getUserData()!=null){
            refreshSpecificBoard((Long) hboxList.getUserData());
        }

        if (boardsVBox.getChildren().size() > 0) {
            boardsVBox.getChildren().subList(0, boardsVBox.getChildren().size()).clear();
        }

        for (Board boardCurr : boards){
            Label label = new Label(boardCurr.name);

            label.setUserData(boardCurr.id);
            if(idOfCurrentBoard != -1 && idOfCurrentBoard==boardCurr.id){
                label.setStyle("-fx-font-size: 18px;");
            }

            label.setOnMouseClicked(e -> {
                for(Node child : boardsVBox.getChildren()) {
                    child.setStyle("");
                }
                idOfCurrentBoard = (Long) label.getUserData();
                label.setStyle("-fx-font-size: 18px;");
                refreshSpecificBoard((Long) label.getUserData());
            });

            boardsVBox.getChildren().add(label);
            boardsVBox.setPadding(new Insets(5, 5, 5, 10));
            boardsVBox.setSpacing(10);
        }
    }

    public void refreshSpecificBoard(long id) {
        hboxList.setUserData(id);
        if (hboxList.getChildren().size() > 0) {
            hboxList.getChildren().subList(0, hboxList.getChildren().size()).clear();
        }

        Button addListButton = new Button("Create List");

        VBox vboxEnd = new VBox();
        vboxEnd.getChildren().add(addListButton);
        hboxList.getChildren().add(vboxEnd);

        addListButton.setOnAction(e -> {
            createList(vboxEnd, id);
        });

        if (server.getBoard(id).lists != null && server.getBoard(id).lists.size() > 0) {
            if (hboxList.getChildren().size() > 1) {
                hboxList.getChildren().subList(0, hboxList.getChildren().size() ).clear();
            }

            addLists(server.getBoard(id).lists, id);
        } else if (hboxList.getChildren().size() > 1) {
                hboxList.getChildren().subList(0, hboxList.getChildren().size() ).clear();
        }

        hboxList.setPadding(new Insets(30, 30, 30, 30));
        hboxList.setSpacing(30);
    }

    private void addLists(java.util.List<List>list, long boardId){
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
                    editList(vBox,label, boardId);
                }
            });

            //edit list using edit button
            edit.setOnAction(e->{
                editList(vBox, label, boardId);
            });

            // Add Card Button
            label.setFont(Font.font(20));
            Button addTaskButton = new Button("Add Task");

            addTaskButton.setOnAction(e -> {
                mainCtrl.switchTaskCreation(listCurr);
            });

            ListView<Card>listView = new ListView<>();

            listView.setOnDragOver(event -> {
                event.acceptTransferModes(TransferMode.MOVE);
            });

            listView.setOnDragDropped(event -> { // if the drag ended on a tableview I add a new card to it
                if (draggedCard != null) {
                    done = true; // the dragged ended succesfully
                    var sourceListView = draggedCard.getListView();
                    var sourceItems = sourceListView.getItems();
                    int sourceIndex = draggedCard.getIndex();

                    listCurr.cards.add(cardDragged); // update with the card dropped
                    server.updateList(listCurr);
                }

                event.setDropCompleted(true);
                event.consume();
            });
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

    private void editList(VBox vBox, Label label, Long boardId) {
        TextField textField = new TextField(label.getText());

        int labelIndex = vBox.getChildren().indexOf(label);
        vBox.getChildren().remove(labelIndex);
        vBox.getChildren().add(labelIndex, textField);

        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                String txt=textField.getText();
                //update the database with the changes
                List newList=server.getListById((Long) label.getUserData());
                newList.setName(txt);

                Board boardCurr=server.getBoard(boardId);
                for(int i=0; i<boardCurr.lists.size(); i++){
                    if(boardCurr.lists.get(i).getID()==newList.getID()){
                        boardCurr.lists.set(i,newList);
                    }
                }
                newList.setBoard(boardCurr);
                System.out.println(newList);

                server.updateList(newList);
            }
        });
    }

    private void setFactory(ListView list){
        list.setCellFactory(q -> new ListCell<Card>() {
            @Override
            protected void updateItem(Card q, boolean empty) {
                super.updateItem(q, empty);
                if (empty) {
                    setText("");
                }
                else{
                    setText(q.name);
                    setOnMouseClicked(event -> {
                        mainCtrl.switchTaskView(q);
                    });
                }
                setOnDragDetected(event -> { // if we detect the drag we delete the card from the list and set the done variable
                    if (getItem() == null || isEmpty()) {
                        return;
                    }

                    draggedCard = this;
                    cardDragged = getItem(); // store the Card object in a local variable
                    cardDragged.getList().cards.remove(cardDragged); // remove the card from the list
                    server.updateList(cardDragged.getList());
                    server.deleteCard(cardDragged.id);
                    Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent content = new ClipboardContent();

                    content.putString(getItem().name);
                    dragboard.setContent(content);
                    dragboard.setDragView(this.snapshot(null, null), event.getX(), event.getY());

                    event.consume();
                });

                setOnMouseDragged(event -> {

                });

                setOnDragOver(event -> { // if there is a drag over we set the black border and find if it targets the upper cell or lower
                    double mouseX = event.getSceneX();
                    double mouseY = event.getSceneY();

                    double listViewY = this.localToScene(0, 0).getY();
                    if (mouseY - listViewY >= 50) {
                        sus = false;
                        this.setStyle("-fx-border-color: transparent transparent black transparent; -fx-border-width: 0 0 4 0;");
                    } else {
                        sus = true;
                        this.setStyle("-fx-border-color: black transparent transparent transparent; -fx-border-width: 4 0 0 0;");
                    }
                    event.acceptTransferModes(TransferMode.MOVE);

                    event.consume();
                });

                setOnDragEntered(event -> {
                });

                setOnDragExited(event -> { // if the drag exists a card we update the border
                    this.setStyle("-fx-background-insets: 0 0 0 0;");
                });

                setOnDragDropped(event -> { // if the drag ends on a card we update the table
                    if (draggedCard != null) {
                        done = true;
                        var sourceListView = draggedCard.getListView();
                        int sourceIndex = draggedCard.getIndex();
                        int dropIndex = this.getIndex() + (!sus ? 1 : 0);
                        this.getItem().getList().cards.add(dropIndex, cardDragged);
                        cardDragged.setList(this.getItem().getList());
                        server.updateList(this.getItem().getList());
                    }
                    event.setDropCompleted(true);
                    event.consume();
                });

                setOnDragDone(event -> {
                    if(!done) { // if the drag ended neither on a cell nor on a table view we restore the card
                        cardDragged.getList().cards.add(cardDragged.getNumberInTheList() - 1, cardDragged);
                        server.updateList(cardDragged.getList());
                    }
                    done = false;
                });

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

    public void createList(VBox vboxEnd, long boardId){
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

                    Board boardCurr = server.getBoard(boardId);
                    List newList=new List(new ArrayList<Card>(), newText, boardCurr, boardCurr.lists.size() + 1);
                    boardCurr.lists.add(newList);

                    server.updateBoard(boardCurr);//send the text to the database

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
    }

    public void addCards(List list, VBox vBox, ListView listView){// Set the card in our lists
        java.util.List<Card> cardlist = list.cards;
        listView.setItems(FXCollections.observableList(cardlist));
        int index=0;
        if(hboxList.getChildren().size()>0){
            index = hboxList.getChildren().size() - 1;
        }
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
        hboxList.setUserData(null);
        mainCtrl.getPrimaryStage().close();
        main.start(new Stage());
    }


}

