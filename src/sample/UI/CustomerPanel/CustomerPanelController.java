package sample.UI.CustomerPanel;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import sample.Alert.AlertMaker;
import sample.Database.DatabaseHelper;
import sample.Database.ExcelDatabaseHelper;
import sample.Database.ExcelHelper;
import sample.Main;
import sample.Model.Customer;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class CustomerPanelController implements Initializable {
    @FXML
    public JFXCheckBox checkGST, checkGST1, checkGST2;
    @FXML
    private BorderPane borderPane;
    @FXML
    private JFXTextField cnName, phone, gstIn, address1, address2, state, zip;

    private Main mainApp;

    @FXML
    private TableView<Customer> userTableView;

    private RequiredFieldValidator validator = new RequiredFieldValidator("*");
    private boolean isNewCustomer = true;

    private Customer customer = null;

    private ObservableList<Customer> all = FXCollections.observableArrayList();

    private ObservableList<Customer> gst = FXCollections.observableArrayList();

    private ObservableList<Customer> nonGST = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cnName.getValidators().add(validator);
        phone.getValidators().add(validator);
        gstIn.getValidators().add(validator);
        address1.getValidators().add(validator);
        address2.getValidators().add(validator);
        state.getValidators().add(validator);
        zip.getValidators().add(validator);
        checkGST1.setSelected(true);
        checkGST2.setSelected(true);
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
        init();
        gstIn.setDisable(true);
        userTableView.setOnMouseClicked(e -> {
            clearAll();
            customer = userTableView.getSelectionModel().getSelectedItem();
            if (customer == null) return;
            isNewCustomer = false;
            setAll(customer);
        });
        checkGST.setOnAction(e -> toggle(checkGST.isSelected()));
        checkGST1.setOnAction(e -> setCustomer());
        checkGST2.setOnAction(e -> setCustomer());
    }

    private void setAll(Customer customer) {
        cnName.setText(customer.getName());
        if (!customer.getGstIn().equals("For Own Use")) checkGST.setSelected(true);
        else checkGST.setSelected(false);
        toggle(checkGST.isSelected());
        gstIn.setText(customer.getGstIn());
        address1.setText(customer.getStreetAddress());
        address2.setText(customer.getCity());
        state.setText(customer.getState());
        zip.setText(customer.getZipCode());
        phone.setText(customer.getPhone());
    }

    private void init() {
        userTableView.getColumns().clear();
        userTableView.getItems().clear();

        addTableColumn("Name", "name");
        addTableColumn("Phone", "phone");
        addTableColumn("GST IN", "gstIn");
        addTableColumn("Street", "streetAddress");
        addTableColumn("City", "city");
        addTableColumn("State", "state");
        addTableColumn("Zip", "zipCode");
        userTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        setCustomer();
        borderPane.setCenter(userTableView);
    }

    @FXML
    void handleDelete() {
        try {
            Customer customer = userTableView.getSelectionModel().getSelectedItem();
            boolean okay;
            okay = AlertMaker.showMCAlert("Confirm delete?"
                    , "Are you sure you want to delete" + customer.getName() + "'s data"
                    , mainApp);
            if (okay) {
                if (DatabaseHelper.deleteCustomer(customer)) {
                    mainApp.snackBar("Success"
                            , "Selected Customer's data is deleted"
                            , "green");
                    clearAll();
                } else {
                    mainApp.snackBar("Failed"
                            , "Selected User's data is not deleted"
                            , "red");
                    clearAll();
                }
            }
        } catch (Exception e) {
            AlertMaker.showErrorMessage(e);
            e.printStackTrace();
        }
        setCustomer();
    }

    @FXML
    void handleSubmit() {
        boolean submitted = false;
        if (isNewCustomer) {
            if (check()) {
                if (phone.getText().length() != 10 && checkNum(phone.getText())) {
                    mainApp.snackBar("Info", "Enter Correct Phone", "red");
                    return;
                }
                if (!checkGST.isSelected()) {
                    gstIn.setText("For Own Use");
                } else {
                    if (gstIn.getText().length() != 15) {
                        mainApp.snackBar("Info", "Enter Correct length of GSTIN NO Entered : "
                                + gstIn.getText().length(), "red");
                        return;
                    }
                }

                Customer customer = new Customer(cnName.getText(), phone.getText(), gstIn.getText()
                        , "" + address1.getText(), "" + address2.getText(), state.getText(), zip.getText()
                        , "CUS" + new SimpleDateFormat("yyyyMMddHHSSS").format(new Date()));
                submitted = DatabaseHelper.insertNewCustomer(customer);
                if (submitted) {
                    mainApp.snackBar("Success", "New Customer Added Successfully", "green");
                } else {
                    mainApp.snackBar("Failed", "New Customer Not Added", "red");
                }
            }
        } else {
            if (check()) {
                if (phone.getText().length() != 10 && checkNum(phone.getText())) {
                    mainApp.snackBar("Info", "Enter Correct Phone", "red");
                    return;
                }
                if (!checkGST.isSelected()) {
                    gstIn.setText("For Own Use");
                } else {
                    if (gstIn.getText().length() != 15) {
                        mainApp.snackBar("Info", "Enter Correct length of GSTIN NO Entered : "
                                + gstIn.getText().length(), "red");
                        return;
                    }
                }
                Customer newCustomer = new Customer(cnName.getText()
                        , "" + phone.getText(), "" + gstIn.getText()
                        , "" + address1.getText(), "" + address2.getText()
                        , "" + state.getText(), "" + zip.getText()
                        , customer.getId());
                submitted = DatabaseHelper.updateCustomer(newCustomer);
                if (submitted) {
                    mainApp.snackBar("Success", "Customer Data Updated Successfully"
                            , "green");
                } else {
                    mainApp.snackBar("Failed"
                            , "Customer Data Not Updated Successfully", "red");
                }
            }
        }
        if (submitted) {
            setCustomer();
            clearAll();
        }
    }

    @FXML
    void handleImport() {
        mainApp.snackBar("", "Choose File", "green");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll
                (new FileChooser.ExtensionFilter("Excel", "*.xlsx"));
        File dest = fileChooser.showSaveDialog(mainApp.getPrimaryStage());
        if (dest == null) {
            mainApp.snackBar("INFO","Operation Cancelled","green");
        } else {
            JFXSpinner spinner = new JFXSpinner();
            double d = 50;
            spinner.setMaxSize(d, d);
            spinner.setPrefSize(d, d);
            borderPane.getChildren().add(spinner);
            if (ExcelDatabaseHelper.writeDBCustomer(dest)) {
                mainApp.snackBar("Success"
                        , "Stock History Data Written to Excel"
                        , "green");
            } else {
                mainApp.snackBar("Failed"
                        , "Stock History Data is NOT written to Excel"
                        , "red");
            }
            borderPane.getChildren().remove(spinner);
        }
        init();
    }

    @FXML
    void handleExport() {
        if (all.size() == 0) {
            mainApp.snackBar("", "Nothing to Import", "red");
            return;
        }
        mainApp.snackBar("", "Choose File", "green");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll
                (new FileChooser.ExtensionFilter("Excel", "*.xlsx"));
        File dest = fileChooser.showSaveDialog(mainApp.getPrimaryStage());
        if (dest == null) {
            mainApp.snackBar("INFO","Operation Cancelled","green");
        } else {
            JFXSpinner spinner = new JFXSpinner();
            double d = 50;
            spinner.setMaxSize(d, d);
            spinner.setPrefSize(d, d);
            borderPane.getChildren().add(spinner);
            if (ExcelHelper.writeExcelCustomer(dest, all))
                mainApp.snackBar("Success"
                    , "Customer Data Written to Excel"
                    , "green");
            else
                mainApp.snackBar("Failed"
                        , "Customer Data is NOT written to Excel"
                        , "red");
            borderPane.getChildren().remove(spinner);
        }
    }

    private void addTableColumn(String name, String msg) {
        TableColumn<Customer, String> column = new TableColumn<>(name);
        column.setCellValueFactory(new PropertyValueFactory<>(msg));
        column.setPrefWidth(100);
        userTableView.getColumns().add(column);
    }

    private void clearAll() {
        isNewCustomer = true;
        cnName.setText("");
        phone.setText("");
        address1.setText("");
        gstIn.setText("");
        address2.setText("");
        state.setText("");
        zip.setText("");
        checkGST.setSelected(false);
        gstIn.setDisable(true);
        customer = null;
    }

    private boolean check() {
        if (cnName.getText() == null || cnName.getText().isEmpty()
                || phone.getText() == null || phone.getText().isEmpty()) {
            if (cnName.getText() == null || cnName.getText().isEmpty()) cnName.validate();
            if (phone.getText() == null || phone.getText().isEmpty()) phone.validate();
            return false;
        } else {
            return true;
        }
    }

    private boolean checkNum(String s) {
        try {
            Long.parseLong(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void toggle(boolean b) {
        if (b) {
            gstIn.setDisable(false);
            gstIn.setEditable(true);
            gstIn.getValidators().add(validator);
            gstIn.validate();
        } else {
            gstIn.setDisable(true);
            gstIn.setEditable(false);
            gstIn.resetValidation();
        }
    }

    private void setCustomer() {
        getCustomers();
        userTableView.getItems().clear();
        if ((checkGST1.isSelected() && checkGST2.isSelected())
                || (!checkGST1.isSelected() && !checkGST2.isSelected()))
            userTableView.getItems().addAll(all);
        else if (checkGST1.isSelected())
            userTableView.getItems().addAll(gst);
        else
            userTableView.getItems().addAll(nonGST);
    }

    private void getCustomers() {
        all = DatabaseHelper.getCustomerList();
        nonGST.clear();
        gst.clear();
        for (Customer c : all) {
            if (c.getGstIn().equals("For Own Use"))
                nonGST.add(c);
            else gst.add(c);
        }
    }
}
