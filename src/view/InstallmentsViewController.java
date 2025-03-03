package view;

import config.DbIntegrityException;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Installments;
import model.services.InstallmentsServices;
import view.listeners.DataChangeListener;
import view.util.Alerts;
import view.util.Utils;

public class InstallmentsViewController extends DataChangeListener implements Initializable {

    private InstallmentsServices services;

    @FXML
    private TableView<Installments> tableInstallments;

    @FXML
    private TableColumn<Installments, Integer> columnId;

    @FXML
    private TableColumn<Installments, String> columnItem;

    @FXML
    private TableColumn<Installments, Double> columnTotalValue;

    @FXML
    private TableColumn<Installments, Integer> columnInstallments;

    @FXML
    private TableColumn<Installments, Double> columnValueInstallments;

    @FXML
    private TableColumn<Installments, Integer> columnInstallmentsPaid;

    @FXML
    private TableColumn<Installments, Integer> columnRemainingInstallments;

    @FXML
    private TableColumn<Installments, Date> columnLastPayment;

    @FXML
    private TextField txtItem;

    @FXML
    private TextField txtTotalValue;

    @FXML
    private TextField txtInstallments;

    @FXML
    private TextField txtInstallmentsPaid;

    @FXML
    private DatePicker txtDatePayment;

    private ObservableList<Installments> obsList;

    public void setInstallmentsServices(InstallmentsServices services) {
        this.services = services;
        updateTableView();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeNodes();
    }

    public void onBtSaveInstallments() {
        if (txtInstallments.getText().equals("") || txtItem.getText().equals("") || txtTotalValue.getText().equals("")) {
            Alerts.showAlert("Info", "", "Prencha ambos os campos", Alert.AlertType.INFORMATION);
        } else {
            String item = txtItem.getText();
            double value = Utils.formatNumber(txtTotalValue.getText());
            int number = Utils.tryParseToInt(txtInstallments.getText());
            int paid = (txtInstallmentsPaid.getText().equals("")) ? 0 : Utils.tryParseToInt(txtInstallmentsPaid.getText());
            
            LocalDate localDate = txtDatePayment.getValue();
            
            Date date = null;
            if (localDate != null) {
                Instant instant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant() ;
                date = Date.from(instant);
            }
                
            Installments obj = new Installments(null, item, value, number, paid, date);
            services.saveOrUpdate(obj);
            updateTableView();
        }
    }

    public void onBtShowWindow (ActionEvent event) {
        Installments obj = tableInstallments.getSelectionModel().getSelectedItem();
        if (obj != null) {
           createDialogForm(obj); 
        } else {
            Alerts.showAlert("Info", "", "Selecione uma linha", Alert.AlertType.INFORMATION);
        }
    }
    
    public void onBtRemove() {
        Installments selectionId = tableInstallments.getSelectionModel().getSelectedItem();
        if (selectionId != null) {
            Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Tem certeza de que deseja excluir?");
            if (result.get() == ButtonType.OK) {
                if (services == null) {
                    throw new IllegalStateException("Service was null");
                }
                try {
                    services.remove(selectionId.getId());
                    updateTableView();
                } catch (DbIntegrityException e) {
                    Alerts.showAlert("Error removing object", null, e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        } else {
            Alerts.showAlert("Info", "", "Selecione uma linha", Alert.AlertType.INFORMATION);
        }
    }

    private void createDialogForm(Installments obj) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/InstallmentsForm.fxml"));
            AnchorPane anchorPane = loader.load();

            InstallmentsFormController controller = loader.getController();
            controller.setInstallments(obj);
            controller.setService(services);
            controller.subscriberDataChangeListener(this);
            controller.updateFormData();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Editar Dados");
            dialogStage.setScene(new Scene(anchorPane));
            dialogStage.setResizable(false);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();
        } catch (IOException e) {
            Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void initializeNodes() {
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnItem.setCellValueFactory(new PropertyValueFactory<>("item"));
        columnTotalValue.setCellValueFactory(new PropertyValueFactory<>("totalValue"));
        columnInstallments.setCellValueFactory(new PropertyValueFactory<>("installments"));
        columnValueInstallments.setCellValueFactory(new PropertyValueFactory<>("installmentsValue"));
        columnInstallmentsPaid.setCellValueFactory(new PropertyValueFactory<>("InstallmentsPaid"));
        columnRemainingInstallments.setCellValueFactory(new PropertyValueFactory<>("remainingInstallments"));
        columnLastPayment.setCellValueFactory(new PropertyValueFactory<>("lastPayment"));
    }

    @FXML
    private void updateTableView() {
        if (services == null) {
            throw new IllegalStateException("Service was null");
        }

        List<Installments> list = services.findAll();

        List<Installments> newList = new ArrayList<>();

        for (Installments obj : list) {
            double value = obj.installmentValue(obj.getTotalValue(), obj.getInstallments());
            int number = obj.remainingInstallments(obj.getInstallments(), obj.getInstallmentsPaid());
            obj.setInstallmentsValue(value);
            obj.setRemainingInstallments(number);
            newList.add(obj);
        }

        obsList = FXCollections.observableArrayList(newList);
        tableInstallments.setItems(obsList);
    }

    @Override
    public void onDataChanged() {
        updateTableView();
    }
}
