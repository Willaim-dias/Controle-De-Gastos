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
import model.dao.InstallmentsDao;
import model.entities.Installments;
import view.util.Alerts;

public class InstallmentsJDBC implements InstallmentsDao {

    private Connection conn;

    public InstallmentsJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Installments obj) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("INSERT INTO tb_installments(item,total_value,number_installment,installment_paid,last_payment) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            st.setString(1, obj.getItem());
            st.setDouble(2, obj.getTotalValue());
            st.setInt(3, obj.getInstallments());
            st.setInt(4, obj.getInstallmentsPaid());
            
            if (obj.getLastPayment() != null) {
                st.setDate(5, new java.sql.Date(obj.getLastPayment().getTime()));
            }
            
            st.executeUpdate();
        } catch (SQLException e) {
            Alerts.showAlert("Erro", "", e.getMessage(), Alert.AlertType.ERROR);
            throw new DbException("Erro:" + e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Installments obj) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("UPDATE tb_installments SET item = ?,total_value = ?, number_installment = ?, installment_paid = ?, last_payment = ? WHERE id = ?", Statement.RETURN_GENERATED_KEYS);
            st.setString(1, obj.getItem());
            st.setDouble(2, obj.getTotalValue());
            st.setInt(3, obj.getInstallments());
            st.setInt(4, obj.getInstallmentsPaid());
            
            if (obj.getLastPayment() != null) {
                st.setDate(5, new java.sql.Date(obj.getLastPayment().getTime()));
            }
            
            st.setInt(6, obj.getId());
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
            st = conn.prepareStatement("DELETE FROM tb_installments WHERE id = ?");
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public List<Installments> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM tb_installments");

            rs = st.executeQuery();

            List<Installments> list = new ArrayList<>();
            while (rs.next()) {
                Installments installments = new Installments(rs.getInt("id"), rs.getString("item"), rs.getDouble("total_value"), rs.getInt("number_installment"), rs.getInt("installment_paid"), rs.getDate("last_payment"));
                list.add(installments);
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
