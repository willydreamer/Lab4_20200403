package com.example.lab4_20200403.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab4_20200403.Objetos.CiudadGeo;
import com.example.lab4_20200403.R;

import java.util.ArrayList;
import java.util.List;

public class GeolocalizacionAdapter extends  RecyclerView.Adapter<GeolocalizacionAdapter.GeolocalizacionViewHolder> {
    private Context context;
    private List<CiudadGeo> listaCiudadesGeo;

    @NonNull
    @Override
    public GeolocalizacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.rv_geolocalizacion, parent, false);
        return new GeolocalizacionViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull GeolocalizacionAdapter.GeolocalizacionViewHolder holder, int position) {
        CiudadGeo ciudadGeo = listaCiudadesGeo.get(position);
        holder.tvcity.setText(String.format(ciudadGeo.getName()));
        holder.tvlat.setText(String.format("Lat: " + ciudadGeo.getLat()));
        holder.tvlon.setText(String.format("Lon: " +  ciudadGeo.getLon()));
    }

    public void setCiudadesGeo(List<CiudadGeo> listaCiudadesGeo) {
        this.listaCiudadesGeo = listaCiudadesGeo;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return listaCiudadesGeo.size();
    }

    public class GeolocalizacionViewHolder extends RecyclerView.ViewHolder {
        TextView tvcity, tvlat, tvlon;
        public GeolocalizacionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvcity = itemView.findViewById(R.id.ciudad);
            tvlat = itemView.findViewById(R.id.latitud);
            tvlon= itemView.findViewById(R.id.longitud);
        }
    }
}
