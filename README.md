# VigiaVial

Aplicación Android para reportar anomalías, fallas técnicas y conductas
indebidas en los sistemas de transporte público de la Zona Metropolitana de
Guadalajara (ZMG), y para consultar el estado de esos reportes.

Los datos se guardan localmente en SQLite. No hay backend ni sincronización
entre dispositivos.

## Funcionalidad

- **Registro e inicio de sesión** con correo y contraseña; la sesión se
  conserva en `SharedPreferences`.
- **Nuevo reporte**: tipo de transporte (Camión, Tren, Bicicleta, Taxi),
  número de unidad, descripción y una foto opcional de la galería.
- **Historial personal**: reportes del usuario, con una barra de progreso
  `Recibido → En Proceso → Solucionado`.
- **Historial global**: todos los reportes, con filtros por hoy, última
  semana y último mes, y un badge con el estado.
- **Simulador de estados**: cada 10 segundos avanza los reportes abiertos.
  El intervalo se configura en `DBHelper.INTERVALO_AVANCE_MS`.

## Tecnología

| Componente | Versión |
|---|---|
| Lenguaje | Java 11 |
| Android Gradle Plugin | 9.0.1 |
| compileSdk / targetSdk | 36 |
| minSdk | 24 |
| UI | AppCompat, Material, RecyclerView, CardView |
| Persistencia | SQLite (`SQLiteOpenHelper`) |

## Requisitos

- Android Studio (o JDK 11+ y Android SDK 36).
- Conexión a internet la primera vez para descargar dependencias de Gradle.

## Compilación y ejecución

```bash
./gradlew assembleDebug
```

El APK se genera en `app/build/outputs/apk/debug/app-debug.apk`.

También se puede abrir el proyecto en Android Studio y ejecutarlo con
**Run ▸ Run 'app'**.

No hace falta configurar ninguna API ni clave: la base de datos se crea sola
al primer arranque.

## Estructura

```
VigiaVial/
├── app/
│   ├── build.gradle.kts
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/example/vigiavial/
│       │   ├── splash.java            # Pantalla de arranque
│       │   ├── Login.java             # Inicio de sesión
│       │   ├── Registro.java          # Registro de usuarios
│       │   ├── MainActivity.java      # Menú principal
│       │   ├── Reporte.java           # Formulario de reporte
│       │   ├── ReporteModel.java      # Modelo de reporte
│       │   ├── ReporteAdapter.java    # Lista de reportes
│       │   ├── HistorialPersonal.java # Reportes del usuario
│       │   ├── HistorialGlobal.java   # Todos los reportes
│       │   ├── EstadoSimulador.java   # Avance automático de estados
│       │   └── DBHelper.java          # SQLite: usuarios y reportes
│       └── res/                       # Layouts, drawables y temas
├── build.gradle.kts
├── settings.gradle.kts
└── gradle/libs.versions.toml
```

## Base de datos

`VigiaVial.db` se crea en el almacenamiento interno de la app.

| Tabla | Campos |
|---|---|
| `usuarios` | `correo` (PK), `contrasena`, `nombre` |
| `reportes` | `id`, `correo_usuario`, `tipo_transporte`, `numero_unidad`, `descripcion`, `foto_path`, `estado`, `fecha` |

## Limitaciones conocidas

- Las contraseñas se guardan en **texto plano** en SQLite y la sesión solo
  guarda el correo. No usar en producción sin hashing y un backend real.
- La foto se guarda como URI de `ContentResolver`; el permiso es temporal y
  la imagen no se muestra en los historiales. Tras reiniciar la app la URI
  puede dejar de ser legible.
- `DBHelper.onUpgrade` elimina y recrea las tablas: una actualización de
  versión borra los datos locales.
- Cualquier usuario registrado puede ver el historial global.
- La bienvenida usa la parte local del correo, no el nombre registrado.
- Los estados son simulados localmente; no reflejan una atención real.
- Los textos y colores están hardcodeados en layouts y Java (sin i18n).
- Solo existen las pruebas de plantilla (`ExampleUnitTest`,
  `ExampleInstrumentedTest`).

## Créditos

Desarrollo original: AlexWhite / AlexSo11.
