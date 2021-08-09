package com.william.novaChavePix

import com.william.TipoDaConta
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client


@Client("\${endereco.client.itau}")
interface ItauClient {

    @Get("/api/v1/clientes/{id}/contas")
    fun consultaConta(
        @PathVariable id: String,
        @QueryValue tipo: TipoDaConta
    ): HttpResponse<DadosDaContaResponse>


}