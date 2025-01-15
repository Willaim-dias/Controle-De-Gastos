package model.services;

import java.util.List;
import model.dao.AccountDao;
import model.dao.DaoFactory;
import model.entities.Account;

public class AccountServices {
    
    private AccountDao dao = DaoFactory.createAccountDao();
    
    public List<Account> findAll() {
        return dao.findAll();
    }
    
    public void saveOrUpdate(Account obj) {
        if (obj.getId() == null) {
            dao.insert(obj);
        } else {
            dao.update(obj);
        }
    }
    
    public void remove(Account obj) {
        dao.deleteById(obj.getId());
    }
 
}
