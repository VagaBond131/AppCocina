package com.example.picana_apk.presentation.view.menu;

import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.picana_apk.R;
import com.example.picana_apk.presentation.viewmodel.MenuViewModel;
import com.google.android.material.tabs.TabLayout;
import java.util.HashMap;
import java.util.Map;

public class MenuActivity extends AppCompatActivity {

    private MenuViewModel viewModel;
    private final Map<String, String> categoriaMapa = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // IDs Sincronizados con MenuViewModel para corregir el desorden de platos
        categoriaMapa.put("Parrillas", "1");
        categoriaMapa.put("Alitas", "4");      // Corregido: ID 2 era de Parrillas
        categoriaMapa.put("Frituras", "6");    // Corregido: ID 3 era de Parrillas
        categoriaMapa.put("Hamburguesas", "8"); 
        categoriaMapa.put("Salchipapas", "5");
        categoriaMapa.put("Bebidas", "101");   // ID Virtual para lógica de tabla bebidas
        categoriaMapa.put("Cocteles", "102");  // ID Virtual para lógica de tabla bebidas
        categoriaMapa.put("Adicionales", "201"); // ID Virtual para tabla adicional

        viewModel = new ViewModelProvider(this).get(MenuViewModel.class);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        String seleccionada = getIntent().getStringExtra("CATEGORIA_SELECCIONADA");
        if (seleccionada == null || seleccionada.isEmpty()) seleccionada = "Parrillas";
        
        configurarTabs(seleccionada);

        viewModel.cargarPlatosPorCategoria(seleccionada);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contenedor_menu, new MenuFragment())
                    .commit();
        }
    }

    private void configurarTabs(String seleccionada) {
        TabLayout tabs = findViewById(R.id.tabLayoutCategorias);
        String[] nombres = {"Parrillas", "Alitas", "Frituras", "Hamburguesas", "Salchipapas", "Bebidas", "Cocteles", "Adicionales"};

        tabs.removeAllTabs();
        for (String nombre : nombres) {
            TabLayout.Tab tab = tabs.newTab().setText(nombre);
            tabs.addTab(tab);
            if (nombre.equals(seleccionada)) {
                tab.select();
            }
        }

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String nombreCat = tab.getText().toString();
                viewModel.cargarPlatosPorCategoria(nombreCat);
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
}
