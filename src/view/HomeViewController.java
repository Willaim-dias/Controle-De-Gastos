package view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import model.services.AccountServices;
import model.services.InstallmentsServices;

public class HomeViewController implements Initializable {

    @FXML
    private AnchorPane panelGeneral;

    public void onMenuItemContaAction() {
        loadPanel("/view/ContaView.fxml", (ContaViewController controller) -> {
            controller.setAccountServices(new AccountServices());
            controller.updateTableView();
        });
    }

    public void onMenuItemGraphicsAction(){
        loadPanel("/view/GraphicsView.fxml", (GraphicsViewController controller) -> {
            controller.setAccountServices(new AccountServices());
        });
    }
    
    public void onMenuItemInstallmentsAction() {
        loadPanel("/view/InstallmentsView.fxml", (InstallmentsViewController controller) -> {
            controller.setInstallmentsServices(new InstallmentsServices());
        });
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadPanel("/view/GraphicsView.fxml", (GraphicsViewController controller) -> {
            controller.setAccountServices(new AccountServices());
        });
    }
    
    private synchronized <T> void loadPanel(String path, Consumer<T> initializingAction) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Parent newPanel = loader.load();
            
            panelGeneral.getChildren().clear();
            panelGeneral.getChildren().add(newPanel);
        
            T conttroler = loader.getController();
            initializingAction.accept(conttroler);
        } catch (IOException e) { 
            System.out.println(e.getMessage());
        }
    }

}
