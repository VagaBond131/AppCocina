# ✅ CHECKLIST DE IMPLEMENTACIÓN

## ARCHIVO MODIFICADO

✅ `MenuViewModel.java` (presentation/viewmodel)
- Líneas 52-67: Mapeo actualizado (BEB_ALL, ADI_ALL)
- Líneas 82-100: Lógica de categorías actualizada
- Líneas 120-168: Logging mejorado
- Líneas 170-241: Nuevo método filtrarBebidas()

---

## ANTES DE COMPILAR

- [ ] Verificar que los tres archivos generados existen:
  - [ ] CAMBIOS_REALIZADOS.md
  - [ ] GUIA_PRUEBA.md
  - [ ] RESUMEN_CAMBIOS.md

- [ ] Verificar que MenuViewModel.java tiene:
  - [ ] `mapaCategorias.put("Bebidas", "BEB_ALL");`
  - [ ] `mapaCategorias.put("Cocteles", "BEB_ALL");`
  - [ ] `mapaCategorias.put("Adicionales", "ADI_ALL");`
  - [ ] Método `filtrarBebidas()` con líneas de logging

---

## COMPILACIÓN

### Opción A: Android Studio (Recomendado)
1. [ ] Abre Android Studio
2. [ ] Va a: **Build > Rebuild Project**
3. [ ] Espera a que termine
4. [ ] Cierra cualquier error que aparezca

### Opción B: Gradle Wrapper
```bash
cd "D:\Android Studio\Poyecto_Restaurante"
./gradlew clean build
```

---

## INSTALACIÓN Y PRUEBA

1. [ ] Conecta un dispositivo Android o abre el emulador
2. [ ] En Android Studio: **Run > Run 'app'**
3. [ ] Espera a que instale y abra la app

---

## PRUEBAS FUNCIONALES

### Test 1: Parrillas (Control)
- [ ] Haz click en pestaña "Parrillas"
- [ ] Deberías ver 13+ platos (según tu BD)
- [ ] ✓ Esto SIEMPRE funcionaba, es un control

### Test 2: Bebidas (Nueva Funcionalidad)
- [ ] Haz click en pestaña "Bebidas"
- [ ] Deberías ver: Vino, Cerveza, Gaseosas, Bebidas Naturales, Bebidas Calientes
- [ ] Si en lugar de eso ves: Cócteles, Margarita Corona, etc.
  - Significa que el filtrado de nombres está invertido

### Test 3: Cocteles (Nueva Funcionalidad)
- [ ] Haz click en pestaña "Cocteles"
- [ ] Deberías ver: Cócteles, Margarita Corona, Pisco Sour, Pisco Sour Especial (si existen)
- [ ] Si en lugar de eso ves: Vino, Cerveza, Gaseosas
  - Significa que el filtrado de nombres está invertido

### Test 4: Adicionales (Nueva Funcionalidad)
- [ ] Haz click en pestaña "Adicionales"
- [ ] Deberías ver todos los adicionales de tu BD
- [ ] Si sale ERROR:
  - Verificar que la tabla se llama "adicionales" (no "adicional")

---

## VERIFICACIÓN DE LOGS

### Abrir Logcat
1. [ ] Android Studio > **View > Tool Windows > Logcat** (o Alt + 6)
2. [ ] En el campo de búsqueda, escribe: `MenuViewModel`

### Verificar Logs para Bebidas
Deberías ver algo como:
```
D Consultando tabla: bebidas categoría: Bebidas con filtros: 
D === BEBIDAS RECIBIDAS TOTALES: 7 ===
D   [0] id=1 | idCat=1 | nombre=cocteles
D   [1] id=2 | idCat=1 | nombre=margarita corona
...
D   ✓ Incluido en Bebidas: vinos
D   ✗ Excluido de Bebidas: cocteles
D Respuesta exitosa. Items recibidos: 5 después de filtrar
D === RESULTADO: 5 elementos para Bebidas ===
```

