package revlfinal;

/**
 * Created by Coleten McGuire on 5/23/2016.
 */
import com.kinvey.java.AppData;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import org.apache.commons.math3.util.Precision;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;

public class AmazonReport {
    public String reportGenerationStatus = "";
    public ObservableList<VideoGame> reportList;
    public AmazonReport(ObservableList<VideoGame> list)

    {
        this.reportList = list;
    }
    AppData<VideoGame> myEvents;

    /*
    This function will ask the user for location and will call generateFile
     */
    public String execute() throws Exception {
        // Create the custom dialog.
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Save File");

        // Set the button types.
        ButtonType browseLocationBTN = new ButtonType("Browse Location", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(browseLocationBTN, ButtonType.CANCEL);

        // Create the userLocation and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        TextField userLocation = new TextField();
        userLocation.setPromptText("Amazon Report");
        grid.add(new Label("File Name: "), 0, 0);
        grid.add(userLocation, 1, 0);

        // Enable/Disable login button depending on whether a userLocation was entered.
        Node loginButton = dialog.getDialogPane().lookupButton(browseLocationBTN);
        loginButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        userLocation.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });
        dialog.getDialogPane().setContent(grid);

        // Request focus on the userLocation field by default.
        Platform.runLater(() -> userLocation.requestFocus());

        /*
        This will run when browse location button is clicked
         */
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == browseLocationBTN) {
                DirectoryChooser dc = new DirectoryChooser();
                File fileLocation = dc.showDialog(null);
                if (fileLocation != null) {
                    fileLocation = new File(fileLocation.getAbsolutePath());
                }

                /*
                Generate File is called here
                 */
                generateFile(userLocation, fileLocation, reportList);
                return (userLocation.getText());
            }
            return null;
        });
        Optional<String> result = dialog.showAndWait();
        return reportGenerationStatus;
    }

    /*
    This method will just generate the file in the selected location
     */
    public void generateFile(TextField username, File fileLocation, ObservableList<VideoGame> reportList) {
        double priceChange;
        String fileName = username.getText();
        System.out.println("");
        System.out.println("FileName:" + fileName + "File Location: " + fileLocation);
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFFont font = workbook.createFont();
        font.setFontName("Serif");
        font.setFontHeightInPoints((short) 11);
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        HSSFSheet sheet = workbook.createSheet("Amazon Report");
        Map<Integer, Object[]> data = new TreeMap<>();
        data.put(1, new Object[]{"Name", "Current Price", "Previous Price", "Previous Date", "Price Change"});
        int count = 1;
        for (VideoGame item : reportList) {
            if (item.getSearching().equals("Yes")) {
                priceChange = Double.valueOf(item.getAmazon()) - Double.valueOf(item.getPreviousAmazon());
                priceChange = Precision.round(priceChange, 2, BigDecimal.ROUND_HALF_UP);
                System.out.println(priceChange);
                count++;
                data.put(count, new Object[]{item.getName(), item.getAmazon(), item.getPreviousAmazon(), item.getPreviousAmazonDate(), priceChange});
            }
        }
        System.out.println("");
        System.out.println("add everything to the list");
        Set<Integer> keyset = data.keySet();
        int rownum = 0;
        for (Integer key : keyset) {
            Row row = sheet.createRow(rownum++);
            Object[] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr) {
                Cell cell = row.createCell(cellnum++);
                cell.setCellStyle(style);
                if (obj instanceof Date)
                    cell.setCellValue((Date) obj);
                else if (obj instanceof Boolean)
                    cell.setCellValue((Boolean) obj);
                else if (obj instanceof String)
                    cell.setCellValue((String) obj);
                else if (obj instanceof Double)
                    cell.setCellValue((Double) obj);
                sheet.setColumnWidth(0, 15000);
                sheet.setColumnWidth(2, 5000);
                sheet.setColumnWidth(4, 5000);
                sheet.setColumnWidth(6, 5000);
                sheet.setColumnWidth(8, 5000);
                sheet.autoSizeColumn(cellnum++);
            }
        }
        try {
            FileOutputStream out = new FileOutputStream(new File(fileLocation + "\\" + fileName + ".xls"));
            System.out.println("The total was === " + fileLocation + "\\" + fileName + ".xls");
            workbook.write(out);
            out.close();
            System.out.println("Excel written successfully..");
            reportGenerationStatus = "Amazon Report Generated";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
