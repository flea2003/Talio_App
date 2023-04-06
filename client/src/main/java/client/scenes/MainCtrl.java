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
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.awt.*;
import java.util.Stack;

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
        primaryStage.setScene(taskView);
//        taskView.setFill(Color.TRANSPARENT);
//        StackPane stackPane = new StackPane();
//        stackPane.getChildren().addAll(primaryStage.getScene().getRoot(), taskView.getRoot());
//        primaryStage.getScene().setRoot(stackPane);
//        primaryStage.setScene(primaryStage.getScene());
//        Stage popup = new Stage();
//        popup.initModality(Modality.NONE);
//        popup.initOwner(primaryStage.getOwner());
//        popup.setScene(taskView);
//        popup.setX(primaryStage.getX());
//        popup.setY(primaryStage.getY());
//        popup.show();
//        BorderPane borderPane = new BorderPane();
//        // set the current scene as the center of the BorderPane
//        borderPane.setCenter(primaryStage.getScene().getRoot());
//        // set the taskView as the top of the BorderPane
//        borderPane.setTop(taskView.getRoot());
//        ((Region)taskView.getRoot()).setMaxHeight(primaryStage.getHeight() * 0.8);
//
//        // set the scene to the stack pane and show it
//        // create a new scene with the BorderPane as the root
//        Scene newScene = new Scene(borderPane);
//        // set the new scene as the scene for the primaryStage
//        primaryStage.setScene(newScene);
//        // center the taskView in the BorderPane
//        BorderPane.setAlignment(taskView.getRoot(), Pos.CENTER);
//        CustomMenuItem popUpMenu = new CustomMenuItem(taskView.getRoot());
//        popUpMenu.setHideOnClick(false);
//
//        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
//        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
//        popUpMenu.setMaxSize(screenWidth * 0.8, screenHeight * 0.8);
//
//        // create a StackPane as the parent container and add the CustomMenuItem to it
//        StackPane stackPane = new StackPane();
//        stackPane.getChildren().add(popUpMenu);
//
//        // set the alignment of the StackPane to center
//        StackPane.setAlignment(popUpMenu, Pos.CENTER);
//        CustomMenuItem popUpMenu = new CustomMenuItem();
//        popUpMenu.setContent(taskView.getRoot());

//        // set the maximum size of the root node of the taskView scene to be 80% of the screen size
//        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
//        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
//        ((Region)taskView.getRoot()).setMaxSize(screenWidth * 0.8, screenHeight * 0.8);
//
//        // create a StackPane as the parent container and add the CustomMenuItem to it
//        StackPane stackPane = new StackPane();
//        stackPane.getChildren().add(popUpMenu);
//
//        // set the alignment of the StackPane to center
//        StackPane.setAlignment(popUpMenu, Pos.CENTER);
//
//        // add a mouse event handler to the CustomMenuItem to close it when clicked
//        popUpMenu.getContent().setOnMouseClicked(event -> {
//            primaryStage.setScene(previousScene);
//        });
//
//        // create a new scene with the StackPane as the root
//        Scene newScene = new Scene(stackPane, Color.TRANSPARENT);

