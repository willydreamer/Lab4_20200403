package com.example.lab4_20200403.Viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.lab4_20200403.Objetos.LugarClima;

import java.util.ArrayList;
import java.util.List;

public class ClimaViewModel extends ViewModel {
    private MutableLiveData<List<LugarClima>> lugaresClima = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<LugarClima>> getLugaresClima() {
        return lugaresClima;
    }

    public void addLugarClima(LugarClima lugar) {
        List<LugarClima> currentList = lugaresClima.getValue();
        if (currentList == null) currentList = new ArrayList<>();
        currentList.add(lugar);
        lugaresClima.postValue(currentList);
    }
}
