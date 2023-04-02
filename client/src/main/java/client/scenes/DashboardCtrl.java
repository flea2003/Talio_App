package client.scenes;

import client.Main;
import client.services.ButtonTalio;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Card;
import commons.List;
import commons.Subtask;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;


public class DashboardCtrl implements Initializable {

    private Main main;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private HBox hboxList;

    private List data;

    @FXML
    public Button shareBoard;
    @FXML
    private ScrollPane pane;
    @FXML
    private Button disconnectButton;
    @FXML
    private AnchorPane boardsPane;
    @FXML
    private VBox boardsVBox;

    @FXML
    private Button addBoardButton;
    private ListCell<Card> draggedCard;
    private VBox draggedVbox;
    private boolean sus;
    private boolean done = false;
    // this variable checks if the drag ended on a listcell or tableview
    private Card cardDragged; // this sets the dragged card
    private long idOfCurrentBoard = -1;
    @FXML
    private BorderPane innerBoardsPane;
    private java.util.List<commons.Board> localBoards;
    private Board focusedBoard;
    java.util.List<Board> connectedBoards;
    Map<String, java.util.List<Board>> serverBoards;
    @FXML
    private Button addBoard;
    @FXML
    private TextField addBoardLabel;

    /**
     * constructor
     * @param main a reference to the main method of the client side
     * @param server the current server
     * @param mainCtrl a reference to the MainCtrl
     */
    @Inject
    public DashboardCtrl(Main main, ServerUtils server, MainCtrl mainCtrl) {
        this.main = main;
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     *
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String currentServer = server.getServer();
        if(serverBoards == null) {
            serverBoards = new HashMap<>();
        }
        if (serverBoards.get(currentServer) == null) {
            java.util.List<Board> boards = new ArrayList<>();
            serverBoards.put(currentServer, boards);
        }
        addBoardLabel.setVisible(false);
        connectedBoards = new ArrayList<>();
        connectedBoards.addAll(serverBoards.get(currentServer));
        openShare();
        openAddBoard();

//        ButtonTalio addBoard = new ButtonTalio("Create Board", "Enter Board Name") {
//            @Override
//            public void processData(String data) {
//
//            }
//
//            @Override
//            public void addLabel(Pane node) {
//
//            }
//
//            @Override
//            public void deleteLabel(Pane node) {
//
//            }
//
//            @Override
//            public Pane addButton() {
//                return null;
//            }
//        }

        addBoard.setOnAction(e -> {
            createBoard();
        });

        refreshBoards(connectedBoards);
        server.refreshBoards("/topic/updates", Boolean.class, l -> {
            // this method refreshes. The platform.runLater() because of thread issues.
            Platform.runLater(() -> {
                try{
                    refreshBoards(serverBoards.get(currentServer));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
    }

    /**
     * refreshes the dashboard with the new boards
     * @param boards the boards to populate the dashboard
     */
    public void refreshBoards(java.util.List<Board> boards){
        if(hboxList.getUserData()!=null){
            refreshSpecificBoard((Long) hboxList.getUserData());
            refreshSpecificBoard((Long) hboxList.getUserData());
        }

        if (boardsVBox.getChildren().size() > 0) {
            boardsVBox.getChildren().subList(0, boardsVBox.getChildren().size()).clear();
        }

        for (Board boardCurr : boards) {
            Label label = new Label(boardCurr.name);

            //create delete icon
            Image dots = new Image("pictures/dots.png");
            ImageView dotsView = new ImageView(dots);
            dotsView.setFitWidth(20);
            dotsView.setFitHeight(20);

            Button dotsMenu = new Button();
            dotsMenu.setGraphic(dotsView);
            dotsMenu.setContextMenu(createThreeDotContextMenu(label, dotsMenu));

//            Node dotMenu = new Group(backroundDelete, imageDelete);

            HBox hBox = new HBox(label, new Region(), dotsMenu);
            hBox.setMaxWidth(120);
            //set the preferred width to the max width so the updates are noy noticeable
            hBox.setPrefWidth(120);
            hBox.setAlignment(Pos.CENTER_LEFT);
            //Set spacer to fill available space
            HBox.setHgrow(hBox.getChildren().get(1), Priority.ALWAYS);


            //Ensure that there is space for the buttons
            label.maxWidthProperty().bind(hBox.widthProperty().multiply(0.75));


            //Make the icons visible only when hovering on the specific board

            label.setUserData(boardCurr);

            if (idOfCurrentBoard != -1 && idOfCurrentBoard == boardCurr.id) {
                label.setStyle("-fx-font-size: 18px;");
            }

            //populate the interface with the clicked board
            label.setOnMouseClicked(e -> {
                for (Node child : boardsVBox.getChildren()) {
                    ((HBox) child).getChildren().get(0).setStyle("");
                }
                focusedBoard = (Board) label.getUserData();
                idOfCurrentBoard = focusedBoard.getId();
                label.setStyle("-fx-font-size: 18px;");
                refreshSpecificBoard(idOfCurrentBoard);
            });

            boardsVBox.getChildren().add(hBox);
            boardsVBox.setPadding(new Insets(5, 5, 5, 10));
            boardsVBox.setSpacing(10);
        }
    }

    /**
     * populates the dashboard with the lists of the specific board
     * @param id the id of the boards of which its lists will be used
     */
    public void refreshSpecificBoard(long id) {
        hboxList.setUserData(id);
        if (hboxList.getChildren().size() > 0) {
            hboxList.getChildren().subList(0, hboxList.getChildren().size()).clear();
        }

        ButtonTalio addListButton = new ButtonTalio("Create List", "Add List Name") {
            @Override
            public void processData(String data) {
                Board boardCurr = server.getBoard(id);
                List newList=new List(new ArrayList<Card>(), data,
                        boardCurr, boardCurr.lists.size() + 1);
                newList.setBoard(boardCurr);
                boardCurr.lists.add(newList);
                server.updateBoard(boardCurr);
            }

            @Override
            public void addLabel(Pane vboxEnd) {
                if(vboxEnd.getChildren().size()>1){
                    ObservableList<Node> children = vboxEnd.getChildren();
                    int numChildren = children.size();
                    children.remove(numChildren - 1);
                    children.remove(numChildren - 2);
                }
                Region spacer = new Region();
                spacer.setPrefHeight(10);
                vboxEnd.getChildren().add(spacer);
                vboxEnd.getChildren().add(textField);
            }

            @Override
            public void deleteLabel(Pane vboxEnd) {
                vboxEnd.getChildren().remove(textField);
                Region spacer = new Region();
                spacer.setPrefHeight(10);
                vboxEnd.getChildren().remove(spacer);
            }

            @Override
            public Pane addButton() {
                VBox vboxEnd = new VBox();
                vboxEnd.getChildren().add(this);
                hboxList.getChildren().add(vboxEnd);
                return vboxEnd;
            }
        };

//        Button addListButton = new Button("Create List");
//
//        VBox vboxEnd = new VBox();
//        vboxEnd.getChildren().add(addListButton);
//        hboxList.getChildren().add(vboxEnd);
//
//        addListButton.setOnAction(e -> {
//            createList(vboxEnd, id);
//        });


        if (server.getBoard(id).lists != null && server.getBoard(id).lists.size() > 0) {
            if (hboxList.getChildren().size() > 1) {
                hboxList.getChildren().subList(0, hboxList.getChildren().size()).clear();
            }

            addLists(server.getBoard(id).lists, id);
        } else if (hboxList.getChildren().size() > 1) {
            hboxList.getChildren().subList(0, hboxList.getChildren().size()).clear();
        }

        hboxList.setPadding(new Insets(30, 30, 30, 30));
        hboxList.setSpacing(30);
    }

    /**
     * deletes a board after confirming the deletion
     * @param board the board to be deleted
     */
    public void deleteBoard(Board board){
        //Show a confirmation message
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Delete board '" + board.getName() + "'?");
        alert.setContentText("Are you sure you want to delete board '" + board.getName() +
                "'?\nThis will permanently delete the board from the server.");

        ButtonType delete = new ButtonType("Delete");
        ButtonType cancel = new ButtonType("Cancel");
        alert.getButtonTypes().setAll(delete, cancel);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == delete) {
            //if the board to be deleted is selected remove its data from the interface
            if (hboxList.getUserData() != null && (long) hboxList.getUserData() == board.getId()) {
                hboxList.setUserData(null);
                hboxList.getChildren().clear();
            }
            //remove board from connectedBoards
            connectedBoards.remove(board);
            //delete board
            connectedBoards.remove(board);
            serverBoards.get(server.getServer()).remove(board);
            server.deleteBoard(board.getId());
        }
    }

    /**
     * edits the name of the board
     * @param label the new name of the board
     */
    public void editBoard(Label label){
        //create a new stage
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(label.getScene().getWindow());
        stage.setTitle("Edit Board");

        //create a vbox to add the fields in
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: rgb(214,202,221)");

        //create the fields of the scene
        Board boardCurr = (Board) label.getUserData();
        TextField textField = new TextField(boardCurr.getName());
        textField.setStyle("-fx-background-color: rgb(230,230,250)");
        Label error = new Label("");

        //create the buttons
        Button ok = new Button("OK");
        Button cancel = new Button("Cancel");
        HBox buttons = new HBox(ok, cancel);
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(10);

        //set colour for the buttons and create hovering effect
        ok.setStyle("-fx-background-color: rgb(230,230,250)");
        cancel.setStyle("-fx-background-color: rgb(230,230,250)");
        ok.setOnMouseEntered(e -> {
            ok.setStyle("-fx-background-color: rgb(178,132,190)");
        });
        ok.setOnMouseExited(e ->{
            ok.setStyle("-fx-background-color: rgb(230,230,250)");
        });
        cancel.setOnMouseEntered(e -> {
            cancel.setStyle("-fx-background-color: rgb(178,132,190)");
        });
        cancel.setOnMouseExited(e ->{
            cancel.setStyle("-fx-background-color: rgb(230,230,250)");
        });

        //add listeners
        ok.setOnAction(event -> {
            if (textField.getText().strip().length() > 0) {
                boardCurr.setName(textField.getText());
                server.updateBoard(boardCurr);
                stage.close();
            } else {
                error.setText("The name of the board can not be empty");
                error.setStyle("-fx-text-fill: red");
            }
        });

        cancel.setOnAction(e -> {
            stage.close();
        });

        //add all the fields in the vbox and show the scene
        vbox.getChildren().addAll(new Label("Enter new name for board '" +
                boardCurr.getName() + "':"), textField, error, buttons);

        Scene scene = new Scene(vbox, 377, 233);
        stage.setScene(scene);
        stage.showAndWait();
    }

    /**
     * creates a new board
     */
    public void createBoard(){
        addBoardLabel.setVisible(true);
        addBoardLabel.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                addBoardLabel.setText("");
            } else {
                if (addBoardLabel.getText().strip().length() != 0) {
                    String newText = addBoardLabel.getText();

                    Board boardCurr = new Board(newText);
                    boardCurr = server.addBoard(boardCurr);
                    boardCurr.lists = new ArrayList<>();
//                    boardCurr = server.getBoard(boardCurr.id);
//                    System.out.println(boardCurr);
                    connectedBoards.add(boardCurr);
                    serverBoards.get(server.getServer()).add(boardCurr);
                    addBoardLabel.setText("");
                    addBoardLabel.setVisible(false);
                    refreshBoards(connectedBoards);
                }
            }
        });

        /**
         * this method handles the event in which Add Board button is pressed
         */
        addBoardLabel.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                Scene scene = addBoardLabel.getScene();
                scene.getRoot().requestFocus(); // take the focus away from the textlabel
                addBoardLabel.setText("");
                addBoardLabel.setVisible(false);
            }
        });
    }

    /**
     * responsible for adding the lists in the UI along with its buttons
     * @param list the list to be added
     * @param boardId the id of the board of which its lists are used
     */
    private void addLists(java.util.List<List> list, long boardId){
        Board boardCurr=server.getBoard(boardId);
        for(List listCurr : list){
            VBox vBox = new VBox();
            Label label = new Label(listCurr.name);

            label.setUserData(listCurr.getID()); // set the list id as the label's UserData

            HBox hboxButtons = new HBox();
            Button delete = new Button("Delete List");
            Button edit = new Button("Edit List");

            //edit list using double-click
            label.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2) {
                    editList(vBox, label, boardId);
                }
            });

            //edit list using edit button
            edit.setOnAction(e -> {
                editList(vBox, label, boardId);
            });

            // Add Card Button
            label.setFont(Font.font(20));
            Button addTaskButton = new Button("Add Task");

            addTaskButton.setOnAction(e -> {
                mainCtrl.switchTaskCreation(listCurr, boardId);
            });

            ListView<Card> listView = new ListView<>();

            listView.setOnDragOver(event -> {
                event.acceptTransferModes(TransferMode.MOVE);
            });

            // if the drag ended on a tableview I add a new card to it
            listView.setOnDragDropped(event -> {
                if (draggedCard != null) {
                    done = true; // the dragged ended succesfully
                    var sourceListView = draggedCard.getListView();
                    var sourceItems = sourceListView.getItems();
                    int sourceIndex = draggedCard.getIndex();

                    listCurr.cards.add(cardDragged); // update with the card dropped

                    for (int i = 0; i < boardCurr.lists.size(); i++) {
                        if (boardCurr.lists.get(i).getID() == listCurr.getID()) {
                            boardCurr.lists.set(i, listCurr);
                        }
                    }

                    server.updateBoard(boardCurr);
                }

                event.setDropCompleted(true);
                event.consume();
            });
            // Call the method that sets the cell factory review.
            setFactory(listView, boardId);

            // Delete List Button
            delete.setOnAction(e -> {
                deleteList((Long) label.getUserData());
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

    private void deleteList(Long listId) {

        List list = server.getList(listId);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Delete List '" + list.getName() + "'?");
        alert.setContentText("Are you sure you want to delete list '" + list.getName() +
                "'?\nThis will permanently delete the list from the server.");

        ButtonType delete = new ButtonType("Delete");
        ButtonType cancel = new ButtonType("Cancel");
        alert.getButtonTypes().setAll(delete, cancel);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == delete) {
            server.deleteList(listId);
        }
    }

    private void editList(VBox vBox, Label label, Long boardId) {
        TextField textField = new TextField(label.getText());

        int labelIndex = vBox.getChildren().indexOf(label);
        vBox.getChildren().remove(labelIndex);
        vBox.getChildren().add(labelIndex, textField);

        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                String txt = textField.getText();
                List newList = server.getListById((Long) label.getUserData());
                Board boardCurr = server.getBoard(boardId);
                newList.setName(txt);
                newList.setBoard(boardCurr);

                for (int i = 0; i < boardCurr.lists.size(); i++) {
                    if (boardCurr.lists.get(i).getID() == newList.getID()) {
                        boardCurr.lists.set(i, newList);
                    }
                }

                server.updateBoard(boardCurr);//send the text to the database

            }
        });
    }

    private void setFactory(ListView list, long boardId) {
        Board boardCurr = server.getBoard(boardId);
        list.setCellFactory(q -> new ListCell<Card>() {
            @Override
            protected void updateItem(Card q, boolean empty) {
                super.updateItem(q, empty);
                if (empty) {
                    setText("");
                } else {
                    setText(q.name);
                    setOnMouseClicked(event -> {
                        if (event.getClickCount() == 2) {
                            mainCtrl.switchTaskView(q, server.getBoard(boardId));
                        }
                    });
                    //if the card has a description add the description icon
                    if (q.description != null && q.description.strip().length() != 0) {

                        Image imgDescription = new Image("pictures/description_icon.png");
                        ImageView imageDescription = new ImageView(imgDescription);
                        imageDescription.setFitWidth(20);
                        imageDescription.setFitHeight(20);

                        StackPane stackPane = new StackPane(imageDescription);
                        StackPane.setAlignment(imageDescription, Pos.TOP_RIGHT);

                        setGraphic(stackPane);
                    }

                    if(q.subtasks.size() >= 1){
                        int total = q.subtasks.size();
                        int done = 0;
                        for(Subtask subtask : q.getSubtasks()){
                            if(subtask.isCompleted() == 1) done++;
                        }
                        setText(this.getText() + " done: " + done + "/" + total);
                    }
                }
                // if we detect the drag we delete the card from the list and set the done variable
                setOnDragDetected(event -> {
                    if (getItem() == null || isEmpty()) {
                        return;
                    }

                    draggedCard = this;
                    cardDragged = getItem(); // store the Card object in a local variable
                    List listCurr = cardDragged.getList();
                    listCurr.cards.remove(cardDragged); // remove the card from the list

                    for (int i = 0; i < boardCurr.lists.size(); i++) {
                        if (boardCurr.lists.get(i).getID() == listCurr.getID()) {
                            boardCurr.lists.set(i, listCurr);
                        }
                    }

                    server.updateBoard(boardCurr);
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

                // if there is a drag over we set the black border and
                // find if it targets the upper cell or lower
                setOnDragOver(event -> {
                    double mouseX = event.getSceneX();
                    double mouseY = event.getSceneY();

                    double listViewY = this.localToScene(0, 0).getY();
                    if (mouseY - listViewY >= 50) {
                        sus = false;
                        this.setStyle("-fx-border-color: transparent transparent black " +
                                "transparent; -fx-border-width: 0 0 4 0;");
                    } else {
                        sus = true;
                        this.setStyle("-fx-border-color: black transparent transparent " +
                                "transparent;-fx-border-width: 4 0 0 0;");
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
                        List listCurr = this.getItem().getList();
                        listCurr.cards.add(dropIndex, cardDragged);
                        cardDragged.setList(this.getItem().getList());

                        for (int i = 0; i < boardCurr.lists.size(); i++) {
                            if (boardCurr.lists.get(i).getID() == listCurr.getID()) {
                                boardCurr.lists.set(i, listCurr);
                            }
                        }

                        server.updateBoard(boardCurr);
                    }
                    event.setDropCompleted(true);
                    event.consume();
                });

                setOnDragDone(event -> {
                    // if the drag ended neither on a cell nor on a table view we restore the card
                    if(!done) {
                        List listCurr= cardDragged.getList();
                        listCurr.cards.add(cardDragged.getNumberInTheList() - 1, cardDragged);

                        for (int i = 0; i < boardCurr.lists.size(); i++) {
                            if (boardCurr.lists.get(i).getID() == listCurr.getID()) {
                                boardCurr.lists.set(i, listCurr);
                            }
                        }

                        server.updateBoard(boardCurr);
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

    /**
     * creates a new list
     * @param vboxEnd the vbox the list is in
     * @param boardId the id of the board the list will be in
     */
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
            } else {
                if (textField.getText().strip().length() != 0) {
                    String newText = textField.getText();

                    Board boardCurr = server.getBoard(boardId);
                    List newList=new List(new ArrayList<Card>(), newText,
                            boardCurr, boardCurr.lists.size() + 1);
                    newList.setBoard(boardCurr);
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

    /**
     * adds a new card to a list
     * @param list the list the card will be added
     * @param vBox the vbox the list is in
     * @param listView a listview to add the cards in
     */
    public void addCards(List list, VBox vBox, ListView listView){// Set the card in our lists
        java.util.List<Card> cardlist = list.cards;
        listView.setItems(FXCollections.observableList(cardlist));
        int index = 0;
        if (hboxList.getChildren().size() > 0) {
            index = hboxList.getChildren().size() - 1;
        }
        hboxList.getChildren().add(index, vBox);

        // Make the card have a specified height and width
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        double screenHeight = bounds.getHeight();
        double screenWidth = bounds.getWidth();
        VBox.setMargin(vBox, new Insets(10, 10, 10, 10));
        vBox.setMaxWidth(250);
        // Set a default height based on the number of items
        // (assuming each item is 24 pixels high)
        listView.setPrefHeight(Math.min(screenHeight - screenHeight/4,
                listView.getItems().size() * 100));
    }

    /**
     * disconnects from the server
     */
    @FXML
    public void serverDisconnect() {
        idOfCurrentBoard = -1;
        hboxList.setUserData(null);
        mainCtrl.getPrimaryStage().close();
        main.start(new Stage());
    }

    /**
     * used for copying the key of a board
     */
    @FXML
    public void openShare() {
        final ContextMenu contextMenu = new ContextMenu();
        MenuItem copy = new MenuItem("Copy board code");

        copy.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (focusedBoard != null) {
                    Clipboard clipboard = Clipboard.getSystemClipboard();
                    ClipboardContent content = new ClipboardContent();
                    content.putString(focusedBoard.getKey());
                    clipboard.setContent(content);
                }
            }
        });

        contextMenu.getItems().addAll(copy);
        contextMenu.setAutoHide(true);
        contextMenu.setHideOnEscape(true);


        shareBoard.focusedProperty().addListener(((observable, oldValue, newValue) -> {
            if(!newValue) {
                contextMenu.hide();
            }
        }));

        shareBoard.setOnMouseClicked(event -> {
            Point2D absoluteCoordinates = shareBoard.
                    localToScreen(shareBoard.getLayoutX(), shareBoard.getLayoutY());
            if(event.getButton() == MouseButton.PRIMARY) {
                contextMenu.show(pane, absoluteCoordinates.getX(),
                        absoluteCoordinates.getY() + shareBoard.getHeight());
            }
        });

        shareBoard.setContextMenu(contextMenu);
    }

    public ContextMenu createThreeDotContextMenu(Label label, Button button) {
        final ContextMenu contextMenu = new ContextMenu();
        MenuItem edit = new MenuItem("Rename board");
        MenuItem remove = new MenuItem(("Leave board"));
        MenuItem delete = new MenuItem("Delete board");


////            hBox.getChildren().get(3).setVisible(false);
//        hBox.getChildren().get(2).setVisible(false);
//        hBox.setOnMouseEntered(e ->{
//            hBox.getChildren().get(3).setVisible(true);
//            hBox.getChildren().get(2).setVisible(true);
//        });
//        hBox.setOnMouseExited(e ->{
//            hBox.getChildren().get(3).setVisible(false);
//            hBox.getChildren().get(2).setVisible(false);
//        });


//        //Make it noticable when hovering on delete icon
//        deleteBoard.setOnMouseEntered(e ->{
//            backroundDelete.setFill(Color.rgb(255,99,71));
//        });
//        deleteBoard.setOnMouseExited(e ->{
//            backroundDelete.setFill(Color.TRANSPARENT);
//        });

//        //Make it noticable when hovering on edit icon
//        editBoard.setOnMouseEntered(e ->{
//            backroundEdit.setFill(Color.YELLOW);
//        });
//        editBoard.setOnMouseExited(e ->{
//            backroundEdit.setFill(Color.TRANSPARENT);
//        });


//        delete.setOnMouseClicked(e ->{
//            deleteBoard((Board) label.getUserData());
//        });
//
//        editBoard.setOnMouseClicked(e ->{
//            editBoard(label);
//        });

        edit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                editBoard(label);
            }
        });

        remove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Board board = (Board) label.getUserData();

                //if the board to be removed is selected remove its data from the interface
                if (hboxList.getUserData() != null && (long) hboxList.getUserData() == board.getId()) {
                    hboxList.setUserData(null);
                    hboxList.getChildren().clear();
                }

                idOfCurrentBoard = -1;

                connectedBoards.remove(board);
                serverBoards.get(server.getServer()).remove(board);
                refreshBoards(connectedBoards);
            }
        });

        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                deleteBoard((Board) label.getUserData());
            }
        });

        contextMenu.getItems().addAll(edit, remove, delete);
        contextMenu.setAutoHide(true);
        contextMenu.setHideOnEscape(true);

        int[] clickCount = {0};
        label.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue)
                contextMenu.hide();
        });

        final int[] nrClicks = {0};
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Point2D absoluteCoordinates = button.localToScreen(button.getLayoutX(), button.getLayoutY());
                if (event.getButton() == MouseButton.PRIMARY) {
                    if (nrClicks[0] == 1) {
                        contextMenu.hide();
                        nrClicks[0] = 0;
                    } else {
                        contextMenu.show(pane, absoluteCoordinates.getX(), absoluteCoordinates.getY() + button.getHeight());
                        nrClicks[0]++;
                    }
                }
            }
        });