//        CustomMenuItem popUpMenu = new CustomMenuItem(taskView.getRoot());
//
//        // Create an AnchorPane that overlays the previous scene
//        AnchorPane anchorPane = new AnchorPane();
//        AnchorPane.setTopAnchor(popUpMenu, 0.0);
//        AnchorPane.setRightAnchor(popUpMenu, 0.0);
//        AnchorPane.setBottomAnchor(popUpMenu, 0.0);
//        AnchorPane.setLeftAnchor(popUpMenu, 0.0);
//        anchorPane.getChildren().add(popUpMenu);
//
//        // Add a mouse event handler to the CustomMenuItem to close it when clicked
//        popUpMenu.getContent().setOnMouseClicked(event -> {
//            anchorPane.getChildren().remove(popUpMenu);
//        });
//
//        // Add the AnchorPane to the root of the previous scene
//        ((Pane) previousScene.getRoot()).getChildren().add(anchorPane);
//
//        // set the new scene as the scene for the primaryStage
//        primaryStage.setScene(newScene);
//
//        Stage dialogStage = new Stage();
//        dialogStage.initModality(Modality.APPLICATION_MODAL);
//        dialogStage.initOwner(primaryStage.getScene().getWindow());
//
//        // Create a Label to display the message in the dialog
//        Label messageLabel = new Label(message);
//        messageLabel.setWrapText(true);
//        messageLabel.setMaxWidth(250);
//
//        // Create a Button to close the dialog
//        Button closeButton = new Button("Close");
//        closeButton.setOnAction(event -> {
//            dialogStage.close();
//        });
//
//        // Create a VBox to hold the Label and Button
//        VBox vbox = new VBox(messageLabel, closeButton);
//        vbox.setAlignment(Pos.CENTER);
//        vbox.setSpacing(10);
//        vbox.setPadding(new Insets(10));
//
//        // Set the VBox as the content of the Scene
//        Scene dialogScene = new Scene(vbox);
//
//        // Set the size of the Scene
//        dialogScene.setMinWidth(300);
//        dialogScene.setMinHeight(200);
//
//        // Set the position of the Stage in the center of the parent Scene
//        dialogStage.setX(parentScene.getWindow().getX() + (parentScene.getWidth() / 2) - (dialogScene.getMinWidth() / 2));
//        dialogStage.setY(parentScene.getWindow().getY() + (parentScene.getHeight() / 2) - (dialogScene.getMinHeight() / 2));
//
//        // Set the Scene of the Stage and show the dialog
//        dialogStage.setScene(dialogScene);
//        dialogStage.show();

        // Create a new Stage for the dialog
//        Stage dialogStage = new Stage();
//        dialogStage.initModality(Modality.APPLICATION_MODAL);
//        dialogStage.initOwner(primaryStage.getScene().getWindow());
//
//        // Create a Label to display the message in the dialog
//        Label messageLabel = new Label("heh");
////        messageLabel.setWrapText(true);
////        messageLabel.setMaxWidth(250);
//
//        // Create a Button to close the dialog
//        Button closeButton = new Button("Close");
////        closeButton.set(event -> {
////            dialogStage.close();
////        });
//
//        // Create a VBox to hold the Label and Button
//        VBox vbox = new VBox();
//        vbox.setAlignment(Pos.CENTER);
//        vbox.setSpacing(10);
////        vbox.setPadding(new Insets(10, 10, 10, 10));
//
//        // Set the VBox as the content of the Scene
//        Scene dialogScene = new Scene(vbox);
//
//        // Set the size of the Scene
////        dialogScene.setMinWidth(300);
////        dialogScene.setMinHeight(200);
//
//        // Set the position of the Stage in the center of the parent Scene
//        dialogStage.setX(primaryStage.getScene().getWindow().getX() + (primaryStage.getScene().getWidth() / 2) );
//        dialogStage.setY(primaryStage.getScene().getWindow().getY() + (primaryStage.getScene().getHeight() / 2));
//
//        // Set the Scene of the Stage and show the dialog
//        dialogStage.setScene(dialogScene);
//        dialogStage.show();

//        primaryStage.getScene().widthProperty().bindBidirectional(stackPane.prefWidthProperty());
//        scene.heightProperty().bindBidirectional(stackPane.prefHeightProperty());

        // Create a StackPane and add the scenes to it
//        StackPane stackPane = new StackPane();
//        stackPane.getChildren().addAll(primaryStage.getScene().getRoot(), taskView.getRoot());
//
//        // Set the StackPane as the primary stage's scene
//        primaryStage.setScene(new Scene(stackPane));
////        primaryStage.show();

//        StackPane root = new StackPane();
//        root.getChildren().addAll(primaryStage.getScene().getRoot(), taskView.getRoot());
////        taskView.getRoot().setScaleX(0.5);
////        taskView.getRoot().setScaleY(0.8);
//        Scene overlayScene = new Scene(root);
//        Stage overlayStage = new Stage();
//        overlayStage.setScene(overlayScene);
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