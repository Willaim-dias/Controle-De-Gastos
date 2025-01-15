package model.services;

import java.util.List;
import model.dao.DaoFactory;
import model.dao.InstallmentsDao;
import model.entities.Installments;

public class InstallmentsServices {

    private InstallmentsDao dao = DaoFactory.createInstallmentsDao();
    
    public List<Installments> findAll() {
        return dao.findAll();
    }

    public void saveOrUpdate(Installments obj) {
        if (obj.getId() == null) {
            dao.insert(obj);
        } else {
            dao.update(obj);
        }
    }
    
    public void remove(int id) {
        dao.deleteById(id);
    }
}
