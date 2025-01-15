package model.entities;

import java.util.Date;

public class Extract {
    
    private Integer id;
    private String DataList;
    private Date date;

    public Extract() {
    }

    public Extract(Integer id, String DataList, Date date) {
        this.id = id;
        this.DataList = DataList;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDataList() {
        return DataList;
    }

    public void setDataList(String DataList) {
        this.DataList = DataList;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }  
}
