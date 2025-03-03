package model.entities;

import java.util.Date;

public class Installments {

    private Integer id;
    private String item;
    private Double totalValue;
    private Integer installments;
    private Double installmentsValue;
    private Integer installmentsPaid;
    private Integer remainingInstallments;
    private Date lastPayment;

    public Installments() {
    }
    
    public Installments(Integer id, String item, Double totalValue, Integer installments,Integer installmentPaid, Date lastPayment) {
        this.id = id;
        this.item = item;
        this.totalValue = totalValue;
        this.installments = installments;
        this.installmentsPaid = installmentPaid;
        this.lastPayment = lastPayment;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Double totalValue) {
        this.totalValue = totalValue;
    }

    public Integer getInstallments() {
        return installments;
    }

    public void setInstallments(Integer installments) {
        this.installments = installments;
    }

    public Double getInstallmentsValue() {
        return installmentsValue;
    }

    public void setInstallmentsValue(Double installmentsValue) {
        this.installmentsValue = installmentsValue;
    }

    public Integer getInstallmentsPaid() {
        return installmentsPaid;
    }

    public void setInstallmentsPaid(Integer InstallmentsPaid) {
        this.installmentsPaid = InstallmentsPaid;
    }

    public Integer getRemainingInstallments() {
        return remainingInstallments;
    }

    public void setRemainingInstallments(Integer remainingInstallments) {
        this.remainingInstallments = remainingInstallments;
    }

    public Date getLastPayment() {
        return lastPayment;
    }

    public void setLastPayment(Date lastPayment) {
        this.lastPayment = lastPayment;
    }
    
    public double installmentValue(double value,int number) {
        return Math.round(value/number);
    }
    
    public int remainingInstallments(int installmentsA,int installmentsB) {
        return installmentsA-installmentsB;
    }
}
