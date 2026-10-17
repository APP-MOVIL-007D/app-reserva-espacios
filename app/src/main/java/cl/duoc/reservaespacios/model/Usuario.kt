package cl.duoc.reservaespacios.model

data class Usuario(val correo: String, val nombre : String, val apellido: String)

val usuariosDePrueba = listOf(
    Usuario("nan.bernal@profesor.duoc.cl" , "Nancy", "Bernal"),
    Usuario("agajardo@duoc.cl" , "Alejandra", "Gajardo"),
    Usuario("rob.ertoto@duoc.cl" , "Roberto", "Toto"),
    Usuario("nic.olivaresz@duoc.cl", "Nicolás", "Olivares")

)

