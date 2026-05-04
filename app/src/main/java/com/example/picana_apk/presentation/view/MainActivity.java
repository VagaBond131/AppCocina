package com.example.picana_apk.presentation.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.lifecycle.ViewModelProvider;
import com.example.picana_apk.R;
import com.example.picana_apk.presentation.view.auth.Sesion;
import com.example.picana_apk.presentation.viewmodel.AuthViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private AuthViewModel authViewModel;
    private TextView tvSaludoUsuario;
    private Button btnOrdenar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            irASesion();
        } else {
            setContentView(R.layout.activity_bienvenida);

            tvSaludoUsuario = findViewById(R.id.tvSaludoUsuario);
            btnOrdenar = findViewById(R.id.btnOrdenarPrincipal);

            authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

            authViewModel.getUserLiveData().observe(this, firebaseUser -> {
                if (firebaseUser != null) {
                    String nombreCompleto = firebaseUser.getDisplayName();
                    if (nombreCompleto != null && !nombreCompleto.isEmpty()) {
                        String primerNombre = nombreCompleto.split(" ")[0];
                        tvSaludoUsuario.setText("BUEN DÍA,\n" + primerNombre.toUpperCase() + ".");
                    } else {
                        tvSaludoUsuario.setText("BUEN DÍA,\nBIENVENIDO.");
                    }
                } else {
                    String email = currentUser.getEmail();
                    if (email != null) {
                        String nombreDesdeEmail = email.split("@")[0];
                        tvSaludoUsuario.setText("BUEN DÍA,\n" + nombreDesdeEmail.toUpperCase() + ".");
                    }
                }
            });

            btnOrdenar.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, Principal.class);
                startActivity(intent);
                finish();
            });
        }
    }

    private void irASesion() {
        Intent intent = new Intent(this, Sesion.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}