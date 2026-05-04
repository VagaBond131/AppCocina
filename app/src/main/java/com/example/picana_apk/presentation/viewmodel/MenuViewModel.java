package com.example.picana_apk.presentation.viewmodel;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.picana_apk.data.model.Plato;
import com.example.picana_apk.data.network.SupabaseApi;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MenuViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Plato>> platos = new MutableLiveData<>();
    private final MutableLiveData<Plato> platoActual = new MutableLiveData<>();
    private int currentIndex = 0;
    private final Map<String, String> mapaCategorias = new HashMap<>();

    private final String supabaseUrl = "https://dzgcngblutosxjajlxov.supabase.co/";
    private final String supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImR6Z2NuZ2JsdXRvc3hqYWpseG92Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzY4Njk5MzksImV4cCI6MjA5MjQ0NTkzOX0.mdydbVqsBDSnFbG3Ntlamy-cj-l-SXlSLsSa2aAMs4M";

    private final SupabaseApi api;

    public MenuViewModel(@NonNull Application application) {
        super(application);
        inicializarMapa();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.NONE);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(logging).build();

        api = new Retrofit.Builder()
                .baseUrl(supabaseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(SupabaseApi.class);
    }

    private void inicializarMapa() {
        // Parrillas: Incluye id_categoria 1, 2 y 3 (Platos, Especiales, Combos)
        mapaCategorias.put("Parrillas", "in.(1,2,3)");
        mapaCategorias.put("Alitas", "4");
        mapaCategorias.put("Salchipapas", "5");
        mapaCategorias.put("Frituras", "6");
        mapaCategorias.put("Hamburguesas", "8"); 
        
        // Bebidas: Se cargan todas las bebidas sin filtro (todos tienen id_catbeb = 1)
        // Se agruparán en dos categorías de menú: Cócteles y Bebidas basado en el nombre
        mapaCategorias.put("Bebidas", "BEB_ALL");
        mapaCategorias.put("Cocteles", "BEB_ALL");

        // Adicionales (Tabla: adicional - SINGULAR)
        mapaCategorias.put("Adicionales", "ADI_ALL");
    }

    public void cargarPlatosPorCategoria(String categoriaInput) {
        if (categoriaInput == null || categoriaInput.isEmpty()) categoriaInput = "Parrillas";

        platos.setValue(new ArrayList<>());
        platoActual.setValue(null);

        Log.d("MenuViewModel", "=== CARGAR CATEGORÍA: " + categoriaInput + " ===");

        final String mapping = mapaCategorias.getOrDefault(categoriaInput, categoriaInput);
        Log.d("MenuViewModel", "Mapping: " + mapping);

        final String tabla;
        final String columnaFiltro;
        final String valorFiltro;
        final String categoriaMenuFinal = categoriaInput; // Para filtrado por nombre en bebidas

        if (mapping.startsWith("BEB_")) {
            tabla = "bebidas";
            Log.d("MenuViewModel", "Detectado BEB_, tabla: " + tabla);
            // Si es BEB_ALL, cargamos todas sin filtro de id_catbeb
            if (mapping.equals("BEB_ALL")) {
                columnaFiltro = null;
                valorFiltro = null;
                Log.d("MenuViewModel", "BEB_ALL detectado: cargando SIN filtro");
            } else {
                columnaFiltro = "id_catbeb";
                valorFiltro = mapping.replace("BEB_", "");
                Log.d("MenuViewModel", "Otro BEB_: filtrando por " + valorFiltro);
            }
        } else if (mapping.equals("ADI_ALL")) {
            tabla = "adicional";
            columnaFiltro = null;
            valorFiltro = null;
            Log.d("MenuViewModel", "ADI_ALL detectado: cargando SIN filtro de tabla adicional");
        } else {
            tabla = "plato"; // Singular
            columnaFiltro = "id_categoria";
            valorFiltro = mapping;
            Log.d("MenuViewModel", "Tabla plato: filtrando por id_categoria=" + valorFiltro);
        }

        Map<String, String> filtros = new HashMap<>();

        // Filtrar por sucursal 1 solo para platos (tabla: plato)
        if (tabla.equals("plato")) {
            filtros.put("id_sucursal", "eq.1");
            Log.d("MenuViewModel", "Agregado filtro sucursal para platos");
        }

        if (columnaFiltro != null) {
            if (valorFiltro.contains("in.")) {
                filtros.put(columnaFiltro, valorFiltro);
                Log.d("MenuViewModel", "Agregado filtro con IN: " + columnaFiltro + "=" + valorFiltro);
            } else {
                filtros.put(columnaFiltro, "eq." + valorFiltro);
                Log.d("MenuViewModel", "Agregado filtro con EQ: " + columnaFiltro + "=eq." + valorFiltro);
            }
        } else {
            Log.d("MenuViewModel", "Sin filtros de columna (NULL)");
        }

        Log.d("MenuViewModel", "Filtros finales: " + filtros.toString());
        ejecutarConsulta(tabla, filtros, columnaFiltro, valorFiltro, categoriaMenuFinal);
    }

    private void ejecutarConsulta(String tabla, Map<String, String> filtros, String columnaFiltro, String valorFiltro, String categoriaMenu) {
        ejecutarConsulta(tabla, filtros, columnaFiltro, valorFiltro, categoriaMenu, false);
    }

    private void ejecutarConsulta(String tabla, Map<String, String> filtros, String columnaFiltro, String valorFiltro, String categoriaMenu, boolean esReintento) {
        // Log para debug
        StringBuilder logFiltros = new StringBuilder();
        for (Map.Entry<String, String> entry : filtros.entrySet()) {
            logFiltros.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }

        String urlLog = "rest/v1/" + tabla + "?select=*";
        if (filtros.size() > 0) {
            urlLog += "&" + logFiltros.toString();
        }
        Log.d("MenuViewModel", "═══ CONSULTANDO API ═══");
        Log.d("MenuViewModel", "URL aproximada: " + urlLog);
        Log.d("MenuViewModel", "Tabla: " + tabla);
        Log.d("MenuViewModel", "Categoría Menú: " + categoriaMenu);
        Log.d("MenuViewModel", "Filtros Map size: " + filtros.size());
        Log.d("MenuViewModel", "Filtros: " + logFiltros.toString());
        if (esReintento) {
            Log.d("MenuViewModel", "⚠️ Esta es un REINTENTO");
        }

        api.getProductos(tabla, supabaseKey, "Bearer " + supabaseKey, filtros, "nombre.asc")
            .enqueue(new Callback<List<Plato>>() {
                @Override
                public void onResponse(Call<List<Plato>> call, Response<List<Plato>> response) {
                    Log.d("MenuViewModel", "═══ RESPUESTA RECIBIDA ═══");
                    Log.d("MenuViewModel", "Código HTTP: " + response.code());
                    Log.d("MenuViewModel", "Es exitosa: " + response.isSuccessful());

                    if (response.isSuccessful() && response.body() != null) {
                        List<Plato> lista = response.body();
                        Log.d("MenuViewModel", "Items en respuesta: " + lista.size());

                        // Filtrar por nombre si es categoría de bebidas
                        if (tabla.equals("bebidas")) {
                            Log.d("MenuViewModel", "Es tabla bebidas. Aplicando filtro de nombres para: " + categoriaMenu);
                            lista = filtrarBebidas(lista, categoriaMenu);
                        }

                        Log.d("MenuViewModel", "✅ RESPUESTA EXITOSA. Items final: " + lista.size());
                        platos.setValue(lista);
                        if (!lista.isEmpty()) {
                            currentIndex = 0;
                            platoActual.setValue(lista.get(0));
                            Log.d("MenuViewModel", "Primer item establecido: " + lista.get(0).getNombre());
                        } else {
                            Log.d("MenuViewModel", "⚠️ Lista vacía después de filtrado");
                        }
                    } else {
                        Log.e("MenuViewModel", "❌ ERROR EN RESPUESTA");
                        Log.e("MenuViewModel", "Código: " + response.code());
                        Log.e("MenuViewModel", "Mensaje: " + response.message());
                        try {
                            if (response.errorBody() != null) {
                                String errorBody = response.errorBody().string();
                                Log.e("MenuViewModel", "Error Body: " + errorBody);
                            }
                        } catch (Exception e) {
                            Log.e("MenuViewModel", "Error al leer error body: " + e.getMessage());
                        }

                        // Si falla CON REINTENTO FALLIDO, no intentamos de nuevo
                        if (response.code() == 404 && !esReintento) {
                            String tablaAlternativa = tabla.endsWith("s") ? tabla.substring(0, tabla.length() - 1) : tabla + "s";
                            Log.d("MenuViewModel", "🔄 Error 404. Reintentando UNA VEZ con tabla: " + tablaAlternativa);
                            ejecutarConsulta(tablaAlternativa, filtros, null, null, categoriaMenu, true);
                        } else if (response.code() == 404 && esReintento) {
                            Log.e("MenuViewModel", "❌ Error 404 también en REINTENTO. Tabla no existe: " + tabla);
                        }
                    }
                }
                @Override
                public void onFailure(Call<List<Plato>> call, Throwable t) {
                    Log.e("MenuViewModel", "❌ FALLO DE RED");
                    Log.e("MenuViewModel", "Mensaje: " + t.getMessage());
                    Log.e("MenuViewModel", "Clase: " + t.getClass().getSimpleName());
                    t.printStackTrace();
                }
            });
    }

    private List<Plato> filtrarBebidas(List<Plato> bebidas, String categoria) {
        List<Plato> filtradas = new ArrayList<>();
        
        Log.d("MenuViewModel", "╔════ FILTRADO DE BEBIDAS ════╗");
        Log.d("MenuViewModel", "Categoría solicitada: " + categoria);
        Log.d("MenuViewModel", "Total de bebidas recibidas: " + bebidas.size());
        // Log de debug: mostrar TODAS las bebidas recibidas
        for (int i = 0; i < bebidas.size(); i++) {
            Plato p = bebidas.get(i);
            Log.d("MenuViewModel", "  [" + i + "] id=" + p.getId() +
                    " | idCat=" + p.getIdCategoria() +
                    " | nombre=\"" + p.getNombre() + "\"" +
                    " | precio=" + p.getPrecio());
        }
        
        for (Plato bebida : bebidas) {
            String nombreBebida = bebida.getNombre().toLowerCase().trim();
            boolean incluir = false;
            String razon = "";

            if (categoria.equals("Cocteles")) {
                // Lógica: Si contiene ciertas palabras = CÓCTEL
                if (nombreBebida.contains("coct") ||
                    nombreBebida.contains("margarita") ||
                    nombreBebida.contains("corona") ||
                    nombreBebida.contains("pisco") ||
                    nombreBebida.contains("chilcano") ||
                    nombreBebida.contains("sour") ||
                    nombreBebida.contains("gin") ||
                    nombreBebida.contains("vodka") ||
                    nombreBebida.contains("rum") ||
                    nombreBebida.contains("ron") ||
                    nombreBebida.contains("whiskey") ||
                    nombreBebida.contains("tequila") ||
                    nombreBebida.contains("brandy") ||
                    nombreBebida.contains("mojito") ||
                    nombreBebida.contains("caipirinha") ||
                    nombreBebida.contains("daiquiri") ||
                    nombreBebida.contains("cosmopolitan")) {
                    incluir = true;
                    razon = "Contiene palabra clave de cóctel";
                }
            } else if (categoria.equals("Bebidas")) {
                // Bebidas no alcohólicas y bebidas regulares
                if (nombreBebida.contains("vino") ||
                    nombreBebida.contains("cerv") ||
                    nombreBebida.contains("gaseosa") ||
                    nombreBebida.contains("natural") ||
                    nombreBebida.contains("calient") ||
                    nombreBebida.contains("jugo") ||
                    nombreBebida.contains("refresco") ||
                    nombreBebida.contains("agua") ||
                    nombreBebida.contains("bebida") ||
                    nombreBebida.contains("infusi") ||
                    nombreBebida.contains("caf") ||
                    nombreBebida.contains("té") ||
                    nombreBebida.contains("te ") ||
                    nombreBebida.contains("chocolate") ||
                    nombreBebida.contains("smoothie") ||
                    nombreBebida.contains("batido") ||
                    nombreBebida.contains("limonada") ||
                    nombreBebida.contains("horchata")) {
                    incluir = true;
                    razon = "Contiene palabra clave de bebida";
                }
            }
            
            if (incluir) {
                filtradas.add(bebida);
                Log.d("MenuViewModel", "  ✓ " + nombreBebida + " | " + razon);
            } else {
                Log.d("MenuViewModel", "  ✗ " + nombreBebida + " | No coincide");
            }
        }
        
        Log.d("MenuViewModel", "╚════ RESULTADO: " + filtradas.size() + " elementos para " + categoria + " ════╝");
        return filtradas;
    }

    public LiveData<List<Plato>> getPlatos() { return platos; }
    public LiveData<Plato> getPlatoActual() { return platoActual; }

    public void rechazarPlato() {
        List<Plato> lista = platos.getValue();
        if (lista != null && !lista.isEmpty()) {
            currentIndex++;
            if (currentIndex >= lista.size()) currentIndex = 0;
            platoActual.setValue(lista.get(currentIndex));
        }
    }
}
