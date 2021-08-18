package com.william.adicionaEremoveNoBcb

import com.william.adicionaEremoveNoBcb.entidades.CriarChaveBcbRequest
import com.william.adicionaEremoveNoBcb.entidades.CriarChaveBcbResponse
import com.william.adicionaEremoveNoBcb.entidades.DeletePixKeyRequest
import com.william.bcbClient.DeletePixKeyResponse
import com.william.consultaChavePix.ConsultaChavePixResponseDTO
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.http.client.annotation.Client
import javax.validation.constraints.NotBlank


@Client("\${endereco.client.bcb}")
interface BancoCentralClient {


    @Post(
        "/api/v1/pix/keys",
        consumes = [MediaType.APPLICATION_XML],
        processes = [MediaType.APPLICATION_XML]
    )
    fun registraChavePix(@Body request: CriarChaveBcbRequest): HttpResponse<CriarChaveBcbResponse>

    @Delete(
        "/api/v1/pix/keys/{key}",
        consumes = [MediaType.APPLICATION_XML],
        processes = [MediaType.APPLICATION_XML]
    )
    fun removeChavePix(@PathVariable key: String, @Body body: DeletePixKeyRequest): HttpResponse<DeletePixKeyResponse>

    @Get(
        "/api/v1/pix/keys/{key}",
        consumes = [MediaType.APPLICATION_XML],
        processes = [MediaType.APPLICATION_XML]
    )
    fun consultaChavePix(@PathVariable @NotBlank key: String): HttpResponse<ConsultaChavePixResponseDTO>
}