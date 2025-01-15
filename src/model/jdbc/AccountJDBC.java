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
import model.dao.AccountDao;
import model.entities.Account;
import view.util.Alerts;

public class AccountJDBC implements AccountDao{
    
    private Connection conn;
    
    public AccountJDBC(Connection conn) {
        this.conn = conn;
    }
    
    @Override
    public void insert(Account obj) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("INSERT INTO tb_account(account,value) VALUES (?,?)",Statement.RETURN_GENERATED_KEYS);
            st.setString(1, obj.getAccount());
            st.setDouble(2, obj.getValue());
            
            st.executeUpdate();          
        } catch (SQLException e) {
            Alerts.showAlert("Erro","",e.getMessage(), Alert.AlertType.ERROR);
            throw new DbException("Erro:"+e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }
    
    @Override
    public void update(Account obj) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("UPDATE tb_account SET account = ?,value = ? WHERE id = ?",Statement.RETURN_GENERATED_KEYS);
            st.setString(1, obj.getAccount());
            st.setDouble(2, obj.getValue());
            st.setInt(3, obj.getId());
            int rowsAffected = st.executeUpdate();
            
            if (rowsAffected > 0) {
               Alerts.showAlert("Info", "", "Atualizado com success", Alert.AlertType.INFORMATION); 
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }
    
    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("DELETE FROM tb_account WHERE id = ?");
            st.setInt(1, id);
            st.executeUpdate();
        } catch(SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }
    
    @Override
    public List<Account> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM tb_account");
            rs = st.executeQuery();
            
            List<Account> list = new ArrayList<>();
            while (rs.next()) {       
                Account account = new Account(rs.getInt("id"),rs.getString("account"),rs.getDouble("value"));
                list.add(account);
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
