package com.example.picana_apk.presentation.view;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.example.picana_apk.R;
import com.example.picana_apk.presentation.view.menu.CartaFragment;
import com.example.picana_apk.presentation.view.menu.MenuMatchFragment;

public class Principal extends AppCompatActivity {

    private ImageView navHome, navCarta, navCarrito, navPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        navHome = findViewById(R.id.navHome);
        navCarta = findViewById(R.id.navCarta);
        navCarrito = findViewById(R.id.navCarrito);
        navPerfil = findViewById(R.id.navPerfil);

        if (savedInstanceState == null) {
            reemplazarFragmento(new MenuMatchFragment(), navHome);
        }

        navHome.setOnClickListener(v -> reemplazarFragmento(new MenuMatchFragment(), navHome));
        navCarta.setOnClickListener(v -> reemplazarFragmento(new CartaFragment(), navCarta));

        navCarrito.setOnClickListener(v -> {
        });

        navPerfil.setOnClickListener(v -> {
        });
    }

    private void reemplazarFragmento(Fragment fragmento, ImageView iconoSeleccionado) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.contenedor_fragmentos_principal, fragmento)
                .commit();

        resetearColoresIconos();
        iconoSeleccionado.setColorFilter(ContextCompat.getColor(this, R.color.color_dorado_pincana));
    }

    private void resetearColoresIconos() {
        int colorBlanco = ContextCompat.getColor(this, R.color.color_texto_blanco);
        navHome.setColorFilter(colorBlanco);
        navCarta.setColorFilter(colorBlanco);
        navCarrito.setColorFilter(colorBlanco);
        navPerfil.setColorFilter(colorBlanco);
    }
}