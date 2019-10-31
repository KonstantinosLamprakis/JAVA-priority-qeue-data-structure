/*
	Author: 
		Konstantinos Lamprakis, koslamprakis@gmail.com
		
    This class contains main method and javaFX components for GUI.
    Reads a file and set each job to a processor properly.
    Makes use of MaxPQ priority data structure.
*/

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Greedy extends Application {

    // GUI:

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("greedy.fxml"));
        primaryStage.setTitle("Greedy");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    // Controller:

    @FXML
    private MenuBar menuBar;

    @FXML
    private TextArea textArea;

    public void initialize() {
        menuBar.getMenus().add(chooseFileMenu());
    }

    private Menu chooseFileMenu(){

        textArea.setText(""); // clear text area from previous data.
        Label FileLabel = new Label("Choose File");

        FileLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                // Show open file dialog
                File file = chooseFile();

                if(file != null) {
                    try {
                        Scanner input = new Scanner(file);
                        int processorsNumber = input.nextInt(); // read number of processors
                        int jobsNumber = input.nextInt(); // read number of jobs.

                        if(jobsNumber <= 0){
                            showErrorAlert("Error Reading File","Number of jobs must be greater than 0.");
                            return;
                        }

                        MaxPQ queue = algorithmOne(processorsNumber, getArrayOfJobs(jobsNumber, input));
                        textArea.setText((jobsNumber < 100)? queue + "\nMakespan = " + queue.getMakespan().getActiveTime() + "\n\n": "\nMakespan = " + queue.getMakespan().getActiveTime() + "\n\n");

                    } catch (FileNotFoundException | NullPointerException e) {
                        showErrorAlert("Error Opening File","File not found.");
                        return;
                    } catch (NoSuchElementException e) { // no integers as input
                        showErrorAlert("Error Reading File", "File must contains only integers. Also number of these integers must be proper.");
                        return;
                    } catch (IllegalArgumentException e){
                        showErrorAlert("Error Reading File", "Number of processors must be greater than 0.");
                        return;
                    }
                }
            }
        });

        Menu FileMenu = new Menu();
        FileMenu.setGraphic(FileLabel);
        return FileMenu;
    }

    // showErrorAlert shows an Error Alert windows.
    static void showErrorAlert(String title, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // choose and return a single file.
    static File chooseFile(){

        FileChooser fileChooser = new FileChooser();
        // Set extension filter only for .txt
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("TEXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        return fileChooser.showOpenDialog(null);
    }

    // read time of each job as integer, and return an int array with these jobs.
    static int[] getArrayOfJobs(int jobsNumber, Scanner input) throws NoSuchElementException{
        int array[] = new int[jobsNumber];
        for (int i = 0; i < jobsNumber; i++) {
            array[i] = input.nextInt();
        }
        return array;
    }

    // set each job to the processor with the minimum job_time(maximum priority).
    static MaxPQ algorithmOne(int processorsNumber, int[] jobs) throws IllegalArgumentException{
        MaxPQ queue = new MaxPQ(processorsNumber); // create a priority queue.
        for (int i = 0; i < processorsNumber; i++) queue.insert(new Processor()); // initialize queue.
        for (int i = 0; i < jobs.length; i++) {
            Processor max = queue.getMax(); // get processor with max priority.
            max.execute(jobs[i]); // processor executes next job.
            queue.insert(max); // insert again processor at right position.
        }
        return queue;
    }
}
