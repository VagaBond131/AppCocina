package com.example.picana_apk.view.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.picana_apk.R;
import com.example.picana_apk.viewmodel.MenuViewModel;

public class MenuMatchFragment extends Fragment {

    private MenuViewModel viewModel;
    private TextView tvNombrePlato;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu_match, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvNombrePlato = view.findViewById(R.id.tvNombrePlato);

        viewModel = new ViewModelProvider(requireActivity()).get(MenuViewModel.class);

        viewModel.getPlatoActual().observe(getViewLifecycleOwner(), plato -> {
            if (plato != null) {
                tvNombrePlato.setText(plato.getNombre());
            }
        });

        view.findViewById(R.id.btnRechazar).setOnClickListener(v -> viewModel.rechazarPlato());
    }
}