package com.example.lab4_20200403.Objetos;

public class Main {
    private String temp_min;
    private String temp_max;
    private String temp;

    public Main(String temp_min) {
        this.temp_min = temp_min;
    }

    public String getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(String temp_min) {
        this.temp_min = temp_min;
    }

    public String getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(String temp_max) {
        this.temp_max = temp_max;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }
}
