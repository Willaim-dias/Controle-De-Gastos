package view;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import model.entities.Installments;
import model.services.InstallmentsServices;
import view.listeners.DataChangeListener;
import view.util.Utils;

public class InstallmentsFormController implements Initializable {

    private Installments installments;
    private InstallmentsServices service;

    private final List<DataChangeListener> DataChangeListeners = new ArrayList<>();
    
    @FXML
    private DatePicker txtDatePayment;

    @FXML
    private TextField txtInstallments;

    @FXML
    private TextField txtInstallmentsPaid;

    @FXML
    private TextField txtItem;

    @FXML
    private TextField txtTotalValue;

    public void setInstallments(Installments installments) {
        this.installments = installments;
    }

    public void setService(InstallmentsServices services) {
        this.service = services;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    public void onBtUpdateData(ActionEvent event) {
        service.saveOrUpdate(getFormData());
        notifyDataChangeListeners();
        Utils.currentStage(event).close();
    }

    public void updateFormData() {
        if (installments == null) {
            throw new IllegalStateException("Entity was null");
        }

        txtItem.setText(installments.getItem());
        txtTotalValue.setText(String.valueOf(installments.getTotalValue()));
        txtInstallments.setText(String.valueOf(installments.getInstallments()));
        txtInstallmentsPaid.setText(String.valueOf(installments.getInstallmentsPaid()));

        if (installments.getLastPayment() != null) {

            java.sql.Date lastPaymentDate = (java.sql.Date) installments.getLastPayment();
            LocalDate localDate = lastPaymentDate.toLocalDate();

            txtDatePayment.setValue(localDate);
        } else {
            txtDatePayment.setValue(null);
        }
    }

    private Installments getFormData() {
        installments.setItem(txtItem.getText());
        installments.setTotalValue(Utils.tryParseToDouble(txtTotalValue.getText()));
        installments.setInstallments(Integer.valueOf(txtInstallments.getText()));
        installments.setInstallmentsPaid(Integer.valueOf(txtInstallmentsPaid.getText()));
        
        LocalDate localDate = txtDatePayment.getValue();
        if (localDate != null) {
           Instant instant = txtDatePayment.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
           installments.setLastPayment(Date.from(instant)); 
        }
        
        return installments;
    }

    public void subscriberDataChangeListener(DataChangeListener  listener) {
        DataChangeListeners.add(listener);
    }
    
    private void notifyDataChangeListeners() {
        for (DataChangeListener listener : DataChangeListeners) {
            listener.onDataChanged();
        }
    }
}
