package com.william.novaChavePix

import com.william.novaChavePix.entidades.ChavePix
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*


@Repository
interface ChavePixRepository : JpaRepository<ChavePix, Long> {
    fun findByIdCliente(idCLiente: String): Optional<ChavePix>
    fun findByValorChave(valor: String): List<ChavePix>
    fun existsByValorChave(valor: String): Boolean
    fun existsByValorChaveAndIdCliente(valorChave: String, idCLiente: String): Boolean
}