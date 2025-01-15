package model.services;

import java.util.List;
import model.dao.AccountDao;
import model.dao.DaoFactory;
import model.entities.Account;
import model.entities.Extract;
import model.dao.ExtractDao;

public class ExtractServices {
    
    private ExtractDao dao = DaoFactory.createHistoryDao();
    
    public List<Extract> findAll() {
        return dao.findAll();
    }
    
    public void save(Extract obj) {
        if (obj.getId() == null) {
            dao.insert(obj);
        } 
    }
    
    public void remove(Extract obj) {
        dao.deleteById(obj.getId());
    }

}
