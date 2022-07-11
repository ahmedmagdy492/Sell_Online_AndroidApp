package com.magdyradwan.sellonline.responsemodels;

public class TokenResponseModel {
    private long exp;
    private String Name;


    public long getExp() {
        return exp;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
