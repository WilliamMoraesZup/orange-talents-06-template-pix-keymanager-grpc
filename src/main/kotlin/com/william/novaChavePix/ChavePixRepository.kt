package com.william.novaChavePix

import com.william.novaChavePix.entidades.ChavePix
import io.micronaut.data.annotation.Query
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*


@Repository
interface ChavePixRepository : JpaRepository<ChavePix, Long> {
    fun findByIdCliente(idCLiente: String): Optional<ChavePix>

    @Query(value = "Select u from ChavePix u WHERE u.idCliente = :idCLiente")
    fun findChavesByCliente( idCLiente: String): List<ChavePix>
    fun findAllByIdCliente( idCLiente: String): List<ChavePix>
    fun findByValorChave(valor: String): List<ChavePix>
    fun existsByValorChave(valor: String): Boolean
    fun existsByValorChaveAndIdCliente(valorChave: String, idCLiente: String): Boolean
}