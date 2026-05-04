package com.example.picana_apk.presentation.view.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.picana_apk.R;

public class CartaFragment extends Fragment {

    public CartaFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_categorias, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btnCatParrillas).setOnClickListener(v -> abrirMenuPorCategoria("Parrillas"));
        view.findViewById(R.id.btnCatAlitas).setOnClickListener(v -> abrirMenuPorCategoria("Alitas"));
        view.findViewById(R.id.btnCatFrituras).setOnClickListener(v -> abrirMenuPorCategoria("Frituras"));
        view.findViewById(R.id.btnCatHamburguesas).setOnClickListener(v -> abrirMenuPorCategoria("Hamburguesas"));
        view.findViewById(R.id.btnCatSalchipapas).setOnClickListener(v -> abrirMenuPorCategoria("Salchipapas"));
        view.findViewById(R.id.btnCatBebidas).setOnClickListener(v -> abrirMenuPorCategoria("Bebidas"));
        view.findViewById(R.id.btnCatCocteles).setOnClickListener(v -> abrirMenuPorCategoria("Cocteles"));
        view.findViewById(R.id.btnCatAdicionales).setOnClickListener(v -> abrirMenuPorCategoria("Adicionales"));
    }

    private void abrirMenuPorCategoria(String categoria) {
        Intent intent = new Intent(getActivity(), MenuActivity.class);
        intent.putExtra("CATEGORIA_SELECCIONADA", categoria);
        startActivity(intent);
    }
}