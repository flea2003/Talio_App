package client.scenes;

import client.Main;
import client.scenes.services.ButtonTalio;
import client.scenes.services.taskEdits;
import client.scenes.services.taskViews;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.List;
import commons.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
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
    Map<String, java.util.List<Board>> serverBoards;

    Map<String, java.util.List<Board>> previousStateServerBoards;
    @FXML
    private Button addBoard;
    @FXML
    private TextField addBoardLabel;
    @FXML
    private Button viewTags;
    public boolean adminAccess;

    @FXML
    private Text boardsHeader;
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

        if (serverBoards == null) {
            serverBoards = new HashMap<>();
        }
        if (serverBoards.get(currentServer) == null) {
            java.util.List<Board> boards = new ArrayList<>();
            serverBoards.put(currentServer, boards);
        }
        addBoardLabel.setVisible(false);

        //make a local map with all of the boards
        //save the state of serverBoards somewhere
        //set server board to this map
        //in disconnect if you are an admin restore serverBoards state
        if(adminAccess){
            Map<String, java.util.List<Board>> adminBoards = new HashMap();
            adminBoards.put(server.getServer(), server.getBoards());

            //swap
            previousStateServerBoards = serverBoards;
            serverBoards = adminBoards;
        }

        openShare();
        openAddBoard();
        addBoard.setOnAction(e -> {
            createBoard();
        });

        setBoards();
        refreshBoards(serverBoards.get(currentServer));
        server.refreshBoards("/topic/updates", Boolean.class, l -> {
            // this method refreshes. The platform.runLater() because of thread issues.
            Platform.runLater(() -> {
                try{
                    setBoards();
                    refreshBoards(serverBoards.get(currentServer));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
    }

    /**
     *  * checks that the changes on each board have been updated and
     *      * if not, it updates them
     *      no parameters
     */
    public void setBoards(){
        String currentServer = server.getServer();
        java.util.List<Board> serBoards = server.getBoards();
        //Get the list of boards for the current server
        java.util.List<Board> localBoards = serverBoards.get(currentServer);

//        if(adminAccess){
//            boardsHeader.setText("All Boards");
//            localBoards = serBoards;
//        }

        //Create a set of board IDs for the server boards
        Set<Long> serverIds = new HashSet<>();
        for (Board serBoard : serBoards) {
            serverIds.add(serBoard.getId());
        }

        // Use an iterator to update and remove boards
        Iterator<Board> iterator = localBoards.iterator();
        while (iterator.hasNext()) {
            Board board = iterator.next();
            if (serverIds.contains(board.getId())) {
                for (Board serBoard : serBoards) {
                    if (board.getId() == serBoard.getId()) {
                        board.setName(serBoard.getName());
                        break;
                    }
                }
            } else {
                if(hboxList.getUserData() != null && (Long)hboxList.getUserData() == board.getId()){
                    hboxList.setUserData(null);
                }
                iterator.remove();
            }
        }
    }

    /**
     * refreshes the dashboard with the new boards
     * @param boards the boards to populate the dashboard
     */
    public void refreshBoards(java.util.List<Board> boards){
        //if an update happens to an already selected board, it updates it
        if(hboxList.getUserData() != null){
            refreshSpecificBoard((Long) hboxList.getUserData());
        }

        //if no board is selected, clear the list interface
        // (used for when another client deletes a board)
        if(hboxList.getUserData() == null){
            if (hboxList.getChildren().size() > 0) {
                hboxList.getChildren().subList(0, hboxList.getChildren().size()).clear();
            }
            shareBoard.setVisible(false);
            viewTags.setVisible(false);
        }

        if (boardsVBox.getChildren().size() > 0) {
            boardsVBox.getChildren().subList(0, boardsVBox.getChildren().size()).clear();
        }

        for (Board boardCurr : boards) {
            Label label = new Label(boardCurr.name);

            //create delete icon
            Image dots = new Image("pictures/dots.png");
            ImageView dotsView = new ImageView(dots);
            dotsView.setFitWidth(16);
            dotsView.setFitHeight(16);

            Button dotsMenu = new Button();
            dotsMenu.setGraphic(dotsView);
            dotsMenu.setContextMenu(createThreeDotContextMenu(label, dotsMenu));

//            Node dotMenu = new Group(backroundDelete, imageDelete);

            HBox hBox = new HBox(label, new Region(), dotsMenu);
            hBox.setMaxWidth(120);
            hBox.setPrefWidth(120); //set the preferred width to the max
                                    // width so the updates are noy noticeable

            hBox.setStyle("fx-background-color: #e5e3f1; -fx-background-radius: 10px;");
            hBox.setAlignment(Pos.CENTER);
            HBox.setHgrow(hBox.getChildren().get(1), Priority.ALWAYS); //Set spacer to fill
                                                                        // available space

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
        pane.getStylesheets().add("CSS/button.css");
        openTags(id);
        shareBoard.setVisible(true);
        viewTags.setVisible(true);
        hboxList.setUserData(id);
        if (hboxList.getChildren().size() > 0) {
            hboxList.getChildren().subList(0, hboxList.getChildren().size()).clear();
        }
        // Created a new Interface, where we provide the methods of where
        // to put the button, where the label should appear, and
        ButtonTalio addListButton = new ButtonTalio("Create List", "Add List Name") {
            /**
             * processing the new information
             * @param data the string that should be sent to the database
             */
            @Override
            public void processData(String data) {
                Board boardCurr = server.getBoard(id);
                List newList=new List(new ArrayList<Card>(), data,
                        boardCurr, boardCurr.lists.size() + 1);
                newList.setBoard(boardCurr);
                boardCurr.lists.add(newList);
                server.updateBoard(boardCurr);
            }

            /**
             * add the label to the VBox
             * @param vboxEnd the structure where the node is added
             */
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

            /**
             * delete the label from the VBox
             * @param vboxEnd pane where the label is right now
             */
            @Override
            public void deleteLabel(Pane vboxEnd) {
                vboxEnd.getChildren().remove(textField);
                Region spacer = new Region();
                spacer.setPrefHeight(10);
                vboxEnd.getChildren().remove(spacer);
            }

            /**
             * add Button to the dashboard
             * @return Pane
             */
            @Override
            public Pane addButton() {
                VBox vboxEnd = new VBox();
                vboxEnd.getChildren().add(this);
                hboxList.getChildren().add(vboxEnd);
                return vboxEnd;
            }
        };

        addListButton.getStyleClass().add("connectButton");
        addListButton.setStyle("-fx-text-fill: white");

        var lists = server.getBoard(id).lists;
        taskViews.getInstance().checkClosed(lists);
        taskEdits.getInstance().checkClosed(lists);

        if ( lists != null && lists.size() > 0) {
            if (hboxList.getChildren().size() > 1) {
                hboxList.getChildren().subList(0, hboxList.getChildren().size()).clear();
            }

            addLists(lists, id);
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
        //create a new stage
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Delete board '" + board.getName() + "'?");

        //create a vbox to add the fields in
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: #a29cf4");
        vbox.getStylesheets().add("CSS/button.css");

        //create the buttons
        Button delete = new Button("Delete");
        Button cancel = new Button("Cancel");
        HBox buttons = new HBox(delete, cancel);
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(10);

        //set colour for the buttons and create hovering effect
        delete.getStyleClass().add("connectButton");
        delete.setStyle("-fx-text-fill: rgb(250,240,230)");
        cancel.getStyleClass().add("connectButton");
        cancel.setStyle("-fx-text-fill: rgb(250,240,230)");

        delete.setOnAction( e -> {
            //if the board to be deleted is selected remove its data from the interface
            if (hboxList.getUserData() != null && (long) hboxList.getUserData() == board.getId()) {
                shareBoard.setVisible(false);
                viewTags.setVisible(false);
                hboxList.setUserData(null);
                hboxList.getChildren().clear();
            }
            //remove board from serverBoards
            serverBoards.get(server.getServer()).remove(board);
            //delete board
            server.deleteBoard(board.getId());
            stage.close();
        });

        cancel.setOnAction(e -> {
            stage.close();
        });

        ImageView deletion = new ImageView(new Image("/pictures/deletion.png"));
        deletion.maxHeight(30);
        deletion.maxWidth(30);

        VBox message = new VBox();
        Label sure = new Label("Are you sure you want to delete board '" + board.getName() + "'?");
        sure.setStyle("-fx-font-size: 16px");
        message.getChildren().addAll(sure,
                new Label("This will permanently delete the board from the server.")
        );
        message.setAlignment(Pos.CENTER);
        message.setSpacing(10);

        //add all the fields in the vbox and show the scene
        vbox.getChildren().addAll(deletion, message, buttons);

        Scene scene = new Scene(vbox, 395, 250);
        stage.setScene(scene);
        stage.showAndWait();

    }

    /**
     * edits the name of the board
     * @param label the new name of the board
     */
    public void editBoard(Label label){
        Board boardCurr = (Board) label.getUserData();

        //create a new stage
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(label.getScene().getWindow());
        stage.setTitle("Edit Board '" + boardCurr.getName() + "'");

        //create a vbox to add the fields in
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: #a29cf4");
        vbox.getStylesheets().add("CSS/button.css");

        //create the fields of the scene
        TextField textField = new TextField(boardCurr.getName());
        textField.setStyle("-fx-background-color: rgb(204,204,255)");
        Label error = new Label("");

        //create the buttons
        Button ok = new Button("OK");
        Button cancel = new Button("Cancel");
        HBox buttons = new HBox(ok, cancel);
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(10);

        //set colour for the buttons and create hovering effect
        ok.getStyleClass().add("connectButton");
        ok.setStyle("-fx-text-fill: rgb(250,240,230)");
        cancel.getStyleClass().add("connectButton");
        cancel.setStyle("-fx-text-fill: rgb(250,240,230)");

        //add listeners
        ok.setOnAction(event -> {
            if (textField.getText().strip().length() > 0) {
                boardCurr.setName(textField.getText());
                server.updateBoard(boardCurr);
                stage.close();
            } else {
                error.setText("The name of the board can not be empty");
                error.setStyle("-fx-text-fill: rgb(178,34,34)");
            }
        });

        cancel.setOnAction(e -> {
            stage.close();
        });
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
                    boardCurr.tags = new ArrayList<>();

                    serverBoards.get(server.getServer()).add(boardCurr);
                    addBoardLabel.setText("");
                    addBoardLabel.setVisible(false);
                    refreshBoards(serverBoards.get(server.getServer()));
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
            vBox.getStylesheets().add("/CSS/button.css");
            Label label = new Label(listCurr.name);
            VBox addTask = new VBox();
            addTask.setVisible(false);

            label.setUserData(listCurr.getID()); // set the list id as the label's UserData

            HBox hboxButtons = new HBox();
            Button delete = new Button("Delete List");
            delete.getStyleClass().add("connectButton");
            delete.setStyle("-fx-text-fill: rgb(250,240,230)");
            Button edit=new Button("Edit List");
            edit.setStyle("-fx-text-fill: rgb(250,240,230)");
            edit.getStyleClass().add("connectButton");

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
            addTaskButton.getStyleClass().add("connectButton");
            addTaskButton.setStyle("-fx-text-fill: rgb(250,240,230)");

            addTaskButton.setOnMouseClicked(e -> {
                if( e.getClickCount() == 2){
                    mainCtrl.switchTaskCreation(listCurr, boardId);
                } else {
                    createTask(listCurr, addTask, boardId);
                }
            });

            ListView<Card> listView = new ListView<>();

            listView.setOnDragOver(event -> {
                event.acceptTransferModes(TransferMode.MOVE);
            });

            // if the drag ended on a tableview I add a new card to it
            listView.setOnDragDropped(event -> {
                event.acceptTransferModes(TransferMode.MOVE);
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
            vBox.getChildren().add(addTask);
            HBox buttons = new HBox(addTaskButton, delete, edit);
            buttons.setSpacing(5);
            hboxButtons.getChildren().add(buttons);

            addCards(listCurr, vBox, listView);
        }
    }

    /**
     * method that deletes lists
     * @param listId
     */
    private void deleteList(Long listId) {
        List list = server.getList(listId);

        //create a new stage
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Delete List '" + list.getName() + "'?");

        //create a vbox to add the fields in
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: #a29cf4");
        vbox.getStylesheets().add("CSS/button.css");

        //create the buttons
        Button delete = new Button("Delete");
        Button cancel = new Button("Cancel");
        HBox buttons = new HBox(delete, cancel);
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(10);

        //set colour for the buttons and create hovering effect
        delete.getStyleClass().add("connectButton");
        delete.setStyle("-fx-text-fill: rgb(250,240,230)");
        cancel.getStyleClass().add("connectButton");
        cancel.setStyle("-fx-text-fill: rgb(250,240,230)");

        delete.setOnAction( e -> {
            server.deleteList(listId);
            stage.close();
        });

        cancel.setOnAction(e -> {
            stage.close();
        });

        ImageView deletion = new ImageView(new Image("/pictures/deletion.png"));
        deletion.maxHeight(30);
        deletion.maxWidth(30);

        VBox message = new VBox();
        Label sure = new Label("Are you sure you want to delete list '" + list.getName() + "'?");
        sure.setStyle("-fx-font-size: 16px");
        message.getChildren().addAll(sure,
                new Label("This will permanently delete the list from the server.")
        );
        message.setAlignment(Pos.CENTER);
        message.setSpacing(10);

        //add all the fields in the vbox and show the scene
        vbox.getChildren().addAll(deletion, message, buttons);

        Scene scene = new Scene(vbox, 395, 250);
        stage.setScene(scene);
        stage.showAndWait();
    }

    /**
     * method to edit the List name
     * @param vBox
     * @param label
     * @param boardId
     */
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

                if (txt.strip().length() == 0) {
                    textField.setText(label.getText());
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Empty list name");
                    alert.setContentText("Can not have a list without a name." +
                            " Please enter a valid name.");
                    alert.showAndWait();
                } else {
                    newList.setName(txt);
                    newList.setBoard(boardCurr);

                    for (int i = 0; i < boardCurr.lists.size(); i++) {
                        if (boardCurr.lists.get(i).getID() == newList.getID()) {
                            boardCurr.lists.set(i, newList);
                        }
                    }

                    server.updateBoard(boardCurr);//send the text to the database
                }
            }
        });
    }

    int[] curr = new int[1];

    /**
     * This method describes the manner in which all the
     * cards will be created and arranged into lists
     * This method also contains the drag and drop functionality
     * @param list the list in which we store the cards
     * @param boardId - the id of the board where the list is located
     */
    private void setFactory(ListView list, long boardId) {
        Board boardCurr = server.getBoard(boardId);
        list.setCellFactory(q -> new ListCell<Card>() {
            @Override
            protected void updateItem(Card q, boolean empty) {
                super.updateItem(q, empty);
                if (empty) {
                    setText("");
                } else {
                    HBox content = new HBox();
                    //setText(q.name);
                    setOnMouseClicked(event -> {
                        if (event.getClickCount() == 2) {
                            if(server.getCardById(q.id) == null){
                                // because of this + drag and drop we got
                                // an error before
                            }
                            else{
                                mainCtrl.switchTaskView(q, server.getBoard(boardId));
                            }
                        }
                    });
                    //if the card has a description add the description icon
                    HBox icons = new HBox();
                    if (q.description != null && q.description.strip().length() != 0) {

                        Image imgDescription = new Image("pictures/description_icon.png");
                        ImageView imageDescription = new ImageView(imgDescription);
                        imageDescription.setFitWidth(20);
                        imageDescription.setFitHeight(20);

                        StackPane stackPane = new StackPane(imageDescription);

                        content.getChildren().add(stackPane);
                        stackPane.setAlignment(Pos.CENTER_LEFT);
                    }
                    else{
                        StackPane stackPane = new StackPane();
                        stackPane.setAlignment(Pos.CENTER_LEFT);
                        stackPane.setMaxWidth(0);
                        content.getChildren().add(stackPane);
                    }
                    Text display = null;
                    display = new Text(q.name);
                    if(q.name.length() >= 20){
                        display = new Text(q.name.substring(0, 20) + "...");
                    }
                    else{
                        display = new Text(q.name);
                    }
                    display.setStyle("-fx-font-style: Italic;");
                    display.setStyle("-fx-font-family: Calibri Light;");
                    display.setStyle("-fx-font-size: 14px;");
                    content.getChildren().add(display);

                    Image imgDone = new Image("pictures/done_icon.png");
                    ImageView imageDone = new ImageView(imgDone);
                    imageDone.setFitWidth(20);
                    imageDone.setFitHeight(20);
                    int total = 0, done = 0;
                    if(q.subtasks != null) {
                        total = q.subtasks.size();
                        done = 0;
                        for (Subtask subtask : q.getSubtasks()) {
                            if (subtask.isCompleted() == 1){
                                done++;
                            }
                        }
                    }
                    HBox hBox = new HBox();
                    hBox.getChildren().add(imageDone);
                    hBox.getChildren().add(new Text(done + "/" + total));

                    content.getChildren().add(hBox);
                    hBox.setAlignment(Pos.BOTTOM_RIGHT);
                    HBox.setHgrow(hBox, Priority.ALWAYS);
                    if(q.subtasks.size() == 0){
                        hBox.setVisible(false);
                    }

                    content.setAlignment(Pos.CENTER);
                    content.setSpacing(Region.USE_COMPUTED_SIZE); // set spacing to computed size
                    setGraphic(content);
                }
                // if we detect the drag we delete the card from the list and set the done variable
                setOnDragDetected(event -> {
                    if (getItem() == null || isEmpty()) {
                        return;
                    }

                    draggedCard = this;
                    cardDragged = getItem(); // store the Card object in a local variable
                    if(cardDragged == null){
                        return;
                    }
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
                        this.setStyle("-fx-border-color: transparent" +
                                " transparent #e5e3f1 transparent; -fx-border-width: 0 0 4 0;");
                    } else {
                        sus = true;
                        this.setStyle("-fx-border-color: #e5e3f1 transparent" +
                                " transparent transparent; -fx-border-width: 4 0 0 0;");
                    }

                    double y = event.getY() + this.localToScene(0, 0).getY();
                    listViewY = this.getListView().localToScene(0, 0).getY();
                    if(y - listViewY <= 100){
                        if(curr[0] % 16 == 0) {
                            this.getListView().scrollTo(this.getIndex() - 1);
                        }
                        curr[0]++;
                    }
                    else if(listViewY + this.getListView().getHeight() - y <= 100){
                        if(curr[0] % 16 == 0){
                            this.getListView().scrollTo(this.getIndex() - 5);
                        }
                        curr[0]++;
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
                    if(server.getCardById(q.id) == null){
                        return;
                    }
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
        ObservableList<Card> observableList = FXCollections.observableList(list.getCards());
        listView.setItems(observableList);
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
        mainCtrl.closeStages();
        if(adminAccess){
            serverBoards = previousStateServerBoards;
            adminAccess = false;
        }
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
            Point2D absoluteCoordinates = shareBoard.localToScreen(shareBoard.getLayoutX(),
                                                                   shareBoard.getLayoutY());
            if(event.getButton() == MouseButton.PRIMARY) {
                contextMenu.show(pane, absoluteCoordinates.getX() - 75,
                                       absoluteCoordinates.getY() + shareBoard.getHeight() + 10);
            }
        });
        shareBoard.setContextMenu(contextMenu);
    }

    /**
     * This method creates the ThreeDotContextMenu, which appears near
     * each Board into My Boards overview, where we can choose to either
     * detele, leave or rename a board
     * @param label label
     * @param button button
     * @return the context menu itself
     */
    public ContextMenu createThreeDotContextMenu(Label label, Button button) {
        final ContextMenu contextMenu = new ContextMenu();
        MenuItem edit = new MenuItem("Rename board");
        MenuItem remove = new MenuItem(("Leave board"));
        MenuItem delete = new MenuItem("Delete board");


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
                if (hboxList.getUserData() != null
                        && (long) hboxList.getUserData() == board.getId()) {
                    hboxList.setUserData(null);
                    shareBoard.setVisible(false);
                    viewTags.setVisible(false);
                    hboxList.getChildren().clear();
                }

                idOfCurrentBoard = -1;

                serverBoards.get(server.getServer()).remove(board);
                refreshBoards( serverBoards.get(server.getServer()));
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
            if (!newValue) {
                contextMenu.hide();
            }
        });

        button.setOnMouseClicked(event -> {
            Point2D absoluteCoordinates = button.localToScreen(button.getLayoutX(),
                                                               button.getLayoutY());
            if (event.getButton() == MouseButton.PRIMARY) {
                contextMenu.show(pane, absoluteCoordinates.getX() - 21,
                        absoluteCoordinates.getY() + button.getHeight() - 33);
            }
        });

        return contextMenu;
    }

    /**
     * This method creates the pop-out screen where we can
     * introduce the key of a board in order to connect to it
     * In case the key doesn't work, we get an error back
     */
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
                        for (Board board : serverBoards.get(server.getServer())) {
                            if (board.id == retrievedBoard.id) {
                                errorMessage.setText("Board is already added");
                                boardAdded = true;
                            }
                        }
                        if (!boardAdded) {
                            errorMessage.setText("");
                            serverBoards.get(server.getServer()).add(retrievedBoard);
                            refreshBoards(serverBoards.get(server.getServer()));
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
                    localToScreen(boardsVBox.getBoundsInLocal().getMinX() + 150,
                            addBoardLabel.getBoundsInLocal().getMaxY() - 68);
            if(event.getButton() == MouseButton.PRIMARY) {
                contextMenu.show(pane, absoluteCoordinates.getX(),
                        absoluteCoordinates.getY() + addBoardButton.getHeight());
            }

        });

        addBoardButton.setContextMenu(contextMenu);
    }

    /**
     * Methods adds a label where we can enter the name of a
     * new task we would like to create
     * Sets an error if the name is empty
     * @param list list
     * @param vboxEnd vbox
     * @param boardId id of the board
     */
    public void createTask(List list, VBox vboxEnd, long boardId){
        vboxEnd.setVisible(true);

        if(vboxEnd.getChildren().size()>1){
            ObservableList<Node> children = vboxEnd.getChildren();
            int numChildren = children.size();
            children.remove(numChildren - 1);
            children.remove(numChildren - 2);
        }
        TextField textField = new TextField("Enter Task Name");
        Region spacer = new Region();
        spacer.setPrefHeight(10);
        vboxEnd.getChildren().add(spacer);
        vboxEnd.getChildren().add(textField);

        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                textField.setText("");
            }else{
                if (textField.getText().strip().length() == 0) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Can not create a task with no name");
                    alert.showAndWait();
                    vboxEnd.setVisible(false);
                } else {
                    String taskName = textField.getText();
                    Board boardCurr=server.getBoard(boardId);

                    Card card = new Card();
                    card.setName(taskName);
                    list.cards.add(card);

                    for(int i=0; i<boardCurr.lists.size(); i++){
                        if(boardCurr.lists.get(i).getID()==list.getID()){
                            boardCurr.lists.set(i,list);
                        }
                    }

                    server.updateBoard(boardCurr);

                }
            }
        });

        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                Scene scene = textField.getScene();
                scene.getRoot().requestFocus(); // take the focus away from the textlabel
                textField.setText("");
                textField.setVisible(false);
            }
        });

    }

    /**
     * opens a tag scene
     * @param boardId the tag's board
     */
    public void openTags(long boardId) {
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setStyle("-fx-background-color: #a29cf4");

        for(Tag tag: server.getBoard(boardId).tags){
            Circle circle = new Circle(10);
            circle.setStrokeType(StrokeType.OUTSIDE);
            circle.setStroke(Color.BLACK);
            circle.setStrokeWidth(1);
            circle.setFill(Color.rgb(tag.getRed(), tag.getGreen(), tag.getBlue()));

            HBox hBox = new HBox(circle, new Label(tag.getName()));
            CustomMenuItem customMenuItem = new CustomMenuItem(hBox);

            contextMenu.getItems().add(customMenuItem);

            customMenuItem.setOnAction( e -> {
                openTag(tag, boardId);
            });
        }

        Image add = new Image("pictures/plus_icon.png");
        ImageView addView = new ImageView(add);
        addView.setFitWidth(16);
        addView.setFitHeight(16);

        Button addButton = new Button();
        addButton.setGraphic(addView);
        CustomMenuItem addMenuItem = new CustomMenuItem(addButton);

        contextMenu.getItems().add(addMenuItem);
        contextMenu.setAutoHide(true);
        contextMenu.setHideOnEscape(true);

        viewTags.setOnMouseClicked(event -> {
            Point2D absoluteCoordinates = viewTags.localToScreen(viewTags.getLayoutX(),
                    viewTags.getLayoutY());
            if (event.getButton() == MouseButton.PRIMARY) {
                contextMenu.show(pane, absoluteCoordinates.getX() - 75,
                        absoluteCoordinates.getY() + shareBoard.getHeight() + 10);
            }
        });

        addMenuItem.setOnAction( e-> {
            addTag(boardId);
        });
        viewTags.setContextMenu(contextMenu);
    }

    private void openTag(Tag tag, long boardId) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Tag '" + tag.getName() + "'");

        //create a vbox to add the fields in
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: #a29cf4");
        vbox.getStylesheets().add("CSS/button.css");

        //create the fields of the scene
        Text title = new Text(tag.getName());
        title.setStyle("-fx-font-size: 30 px");
        Circle circle = new Circle(20);
        circle.setFill(Color.rgb(tag.getRed(), tag.getGreen(), tag.getBlue()));
        Label error = new Label("");

        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setValue(Color.rgb(tag.getRed(), tag.getGreen(), tag.getBlue()));
        colorPicker.setOnAction(e -> {
            circle.setFill(colorPicker.getValue());
        });

        Image edit = new Image("pictures/edit_icon.png");
        ImageView editView = new ImageView(edit);
        editView.setFitWidth(16);
        editView.setFitHeight(16);

        Button editButton = new Button();
        editButton.setGraphic(editView);

        HBox titleBox = new HBox(circle, title,editButton);
        titleBox.setSpacing(10);
        titleBox.setAlignment(Pos.CENTER);
        //create the buttons
        Button delete = new Button("Delete");
        Button ok = new Button("Save Changes");
        Button cancel = new Button("Cancel");
        HBox buttons = new HBox(delete, ok, cancel);
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(10);

        //set colour for the buttons and create hovering effect
        ok.getStyleClass().add("connectButton");
        ok.setStyle("-fx-text-fill: rgb(250,240,230)");
        cancel.getStyleClass().add("connectButton");
        cancel.setStyle("-fx-text-fill: rgb(250,240,230)");
        delete.getStyleClass().add("connectButton");
        delete.setStyle("-fx-text-fill: rgb(250,240,230)");

        cancel.setOnAction(e -> {
            stage.close();
        });

        ok.setOnAction(e -> {
            tag.setGreen((int)(colorPicker.getValue().getGreen()*255));
            tag.setBlue((int)(colorPicker.getValue().getBlue()*255));
            tag.setRed((int)(colorPicker.getValue().getRed()*255));
            tag.setName(title.getText());
            Board board = server.getBoard(boardId);
            for(int i=0; i<board.getTags().size(); i++){
                if(board.getTags().get(i).getId() == tag.getId()){
                    board.getTags().set(i, tag);
                }
            }
            server.updateBoard(board);
            stage.close();
        });

        delete.setOnAction(e -> {
            deleteTag(tag, boardId, stage);
        });

        editButton.setOnAction( e -> {
            TextField textField = new TextField(title.getText());

            int labelIndex = titleBox.getChildren().indexOf(title);
            titleBox.getChildren().remove(labelIndex);
            titleBox.getChildren().add(labelIndex, textField);

            textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (oldValue) {
                    String txt = textField.getText();

                    if (txt.strip().length() == 0) {
                        textField.setText(title.getText());
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText("Empty tag name");
                        alert.setContentText("Can not have a tag without a name." +
                                " Please enter a valid name.");
                        alert.showAndWait();
                    } else {
                        title.setText(textField.getText());
                        titleBox.getChildren().remove(labelIndex);
                        titleBox.getChildren().add(labelIndex, title);
                    }
                }
            });
        });

        HBox.setHgrow(titleBox, Priority.ALWAYS);

        //add all the fields in the vbox and show the scene
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(titleBox, error,colorPicker,new Label(" "), buttons);

        Scene scene = new Scene(vbox, 411, 266);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private void deleteTag(Tag tag, long boardId, Stage tagStage) {
        //create a new stage
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Delete tag '" + tag.getName() + "'?");

        //create a vbox to add the fields in
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: #a29cf4");
        vbox.getStylesheets().add("CSS/button.css");

        //create the buttons
        Button delete = new Button("Delete");
        Button cancel = new Button("Cancel");
        HBox buttons = new HBox(delete, cancel);
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(10);

        //set colour for the buttons and create hovering effect
        delete.getStyleClass().add("connectButton");
        delete.setStyle("-fx-text-fill: rgb(250,240,230)");
        cancel.getStyleClass().add("connectButton");
        cancel.setStyle("-fx-text-fill: rgb(250,240,230)");

        cancel.setOnAction(e -> {
            stage.close();
        });

        delete.setOnAction(e -> {
            server.deleteTag(tag.getId());
            tagStage.close();
            stage.close();
        });

        ImageView deletion = new ImageView(new Image("/pictures/deletion.png"));
        deletion.maxHeight(30);
        deletion.maxWidth(30);

        VBox message = new VBox();
        Label sure = new Label("Are you sure you want to delete tag '" + tag.getName() + "'?");
        sure.setStyle("-fx-font-size: 16px");
        message.getChildren().addAll(sure,
                new Label("This will permanently delete the tag from the server.")
        );
        message.setAlignment(Pos.CENTER);
        message.setSpacing(10);

        //add all the fields in the vbox and show the scene
        vbox.getChildren().addAll(deletion, message, buttons);

        Scene scene = new Scene(vbox, 395, 250);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private void addTag(long boardId) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Add a tag");

        //create a vbox to add the fields in
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: #a29cf4");
        vbox.getStylesheets().add("CSS/button.css");

        //create the fields of the scene
        TextField name = new TextField();
        name.setStyle("-fx-background-color: rgb(204,204,255)");
        Label error = new Label("");

        ColorPicker colorPicker = new ColorPicker();

        //create the buttons
        Button ok = new Button("Create");
        Button cancel = new Button("Cancel");
        HBox buttons = new HBox(ok, cancel);
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(10);

        //set colour for the buttons and create hovering effect
        ok.getStyleClass().add("connectButton");
        ok.setStyle("-fx-text-fill: rgb(250,240,230)");
        cancel.getStyleClass().add("connectButton");
        cancel.setStyle("-fx-text-fill: rgb(250,240,230)");

        cancel.setOnAction(e -> {
            stage.close();
        });

        ok.setOnAction(e -> {
            if(name.getText().strip().length() == 0){
                error.setText("Tag name can not be empty");
                error.setStyle("-fx-text-fill: rgb(178,34,34)");
            } else {
                Board board = server.getBoard(boardId);
                Tag tag = new Tag(name.getText(), (int) (colorPicker.getValue().getGreen()*255),
                        (int) (colorPicker.getValue().getBlue()*255),
                        (int) (colorPicker.getValue().getRed()*255), board, new ArrayList<>());

                tag.setBoard(board);
                board.getTags().add(tag);
                server.addTag(tag);
                server.updateBoard(board);
                stage.close();
            }
        });

        //add all the fields in the vbox and show the scene
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(new Label("Tag name"), name,
                error,new Label("Color"), colorPicker, new Label(" "), buttons);

        Scene scene = new Scene(vbox, 377, 233);
        stage.setScene(scene);
        stage.showAndWait();
    }

}

