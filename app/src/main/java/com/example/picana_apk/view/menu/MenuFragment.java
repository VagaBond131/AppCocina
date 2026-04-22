package com.example.picana_apk.view.menu;

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
import com.example.picana_apk.adapter.PlatoAdapter;
import com.example.picana_apk.viewmodel.MenuViewModel;

public class MenuFragment extends Fragment {

    private RecyclerView rvPlatos;
    private MenuViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPlatos = view.findViewById(R.id.rvPlatos);
        rvPlatos.setLayoutManager(new GridLayoutManager(getContext(), 2));

        viewModel = new ViewModelProvider(requireActivity()).get(MenuViewModel.class);

        viewModel.getPlatos().observe(getViewLifecycleOwner(), platos -> {
            if (platos != null) {
                PlatoAdapter adapter = new PlatoAdapter(platos);
                rvPlatos.setAdapter(adapter);
            }
        });
    }
}