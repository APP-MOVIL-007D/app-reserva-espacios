package cl.duoc.reservaespacios.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey val correo: String,
    val nombre : String,
    val apellido: String,
    val rol : String,
    val password: String
)

val usuariosDePrueba = listOf(
    Usuario("nan.bernal@profesor.duoc.cl", "Nancy", "Bernal", "Docente", "nancy2026"),
    Usuario("agajardo@duoc.cl", "Alejandra", "Gajardo", "Administrativo", "alejandra2026"),
    Usuario("rob.ertoto@duoc.cl", "Roberto", "Toto", "Administrativo", "roberto2026"),
    Usuario("nic.olivaresz@duoc.cl", "Nicolás", "Olivares", "Administrador", "nicolas2026"),
    Usuario("fra.olmos@duoc.cl", "Francisco", "Olmos", "Administrador", "francisco2026"),
    Usuario("jor.espinoza@duoc.cl", "Jorge", "Espinoza", "Administrador", "jorge2026")
)