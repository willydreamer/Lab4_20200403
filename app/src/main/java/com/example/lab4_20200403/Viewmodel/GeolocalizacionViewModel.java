package com.example.lab4_20200403.Viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.lab4_20200403.Objetos.CiudadGeo;

import java.util.ArrayList;
import java.util.List;

public class GeolocalizacionViewModel extends ViewModel {
    private MutableLiveData<List<CiudadGeo>> ciudadesGeo = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<CiudadGeo>> getCiudadesGeo() {
        return ciudadesGeo;
    }

    public void addGeolocalizacion(CiudadGeo city) {
        List<CiudadGeo> currentList = ciudadesGeo.getValue();
        if (currentList == null) currentList = new ArrayList<>();
        currentList.add(city);
        ciudadesGeo.postValue(currentList);
    }
    // Metodo para eliminar el ultimo item de a lista
    public void removeLastGeolocalizacion() {
        List<CiudadGeo> currentList = ciudadesGeo.getValue();
        if (currentList != null && !currentList.isEmpty()) {
            currentList.remove(currentList.size() - 1);
            ciudadesGeo.setValue(currentList);
        }
    }
}
