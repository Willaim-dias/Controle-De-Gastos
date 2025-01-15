package view;

import config.DbException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import model.entities.Account;
import model.services.AccountServices;
import view.listeners.DataChangeListener;
import view.util.Alerts;
import view.util.Utils;

public class ContaFormController implements Initializable {

    private Account account;
    private AccountServices services;
    
    private final List<DataChangeListener> DataChangeListeners = new ArrayList<>();
    
    @FXML
    private TextField txtId;

    @FXML
    private TextField txtAccount;

    @FXML
    private TextField txtValue;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    @FXML
    public void onBtSaveAction(ActionEvent event) {
        if (account == null) {
            throw new IllegalStateException("Entity was null");
        }
        if (account == null) {
            throw new IllegalStateException("Service was null");
        }
        try {
            account = getFormData();
            services.saveOrUpdate(account);
            notifyDataChangeListeners();
            Utils.currentStage(event).close();
        } catch (DbException e) {
            Alerts.showAlert("Error saving object", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    public void onBtCancelAction(ActionEvent event) {
      Utils.currentStage(event).close();  
    }
    
    public void setAccount(Account account) {
        this.account = account;
    }
    
    public void setAccountService(AccountServices services) {
        this.services = services;
    }
    
    public void subscriberDataChangeListener(DataChangeListener  listener) {
        DataChangeListeners.add(listener);
    }
    
    public void updateFormData() {
        if (account == null) {
            throw new IllegalStateException("Entity was null");
        }
        txtId.setText(String.valueOf(account.getId()));
        txtAccount.setText(account.getAccount());
        txtValue.setText(String.valueOf(account.getValue()));
    }
    
    private Account getFormData() {
        Account obj = new Account();

        obj.setId(Utils.tryParseToInt(txtId.getText()));
        obj.setAccount(txtAccount.getText());
        obj.setValue(Utils.formatNumber(txtValue.getText()));
              
        return obj;
    }

    private void notifyDataChangeListeners() {
        for (DataChangeListener listener : DataChangeListeners) {
            listener.onDataChanged();
        }
    }
}
