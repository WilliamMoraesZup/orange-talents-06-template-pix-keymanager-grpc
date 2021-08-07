package com.william.novaChavePix

import com.william.TipoDaConta
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client


@Client("http://localhost:9091/api/v1/")
interface ItauClient {

    @Get("clientes/{id}/contas")
    fun consultaConta(
        @PathVariable id: String,
        @QueryValue tipo: TipoDaConta
    ): HttpResponse<DadosDaContaResponse>


}