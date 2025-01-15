package model.dao;

import java.util.List;
import model.entities.Card;

public interface CardDao {
    
    void insert(Card obj);
    void update(Card obj);
    void deleteById(Integer id);
    List<Card> findAll();
    
}
