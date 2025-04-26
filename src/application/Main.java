package application;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {

    private static Scene mainScene;
    
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/HomeView.fxml"));
            AnchorPane anchorPane = loader.load();
    
            mainScene = new Scene(anchorPane);
            primaryStage.setScene(mainScene);
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/imgs/icons/processDevelopment.png")));
            primaryStage.setResizable(false);
            primaryStage.setTitle("Home");
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("Erro: "+ e.getMessage());
        }
    }

    public static Scene getMainScene() {
        return mainScene;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
