package com.william.deletaChavePix

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client

@Client("\${endereco.client.itau}")
interface ItauClienteRemocao {


    @Get("/api/v1/clientes/{clienteId}")
    fun consultaUsuario(
        @QueryValue clienteId: String
    ): HttpResponse<Map<String, Object>>

}