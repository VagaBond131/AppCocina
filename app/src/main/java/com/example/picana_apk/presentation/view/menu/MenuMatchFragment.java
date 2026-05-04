package com.example.picana_apk.presentation.view.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.example.picana_apk.R;
import com.example.picana_apk.data.model.Plato;
import com.example.picana_apk.presentation.viewmodel.MenuViewModel;

public class MenuMatchFragment extends Fragment {

    private MenuViewModel viewModel;
    private TextView tvNombrePlato;
    private ImageView imgPlato;
    private static final String BASE_URL_STORAGE = "https://dzgcngblutosxjajlxov.supabase.co/storage/v1/object/public/productos/";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu_match, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvNombrePlato = view.findViewById(R.id.tvNombrePlato);
        imgPlato = view.findViewById(R.id.imgPlato);

        // Usar requireActivity() para compartir el mismo ViewModel que la actividad
        viewModel = new ViewModelProvider(requireActivity()).get(MenuViewModel.class);

        // Si la lista está vacía, forzamos una carga inicial
        if (viewModel.getPlatos().getValue() == null || viewModel.getPlatos().getValue().isEmpty()) {
            viewModel.cargarPlatosPorCategoria("Parrillas");
        }

        viewModel.getPlatoActual().observe(getViewLifecycleOwner(), plato -> {
            if (plato != null) {
                tvNombrePlato.setText(plato.getNombre().toUpperCase());
                
                String urlFinal = plato.getImagenUrl();
                if (urlFinal != null && !urlFinal.startsWith("http") && !urlFinal.isEmpty()) {
                    // CORRECCIÓN SEGÚN BD: Se convierte a minúsculas para que coincida con el storage
                    urlFinal = BASE_URL_STORAGE + urlFinal.toLowerCase();
                }

                // Cargar la imagen con Glide
                Glide.with(this)
                        .load(urlFinal)
                        .placeholder(R.drawable.logo_pincana_v2)
                        .error(R.drawable.img_plato_ejemplo) 
                        .centerCrop()
                        .into(imgPlato);
            }
        });

        // Configurar botones
        view.findViewById(R.id.btnRechazar).setOnClickListener(v -> {
            viewModel.rechazarPlato();
        });

        view.findViewById(R.id.btnLike).setOnClickListener(v -> {
            // Acción de me gusta
            viewModel.rechazarPlato();
            Toast.makeText(getContext(), "¡Te gusta!", Toast.LENGTH_SHORT).show();
        });
    }
}
