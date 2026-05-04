package com.example.picana_apk.presentation.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.picana_apk.R;
import com.example.picana_apk.data.model.Plato;
import java.util.List;
import java.util.Locale;

public class PlatoAdapter extends RecyclerView.Adapter<PlatoAdapter.PlatoViewHolder> {

    private static final String BASE_URL_STORAGE = "https://dzgcngblutosxjajlxov.supabase.co/storage/v1/object/public/productos/";

    private static final RequestOptions GLIDE_OPTIONS = new RequestOptions()
            .format(DecodeFormat.PREFER_RGB_565)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .override(300, 300)
            .centerCrop();

    private final AsyncListDiffer<Plato> mDiffer = new AsyncListDiffer<>(this, DIFF_CALLBACK);

    private static final DiffUtil.ItemCallback<Plato> DIFF_CALLBACK = new DiffUtil.ItemCallback<Plato>() {
        @Override
        public boolean areItemsTheSame(@NonNull Plato oldItem, @NonNull Plato newItem) {
            return oldItem.getStableId() == newItem.getStableId();
        }
        @Override
        public boolean areContentsTheSame(@NonNull Plato oldItem, @NonNull Plato newItem) {
            return oldItem.getNombre().equals(newItem.getNombre()) && 
                   oldItem.getPrecio() == newItem.getPrecio() &&
                   String.valueOf(oldItem.getImagenUrl()).equals(String.valueOf(newItem.getImagenUrl()));
        }
    };

    public PlatoAdapter() {
        setHasStableIds(true);
    }

    public void updateData(List<Plato> nuevosPlatos) {
        mDiffer.submitList(nuevosPlatos);
    }

    @Override
    public long getItemId(int position) {
        return mDiffer.getCurrentList().get(position).getStableId();
    }

    @NonNull
    @Override
    public PlatoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plato, parent, false);
        return new PlatoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlatoViewHolder holder, int position) {
        Plato plato = mDiffer.getCurrentList().get(position);
        
        holder.tvNombre.setText(plato.getNombre());
        holder.tvPrecio.setText(String.format(Locale.getDefault(), "S/ %.2f", plato.getPrecio()));
        
        if (plato.getPeso() > 0) {
            holder.tvPeso.setVisibility(View.VISIBLE);
            holder.tvPeso.setText(plato.getPeso() + " gr.");
        } else {
            holder.tvPeso.setVisibility(View.GONE);
        }

        String urlFinal = plato.getImagenUrl();
        if (urlFinal != null && !urlFinal.isEmpty()) {
            if (urlFinal.startsWith("http")) {
                // Corrección para links de Google Drive: convertir export=view a direct link
                if (urlFinal.contains("drive.google.com")) {
                    urlFinal = urlFinal.replace("export=view&", "");
                }
            } else {
                // Para Supabase Storage, no usamos toLowerCase() para evitar errores con nombres de archivo
                urlFinal = BASE_URL_STORAGE + urlFinal;
            }
        }

        Glide.with(holder.imgPlato.getContext())
                .load(urlFinal)
                .apply(GLIDE_OPTIONS)
                .placeholder(R.drawable.logo_pincana_v2)
                .error(R.drawable.img_plato_ejemplo)
                .into(holder.imgPlato);
    }

    @Override
    public int getItemCount() {
        return mDiffer.getCurrentList().size();
    }

    public static class PlatoViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPlato;
        TextView tvNombre, tvPrecio, tvPeso;

        public PlatoViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPlato = itemView.findViewById(R.id.imgPlato);
            tvNombre = itemView.findViewById(R.id.tvNombrePlato);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            tvPeso = itemView.findViewById(R.id.tvPeso);
        }
    }
}
