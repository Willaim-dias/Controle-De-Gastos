package model.dao;

import java.util.List;
import model.entities.Installments;


public interface InstallmentsDao {
    
    void insert(Installments obj);
    void update(Installments obj);
    void deleteById(Integer id);
    List<Installments> findAll();
}
