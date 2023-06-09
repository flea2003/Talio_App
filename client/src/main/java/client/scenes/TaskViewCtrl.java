package client.scenes;

import client.scenes.services.ButtonTalio;
import client.scenes.services.CardControllerState;
import client.scenes.services.taskViews;
import client.utils.ServerUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Board;
import commons.Card;
import commons.List;
import commons.Subtask;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.inject.Inject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class TaskViewCtrl extends Application implements CardControllerState {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private Card card;

    @FXML
    private Scene scene;

    @FXML
    private Text taskName;

    @FXML
    private Text taskDescription;

    private Card currCard;
    @FXML
    private Button editTask;

    @FXML
    private Button done;
    @FXML
    private Text taskNo;

    @FXML
    private List listCurr;

    @FXML
    private Text error;

    @FXML
    private Button deleteButton;

    private Board boardCurr;
    @FXML
    private VBox subTasks;
    @FXML
    private Button addSubtask;
    private boolean hasTextField = false;
    @FXML
    private Text description;
    @FXML
    HBox taskListHBox;
    @FXML
    TextFlow descriptionPane;

    @FXML
    private HBox hboxButtons;
    @FXML
    private BorderPane borderPane;
    @FXML
    private ScrollPane scrollPane;
    private Scene taskView;

    private Stage newStage;

    private taskViews viewTasks;

    private ExecutorService exec;

    @FXML
    private VBox actualSubtasks;
    /**
     * constructor
     * @param server the current server
     * @param mainCtrl a reference to the MainCtrl
     * @param card the card that is being viewed
     */
    @Inject
    public TaskViewCtrl(ServerUtils server, MainCtrl mainCtrl, Card card) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.card = card;
        this.currCard = card;
        this.viewTasks = viewTasks;
    }

    /**
     * A method which sends information to our scene controller
     * @param scene the scene that is bind to this controller
     * @param card the card that needs to be loaded in this scene
     * @param board the board that the cards belongs to
     */
    public void sendData(Scene scene, Card card, Board board){
        this.taskView = scene;
        this.currCard = card;
        this.boardCurr = board;
    }

    /**
     * Start bt making the Task View Window appear
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     */
    @Override
    public void start(javafx.stage.Stage primaryStage)  {
        if (primaryStage != null) {
            newStage = primaryStage;
        } else {
            newStage = new Stage();
        }
        newStage.setTitle("Task View");
        newStage.setScene(taskView);
        renderInfo(currCard);
        newStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                taskViews.getInstance().remove(TaskViewCtrl.this);
                newStage.hide();
                server.stopThread(exec);
            }
        });
        newStage.show();
        addEditFunctionality((Pane)taskName.getParent(), taskName, taskName, e -> {
            TextField textField = new TextField(taskName.getText());
            if(taskName.getParent() != null) {
                ((Pane) taskName.getParent()).getChildren()
                        .set(((Pane) taskName.getParent())
                                .getChildren().indexOf(taskName), textField);
                textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                    } else {
                        if (textField.getText().strip().length() != 0) {
                            String data = textField.getText();
                            currCard.name = data;
                            server.updateBoard(currCard.getList().getBoard());
                            if ((Pane) textField.getParent() != null) {
                                int indx = ((Pane) textField
                                        .getParent()).getChildren().indexOf(textField);
                                ((Pane) textField.getParent()).getChildren().set(indx, taskName);
                            }
                        } else {
                            if ((Pane) textField.getParent() != null) {
                                int indx = ((Pane) textField.getParent())
                                        .getChildren().indexOf(textField);
                                ((Pane) textField.getParent()).getChildren().set(indx, taskName);
                            }
                        }
                    }
                });

                textField.setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.ENTER) {
                        int indx = ((Pane) textField.getParent()).getChildren().indexOf(textField);
                        ((Pane) textField.getParent()).getChildren().set(indx, taskName);
                    }
                });

                textField.requestFocus();
            }

        });

        addEditFunctionality((Pane)description.getParent(), description, taskDescription, e -> {
            TextField textField = new TextField(taskDescription.getText());
            int index = descriptionPane.getParent().getChildrenUnmodifiable()
                    .indexOf(descriptionPane);
            VBox vBox = (VBox) descriptionPane.getParent();
            vBox.getChildren().set(index, textField);
            textField.requestFocus();
            textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                } else {
                    String data = textField.getText();
                    currCard.description = data;
                    server.updateBoard(currCard.getList().getBoard());
                    vBox.getChildren().set(index, descriptionPane);
                    taskDescription.setText(data);
                }
            });

            textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    String data = textField.getText();
                    vBox.getChildren().set(index, descriptionPane);
                    taskDescription.setText(data);
                }
            });
        });

        ButtonTalio addSubtask = new ButtonTalio("+", "Enter Subtask Name") {
            /**
             * processes the new information
             * @param data the string that should be sent to the database
             */
            @Override
            public void processData(String data) {
                Subtask subtask = new Subtask(data, "", currCard.subtasks.size() + 1, currCard, 0);

                subtask = server.saveSubtask(subtask);
                currCard.addSubtask(subtask);
                server.updateBoard(currCard.getList().board);
            }

            /**
             * adds the label
             * @param node the structure where the node is added
             */
            @Override
            public void addLabel(Pane node) {
                if(node.getChildren().size() == 0){
                    node.getChildren().add(this.textField);
                }
                else if(node.getChildren().get(node.getChildren().size() - 1) instanceof TextField){
                    return;
                }
                else{
                    node.getChildren().add(this.textField);
                }
                this.textField.setStyle("-fx-background-color: #e5e3f1;");
            }

            /**
             * deletes the label
             * @param node pane where the label is right now
             */
            @Override
            public void deleteLabel(Pane node) {
                node.getChildren().remove(this.textField);
            }

            /**
             * add Button
             * @return Pane
             */
            @Override
            public Pane addButton() {
                this.setPadding(new Insets(0, 0, 10, 0));
                if(!(taskListHBox.getChildren().size() >= 1 && taskListHBox.
                        getChildren().get(taskListHBox.getChildren()
                                .size() - 1) instanceof ButtonTalio)){
                    taskListHBox.getChildren().add(this);
                }
                return actualSubtasks;
            }
        };
        addSubtask.setStyle("-fx-background-color: transparent;");
        addSubtask.setOnMouseEntered(e -> {
            addSubtask.setStyle("-fx-background-color: #e0e0e0;");
        });
        addSubtask.setOnMouseExited(e -> {
            addSubtask.setStyle("-fx-background-color: transparent;");
        });

    }

    /**
     * show the information of the card in th UI
     * @param card the card to show
     */
    public void renderInfo(Card card){
        Board board = server.getBoard(currCard.getList().board.id);
        for(List list : board.getLists()){
            for(Card cardCurr : list.getCards()){
                if(cardCurr.id == card.id){
                    currCard = cardCurr;
                }
            }
        }
        exec = Executors.newSingleThreadExecutor();
        server.longPolling(exec, q -> {
            var exists = server.getCardById(q.id);
            if(exists == null){
                Platform.runLater(() -> {
                    try{
                        newStage.fireEvent(new javafx.stage.WindowEvent(newStage,
                                javafx.stage.WindowEvent.WINDOW_CLOSE_REQUEST));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }else{
                server.stopThread(exec);
                renderInfo(exists);
            }
        }, currCard);
        Platform.runLater(() -> {
            hboxButtons.getStylesheets().add("CSS/button.css");
            while(subTasks.getChildren().size() >= 2){
                subTasks.getChildren().remove(subTasks.getChildren().size() - 1);
            }
            actualSubtasks.getChildren().clear();

            taskName.setText(card.name);
            if(taskName.layoutBoundsProperty().get().getWidth() >= 400){
                taskName.setWrappingWidth(400);
            }

            taskDescription.setText(card.description);
            taskDescription.setWrappingWidth(400);

            if(taskNo==null){
                taskNo=new Text();
            }
            taskNo.setText("Task No. " + card.getNumberInTheList());
            for(Subtask subtask : currCard.getSubtasks()){
                createSubtask(subtask);
            }
            return;

        });
    }

    /**
     * switches the scene to the taskEdit
     */
    public void goEdit(){
        mainCtrl.switchEdit(currCard, boardCurr);
    }

    /**
     * throws a confirmation message for deleting the card and deletes the card
     */
    public void goDelete(){
        //create a new stage
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Delete Task '" + currCard.getName() + "'?");

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
            List listCurr=currCard.getList();
            listCurr.cards.remove(currCard);

            for(int i=0; i<boardCurr.lists.size(); i++){
                if(boardCurr.lists.get(i).getID()==listCurr.getID()){
                    boardCurr.lists.set(i,listCurr);
                }
            }

            server.updateBoard(boardCurr);
            server.deleteCard(currCard.id);
            newStage.close();
            taskViews.getInstance().remove(this);
            stage.close();
        });

        cancel.setOnAction(e -> {
            stage.close();
        });

        ImageView deletion = new ImageView(new Image("/pictures/deletion.png"));
        deletion.maxHeight(30);
        deletion.maxWidth(30);

        VBox message = new VBox();
        Label sure = new Label("Are you sure you want to delete task '" +
                currCard.getName() + "'?");
        sure.setStyle("-fx-font-size: 16px");
        message.getChildren().addAll(sure,
                new Label("This will permanently delete the task from the server.")
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
     * switches the scene to dashboard
     */
    @FXML
    public void setDone(){
        taskViews.getInstance().remove(TaskViewCtrl.this);
        server.stopThread(exec);
        newStage.close();
    }

    private String extractValue(Text curr){
        return curr.getText();
    }

    private void setError(String err){
        error.setText(err);
    }

    /**
     * sets the current list to a new one
     * @param listCurr the new list
     */
    public void setListCurr(List listCurr) {
        this.listCurr = listCurr;
    }

    /**
     * sets the current board to a new one
     * @param boardCurr the new board
     */
    public void setBoardCurr(Board boardCurr) {
        this.boardCurr = boardCurr;
    }

    /**
     * gets a card
     * @return the current card
     */
    @Override
    public Card getCard() {
        return currCard;
    }

    /**
     * adds a subtask
     */
    @FXML
    public void addSubtask(){
        if(hasTextField){
            return;
        }
        else{
            hasTextField = true;
            TextField textField = new TextField();
            subTasks.getChildren().add(textField);
            textField.requestFocus();
            textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    textField.setText("");
                }else{
                    if(textField.getText().strip().length()!=0) {
                        String newText = textField.getText();

                        Subtask subtask = new Subtask(newText, "", 1, card, 0);
                        card.addSubtask(subtask);
                        server.addCard(card);
                        hasTextField = false;
                        subTasks.getChildren().remove(textField);
                    }
                }
            });
            textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    String newText = textField.getText();
                    hasTextField = false;
                    subTasks.getChildren().remove(textField);
                }
            });
        }

    }

    /**
     * method to create a new Subtask within the Task
     * @param subtask the new subtask
     */
    public void createSubtask(Subtask subtask){
        HBox hbox = new HBox();
        CheckBox checkBox = new CheckBox(subtask.getName());
        checkBox.getStylesheets().add("CSS/button.css");
        checkBox.getStyleClass().add("checkBox-custom");
        checkBox.setStyle("-fx-font-size: 18px; -fx-font-style: italic;" +
                " -fx-font-family: 'Candara Light';");
        checkBox.setSelected(subtask.isCompleted() != 0);
        hbox.getChildren().add(checkBox);
        ImageView imageView = new ImageView(new Image("pictures/edit_icon.png"));
        imageView.setFitWidth(15);
        imageView.setFitHeight(15);
        StackPane container = new StackPane(imageView);
        container.setVisible(false);
        hbox.getChildren().add(container);
        StackPane finalContainer1 = container;
        imageView = new ImageView(new Image("pictures/down_arrow.png"));
        imageView.setFitWidth(15);
        imageView.setFitHeight(15);
        container = new StackPane(imageView);
        hbox.getChildren().add(container);
        container.setVisible(false);
        StackPane finalContainer2 = container;
        imageView = new ImageView(new Image("pictures/up_arrow.png"));
        imageView.setFitWidth(15);
        imageView.setFitHeight(15);
        container = new StackPane(imageView);
        hbox.getChildren().add(container);
        StackPane finalContainer3 = container;
        finalContainer3.setVisible(false);
        imageView = new ImageView(new Image("pictures/delete_icon.png"));
        imageView.setFitWidth(15);
        imageView.setFitHeight(15);
        container = new StackPane(imageView);
        hbox.getChildren().add(container);
        StackPane finalContainer4 = container;
        finalContainer4.setVisible(false);
        finalContainer1.setOnMouseEntered(e -> {
            finalContainer1.setStyle("-fx-background-color: #e0e0e0;");
        });
        finalContainer1.setOnMouseExited(e -> {
            finalContainer1.setStyle("-fx-background-color: transparent;");
        });
        finalContainer2.setOnMouseEntered(e -> {
            finalContainer2.setStyle("-fx-background-color: #e0e0e0;");
        });
        finalContainer2.setOnMouseExited(e -> {
            finalContainer2.setStyle("-fx-background-color: transparent;");
        });
        finalContainer3.setOnMouseEntered(e -> {
            finalContainer3.setStyle("-fx-background-color: #e0e0e0;");
        });
        finalContainer3.setOnMouseExited(e -> {
            finalContainer3.setStyle("-fx-background-color: transparent;");
        });
        finalContainer4.setOnMouseEntered(e -> {
            finalContainer4.setStyle("-fx-background-color: #e0e0e0;");
        });
        finalContainer4.setOnMouseExited(e -> {
            finalContainer4.setStyle("-fx-background-color: transparent;");
        });
        finalContainer4.setOnMouseClicked(e -> {
            deleteSubtask(subtask);
        });
        finalContainer1.setOnMouseClicked(e -> {
            editSubtask(subtask, checkBox);
        });
        finalContainer2.setOnMouseClicked(e -> {
            moveDownSubtask(subtask);
        });
        finalContainer3.setOnMouseClicked(e -> {
            moveUpSubtask(subtask);
        });
        hbox.setOnMouseEntered(event -> {
            finalContainer1.setVisible(true);
            finalContainer2.setVisible(true);
            finalContainer3.setVisible(true);
            finalContainer4.setVisible(true);
        });
        hbox.setOnMouseExited(event -> {
            finalContainer1.setVisible(false);
            finalContainer2.setVisible(false);
            finalContainer3.setVisible(false);
            finalContainer4.setVisible(false);
        });
        checkBox.setOnAction(e -> {
            subtask.switchState();
            server.updateBoard(currCard.getList().board);
        });
        actualSubtasks.getChildren().add(hbox);

    }

    /**
     * This method takes
     * @param pane Pane
     * @param control Node
     * @param consumer Consumer
     */
    void addEditFunctionality(Pane pane, Node afterWhat, Node control, Consumer consumer){
        Image edit = new Image("pictures/edit_icon.png");
        ImageView editView = new ImageView(edit);
        editView.setFitWidth(12);
        editView.setFitHeight(12);
        StackPane container = new StackPane(editView);
        editView.setPickOnBounds(true);
        container.setStyle("-fx-background-color: transparent;");
        container.setOnMouseEntered(e -> {
            container.setStyle("-fx-background-color: #e0e0e0;");
        });
        container.setOnMouseExited(e -> {
            container.setStyle("-fx-background-color: transparent;");
        });
        container.setPickOnBounds(true); // set pickOnBounds to true
        if(pane.getChildren().size() == pane.getChildren().indexOf(afterWhat) + 1){
            pane.getChildren().add(container);
        }
        else if(pane.getChildren().get(pane.getChildren().indexOf(afterWhat) + 1)
                instanceof StackPane){
            return;
        }
        else {
            pane.getChildren().add(pane.getChildren().indexOf(afterWhat) + 1, container);
        }
        editView.setOnMouseClicked(consumer::accept);
    }

    /**
     * method that deletes a subtask
     * @param subtask the subtasks the user wants to delete
     */
    private void deleteSubtask(Subtask subtask) {
        //create a new stage
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Delete Subtask '" + subtask.getName() + "'?");

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
            ObjectMapper objectMapper = new ObjectMapper();
            currCard.subtasks.remove(subtask);
            subtask.setCard(null);
            server.deleteSubtask(subtask);
            server.updateBoard(currCard.getList().board);
            stage.close();
        });

        ImageView deletion = new ImageView(new Image("/pictures/deletion.png"));
        deletion.maxHeight(30);
        deletion.maxWidth(30);

        VBox message = new VBox();
        Label sure = new Label("Are you sure you want to delete subtask '" +
                subtask.getName() + "'?");
        sure.setStyle("-fx-font-size: 16px");
        message.getChildren().addAll(sure,
                new Label("This will permanently delete the subtask from the server.")
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
     * move Subtask in a different Card
     * @param subtask used Subtask
     */
    private void moveDownSubtask(Subtask subtask){
        if(subtask.getNumberInTheCard() == subtask.getCard().subtasks.size()){
            return;
        }
        else{
            int indx = subtask.getNumberInTheCard();
            subtask.setNumberInTheCard(indx + 1);
            subtask.getCard().subtasks.get(indx)
                    .setNumberInTheCard(indx);
            server.updateBoard(subtask.getCard().getList().board);
        }
    }
    /**
     * move Subtask in a different Card
     * @param subtask used Subtask
     */
    private void moveUpSubtask(Subtask subtask){
        if(subtask.getNumberInTheCard() == 1){
            return;
        }
        else{
            int indx = subtask.getNumberInTheCard();
            subtask.setNumberInTheCard(indx - 1);
            subtask.getCard().subtasks.get(indx - 2)
                    .setNumberInTheCard(indx);
            server.updateBoard(subtask.getCard().getList().board);
        }
    }

    /**
     * edits a subtasks and updates it
     * @param subtask current Subtask to be updated
     * @param checkBox ckeckbox
     */
    private void editSubtask(Subtask subtask, CheckBox checkBox){
        TextField textField = new TextField(subtask.getName());

        int labelIndex = -1;
        actualSubtasks.getChildren().indexOf(checkBox);
        for(int i=0;i<actualSubtasks.getChildren().size();i++){
            HBox hBox = (HBox) (actualSubtasks.getChildren().get(i));
            if(hBox.getChildren().get(0).equals(checkBox)){
                labelIndex = i;
            }
        }
        Node node = actualSubtasks.getChildren().remove(labelIndex);
        actualSubtasks.getChildren().add(labelIndex, textField);
        textField.requestFocus();
        int finalLabelIndex = labelIndex;
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                String txt = textField.getText();
                if(txt.length() > 0) {
                    subtask.setName(txt);
                    //send the text to the database
                    server.updateBoard(subtask.getCard().getList().board);
                }
                actualSubtasks.getChildren().set(finalLabelIndex, node);
            }
        });
        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                actualSubtasks.getChildren().set(finalLabelIndex, node);
            }
        });
    }

    /**
     * get the Stage
     * @return a stage
     */
    public Stage getStage() {
        return newStage;
    }
}

