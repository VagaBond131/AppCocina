package com.example.picana_apk.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.picana_apk.R;
import com.example.picana_apk.model.Plato;
import java.util.List;

public class PlatoAdapter extends RecyclerView.Adapter<PlatoAdapter.PlatoViewHolder> {

    private List<Plato> listaPlatos;

    public PlatoAdapter(List<Plato> listaPlatos) {
        this.listaPlatos = listaPlatos;
    }

    @NonNull
    @Override
    public PlatoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plato, parent, false);
        return new PlatoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlatoViewHolder holder, int position) {
        Plato plato = listaPlatos.get(position);
        holder.tvNombre.setText(plato.getNombre());
        holder.tvPrecio.setText("S/ " + plato.getPrecio());
    }

    @Override
    public int getItemCount() {
        return listaPlatos != null ? listaPlatos.size() : 0;
    }

    public static class PlatoViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPlato;
        TextView tvNombre, tvPrecio;

        public PlatoViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPlato = itemView.findViewById(R.id.imgPlato);
            tvNombre = itemView.findViewById(R.id.tvNombrePlato);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
        }
    }
}