//                button.setOnMouseClicked(event -> {
//                    Point2D absoluteCoordinates = button.localToScreen(button.getLayoutX(), button.getLayoutY());
//                    if (event.getButton() == MouseButton.PRIMARY) {
//                        if (clickCount[0] == 1) {
//                            contextMenu.hide();
//                            clickCount[0] = 0;
//                        } else {
//                            contextMenu.show(pane, absoluteCoordinates.getX(), absoluteCoordinates.getY() + button.getHeight());
//                            clickCount[0]++;
//                        }
//                    }
//                });

        return contextMenu;
    }

    @FXML
    public void openAddBoard() {
        Label description = new Label("Key of the board:");
        Label errorMessage = new Label();
        errorMessage.setTextFill(Color.RED);
        TextField input = new TextField();

        Button submit = new Button("Add board");
        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String key = input.getText();
                Board retrievedBoard;

                if (input.getText().isEmpty()) {
                    errorMessage.setText("The key that you have entered is empty");
                } else {
                    retrievedBoard = server.getBoardByKey(key);
                    if (retrievedBoard != null) {
                        boolean boardAdded = false;
                        for (Board board : connectedBoards) {
                            if (board.id == retrievedBoard.id) {
                                errorMessage.setText("Board is already added");
                                boardAdded = true;
                            }
                        }
                        if (!boardAdded) {
                            errorMessage.setText("");
                            connectedBoards.add(retrievedBoard);
                            serverBoards.get(server.getServer()).add(retrievedBoard);
                            refreshBoards(connectedBoards);
                            ContextMenu contextMenu = addBoardButton.getContextMenu();
                            contextMenu.setY(contextMenu.getY() + 24);
                        }
                    } else {
                        errorMessage.setText("Such a board doesn't exist");
                    }

                }
            }
        });

        VBox container = new VBox();
        container.getChildren().addAll(description, errorMessage, input, submit);
        CustomMenuItem popUpMenu = new CustomMenuItem(container);
        popUpMenu.setHideOnClick(false);

        final ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(popUpMenu);
        contextMenu.setAutoHide(true);
        contextMenu.setHideOnEscape(true);

        addBoardButton.focusedProperty().addListener(((observable, oldValue, newValue) -> {
            if(!newValue) {
                contextMenu.hide();
            }
        }));

        addBoardButton.setOnMouseClicked(event -> {
            Point2D absoluteCoordinates = addBoardButton.
                    localToScreen(addBoardButton.getLayoutX(), addBoardButton.getLayoutY());
            if(event.getButton() == MouseButton.PRIMARY) {
                contextMenu.show(pane, absoluteCoordinates.getX(),
                        absoluteCoordinates.getY() + addBoardButton.getHeight());
            }

        });

        addBoardButton.setContextMenu(contextMenu);
    }
}

