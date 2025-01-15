package view;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Account;
import model.services.AccountServices;
import view.util.Alerts;

public class GraphicsViewController implements Initializable {

    private AccountServices service;
    private Double sum = 0.0;
    
    @FXML
    private Label labelValue;

    @FXML
    private PieChart pieChartExpenses;

    public void setAccountServices(AccountServices services) {
        this.service = services;
        addDataGraphic();
    }
    
    public void onBtShowHistory() {
        showWindow("/view/SpendingHistoryView.fxml","Historico de Gastos");
    }

    public void onBtShowSpendingForecast() {
        showWindow("/view/SpendingForecast.fxml","Previsao de Gastos");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    private boolean showValue = false;
    
    @FXML
    private void totalValueShow() {
        if (showValue) {
            labelValue.setText("");
            showValue = false;
        } else {
            labelValue.setText("R$ "+String.format("%.2f", sum));
            showValue = true;
        }
    }
    
    private void addDataGraphic() {
        if (service == null) {
            throw new IllegalStateException("Service was not initialized");
        }
        
        List<Account> list = service.findAll();

        ObservableList<PieChart.Data> pieCharData = FXCollections.observableArrayList();

        for (Account a : list) {
            sum += a.getValue();
            pieCharData.add(new PieChart.Data(a.getAccount(), a.getValue()));
        }

        pieCharData.forEach(data -> data.nameProperty().bind(
                Bindings.concat(
                        data.getName(), ": ", data.pieValueProperty()
                )
        ));

        pieChartExpenses.getData().addAll(pieCharData);
    }
    
    private void showWindow(String path,String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            AnchorPane anchorPane = loader.load();
            
            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            dialogStage.setScene(new Scene(anchorPane));
            dialogStage.setResizable(false);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();
        } catch (IOException e) {
            Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

}
