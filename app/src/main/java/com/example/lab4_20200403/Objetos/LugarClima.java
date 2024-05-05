package com.example.lab4_20200403.Objetos;

public class LugarClima {
    private String name;
    private Main main;
    private String direccionViento;

    public LugarClima(String name, Main main, String direccionViento) {
        this.name = name;
        this.main = main;
        this.direccionViento = direccionViento;
    }

    public String getDireccionViento() {
        return direccionViento;
    }

    public void setDireccionViento(String direccionViento) {
        this.direccionViento = direccionViento;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }
}
