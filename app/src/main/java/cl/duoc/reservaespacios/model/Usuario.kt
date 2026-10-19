package cl.duoc.reservaespacios.model

data class Usuario(val correo: String, val nombre : String, val apellido: String, val rol : String)

val usuariosDePrueba = listOf(
    Usuario("nan.bernal@profesor.duoc.cl" , "Nancy", "Bernal", "Docente"),
    Usuario("agajardo@duoc.cl" , "Alejandra", "Gajardo", "Administrativo"),
    Usuario("rob.ertoto@duoc.cl" , "Roberto", "Toto", "Administrativo"),
    Usuario("nic.olivaresz@duoc.cl", "Nicolás", "Olivares", "Administrador"),
    Usuario("fra.olmos@duoc.cl", "Francisco", "Olmos", "Administrador")

)