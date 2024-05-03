package com.example.lab4_20200403;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lab4_20200403.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonIngreso.setOnClickListener(v->{
            if (conexionInternet()){
                Intent intent = new Intent(MainActivity.this, AppActivity.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(this, "No se pudo establecer una conexión con internet", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public boolean conexionInternet() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        boolean siu = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        return siu;
    }
}