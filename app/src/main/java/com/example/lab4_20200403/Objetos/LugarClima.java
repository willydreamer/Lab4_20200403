package com.example.lab4_20200403.Objetos;

public class LugarClima {
    private String name;
    private Main main;

    public LugarClima(String name, Main main) {
        this.name = name;
        this.main = main;
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
