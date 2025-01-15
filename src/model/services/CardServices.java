package model.services;

import java.util.List;
import model.dao.CardDao;
import model.dao.DaoFactory;
import model.entities.Card;

public class CardServices {
    
    private CardDao dao = DaoFactory.createCardDao();
    
    public List<Card> findAll() {
        return dao.findAll();
    }
    
    public void saveOrUpdate(Card obj) {
        if (obj.getId() == null) {
            dao.insert(obj);
        } else {
            dao.update(obj);
        }
    }
    
    public void remove(Card obj) {
        dao.deleteById(obj.getId());
    }
}
