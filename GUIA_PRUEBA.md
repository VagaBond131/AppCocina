# GUÍA RÁPIDA - Cómo Probar Bebidas, Cócteles y Adicionales

## ✅ CAMBIOS REALIZADOS

### Archivo: `MenuViewModel.java` (presentation/viewmodel)

**Cambio 1: Mapeo de categorías actualizado**
```java
// ANTES:
mapaCategorias.put("Bebidas", "BEB_in.(3,4,5,6,7)");
mapaCategorias.put("Cocteles", "BEB_in.(1,2)");

// AHORA:
mapaCategorias.put("Bebidas", "BEB_ALL");      // Sin filtro de id_catbeb
mapaCategorias.put("Cocteles", "BEB_ALL");     // Sin filtro de id_catbeb
mapaCategorias.put("Adicionales", "ADI_ALL");  // Sin filtro
```

**Cambio 2: Método `filtrarBebidas()` agregado**
- Clasifica bebidas por NOMBRE (no por id_catbeb)
- Cócteles: busca "coct", "margarita", "pisco", "chilcano", "sour", "gin", "vodka", "ron", etc.
- Bebidas: busca "vino", "cerv", "gaseosa", "natural", "calient", "jugo", "refresco", "bebida", "caf", "chocolate", etc.

**Cambio 3: Mejor logging para debug**
- Muestra TODAS las bebidas recibidas
- Muestra si cada bebida se incluye o excluye
- Muestra el resultado final

---

## 🧪 CÓMO PROBAR

### 1. Compilar
```bash
# En Android Studio:
Build > Rebuild Project

# O en terminal:
./gradlew clean build
```

### 2. Ejecutar la app
- Instala en un dispositivo o emulador
- Abre la app
- Ve a la sección de Menú

### 3. Probar bebidas
- Haz click en la pestaña **"Bebidas"**
- Deberías ver: vino, cerveza, gaseosas, bebidas naturales, bebidas calientes, etc.

### 4. Probar cócteles
- Haz click en la pestaña **"Cocteles"**
- Deberías ver: cócteles, margarita corona, pisco sour, etc.

### 5. Probar adicionales
- Haz click en la pestaña **"Adicionales"**
- Deberías ver todos los adicionales

---

## 🔍 DEBUGGING - Ver los Logs

### Abrir Logcat en Android Studio
1. **Android Studio** > **Logcat** (parte inferior)
2. O presiona: **Alt + 6**

### Filtrar por MenuViewModel
En la cajita que dice "Edit Filter Configuration":
- Busca: `MenuViewModel`

### Logs esperados para Bebidas

```log
Consultando tabla: bebidas categoría: Bebidas con filtros: 

=== BEBIDAS RECIBIDAS TOTALES: 7 ===
  [0] id=1 | idCat=1 | nombre=cocteles
  [1] id=2 | idCat=1 | nombre=margarita corona
  [2] id=3 | idCat=1 | nombre=vinos
  [3] id=4 | idCat=1 | nombre=cerveza
  [4] id=5 | idCat=1 | nombre=gaseosas
  [5] id=6 | idCat=1 | nombre=bebidas naturales
  [6] id=7 | idCat=1 | nombre=bebidas calientes

  ✓ Incluido en Bebidas: vinos
  ✓ Incluido en Bebidas: cerveza
  ✓ Incluido en Bebidas: gaseosas
  ✓ Incluido en Bebidas: bebidas naturales
  ✓ Incluido en Bebidas: bebidas calientes
  ✗ Excluido de Bebidas: cocteles
  ✗ Excluido de Bebidas: margarita corona

Respuesta exitosa. Items recibidos: 5 después de filtrar

=== RESULTADO: 5 elementos para Bebidas ===
```

### Logs esperados para Cócteles

```log
Consultando tabla: bebidas categoría: Cocteles con filtros: 

=== BEBIDAS RECIBIDAS TOTALES: 7 ===
  [0] id=1 | idCat=1 | nombre=cocteles
  ...

  ✓ Incluido en Cocteles: cocteles
  ✓ Incluido en Cocteles: margarita corona
  ✗ Excluido de Cocteles: vinos
  ...

Respuesta exitosa. Items recibidos: 2 después de filtrar

=== RESULTADO: 2 elementos para Cocteles ===
```

---

## ⚠️ PROBLEMAS Y SOLUCIONES

### Problema 1: "Items recibidos: 0"
**Causa**: Los filtros no están correctos o la tabla no existe
**Solución**: 
- Verifica en los logs que dice: `Consultando tabla: bebidas con filtros: ` (SIN filtros)
- Si dice algo como `id_catbeb=...` entonces el filtrado aún está activo

### Problema 2: "Items recibidos: 7, pero después de filtrar: 0"
**Causa**: Las palabras clave en el filtrado no coinciden con los nombres reales
**Solución**:
- Revisa qué nombres están en los logs: `nombre=...`
- Si no coinciden, agrega esas palabras clave a `filtrarBebidas()`
  ```java
  if (nombreBebida.contains("tu_palabra_clave")) {
      incluir = true;
  }
  ```

### Problema 3: Error 404 "Tabla no encontrada"
**Causa**: La tabla "bebidas" o "adicionales" no existe
**Solución**:
- Consulta con el de la BD exactamente cómo se llaman las tablas
- Si son "bebida" y "adicional" (singular), cambiar:
  ```java
  tabla = "bebida";      // en lugar de "bebidas"
  tabla = "adicional";   // en lugar de "adicionales"
  ```

### Problema 4: Aparecer algunos cócteles en bebidas o viceversa
**Causa**: Las palabras clave están siendo mal interpretadas
**Solución**:
- Agrega más palabras clave específicas al filtrado
- Usa búsquedas más específicas (ejemplo: "coct" en lugar de solo "co")

---

## 📝 PERSONALIZACIÓN

Si necesitas cambiar el mapeo de bebidas/cócteles:

### Opción A: Cambiar palabras clave en `filtrarBebidas()`
```java
if (categoria.equals("Cocteles")) {
    if (nombreBebida.contains("TU_PALABRA_AQUI") || 
        nombreBebida.contains("OTRA_PALABRA")) {
        incluir = true;
    }
}
```

### Opción B: Cambiar el filtrado a por id_catbeb (si la BD lo permite)
Si los IDs sí existen en la BD, cambia en `inicializarMapa()`:
```java
mapaCategorias.put("Bebidas", "BEB_in.(3,4,5,6,7)");
mapaCategorias.put("Cocteles", "BEB_in.(1,2)");
```

### Opción C: Cambiar los nombres de las tablas
```java
tabla = "nombre_tabla_real";
```

---

## 📞 INFORMACIÓN PARA DEBUG

Si nada funciona, proporciar esta información:

1. **Screenshot del Logcat** (tal como aparece)
2. **Screenshot de la tabla "bebidas" de Supabase** con todos los registros y columnas
3. **Screenshot de la tabla "adicionales" de Supabase** con todos los registros y columnas
4. **Screenshot de lo que ves en la app** (¿está vacío? ¿sale error?)

---

## ✨ Resumen Rápido

| Elemento | Antes | Ahora |
|----------|-------|-------|
| Bebidas | Filtraba por id_catbeb | Carga todas + filtra por nombre |
| Cócteles | Filtraba por id_catbeb | Carga todas + filtra por nombre |
| Adicionales | Error de tabla | Carga todas sin filtro |
| Logging | Mínimo | Detallado (muestra cada paso) |

---

**Versión**: 1.0  
**Fecha**: 4 de mayo de 2026  
**Estado**: Listo para probar

