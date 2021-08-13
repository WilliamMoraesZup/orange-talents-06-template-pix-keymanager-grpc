package com.william.bcbClient

import com.william.bcbClient.classes.CriarChaveBcbRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client


@Client("\${endereco.client.bcb}")
interface BcbClient {


    @Post(
        "/api/v1/pix/keys",
        consumes = [MediaType.APPLICATION_XML],
        processes = [MediaType.APPLICATION_XML]
    )
    fun registraChavePix(@Body request: CriarChaveBcbRequest): HttpResponse<Map<String, Any>>
}