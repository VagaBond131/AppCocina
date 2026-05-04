package com.example.picana_apk.presentation.view.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.picana_apk.R;
import com.example.picana_apk.presentation.adapter.PlatoAdapter;
import com.example.picana_apk.presentation.viewmodel.MenuViewModel;

public class MenuFragment extends Fragment {

    private RecyclerView rvPlatos;
    private PlatoAdapter adapter;
    private MenuViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPlatos = view.findViewById(R.id.rvPlatos);
        rvPlatos.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        
        rvPlatos.setHasFixedSize(true);
        rvPlatos.setItemViewCacheSize(20);
        
        adapter = new PlatoAdapter();
        rvPlatos.setAdapter(adapter);

        // CORRECCIÓN: Usar 'requireActivity()' para compartir el ViewModel con MenuActivity.
        // Esto permite que el fragmento reaccione cuando se cambian las pestañas en la actividad.
        viewModel = new ViewModelProvider(requireActivity()).get(MenuViewModel.class);

        viewModel.getPlatos().observe(getViewLifecycleOwner(), platos -> {
            if (platos != null) {
                adapter.updateData(platos);
            }
        });
    }
}
