package com.example.lab4_20200403.RetrofitHelpers;

import com.example.lab4_20200403.Objetos.CiudadGeo;
import com.example.lab4_20200403.Objetos.LugarClima;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeolocalizacionService {
    @GET("/geo/1.0/direct")
    Call<List<CiudadGeo>> getGeolocalizacionWithData(@Query("q") String q,
                                                    @Query("appid") String appid);
}
