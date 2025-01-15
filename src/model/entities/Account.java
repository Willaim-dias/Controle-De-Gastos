package model.entities;

public class Account {
    
    private Integer id;
    private String account;
    private Double Value;
    
    public Account(){
    }

    public Account(Integer id, String account, Double Value) {
        this.id = id;
        this.account = account;
        this.Value = Value;
    }
 
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Double getValue() {
        return Value;
    }

    public void setValue(Double Value) {
        this.Value = Value;
    }
    
}
