/*
    This class contains main method and javaFX components for GUI.
    Reads a single file or multiple files and set each job to a processor properly.
    Makes use of both algorithm1(using MaxPQ priority structure) ad algorithm2(sorts jobs with quicksort)
*/

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Comparisons extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("comparisons.fxml"));
        primaryStage.setTitle("Comparisons");
        primaryStage.setScene(new Scene(root, 400, 275));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    // Controller:

    private static int id = 0;

    @FXML
    private MenuBar menuBar;

    @FXML
    private TextArea textArea1;

    @FXML
    private TextArea textArea2;

    public void initialize() {
        menuBar.getMenus().addAll(chooseSingleFileMenu(), choosemultipleFilesMenu(), createFileMenu());
    }

    private Menu chooseSingleFileMenu(){

        Label FileLabel = new Label("Choose Single File");

        FileLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                // clear previous data.
                textArea1.setText("");
                textArea2.setText("");

                // Show open file dialog
                File file = Greedy.chooseFile();

                if(file != null) {
                    try {
                        Scanner input = new Scanner(file);
                        int processorsNumber = input.nextInt(); // read number of processors
                        int jobsNumber = input.nextInt(); // read number of jobs.

                        if(jobsNumber <= 0){
                            Greedy.showErrorAlert("Error Reading File","Number of jobs must be greater than 0.");
                            return;
                        }

                        // algorithm 1:
                        int array[] = Greedy.getArrayOfJobs(jobsNumber, input);
                        MaxPQ queue1 =  Greedy.algorithmOne(processorsNumber,  array);
                        textArea1.setText((jobsNumber < 100)? queue1 + "\nMakespan = " + queue1.getMakespan().getActiveTime() + "\n\n": "\nMakespan = " + queue1.getMakespan().getActiveTime() + "\n\n");

                        // algorithm 2:
                        Sort.quickSort(array,0, array.length-1);
                        MaxPQ queue2 =  Greedy.algorithmOne(processorsNumber, array);
                        textArea2.setText((jobsNumber < 100)? queue2 + "\nMakespan = " + queue2.getMakespan().getActiveTime() + "\n\n": "\nMakespan = " + queue2.getMakespan().getActiveTime() + "\n\n");

                    } catch (FileNotFoundException | NullPointerException e) {
                        Greedy.showErrorAlert("Error Opening File","File not found.");
                        return;
                    } catch (NoSuchElementException e) { // no integers as input
                        Greedy.showErrorAlert("Error Reading File", "File must contains only integers. Also number of these integers must be proper.");
                        return;
                    } catch (IllegalArgumentException e){
                        Greedy.showErrorAlert("Error Reading File", "Number of processors must be greater than 0.");
                        return;
                    }
                }
            }
        });

        Menu fileMenu = new Menu();
        fileMenu.setGraphic(FileLabel);
        return fileMenu;
    }

    private Menu choosemultipleFilesMenu(){

        Label FileLabel = new Label("Choose Multiple Files");

        FileLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                // Show open file dialog
                FileChooser fileChooser = new FileChooser();
                // Set extension filter only for .txt
                FileChooser.ExtensionFilter extFilter =
                        new FileChooser.ExtensionFilter("TEXT files (*.txt)", "*.txt");
                fileChooser.getExtensionFilters().add(extFilter);
                List<File> list = fileChooser.showOpenMultipleDialog(null);

                // clear previous data.
                textArea1.setText("");
                textArea2.setText("");

                double average1 = 0; // average of makespans for algorithm 1.
                double average2 = 0; // average of makespans for algorithm 2.
                int i=0;

                if(list == null)return;
                for(File file: list){
                    ++i;
                    try {
                        Scanner input = new Scanner(file);
                        int processorsNumber = input.nextInt(); // read number of processors
                        int jobsNumber = input.nextInt(); // read number of jobs.

                        if(jobsNumber <= 0){
                            Greedy.showErrorAlert("Error Reading File","Number of jobs must be greater than 0.");
                            return;
                        }

                        // algorithm 1:
                        int array[] = Greedy.getArrayOfJobs(jobsNumber, input);
                        MaxPQ queue1 =  Greedy.algorithmOne(processorsNumber,  array);
                        textArea1.setText(textArea1.getText() + "\nFile: " + i + " Makespan = " + queue1.getMakespan().getActiveTime());
                        average1 += queue1.getMakespan().getActiveTime();

                        // algorithm 2:
                        Sort.quickSort(array,0, array.length-1);
                        MaxPQ queue2 =  Greedy.algorithmOne(processorsNumber, array);
                        textArea2.setText(textArea2.getText() + "\nFile: " + i + " Makespan = " + queue2.getMakespan().getActiveTime());
                        average2 += queue2.getMakespan().getActiveTime();

                    } catch (FileNotFoundException | NullPointerException e) {
                        Greedy.showErrorAlert("Error Opening File","File not found.");
                        return;
                    } catch (NoSuchElementException e) { // no integers as input
                        Greedy.showErrorAlert("Error Reading File", "File must contains only integers. Also number of these integers must be proper.");
                        return;
                    } catch (IllegalArgumentException e){
                        Greedy.showErrorAlert("Error Reading File", "Number of processors must be greater than 0.");
                        return;
                    }
                }

                textArea1.setText(textArea1.getText() + "\n\nAverage Makespan: "+ (float) average1/i + "\n\n");
                textArea2.setText(textArea2.getText() + "\n\nAverage Makespan: "+ (float) average2/i + "\n\n");

            }
        });

        Menu fileMenu = new Menu();
        fileMenu.setGraphic(FileLabel);
        return fileMenu;
    }

    private Menu createFileMenu(){

        Label createLabel = new Label("Create Random Files");

        createLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                // Create the dialog.
                Dialog<String> dialog = new Dialog<>();
                dialog.setTitle("Random Files Creation");
                dialog.setHeaderText("");

                // set button.
                dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

                // Create the labels and fields.
                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 150, 10, 10));

                TextField jobsNumberField = new TextField();
                jobsNumberField.setPromptText("number of job");

                TextField filesNumberField = new TextField();
                filesNumberField.setPromptText("number of radom files");

                grid.add(new Label("Enter number of jobs:"), 0, 0);
                grid.add(jobsNumberField, 1, 0);
                grid.add(new Label("Enter number of random files:"), 0, 1);
                grid.add(filesNumberField, 1, 1);

                dialog.getDialogPane().setContent(grid);
                Optional<String> result = dialog.showAndWait();

                if (result.isPresent()){
                    String jobs = jobsNumberField.getText();
                    String files = filesNumberField.getText();
                    if(!isInteger(jobs) || Integer.parseInt(jobs)<1){
                        Greedy.showErrorAlert("Error Creating Files","The number of jobs must be an integer greater than 0.");
                        return;
                    }
                    if(!isInteger(files) || Integer.parseInt(files)<1){
                        Greedy.showErrorAlert("Error Creating Files","The number of random files must be an integer greater than 0.");
                        return;
                    }
                    writeFiles(Integer.parseInt(jobs), Integer.parseInt(files));
                }

            }
        });

        Menu creationMenu = new Menu();
        creationMenu.setGraphic(createLabel);
        return creationMenu;
    }

    private boolean isInteger(String str){
        return str.matches("\\d+");
    }

    private void writeFiles(int jobsNumber, int filesNumber){

        try{

            // choose directory for storing random files.
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Choose directory for saving random files");
            directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));

            // write file.
            String mainPath = directoryChooser.showDialog(null).getPath();
            for(int i = 0; i < filesNumber; i++){
                Path path = Paths.get( mainPath + File.separator + "random_file" + ++id +".txt");
                Files.write(path,  getRandomList(jobsNumber) , Charset.forName("UTF-8"));
            }

        }catch (IOException e){
            Greedy.showErrorAlert("Error Writing File", "An error occured.");
            return;
        }

        // showInfoAlert shows an Information Alert windows.
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Successful Creation");
        alert.setHeaderText(null);
        alert.setContentText("Creation of random file is successful!");
        alert.showAndWait();

    }

    private List<String> getRandomList(int jobsNumber){
        Random rand = new Random();
        List<String> randomList = new ArrayList<String>();

        randomList.add((int) Math.sqrt(jobsNumber) +"");// processor number
        randomList.add(jobsNumber +"");
        for(int i=0; i<jobsNumber; i++) {
            randomList.add((rand.nextInt(200) + 10) +""); // 10-210 range of random integers(= range of random time of each job).
        }
        return randomList;
    }

}
