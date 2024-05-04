package com.example.lab4_20200403.RetrofitHelpers;

import com.example.lab4_20200403.Objetos.LugarClima;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ClimaService {
    @GET("/data/2.5/weather")
    Call<LugarClima> getClimaWithData(@Query("lat") String lat,
                                           @Query("lon") String lon,
                                           @Query("appid") String appid);
}
