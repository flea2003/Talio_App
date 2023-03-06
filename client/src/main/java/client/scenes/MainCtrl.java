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
    private Scene registration;

    private Scene dashboard;

    public void initialize(Stage primaryStage, Pair<QuoteOverviewCtrl, Parent> overview,
                           Pair<AddQuoteCtrl, Parent> add,
                           Pair<RegistrationCtrl, Parent>registration,
                           Pair<DashboardCtrl, Parent>dashboard) {
        this.primaryStage = primaryStage;
        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());

        this.addCtrl = add.getKey();
        this.add = new Scene(add.getValue());

        this.registration = new Scene(registration.getValue());

        this.dashboard = new Scene(dashboard.getValue());

        showRegistration();
        primaryStage.show();

    }

    public void showRegistration() {
        primaryStage.setTitle("Welcome");
        primaryStage.setScene(registration);
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

    public void switchDashboard(String user){
        primaryStage.setTitle("Dashboard");
        primaryStage.setScene(dashboard);
    }
}