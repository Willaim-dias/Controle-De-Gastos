package config;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.scene.control.Alert;
import view.util.Alerts;


public class DB {

    private static Connection conn = null;
    
    public static Connection getConnection() {
        File file = new File("data_account.sqlite");

        if (file.exists()) {
            if (conn == null) {
                try {
                    conn = DriverManager.getConnection("jdbc:sqlite:data_account.sqlite");
                    return conn;
                } catch (SQLException ex) {
                }
            } else {
                return conn;
            }
        } else {
            Alerts.showAlert("Erro", "", "Banco de dados nao encontrado", Alert.AlertType.ERROR);
        }
        return null;
    }

    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
            Alerts.showAlert("Erro", "", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public static void closeStatement(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                Alerts.showAlert("Erro", "", e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                Alerts.showAlert("Erro", "", e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }
    
}
