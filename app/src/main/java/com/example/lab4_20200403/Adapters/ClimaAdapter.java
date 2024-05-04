package com.example.lab4_20200403.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab4_20200403.Objetos.LugarClima;
import com.example.lab4_20200403.R;

import java.util.List;

public class ClimaAdapter extends RecyclerView.Adapter<ClimaAdapter.ClimaViewHolder> {
    private Context context;
    private List<LugarClima> listaLugaresClimas;

    @NonNull
    @Override
    public ClimaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.rv_clima, parent, false);
        return new ClimaViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ClimaAdapter.ClimaViewHolder holder, int position) {
        LugarClima lugarClima = listaLugaresClimas.get(position);
        String cityName = lugarClima.getName();
        if (cityName == null || cityName.isEmpty()) {
            holder.tvcity.setText("Desconocido");
        } else {
            holder.tvcity.setText(cityName);
        }
        holder.tvtemp.setText(String.format("%s K", lugarClima.getMain().getTemp_min()));
        holder.tvtempMin.setText(String.format("%s K", lugarClima.getMain().getTemp_min()));
        holder.tvtempMax.setText(String.format("%s K", lugarClima.getMain().getTemp_max()));
        holder.tvviento.setText("Viento: Oeste");
    }

    @Override
    public int getItemCount() {
        return listaLugaresClimas.size();
    }
    public void setContext(Context context) {
        this.context = context;
    }

    public void setLugaresClima(List<LugarClima> lugaresClima) {
        this.listaLugaresClimas = lugaresClima;
    }

    public class ClimaViewHolder extends RecyclerView.ViewHolder {

        TextView tvcity, tvtemp, tvtempMin, tvtempMax, tvviento;
        public ClimaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvcity = itemView.findViewById(R.id.ciudad);
            tvtemp = itemView.findViewById(R.id.temperatura);
            tvtempMin = itemView.findViewById(R.id.textMinimo);
            tvtempMax = itemView.findViewById(R.id.textMaximo);
            tvviento = itemView.findViewById(R.id.textViento);
        }
    }
}


