package com.example.lab4_20200403.Navegation;

import static android.content.Context.SENSOR_SERVICE;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.DocumentsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.lab4_20200403.Adapters.ClimaAdapter;
import com.example.lab4_20200403.Objetos.LugarClima;
import com.example.lab4_20200403.R;
import com.example.lab4_20200403.RetrofitHelpers.ClimaService;
import com.example.lab4_20200403.Viewmodel.ClimaViewModel;
import com.example.lab4_20200403.databinding.FragmentClimaBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClimaFragment extends Fragment implements SensorEventListener {
    FragmentClimaBinding binding;
    ClimaService climaService;
    private String apikey = "792edf06f1f5ebcaf43632b55d8b03fe";
    List<LugarClima> lugaresClimas = new ArrayList<>();
    ClimaAdapter climaAdapter = new ClimaAdapter();
    //View Model
    private ClimaViewModel viewModel;

    //Sensores
    private SensorManager mSensorManager;
    private Sensor magnetometro;
    private float lastXMag = 0.0f;
    private float lastYMag = 0.0f;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentClimaBinding.inflate(inflater, container, false);

        //Navegación
        NavController navController = NavHostFragment.findNavController(ClimaFragment.this);
        Button botonGeo =getActivity().findViewById(R.id.buttonGeolocalizacion);
        Button botonClima =getActivity().findViewById(R.id.buttonClima);
        botonGeo.setOnClickListener( view -> {
            navController.navigate(R.id.action_climaFragment_to_geolocalizacionFragment);

        });
        botonClima.setOnClickListener( view -> {
            navController.navigate(R.id.climaFragment);
        });

        // Retrofit
        createRetrofitService();

        // Boton de buscar inicia la consulta por la API
        binding.buttonBuscar.setOnClickListener(view -> {
            String latitud = binding.latitud.getText().toString();
            String longitud = binding.longitud.getText().toString();
            cargarClimaLugar(latitud, longitud, apikey);

            // Se debe deshabilitar los botones de buscar y cambiar de fragmento
            binding.buttonBuscar.setEnabled(false);
            botonGeo.setEnabled(false);
            botonClima.setEnabled(false);

        });
        climaAdapter.setContext(getContext());
        climaAdapter.setLugaresClima(lugaresClimas);

        binding.rvClima.setAdapter(climaAdapter);
        binding.rvClima.setLayoutManager(new LinearLayoutManager(getActivity()));

        // View Model
        viewModel = new ViewModelProvider(requireActivity()).get(ClimaViewModel.class);
        viewModel.getLugaresClima().observe(getViewLifecycleOwner(), lugares -> {
            climaAdapter.setLugaresClima(lugares);
            climaAdapter.notifyDataSetChanged();
        });

        // Sensores

        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        magnetometro = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);


        return binding.getRoot();
    }

    public void createRetrofitService() {
        climaService = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ClimaService.class);
    }
    void cargarClimaLugar(String lat,String lon, String appid){
        // Se actualizan las variables del sensor :)
        float xMag = lastXMag;
        float yMag = lastYMag;
        double degrees = Math.toDegrees(Math.atan2(yMag, xMag));
        degrees = (degrees + 360) % 360;
        String cardinalDirection = getCardinalDirection(degrees);
        Log.d("Go bati", cardinalDirection);

        climaService.getClimaWithData(lat,lon,appid).enqueue(new Callback<LugarClima>() {
            @Override
            public void onResponse(Call<LugarClima> call, Response<LugarClima> response) {
                if(response.isSuccessful()){
                    LugarClima place = response.body();

                    // Setear la direccion del viento
                    place.setDireccionViento(cardinalDirection);

                    //Verificacion de la solicitud
                    Log.d("msg-test-ws-post", "name: " + place.getName() +
                            " | temperatura maxima: " + place.getMain().getTemp() +
                            " | dirección del viento: " + place.getDireccionViento());

                    lugaresClimas.add(place);
                    climaAdapter.setLugaresClima(lugaresClimas);
                    climaAdapter.notifyDataSetChanged();
                    viewModel.addLugarClima(response.body());

                    //Habilitacion de los botones
                    getActivity().findViewById(R.id.buttonClima).setEnabled(true);
                    getActivity().findViewById(R.id.buttonGeolocalizacion).setEnabled(true);
                    binding.buttonBuscar.setEnabled(true);

                }
            }
            @Override
            public void onFailure(Call<LugarClima> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, magnetometro, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onStop(){
        super.onStop();
        SensorManager sensorManager = (SensorManager) requireActivity().getSystemService(SENSOR_SERVICE);
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();
        if (sensorType == Sensor.TYPE_MAGNETIC_FIELD) {
            lastXMag = event.values[0];
            lastYMag = event.values[1];
        }
    }

    private String getCardinalDirection(double degrees) {
        if (degrees >= 337.5 || degrees < 22.5)
            return "Norte";
        if (degrees >= 22.5 && degrees < 67.5)
            return "Noreste";
        if (degrees >= 67.5 && degrees < 112.5)
            return "Este";
        if (degrees >= 112.5 && degrees < 157.5)
            return "Sureste";
        if (degrees >= 157.5 && degrees < 202.5)
            return "Sur";
        if (degrees >= 202.5 && degrees < 247.5)
            return "Suroeste";
        if (degrees >= 247.5 && degrees < 292.5)
            return "Oeste";
        if (degrees >= 292.5 && degrees < 337.5)
            return "Noroeste";
        return "Indefinido";
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}