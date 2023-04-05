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

import commons.Board;
import commons.Card;
import commons.List;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {

    private Stage primaryStage;;
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
        primaryStage.setTitle("Task Creation");
        primaryStage.setScene(taskCreation);
        taskCreationCtrl.setListCurr(listCurr);
        taskCreationCtrl.setBoardId(boardId);
        taskCreation.setOnKeyPressed(e -> taskCreationCtrl.keyPressed(e));
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
        primaryStage.setTitle("View Task");
//        taskView.getRoot().toFront();
        primaryStage.setScene(taskView);
//        taskView.set

//        StackPane root = new StackPane();
//        root.getChildren().addAll(dashboard.getRoot(), taskView.getRoot());
//        taskView.getRoot().setScaleX(0.8);
//        taskView.getRoot().setScaleY(0.8);
//
//// Align the dashboard node to the center of the stack pane
//        StackPane.setAlignment(dashboard.getRoot(), Pos.CENTER);
//        dashboard.getRoot().setAlignment(Pos.CENTER);
//
//        Scene overlayScene = new Scene(root, primaryStage.getScene().getRoot().getLayoutBounds().getWidth(), primaryStage.getScene().getRoot().getLayoutBounds().getHeight());
//        primaryStage.setScene(overlayScene);

        taskViewCtrl.setBoardCurr(boardCurr);
        taskViewCtrl.renderInfo(q);
    }

    /**
     * sets the scene to taskEdit
     * @param q the card to be edited
     * @param boardCurr the board the card is in
     */
    public void switchEdit(Card q, Board boardCurr){
        primaryStage.setTitle("Edit Task");
        primaryStage.setScene(taskEdit);
        taskEditCtrl.setBoardCurr(boardCurr);
        taskEditCtrl.renderInfo(q);
    }

    /**
     * sets the scene to dashboard with deleted as the user
     * @param currCard the card that was deleted
     */
    public void switchDelete(Card currCard) {
        switchDashboard("deleted!");
    }

}