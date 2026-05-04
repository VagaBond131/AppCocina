package com.example.picana_apk.data.network;

import com.example.picana_apk.data.model.Plato;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface SupabaseApi {
    /**
     * Obtiene productos de una tabla específica con filtros dinámicos.
     * @param tabla Nombre de la tabla (plato, bebidas, adicional).
     * @param apiKey Clave pública de Supabase.
     * @param auth Token de autorización (Bearer clave).
     * @param filtros Mapa de filtros (ej: "id_categoria" -> "eq.1" o "in.(1,2,3)").
     * @param orden Criterio de ordenación (ej: "nombre.asc").
     */
    @GET("rest/v1/{table}?select=*")
    Call<List<Plato>> getProductos(
            @Path("table") String tabla,
            @Header("apikey") String apiKey,
            @Header("Authorization") String auth,
            @QueryMap Map<String, String> filtros,
            @Query("order") String orden
    );
}
