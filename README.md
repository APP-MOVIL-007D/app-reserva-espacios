# Reserva de espacios - Duoc UC Sede Viña del Mar

App Android para reservar salas, laboratorios y otros espacios de la sede. Está pensada para docentes y administrativos: ven los espacios, revisan el calendario de cada uno, piden una reserva y siguen en qué estado está.

Ahora las reservas se piden por un formulario de Notion, se anotan en Teamup y el resto se coordina por WhatsApp. Con la app la idea es tener todo en un solo lugar.

Proyecto de DSY1105 Desarrollo de Aplicaciones Móviles, sección 007D (Evaluaciones Parciales 2 y 3).

## Integrantes

- Nicolás Olivares
- Exequiel Guzmán
- Víctor Godoy
- Xavier Marín

## Qué hace la app

- **Login** solo con correo institucional (`@duoc.cl` o `@profesor.duoc.cl`) y solo para usuarios registrados. Los correos de alumnos (`@duocuc.cl`) no entran.
- Cada campo del login muestra su error con un ícono. Si el usuario no está registrado o la contraseña no corresponde, lo dice. "¿Olvidaste tu contraseña?" explica a quién contactar.
- La **sesión queda guardada**: si no cerraste sesión, la app parte directo en el inicio.
- **Tres perfiles:** docente, administrativo y administrador. El administrador ve arriba la franja "VISTA ADMINISTRADOR".
- Barra inferior con Inicio, Mis reservas, Espacios, Notificaciones y Menú, con una rayita amarilla bajo la pestaña seleccionada.
- **Inicio:** saludo con el nombre, la foto de perfil (o las iniciales si no tiene) y el rol del usuario, botón a notificaciones, la próxima reserva aprobada con su fecha y los espacios disponibles hoy.
- **Espacios:** lista de salas con buscador. Cada sala abre su **calendario**, con los bloques del día disponibles, pendientes o reservados. Se puede cambiar de día y de mes.
- **Solicitar reserva:** al tocar un bloque libre del calendario, se abre con el espacio, la fecha y la hora ya puestos. Se elige la hora de término y el motivo. La reserva queda Pendiente y aparece en Mis reservas.
- **Mis reservas:** separa Pendientes, Aprobadas e Historial. Se puede cancelar una reserva y antes pide confirmar. El día de la reserva aprobada aparece **Registrar ingreso** (queda En uso con la hora) y después **Registrar salida** (queda Finalizada).
- **Notificaciones:** avisa cuando una reserva fue aprobada o rechazada, en la app y en el teléfono.
- **Perfil (Menú):** foto de perfil con la **cámara** o desde la **galería**, se guarda y se ve también en el inicio. Cerrar sesión pide confirmación.
- **Administrador:** su inicio son las solicitudes pendientes, con cuántas hay en lista de espera para el mismo bloque, y botones para aprobar o rechazar. Puede agregar un **nuevo espacio** con validaciones y foto, y **cargar el horario** eligiendo un archivo CSV.
- Todo queda guardado en el teléfono con **SQLite (Room)**: reservas, espacios y usuarios. No se pierde al cerrar la app.
- Tema claro y oscuro con los colores del manual de marca de la sede (amarillo Pantone 1235 C y negro).

## Cómo está organizado

Usamos MVVM con repositorios:

- `model/`: `Reserva`, `Espacio` y `Usuario`, que son las tablas de Room, con sus datos de prueba.
- `data/room/`: `AppDatabase` y los DAO de cada tabla.
- `data/repository/`: un repository por tabla, entre el viewmodel y el DAO. `SesionRepository` guarda el usuario conectado con SharedPreferences.
- `ui/viewmodel/ReservasViewModel.kt`: las reservas, los espacios y el usuario que inició sesión, con las funciones para agregar, cancelar, aprobar, rechazar y registrar ingreso y salida.
- `ui/viewmodel/LoginViewModel.kt`: el estado del login con StateFlow y sus validaciones.
- `ui/viewmodel/MenuViewModel.kt`: la foto de perfil y el aviso de cerrar sesión.
- `ui/viewmodel/`: también los de calendario, solicitar reserva y cargar horario.
- `ui/screens/`: las pantallas en Compose.
- `ui/componentes/`: piezas reutilizables, `CampoPassword` (el campo de contraseña), `BarraAdmin` (la franja del administrador) y `CuadroFecha` (el cuadrito con el día y el mes).
- `utils/Fechas.kt`: fechas a partir de hoy, bloques horarios y textos como "Hoy" o "Mañana".
- `utils/Validaciones.kt`: validaciones del login, separadas de la pantalla.
- `utils/Imagenes.kt`: lee las fotos de la galería.
- `navigation/Navigation.kt`: las rutas de la app, algunas con argumentos (espacio, fecha y hora).

## Cómo ejecutarla

1. Clonar el repo:
   ```
   git clone https://github.com/APP-MOVIL-007D/app-reserva-espacios.git
   ```
2. Abrirlo en Android Studio y esperar que sincronice Gradle.
3. Usar un emulador o teléfono con Android 7.0 (API 24) o más.
4. Darle a Run.
5. Entrar con uno de los usuarios de prueba. La contraseña de cada uno está en `model/Usuario.kt`:

| Correo | Perfil |
|---|---|
| `nan.bernal@profesor.duoc.cl` | Docente |
| `agajardo@duoc.cl` | Administrativo |
| `fra.olmos@duoc.cl` | Administrador |

La app viene con 11 usuarios, 15 espacios y 11 reservas de prueba. Las fechas de las reservas se calculan desde el día de hoy, y Nancy tiene una aprobada para hoy para probar registrar ingreso. Para volver a los datos de prueba hay que borrar los datos de la app en el teléfono.

## Documentos

Requisitos, backlog, mockups y manual de marca: [documentos-caso](https://github.com/APP-MOVIL-007D/documentos-caso)
