/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;

import client.scenes.services.taskCreations;
import client.scenes.services.taskEdits;
import client.scenes.services.taskViews;
import commons.Board;
import commons.Card;
import commons.List;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Pair;

import java.awt.*;
import java.util.Stack;

import static client.Main.FXML;

public class MainCtrl {


    private Stage primaryStage;
    private Scene overview;
    private Scene add;
    private TaskViewCtrl taskViewCtrl;
    private TaskCreationCtrl taskCreationCtrl;
    private DashboardCtrl dashboardCtrl;
    private CreateBoardCtrl boardCtrl;
    private Scene board;
    private Scene dashboard;
    private Scene taskView;
    private Scene taskCreation;
    private Scene taskEdit;
    private TaskEditCtrl taskEditCtrl;


    /**
     * initializes the application with the provided parameters
     *
     * @param primaryStage
     * @param dashboard    the pair containing the DashboardCtrl and Parent
     *                     objects for the dashboard scene
     * @param board        the pair containing the CreateBoardCtrl and Parent
     *                     objects for the create board scene
     * @param taskCreation the pair containing the TaskCreationCtrl and Parent
     *                     objects for the task creation scene
     * @param taskView     the pair containing the TaskViewCtrl and Parent
     *                     objects for the task view scene
     * @param taskEdit     the pair containing the TaskEditCtrl and Parent
     *                     objects for the task edit scene
     */
    public void initialize(Stage primaryStage, Pair<DashboardCtrl, Parent> dashboard,
                           Pair<CreateBoardCtrl, Parent> board,
                           Pair<TaskCreationCtrl, Parent>taskCreation,
                           Pair<TaskViewCtrl, Parent>taskView,
                           Pair<TaskEditCtrl, Parent>taskEdit) {

        this.primaryStage = primaryStage;

        this.dashboardCtrl = dashboard.getKey();
        this.dashboard = new Scene(dashboard.getValue());

        this.board = new Scene(board.getValue());
        this.boardCtrl = board.getKey();

        this.taskViewCtrl = taskView.getKey();
        this.taskView = new Scene(taskView.getValue());

        this.taskEditCtrl = taskEdit.getKey();
        this.taskEdit = new Scene(taskEdit.getValue());

        this.taskCreation = new Scene(taskCreation.getValue());
        this.taskCreationCtrl = taskCreation.getKey();

        switchDashboard("");
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {

            }
        });
    }

    /**
     * gets the primary stage
     * @return the primary stage
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * sets the scene to taskCreation
     * @param listCurr th list where the task will be added
     * @param boardId the id of the board the list is in
     */
    public void switchTaskCreation(List listCurr, long boardId){
        Stage stage = new Stage();
        taskCreationCtrl.sendData(stage, taskCreation, boardId, listCurr);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(primaryStage.getScene().getWindow());
        stage.setTitle("Create a Task");
        stage.setScene(taskCreation);
        stage.showAndWait();
    }

    /**
     * switches the scene to the dashboard
     * @param user user
     */
    public void switchDashboard(String user){
        primaryStage.setTitle("Dashboard");
        primaryStage.setScene(dashboard);
    }

    /**
     * switches the scene to the create a board
     */
    public void switchCreateBoard() {
        primaryStage.setTitle("Create a Board");
        primaryStage.setScene(board);
        board.setOnKeyPressed(e -> boardCtrl.keyPressed(e));
    }

    /**
     * sets the scene to taskView
     * @param q the card to be viewed
     * @param boardCurr the board the card is in
     */
    public void switchTaskView(Card q, Board boardCurr){
        if(taskViews.getInstance().isOpened(q))
            return;
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("client/src/main/resources/client/scenes/TaskView.fxml"));
//        Parent root = null;
//        try {
//            root = loader.load();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        TaskViewCtrl controller = loader.getController();
//
//        controller.sendData(new Scene(root), q, boardCurr);
//        controller.start(null);
//        taskViews.getInstance().add(controller);

        var taskView = FXML.load(TaskViewCtrl.class, "client", "scenes", "TaskView.fxml");
        taskView.getKey().sendData(new Scene(taskView.getValue()), q, boardCurr);
        taskView.getKey().start(null);
        taskViews.getInstance().add(taskView.getKey());
    }

    /**
     * sets the scene to taskEdit
     * @param q the card to be edited
     * @param boardCurr the board the card is in
     */
    public void switchEdit(Card q, Board boardCurr){
        TaskViewCtrl controller = taskViews.getInstance().getCotroller(q);
        Stage viewStage = controller.getStage();
        if(viewStage == null)
            return;
        taskViews.getInstance().remove(controller);
        var taskEdit = FXML.load(TaskEditCtrl.class, "client", "scenes", "TaskEdit.fxml");
        taskEdit.getKey().sendData(new Scene(taskEdit.getValue()), q, boardCurr);
        taskEdit.getKey().start(viewStage);
        taskEdits.getInstance().add(taskEdit.getKey());
    }

    /**
     * sets the scene to dashboard with deleted as the user
     * @param currCard the card that was deleted
     */
    public void switchDelete(Card currCard) {
        switchDashboard("deleted!");
    }

    public void reallySwitchTaskView(Card q, Board boardCurr, Stage stage) {
        if(taskViews.getInstance().isOpened(q))
        return;

        var taskView = FXML.load(TaskViewCtrl.class, "client", "scenes", "TaskView.fxml");
        taskView.getKey().sendData(new Scene(taskView.getValue()), q, boardCurr);
        taskView.getKey().start(stage);
        taskViews.getInstance().add(taskView.getKey());
    }

    public void closeStages() {
        taskCreations.getInstance().closeAll();
        taskViews.getInstance().closeAll();
        taskEdits.getInstance().closeAll();
    }
}