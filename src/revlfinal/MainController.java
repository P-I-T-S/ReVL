package revlfinal;

/**
 * Created by Coleten McGuire on 5/14/2016.
 */
import com.kinvey.nativejava.AppData;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainController implements Initializable {
    @FXML
    TableColumn<VideoGame, String> colName;
    @FXML
    TableColumn<VideoGame, String> colSystem;
    @FXML
    TableColumn<VideoGame, String> colPreviousAmazon;
    @FXML
    TableColumn<VideoGame, String> colAmazon;
    @FXML
    TableColumn<VideoGame, String> colPreviousGamestop;
    @FXML
    TableColumn<VideoGame, String> colGamestop;
    @FXML
    TableColumn<VideoGame, String> colSearching;
    @FXML
    Button addItem;
    @FXML
    Label timeText;
    @FXML
    Label statusBar;
    @FXML
    Button refresh;
    @FXML
    private TextField filterField;
    @FXML
    TableView<VideoGame> myTable;
    ArrayList<String> idList = new ArrayList();
    public ObservableList<VideoGame> list = FXCollections.observableArrayList();
    AppData<VideoGame> myEvents;
    ObservableList<VideoGame> eventSelected;

    public MainController() {

    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     * <p>
     * Initializes the table columns and sets up sorting and filtering.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configureTable();
        //search bar has to be called here
        searchBar();
        myTable.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //single click sets focus
                if (event.isPrimaryButtonDown() && event.getClickCount() == 1) {
                    System.out.println("item on focus: " + myTable.getSelectionModel().getSelectedItem());
                }
                //double click opens edit window
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    System.out.println(myTable.getSelectionModel().getSelectedItem());
                    try {
                        String textId1 = myTable.getSelectionModel().getSelectedItem().getId();
                        String textName1 = myTable.getSelectionModel().getSelectedItem().getName();
                        String textSystem1 = myTable.getSelectionModel().getSelectedItem().getSystem();
                        String textPreviousAmazon1 = myTable.getSelectionModel().getSelectedItem().getAmazon();
                        String textAmazon1 = myTable.getSelectionModel().getSelectedItem().getAmazon();
                        String textPreviousGamestop1 = myTable.getSelectionModel().getSelectedItem().getGamestop();
                        String textGamestop1 = myTable.getSelectionModel().getSelectedItem().getGamestop();
                        String textSearching1 = myTable.getSelectionModel().getSelectedItem().getSearching();
                        String textPreviousAmazonDate1 = myTable.getSelectionModel().getSelectedItem().getPreviousAmazonDate();
                        String textPreviousGamestopDate1 = myTable.getSelectionModel().getSelectedItem().getPreviousGamestopDate();
                        editItemClick(textId1, textName1, textSystem1, textPreviousAmazon1, textAmazon1, textPreviousGamestop1, textGamestop1, textSearching1, textPreviousAmazonDate1, textPreviousGamestopDate1);
                    } catch (Exception e) {
                        System.out.println("the function will not work");
                    }
                }
            }
        });
    }

    //displays information of actions performed
    public void statusBar(String str) {
        statusBar.setVisible(true);
        statusBar.setText(str);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2500);
                    statusBar.setText("Add/Edit Video Games");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //allows uer to search for items based on name
    public void searchBar() {
        //Checking the focus on search bar every single time
        filterField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {

                //Initialize columns
                colName.setCellValueFactory(new PropertyValueFactory<>("Name"));
                //Wrap the ObservableList in a FilteredList (initially display all data)
                FilteredList<VideoGame> filteredData = new FilteredList<>(list, p -> true);
                //Set the filter Predicate whenever the filter changes
                filterField.textProperty().addListener((observable, oldValue, newValue) -> {
                    filteredData.setPredicate(person -> {
                        //If filter text is empty, display all persons
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }
                        //Compare name of every person with filter text.
                        String lowerCaseFilter = newValue.toLowerCase();
                        return person.getName().toLowerCase().contains(lowerCaseFilter);
                    });
                });
                //Wrap the FilteredList in a SortedList
                SortedList<VideoGame> sortedData = new SortedList<>(filteredData);
                //Bind the SortedList comparator to the TableView comparator
                sortedData.comparatorProperty().bind(myTable.comparatorProperty());
                //Add sorted (and filtered) data to the table
                myTable.setItems(sortedData);
            }
        });
    }

    //updates information in table and tells user and displays last time refresh was performed
    public void refresh() {
        refresh.requestFocus();
        System.out.println("Table Updated !!!");
        lastUpdated();
        configureTable();
        statusBar("Data Refreshed");
    }

    //works with refresh to show last time data was updated
    public void lastUpdated() {
        String timeStamp = new SimpleDateFormat("MM/dd/yyyy  HH:mm:ss").format(Calendar.getInstance().getTime());
        timeText.setText(timeStamp);
    }


    //pulls current item data from database and allows user to make changes to the data
    public void editItemClick(String textId1, String textName1, String textSystem1, String textPreviousAmazon1, String textAmazon1, String textPreviousGamestop1, String textGamestop1, String textSearching1, String textPreviousAmazonDate1, String textPreviousGamestopDate1) throws Exception {
        /**
         * Build the dialog box and create all of the text fields/labels (maybe make the unit a dropdown box)
         * when they press ok, validate input and add into kinvey*/
        Dialog<VideoGame> dialog = new Dialog<>();
        dialog.setTitle("Edit Video Game");
        dialog.setResizable(false);

        Label labelName = new Label("Name:");
        Label labelSystem = new Label("System:");
        Label labelAmazon = new Label("Current Amazon Price:");
        Label labelGamestop = new Label("Current Gamestop Price:");
        Label labelSearching = new Label("Searching?:");

        TextField textName = new TextField();
        TextField textSystem = new TextField();
        TextField textAmazon = new TextField();
        TextField textGamestop = new TextField();
        ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList("Yes", "No"));

        textName.setText(textName1);
        textSystem.setText(textSystem1);
        textAmazon.setText(textAmazon1);
        textGamestop.setText(textGamestop1);
        if( myTable.getSelectionModel().getSelectedItem().containsValue("Yes")){
            cb.setValue("Yes");
        }
        if( myTable.getSelectionModel().getSelectedItem().containsValue("No")){
            cb.setValue("No");
        }

        GridPane grid = new GridPane();
        grid.add(labelName, 1, 1);
        grid.add(labelSystem, 1, 2);
        grid.add(labelAmazon, 1, 3);
        grid.add(labelGamestop, 1, 4);
        grid.add(labelSearching, 1, 5);
        grid.add(textName, 2, 1);
        grid.add(textSystem, 2, 2);
        grid.add(textAmazon, 2, 3);
        grid.add(textGamestop, 2, 4);
        grid.add(cb, 2, 5);

        dialog.getDialogPane().setContent(grid);
        ButtonType buttonOK = new ButtonType("Save Changes", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonOK);
        dialog.setResultConverter(button -> {
            //sets prices to 0 if null
            if (button == buttonOK) {
                String PreviousAmazonDate = textPreviousAmazonDate1;
                String PreviousGamestopDate = textPreviousGamestopDate1;
                String Amazon = "0.0";
                String Gamestop = "0.0";
                if(!textName.getText().equals("") && !textSystem.getText().equals(""))
                {
                    System.out.println("Text fields are valid.");
                }
                else
                {
                    return null;
                }
                //get values of prices if not null
                if (!textAmazon.getText().equals("")) {
                    if (!textAmazon.getText().equals(Amazon)) {
                        Date date = new Date();
                        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                        PreviousAmazonDate = format.format(date);
                    }
                    if (isNumeric(textAmazon.getText()))
                        Amazon = textAmazon.getText();
                }
                if (!textGamestop.getText().equals("")) {
                    if (!textGamestop.getText().equals(Gamestop)) {
                        Date date = new Date();
                        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                        PreviousGamestopDate = format.format(date);
                    }
                    if (isNumeric(textGamestop.getText()))
                        Gamestop = textGamestop.getText();
                }
                String selectedSearching = String.valueOf(cb.getValue());
                VideoGame newItem = new VideoGame(textId1, textName.getText(), textSystem.getText(), textPreviousAmazon1, Amazon, textPreviousGamestop1, Gamestop, selectedSearching, PreviousAmazonDate, PreviousGamestopDate);
                try {
                    myEvents.saveBlocking(newItem).execute();

                } catch (IOException e) {
                    System.out.println("Couldn't save new item! -> " + e);
                }
                return newItem;
            }
            return null;
        });
        Optional<VideoGame> result = dialog.showAndWait();
        if (result.isPresent()) {
            System.out.println("Result is present");
            configureTable();
            statusBar("Changes Saved");
        }
    }

    //set up columns in table and pull from database
    public void configureTable() {
        colName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colSystem.setCellValueFactory(new PropertyValueFactory<>("System"));
        colPreviousAmazon.setCellValueFactory(new PropertyValueFactory<>("PreviousAmazon"));
        colAmazon.setCellValueFactory(new PropertyValueFactory<>("Amazon"));
        colPreviousGamestop.setCellValueFactory(new PropertyValueFactory<>("PreviousGamestop"));
        colGamestop.setCellValueFactory(new PropertyValueFactory<>("Gamestop"));
        colSearching.setCellValueFactory(new PropertyValueFactory<>("Searching"));
        updateTable();
    }

    //allows user to delete item
    public void deleteItemClick() throws Exception {
        String eventName;
        //Alert dialog created
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Delete Video Game");
        alert.setHeaderText("This will delete the video game permanently!");
        ButtonType cancelButtonDeleteDialog = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType buttonTypeCancel = new ButtonType("Delete");
        alert.getButtonTypes().setAll(buttonTypeCancel, cancelButtonDeleteDialog);
        Optional<ButtonType> result1 = alert.showAndWait();
        if (result1.get() == buttonTypeCancel) {
            //the highlighted item
            eventSelected = myTable.getSelectionModel().getSelectedItems();
            //we will be deting this id element from the database
            eventName = eventSelected.get(0).getId();
            myEvents = Main.mKinveyClient.appData(Main.nameOfCollection, VideoGame.class);
            try {
                myEvents.deleteBlocking(eventName).execute();
            } catch (IOException e) {
                System.out.println("Couldn't delete! -> " + e);
            }
            configureTable();
            statusBar("Video Game Removed");
        } else {
            System.out.println("Exit : pressed");
        }
    }


    //allows user to add new item
    public void addItemClick() throws Exception {
        /**
         * Build the dialog box and create all of the text fields/labels (maybe make the unit a dropdown box)
         * when they press ok, validate input and add into kinvey*/
        Dialog<VideoGame> dialog = new Dialog<>();
        dialog.setTitle("Add Video Game");
        dialog.setResizable(false);

        Label labelName = new Label("Name:");
        Label labelSystem = new Label("System:");
        Label labelAmazon = new Label("Current Amazon Price:");
        Label labelGamestop = new Label("Current Gamestop Price:");
        Label labelSearching = new Label("Searching?:");

        TextField textName = new TextField();
        TextField textSystem = new TextField();
        TextField textAmazon = new TextField();
        TextField textGamestop = new TextField();
        ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList("Yes", "No"));
        cb.setValue("Yes");

        GridPane grid = new GridPane();
        grid.add(labelName, 1, 1);
        grid.add(labelSystem, 1, 2);
        grid.add(labelAmazon, 1, 3);
        grid.add(labelGamestop, 1, 4);
        grid.add(labelSearching, 1, 5);
        grid.add(textName, 2, 1);
        grid.add(textSystem, 2, 2);
        grid.add(textAmazon, 2, 3);
        grid.add(textGamestop, 2, 4);
        grid.add(cb, 2, 5);

        dialog.getDialogPane().setContent(grid);
        ButtonType buttonOK = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonOK);
        dialog.setResultConverter(button -> {
            //sets prices to zero if null
            if (button == buttonOK) {
                //generates random id for each new item added
                Random rand = new Random();
                int randNum = rand.nextInt(1001) + 1;
                String id = Integer.toString(randNum);
                while (idList.contains(id)) {
                    randNum = rand.nextInt(1001) + 1;
                    id = Integer.toString(randNum);
                }
                idList.add(id);
                String previousAmazonDate = "null";
                String previousGamestopDate = "null";
                String PreviousAmazon = "0.0";
                String Amazon = "0.0";
                String PreviousGamestop = "0.0";
                String Gamestop = "0.0";
                if(!textName.getText().equals("") && !textSystem.getText().equals(""))
                {
                    System.out.println("Text fields are valid.");
                }
                else
                {
                    return null;
                }
                //get values of prices if not null
                if (!textAmazon.getText().equals("")) {
                    if (isNumeric(textAmazon.getText()))
                        Amazon = textAmazon.getText();
                }
                if (!textGamestop.getText().equals("")) {
                    if (isNumeric(textGamestop.getText()))
                        Gamestop = textGamestop.getText();
                }
                String selectedSearching = String.valueOf(cb.getValue());
                VideoGame newItem = new VideoGame(id, textName.getText(), textSystem.getText(), PreviousAmazon, Amazon, PreviousGamestop, Gamestop, selectedSearching, previousAmazonDate, previousGamestopDate);
                try {
                    myEvents.saveBlocking(newItem).execute();
                } catch (IOException e) {
                    System.out.println("Couldn't save new item! -> " + e);
                }
                return newItem;
            }
            return null;
        });
        Optional<VideoGame> result = dialog.showAndWait();
        if (result.isPresent()) {
            System.out.println("Result is present");
            statusBar("Video Game Saved");
            configureTable();
        }
    }

    /**
     * Make sure that the values in the addItem method are valid numbers
     */
    public boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    //populates table with new data
    public void updateTable() {
        lastUpdated();
        myEvents = Main.mKinveyClient.appData(Main.nameOfCollection, VideoGame.class);
        list.clear();
        idList.clear();
        //this should be the final list that is displayed at the table
        try {
            VideoGame[] results = myEvents.getBlocking().execute();
            System.out.println("in updateTable");
            for (VideoGame item1 : results) {
                if (!list.contains(item1)) {
                    idList.add(item1.getId());
                    list.add(item1);
                }
            }
            for (int i = 0; i < idList.size(); i++) {
                System.out.println(idList.get(i));
            }
        } catch (IOException e) {
            System.out.println("Couldn't get! -> " + e);
        }
        //whole list created till here
        myTable.setItems(list);
    }
    public void generateAmazonReport() throws Exception {
        //generating the report
        AmazonReport amazonReport = new AmazonReport(list);
        String returnedStatus =  amazonReport.execute();
        System.out.println("The returned status was : " + returnedStatus);
        statusBar(returnedStatus);
    }

    public void generateGamestopReport() throws Exception {
        //generating the report
        GamestopReport gamestopReport = new GamestopReport(list);
        String returnedStatus =  gamestopReport.execute();
        System.out.println("The returned status was : " + returnedStatus);
        statusBar(returnedStatus);
    }
}