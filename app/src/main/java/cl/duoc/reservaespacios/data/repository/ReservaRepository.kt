package cl.duoc.reservaespacios.data.repository

import cl.duoc.reservaespacios.data.room.ReservaDao
import cl.duoc.reservaespacios.model.Reserva
import cl.duoc.reservaespacios.model.reservasDePrueba

class ReservaRepository(private val dao: ReservaDao){


    suspend fun cargarReservas(): List<Reserva>{
        if(dao.obtenerTodas().isEmpty()) dao.insertarTodas(reservasDePrueba)
        return dao.obtenerTodas()
    }

    suspend fun agregar(reserva: Reserva){
        dao.insertar(reserva)
    }


    suspend fun actualizar(reserva: Reserva){
        dao.actualizar(reserva)
    }


}