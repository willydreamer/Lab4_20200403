package com.example.lab4_20200403.Navegation;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.lab4_20200403.R;
import com.example.lab4_20200403.databinding.FragmentGeolocalizacionBinding;

public class GeolocalizacionFragment extends Fragment {
    FragmentGeolocalizacionBinding binding;

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

        return binding.getRoot();
    }
}