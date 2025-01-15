package view;

import config.DbIntegrityException;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Account;
import model.entities.Extract;
import model.services.AccountServices;
import model.services.ExtractServices;
import view.listeners.DataChangeListener;
import view.util.Alerts;
import view.util.Utils;

public class ContaViewController extends DataChangeListener implements Initializable {

    private AccountServices service;
    
    @FXML
    private TextField txtAccount;

    @FXML
    private TextField txtValue;

    @FXML
    private TableView<Account> tableAccount;

    @FXML
    private TableColumn<Account, Integer> columnId;

    @FXML
    private TableColumn<Account, String> conlumnAccount;

    @FXML
    private TableColumn<Account, Double> columnValue;

    @FXML
    private TableColumn<Account, Account> columnEdi;

    @FXML
    private TableColumn<Account, Account> columnRemo;

    @FXML
    private Label labelResult;

    @FXML
    private Label labelTotal;

    @FXML
    private TextField txtIncome;

    private ObservableList<Account> obsList;

    public void setAccountServices(AccountServices services) {
        this.service = services;
    }
    
    public void onBtSaveAccount() {
        if (txtAccount.getText().equals("") || txtValue.getText().equals("")) {
            Alerts.showAlert("Info", "", "Preencha ambos os campos", Alert.AlertType.INFORMATION);
        } else {
            Account obj = new Account(null, txtAccount.getText(), Utils.formatNumber(txtValue.getText()));
            service.saveOrUpdate(obj);
            updateTableView();
        }
    }

    public void onBtShowHistory() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SpendingHistoryView.fxml"));
            AnchorPane anchorPane = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Historico de Gastos");
            dialogStage.setScene(new Scene(anchorPane));
            dialogStage.setResizable(false);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();
        } catch (IOException e) {
            Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void onBtRemainingValue() {
        if (txtIncome.getText().equals("")) {
            txtIncome.setStyle("-fx-border-color: red;");
        } else if (labelTotal.getText().equals("")) {
            Alerts.showAlert("Erro", "", "ocorreu um erro inesperado", Alert.AlertType.INFORMATION);
        } else {
            try {
                String[] partes = labelTotal.getText().split("Total: R\\$");

                double tolaSum = Double.parseDouble(partes[1].replace(",", "."));
                double income = Double.parseDouble(txtIncome.getText().replace(",", "."));
                double sum = (income - tolaSum);

                if (tolaSum <= 0) {
                    labelResult.setText("00.00");
                } else {
                    labelResult.setText("R$ " + String.format("%.2f", sum));
                }
            } catch (NumberFormatException e) {
                Alerts.showAlert("Erro", "Erro ao  calcular", e.getMessage(), Alert.AlertType.WARNING);
            }
        }
    }

    public void onBtSaveExtract() {
        Optional<ButtonType> result = Alerts.showConfirmation("Confi","Salvar gastos?");
        if (ButtonType.OK == result.get()) {
            ExtractServices ExtractServices = new ExtractServices();
            String dataList = "";

            for (Account a : obsList) {
                dataList += a.getAccount() + ";" + a.getValue() + ":";
            }
            if (!dataList.equals("")) {
                Extract history = new Extract(null, dataList, new Date());
                ExtractServices.save(history);
            } else {
                Alerts.showAlert("Info", "", "Nao a dados para salvar", Alert.AlertType.INFORMATION);
            }
        }
    }

    public void updateTableView() {
        if (service == null) {
            throw new IllegalStateException("Service was null");
        }

        List<Account> list = service.findAll();
        obsList = FXCollections.observableArrayList(list);
        tableAccount.setItems(obsList);
        initEditButtons();
        initRemoveButtons();
        totalValue(list);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeNodes();
    }

    private void initializeNodes() {
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        conlumnAccount.setCellValueFactory(new PropertyValueFactory<>("account"));
        columnValue.setCellValueFactory(new PropertyValueFactory<>("value"));
    }

    private void createDialogForm(Account obj, String absoluteName, Stage parentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            AnchorPane anchorPane = loader.load();

            ContaFormController controller = loader.getController();
            controller.setAccount(obj);
            controller.setAccountService(new AccountServices());
            controller.subscriberDataChangeListener(this);
            controller.updateFormData();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Editar Dados");
            dialogStage.setScene(new Scene(anchorPane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(parentStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();
        } catch (IOException e) {
            Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void initEditButtons() {
        columnEdi.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        columnEdi.setCellFactory(param -> new TableCell<Account, Account>() {
            private final Button button = new Button("editar");

            {
                button.setPrefWidth(100);
                button.setPrefHeight(20);
            }

            @Override
            protected void updateItem(Account obj, boolean empty) {
                super.updateItem(obj, empty);

                if (obj == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(button);
                button.setOnAction(event -> createDialogForm(obj, "/view/ContaForm.fxml", Utils.currentStage(event)));
            }
        });
    }

    private void initRemoveButtons() {
        columnRemo.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        columnRemo.setCellFactory(param -> new TableCell<Account, Account>() {
            private final Button button = new Button("remover");

            {
                button.setPrefWidth(100);
                button.setPrefHeight(20);
            }

            @Override
            protected void updateItem(Account obj, boolean empty) {
                super.updateItem(obj, empty);

                if (obj == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(button);
                button.setOnAction(event -> removeEntity(obj));
            }
        });
    }

    private void removeEntity(Account obj) {
        Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Tem certeza de que deseja excluir?");
        if (result.get() == ButtonType.OK) {
            if (service == null) {
                throw new IllegalStateException("Service was null");
            }
            try {
                service.remove(obj);
                updateTableView();
            } catch (DbIntegrityException e) {
                Alerts.showAlert("Error removing object", null, e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private void totalValue(List<Account> list) {
        double sum = 0;
        for (Account a : list) {
            sum += a.getValue();
        }
        labelTotal.setText("Total: R$" + String.format("%.2f", sum));
    }

    @Override
    public void onDataChanged() {
        updateTableView();
    }
}
