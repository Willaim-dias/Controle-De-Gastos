package model.jdbc;

import config.DB;
import config.DbException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert;
import model.entities.Extract;
import view.util.Alerts;
import model.dao.ExtractDao;

public class ExtractJDBC implements ExtractDao{

    private Connection conn;
    
    public ExtractJDBC(Connection conn) {
        this.conn = conn;
    }
    
    @Override
    public void insert(Extract obj) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("INSERT INTO tb_history (data_list, date_save) VALUES (?, ?)",Statement.RETURN_GENERATED_KEYS);
            st.setString(1, obj.getDataList());
            st.setDate(2, new java.sql.Date(obj.getDate().getTime()));
            
            int rowsAffected = st.executeUpdate();
            
            if (rowsAffected > 0) {
                Alerts.showAlert("Info", "", "Salvo com success", Alert.AlertType.INFORMATION);
            }
        } catch (SQLException e) {
            throw new DbException("Erro:"+e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }
    
    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("DELETE FROM tb_history WHERE id = ?");
            st.setInt(1, id);
            st.executeUpdate();
        } catch(SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public List<Extract> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM tb_history");
            rs = st.executeQuery();
            
            List<Extract> list = new ArrayList<>();
            while (rs.next()) {
                Extract history = new Extract(rs.getInt("id"),rs.getString("data_list"),rs.getDate("date_save"));
                list.add(history);
            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }
    
    
}
