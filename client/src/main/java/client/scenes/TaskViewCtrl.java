package client.scenes;

import client.services.ButtonTalio;
import client.utils.ServerUtils;
import commons.Board;
import commons.Card;
import commons.List;
import commons.Subtask;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Consumer;

public class TaskViewCtrl {

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
    }

    /**
     * show the information of the card in th UI
     * @param card the card to show
     */
    public void renderInfo(Card card){
        while(subTasks.getChildren().size() >= 2){
            subTasks.getChildren().remove(subTasks.getChildren().size() - 1);
        }
        currCard = card;
        taskName.setText(card.name);

        addEditFunctionality((Pane)taskName.getParent(), taskName, taskName, e -> {
            TextField textField = new TextField(taskName.getText());
            ((Pane) taskName.getParent()).getChildren().set(((Pane) taskName.getParent()).getChildren().indexOf(taskName), textField);
            textField.setStyle(taskName.getStyle());
            textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                } else {
                    if (textField.getText().strip().length() != 0) {
                        String data = textField.getText();
                        currCard.name = data;
                        server.updateBoard(currCard.getList().getBoard());
                        ((Pane) textField.getParent()).getChildren().set(((Pane) textField.getParent()).getChildren().indexOf(textField), taskName);
                    }
                }
            });

            textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    ((Pane) textField.getParent()).getChildren().set(((Pane) textField.getParent()).getChildren().indexOf(textField), taskName);
                }
            });
        });

        taskDescription.setText(card.description);
        taskDescription.setWrappingWidth(400);
        addEditFunctionality((Pane)description.getParent(), description, taskDescription, e -> {
            TextField textField = new TextField(taskDescription.getText());
            textField.setPrefWidth(400);
            textField.setMinWidth(400);
            textField.setMaxWidth(400);
            int index = descriptionPane.getParent().getChildrenUnmodifiable().indexOf(descriptionPane);
            VBox vBox = (VBox) descriptionPane.getParent();
            vBox.getChildren().set(index, textField);
            textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                } else {
                    if (textField.getText().strip().length() != 0) {
                        String data = textField.getText();
                        currCard.description = data;
                        server.updateBoard(currCard.getList().getBoard());
                        vBox.getChildren().set(index, descriptionPane);
                        taskDescription.setText(data);
                    }
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

        if(taskNo==null){
            taskNo=new Text();
        }
        taskNo.setText("Task No. " + card.getNumberInTheList());
        for(Subtask subtask : currCard.getSubtasks()){
            System.out.println(subtask);
            createSubtask(subtask);
        }
        ButtonTalio addSubtask = new ButtonTalio("+", "Enter Subtask Name") {
            @Override
            public void processData(String data) {
                //System.out.println(data);
                currCard.addSubtask(new Subtask(data, "", subTasks.getChildren().size(), card, 0));
                //System.out.println(currCard);
                server.updateBoard(currCard.getList().board);
            }

            @Override
            public void addLabel(Pane node) {
                if(node.getChildren().get(node.getChildren().size() - 1) instanceof TextField){
                    return;
                }
                else{
                    node.getChildren().add(this.textField);
                }
            }

            @Override
            public void deleteLabel(Pane node) {
                node.getChildren().remove(this.textField);
            }

            @Override
            public Pane addButton() {
                if(!(taskListHBox.getChildren().size() >= 1 && taskListHBox.getChildren().get(taskListHBox.getChildren().size() - 1) instanceof ButtonTalio)){
                    taskListHBox.getChildren().add(this);
                }
                return subTasks;
            }
        };
        addSubtask.setStyle("-fx-background-color: transparent;");
        addSubtask.setOnMouseEntered(e -> {
            addSubtask.setStyle("-fx-background-color: #e0e0e0;");
        });
        addSubtask.setOnMouseExited(e -> {
            addSubtask.setStyle("-fx-background-color: transparent;");
        });
        return;
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Delete Task '" + currCard.getName() + "'?");
        alert.setContentText("Are you sure you want to delete task '" + currCard.getName() +
                "'?\nThis will permanently delete the task from the server.");

        ButtonType delete = new ButtonType("Delete");
        ButtonType cancel = new ButtonType("Cancel");
        alert.getButtonTypes().setAll(delete, cancel);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == delete) {
            List listCurr=currCard.getList();
            listCurr.cards.remove(currCard);

            for(int i=0; i<boardCurr.lists.size(); i++){
                if(boardCurr.lists.get(i).getID()==listCurr.getID()){
                    boardCurr.lists.set(i,listCurr);
                }
            }

            server.updateBoard(boardCurr);
            server.deleteCard(currCard.id);
            mainCtrl.switchDelete(currCard);
        }

    }

    /**
     * switches the scene to dashboard
     */
    @FXML
    public void setDone(){
        mainCtrl.switchDashboard("LOL");
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

    @FXML
    public void addSubtask(){
        if(hasTextField){
           return;
        }
        else{
            hasTextField = true;
            TextField textField = new TextField();
            subTasks.getChildren().add(textField);
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

    public void createSubtask(Subtask subtask){
        HBox hbox = new HBox();
        CheckBox checkBox = new CheckBox(subtask.getName());
        checkBox.setSelected(subtask.isCompleted() != 0);
        hbox.getChildren().add(checkBox);
        ImageView imageView = new ImageView(new Image("pictures/edit_icon.png"));
        imageView.setFitWidth(15);
        imageView.setFitHeight(15);
        StackPane container = new StackPane(imageView);
        hbox.getChildren().add(container);
        imageView = new ImageView(new Image("pictures/down_arrow.png"));
        imageView.setFitWidth(15);
        imageView.setFitHeight(15);
        container = new StackPane(imageView);
        hbox.getChildren().add(container);
        imageView = new ImageView(new Image("pictures/up_arrow.png"));
        imageView.setFitWidth(15);
        imageView.setFitHeight(15);
        container = new StackPane(imageView);
        hbox.getChildren().add(container);
        imageView = new ImageView(new Image("pictures/delete_icon.png"));
        imageView.setFitWidth(15);
        imageView.setFitHeight(15);
        container = new StackPane(imageView);
        hbox.getChildren().add(container);
        checkBox.setOnAction(e -> {
            System.out.println(subtask.isCompleted());
            subtask.switchState();
            System.out.println(subtask.isCompleted());

            server.updateBoard(currCard.getList().board);
        });
        subTasks.getChildren().add(hbox);

    }

    /**
     * This method takes
     * @param pane
     * @param control
     * @param consumer
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
        else if(pane.getChildren().get(pane.getChildren().indexOf(afterWhat) + 1) instanceof StackPane){
            return;
        }
        else {
            pane.getChildren().add(pane.getChildren().indexOf(afterWhat) + 1, container);
        }
        editView.setOnMouseClicked(consumer::accept);
    }
}
