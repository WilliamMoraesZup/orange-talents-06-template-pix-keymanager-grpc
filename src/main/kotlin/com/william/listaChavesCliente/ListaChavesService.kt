package com.william.listaChavesCliente

import com.william.exceptions.StatusNotFound
import com.william.novaChavePix.ChavePixRepository
import io.micronaut.validation.Validated
import java.util.stream.Collectors
import javax.inject.Singleton
import javax.validation.constraints.NotBlank


@Singleton
@Validated
class ListaChavesService(val repository: ChavePixRepository) {

    fun consulta(@NotBlank clienteId: String): MutableList<ListaChaveRequest>? {

        if (repository.findByIdCliente(clienteId).isEmpty) {
            throw StatusNotFound("Cliente nao encontrado no sistema")

        }
        val findByIdCliente = repository.findChavesByCliente(clienteId)

        val listaChaves = findByIdCliente.stream().map { ListaChaveRequest(it) }.collect(Collectors.toList())
        return listaChaves
    }

}