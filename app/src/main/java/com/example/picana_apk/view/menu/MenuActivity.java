package com.example.picana_apk.view.menu;

import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.picana_apk.R;
import com.example.picana_apk.viewmodel.MenuViewModel;
import com.google.android.material.tabs.TabLayout;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        String categoria = getIntent().getStringExtra("CATEGORIA_SELECCIONADA");

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        MenuViewModel viewModel = new ViewModelProvider(this).get(MenuViewModel.class);
        viewModel.cargarPlatosPorCategoria(categoria);

        configurarTabs(categoria);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contenedor_menu, new MenuFragment())
                    .commit();
        }
    }

    private void configurarTabs(String seleccionada) {
        TabLayout tabs = findViewById(R.id.tabLayoutCategorias);
        String[] categorias = {"Parrillas", "Alitas", "Frituras", "Hamburguesas", "Salchipapas", "Bebidas", "Cocteles", "Adicionales"};

        for (String cat : categorias) {
            TabLayout.Tab tab = tabs.newTab().setText(cat);
            tabs.addTab(tab);
            if (cat != null && cat.equals(seleccionada)) {
                tab.select();
            }
        }

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                new ViewModelProvider(MenuActivity.this).get(MenuViewModel.class)
                        .cargarPlatosPorCategoria(tab.getText().toString());
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
}