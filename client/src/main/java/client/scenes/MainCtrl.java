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

import client.utils.ServerUtils;
import commons.Card;
import commons.List;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {

    private Stage primaryStage;
    private QuoteOverviewCtrl overviewCtrl;
    private Scene overview;
    private AddQuoteCtrl addCtrl;
    private Scene add;
    private RegistrationCtrl regCtrl;
    private TaskViewCtrl taskViewCtrl;
    private TaskCreationCtrl taskCreationCtrl;
    private DashboardCtrl dashboardCtrl;
    private Scene registration;
    private CreateBoardCtrl boardCtrl;
    private Scene board;
    private Scene dashboard;
    private Scene taskView;
    private Scene taskCreation;
    private Scene taskEdit;
    private TaskEditCtrl taskEditCtrl;

    private Scene server;

    private  ServerConnectCtrl serverCtrl;
    public void initialize(Stage primaryStage, Pair<QuoteOverviewCtrl, Parent> overview,
                           Pair<AddQuoteCtrl, Parent> add,
                           Scene server, ServerConnectCtrl serverConnectCtrl,
                           Pair<RegistrationCtrl, Parent> registration,
                           Pair<DashboardCtrl, Parent> dashboard,
                           Pair<CreateBoardCtrl, Parent> board,
                           Pair<TaskCreationCtrl, Parent>taskCreation,
                           Pair<TaskViewCtrl, Parent>taskView,
                           Pair<TaskEditCtrl, Parent>taskEdit) {

        this.primaryStage = primaryStage;
        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());

        this.addCtrl = add.getKey();
        this.regCtrl = registration.getKey();

        this.add = new Scene(add.getValue());

        this.registration = new Scene(registration.getValue());

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

        this.server=server;
        this.serverCtrl= serverCtrl;

        switchDashboard("");
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void showOverview() {
        primaryStage.setTitle("Quotes: Overview");
        primaryStage.setScene(overview);
        overviewCtrl.refresh();
    }

    public void showAdd() {
        primaryStage.setTitle("Quotes: Adding Quote");
        primaryStage.setScene(add);
        add.setOnKeyPressed(e -> addCtrl.keyPressed(e));
    }

    public void switchRegistration(){
        primaryStage.setTitle("Registration");
        primaryStage.setScene(registration);
        registration.setOnKeyPressed(e -> regCtrl.keyPressed(e));
    }
    
    public void switchTaskCreation(List listCurr){
        System.out.println(listCurr);
        primaryStage.setTitle("Task Creation");
        primaryStage.setScene(taskCreation);
        taskCreationCtrl.setListCurr(listCurr);
        taskCreation.setOnKeyPressed(e -> taskCreationCtrl.keyPressed(e));
    }

    /**
     * switches the scene to the dashboard
     * @param user
     */
    public void switchDashboard(String user){
        System.out.println("SWITCH TO DASHBOARD");
        primaryStage.setTitle("Dashboard");
        dashboardCtrl.refresh();
        primaryStage.setScene(dashboard);
    }

    public void fetchUpdatesDashboard(String user){
        dashboardCtrl.refresh();
    }

    public void switchServer(){
        primaryStage.setTitle("Choose a server");
        primaryStage.setScene(server);
    }

    /**
     * switches the scene to the create a board
     */
    public void switchCreateBoard() {
        primaryStage.setTitle("Create a Board");
        primaryStage.setScene(board);
        board.setOnKeyPressed(e -> boardCtrl.keyPressed(e));
    }

    public void switchTaskView(Card q){
        primaryStage.setTitle("View Task");
        primaryStage.setScene(taskView);
        System.out.println(q);
        taskViewCtrl.renderInfo(q);
    }

    public void switchEdit(Card q){
        primaryStage.setTitle("Edit Task");
        primaryStage.setScene(taskEdit);
        System.out.println(q);
        taskEditCtrl.renderInfo(q);
    }


}