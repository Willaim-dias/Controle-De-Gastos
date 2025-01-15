package view;

import config.DbIntegrityException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.entities.Card;
import model.services.CardServices;
import view.util.Alerts;
import view.util.Utils;

public class CreditCardViewController implements Initializable {

    private CardServices service;

    @FXML
    private TextField txtValue;

    @FXML
    private DatePicker txtDate;

    @FXML
    private TableView<Card> tableCard;

    @FXML
    private TableColumn<Card, Integer> columnId;

    @FXML
    private TableColumn<Card, Double> columnValue;

    @FXML
    private TableColumn<Card, Date> columnDate;

    @FXML
    private TableColumn<Card, Card> columnRemo;

    @FXML
    private ComboBox<String> comboBoxDate;

    @FXML
    private ListView<String> listFilter;

    @FXML
    private Label labelFilterTotal;

    private ObservableList<Card> obsList;

    public void setCardServices(CardServices services) {
        this.service = services;
    }
    
    public void onBtnSaveAccount(ActionEvent event) {
        if (txtValue.getText().equals("") && txtDate.getValue() == null) {
            Alerts.showAlert("Info", "", "Preencha ambos os campos", Alert.AlertType.INFORMATION);
        } else {
            try {
                Instant instant = txtDate.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
                Card obj = new Card(null, Date.from(instant), Utils.formatNumber(txtValue.getText()));
                service.saveOrUpdate(obj);
                updateTableView();
            } catch (RuntimeException e)  {
                Alerts.showAlert("Erro", "", "Ocorreu um erro inesperado", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void updateListView() {
        double sum = 0;
        listFilter.getItems().clear();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        SimpleDateFormat sdf0 = new SimpleDateFormat("dd/MM/yyyy");
        for (Card card : obsList) {
            String date = sdf.format(card.getDate());

            if (date.equals(comboBoxDate.getValue())) {
                String data = "Valor: R$" + card.getValue() + ", Data: " + sdf0.format(card.getDate());
                sum += card.getValue();
                listFilter.getItems().add(data);
            }
        }
        labelFilterTotal.setText("Total R$" + String.format("%.2f", sum));
    }

    public void updateTableView() {
        if (service == null) {
            throw new IllegalStateException("Service was null");
        }
        List<Card> list = service.findAll();
        obsList = FXCollections.observableArrayList(list);
        tableCard.setItems(obsList);
        initRemoveButtons();
        addDateComboBox(list);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeNodes();
    }

    private void initializeNodes() {
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnValue.setCellValueFactory(new PropertyValueFactory<>("value"));
        columnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        Utils.formatTableColumnDate(columnDate, "dd/MM/yyyy");
    }

    private void initRemoveButtons() {
        columnRemo.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        columnRemo.setCellFactory(param -> new TableCell<Card, Card>() {
            private final Button button = new Button("remover");

            {
                button.setPrefWidth(100);
                button.setPrefHeight(20);
            }

            @Override
            protected void updateItem(Card obj, boolean empty) {
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

    private void removeEntity(Card obj) {
        Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Tem certeza de que deseja excluir?");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (service == null) {
                throw new IllegalStateException("Service was null");
            }
            try {
                service.remove(obj);
                updateTableView();
            } catch (DbIntegrityException e) {
                System.out.println(e.getMessage());
                Alerts.showAlert("Error removing object", null, e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private void addDateComboBox(List<Card> list) {
        comboBoxDate.getItems().clear();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Set<String> uniqueYears = new HashSet<>(); // Conjunto para armazenar anos únicos

        for (Card card : list) {
            String year = sdf.format(card.getDate());

            if (uniqueYears.add(year)) { // Adiciona o ano ao conjunto e verifica se é único
                comboBoxDate.getItems().add(year);
            }
        }
    }

}
