package com.example.firebaseexperience.Model;

public class Models {

    String name, price, dec;

    public Models(String name, String price, String dec) {
        this.name = name;
        this.price = price;
        this.dec = dec;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDec() {
        return dec;
    }

    public void setDec(String dec) {
        this.dec = dec;
    }
}
