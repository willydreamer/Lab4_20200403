package com.example.lab4_20200403.Navegation;

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

public class ClimaFragment extends Fragment {
    FragmentClimaBinding binding;
    ClimaService climaService;
    private String apikey = "792edf06f1f5ebcaf43632b55d8b03fe";
    List<LugarClima> lugaresClimas = new ArrayList<>();
    ClimaAdapter climaAdapter = new ClimaAdapter();
    //View Model
    private ClimaViewModel viewModel;

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
        climaService.getClimaWithData(lat,lon,appid).enqueue(new Callback<LugarClima>() {
            @Override
            public void onResponse(Call<LugarClima> call, Response<LugarClima> response) {
                if(response.isSuccessful()){
                    LugarClima place = response.body();
                    //Verificacion de la solicitud
                    Log.d("msg-test-ws-post","name: " + place.getName()
                                + " | temperatura maxima: " + place.getMain().getTemp());

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


}