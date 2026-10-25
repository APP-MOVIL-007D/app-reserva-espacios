package cl.duoc.reservaespacios.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "espacios")
data class Espacio(
    @PrimaryKey val nombre : String,
    val capacidad : Int,
    val equipamiento : String,
    val ubicacion: String,
    val estado : String = "Disponible",
    val foto: String = ""
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
    Espacio("Sala S608", 40, "Mesas · Proyector", "Edificio Tecnológico CTI, piso 6"),
    Espacio("Sala S81", 25, "Proyector · Pizarra", "Edificio Sur, piso 8"),
    Espacio("Laboratorio LC3", 30, "Computadores · Proyector", "Edificio Tecnológico CTI, piso 2"),
    Espacio("Laboratorio LC4", 30, "Computadores · Proyector", "Edificio Tecnológico CTI, piso 2"),
    Espacio("Laboratorio LC5", 30, "Computadores · Proyector", "Edificio Tecnológico CTI, piso 2"),
    Espacio("Laboratorio LC47", 32, "Computadores · Proyector", "Edificio Norte, piso 4"),
)