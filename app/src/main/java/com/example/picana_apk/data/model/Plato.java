package com.example.picana_apk.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.Objects;

/**
 * Modelo de datos unificado para productos (Platos, Bebidas, Adicionales).
 * Soporta múltiples alias para ser compatible con diversas estructuras de tablas en la BD.
 */
public class Plato {
    @SerializedName(value = "id", alternate = {"id_plato", "id_bebida", "id_adicional", "ID", "id_producto", "codigo", "id_prod", "id_item"})
    private int id;
    
    @SerializedName(value = "id_categoria", alternate = {
        "id_catbeb", "id_catadi", "id_cat_beb", "id_cat_adi", 
        "categoria_id", "id_cat", "categoria", "id_tipo", "cat_id", "id_c", "id_cat_plato"
    })
    private String idCategoria;
    
    @SerializedName(value = "nombre", alternate = {
        "nombre_plato", "nombre_bebida", "nombre_adicional", 
        "producto", "title", "name", "descripcion_corta", "nom_plato", "nombre_prod", "nombre_item"
    })
    private String nombre;
    
    @SerializedName(value = "costo", alternate = {
        "precio", "valor", "precio_unitario", "monto", "costo_unitario", 
        "price", "costo_venta", "precio_venta", "costo_total"
    })
    private double precio;
    
    @SerializedName(value = "imagen_url", alternate = {
        "imagen", "url_imagen", "foto", "img", "thumbnail", "url", 
        "imagen_plato", "img_url", "foto_url", "path_imagen", 
        "resource_url", "src", "imagen_producto", "foto_producto", 
        "img_plato", "imagen_bebida", "foto_bebida", "url_foto",
        "imagen_adicional", "foto_adicional", "img_path", "imagen_prod", "img_url_plato"
    })
    private String imagenUrl;
    
    @SerializedName(value = "peso", alternate = {"gramos", "cantidad_gramos", "peso_gr", "peso_neto", "peso_total"})
    private int peso;
    
    @SerializedName(value = "descripcion", alternate = {"detalle", "observacion", "info", "desc", "descripcion_plato", "resumen"})
    private String descripcion;

    @SerializedName(value = "id_sucursal", alternate = {"sucursal_id", "id_suc", "sucursal", "id_local"})
    private Integer idSucursal;

    public int getId() { return id; }
    public String getIdCategoria() { return idCategoria; }
    public String getNombre() { return nombre != null ? nombre : "Producto"; }
    public double getPrecio() { return precio; }
    public String getImagenUrl() { return imagenUrl; }
    public int getPeso() { return peso; }
    public String getDescripcion() { return descripcion; }
    public Integer getIdSucursal() { return idSucursal; }

    /**
     * Retorna la URL de la imagen lista para ser cargada por Glide.
     * Maneja links de Google Drive y rutas relativas de Supabase Storage.
     */
    public String getSanitizedImagenUrl() {
        if (imagenUrl == null || imagenUrl.isEmpty()) return null;
        
        String url = imagenUrl.trim();
        if (url.startsWith("http")) {
            // Corrección para links de Google Drive (convertir a link directo)
            if (url.contains("drive.google.com")) {
                if (url.contains("uc?")) {
                    return url.replace("export=view&", "").replace("export=download&", "");
                } else if (url.contains("/file/d/")) {
                    try {
                        String[] parts = url.split("/file/d/");
                        String fileId = parts[1].split("/")[0];
                        return "https://drive.google.com/uc?id=" + fileId;
                    } catch (Exception e) { return url; }
                }
            }
            return url;
        } else {
            // Supabase Storage
            return "https://dzgcngblutosxjajlxov.supabase.co/storage/v1/object/public/productos/" + url;
        }
    }

    /**
     * Genera un ID estable y único para DiffUtil.
     */
    public long getStableId() {
        return (long) Objects.hash(getNombre(), id, idCategoria, precio);
    }
}
