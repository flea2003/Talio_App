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
package client;

import client.scenes.*;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.google.inject.Guice.createInjector;

public class Main extends Application {

    public static final Injector INJECTOR = createInjector(new MyModule());
    public static final MyFXML FXML = new MyFXML(INJECTOR);

    /**
     * the main method of the client
     * @param args an array of command-line arguments that are passed to the application
     * @throws URISyntaxException if there is an error when constructing a URI
     * @throws IOException if there is an error with input/output or network connection
     */
    public static void main(String[] args) throws URISyntaxException, IOException {
        launch();
    }

    /**
     *
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     */
    @Override
    public void start(Stage primaryStage) {

        var serverConnect=FXML.load(ServerConnectCtrl.class,"client","scenes","ServerConnect.fxml");

        Scene server=new Scene(serverConnect.getValue());
        ServerConnectCtrl serverConnectCtrl=serverConnect.getKey();
        primaryStage.setTitle("Choose a server");
        primaryStage.setScene(server);
        primaryStage.show();
        Button connectButton = (Button) serverConnect.getValue().lookup("#connectButton");
        Button connectAdmin = (Button) serverConnect.getValue().lookup("#connectAdmin");
        connectButton.setOnAction(e -> {
            if (serverConnectCtrl.connectToTheServer(e)) {
                var dashboard = FXML.
                        load(DashboardCtrl.class, "client", "scenes", "Dashboard.fxml");
                var board = FXML.
                        load(CreateBoardCtrl.class, "client", "scenes", "CreateBoard.fxml");
                var taskCreation = FXML.
                        load(TaskCreationCtrl.class, "client", "scenes", "TaskCreation.fxml");
                var taskView = FXML.
                        load(TaskViewCtrl.class, "client", "scenes", "TaskView.fxml");
                var taskEdit = FXML.
                        load(TaskEditCtrl.class, "client", "scenes", "TaskEdit.fxml");
                var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
                mainCtrl.initialize(primaryStage, dashboard, board,
                        taskCreation, taskView, taskEdit);
            }

        });
        Button connectButton2 = (Button) serverConnect.getValue().lookup("#connectButton2");
        connectButton2.setOnAction(e -> {
            if (serverConnectCtrl.checkPassword(e) && serverConnectCtrl.connectToTheServer(e)) {
                var dashboard = FXML.load(DashboardCtrl.class,
                        "client", "scenes", "Dashboard.fxml");
                var board = FXML.load(CreateBoardCtrl.class,
                        "client", "scenes", "CreateBoard.fxml");
                var taskCreation = FXML.load(TaskCreationCtrl.class,
                        "client", "scenes", "TaskCreation.fxml");
                var taskView = FXML.load(TaskViewCtrl.class, "client", "scenes", "TaskView.fxml");
                var taskEdit = FXML.load(TaskEditCtrl.class, "client", "scenes", "TaskEdit.fxml");
                var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
                mainCtrl.initialize(primaryStage,
                        dashboard, board, taskCreation, taskView, taskEdit);
            }

        });

    }


}