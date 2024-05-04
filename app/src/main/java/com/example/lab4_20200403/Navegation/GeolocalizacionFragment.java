package com.example.lab4_20200403.Navegation;

import static android.content.Context.SENSOR_SERVICE;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.lab4_20200403.Adapters.GeolocalizacionAdapter;
import com.example.lab4_20200403.Objetos.CiudadGeo;
import com.example.lab4_20200403.Objetos.CiudadGeo;
import com.example.lab4_20200403.R;
import com.example.lab4_20200403.RetrofitHelpers.ClimaService;
import com.example.lab4_20200403.RetrofitHelpers.GeolocalizacionService;
import com.example.lab4_20200403.Viewmodel.ClimaViewModel;
import com.example.lab4_20200403.Viewmodel.GeolocalizacionViewModel;
import com.example.lab4_20200403.databinding.FragmentGeolocalizacionBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GeolocalizacionFragment extends Fragment implements SensorEventListener {
    FragmentGeolocalizacionBinding binding;
    GeolocalizacionService geolocalizacionService;
    private String apikey = "8dd6fc3be19ceb8601c2c3e811c16cf1";
    List<CiudadGeo> ciudadGeoList = new ArrayList<>();
    GeolocalizacionAdapter geolocalizacionAdapter = new GeolocalizacionAdapter();
    //View Model
    private GeolocalizacionViewModel viewModel;

    //Sensores
    private SensorManager mSensorManager;
    private Sensor accelerometer;
    // Validacion de la existencia de un dialogo abierto
    private boolean dialogoAbierto = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =  FragmentGeolocalizacionBinding.inflate(inflater, container, false);

        //Navegación
        NavController navController = NavHostFragment.findNavController(GeolocalizacionFragment.this);
        Button botonGeo =getActivity().findViewById(R.id.buttonGeolocalizacion);
        Button botonClima =getActivity().findViewById(R.id.buttonClima);
        botonGeo.setOnClickListener( view -> {
            navController.navigate(R.id.geolocalizacionFragment);

        });
        botonClima.setOnClickListener( view -> {
            navController.navigate(R.id.action_geolocalizacionFragment_to_climaFragment);

        });

        // Retrofit
        createRetrofitService();

        // Boton de buscar inicia la consulta por la API
        binding.buttonBuscar.setOnClickListener(view -> {
            String ciudad = binding.ciudad.getText().toString();
            cargarGeolocalizacionLugar(ciudad, apikey);

            // Se debe deshabilitar los botones de buscar y cambiar de fragmento
            binding.buttonBuscar.setEnabled(false);
            botonGeo.setEnabled(false);
            botonClima.setEnabled(false);

        });

        geolocalizacionAdapter.setContext(getContext());
        geolocalizacionAdapter.setCiudadesGeo(ciudadGeoList);

        binding.rvGeolocalizacion.setAdapter(geolocalizacionAdapter);
        binding.rvGeolocalizacion.setLayoutManager(new LinearLayoutManager(getActivity()));

        // View Model
        viewModel = new ViewModelProvider(requireActivity()).get(GeolocalizacionViewModel.class);
        viewModel.getCiudadesGeo().observe(getViewLifecycleOwner(), lugares -> {
            geolocalizacionAdapter.setCiudadesGeo(lugares);
            geolocalizacionAdapter.notifyDataSetChanged();
        });

        // Sensores

        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        return binding.getRoot();
    }
    public void createRetrofitService() {
        geolocalizacionService = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GeolocalizacionService.class);
    }

    void cargarGeolocalizacionLugar(String city, String appid) {
        geolocalizacionService.getGeolocalizacionWithData(city, appid).enqueue(new Callback<List<CiudadGeo>>() {
            @Override
            public void onResponse(Call<List<CiudadGeo>> call, Response<List<CiudadGeo>> response) {
                if (response.isSuccessful()) {
                    List<CiudadGeo> places = response.body();

                    CiudadGeo firstPlace = places.get(0);
                    Log.d("msg-test-ws-post", "name: " + firstPlace.getName() +
                            " | latitud: " + firstPlace.getLat());

                    ciudadGeoList.add(firstPlace);
                    Log.d("Debug", "Lista actualizada: Tamaño=" + ciudadGeoList.size());
                    geolocalizacionAdapter.setCiudadesGeo(ciudadGeoList);
                    geolocalizacionAdapter.notifyDataSetChanged();

                    viewModel.addGeolocalizacion(firstPlace);

                    //Habilitacion de los botones
                    getActivity().findViewById(R.id.buttonClima).setEnabled(true);
                    getActivity().findViewById(R.id.buttonGeolocalizacion).setEnabled(true);
                    binding.buttonBuscar.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<List<CiudadGeo>> call, Throwable t) {
                t.printStackTrace();
                Log.d("msg-test-ws-post", "error");
            }
        });
    }

    // Metodos para el sensor

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
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
        if(sensorType == Sensor.TYPE_ACCELEROMETER){
            float xAcc = event.values[0];
            float yAcc = event.values[1];
            float zAcc = event.values[2];

            double aceleracion= Math.sqrt(Math.pow(xAcc, 2) + Math.pow(yAcc, 2)+ Math.pow(zAcc, 2));

            Log.d("infoAppx",String.valueOf(xAcc));
            Log.d("infoAppy",String.valueOf(yAcc));
            Log.d("infoAppz",String.valueOf(zAcc));
            Log.d("Aceleracion",String.valueOf(aceleracion));

            // Setie la aceleracion en 11 m/s^2 porque 15 m/s^2 era dificil de lograr :)
            if (aceleracion > 11.0) {
                showUndoDialog();
            }
        }
    }


    private void showUndoDialog() {
        if (getActivity() != null && isAdded() && !ciudadGeoList.isEmpty() && !dialogoAbierto) {
            dialogoAbierto = true;
            new AlertDialog.Builder(getActivity())
                    .setTitle("Deshacer acción")
                    .setMessage("¿Deseas eliminar la última ubicación?")
                    .setPositiveButton("Deshacer", (dialog, which) -> {
                        viewModel.removeLastGeolocalizacion();
                        dialogoAbierto = false;
                    })
                    .setNegativeButton("Cancelar", (dialog, which) -> {
                        dialog.dismiss();
                        dialogoAbierto = false;
                    })
                    .setOnDismissListener(dialog -> dialogoAbierto = false)
                    .show();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}