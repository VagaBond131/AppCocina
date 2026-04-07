package com.example.picana_apk.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.picana_apk.model.Plato;
import java.util.ArrayList;
import java.util.List;

public class MenuViewModel extends ViewModel {

    private MutableLiveData<Plato> platoActual = new MutableLiveData<>();
    private MutableLiveData<List<Plato>> platos = new MutableLiveData<>();
    private List<Plato> platosFiltrados = new ArrayList<>();
    private int posicionActual = 0;

    public void cargarPlatosPorCategoria(String categoria) {
        this.posicionActual = 0;

        List<Plato> listaPrueba = new ArrayList<>();


        listaPrueba.add(new Plato(1, "Chuleta de Res", 22.00, "Parrillas", "", "330 gr.", "15 min."));
        listaPrueba.add(new Plato(2, "Chuleta de Chancho", 22.00, "Parrillas", "", "330 gr.", "15 min."));
        listaPrueba.add(new Plato(3, "Filete de Pollo", 20.00, "Parrillas", "", "330 gr.", "15 min."));
        listaPrueba.add(new Plato(4, "Anticuchos", 21.00, "Parrillas", "", "330 gr.", "15 min."));

        this.platosFiltrados = listaPrueba;
        this.platos.setValue(listaPrueba);

        if (!listaPrueba.isEmpty()) {
            platoActual.setValue(listaPrueba.get(0));
        }
    }

    public void cargarMenuPorCategoria(String categoria) {
        cargarPlatosPorCategoria(categoria);
    }

    public LiveData<List<Plato>> getPlatos() {
        return platos;
    }

    public LiveData<Plato> getPlatoActual() {
        return platoActual;
    }

    public void rechazarPlato() {
        if (platosFiltrados != null && posicionActual < platosFiltrados.size() - 1) {
            posicionActual++;
            platoActual.setValue(platosFiltrados.get(posicionActual));
        }
    }
}