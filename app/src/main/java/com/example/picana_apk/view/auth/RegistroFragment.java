package com.example.picana_apk.view.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.picana_apk.R;

public class RegistroFragment extends Fragment {

    public RegistroFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registro, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnIrIniciarSesion = view.findViewById(R.id.btnIrIniciarSesion);

        btnIrIniciarSesion.setOnClickListener(v -> {
            Sesion activity = (Sesion) getActivity();
            if (activity != null) {
                activity.reemplazarFragmento(new InicioSesionFragment());
            }
        });

        view.findViewById(R.id.btnRegresar).setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getOnBackPressedDispatcher().onBackPressed();
            }
        });
    }
}