- [ ] Verifica que dice: `Consultando tabla: bebidas` (sin `id_catbeb=`)
- [ ] Verifica que dice: `Items recibidos: X` (donde X > 0)
- [ ] Verifica que hay líneas `✓ Incluido` y `✗ Excluido`

### Verificar Logs para Cocteles
Deberías ver:
```
D Consultando tabla: bebidas categoría: Cocteles con filtros: 
D === BEBIDAS RECIBIDAS TOTALES: 7 ===
...
D   ✓ Incluido en Cocteles: cocteles
D   ✗ Excluido de Cocteles: vinos
D Respuesta exitosa. Items recibidos: 2 después de filtrar
D === RESULTADO: 2 elementos para Cocteles ===
```

- [ ] Verifica que dice: `categoría: Cocteles`
- [ ] Verifica que los filtros están correctos
- [ ] Verifica que `Items recibidos:` es mayor que antes para bebidas

---

## SI NO FUNCIONA

### Síntoma: Items recibidos: 0
[ ] Verificar en los logs:
  - [ ] ¿Dice " con filtros: " (vacío)? Sí = Correcto
  - [ ] ¿Dice " con filtros: id_catbeb=..." ? No = Error, volver a cambiar código
  - [ ] ¿Dice error 404? = Tabla no existe

[ ] Solución:
  - Asegúrate que `BEB_ALL` está escrito correctamente en `inicializarMapa()`
  - Verifica que la condición `if (mapping.equals("BEB_ALL"))` existe

### Síntoma: Items recibidos: 7, pero después Bebidas: 0
[ ] El filtrado no está funcionando
[ ] Verificar los logs:
  - [ ] ¿Vee "✓ Incluido" o solo "✗ Excluido"?
  - [ ] Si solo "✗", significa que las palabras clave no coinciden

[ ] Solución:
  - Copiar exactamente los nombres de los logs
  - Agregar esas palabras a `filtrarBebidas()`

### Síntoma: Se ve Bebidas pero Cocteles está vacío (o viceversa)
[ ] El filtrado está invertido
[ ] Verificar que:
  - [ ] `if (categoria.equals("Cocteles"))` busca cocteles
  - [ ] `else if (categoria.equals("Bebidas"))` busca bebidas

[ ] Si está invertido, cambiar qué categoría busca qué palabras

### Síntoma: Error al abrir Adicionales
[ ] La tabla no existe o tiene otro nombre
[ ] Verificar en Supabase:
  - [ ] ¿Existe tabla "adicionales"?
  - [ ] ¿O se llama "adicional" (singular)?
  - [ ] ¿O tiene otro nombre?

[ ] Solución:
  - Cambiar en `CargarPlatosPorCategoria()`: `tabla = "nombre_correcto";`

---

## SEÑALES DE ÉXITO ✨

Cuando todo funcione, verás:

1. **Pepstaña "Bebidas"** muestra 5+ bebidas (no cócteles)
2. **Pestaña "Cocteles"** muestra 2+ cócteles (no bebidas)
3. **Pestaña "Adicionales"** muestra todos los adicionales
4. **Logs en Logcat** muestran líneas con "✓ Incluido"
5. **Imágenes** se cargan correctamente
6. **Precios** se muestran con formato "S/ X.XX"

---

## PRÓXIMAS ACCIONES

Cuando funcione todo:

1. [ ] Hacer commit en Git (si lo usas)
2. [ ] Hacer una versión de la app
3. [ ] Pasar a producción

Si algo falla:
1. [ ] Copiar los logs completos de Logcat
2. [ ] Descripción del problema exacto
3. [ ] Screenshots
4. [ ] Contactar al equipo técnico

---

## CONTACTO PARA DUDAS

**Problema**: "No aparecen datos en Bebidas"
**Información a proporcionar**:
- [ ] Logs completos de Logcat (MenuViewModel)
- [ ] Screenshot de la tabla "bebidas" en Supabase
- [ ] Screenshot de la app

---

**Versión**: 1.0
**Fecha**: 4 de mayo de 2026
**Estado**: Listo para usar ✅

¡Éxito! 🚀

