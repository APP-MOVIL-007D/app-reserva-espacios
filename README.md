# Reserva de espacios - Duoc UC Sede Viña del Mar

App Android para reservar salas, laboratorios y otros espacios de la sede. Está pensada para docentes y administrativos: ven los espacios, revisan el calendario de cada uno, piden una reserva y siguen en qué estado está.

Ahora las reservas se piden por un formulario de Notion, se anotan en Teamup y el resto se coordina por WhatsApp. Con la app la idea es tener todo en un solo lugar.

Proyecto de DSY1105 Desarrollo de Aplicaciones Móviles, sección 007D (Evaluación Parcial 2).

## Integrantes

- Nicolás Olivares
- Exequiel Guzmán
- Víctor Godoy
- Xavier Marín

## Qué hace la app

- **Login** solo con correo institucional (`@duoc.cl` o `@profesor.duoc.cl`) y solo para usuarios registrados. Los correos de alumnos (`@duocuc.cl`) no entran.
- Cada campo del login muestra su error con un ícono. La contraseña se pone roja o verde según el largo y dice cuántos caracteres faltan. "¿Olvidaste tu contraseña?" explica a quién contactar.
- **Tres perfiles:** docente, administrativo y administrador. El administrador ve arriba la franja "VISTA ADMINISTRADOR".
- Barra inferior con Inicio, Mis reservas, Espacios, Notificaciones y Menú.
- **Inicio:** saludo con el nombre, las iniciales y el rol del usuario, botón a notificaciones y la próxima reserva aprobada con su fecha.
- **Espacios:** lista de salas con buscador. Cada sala abre su **calendario**, con los bloques del día disponibles, pendientes o reservados.
- **Solicitar reserva:** al tocar un bloque libre del calendario, se abre con el espacio, la fecha y la hora ya puestos. La reserva queda Pendiente y aparece en Mis reservas.
- **Mis reservas:** separa Pendientes, Aprobadas e Historial. Se puede cancelar una reserva y antes pide confirmar.
- **Perfil (Menú):** foto de perfil con la **cámara** o desde la **galería**, con animación al aparecer. Cerrar sesión pide confirmación.
- Las reservas quedan guardadas en el teléfono (DataStore), no se pierden al cerrar la app.
- Tema claro y oscuro con los colores del manual de marca de la sede (amarillo Pantone 1235 C y negro).

## Cómo está organizado

Usamos MVVM:

- `model/Reserva.kt`: los datos de una reserva y las reservas de prueba.
- `model/Usuario.kt`: los usuarios de prueba con su rol.
- `ui/viewmodel/ReservasViewModel.kt`: la lista de reservas, el usuario que inició sesión y las funciones para agregar, cancelar, aprobar y rechazar. Cada cambio se guarda en DataStore.
- `ui/viewmodel/MenuViewModel.kt`: el estado del perfil y el aviso de cerrar sesión.
- `ui/screens/`: las pantallas en Compose.
- `ui/componentes/`: piezas reutilizables, `CampoPassword` (el campo de contraseña) y `BarraAdmin` (la franja del administrador).
- `data/DataStoreManager.kt`: guarda y lee las reservas como JSON con Gson.
- `utils/Validaciones.kt`: validaciones del login, separadas de la pantalla.
- `navigation/Navigation.kt`: las rutas de la app, algunas con argumentos (espacio, fecha y hora).

## Cómo ejecutarla

1. Clonar el repo:
   ```
   git clone https://github.com/APP-MOVIL-007D/app-reserva-espacios.git
   ```
2. Abrirlo en Android Studio y esperar que sincronice Gradle.
3. Usar un emulador o teléfono con Android 7.0 (API 24) o más.
4. Darle a Run.
5. Entrar con uno de los usuarios de prueba y una contraseña de 6 caracteres o más:

| Correo | Perfil |
|---|---|
| `nan.bernal@profesor.duoc.cl` | Docente |
| `agajardo@duoc.cl` | Administrativo |
| `fra.olmos@duoc.cl` | Administrador |

La app viene con 10 reservas de prueba. Para volver a ellas hay que borrar los datos de la app en el teléfono.

## Documentos

Requisitos, backlog, mockups y manual de marca: [documentos-caso](https://github.com/APP-MOVIL-007D/documentos-caso)
