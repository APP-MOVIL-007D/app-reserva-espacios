package cl.duoc.reservaespacios.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "espacios")
data class Espacio(
    @PrimaryKey val nombre : String,
    val capacidad : Int,
    val equipamiento : String,
    val ubicacion: String,
    val estado : String = "Disponible"
)

val espaciosDePrueba = listOf(
    Espacio("Sala S41", 40, "Proyector · Pizarra", "Edificio Norte, piso 4"),
    Espacio("Estudio Sonido 1", 6, "Micrófonos · Consola", "Edificio Norte, piso 2"),
    Espacio("Set TV", 15, "Cámaras · Iluminación", "Edificio Norte, piso 2"),
    Espacio("Laboratorio de Diseño", 20, "Computadores · Tableta gráfica", "Edificio Norte, piso 1"),
    Espacio("Sala S54", 40, "Proyector · Pizarra", "Edificio Central"),
    Espacio("Anfiteatro", 120, "Proyector · Audio · Micrófono", "Edificio Central"),
    Espacio("Salas de Actuación", 25, "Espejos · Audio", "Edificio Sur, piso 5"),
    Espacio("Sala S302", 30, "Pantalla · Pizarra", "Edificio Tecnológico CTI, piso 3"),
    Espacio("Sala S604", 35, "Proyector · Pizarra", "Edificio Tecnológico CTI, piso 6"),
    Espacio("Laboratorio LC5", 24, "Computadores · Proyector", "Edificio Tecnológico CTI, piso 2")
)