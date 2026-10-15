# Reserva de espacios - Duoc UC Sede Viña del Mar

App Android para reservar salas, laboratorios y otros espacios de la sede. Está pensada para docentes y administrativos: ven los espacios, piden una reserva y revisan en qué estado está.

Ahora las reservas se piden por un formulario de Notion, se anotan en Teamup y el resto se coordina por WhatsApp. Con la app la idea es tener todo en un solo lugar.

Proyecto de DSY1105 Desarrollo de Aplicaciones Móviles, sección 007D (Evaluación Parcial 2).

## Integrantes

- Nicolás Olivares
- Exequiel Guzmán
- Víctor Godoy
- Xavier Marín

## Qué hace la app

- Login solo con correo institucional (`@duoc.cl` o `@profesor.duoc.cl`). Los correos de alumnos (`@duocuc.cl`) no entran.
- Cada campo del login muestra su error con un ícono. La contraseña se pone roja o verde según el largo y dice cuántos caracteres faltan.
- Barra inferior con Inicio, Mis reservas, Espacios, Notificaciones y Menú.
- Inicio muestra la próxima reserva aprobada.
- Espacios tiene la lista de salas con buscador.
- Solicitar reserva: se llena espacio, fecha, bloque y motivo. La reserva queda Pendiente y vuelve a Mis reservas.
- Mis reservas separa Pendientes, Aprobadas e Historial. Se puede cancelar una reserva y antes pide confirmar.
- Las reservas quedan guardadas en el teléfono (DataStore), no se pierden al cerrar la app.
- Tema claro y oscuro con los colores del manual de marca de la sede.

## Cómo está organizado

Usamos MVVM:

- `model/Reserva.kt`: los datos de una reserva.
- `viewmodel/ReservasViewModel.kt`: la lista de reservas y las funciones para agregar, cancelar, aprobar y rechazar. Cada cambio se guarda en DataStore.
- `ui/screens/`: las pantallas en Compose.
- `data/DataStoreManager.kt`: guarda y lee las reservas como JSON con Gson.
- `utils/Validaciones.kt`: validaciones del login, separadas de la pantalla.
- `ui/componentes/CampoPassword.kt`: el campo de contraseña, para reutilizarlo.
- `navigation/Navigation.kt`: las rutas de la app.

## Cómo ejecutarla

1. Clonar el repo:
   ```
   git clone https://github.com/APP-MOVIL-007D/app-reserva-espacios.git
   ```
2. Abrirlo en Android Studio y esperar que sincronice Gradle.
3. Usar un emulador o teléfono con Android 7.0 (API 24) o más.
4. Darle a Run.
5. Entrar con cualquier correo tipo `nombre@duoc.cl` y una contraseña de 6 caracteres o más.

La app viene con 10 reservas de prueba. Para volver a ellas hay que borrar los datos de la app en el teléfono.

## Documentos

Requisitos, backlog, mockups y manual de marca: [documentos-caso](https://github.com/APP-MOVIL-007D/documentos-caso)
