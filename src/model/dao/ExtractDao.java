package model.dao;

import java.util.List;
import model.entities.Extract;

public interface ExtractDao {
    
    void insert(Extract obj);
    void deleteById(Integer id);
    List<Extract> findAll();
    
}
